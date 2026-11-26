import java.util.ArrayList;

public class Main {

    static ArrayList<Produto> produtos = new ArrayList<>();

    public static Produto buscar(String nome) {

        for (int i = 0; i < produtos.size(); i++) {

            Produto p = produtos.get(i);

            if (p.nome.equalsIgnoreCase(nome.trim())) {
                return p;
            }
        }

        return null;
    }

    public static ArrayList<Produto> pesquisar(String nome) {

        ArrayList<Produto> resultado = new ArrayList<>();

        for (int i = 0; i < produtos.size(); i++) {

            Produto p = produtos.get(i);

            if (p.nome.toLowerCase().contains(nome.toLowerCase().trim())) {

                resultado.add(p);
            }
        }

        return resultado;
    }

    public static boolean cadastrar(String nome, int quantidade, int minimo) {

        if (buscar(nome) != null) {
            return false;
        }

        if (nome.trim().isEmpty() || quantidade < 0
            || quantidade > 10000 || minimo <= 0
            || minimo > 10000) {
            return false;
        }

        Produto novo = new Produto(nome.trim(), quantidade, minimo);

        produtos.add(novo);

        return true;
    }

    public static boolean excluir(String nome) {

        Produto p = buscar(nome);

        if (p != null) {

            produtos.remove(p);
            return true;

        } else {

            return false;
        }
    }

    public static boolean entrada(String nome, int valor) {

        Produto p = buscar(nome);

        if (p != null) {
            return p.entrada(valor);
        }

        return false;
    }

    public static boolean saida(String nome, int valor) {

        Produto p = buscar(nome);

        if (p != null) {
            return p.saida(valor);
        }

        return false;
    }

    public static void main(String[] args) {

        Interface tela = new Interface();

        tela.setVisible(true);
    }
}