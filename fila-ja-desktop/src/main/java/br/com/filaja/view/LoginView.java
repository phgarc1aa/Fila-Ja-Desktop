package br.com.filaja.view;

import br.com.filaja.model.Usuario;
import br.com.filaja.controller.LoginController;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginView extends JFrame {

    private final LoginController controller;

    private JTextField txtEmail;
    private JPasswordField txtSenha;

    public LoginView() {

        controller = new LoginController();

        setTitle("Fila Já - Login");
        setSize(420, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painelPrincipal = new JPanel(new BorderLayout(15, 15));
        painelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        );

        JLabel titulo = new JLabel("FILA JÁ", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 30));

        JLabel subtitulo = new JLabel(
                "Acesse sua conta",
                SwingConstants.CENTER
        );

        JPanel painelCabecalho = new JPanel(new GridLayout(2, 1, 5, 5));
        painelCabecalho.add(titulo);
        painelCabecalho.add(subtitulo);

        JPanel painelFormulario = new JPanel(new GridLayout(4, 1, 5, 5));

        JLabel lblEmail = new JLabel("E-mail:");
        txtEmail = new JTextField();

        JLabel lblSenha = new JLabel("Senha:");
        txtSenha = new JPasswordField();

        painelFormulario.add(lblEmail);
        painelFormulario.add(txtEmail);
        painelFormulario.add(lblSenha);
        painelFormulario.add(txtSenha);

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.setFont(new Font("Arial", Font.BOLD, 15));
        btnEntrar.setFocusPainted(false);

        btnEntrar.addActionListener(e -> autenticar());

        JPanel painelRodape = new JPanel(new BorderLayout());
        painelRodape.add(btnEntrar, BorderLayout.CENTER);

        painelPrincipal.add(painelCabecalho, BorderLayout.NORTH);
        painelPrincipal.add(painelFormulario, BorderLayout.CENTER);
        painelPrincipal.add(painelRodape, BorderLayout.SOUTH);

        add(painelPrincipal);

        getRootPane().setDefaultButton(btnEntrar);
    }

    private void autenticar() {

        String email = txtEmail.getText().trim();
        String senha = new String(txtSenha.getPassword());

        if (email.isEmpty() || senha.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Preencha o e-mail e a senha.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            Usuario usuario = controller.autenticar(email, senha);

            if (usuario != null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Bem-vindo(a), " + usuario.getNome() + "!"
                );

                MenuPrincipalView menu = new MenuPrincipalView(usuario);
                menu.setVisible(true);

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "E-mail ou senha inválidos, ou usuário inativo.",
                        "Falha no login",
                        JOptionPane.ERROR_MESSAGE
                );

                txtSenha.setText("");
                txtSenha.requestFocus();
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao realizar login: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}