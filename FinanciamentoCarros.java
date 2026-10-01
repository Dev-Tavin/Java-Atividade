import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class FinanciamentoCarros extends JFrame {

    // Taxa de juros aplicada sobre o valor financiado (32%)
    private static final double TAXA = 0.32;

    private JComboBox<String> cbMarca;
    private JTextField txtModelo;
    private JComboBox<Integer> cbAno;
    private JTextField txtValor;
    private JRadioButton rbNovo;
    private JRadioButton rbUsado;
    private JPanel painelUsado;
    private JTextField txtQuilometragem;
    private JTextField txtProprietarios;
    private JCheckBox chkEntrada;
    private JLabel lblEntrada;
    private JTextField txtEntrada;
    private JComboBox<Integer> cbParcelas;
    private JPanel painelResultado;
    private JLabel lblFinanciado;
    private JLabel lblParcela;
    private JLabel lblTotal;

    private final NumberFormat moeda = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));

    public FinanciamentoCarros() {
        super("Financiamento de Carros");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel titulo = new JLabel("Financiamento de Carros", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        conteudo.add(criarPainelVeiculo());
        conteudo.add(criarPainelTipo());
        conteudo.add(criarPainelUsado());
        conteudo.add(criarPainelFinanciamento());
        conteudo.add(criarPainelBotoes());
        conteudo.add(criarPainelResultado());

        setLayout(new BorderLayout());
        add(titulo, BorderLayout.NORTH);
        add(conteudo, BorderLayout.CENTER);

        limpar();
        pack();
        setMinimumSize(new Dimension(450, getHeight()));
        setLocationRelativeTo(null);
    }

    private JPanel criarPainelVeiculo() {
        cbMarca = new JComboBox<>(new String[]{"Fiat", "Volkswagen", "Chevrolet", "Ford", "Toyota", "Honda", "Hyundai", "Renault"});
        txtModelo = new JTextField(15);
        cbAno = new JComboBox<>();
        for (int ano = 2026; ano >= 2000; ano--) {
            cbAno.addItem(ano);
        }
        txtValor = new JTextField(15);

        JPanel painel = criarPainelFormulario("Dados do Veículo");
        adicionarLinha(painel, 0, new JLabel("Marca"), cbMarca);
        adicionarLinha(painel, 1, new JLabel("Modelo"), txtModelo);
        adicionarLinha(painel, 2, new JLabel("Ano"), cbAno);
        adicionarLinha(painel, 3, new JLabel("Valor"), txtValor);
        return painel;
    }

    private JPanel criarPainelTipo() {
        rbNovo = new JRadioButton("Novo");
        rbUsado = new JRadioButton("Usado");
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbNovo);
        grupo.add(rbUsado);

        rbNovo.addActionListener(e -> atualizarPainelUsado());
        rbUsado.addActionListener(e -> atualizarPainelUsado());

        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        painel.add(new JLabel("Tipo"));
        painel.add(rbNovo);
        painel.add(rbUsado);
        return painel;
    }

    private JPanel criarPainelUsado() {
        txtQuilometragem = new JTextField(15);
        txtProprietarios = new JTextField(15);

        painelUsado = criarPainelFormulario("Dados do Veículo Usado");
        adicionarLinha(painelUsado, 0, new JLabel("Quilometragem"), txtQuilometragem);
        adicionarLinha(painelUsado, 1, new JLabel("Proprietários"), txtProprietarios);
        return painelUsado;
    }

    private JPanel criarPainelFinanciamento() {
        chkEntrada = new JCheckBox("Possui entrada?");
        lblEntrada = new JLabel("Entrada");
        txtEntrada = new JTextField(15);
        cbParcelas = new JComboBox<>(new Integer[]{12, 24, 36, 48, 60});

        chkEntrada.addActionListener(e -> atualizarCampoEntrada());

        JPanel painel = criarPainelFormulario("Financiamento");
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(4, 4, 4, 4);
        painel.add(chkEntrada, c);
        adicionarLinha(painel, 1, lblEntrada, txtEntrada);
        adicionarLinha(painel, 2, new JLabel("Parcelas"), cbParcelas);
        return painel;
    }

    private JPanel criarPainelBotoes() {
        JButton btnCalcular = new JButton("Calcular");
        JButton btnLimpar = new JButton("Limpar");
        btnCalcular.addActionListener(e -> calcular());
        btnLimpar.addActionListener(e -> limpar());

        JPanel painel = new JPanel(new GridLayout(1, 2, 15, 0));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        painel.add(btnCalcular);
        painel.add(btnLimpar);
        return painel;
    }

    private JPanel criarPainelResultado() {
        lblFinanciado = new JLabel();
        lblParcela = new JLabel();
        lblTotal = new JLabel();

        painelResultado = new JPanel(new GridLayout(3, 1, 0, 5));
        painelResultado.setBorder(BorderFactory.createTitledBorder("Resultado"));
        painelResultado.add(lblFinanciado);
        painelResultado.add(lblParcela);
        painelResultado.add(lblTotal);
        return painelResultado;
    }

    private JPanel criarPainelFormulario(String titulo) {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createTitledBorder(titulo));
        return painel;
    }

    private void adicionarLinha(JPanel painel, int linha, JComponent rotulo, JComponent campo) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = linha;
        c.insets = new Insets(4, 4, 4, 4);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;
        painel.add(rotulo, c);

        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        painel.add(campo, c);
    }

    private void atualizarPainelUsado() {
        painelUsado.setVisible(rbUsado.isSelected());
        pack();
    }

    private void atualizarCampoEntrada() {
        lblEntrada.setVisible(chkEntrada.isSelected());
        txtEntrada.setVisible(chkEntrada.isSelected());
        pack();
    }

    private void limpar() {
        cbMarca.setSelectedIndex(0);
        txtModelo.setText("");
        cbAno.setSelectedIndex(0);
        txtValor.setText("");
        rbNovo.setSelected(true);
        txtQuilometragem.setText("");
        txtProprietarios.setText("");
        chkEntrada.setSelected(false);
        txtEntrada.setText("");
        cbParcelas.setSelectedItem(36);
        painelResultado.setVisible(false);
        atualizarPainelUsado();
        atualizarCampoEntrada();
    }

    private void calcular() {
        try {
            if (txtModelo.getText().isBlank()) {
                throw new IllegalArgumentException("Informe o modelo do veículo.");
            }

            double valor = lerNumero(txtValor, "Valor");
            if (valor <= 0) {
                throw new IllegalArgumentException("O valor do veículo deve ser maior que zero.");
            }

            if (rbUsado.isSelected()) {
                if (lerNumero(txtQuilometragem, "Quilometragem") < 0) {
                    throw new IllegalArgumentException("A quilometragem não pode ser negativa.");
                }
                double proprietarios = lerNumero(txtProprietarios, "Proprietários");
                if (proprietarios < 1 || proprietarios != Math.floor(proprietarios)) {
                    throw new IllegalArgumentException("Proprietários deve ser um número inteiro maior que zero.");
                }
            }

            double entrada = 0;
            if (chkEntrada.isSelected()) {
                entrada = lerNumero(txtEntrada, "Entrada");
                if (entrada < 0 || entrada >= valor) {
                    throw new IllegalArgumentException("A entrada deve ser maior ou igual a zero e menor que o valor do veículo.");
                }
            }

            int parcelas = (Integer) cbParcelas.getSelectedItem();
            double valorFinanciado = valor - entrada;
            double valorTotal = valorFinanciado * (1 + TAXA);
            double valorParcela = valorTotal / parcelas;

            lblFinanciado.setText("Valor financiado: " + moeda.format(valorFinanciado));
            lblParcela.setText("Valor da parcela: " + moeda.format(valorParcela));
            lblTotal.setText("Total a pagar: " + moeda.format(valorParcela * parcelas));
            painelResultado.setVisible(true);
            pack();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Dados inválidos", JOptionPane.WARNING_MESSAGE);
        }
    }

    private double lerNumero(JTextField campo, String nome) {
        String texto = campo.getText().trim().replace(",", ".");
        if (texto.isEmpty()) {
            throw new IllegalArgumentException("Informe o campo " + nome + ".");
        }
        try {
            return Double.parseDouble(texto);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("O campo " + nome + " deve conter um número válido.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FinanciamentoCarros().setVisible(true));
    }
}
