public class Produto {
    private int id_produto;
    private String nome_produto;
    private int categoria_produto;

    public Produto(){};
    public Produto(int id_produto, String nome_produto, int categoria_produto) {
        this.id_produto = id_produto;
        this.nome_produto = nome_produto;
        this.categoria_produto = categoria_produto;
    }

    public int getId_produto() {return id_produto;}
    public void setId_produto(int id_produto) {this.id_produto = id_produto;}
    public String getNome_produto() {return nome_produto;}
    public void setNome_produto(String nome_produto) {this.nome_produto = nome_produto;}
    public int getCategoria_produto() {return categoria_produto;}
    public void setCategoria_produto(int categoria_produto) {this.categoria_produto = categoria_produto;}

}
