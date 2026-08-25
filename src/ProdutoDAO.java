import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    private Connection conexao;

    public ProdutoDAO(Connection conexao){
        this.conexao = conexao;
    }

    public void inserir(Produto produto) throws SQLException{
        String sql = "INSERT INTO produto(nome_produto, categoria_produto_id_categoria_produto) VALUES (?, ?);";
        try(PreparedStatement stmt = conexao.prepareStatement(sql)){
            stmt.setString(1, produto.getNome_produto());
            stmt.setInt(1, produto.getCategoria_produto());
            stmt.executeUpdate();
        }
    }

    public List<Produto> listarTodos() throws SQLException{
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produto ORDER BY id_produto";
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Produto p = new Produto();
                p.setId_produto(rs.getInt("id_produto"));
                p.setNome_produto(rs.getString("nome_produto"));
                p.setCategoria_produto(rs.getInt("categoria_produto"));
                produtos.add(p);
            }
        }
        return produtos;
    }

    public void atualizar(Produto produto) throws SQLException {
        String sql = "UPDATE produto SET nome_produto = ?, categoria_produto_id_categoria_produto=? WHERE id_produto = ?;";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome_produto());
            stmt.setInt(2, produto.getCategoria_produto());
            stmt.setInt(3, produto.getId_produto());
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM produto WHERE id_produto = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
