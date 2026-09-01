import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    private Connection conexao;

    public CategoriaDAO(Connection conexao){
        this.conexao = conexao;
    }

    public void inserir(Categoria categoria) throws SQLException{
        String sql = "INSERT INTO categoria_produto( decricao_categoria_produto) VALUES ( ? );";
        try(PreparedStatement stmt = conexao.prepareStatement(sql)){
            stmt.setString(1, categoria.getDescricao_categoria_produto());
            stmt.executeUpdate();
        }
    }

    public List<Categoria> listarTodos() throws SQLException{
        List<Categoria> categoria = new ArrayList<>();
        String sql = "SELECT * FROM categoria_produto ORDER BY id_categoria_produto;";
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Categoria cat = new Categoria();
                cat.setId_categoria_produto(rs.getInt("id_categoria_produto"));
                cat.setDescricao_categoria_produto(rs.getString("decricao_categoria_produto"));
                categoria.add(cat);
            }
        }
        return categoria;
    }

    public void atualizar(Categoria categoria) throws SQLException {
        String sql = "UPDATE categoria_produto SET descricao_categoria_produto= ? WHERE id_categoria_produto = ?;";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, categoria.getDescricao_categoria_produto());
            stmt.setInt(2, categoria.getId_categoria_produto());
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM categoria_produto WHERE id_categoria_produto = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
}
