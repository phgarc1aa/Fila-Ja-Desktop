package br.com.filaja.dao;

import br.com.filaja.model.Atendimento;
import br.com.filaja.model.Fila;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FilaDAO {

    // CADASTRAR NA FILA
    public void cadastrar(Fila fila) throws SQLException {

        String sql = """
                INSERT INTO fila
                (id_fila, id_paciente, id_ubs, id_servico,
                 id_usuario, senha)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conexao = ConnectionFactory.getConnection()) {

            conexao.setAutoCommit(false);

            try {

                int idFila;

                try (PreparedStatement ps = conexao.prepareStatement(
                        "SELECT gerar_id(?)")) {

                    ps.setString(1, "fila");

                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        idFila = rs.getInt(1);
                    }
                }

                // Bloqueia a UBS durante a geração da senha.
                String sqlLock = """
                        SELECT id_ubs
                        FROM ubs
                        WHERE id_ubs = ?
                        FOR UPDATE
                        """;

                try (PreparedStatement ps = conexao.prepareStatement(sqlLock)) {

                    ps.setInt(1, fila.getIdUbs());

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {
                            throw new SQLException("UBS não encontrada.");
                        }
                    }
                }

                // Descobre a posição da UBS entre as unidades ativas.
                int ordemUbs;

                String sqlOrdem = """
                        SELECT COUNT(*)
                        FROM ubs
                        WHERE ativo = 1
                          AND id_ubs <= ?
                        """;

                try (PreparedStatement ps = conexao.prepareStatement(sqlOrdem)) {

                    ps.setInt(1, fila.getIdUbs());

                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        ordemUbs = rs.getInt(1);
                    }
                }

                if (ordemUbs < 1 || ordemUbs > 26) {
                    throw new SQLException(
                            "Não foi possível gerar o prefixo da senha."
                    );
                }

                String prefixo = String.valueOf(
                        (char) ('A' + ordemUbs - 1)
                );

                // Descobre o último número utilizado pela UBS.
                String sqlNumero = """
                        SELECT COALESCE(
                            MAX(CAST(SUBSTRING(senha, 2) AS UNSIGNED)),
                            0
                        )
                        FROM fila
                        WHERE id_ubs = ?
                        """;

                int numero;

                try (PreparedStatement ps = conexao.prepareStatement(sqlNumero)) {

                    ps.setInt(1, fila.getIdUbs());

                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        numero = rs.getInt(1) + 1;
                    }
                }

                String senha = prefixo + String.format("%03d", numero);

                try (PreparedStatement ps = conexao.prepareStatement(sql)) {

                    ps.setInt(1, idFila);
                    ps.setInt(2, fila.getIdPaciente());
                    ps.setInt(3, fila.getIdUbs());
                    ps.setInt(4, fila.getIdServico());
                    ps.setInt(5, fila.getIdUsuario());
                    ps.setString(6, senha);

                    ps.executeUpdate();
                }

                conexao.commit();

            } catch (SQLException e) {

                conexao.rollback();
                throw e;
            }
        }
    }

    // LISTAR FILA
    public List<Fila> listarTodos() throws SQLException {

        List<Fila> lista = new ArrayList<>();

        String sql = """
                SELECT *
                FROM vw_fila_atendimento
                ORDER BY data_entrada DESC, id_fila DESC
                """;

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Fila fila = new Fila();

                fila.setIdFila(rs.getInt("id_fila"));
                fila.setSenha(rs.getString("senha"));
                fila.setPaciente(rs.getString("paciente"));
                fila.setUbs(rs.getString("ubs"));
                fila.setServico(rs.getString("servico"));
                fila.setStatus(rs.getString("status"));

                Timestamp entrada = rs.getTimestamp("data_entrada");

                if (entrada != null) {
                    fila.setDataEntrada(entrada.toLocalDateTime());
                }

                lista.add(fila);
            }
        }

        return lista;
    }

    // LISTAR PACIENTES
    public List<Fila.ItemCombo> listarPacientes() throws SQLException {

        String sql = """
                SELECT id_paciente, nome
                FROM pacientes
                ORDER BY nome
                """;

        return listarOpcoes(sql);
    }

    // LISTAR UBS
    public List<Fila.ItemCombo> listarUbs() throws SQLException {

        String sql = """
                SELECT id_ubs, nome
                FROM ubs
                WHERE ativo = 1
                ORDER BY nome
                """;

        return listarOpcoes(sql);
    }

    // LISTAR SERVIÇOS
    public List<Fila.ItemCombo> listarServicosPorUbs(int idUbs) throws SQLException {

        String sql = """
            SELECT s.id_servico, s.nome
            FROM servicos s
            INNER JOIN ubs_servico us
                ON us.id_servico = s.id_servico
            WHERE us.id_ubs = ?
              AND s.ativo = 1
            ORDER BY s.nome
            """;

        List<Fila.ItemCombo> lista = new ArrayList<>();

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, idUbs);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    lista.add(new Fila.ItemCombo(
                            rs.getInt("id_servico"),
                            rs.getString("nome")
                    ));
                }
            }
        }

        return lista;
    }

    public List<Fila.ItemCombo> listarServicos() throws SQLException {

        String sql = """
            SELECT id_servico, nome
            FROM servicos
            WHERE ativo = 1
            ORDER BY nome
            """;

        return listarOpcoes(sql);
    }

    // LISTAR ATENDENTES
    public List<Fila.ItemCombo> listarAtendentes() throws SQLException {

        String sql = """
                SELECT u.id_usuario, u.nome
                FROM usuarios u
                INNER JOIN grupos_usuarios g
                    ON u.id_grupo = g.id_grupo
                WHERE g.nome = 'Atendente'
                  AND u.ativo = 1
                ORDER BY u.nome
                """;

        return listarOpcoes(sql);
    }

    // LISTAR PROFISSIONAIS
    public List<Fila.ItemCombo> listarProfissionais() throws SQLException {

        String sql = """
                SELECT u.id_usuario, u.nome
                FROM usuarios u
                INNER JOIN grupos_usuarios g
                    ON u.id_grupo = g.id_grupo
                WHERE g.nome = 'Profissional de Saúde'
                  AND u.ativo = 1
                ORDER BY u.nome
                """;

        return listarOpcoes(sql);
    }

    // MÉTODO AUXILIAR PARA COMBOS
    private List<Fila.ItemCombo> listarOpcoes(String sql)
            throws SQLException {

        List<Fila.ItemCombo> lista = new ArrayList<>();

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                lista.add(new Fila.ItemCombo(
                        rs.getInt(1),
                        rs.getString(2)
                ));
            }
        }

        return lista;
    }

    // CHAMAR PRÓXIMO PACIENTE
    public String chamarProximo(int idUbs, int idServico)
            throws SQLException {

        String sql = "{CALL sp_chamar_paciente(?, ?)}";

        try (Connection conexao = ConnectionFactory.getConnection();
             CallableStatement cs = conexao.prepareCall(sql)) {

            cs.setInt(1, idUbs);
            cs.setInt(2, idServico);

            boolean resultado = cs.execute();

            if (resultado) {

                try (ResultSet rs = cs.getResultSet()) {

                    if (rs.next()) {

                        String primeiraColuna =
                                rs.getMetaData().getColumnLabel(1);

                        if ("mensagem".equalsIgnoreCase(primeiraColuna)) {
                            return rs.getString("mensagem");
                        }

                        return "Paciente chamado: "
                                + rs.getString("paciente")
                                + " | Senha: "
                                + rs.getString("senha");
                    }
                }
            }

            return "Não foi possível obter o resultado da chamada.";
        }
    }

    // INICIAR ATENDIMENTO
    public void iniciarAtendimento(int idFila, int idUsuario)
            throws SQLException {

        String sql = """
                UPDATE fila
                SET id_usuario = ?,
                    status = 'EM_ATENDIMENTO'
                WHERE id_fila = ?
                  AND status = 'CHAMADO'
                """;

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, idFila);

            int resultado = ps.executeUpdate();

            if (resultado == 0) {
                throw new SQLException(
                        "Não foi possível iniciar o atendimento. "
                                + "Verifique se o paciente foi chamado."
                );
            }
        }
    }

    // FINALIZAR ATENDIMENTO
    public void finalizarAtendimento(int idFila) throws SQLException {

        String sql = "{CALL sp_finalizar_atendimento(?)}";

        try (Connection conexao = ConnectionFactory.getConnection();
             CallableStatement cs = conexao.prepareCall(sql)) {

            cs.setInt(1, idFila);
            cs.execute();
        }
    }

    // CANCELAR REGISTRO
    public void cancelar(int idFila) throws SQLException {

        String sql = """
                UPDATE fila
                SET status = 'CANCELADO'
                WHERE id_fila = ?
                  AND status IN ('AGUARDANDO', 'CHAMADO')
                """;

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, idFila);

            int resultado = ps.executeUpdate();

            if (resultado == 0) {
                throw new SQLException(
                        "Não foi possível cancelar este registro."
                );
            }
        }
    }

    // RELATÓRIO DE ATENDIMENTOS
    public List<Atendimento> listarAtendimentos(
            LocalDate inicio,
            LocalDate fim) throws SQLException {

        List<Atendimento> lista = new ArrayList<>();

        String sql = """
                SELECT
                    a.id_atendimento,
                    f.senha,
                    p.nome AS paciente,
                    u.nome AS ubs,
                    s.nome AS servico,
                    us.nome AS profissional,
                    a.data_inicio,
                    a.data_fim,
                    a.observacoes
                FROM atendimentos a
                INNER JOIN fila f
                    ON a.id_fila = f.id_fila
                INNER JOIN pacientes p
                    ON f.id_paciente = p.id_paciente
                INNER JOIN ubs u
                    ON f.id_ubs = u.id_ubs
                INNER JOIN servicos s
                    ON f.id_servico = s.id_servico
                INNER JOIN usuarios us
                    ON a.id_usuario = us.id_usuario
                WHERE a.data_inicio >= ?
                  AND a.data_inicio < DATE_ADD(?, INTERVAL 1 DAY)
                ORDER BY a.data_inicio DESC
                """;

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(inicio));
            ps.setDate(2, java.sql.Date.valueOf(fim));

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Atendimento atendimento = new Atendimento();

                    atendimento.setIdAtendimento(
                            rs.getInt("id_atendimento")
                    );

                    atendimento.setSenha(rs.getString("senha"));
                    atendimento.setPaciente(rs.getString("paciente"));
                    atendimento.setUbs(rs.getString("ubs"));
                    atendimento.setServico(rs.getString("servico"));
                    atendimento.setProfissional(
                            rs.getString("profissional")
                    );

                    Timestamp dataInicio = rs.getTimestamp("data_inicio");
                    Timestamp dataFim = rs.getTimestamp("data_fim");

                    if (dataInicio != null) {
                        atendimento.setDataInicio(
                                dataInicio.toLocalDateTime()
                        );
                    }

                    if (dataFim != null) {
                        atendimento.setDataFim(
                                dataFim.toLocalDateTime()
                        );
                    }

                    atendimento.setObservacoes(
                            rs.getString("observacoes")
                    );

                    lista.add(atendimento);
                }
            }
        }

        return lista;
    }
}