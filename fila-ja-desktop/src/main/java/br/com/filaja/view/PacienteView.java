package br.com.filaja.view;

import br.com.filaja.controller.PacienteController;
import br.com.filaja.model.Paciente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

public class PacienteView extends JFrame {

    private final PacienteController controller;

    private JTextField campoNome;
    private JTextField campoCpf;
    private JTextField campoNascimento;
    private JTextField campoTelefone;
    private JTextField campoEmail;

    private JTable tabelaPacientes;
    private DefaultTableModel modeloTabela;

    private JButton botaoCadastrar;
    private JButton botaoEditar;
    private JButton botaoExcluir;

    private Integer pacienteSelecionadoId = null;

    public PacienteView() {

        controller = new PacienteController();

        setTitle("Fila Já - Gerenciamento de Pacientes");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        configurarTela();
        carregarPacientes();
    }

    private void configurarTela() {

        JPanel painelPrincipal = new JPanel(new BorderLayout(15, 15));
        painelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JLabel titulo = new JLabel("Gerenciamento de Pacientes");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));

        painelPrincipal.add(titulo, BorderLayout.NORTH);

        JPanel painelCentral = new JPanel(new BorderLayout(10, 15));

        // Formulário
        JPanel painelFormulario = new JPanel(new GridLayout(5, 2, 10, 10));
        painelFormulario.setBorder(
                BorderFactory.createTitledBorder("Cadastro de Paciente")
        );

        campoNome = new JTextField();
        campoCpf = new JTextField();
        campoNascimento = new JTextField();
        campoTelefone = new JTextField();
        campoEmail = new JTextField();

        painelFormulario.add(new JLabel("Nome:"));
        painelFormulario.add(campoNome);

        painelFormulario.add(new JLabel("CPF:"));
        painelFormulario.add(campoCpf);

        painelFormulario.add(new JLabel("Nascimento (DD/MM/AAAA):"));
        painelFormulario.add(campoNascimento);

        painelFormulario.add(new JLabel("Telefone:"));
        painelFormulario.add(campoTelefone);

        painelFormulario.add(new JLabel("E-mail:"));
        painelFormulario.add(campoEmail);

        // Botões
        botaoCadastrar = new JButton("Cadastrar");
        botaoEditar = new JButton("Salvar Alterações");
        botaoExcluir = new JButton("Excluir");
        JButton botaoAtualizar = new JButton("Atualizar Listagem");

        botaoEditar.setEnabled(false);
        botaoExcluir.setEnabled(false);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));

        painelBotoes.add(botaoCadastrar);
        painelBotoes.add(botaoEditar);
        painelBotoes.add(botaoExcluir);
        painelBotoes.add(botaoAtualizar);

        JPanel painelSuperior = new JPanel(new BorderLayout(10, 10));

        painelSuperior.add(painelFormulario, BorderLayout.CENTER);
        painelSuperior.add(painelBotoes, BorderLayout.SOUTH);

        painelCentral.add(painelSuperior, BorderLayout.NORTH);

        // Tabela
        String[] colunas = {
                "ID", "Nome", "CPF", "Nascimento",
                "Telefone", "E-mail"
        };

        modeloTabela = new DefaultTableModel(colunas, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaPacientes = new JTable(modeloTabela);
        tabelaPacientes.setRowHeight(25);
        tabelaPacientes.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane rolagem = new JScrollPane(tabelaPacientes);

        rolagem.setBorder(
                BorderFactory.createTitledBorder("Pacientes Cadastrados")
        );

        painelCentral.add(rolagem, BorderLayout.CENTER);

        painelPrincipal.add(painelCentral, BorderLayout.CENTER);

        add(painelPrincipal);

        // Eventos dos botões
        botaoCadastrar.addActionListener(e -> cadastrarPaciente());

        botaoEditar.addActionListener(e -> atualizarPaciente());

        botaoExcluir.addActionListener(e -> excluirPaciente());

        botaoAtualizar.addActionListener(e -> carregarPacientes());

        // Seleção de paciente na tabela
        tabelaPacientes.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int linha = tabelaPacientes.getSelectedRow();

                if (linha >= 0) {
                    carregarDadosSelecionados(linha);
                }
            }
        });
    }

    // Cadastrar paciente
    private void cadastrarPaciente() {

        try {

            Paciente paciente = obterDadosFormulario();

            if (paciente == null) {
                return;
            }

            controller.cadastrar(paciente);

            JOptionPane.showMessageDialog(
                    this,
                    "Paciente cadastrado com sucesso!"
            );

            limparCampos();
            carregarPacientes();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao cadastrar paciente: " + e.getMessage()
            );
        }
    }

    // Carregar dados do paciente selecionado
    private void carregarDadosSelecionados(int linha) {

        pacienteSelecionadoId = (Integer) modeloTabela.getValueAt(linha, 0);

        campoNome.setText(
                modeloTabela.getValueAt(linha, 1).toString()
        );

        campoCpf.setText(
                modeloTabela.getValueAt(linha, 2).toString()
        );

        LocalDate dataNascimento = (LocalDate) modeloTabela.getValueAt(linha, 3);

        campoNascimento.setText(
                dataNascimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );

        Object telefone = modeloTabela.getValueAt(linha, 4);
        campoTelefone.setText(telefone == null ? "" : telefone.toString());

        Object email = modeloTabela.getValueAt(linha, 5);
        campoEmail.setText(email == null ? "" : email.toString());

        botaoEditar.setEnabled(true);
        botaoExcluir.setEnabled(true);
    }

    // Atualizar paciente
    private void atualizarPaciente() {

        if (pacienteSelecionadoId == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um paciente na tabela."
            );
            return;
        }

        try {

            Paciente paciente = obterDadosFormulario();

            if (paciente == null) {
                return;
            }

            paciente.setIdPaciente(pacienteSelecionadoId);

            controller.atualizar(paciente);

            JOptionPane.showMessageDialog(
                    this,
                    "Paciente atualizado com sucesso!"
            );

            limparCampos();
            carregarPacientes();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao atualizar paciente: " + e.getMessage()
            );
        }
    }

    // Excluir paciente
    private void excluirPaciente() {

        if (pacienteSelecionadoId == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um paciente na tabela."
            );
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente excluir este paciente?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao == JOptionPane.YES_OPTION) {

            try {

                controller.excluir(pacienteSelecionadoId);

                JOptionPane.showMessageDialog(
                        this,
                        "Paciente excluído com sucesso!"
                );

                limparCampos();
                carregarPacientes();

            } catch (SQLException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível excluir o paciente. "
                                + "Ele pode estar relacionado a outros registros.\n"
                                + e.getMessage()
                );
            }
        }
    }

    // Obter dados do formulário
    private Paciente obterDadosFormulario() {

        String nome = campoNome.getText().trim();
        String cpf = campoCpf.getText().trim();
        String nascimento = campoNascimento.getText().trim();

        if (nome.isEmpty() || cpf.isEmpty() || nascimento.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Preencha nome, CPF e data de nascimento."
            );

            return null;
        }

        LocalDate dataNascimento;

        try {

            DateTimeFormatter formatoData = DateTimeFormatter
                    .ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

            dataNascimento = LocalDate.parse(nascimento, formatoData);

        } catch (DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Data de nascimento inválida!\n"
                            + "Utilize o formato DD/MM/AAAA.\n"
                            + "Exemplo: 07/01/2005."
            );

            return null;
        }

        if (!dataNascimento.isBefore(LocalDate.now())) {

            JOptionPane.showMessageDialog(
                    this,
                    "A data de nascimento deve ser anterior à data atual."
            );

            return null;
        }

        Paciente paciente = new Paciente();

        paciente.setNome(nome);
        paciente.setCpf(cpf);
        paciente.setDataNascimento(dataNascimento);
        paciente.setTelefone(campoTelefone.getText().trim());
        paciente.setEmail(campoEmail.getText().trim());

        return paciente;
    }

    // Listar pacientes
    private void carregarPacientes() {

        try {

            modeloTabela.setRowCount(0);

            List<Paciente> pacientes = controller.listarTodos();

            for (Paciente paciente : pacientes) {

                modeloTabela.addRow(new Object[]{
                        paciente.getIdPaciente(),
                        paciente.getNome(),
                        paciente.getCpf(),
                        paciente.getDataNascimento(),
                        paciente.getTelefone(),
                        paciente.getEmail()
                });
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar pacientes: " + e.getMessage()
            );
        }
    }

    // Limpar formulário
    private void limparCampos() {

        campoNome.setText("");
        campoCpf.setText("");
        campoNascimento.setText("");
        campoTelefone.setText("");
        campoEmail.setText("");

        pacienteSelecionadoId = null;

        tabelaPacientes.clearSelection();

        botaoEditar.setEnabled(false);
        botaoExcluir.setEnabled(false);
    }
}