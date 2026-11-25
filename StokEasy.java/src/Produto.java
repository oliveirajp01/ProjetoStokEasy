public class Produto {
    
    String nome;
    int quantidade;
    int minimo;

    public Produto(String nome, int quantidade, int minimo) {

        this.nome = nome;
        this.quantidade = quantidade;
        this.minimo = minimo;
    }

    public boolean entrada(int valor) {

        if (valor > 0 || valor <= 10000 - quantidade) {

            quantidade = quantidade + valor;
            return true;

        } else {

            return false;
        }
    }

    public boolean saida(int valor) {

        if (valor > 0 || valor <= quantidade) {

            quantidade = quantidade - valor;
            return true;

        } else {

            return false;
        }
    }

    public String situacao() {

        if (quantidade == 0) {

            return "Sem estoque";

        } else if (quantidade < minimo) {

            return "Estoque baixo";

        } else if (quantidade >= minimo * 2) {

            return "Estoque alto";

        } else {

            return "Normal";
        }
    }
}