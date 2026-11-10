package br.com.filaja.controller;

import br.com.filaja.dao.UsuarioDAO;
import br.com.filaja.model.GrupoUsuario;
import br.com.filaja.model.Usuario;

import java.sql.SQLException;
import java.util.List;

public class UsuarioController {

    private final UsuarioDAO dao;

    public UsuarioController() {
        this.dao = new UsuarioDAO();
    }

    public void cadastrar(Usuario usuario) throws SQLException {
        dao.cadastrar(usuario);
    }

    public List<Usuario> listarTodos() throws SQLException {
        return dao.listarTodos();
    }

    public Usuario buscarPorId(int id) throws SQLException {
        return dao.buscarPorId(id);
    }

    public void atualizar(Usuario usuario) throws SQLException {
        dao.atualizar(usuario);
    }

    public void excluir(int id) throws SQLException {
        dao.excluir(id);
    }

    public List<GrupoUsuario> listarGrupos() throws SQLException {
        return dao.listarGrupos();
    }
}