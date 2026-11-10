package br.com.filaja.view;

import br.com.filaja.controller.ServicoController;
import br.com.filaja.model.Servico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ServicoView extends JFrame {

    private final ServicoController controller;

    private JTextField campoNome;
    private JTextField campoDescricao;

    private JCheckBox checkAtivo;

    private JTable tabelaServicos;
    private DefaultTableModel modeloTabela;

    private JButton botaoCadastrar;
    private JButton botaoEditar;
    private JButton botaoExcluir;

    private Integer servicoSelecionadoId = null;

    public ServicoView() {

        controller = new ServicoController();

        setTitle("Fila Já - Gerenciamento de Serviços");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        configurarTela();
        carregarServicos();
    }

    private void configurarTela() {

        JPanel painelPrincipal = new JPanel(new BorderLayout(15, 15));

        painelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        // Título
        JLabel titulo = new JLabel("Gerenciamento de Serviços");

        titulo.setFont(new Font("Arial", Font.BOLD, 24));

        painelPrincipal.add(titulo, BorderLayout.NORTH);

        // Área central
        JPanel painelCentral = new JPanel(new BorderLayout(10, 15));

        // Formulário
        JPanel painelFormulario = new JPanel(
                new GridLayout(3, 2, 10, 10)
        );

        painelFormulario.setBorder(
                BorderFactory.createTitledBorder("Cadastro de Serviço")
        );

        campoNome = new JTextField();
        campoDescricao = new JTextField();

        checkAtivo = new JCheckBox("Serviço ativo", true);

        painelFormulario.add(new JLabel("Nome:"));
        painelFormulario.add(campoNome);

        painelFormulario.add(new JLabel("Descrição:"));
        painelFormulario.add(campoDescricao);

        painelFormulario.add(new JLabel("Situação:"));
        painelFormulario.add(checkAtivo);

        // Botões
        botaoCadastrar = new JButton("Cadastrar");
        botaoEditar = new JButton("Salvar Alterações");
        botaoExcluir = new JButton("Excluir");

        JButton botaoAtualizar = new JButton("Atualizar Listagem");

        botaoEditar.setEnabled(false);
        botaoExcluir.setEnabled(false);

        JPanel painelBotoes = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

        painelBotoes.add(botaoCadastrar);
        painelBotoes.add(botaoEditar);
        painelBotoes.add(botaoExcluir);
        painelBotoes.add(botaoAtualizar);

        // Parte superior
        JPanel painelSuperior = new JPanel(new BorderLayout(10, 10));

        painelSuperior.add(painelFormulario, BorderLayout.CENTER);
        painelSuperior.add(painelBotoes, BorderLayout.SOUTH);

        painelCentral.add(painelSuperior, BorderLayout.NORTH);

        // Tabela
        String[] colunas = {
                "ID",
                "Nome",
                "Descrição",
                "Ativo"
        };

        modeloTabela = new DefaultTableModel(colunas, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaServicos = new JTable(modeloTabela);

        tabelaServicos.setRowHeight(25);

        tabelaServicos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane rolagem = new JScrollPane(tabelaServicos);

        rolagem.setBorder(
                BorderFactory.createTitledBorder(
                        "Serviços Cadastrados"
                )
        );

        painelCentral.add(rolagem, BorderLayout.CENTER);

        painelPrincipal.add(painelCentral, BorderLayout.CENTER);

        add(painelPrincipal);

        // Eventos dos botões
        botaoCadastrar.addActionListener(e -> cadastrarServico());

        botaoEditar.addActionListener(e -> atualizarServico());

        botaoExcluir.addActionListener(e -> excluirServico());

        botaoAtualizar.addActionListener(e -> carregarServicos());

        // Seleção de serviço na tabela
        tabelaServicos.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int linha = tabelaServicos.getSelectedRow();

                if (linha >= 0) {
                    carregarDadosSelecionados(linha);
                }
            }
        });
    }

    // Cadastrar serviço
    private void cadastrarServico() {

        try {

            Servico servico = obterDadosFormulario();

            if (servico == null) {
                return;
            }

            controller.cadastrar(servico);

            JOptionPane.showMessageDialog(
                    this,
                    "Serviço cadastrado com sucesso!"
            );

            limparCampos();
            carregarServicos();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao cadastrar serviço: " + e.getMessage()
            );
        }
    }

    // Carregar dados do serviço selecionado
    private void carregarDadosSelecionados(int linha) {

        servicoSelecionadoId = (Integer)
                modeloTabela.getValueAt(linha, 0);

        campoNome.setText(
                modeloTabela.getValueAt(linha, 1).toString()
        );

        Object descricao = modeloTabela.getValueAt(linha, 2);

        campoDescricao.setText(
                descricao == null ? "" : descricao.toString()
        );

        Object ativo = modeloTabela.getValueAt(linha, 3);

        checkAtivo.setSelected(Boolean.TRUE.equals(ativo));

        botaoEditar.setEnabled(true);
        botaoExcluir.setEnabled(true);
    }

    // Atualizar serviço
    private void atualizarServico() {

        if (servicoSelecionadoId == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um serviço na tabela."
            );

            return;
        }

        try {

            Servico servico = obterDadosFormulario();

            if (servico == null) {
                return;
            }

            servico.setIdServico(servicoSelecionadoId);

            controller.atualizar(servico);

            JOptionPane.showMessageDialog(
                    this,
                    "Serviço atualizado com sucesso!"
            );

            limparCampos();
            carregarServicos();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao atualizar serviço: " + e.getMessage()
            );
        }
    }

    // Excluir serviço
    private void excluirServico() {

        if (servicoSelecionadoId == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um serviço na tabela."
            );

            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente excluir este serviço?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao == JOptionPane.YES_OPTION) {

            try {

                controller.excluir(servicoSelecionadoId);

                JOptionPane.showMessageDialog(
                        this,
                        "Serviço excluído com sucesso!"
                );

                limparCampos();
                carregarServicos();

            } catch (SQLException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível excluir o serviço. "
                                + "Ele pode estar relacionado a outros registros.\n"
                                + e.getMessage()
                );
            }
        }
    }

    // Obter dados do formulário
    private Servico obterDadosFormulario() {

        String nome = campoNome.getText().trim();

        String descricao = campoDescricao.getText().trim();

        if (nome.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "O nome do serviço é obrigatório."
            );

            return null;
        }

        Servico servico = new Servico();

        servico.setNome(nome);

        servico.setDescricao(
                descricao.isEmpty() ? null : descricao
        );

        servico.setAtivo(checkAtivo.isSelected());

        return servico;
    }

    // Listar serviços
    private void carregarServicos() {

        try {

            modeloTabela.setRowCount(0);

            servicoSelecionadoId = null;

            botaoEditar.setEnabled(false);
            botaoExcluir.setEnabled(false);

            List<Servico> servicos = controller.listarTodos();

            for (Servico servico : servicos) {

                modeloTabela.addRow(new Object[]{
                        servico.getIdServico(),
                        servico.getNome(),
                        servico.getDescricao(),
                        servico.isAtivo()
                });
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar serviços: " + e.getMessage()
            );
        }
    }

    // Limpar formulário
    private void limparCampos() {

        campoNome.setText("");
        campoDescricao.setText("");

        checkAtivo.setSelected(true);

        servicoSelecionadoId = null;

        tabelaServicos.clearSelection();

        botaoEditar.setEnabled(false);
        botaoExcluir.setEnabled(false);
    }
}