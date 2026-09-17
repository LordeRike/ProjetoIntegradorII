import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {
    private Connection conexao;

    public ItemDAO(Connection conexao){
        this.conexao = conexao;
    }

    public void inserir(Item vendaItem) throws SQLException{
        String sql = "INSERT INTO venda_item(id_venda, id_produto, quantidade) VALUES (?, ?, ?);";
        try(PreparedStatement stmt = conexao.prepareStatement(sql)){
            stmt.setInt(1, vendaItem.getId_venda());
            stmt.setInt(2, vendaItem.getId_produto());
            stmt.setInt(3, vendaItem.getQuantidade());
            stmt.executeUpdate();
        }
    }

    public void atualizar(Item vendaItem) throws SQLException {
        String sql = "UPDATE venda_item SET id_venda = ?, id_produto = ?, quantidade = ? WHERE id_venda_item = ?;";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, vendaItem.getId_venda());
            stmt.setInt(2, vendaItem.getId_produto());
            stmt.setInt(3, vendaItem.getQuantidade());
            stmt.setInt(4, vendaItem.getId_venda_item());
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM venda_item WHERE id_venda_item = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Item> listarPorVenda(int id_venda) throws SQLException {
        List<Item> vendaItens = new ArrayList<>();
        String sql = "SELECT * FROM venda_item WHERE id_venda = ? ORDER BY id_venda_item";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id_venda);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Item vi = new Item();
                    vi.setId_venda_item(rs.getInt("id_venda_item"));
                    vi.setId_venda(rs.getInt("id_venda"));
                    vi.setId_produto(rs.getInt("id_produto"));
                    vi.setQuantidade(rs.getInt("quantidade"));
                    vendaItens.add(vi);
                }
            }
        }
        return vendaItens;
    }
}
