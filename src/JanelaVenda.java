import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;


public class JanelaVenda extends JFrame {
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private VendaDAO VendaDAO;
    private ClienteDAO clienteDAO;

    private JTextField txtIdVenda;
    private JTextField txtIdCliente;

    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtEndereco;
    private JTextField txtCpf;

    // Botões
    private JButton btnVender;

    public JanelaVenda(Connection conexao) {
        super("Gerenciamento de Vendas (CRUD)");
        this.VendaDAO = new VendaDAO(conexao);
        this.clienteDAO = new ClienteDAO(conexao);
        setSize(850, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes(conexao);
        carregarDadosTabela();
    }

    private void inicializarComponentes(Connection conexao) {      
        String[] colunas = {"ID", "Nome", "Endereço", "CPF"};
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

        // --- 2. CONFIGURAÇÃO DO FORMULÁRIO (DIREITA) ---
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados do Cliente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ID (Apenas leitura, pois é gerado/controlado pelo Banco de Dados)
        gbc.gridx = 0; gbc.gridy = 0;
        painelFormulario.add(new JLabel("ID:"), gbc);
        txtId = new JTextField(5);
        txtId.setEditable(false);
        txtId.setFocusable(false);
        gbc.gridx = 1; gbc.gridy = 0;
        painelFormulario.add(txtId, gbc);      

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnVender = new JButton("Iniciar Venda");
        painelBotoes.add(btnVender);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        painelFormulario.add(painelBotoes, gbc);

        btnVender.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirNovaVenda(conexao);
            }
        });

        
        // --- 4. SEPARAÇÃO DA JANELA EM DOIS LADOS (SPLIT PANE) ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, painelFormulario);
        splitPane.setDividerLocation(420); // Posição inicial do divisor entre a tabela e o formulário
        splitPane.setResizeWeight(0.5);

        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);      
    }

    public void carregarDadosTabela() {
        modeloTabela.setRowCount(0);
        try {
            List<Cliente> lista = clienteDAO.listarTodos();
            for (Cliente c : lista) {
                Object[] linha = {
                    c.getIdCliente(),
                    c.getNome(),
                    c.getEndereco(),
                    c.getCpf()
                };
                modeloTabela.addRow(linha);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao carregar os clientes:\n" + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherCamposComLinhaSelecionada() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada != -1) {
            txtId.setText(modeloTabela.getValueAt(linhaSelecionada, 0).toString());
        }
    }

    private void abrirNovaVenda(Connection conexao) {
        SwingUtilities.invokeLater(() -> {
                 JanelaItem telaGrid = new JanelaItem(conexao);
                 telaGrid.setVisible(true);
                });       
    }

    private void limparCampos() {
        txtId.setText("");
        tabela.clearSelection();
    }
}
