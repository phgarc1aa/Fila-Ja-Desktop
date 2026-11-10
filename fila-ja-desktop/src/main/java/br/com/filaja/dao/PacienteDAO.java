package br.com.filaja.dao;

import br.com.filaja.model.Paciente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {

    // Cadastrar paciente
    public void cadastrar(Paciente paciente) throws SQLException {

        String sql = """
                INSERT INTO pacientes
                (id_paciente, nome, cpf, data_nascimento, telefone, email)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conexao = ConnectionFactory.getConnection()) {

            conexao.setAutoCommit(false);

            try {
                // Gerar identificador
                String sqlId = "SELECT gerar_id(?)";

                try (PreparedStatement stmtId = conexao.prepareStatement(sqlId)) {

                    stmtId.setString(1, "pacientes");

                    try (ResultSet resultado = stmtId.executeQuery()) {
                        if (resultado.next()) {
                            paciente.setIdPaciente(resultado.getInt(1));
                        }
                    }
                }

                // Inserir paciente
                try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

                    stmt.setInt(1, paciente.getIdPaciente());
                    stmt.setString(2, paciente.getNome());
                    stmt.setString(3, paciente.getCpf());
                    stmt.setDate(4, java.sql.Date.valueOf(paciente.getDataNascimento()));
                    stmt.setString(5, paciente.getTelefone());
                    stmt.setString(6, paciente.getEmail());

                    stmt.executeUpdate();
                }

                conexao.commit();

            } catch (SQLException e) {
                conexao.rollback();
                throw e;
            }
        }
    }

    // Listar todos os pacientes
    public List<Paciente> listarTodos() throws SQLException {

        String sql = "SELECT * FROM pacientes ORDER BY id_paciente";

        List<Paciente> pacientes = new ArrayList<>();

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet resultado = stmt.executeQuery()) {

            while (resultado.next()) {
                pacientes.add(mapearPaciente(resultado));
            }
        }

        return pacientes;
    }

    // Buscar paciente por ID
    public Paciente buscarPorId(int id) throws SQLException {

        String sql = "SELECT * FROM pacientes WHERE id_paciente = ?";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet resultado = stmt.executeQuery()) {

                if (resultado.next()) {
                    return mapearPaciente(resultado);
                }
            }
        }

        return null;
    }

    // Atualizar paciente
    public void atualizar(Paciente paciente) throws SQLException {

        String sql = """
                UPDATE pacientes
                SET nome = ?, cpf = ?, data_nascimento = ?,
                    telefone = ?, email = ?
                WHERE id_paciente = ?
                """;

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getCpf());
            stmt.setDate(3, Date.valueOf(paciente.getDataNascimento()));
            stmt.setString(4, paciente.getTelefone());
            stmt.setString(5, paciente.getEmail());
            stmt.setInt(6, paciente.getIdPaciente());

            stmt.executeUpdate();
        }
    }

    // Excluir paciente
    public void excluir(int id) throws SQLException {

        String sql = "DELETE FROM pacientes WHERE id_paciente = ?";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Converter resultado SQL em objeto Paciente
    private Paciente mapearPaciente(ResultSet resultado) throws SQLException {

        Paciente paciente = new Paciente();

        paciente.setIdPaciente(resultado.getInt("id_paciente"));
        paciente.setNome(resultado.getString("nome"));
        paciente.setCpf(resultado.getString("cpf"));
        paciente.setDataNascimento(
                resultado.getDate("data_nascimento").toLocalDate()
        );
        paciente.setTelefone(resultado.getString("telefone"));
        paciente.setEmail(resultado.getString("email"));

        Timestamp dataCadastro = resultado.getTimestamp("data_cadastro");

        if (dataCadastro != null) {
            paciente.setDataCadastro(dataCadastro.toLocalDateTime());
        }

        return paciente;
    }
}