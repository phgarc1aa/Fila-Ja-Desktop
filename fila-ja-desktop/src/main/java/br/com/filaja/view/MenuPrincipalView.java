package br.com.filaja.view;

import br.com.filaja.model.Usuario;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipalView extends JFrame {

    private final Usuario usuario;

    public MenuPrincipalView(Usuario usuario) {

        this.usuario = usuario;

        setTitle("Fila Já - Sistema de Gerenciamento de Filas");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painelPrincipal = new JPanel(new BorderLayout(15, 15));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JPanel painelCabecalho = new JPanel(new GridLayout(3, 1));

        JLabel titulo = new JLabel("FILA JÁ", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 30));

        JLabel subtitulo = new JLabel(
                "Sistema de Gerenciamento de Filas de Atendimento",
                SwingConstants.CENTER
        );

        JLabel saudacao = new JLabel(
                "Bem-vindo(a), " + usuario.getNome() + "!",
                SwingConstants.CENTER
        );
        saudacao.setFont(new Font("Arial", Font.BOLD, 14));

        painelCabecalho.add(titulo);
        painelCabecalho.add(subtitulo);
        painelCabecalho.add(saudacao);

        JPanel painelBotoes = new JPanel(new GridLayout(3, 2, 15, 15));

        adicionarBotao(painelBotoes, "Pacientes",
                () -> new PacienteView().setVisible(true));

        adicionarBotao(painelBotoes, "Unidades de Saúde",
                () -> new UBSView().setVisible(true));

        adicionarBotao(painelBotoes, "Serviços",
                () -> new ServicoView().setVisible(true));

        adicionarBotao(painelBotoes, "Usuários",
                () -> new UsuarioView().setVisible(true));

        adicionarBotao(painelBotoes, "Gerenciamento da Fila",
                () -> new FilaView().setVisible(true));

        adicionarBotao(painelBotoes, "Sair",
                this::sair);

        JLabel rodape = new JLabel(
                "Fila Já | Sistema de Gestão de Atendimento",
                SwingConstants.CENTER
        );
        rodape.setFont(new Font("Arial", Font.PLAIN, 12));

        painelPrincipal.add(painelCabecalho, BorderLayout.NORTH);
        painelPrincipal.add(painelBotoes, BorderLayout.CENTER);
        painelPrincipal.add(rodape, BorderLayout.SOUTH);

        add(painelPrincipal);
    }

    private void adicionarBotao(JPanel painel, String texto, Runnable acao) {

        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.BOLD, 15));
        botao.setFocusPainted(false);
        botao.addActionListener(e -> acao.run());

        painel.add(botao);
    }

    private void sair() {

        int opcao = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente sair da sua conta?",
                "Sair",
                JOptionPane.YES_NO_OPTION
        );

        if (opcao == JOptionPane.YES_OPTION) {

            dispose();

            LoginView login = new LoginView();
            login.setVisible(true);
        }
    }
}