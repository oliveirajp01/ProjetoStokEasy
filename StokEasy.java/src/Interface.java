
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.util.ArrayList;

public class Interface extends JFrame {

    JTextField nome = new JTextField();
    JTextField quantidade = new JTextField();
    JTextField minimo = new JTextField();

    // Novo campo de pesquisa
    JTextField pesquisa = new JTextField();

    DefaultTableModel modelo = new DefaultTableModel(
        new String[]{"Produto", "Quantidade", "Mínimo", "Situação"}, 0) {

        public boolean isCellEditable(int linha, int coluna) {
            return false;
        }
    };

    JTable tabela = new JTable(modelo);

    public Interface() {

        setTitle("Stock Easy - Controle de estoque");
        setSize(760, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Campos de cadastro

        JPanel campos = new JPanel(new GridLayout(2, 3, 8, 5));

        campos.add(new JLabel("Nome do produto"));
        campos.add(new JLabel("Quantidade"));
        campos.add(new JLabel("Estoque mínimo"));

        campos.add(nome);
        campos.add(quantidade);
        campos.add(minimo);

        // Botões

        JPanel botoes = new JPanel(new GridLayout(1, 4, 8, 5));

        JButton cadastro = new JButton("Cadastrar");
        JButton entrada = new JButton("Entrada");
        JButton saida = new JButton("Saída");
        JButton excluir = new JButton("Excluir");

        botoes.add(cadastro);
        botoes.add(entrada);
        botoes.add(saida);
        botoes.add(excluir);

        // Nova barra de pesquisa

        JPanel painelPesquisa = new JPanel(new BorderLayout(8, 5));

        JButton limpar = new JButton("Limpar");

        painelPesquisa.add(new JLabel("Pesquisar produto:"), BorderLayout.WEST);
        painelPesquisa.add(pesquisa, BorderLayout.CENTER);
        painelPesquisa.add(limpar, BorderLayout.EAST);

        // Organização dos botões e da pesquisa

        JPanel painelInferior = new JPanel(new BorderLayout(8, 8));

        painelInferior.add(botoes, BorderLayout.NORTH);
        painelInferior.add(painelPesquisa, BorderLayout.SOUTH);

        // Organização da parte superior

        JPanel topo = new JPanel(new BorderLayout(8, 8));

        topo.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        topo.add(campos, BorderLayout.CENTER);
        topo.add(painelInferior, BorderLayout.SOUTH);

        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        add(new JLabel(
            "  Vermelho: baixo/zerado | Branco: normal | Verde: alto"
        ), BorderLayout.SOUTH);

        tabela.setRowHeight(28);

        // Cores da tabela

        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(
                JTable t, Object v, boolean selecionado,
                boolean foco, int linha, int coluna) {

                Component c = super.getTableCellRendererComponent(
                    t, v, selecionado, foco, linha, coluna);

                Produto p = Main.buscar(
                    t.getValueAt(linha, 0).toString()
                );

                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);

                if (p != null) {

                    if (p.quantidade < p.minimo) {

                        c.setBackground(new Color(255, 190, 190));

                    } else if (p.quantidade >= p.minimo * 2) {

                        c.setBackground(new Color(190, 240, 195));
                    }
                }

                return c;
            }
        });

        // Ações dos botões

        cadastro.addActionListener(e -> operar(1));
        entrada.addActionListener(e -> operar(2));
        saida.addActionListener(e -> operar(3));
        excluir.addActionListener(e -> operar(4));

        limpar.addActionListener(e -> pesquisa.setText(""));

        // Pesquisa automática

        pesquisa.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                atualizar();
            }

            public void removeUpdate(DocumentEvent e) {
                atualizar();
            }

            public void changedUpdate(DocumentEvent e) {
                atualizar();
            }
        });

        // Seleção de produtos na tabela

        tabela.getSelectionModel().addListSelectionListener(e -> {

            int linha = tabela.getSelectedRow();

            if (!e.getValueIsAdjusting() && linha >= 0) {

                Produto p = Main.buscar(
                    tabela.getValueAt(linha, 0).toString()
                );

                if (p != null) {

                    nome.setText(p.nome);
                    minimo.setText(String.valueOf(p.minimo));
                    quantidade.setText("");
                }
            }
        });

        atualizar();
    }

    // Operações do estoque

    private void operar(int opcao) {

        String n = nome.getText();

        boolean sucesso = false;

        try {

            if (opcao == 1) {

                sucesso = Main.cadastrar(
                    n,
                    Integer.parseInt(quantidade.getText().trim()),
                    Integer.parseInt(minimo.getText().trim())
                );

            } else if (opcao == 4) {

                if (Main.buscar(n) == null) {

                    mensagem("Produto não encontrado.");
                    return;
                }

                if (JOptionPane.showConfirmDialog(
                    this,
                    "Excluir este produto?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
                ) != JOptionPane.YES_OPTION) {

                    return;
                }

                sucesso = Main.excluir(n);

            } else {

                Produto p = Main.buscar(n);

                if (p == null) {

                    mensagem("Produto não encontrado.");
                    return;
                }

                int valor = Integer.parseInt(
                    quantidade.getText().trim()
                );

                if (opcao == 2) {

                    sucesso = Main.entrada(n, valor);

                } else {

                    sucesso = Main.saida(n, valor);
                }
            }

            if (sucesso) {

                atualizar();

                nome.setText("");
                quantidade.setText("");
                minimo.setText("");

            } else {

                mensagem("Operação inválida. Verifique os valores, "
                    + "duplicidade ou estoque disponível.");
            }

        } catch (NumberFormatException e) {

            mensagem("Informe números inteiros válidos.");
        }
    }

    // Atualização da tabela com a pesquisa

    private void atualizar() {

        tabela.clearSelection();

        modelo.setRowCount(0);

        ArrayList<Produto> resultado =
            Main.pesquisar(pesquisa.getText());

        for (int i = 0; i < resultado.size(); i++) {

            Produto p = resultado.get(i);

            modelo.addRow(new Object[]{
                p.nome,
                p.quantidade,
                p.minimo,
                p.situacao()
            });
        }
    }

    // Mensagens para o usuário

    private void mensagem(String texto) {

        JOptionPane.showMessageDialog(this, texto);
    }
}