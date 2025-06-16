package view;

import model.Endereco;
import model.Usuario;
import javax.swing.*;
import java.awt.*;

public class AlterarClienteView extends JDialog {

    // Campos Usuário (Telefone é editável, Nome e CPF apenas display)
    private JTextField nomeField;
    private JTextField cpfField;
    private JTextField telefoneField;

    // Campos Endereço
    private JTextField cepField;
    private JTextField localField; // Rua/Avenida
    private JTextField numeroCasaField;
    private JTextField bairroField;
    private JTextField cidadeField;
    private JTextField estadoField;
    private JTextField complementoField;

    private JButton salvarButton;
    private JButton cancelarButton;

    public AlterarClienteView(Frame owner, Usuario usuario, Endereco endereco) {
        super(owner, "Alterar Dados do Cliente", true);
        setSize(550, 550); // Aumentei um pouco a altura para acomodar melhor
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10,10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5); // Aumentei o espaçamento interno
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int y = 0; // Contador de linha do GridBagLayout

        // --- Dados do Usuário ---
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Nome:"), gbc);

        gbc.gridx = 1; gbc.gridy = y++;
        nomeField = new JTextField(25);
        nomeField.setEditable(false);
        formPanel.add(nomeField, gbc);

        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("CPF:"), gbc);

        gbc.gridx = 1; gbc.gridy = y++;
        cpfField = new JTextField(25);
        cpfField.setEditable(false);
        formPanel.add(cpfField, gbc);

        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Telefone:"), gbc);

        gbc.gridx = 1; gbc.gridy = y++;
        telefoneField = new JTextField(25);
        formPanel.add(telefoneField, gbc);

        // --- Separador Visual ---
        gbc.gridx = 0; gbc.gridy = y++;
        gbc.gridwidth = 2; // Ocupa duas colunas
        gbc.insets = new Insets(10,0,10,0); // Espaçamento vertical para o separador
        formPanel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1; // Volta para uma coluna
        gbc.insets = new Insets(5,5,5,5); // Restaura o espaçamento padrão

        // --- Título para Endereço ---
        gbc.gridx = 0; gbc.gridy = y++;
        gbc.gridwidth = 2;
        JLabel lblEndereco = new JLabel("Endereço");
        lblEndereco.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(lblEndereco, gbc);
        gbc.gridwidth = 1;


        // --- Campos de Endereço ---
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("CEP:"), gbc);

        gbc.gridx = 1; gbc.gridy = y++;
        cepField = new JTextField(25);
        formPanel.add(cepField, gbc);

        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Local (Rua/Av.):"), gbc);

        gbc.gridx = 1; gbc.gridy = y++;
        localField = new JTextField(25);
        formPanel.add(localField, gbc);

        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Número:"), gbc);

        gbc.gridx = 1; gbc.gridy = y++;
        numeroCasaField = new JTextField(25);
        formPanel.add(numeroCasaField, gbc);

        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Bairro:"), gbc);

        gbc.gridx = 1; gbc.gridy = y++;
        bairroField = new JTextField(25);
        formPanel.add(bairroField, gbc);

        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Cidade:"), gbc);

        gbc.gridx = 1; gbc.gridy = y++;
        cidadeField = new JTextField(25);
        formPanel.add(cidadeField, gbc);

        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Estado (UF):"), gbc);

        gbc.gridx = 1; gbc.gridy = y++;
        estadoField = new JTextField(2);
        formPanel.add(estadoField, gbc);

        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Complemento:"), gbc);

        gbc.gridx = 1; gbc.gridy = y++;
        complementoField = new JTextField(25);
        formPanel.add(complementoField, gbc);

        // Carrega os dados do usuário e endereço nos campos
        popularCampos(usuario, endereco);

        // --- Botões de Ação ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        salvarButton = new JButton("Salvar Alterações");
        cancelarButton = new JButton("Cancelar");
        buttonPanel.add(salvarButton);
        buttonPanel.add(cancelarButton);

        // Adiciona o painel de formulário com rolagem e o painel de botões ao JDialog
        add(new JScrollPane(formPanel), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // A configuração do gbc é feita diretamente antes de adicionar cada componente.

    private void popularCampos(Usuario usuario, Endereco endereco) {
        if (usuario != null) {
            nomeField.setText(usuario.getNome() != null ? usuario.getNome() : "");
            cpfField.setText(usuario.getCpf() != null ? usuario.getCpf() : "");
            telefoneField.setText(usuario.getTelefone() != null ? usuario.getTelefone() : "");
        }
        if (endereco != null) {
            cepField.setText(endereco.getCep() != null ? endereco.getCep() : "");
            localField.setText(endereco.getLocal() != null ? endereco.getLocal() : "");
            // Correção para numeroCasa que é int
            numeroCasaField.setText(endereco.getNumeroCasa() > 0 ? String.valueOf(endereco.getNumeroCasa()) : "");
            bairroField.setText(endereco.getBairro() != null ? endereco.getBairro() : "");
            cidadeField.setText(endereco.getCidade() != null ? endereco.getCidade() : "");
            estadoField.setText(endereco.getEstado() != null ? endereco.getEstado() : "");
            complementoField.setText(endereco.getComplemento() != null ? endereco.getComplemento() : "");
        } else { // Se o endereço for nulo, limpa os campos de endereço
            cepField.setText("");
            localField.setText("");
            numeroCasaField.setText("");
            bairroField.setText("");
            cidadeField.setText("");
            estadoField.setText("");
            complementoField.setText("");
        }
    }

    // Getters para os campos editáveis e botões
    public JTextField getTelefoneField() { return telefoneField; }
    public JTextField getCepField() { return cepField; }
    public JTextField getLocalField() { return localField; }
    public JTextField getNumeroCasaField() { return numeroCasaField; }
    public JTextField getBairroField() { return bairroField; }
    public JTextField getCidadeField() { return cidadeField; }
    public JTextField getEstadoField() { return estadoField; }
    public JTextField getComplementoField() { return complementoField; }
    public JButton getSalvarButton() { return salvarButton; }
    public JButton getCancelarButton() { return cancelarButton; }
}