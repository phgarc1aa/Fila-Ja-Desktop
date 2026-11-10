package br.com.filaja.dao;

import br.com.filaja.model.Servico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicoDAO {

    // Cadastrar serviço
    public void cadastrar(Servico servico) throws SQLException {

        String sql = """
                INSERT INTO servicos
                (id_servico, nome, descricao, ativo)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conexao = ConnectionFactory.getConnection()) {

            conexao.setAutoCommit(false);

            try {

                // Gerar identificador
                String sqlId = "SELECT gerar_id(?)";

                try (PreparedStatement stmtId =
                             conexao.prepareStatement(sqlId)) {

                    stmtId.setString(1, "servicos");

                    try (ResultSet resultado = stmtId.executeQuery()) {

                        if (resultado.next()) {
                            servico.setIdServico(resultado.getInt(1));
                        }
                    }
                }

                // Inserir serviço
                try (PreparedStatement stmt =
                             conexao.prepareStatement(sql)) {

                    stmt.setInt(1, servico.getIdServico());
                    stmt.setString(2, servico.getNome());
                    stmt.setString(3, servico.getDescricao());
                    stmt.setBoolean(4, servico.isAtivo());

                    stmt.executeUpdate();
                }

                conexao.commit();

            } catch (SQLException e) {

                conexao.rollback();
                throw e;
            }
        }
    }

    // Listar serviços
    public List<Servico> listarTodos() throws SQLException {

        String sql = "SELECT * FROM servicos ORDER BY id_servico";

        List<Servico> servicos = new ArrayList<>();

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet resultado = stmt.executeQuery()) {

            while (resultado.next()) {
                servicos.add(mapearServico(resultado));
            }
        }

        return servicos;
    }

    // Buscar serviço por ID
    public Servico buscarPorId(int id) throws SQLException {

        String sql = "SELECT * FROM servicos WHERE id_servico = ?";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet resultado = stmt.executeQuery()) {

                if (resultado.next()) {
                    return mapearServico(resultado);
                }
            }
        }

        return null;
    }

    // Atualizar serviço
    public void atualizar(Servico servico) throws SQLException {

        String sql = """
                UPDATE servicos
                SET nome = ?, descricao = ?, ativo = ?
                WHERE id_servico = ?
                """;

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, servico.getNome());
            stmt.setString(2, servico.getDescricao());
            stmt.setBoolean(3, servico.isAtivo());
            stmt.setInt(4, servico.getIdServico());

            stmt.executeUpdate();
        }
    }

    // Excluir serviço
    public void excluir(int id) throws SQLException {

        String sql = "DELETE FROM servicos WHERE id_servico = ?";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();
        }
    }

    // Converter resultado SQL em objeto Serviço
    private Servico mapearServico(ResultSet resultado) throws SQLException {

        Servico servico = new Servico();

        servico.setIdServico(resultado.getInt("id_servico"));
        servico.setNome(resultado.getString("nome"));
        servico.setDescricao(resultado.getString("descricao"));
        servico.setAtivo(resultado.getBoolean("ativo"));

        return servico;
    }
}