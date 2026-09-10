import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class JanelaEstoque extends JFrame {

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private EstoqueDAO estoqueDAO;

    public JanelaEstoque(Connection conexao) {
        super("Estoque de Produtos");
        this.estoqueDAO = new EstoqueDAO(conexao);

        setSize(850, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
        carregarDadosTabela();
    }

    private void inicializarComponentes() {
        String[] colunas = {"ID Produto", "Nome Produto", "Quantidade"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tabela);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void carregarDadosTabela() {
        try {
            List<Estoque> estoqueList = estoqueDAO.listarTodos();
            modeloTabela.setRowCount(0); // Limpa a tabela antes de adicionar os dados
            for (Estoque e : estoqueList) {
                Object[] linha = {e.getId_produto(), e.getNome_produto(), e.getQuantidade_produto()};
                modeloTabela.addRow(linha);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados do estoque:\n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
