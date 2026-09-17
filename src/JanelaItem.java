import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 * JanelaItem
 */
public class JanelaItem extends JFrame {
    private ItemDAO itemDAO;
    private ProdutoDAO produtoDAO;

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private JTextField txtIdP;
    private JTextField txtNomeP;
    private JTextField txtQtdP;

    private JButton btnSalvar;
    private JButton btnExcluir;
    private JButton btnLimpar;

    public JanelaItem(Connection conexao) {
        super("Gerenciamento de Itens da Venda (CRUD)");
        this.itemDAO = new ItemDAO(conexao);
        this.produtoDAO = new ProdutoDAO(conexao);
        setSize(850, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
        carregarDadosTabela();
    }   

    private void inicializarComponentes() {
        String[] colunas = {"ID", "Nome", "ID Categoria", "Descrição Categoria"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tabela);

        tabela.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && tabela.getSelectedRow() != -1) {
                    preencherCamposComLinhaSelecionada();
                }
            }
        });

        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados do produto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        painelFormulario.add(new JLabel("ID:"), gbc);
        txtIdP = new JTextField(5);
        txtIdP.setEditable(false);
        txtIdP.setFocusable(false);
        gbc.gridx = 1; gbc.gridy = 0;
        painelFormulario.add(txtIdP, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        painelFormulario.add(new JLabel("Nome:"), gbc);
        txtNomeP = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1;
        painelFormulario.add(txtNomeP, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        painelFormulario.add(new JLabel("Quantidade:"), gbc);
        txtQtdP = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 2;
        painelFormulario.add(txtQtdP, gbc);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnSalvar = new JButton("Salvar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Novo / Limpar");

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnLimpar);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        painelFormulario.add(painelBotoes, gbc);

        btnSalvar.addActionListener(e -> salvarItemVenda());
        /*btnExcluir.addActionListener(e -> excluirProduto());
        btnLimpar.addActionListener(e -> limparCampos());*/

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, painelFormulario);
        splitPane.setDividerLocation(420);
        splitPane.setResizeWeight(0.5);

        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
    }

    public void carregarDadosTabela() {
        modeloTabela.setRowCount(0);
        try {
            List<Produto> lista = produtoDAO.listarTodos();
            for (Produto p : lista) {
                Object[] linha = {
                    p.getId_produto(),
                    p.getNome_produto(),
                    p.getCategoria_produto(),
                    p.getDescricao_categoria()
                };
                modeloTabela.addRow(linha);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao carregar os produto:\n" + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherCamposComLinhaSelecionada() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada != -1) {
            txtIdP.setText(modeloTabela.getValueAt(linhaSelecionada, 0).toString());
            txtNomeP.setText(modeloTabela.getValueAt(linhaSelecionada, 1).toString());
        }
    }

    private void salvarItemVenda() {
        String qtd = txtQtdP.getText().trim();

        try {
            Item vendaItem = new Item();
            int qtd2 = Integer.parseInt(txtQtdP.getText());
            vendaItem.setId_produto(qtd2);

            if (txtQtdP.getText().isEmpty()) {
                // Inserir novo produto (CREATE)
                itemDAO.inserir(vendaItem);
                JOptionPane.showMessageDialog(this, "produto inserido com sucesso!");
            } else {
                // Atualizar produto existente (UPDATE)
                vendaItem.setId_produto(Integer.parseInt(txtIdP.getText()));
                itemDAO.atualizar(vendaItem);
                JOptionPane.showMessageDialog(this, "produto atualizado com sucesso!");
            }

            limparCampos();
            carregarDadosTabela();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar produto:\n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        
        }
    }

    private void limparCampos() {
        txtIdP.setText("");
        txtNomeP.setText("");
        txtQtdP.setText("");
        tabela.clearSelection();
    }

}
