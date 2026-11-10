package br.com.filaja.view;

import br.com.filaja.controller.UBSController;
import br.com.filaja.model.UBS;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class UBSView extends JFrame {

    private final UBSController controller;

    private JTextField campoNome;
    private JTextField campoEndereco;
    private JTextField campoTelefone;

    private JCheckBox checkAtivo;

    private JTable tabelaUBS;
    private DefaultTableModel modeloTabela;

    private JButton botaoCadastrar;
    private JButton botaoEditar;
    private JButton botaoExcluir;

    private Integer ubsSelecionadaId = null;

    public UBSView() {

        controller = new UBSController();

        setTitle("Fila Já - Gerenciamento de UBS");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        configurarTela();
        carregarUBS();
    }

    private void configurarTela() {

        JPanel painelPrincipal = new JPanel(new BorderLayout(15, 15));

        painelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        // Título
        JLabel titulo = new JLabel("Gerenciamento de UBS");

        titulo.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        painelPrincipal.add(titulo, BorderLayout.NORTH);

        // Área central
        JPanel painelCentral = new JPanel(
                new BorderLayout(10, 15)
        );

        // Formulário
        JPanel painelFormulario = new JPanel(
                new GridLayout(4, 2, 10, 10)
        );

        painelFormulario.setBorder(
                BorderFactory.createTitledBorder(
                        "Cadastro de Unidade Básica de Saúde"
                )
        );

        campoNome = new JTextField();
        campoEndereco = new JTextField();
        campoTelefone = new JTextField();

        checkAtivo = new JCheckBox("Unidade ativa", true);

        painelFormulario.add(new JLabel("Nome:"));
        painelFormulario.add(campoNome);

        painelFormulario.add(new JLabel("Endereço:"));
        painelFormulario.add(campoEndereco);

        painelFormulario.add(new JLabel("Telefone:"));
        painelFormulario.add(campoTelefone);

        painelFormulario.add(new JLabel("Situação:"));
        painelFormulario.add(checkAtivo);

        // Botões
        botaoCadastrar = new JButton("Cadastrar");

        botaoEditar = new JButton("Salvar Alterações");

        botaoExcluir = new JButton("Excluir");

        JButton botaoAtualizar = new JButton(
                "Atualizar Listagem"
        );

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
        JPanel painelSuperior = new JPanel(
                new BorderLayout(10, 10)
        );

        painelSuperior.add(
                painelFormulario,
                BorderLayout.CENTER
        );

        painelSuperior.add(
                painelBotoes,
                BorderLayout.SOUTH
        );

        painelCentral.add(
                painelSuperior,
                BorderLayout.NORTH
        );

        // Tabela
        String[] colunas = {
                "ID",
                "Nome",
                "Endereço",
                "Telefone",
                "Ativa"
        };

        modeloTabela = new DefaultTableModel(
                colunas,
                0
        ) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {

                return false;
            }
        };

        tabelaUBS = new JTable(modeloTabela);

        tabelaUBS.setRowHeight(25);

        tabelaUBS.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane rolagem = new JScrollPane(
                tabelaUBS
        );

        rolagem.setBorder(
                BorderFactory.createTitledBorder(
                        "Unidades Básicas de Saúde Cadastradas"
                )
        );

        painelCentral.add(
                rolagem,
                BorderLayout.CENTER
        );

        painelPrincipal.add(
                painelCentral,
                BorderLayout.CENTER
        );

        add(painelPrincipal);

        // Eventos dos botões
        botaoCadastrar.addActionListener(
                e -> cadastrarUBS()
        );

        botaoEditar.addActionListener(
                e -> atualizarUBS()
        );

        botaoExcluir.addActionListener(
                e -> excluirUBS()
        );

        botaoAtualizar.addActionListener(
                e -> carregarUBS()
        );

        // Seleção da UBS na tabela
        tabelaUBS.getSelectionModel().addListSelectionListener(
                e -> {

                    if (!e.getValueIsAdjusting()) {

                        int linha =
                                tabelaUBS.getSelectedRow();

                        if (linha >= 0) {

                            carregarDadosSelecionados(linha);
                        }
                    }
                }
        );
    }

    // Cadastrar UBS
    private void cadastrarUBS() {

        try {

            UBS ubs = obterDadosFormulario();

            if (ubs == null) {
                return;
            }

            controller.cadastrar(ubs);

            JOptionPane.showMessageDialog(
                    this,
                    "UBS cadastrada com sucesso!"
            );

            limparCampos();

            carregarUBS();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao cadastrar UBS: "
                            + e.getMessage()
            );
        }
    }

    // Carregar dados da UBS selecionada
    private void carregarDadosSelecionados(
            int linha
    ) {

        ubsSelecionadaId = (Integer)
                modeloTabela.getValueAt(linha, 0);

        campoNome.setText(
                modeloTabela.getValueAt(
                        linha,
                        1
                ).toString()
        );

        campoEndereco.setText(
                modeloTabela.getValueAt(
                        linha,
                        2
                ).toString()
        );

        Object telefone =
                modeloTabela.getValueAt(
                        linha,
                        3
                );

        campoTelefone.setText(
                telefone == null
                        ? ""
                        : telefone.toString()
        );

        Object ativo =
                modeloTabela.getValueAt(
                        linha,
                        4
                );

        checkAtivo.setSelected(
                Boolean.TRUE.equals(ativo)
        );

        botaoEditar.setEnabled(true);

        botaoExcluir.setEnabled(true);
    }

    // Atualizar UBS
    private void atualizarUBS() {

        if (ubsSelecionadaId == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma UBS na tabela."
            );

            return;
        }

        try {

            UBS ubs = obterDadosFormulario();

            if (ubs == null) {
                return;
            }

            ubs.setIdUbs(
                    ubsSelecionadaId
            );

            controller.atualizar(ubs);

            JOptionPane.showMessageDialog(
                    this,
                    "UBS atualizada com sucesso!"
            );

            limparCampos();

            carregarUBS();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao atualizar UBS: "
                            + e.getMessage()
            );
        }
    }

    // Excluir UBS
    private void excluirUBS() {

        if (ubsSelecionadaId == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma UBS na tabela."
            );

            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente excluir esta UBS?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao == JOptionPane.YES_OPTION) {

            try {

                controller.excluir(
                        ubsSelecionadaId
                );

                JOptionPane.showMessageDialog(
                        this,
                        "UBS excluída com sucesso!"
                );

                limparCampos();

                carregarUBS();

            } catch (SQLException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível excluir a UBS. "
                                + "Ela pode estar relacionada "
                                + "a outros registros.\n"
                                + e.getMessage()
                );
            }
        }
    }

    // Obter dados do formulário
    private UBS obterDadosFormulario() {

        String nome =
                campoNome.getText().trim();

        String endereco =
                campoEndereco.getText().trim();

        String telefone =
                campoTelefone.getText().trim();

        if (nome.isEmpty()
                || endereco.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Preencha o nome e o endereço."
            );

            return null;
        }

        UBS ubs = new UBS();

        ubs.setNome(nome);

        ubs.setEndereco(endereco);

        ubs.setTelefone(
                telefone.isEmpty()
                        ? null
                        : telefone
        );

        ubs.setAtivo(
                checkAtivo.isSelected()
        );

        return ubs;
    }

    // Listar UBS
    private void carregarUBS() {

        try {

            modeloTabela.setRowCount(0);

            ubsSelecionadaId = null;

            botaoEditar.setEnabled(false);

            botaoExcluir.setEnabled(false);

            List<UBS> unidades =
                    controller.listarTodos();

            for (UBS ubs : unidades) {

                modeloTabela.addRow(
                        new Object[]{
                                ubs.getIdUbs(),
                                ubs.getNome(),
                                ubs.getEndereco(),
                                ubs.getTelefone(),
                                ubs.isAtivo()
                        }
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar UBS: "
                            + e.getMessage()
            );
        }
    }

    // Limpar formulário
    private void limparCampos() {

        campoNome.setText("");

        campoEndereco.setText("");

        campoTelefone.setText("");

        checkAtivo.setSelected(true);

        ubsSelecionadaId = null;

        tabelaUBS.clearSelection();

        botaoEditar.setEnabled(false);

        botaoExcluir.setEnabled(false);
    }
}