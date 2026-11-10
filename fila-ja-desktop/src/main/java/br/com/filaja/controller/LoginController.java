package br.com.filaja.controller;

import br.com.filaja.dao.UsuarioDAO;
import br.com.filaja.model.Usuario;

import java.sql.SQLException;

public class LoginController {

    private final UsuarioDAO usuarioDAO;

    public LoginController() {
        usuarioDAO = new UsuarioDAO();
    }

    public Usuario autenticar(String email, String senha) throws SQLException {
        return usuarioDAO.autenticar(email, senha);
    }
}