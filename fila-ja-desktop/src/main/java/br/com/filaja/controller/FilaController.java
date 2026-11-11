package br.com.filaja.controller;

import br.com.filaja.dao.FilaDAO;
import br.com.filaja.model.Atendimento;
import br.com.filaja.model.Fila;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class FilaController {

    private final FilaDAO dao;

    public FilaController() {
        this.dao = new FilaDAO();
    }

    public void cadastrar(Fila fila) throws SQLException {
        dao.cadastrar(fila);
    }

    public List<Fila> listarTodos() throws SQLException {
        return dao.listarTodos();
    }

    public List<Fila.ItemCombo> listarPacientes() throws SQLException {
        return dao.listarPacientes();
    }

    public List<Fila.ItemCombo> listarUbs() throws SQLException {
        return dao.listarUbs();
    }

    public List<Fila.ItemCombo> listarServicos() throws SQLException {
        return dao.listarServicos();
    }

    public List<Fila.ItemCombo> listarServicosPorUbs(int idUbs)
            throws SQLException {

        return dao.listarServicosPorUbs(idUbs);
    }

    public List<Fila.ItemCombo> listarAtendentes() throws SQLException {
        return dao.listarAtendentes();
    }

    public List<Fila.ItemCombo> listarProfissionais() throws SQLException {
        return dao.listarProfissionais();
    }

    public String chamarProximo(int idUbs, int idServico)
            throws SQLException {

        return dao.chamarProximo(idUbs, idServico);
    }

    public void iniciarAtendimento(int idFila, int idUsuario)
            throws SQLException {

        dao.iniciarAtendimento(idFila, idUsuario);
    }

    public void finalizarAtendimento(int idFila)
            throws SQLException {

        dao.finalizarAtendimento(idFila);
    }

    public void cancelar(int idFila) throws SQLException {
        dao.cancelar(idFila);
    }

    public List<Atendimento> listarAtendimentos(
            LocalDate inicio,
            LocalDate fim) throws SQLException {

        return dao.listarAtendimentos(inicio, fim);
    }
}