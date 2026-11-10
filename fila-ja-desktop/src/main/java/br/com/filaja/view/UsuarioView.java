package br.com.filaja.view;

import br.com.filaja.controller.UsuarioController;
import br.com.filaja.model.GrupoUsuario;
import br.com.filaja.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class UsuarioView extends JFrame {

    private final UsuarioController controller;

    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtEmail;
    private JPasswordField txtSenha;

    private JComboBox<GrupoUsuario> cbGrupo;
    private JCheckBox chkAtivo;

    private JTable tabela;
    private DefaultTableModel modelo;

    public UsuarioView() {

        controller = new UsuarioController();

        setTitle("Fila Já - Usuários");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
        carregarGrupos();
        listarUsuarios();
    }

    private void inicializarComponentes() {

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(
                15, 15, 15, 15
        ));

        JPanel painelFormulario = new JPanel(new GridLayout(6, 2, 10, 10));

        txtId = new JTextField();
        txtId.setEditable(false);

        txtNome = new JTextField();
        txtEmail = new JTextField();
        txtSenha = new JPasswordField();

        cbGrupo = new JComboBox<>();

        chkAtivo = new JCheckBox("Usuário ativo", true);

        painelFormulario.add(new JLabel("ID:"));
        painelFormulario.add(txtId);

        painelFormulario.add(new JLabel("Nome:"));
        painelFormulario.add(txtNome);

        painelFormulario.add(new JLabel("E-mail:"));
        painelFormulario.add(txtEmail);

        painelFormulario.add(new JLabel("Senha:"));
        painelFormulario.add(txtSenha);

        painelFormulario.add(new JLabel("Grupo:"));
        painelFormulario.add(cbGrupo);

        painelFormulario.add(new JLabel("Situação:"));
        painelFormulario.add(chkAtivo);

        JPanel painelBotoes = new JPanel(new FlowLayout());

        JButton btnCadastrar = new JButton("Cadastrar");
        JButton btnBuscar = new JButton("Buscar");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnLimpar = new JButton("Limpar");

        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnBuscar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnLimpar);

        modelo = new DefaultTableModel(
                new String[]{
                        "ID",
                        "Nome",
                        "E-mail",
                        "Grupo",
                        "Ativo"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modelo);

        JScrollPane scroll = new JScrollPane(tabela);

        JPanel painelSuperior = new JPanel(new BorderLayout(10, 10));

        painelSuperior.add(painelFormulario, BorderLayout.CENTER);
        painelSuperior.add(painelBotoes, BorderLayout.SOUTH);

        painelPrincipal.add(painelSuperior, BorderLayout.NORTH);
        painelPrincipal.add(scroll, BorderLayout.CENTER);

        add(painelPrincipal);

        btnCadastrar.addActionListener(e -> cadastrar());
        btnBuscar.addActionListener(e -> buscar());
        btnAtualizar.addActionListener(e -> atualizar());
        btnExcluir.addActionListener(e -> excluir());
        btnLimpar.addActionListener(e -> limparCampos());

        tabela.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                selecionarUsuario();
            }
        });
    }

    // CARREGAR GRUPOS
    private void carregarGrupos() {

        try {

            cbGrupo.removeAllItems();

            List<GrupoUsuario> grupos = controller.listarGrupos();

            for (GrupoUsuario grupo : grupos) {
                cbGrupo.addItem(grupo);
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar grupos: " + e.getMessage()
            );
        }
    }

    // CADASTRAR
    private void cadastrar() {

        try {

            if (txtNome.getText().isBlank()
                    || txtEmail.getText().isBlank()
                    || new String(txtSenha.getPassword()).isBlank()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Preencha nome, e-mail e senha."
                );

                return;
            }

            GrupoUsuario grupo =
                    (GrupoUsuario) cbGrupo.getSelectedItem();

            if (grupo == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Selecione um grupo."
                );
                return;
            }

            Usuario usuario = new Usuario();

            usuario.setNome(txtNome.getText().trim());
            usuario.setEmail(txtEmail.getText().trim());
            usuario.setSenha(new String(txtSenha.getPassword()));
            usuario.setIdGrupo(grupo.getIdGrupo());
            usuario.setAtivo(chkAtivo.isSelected());

            controller.cadastrar(usuario);

            JOptionPane.showMessageDialog(
                    this,
                    "Usuário cadastrado com sucesso!"
            );

            limparCampos();
            listarUsuarios();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao cadastrar usuário: " + e.getMessage()
            );
        }
    }

    // LISTAR
    private void listarUsuarios() {

        try {

            modelo.setRowCount(0);

            List<Usuario> usuarios = controller.listarTodos();

            for (Usuario usuario : usuarios) {

                modelo.addRow(new Object[]{
                        usuario.getIdUsuario(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getNomeGrupo(),
                        usuario.isAtivo() ? "Sim" : "Não"
                });
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao listar usuários: " + e.getMessage()
            );
        }
    }

    // BUSCAR
    private void buscar() {

        String idTexto = JOptionPane.showInputDialog(
                this,
                "Digite o ID do usuário:"
        );

        if (idTexto == null || idTexto.isBlank()) {
            return;
        }

        try {

            int id = Integer.parseInt(idTexto);

            Usuario usuario = controller.buscarPorId(id);

            if (usuario == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Usuário não encontrado."
                );

                return;
            }

            preencherCampos(usuario);

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Digite um ID válido."
            );

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao buscar usuário: " + e.getMessage()
            );
        }
    }

    // SELECIONAR USUÁRIO NA TABELA
    private void selecionarUsuario() {

        int linha = tabela.getSelectedRow();

        if (linha >= 0) {

            int id = (int) modelo.getValueAt(linha, 0);

            try {

                Usuario usuario = controller.buscarPorId(id);

                if (usuario != null) {
                    preencherCampos(usuario);
                }

            } catch (SQLException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Erro ao selecionar usuário: " + e.getMessage()
                );
            }
        }
    }

    // PREENCHER CAMPOS
    private void preencherCampos(Usuario usuario) {

        txtId.setText(String.valueOf(usuario.getIdUsuario()));
        txtNome.setText(usuario.getNome());
        txtEmail.setText(usuario.getEmail());

        // A senha não é recuperada nem exibida.
        txtSenha.setText("");

        for (int i = 0; i < cbGrupo.getItemCount(); i++) {

            GrupoUsuario grupo = cbGrupo.getItemAt(i);

            if (grupo.getIdGrupo() == usuario.getIdGrupo()) {
                cbGrupo.setSelectedIndex(i);
                break;
            }
        }

        chkAtivo.setSelected(usuario.isAtivo());
    }

    // ATUALIZAR
    private void atualizar() {

        try {

            if (txtId.getText().isBlank()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Selecione um usuário para atualizar."
                );

                return;
            }

            if (txtNome.getText().isBlank()
                    || txtEmail.getText().isBlank()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Preencha nome e e-mail."
                );

                return;
            }

            GrupoUsuario grupo =
                    (GrupoUsuario) cbGrupo.getSelectedItem();

            if (grupo == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Selecione um grupo."
                );
                return;
            }

            Usuario usuario = new Usuario();

            usuario.setIdUsuario(Integer.parseInt(txtId.getText()));
            usuario.setNome(txtNome.getText().trim());
            usuario.setEmail(txtEmail.getText().trim());
            usuario.setSenha(new String(txtSenha.getPassword()));
            usuario.setIdGrupo(grupo.getIdGrupo());
            usuario.setAtivo(chkAtivo.isSelected());

            controller.atualizar(usuario);

            JOptionPane.showMessageDialog(
                    this,
                    "Usuário atualizado com sucesso!"
            );

            limparCampos();
            listarUsuarios();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao atualizar usuário: " + e.getMessage()
            );
        }
    }

    // EXCLUIR
    private void excluir() {

        if (txtId.getText().isBlank()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um usuário para excluir."
            );

            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente excluir este usuário?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            controller.excluir(Integer.parseInt(txtId.getText()));

            JOptionPane.showMessageDialog(
                    this,
                    "Usuário excluído com sucesso!"
            );

            limparCampos();
            listarUsuarios();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao excluir usuário: " + e.getMessage()
            );
        }
    }

    // LIMPAR CAMPOS
    private void limparCampos() {

        txtId.setText("");
        txtNome.setText("");
        txtEmail.setText("");
        txtSenha.setText("");

        if (cbGrupo.getItemCount() > 0) {
            cbGrupo.setSelectedIndex(0);
        }

        chkAtivo.setSelected(true);

        tabela.clearSelection();
    }
}