public class Categoria {
    private int id_categoria_produto;
    private String descricao_categoria_produto;

    public Categoria() {}

    public Categoria(int id_categoria_produto, String descricao_categoria_produto) {
        this.id_categoria_produto = id_categoria_produto;
        this.descricao_categoria_produto = descricao_categoria_produto;
    }

    public int getId_categoria_produto() {return id_categoria_produto;}
    public void setId_categoria_produto(int id_categoria_produto) {this.id_categoria_produto = id_categoria_produto;}
    public String getDescricao_categoria_produto() {return descricao_categoria_produto;}
    public void setDescricao_categoria_produto(String descricao_categoria_produto) {this.descricao_categoria_produto = descricao_categoria_produto;}

}