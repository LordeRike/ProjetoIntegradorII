public class Cliente {
    private int idCliente;
    private String nome;
    private String endereco;
    private Integer cpf;

    // Construtores
    public Cliente() {}
    public Cliente(int idCliente, String nome, String endereco, Integer cpf) {
        this.idCliente = idCliente;
        this.nome = nome;
        this.endereco = endereco;
        this.cpf = cpf;
    }

    // Getters e Setters
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public Integer getCpf() {return cpf; }
    public void setCpf(Integer cpf) { this.cpf = cpf; }
    
}