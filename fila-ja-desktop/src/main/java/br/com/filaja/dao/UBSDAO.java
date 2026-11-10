package br.com.filaja.dao;

import br.com.filaja.model.UBS;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UBSDAO {

    // Cadastrar UBS
    public void cadastrar(UBS ubs) throws SQLException {

        String sql = """
                INSERT INTO ubs
                (id_ubs, nome, endereco, telefone, ativo)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conexao = ConnectionFactory.getConnection()) {

            conexao.setAutoCommit(false);

            try {

                // Gerar identificador
                String sqlId = "SELECT gerar_id(?)";

                try (PreparedStatement stmtId =
                             conexao.prepareStatement(sqlId)) {

                    stmtId.setString(1, "ubs");

                    try (ResultSet resultado = stmtId.executeQuery()) {

                        if (resultado.next()) {
                            ubs.setIdUbs(resultado.getInt(1));
                        }
                    }
                }

                // Inserir UBS
                try (PreparedStatement stmt =
                             conexao.prepareStatement(sql)) {

                    stmt.setInt(1, ubs.getIdUbs());
                    stmt.setString(2, ubs.getNome());
                    stmt.setString(3, ubs.getEndereco());
                    stmt.setString(4, ubs.getTelefone());
                    stmt.setBoolean(5, ubs.isAtivo());

                    stmt.executeUpdate();
                }

                conexao.commit();

            } catch (SQLException e) {

                conexao.rollback();
                throw e;
            }
        }
    }

    // Listar UBS
    public List<UBS> listarTodos() throws SQLException {

        String sql = "SELECT * FROM ubs ORDER BY id_ubs";

        List<UBS> unidades = new ArrayList<>();

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet resultado = stmt.executeQuery()) {

            while (resultado.next()) {

                unidades.add(mapearUBS(resultado));
            }
        }

        return unidades;
    }

    // Buscar UBS por ID
    public UBS buscarPorId(int id) throws SQLException {

        String sql = "SELECT * FROM ubs WHERE id_ubs = ?";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet resultado = stmt.executeQuery()) {

                if (resultado.next()) {
                    return mapearUBS(resultado);
                }
            }
        }

        return null;
    }

    // Atualizar UBS
    public void atualizar(UBS ubs) throws SQLException {

        String sql = """
                UPDATE ubs
                SET nome = ?, endereco = ?, telefone = ?, ativo = ?
                WHERE id_ubs = ?
                """;

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, ubs.getNome());
            stmt.setString(2, ubs.getEndereco());
            stmt.setString(3, ubs.getTelefone());
            stmt.setBoolean(4, ubs.isAtivo());
            stmt.setInt(5, ubs.getIdUbs());

            stmt.executeUpdate();
        }
    }

    // Excluir UBS
    public void excluir(int id) throws SQLException {

        String sql = "DELETE FROM ubs WHERE id_ubs = ?";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();
        }
    }

    // Converter resultado SQL em objeto UBS
    private UBS mapearUBS(ResultSet resultado) throws SQLException {

        UBS ubs = new UBS();

        ubs.setIdUbs(resultado.getInt("id_ubs"));
        ubs.setNome(resultado.getString("nome"));
        ubs.setEndereco(resultado.getString("endereco"));
        ubs.setTelefone(resultado.getString("telefone"));
        ubs.setAtivo(resultado.getBoolean("ativo"));

        return ubs;
    }
}