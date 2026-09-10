public class Estoque {
    private int id_estoque;
    private int produto_id_produto; 
    private int quantidade_produto;
    private int fk_id_entrada;

    private int id_produto;
    private String nome_produto;

    public Estoque() {}
    public Estoque(int id_estoque, int produto_id_produto, int quantidade_produto, int fk_id_entrada, int id_produto,
            String nome_produto) {
        this.id_estoque = id_estoque;
        this.produto_id_produto = produto_id_produto;
        this.quantidade_produto = quantidade_produto;
        this.fk_id_entrada = fk_id_entrada;
        this.id_produto = id_produto;
        this.nome_produto = nome_produto;
    }

    public int getId_estoque() {return id_estoque;}
    public void setId_estoque(int id_estoque) {this.id_estoque = id_estoque;}
    public int getProduto_id_produto() {return produto_id_produto;}
    public void setProduto_id_produto(int produto_id_produto) {this.produto_id_produto = produto_id_produto;}
    public int getQuantidade_produto() {return quantidade_produto;}
    public void setQuantidade_produto(int quantidade_produto) {this.quantidade_produto = quantidade_produto;}
    public int getFk_id_entrada() {return fk_id_entrada;}
    public void setFk_id_entrada(int fk_id_entrada) {this.fk_id_entrada = fk_id_entrada;}

    public int getId_produto() {return id_produto;}
    public void setId_produto(int id_produto) {this.id_produto = id_produto;}
    public String getNome_produto() {return nome_produto;}
    public void setNome_produto(String nome_produto) {this.nome_produto = nome_produto;}
}
