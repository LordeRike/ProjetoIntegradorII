import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntradaDAO {
    private Connection conexao;

    public EntradaDAO(Connection conexao){
        this.conexao = conexao;    
    }

    /*public void inserir(Entrada entrada) throws SQLException{
        String sql = "INSERT INTO entrada_produto(fk_id_produto, quantidade_produto) VALUES ( ?, ?);";
        try(PreparedStatement stmt = conexao.prepareStatement(sql)){
            stmt.setInt(1, entrada.getFk_id_produto());
            stmt.setInt(2, entrada.getQtd_entrada_produto());
            stmt.executeUpdate();
        }
    }*/

        /*public void inserir(Entrada entrada) throws SQLException {
            String sql = "WITH nova_entrada AS ( " +
                         "    INSERT INTO entrada_produto (fk_id_produto, quantidade_produto) " +
                         "    VALUES (?, ?) " +
                         "    RETURNING id_entrada, fk_id_produto, quantidade_produto " +
                         ") " +
                         "UPDATE estoque " +
                         "SET quantidade_produto = estoque.quantidade_produto + (SELECT quantidade_produto FROM nova_entrada), " +
                         "    fk_id_entrada = (SELECT id_entrada FROM nova_entrada) " +
                         "WHERE produto_id_produto = (SELECT fk_id_produto FROM nova_entrada);";
        
            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
                stmt.setInt(1, entrada.getFk_id_produto());
                stmt.setInt(2, entrada.getQtd_entrada_produto());
                stmt.executeUpdate();
            }
        }*/

        /*public void inserir(Entrada entrada) throws SQLException {
            String sql = "WITH nova_entrada AS ( " +
                            "    INSERT INTO entrada_produto (fk_id_produto, quantidade_produto) " +
                            "    VALUES (?, ?) " +
                            "    RETURNING id_entrada, fk_id_produto, quantidade_produto " +
                            "), " +
                            "atualiza_estoque AS ( " +
                            "    UPDATE estoque " +
                            "    SET quantidade_produto = estoque.quantidade_produto + (SELECT quantidade_produto FROM nova_entrada), " +
                            "        fk_id_entrada = (SELECT id_entrada FROM nova_entrada) " +
                            "    WHERE produto_id_produto = (SELECT fk_id_produto FROM nova_entrada) " +
                            "    RETURNING id_estoque " + 
                            ") " +
                            "INSERT INTO estoque (produto_id_produto, quantidade_produto) " +
                            "SELECT fk_id_produto, quantidade_produto, id_entrada " +
                            "FROM nova_entrada " +
                            "WHERE NOT EXISTS (SELECT 1 FROM atualiza_estoque);";
        
            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
                
                // Os índices continuam sendo apenas dois!
                stmt.setInt(1, entrada.getFk_id_produto());
                stmt.setInt(2, entrada.getQtd_entrada_produto());
                
                stmt.executeUpdate();
            }
        }*/

    public void inserir(Entrada entrada) throws SQLException {
        String sql = "WITH nova_entrada AS ( " +
                        "    INSERT INTO entrada_produto (fk_id_produto, quantidade_produto) " +
                        "    VALUES (?, ?) " +
                        "    RETURNING id_entrada, fk_id_produto, quantidade_produto " +
                        "), " +
                        "atualiza_estoque AS ( " +
                        "    UPDATE estoque " +
                        "    SET quantidade_produto = estoque.quantidade_produto + (SELECT quantidade_produto FROM nova_entrada), " +
                        "        fk_id_entrada = (SELECT id_entrada FROM nova_entrada) " +
                        "    WHERE produto_id_produto = (SELECT fk_id_produto FROM nova_entrada) " +
                        "    RETURNING id_estoque " +
                        ") " +
                        "INSERT INTO estoque (produto_id_produto, quantidade_produto, fk_id_entrada) " +
                        "SELECT fk_id_produto, quantidade_produto, id_entrada " +
                        "FROM nova_entrada " +
                        "WHERE NOT EXISTS (SELECT 1 FROM atualiza_estoque);";
    
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setInt(1, entrada.getFk_id_produto());
            stmt.setInt(2, entrada.getQtd_entrada_produto());
            
            stmt.executeUpdate();
        }
    }

    public List<Entrada> listarTodos() throws SQLException{
        List<Entrada> entrada = new ArrayList<>();
        String sql = "SELECT fk_id_produto, id_entrada, quantidade_produto FROM entrada_produto;";
                        
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Entrada e = new Entrada();
                e.setId_entrada(rs.getInt("id_entrada"));
                e.setFk_id_produto(rs.getInt("fk_id_produto"));
                e.setQtd_entrada_produto(rs.getInt("quantidade_produto"));
                entrada.add(e);
            }
        }
        return entrada;
    }

    public void atualizar(Entrada entrada) throws SQLException {
        String sql = "UPDATE entrada_produto SET fk_id_produto = ? , quantidade_produto = ? WHERE id_entrada;";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, entrada.getFk_id_produto());
            stmt.setInt(2, entrada.getQtd_entrada_produto());
            stmt.setInt(3, entrada.getId_entrada());
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM entrada_produto WHERE id_entrada = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}