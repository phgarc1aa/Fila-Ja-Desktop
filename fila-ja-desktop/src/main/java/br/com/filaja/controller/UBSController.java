package br.com.filaja.controller;

import br.com.filaja.dao.UBSDAO;
import br.com.filaja.model.UBS;

import java.sql.SQLException;
import java.util.List;

public class UBSController {

    private final UBSDAO ubsDAO;

    public UBSController() {
        this.ubsDAO = new UBSDAO();
    }

    // Cadastrar UBS
    public void cadastrar(UBS ubs) throws SQLException {
        ubsDAO.cadastrar(ubs);
    }

    // Listar UBS
    public List<UBS> listarTodos() throws SQLException {
        return ubsDAO.listarTodos();
    }

    // Buscar UBS por ID
    public UBS buscarPorId(int id) throws SQLException {
        return ubsDAO.buscarPorId(id);
    }

    // Atualizar UBS
    public void atualizar(UBS ubs) throws SQLException {
        ubsDAO.atualizar(ubs);
    }

    // Excluir UBS
    public void excluir(int id) throws SQLException {
        ubsDAO.excluir(id);
    }
}