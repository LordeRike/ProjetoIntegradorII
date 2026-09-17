public class Item {
    private int id_venda_item;
    private int id_venda;
    private double valor_unitario;
    private int quantidade; 
    private int id_produto;

    public Item() {}
    public Item(int id_venda_item, int id_venda, double valor_unitario, int quantidade, int id_produto) {
        this.id_venda_item = id_venda_item;
        this.id_venda = id_venda;
        this.valor_unitario = valor_unitario;
        this.quantidade = quantidade;
        this.id_produto = id_produto;
    }

    public int getId_venda_item() {return id_venda_item;}
    public void setId_venda_item(int id_venda_item) {this.id_venda_item = id_venda_item;}
    public int getId_venda() {return id_venda;}
    public void setId_venda(int id_venda) {this.id_venda = id_venda;}
    public double getValor_unitario() {return valor_unitario;}
    public void setValor_unitario(double valor_unitario) {this.valor_unitario = valor_unitario;}
    public int getQuantidade() {return quantidade;}
    public void setQuantidade(int quantidade) {this.quantidade = quantidade;}
    public int getId_produto() {return id_produto;}
    public void setId_produto(int id_produto) {this.id_produto = id_produto;}
    
}
