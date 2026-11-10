package br.com.filaja.controller;

import br.com.filaja.dao.ServicoDAO;
import br.com.filaja.model.Servico;

import java.sql.SQLException;
import java.util.List;

public class ServicoController {

    private final ServicoDAO servicoDAO;

    public ServicoController() {
        this.servicoDAO = new ServicoDAO();
    }

    // Cadastrar serviço
    public void cadastrar(Servico servico) throws SQLException {
        servicoDAO.cadastrar(servico);
    }

    // Listar serviços
    public List<Servico> listarTodos() throws SQLException {
        return servicoDAO.listarTodos();
    }

    // Buscar serviço por ID
    public Servico buscarPorId(int id) throws SQLException {
        return servicoDAO.buscarPorId(id);
    }

    // Atualizar serviço
    public void atualizar(Servico servico) throws SQLException {
        servicoDAO.atualizar(servico);
    }

    // Excluir serviço
    public void excluir(int id) throws SQLException {
        servicoDAO.excluir(id);
    }
}