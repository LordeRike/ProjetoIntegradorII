import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class JanelaProduto extends JFrame {
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private ProdutoDAO produtoDAO;

    private JTextField txtIdP;
    private JTextField txtNomeP;
    private JTextField txtCatP;

    private JButton btnSalvar;
    private JButton btnExcluir;
    private JButton btnLimpar;

    public JanelaProduto(Connection conexao) {
        super("Gerenciamento de Produtos (CRUD)");
        this.produtoDAO = new ProdutoDAO(conexao);

        setSize(850, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
        carregarDadosTabela();
    }

    private void inicializarComponentes() {
        String[] colunas = {"ID", "Nome", "Categoria"};
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
        painelFormulario.add(new JLabel("Categoria:"), gbc);
        txtCatP = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 2;
        painelFormulario.add(txtCatP, gbc);

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

        btnSalvar.addActionListener(e -> salvarProduto());
        btnExcluir.addActionListener(e -> excluirProduto());
        btnLimpar.addActionListener(e -> limparCampos());

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
                    p.getCategoria_produto()
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
            txtCatP.setText(modeloTabela.getValueAt(linhaSelecionada, 2).toString());
        }
    }

    private void salvarProduto() {
        String nomeP = txtNomeP.getText().trim();
        String catP = txtCatP.getText().trim();

        if (nomeP.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome do produto.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Produto produto = new Produto();
            int catP2 = Integer.parseInt(txtCatP.getText());
            produto.setNome_produto(nomeP);
            produto.setCategoria_produto(catP2);;

            if (txtIdP.getText().isEmpty()) {
                // Inserir novo produto (CREATE)
                produtoDAO.inserir(produto);
                JOptionPane.showMessageDialog(this, "produto inserido com sucesso!");
            } else {
                // Atualizar produto existente (UPDATE)
                produto.setId_produto(Integer.parseInt(txtIdP.getText()));
                produtoDAO.atualizar(produto);
                JOptionPane.showMessageDialog(this, "produto atualizado com sucesso!");
            }

            limparCampos();
            carregarDadosTabela();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar produto:\n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        
        }
    }

    private void excluirProduto() {
        if (txtIdP.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
            this,
            "Tem certeza que deseja excluir este produto?",
            "Confirmação de Exclusão",
            JOptionPane.YES_NO_OPTION
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(txtIdP.getText());
                produtoDAO.excluir(id);
                JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");

                limparCampos();
                carregarDadosTabela();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir produto:\n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limparCampos() {
        txtIdP.setText("");
        txtNomeP.setText("");
        txtCatP.setText("");
        tabela.clearSelection();
    }
}
