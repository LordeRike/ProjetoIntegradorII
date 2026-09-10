import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstoqueDAO {
    private Connection conexao;

    public EstoqueDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public List<Estoque> listarTodos() throws SQLException {
        List<Estoque> estoqueList = new ArrayList<>();
        String sql = "SELECT p.id_produto, p.nome_produto, e.quantidade_produto\r\n" + //
                        "\tFROM produto p\r\n" + //
                        "\tINNER JOIN estoque e ON p.id_produto = e.produto_id_produto ORDER BY id_produto\r\n";

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Estoque e = new Estoque();
                //e.setId_estoque(rs.getInt("id_estoque"));
                //e.setProduto_id_produto(rs.getInt("produto_id_produto"));
                e.setId_produto(rs.getInt("id_produto"));
                e.setNome_produto(rs.getString("nome_produto"));
                e.setQuantidade_produto(rs.getInt("quantidade_produto"));
                //e.setFk_id_entrada(rs.getInt("fk_id_entrada"));
                estoqueList.add(e);
            }
        }
        return estoqueList;
    }

}