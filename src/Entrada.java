public class Entrada {
    private int id_entrada;
    private int fk_id_produto;
    private int qtd_entrada_produto;

    public Entrada() {}
    public Entrada(int id_entrada, int fk_id_produto, int qtd_entrada_produto) {
        this.id_entrada = id_entrada;
        this.fk_id_produto = fk_id_produto;
        this.qtd_entrada_produto = qtd_entrada_produto;
    }
    public int getId_entrada() {return id_entrada;}
    public void setId_entrada(int id_entrada) {this.id_entrada = id_entrada;}
    public int getFk_id_produto() {return fk_id_produto;}
    public void setFk_id_produto(int fk_id_produto) {this.fk_id_produto = fk_id_produto;}
    public int getQtd_entrada_produto() {return qtd_entrada_produto;}
    public void setQtd_entrada_produto(int qtd_entrada_produto) {this.qtd_entrada_produto = qtd_entrada_produto;} 
}
