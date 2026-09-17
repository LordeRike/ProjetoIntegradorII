public class Venda {
    private int id_venda;
    private int id_cliente;
    private  String data_venda;

    public Venda() {}

    public Venda(int id_venda, int id_cliente, String data_venda) {
        this.id_venda = id_venda;
        this.id_cliente = id_cliente;
        this.data_venda = data_venda;
    }

    public int getId_venda() {return id_venda;}
    public void setId_venda(int id_venda) {this.id_venda = id_venda;}
    public int getId_cliente() {return id_cliente;}
    public void setId_cliente(int id_cliente) {this.id_cliente = id_cliente;}
    public String getData_venda() {return data_venda;}
    public void setData_venda(String data_venda) {this.data_venda = data_venda;}     
}
