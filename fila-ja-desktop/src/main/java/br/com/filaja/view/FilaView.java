package br.com.filaja.view;

import br.com.filaja.controller.FilaController;
import br.com.filaja.model.Atendimento;
import br.com.filaja.model.Fila;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class FilaView extends JFrame {

    private final FilaController controller;

    private JComboBox<Fila.ItemCombo> cbPaciente;
    private JComboBox<Fila.ItemCombo> cbUbs;
    private JComboBox<Fila.ItemCombo> cbServico;
    private JComboBox<Fila.ItemCombo> cbAtendente;
    private JComboBox<Fila.ItemCombo> cbProfissional;

    private JTable tabelaFila;
    private DefaultTableModel modeloFila;

    private JTable tabelaRelatorio;
    private DefaultTableModel modeloRelatorio;

    private JTextField txtDataInicio;
    private JTextField txtDataFim;

    public FilaView() {

        controller = new FilaController();

        setTitle("Fila Já - Gerenciamento da Fila");
        setSize(1150, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
        carregarCombos();
        listarFila();
    }

    // INICIALIZAR INTERFACE
    private void inicializarComponentes() {

        JTabbedPane abas = new JTabbedPane();

        abas.addTab("Gerenciamento da Fila", criarPainelFila());
        abas.addTab("Relatório de Atendimentos", criarPainelRelatorio());

        add(abas);
    }

    // PAINEL DA FILA
    private JPanel criarPainelFila() {

        JPanel painel = new JPanel(new BorderLayout(10, 10));

        painel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        JPanel formulario = new JPanel(new GridLayout(5, 2, 10, 10));

        cbPaciente = new JComboBox<>();
        cbUbs = new JComboBox<>();
        cbServico = new JComboBox<>();
        cbAtendente = new JComboBox<>();
        cbProfissional = new JComboBox<>();

        formulario.add(new JLabel("Paciente:"));
        formulario.add(cbPaciente);

        formulario.add(new JLabel("UBS:"));
        formulario.add(cbUbs);

        formulario.add(new JLabel("Serviço:"));
        formulario.add(cbServico);

        formulario.add(new JLabel("Atendente responsável:"));
        formulario.add(cbAtendente);

        formulario.add(new JLabel("Profissional de saúde:"));
        formulario.add(cbProfissional);

        JButton btnCadastrar = new JButton("Cadastrar na fila");
        JButton btnChamar = new JButton("Chamar próximo");
        JButton btnIniciar = new JButton("Iniciar atendimento");
        JButton btnFinalizar = new JButton("Finalizar atendimento");
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnAtualizar = new JButton("Atualizar lista");

        JPanel botoes = new JPanel(new FlowLayout());

        botoes.add(btnCadastrar);
        botoes.add(btnChamar);
        botoes.add(btnIniciar);
        botoes.add(btnFinalizar);
        botoes.add(btnCancelar);
        botoes.add(btnAtualizar);

        modeloFila = new DefaultTableModel(
                new String[]{
                        "ID",
                        "Senha",
                        "Paciente",
                        "UBS",
                        "Serviço",
                        "Status",
                        "Data de entrada"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaFila = new JTable(modeloFila);

        JScrollPane scroll = new JScrollPane(tabelaFila);

        JPanel superior = new JPanel(new BorderLayout(10, 10));

        superior.add(formulario, BorderLayout.CENTER);
        superior.add(botoes, BorderLayout.SOUTH);

        painel.add(superior, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);

        btnCadastrar.addActionListener(e -> cadastrar());
        btnChamar.addActionListener(e -> chamarProximo());
        btnIniciar.addActionListener(e -> iniciarAtendimento());
        btnFinalizar.addActionListener(e -> finalizarAtendimento());
        btnCancelar.addActionListener(e -> cancelar());
        btnAtualizar.addActionListener(e -> listarFila());

        return painel;
    }

    // PAINEL DO RELATÓRIO
    private JPanel criarPainelRelatorio() {

        JPanel painel = new JPanel(new BorderLayout(10, 10));

        painel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT));

        txtDataInicio = new JTextField(
                LocalDate.now().withDayOfMonth(1).toString(), 10
        );

        txtDataFim = new JTextField(
                LocalDate.now().toString(), 10
        );

        JButton btnConsultar = new JButton("Consultar relatório");

        filtros.add(new JLabel("Data inicial:"));
        filtros.add(txtDataInicio);

        filtros.add(new JLabel("Data final:"));
        filtros.add(txtDataFim);

        filtros.add(btnConsultar);

        modeloRelatorio = new DefaultTableModel(
                new String[]{
                        "ID",
                        "Senha",
                        "Paciente",
                        "UBS",
                        "Serviço",
                        "Profissional",
                        "Início",
                        "Fim"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaRelatorio = new JTable(modeloRelatorio);

        JScrollPane scroll = new JScrollPane(tabelaRelatorio);

        painel.add(filtros, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);

        btnConsultar.addActionListener(e -> consultarRelatorio());

        return painel;
    }

    // CARREGAR COMBOS
    private void carregarCombos() {

        try {

            preencherCombo(cbPaciente, controller.listarPacientes());
            preencherCombo(cbUbs, controller.listarUbs());
            preencherCombo(cbServico, controller.listarServicos());
            preencherCombo(cbAtendente, controller.listarAtendentes());
            preencherCombo(cbProfissional, controller.listarProfissionais());

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar opções: " + e.getMessage()
            );
        }
    }

    private void preencherCombo(
            JComboBox<Fila.ItemCombo> combo,
            List<Fila.ItemCombo> itens) {

        combo.removeAllItems();

        for (Fila.ItemCombo item : itens) {
            combo.addItem(item);
        }
    }

    // CADASTRAR NA FILA
    private void cadastrar() {

        Fila.ItemCombo paciente =
                (Fila.ItemCombo) cbPaciente.getSelectedItem();

        Fila.ItemCombo ubs =
                (Fila.ItemCombo) cbUbs.getSelectedItem();

        Fila.ItemCombo servico =
                (Fila.ItemCombo) cbServico.getSelectedItem();

        Fila.ItemCombo atendente =
                (Fila.ItemCombo) cbAtendente.getSelectedItem();

        if (paciente == null || ubs == null
                || servico == null || atendente == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione paciente, UBS, serviço e atendente."
            );

            return;
        }

        try {

            Fila fila = new Fila();

            fila.setIdPaciente(paciente.getId());
            fila.setIdUbs(ubs.getId());
            fila.setIdServico(servico.getId());
            fila.setIdUsuario(atendente.getId());

            controller.cadastrar(fila);

            JOptionPane.showMessageDialog(
                    this,
                    "Paciente cadastrado na fila com sucesso!"
            );

            listarFila();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao cadastrar na fila: " + e.getMessage()
            );
        }
    }

    // LISTAR FILA
    private void listarFila() {

        try {

            modeloFila.setRowCount(0);

            List<Fila> filas = controller.listarTodos();

            for (Fila fila : filas) {

                modeloFila.addRow(new Object[]{
                        fila.getIdFila(),
                        fila.getSenha(),
                        fila.getPaciente(),
                        fila.getUbs(),
                        fila.getServico(),
                        fila.getStatus(),
                        fila.getDataEntrada()
                });
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar fila: " + e.getMessage()
            );
        }
    }

    // CHAMAR PRÓXIMO
    private void chamarProximo() {

        Fila.ItemCombo ubs =
                (Fila.ItemCombo) cbUbs.getSelectedItem();

        Fila.ItemCombo servico =
                (Fila.ItemCombo) cbServico.getSelectedItem();

        if (ubs == null || servico == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma UBS e um serviço."
            );
            return;
        }

        try {

            String mensagem = controller.chamarProximo(
                    ubs.getId(),
                    servico.getId()
            );

            JOptionPane.showMessageDialog(this, mensagem);

            listarFila();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao chamar paciente: " + e.getMessage()
            );
        }
    }

    // INICIAR ATENDIMENTO
    private void iniciarAtendimento() {

        int idFila = getIdSelecionado();

        if (idFila == -1) {
            return;
        }

        Fila.ItemCombo profissional =
                (Fila.ItemCombo) cbProfissional.getSelectedItem();

        if (profissional == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um profissional de saúde."
            );

            return;
        }

        try {

            controller.iniciarAtendimento(
                    idFila,
                    profissional.getId()
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Atendimento iniciado com sucesso!"
            );

            listarFila();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao iniciar atendimento: " + e.getMessage()
            );
        }
    }

    // FINALIZAR ATENDIMENTO
    private void finalizarAtendimento() {

        int idFila = getIdSelecionado();

        if (idFila == -1) {
            return;
        }

        try {

            controller.finalizarAtendimento(idFila);

            JOptionPane.showMessageDialog(
                    this,
                    "Atendimento finalizado com sucesso!"
            );

            listarFila();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao finalizar atendimento: " + e.getMessage()
            );
        }
    }

    // CANCELAR
    private void cancelar() {

        int idFila = getIdSelecionado();

        if (idFila == -1) {
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente cancelar este registro?",
                "Confirmar cancelamento",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            controller.cancelar(idFila);

            JOptionPane.showMessageDialog(
                    this,
                    "Registro cancelado com sucesso!"
            );

            listarFila();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao cancelar registro: " + e.getMessage()
            );
        }
    }

    // OBTER ID DA LINHA SELECIONADA
    private int getIdSelecionado() {

        int linha = tabelaFila.getSelectedRow();

        if (linha == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um registro na tabela."
            );

            return -1;
        }

        return (int) modeloFila.getValueAt(linha, 0);
    }

    // CONSULTAR RELATÓRIO
    private void consultarRelatorio() {

        try {

            LocalDate inicio = LocalDate.parse(
                    txtDataInicio.getText().trim()
            );

            LocalDate fim = LocalDate.parse(
                    txtDataFim.getText().trim()
            );

            if (fim.isBefore(inicio)) {

                JOptionPane.showMessageDialog(
                        this,
                        "A data final não pode ser anterior à inicial."
                );

                return;
            }

            modeloRelatorio.setRowCount(0);

            List<Atendimento> atendimentos =
                    controller.listarAtendimentos(inicio, fim);

            for (Atendimento atendimento : atendimentos) {

                modeloRelatorio.addRow(new Object[]{
                        atendimento.getIdAtendimento(),
                        atendimento.getSenha(),
                        atendimento.getPaciente(),
                        atendimento.getUbs(),
                        atendimento.getServico(),
                        atendimento.getProfissional(),
                        atendimento.getDataInicio(),
                        atendimento.getDataFim()
                });
            }

            if (atendimentos.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Nenhum atendimento encontrado nesse período."
                );
            }

        } catch (java.time.format.DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe as datas no formato AAAA-MM-DD."
            );

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao consultar relatório: " + e.getMessage()
            );
        }
    }
}