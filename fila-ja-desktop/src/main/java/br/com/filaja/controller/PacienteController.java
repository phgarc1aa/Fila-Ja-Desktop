package br.com.filaja.controller;

import br.com.filaja.dao.PacienteDAO;
import br.com.filaja.model.Paciente;

import java.sql.SQLException;
import java.util.List;

public class PacienteController {

    private final PacienteDAO pacienteDAO;

    public PacienteController() {
        this.pacienteDAO = new PacienteDAO();
    }

    // Cadastrar paciente
    public void cadastrar(Paciente paciente) throws SQLException {
        pacienteDAO.cadastrar(paciente);
    }

    // Listar pacientes
    public List<Paciente> listarTodos() throws SQLException {
        return pacienteDAO.listarTodos();
    }

    // Buscar paciente por ID
    public Paciente buscarPorId(int id) throws SQLException {
        return pacienteDAO.buscarPorId(id);
    }

    // Atualizar paciente
    public void atualizar(Paciente paciente) throws SQLException {
        pacienteDAO.atualizar(paciente);
    }

    // Excluir paciente
    public void excluir(int id) throws SQLException {
        pacienteDAO.excluir(id);
    }
}