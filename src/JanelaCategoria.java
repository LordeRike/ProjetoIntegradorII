import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class JanelaCategoria extends JFrame {
    
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private CategoriaDAO categoriaDAO;

    private JTextField txtId;
    private JTextField txtDescricao;

    private JButton btnSalvar;
    private JButton btnExcluir;
    private JButton btnLimpar;

    public JanelaCategoria(Connection conexao) {
        super("Gerenciamento de Categoria (CRUD)");
        this.categoriaDAO = new CategoriaDAO(conexao);

        setSize(850, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
        carregarDadosTabela();
    }

    private void inicializarComponentes() {
        // --- 1. CONFIGURAÇÃO DA TABELA (ESQUERDA) ---
        String[] colunas = {"ID", "Descrição"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Células não editáveis diretamente no grid
            }
        };

        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Permite selecionar apenas 1 linha por vez
        JScrollPane scrollPane = new JScrollPane(tabela);

        // Listener para preencher os campos de texto ao clicar numa linha da tabela
        tabela.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && tabela.getSelectedRow() != -1) {
                    preencherCamposComLinhaSelecionada();
                }
            }
        });

        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados do categoria"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        painelFormulario.add(new JLabel("ID:"), gbc);
        txtId = new JTextField(5);
        txtId.setEditable(false);
        txtId.setFocusable(false);
        gbc.gridx = 1; gbc.gridy = 0;
        painelFormulario.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        painelFormulario.add(new JLabel("Descricao:"), gbc);
        txtDescricao = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1;
        painelFormulario.add(txtDescricao, gbc);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnSalvar = new JButton("Salvar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Novo / Limpar");

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnLimpar);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        painelFormulario.add(painelBotoes, gbc);

        btnSalvar.addActionListener(e -> salvarCategoria());
        btnExcluir.addActionListener(e -> excluirCategoria());
        btnLimpar.addActionListener(e -> limparCampos());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, painelFormulario);
        splitPane.setDividerLocation(420); // Posição inicial do divisor entre a tabela e o formulário
        splitPane.setResizeWeight(0.5);

        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
        
    }

    public void carregarDadosTabela() {
        modeloTabela.setRowCount(0);
        try {
            List<Categoria> lista = categoriaDAO.listarTodos();
            for (Categoria cat : lista) {
                Object[] linha = {
                    cat.getId_categoria_produto(),
                    cat.getDescricao_categoria_produto(),

                };
                modeloTabela.addRow(linha);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao carregar as categorias cadastradas:\n" + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherCamposComLinhaSelecionada() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada != -1) {
            txtId.setText(modeloTabela.getValueAt(linhaSelecionada, 0).toString());
            txtDescricao.setText(modeloTabela.getValueAt(linhaSelecionada, 1).toString());
        }
    }

    private void salvarCategoria() {
        String descricao = txtDescricao.getText().trim();

        if (descricao.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe a descrição da categoria.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Categoria categoria = new Categoria();
            categoria.setDescricao_categoria_produto(descricao);

            if (txtId.getText().isEmpty()) {
                // Inserir novo categoria (CREATE)
                categoriaDAO.inserir(categoria);
                JOptionPane.showMessageDialog(this, "categoria inserida com sucesso!");
            } else {
                // Atualizar categoria existente (UPDATE)
                categoria.setId_categoria_produto(Integer.parseInt(txtId.getText()));
                categoriaDAO.atualizar(categoria);
                JOptionPane.showMessageDialog(this, "categoria atualizada com sucesso!");
            }

            limparCampos();
            carregarDadosTabela();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar categoria:\n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirCategoria() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria na tabela para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
            this,
            "Tem certeza que deseja excluir esta categoria?",
            "Confirmação de Exclusão",
            JOptionPane.YES_NO_OPTION
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(txtId.getText());
                categoriaDAO.excluir(id);
                JOptionPane.showMessageDialog(this, "Categoria excluída com sucesso!");

                limparCampos();
                carregarDadosTabela();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir categoria:\n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtDescricao.setText("");
        tabela.clearSelection();
    }

}
