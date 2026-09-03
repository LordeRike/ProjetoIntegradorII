import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class JanelaEntrada extends JFrame {
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private EntradaDAO entradaDAO;

    private JTextField txtId_entrada_produto;
    private JTextField txtFk_id_produto;
    private JTextField txtQtd_entrada;

    private JButton btnSalvar;
    private JButton btnExcluir;
    private JButton btnLimpar;

    public JanelaEntrada(Connection conexao){
        super("Entrada de Produtos");
        this.entradaDAO = new EntradaDAO(conexao);

        setSize(850, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
        carregarDadosTabela();
    }

    private void inicializarComponentes() {
        String[] colunas = {"ID Entrada", "ID Produto", "Quantidade"};
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
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Entrada produto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        painelFormulario.add(new JLabel("ID:"), gbc);
        txtId_entrada_produto = new JTextField(5);
        txtId_entrada_produto.setEditable(false);
        txtId_entrada_produto.setFocusable(false);
        gbc.gridx = 1; gbc.gridy = 0;
        painelFormulario.add(txtId_entrada_produto, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        painelFormulario.add(new JLabel("ID produto"), gbc);
        txtFk_id_produto = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1;
        painelFormulario.add(txtFk_id_produto, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        painelFormulario.add(new JLabel("Quantidade"), gbc);
        txtQtd_entrada = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 2;
        painelFormulario.add(txtQtd_entrada, gbc);

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

        btnSalvar.addActionListener(e -> salvarEntrada());
        btnExcluir.addActionListener(e -> excluirEntrada());
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
            List<Entrada> lista = entradaDAO.listarTodos();
            for (Entrada e : lista) {
                Object[] linha = {
                    e.getId_entrada(),
                    e.getFk_id_produto(),
                    e.getQtd_entrada_produto()
                };
                modeloTabela.addRow(linha);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao carregar:\n" + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherCamposComLinhaSelecionada() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada != -1) {
            txtId_entrada_produto.setText(modeloTabela.getValueAt(linhaSelecionada, 0).toString());
            txtFk_id_produto.setText(modeloTabela.getValueAt(linhaSelecionada, 1).toString());
            txtQtd_entrada.setText(modeloTabela.getValueAt(linhaSelecionada, 2).toString());
        }
    }

    private void salvarEntrada() {
        String qtd = txtQtd_entrada.getText().trim();
        String idFkP = txtQtd_entrada.getText().trim();

        if (idFkP.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o id do produto.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Entrada entrada = new Entrada();
            int qtdP = Integer.parseInt(txtQtd_entrada.getText());
            int idFkP2 = Integer.parseInt(txtFk_id_produto.getText());
            entrada.setFk_id_produto(idFkP2);
            entrada.setQtd_entrada_produto(qtdP);;

            if (txtId_entrada_produto.getText().isEmpty()) {
                // Inserir novo produto (CREATE)
                entradaDAO.inserir(entrada);
                JOptionPane.showMessageDialog(this, "entrada cadastrada com sucesso!");
            } else {
                // Atualizar produto existente (UPDATE)
                entrada.setId_entrada(Integer.parseInt(txtId_entrada_produto.getText()));
                entradaDAO.atualizar(entrada);
                JOptionPane.showMessageDialog(this, "entrada atualizado com sucesso!");
            }

            limparCampos();
            carregarDadosTabela();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar produto:\n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        
        }
    }

    private void excluirEntrada() {
        if (txtId_entrada_produto.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione uma entrada na tabela para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
            this,
            "Tem certeza que deseja excluir esta entrada?",
            "Confirmação de Exclusão",
            JOptionPane.YES_NO_OPTION
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(txtId_entrada_produto.getText());
                entradaDAO.excluir(id);
                JOptionPane.showMessageDialog(this, "Entrada excluído com sucesso!");

                limparCampos();
                carregarDadosTabela();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir entrada:\n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limparCampos() {
        txtId_entrada_produto.setText("");
        txtFk_id_produto.setText("");
        txtQtd_entrada.setText("");
        tabela.clearSelection();
    }


}
