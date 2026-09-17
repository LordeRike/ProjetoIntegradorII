import java.sql.*;

public class VendaDAO {
    private Connection conexao;

    public VendaDAO(Connection conexao){
        this.conexao = conexao;
    }

    public void inserir(Venda venda) throws SQLException{
        String sql = "INSERT INTO venda(id_cliente, data_venda) VALUES (?, ?);";
        try(PreparedStatement stmt = conexao.prepareStatement(sql)){
            stmt.setInt(1, venda.getId_cliente());
            stmt.setString(2, venda.getData_venda());
            stmt.executeUpdate();
        }
    }

    public void atualizar(Venda venda) throws SQLException {
        String sql = "UPDATE venda SET id_cliente = ?, data_venda = ? WHERE id_venda = ?;";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, venda.getId_cliente());
            stmt.setString(2, venda.getData_venda());
            stmt.setInt(3, venda.getId_venda());
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM venda WHERE id_venda = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Connection getConnection() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getConnection'");
    }


    
}
