public class Produto {
    private int id_produto;
    private String nome_produto;
    private int categoria_produto;

    private String descricao_categoria;
    private int id_categoria;

    public Produto(){};
    public Produto(int id_produto, String nome_produto, int categoria_produto) {
        this.id_produto = id_produto;
        this.nome_produto = nome_produto;
        this.categoria_produto = categoria_produto;
    }

    

    public Produto(String descricao_categoria, int id_categoria) {
        this.descricao_categoria = descricao_categoria;
        this.id_categoria = id_categoria;
    }
    public int getId_produto() {return id_produto;}
    public void setId_produto(int id_produto) {this.id_produto = id_produto;}
    public String getNome_produto() {return nome_produto;}
    public void setNome_produto(String nome_produto) {this.nome_produto = nome_produto;}
    public int getCategoria_produto() {return categoria_produto;}
    public void setCategoria_produto(int categoria_produto) {this.categoria_produto = categoria_produto;}

    public String getDescricao_categoria() {return descricao_categoria;}
    public void setDescricao_categoria(String descricao_categoria) {this.descricao_categoria = descricao_categoria;}
    public int getId_categoria() {return id_categoria;}
    public void setId_categoria(int id_categoria) {this.id_categoria = id_categoria;}

    
}
