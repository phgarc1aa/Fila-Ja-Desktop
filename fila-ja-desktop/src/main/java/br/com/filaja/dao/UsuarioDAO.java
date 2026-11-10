package br.com.filaja.dao;

import br.com.filaja.model.GrupoUsuario;
import br.com.filaja.model.Usuario;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // CADASTRAR
    public void cadastrar(Usuario usuario) throws SQLException {

        String sql = """
                INSERT INTO usuarios
                (id_usuario, nome, email, senha, id_grupo, ativo)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conexao = ConnectionFactory.getConnection()) {

            conexao.setAutoCommit(false);

            try {
                int id;

                try (PreparedStatement ps = conexao.prepareStatement(
                        "SELECT gerar_id(?)")) {

                    ps.setString(1, "usuarios");

                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        id = rs.getInt(1);
                    }
                }

                try (PreparedStatement ps = conexao.prepareStatement(sql)) {

                    ps.setInt(1, id);
                    ps.setString(2, usuario.getNome());
                    ps.setString(3, usuario.getEmail());
                    ps.setString(4, gerarHash(usuario.getSenha()));
                    ps.setInt(5, usuario.getIdGrupo());
                    ps.setBoolean(6, usuario.isAtivo());

                    ps.executeUpdate();
                }

                conexao.commit();

            } catch (SQLException e) {
                conexao.rollback();
                throw e;
            }
        }
    }

    // LISTAR TODOS
    public List<Usuario> listarTodos() throws SQLException {

        List<Usuario> usuarios = new ArrayList<>();

        String sql = """
                SELECT u.id_usuario,
                       u.nome,
                       u.email,
                       u.id_grupo,
                       g.nome AS nome_grupo,
                       u.ativo
                FROM usuarios u
                INNER JOIN grupos_usuarios g
                    ON u.id_grupo = g.id_grupo
                ORDER BY u.id_usuario
                """;

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Usuario usuario = new Usuario();

                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setIdGrupo(rs.getInt("id_grupo"));
                usuario.setNomeGrupo(rs.getString("nome_grupo"));
                usuario.setAtivo(rs.getBoolean("ativo"));

                usuarios.add(usuario);
            }
        }

        return usuarios;
    }

    // BUSCAR POR ID
    public Usuario buscarPorId(int id) throws SQLException {

        String sql = """
                SELECT u.id_usuario,
                       u.nome,
                       u.email,
                       u.id_grupo,
                       g.nome AS nome_grupo,
                       u.ativo
                FROM usuarios u
                INNER JOIN grupos_usuarios g
                    ON u.id_grupo = g.id_grupo
                WHERE u.id_usuario = ?
                """;

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Usuario usuario = new Usuario();

                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setIdGrupo(rs.getInt("id_grupo"));
                    usuario.setNomeGrupo(rs.getString("nome_grupo"));
                    usuario.setAtivo(rs.getBoolean("ativo"));

                    return usuario;
                }
            }
        }

        return null;
    }

    // ATUALIZAR
    public void atualizar(Usuario usuario) throws SQLException {

        boolean alterarSenha = usuario.getSenha() != null
                && !usuario.getSenha().isBlank();

        String sql;

        if (alterarSenha) {
            sql = """
                    UPDATE usuarios
                    SET nome = ?,
                        email = ?,
                        senha = ?,
                        id_grupo = ?,
                        ativo = ?
                    WHERE id_usuario = ?
                    """;
        } else {
            sql = """
                    UPDATE usuarios
                    SET nome = ?,
                        email = ?,
                        id_grupo = ?,
                        ativo = ?
                    WHERE id_usuario = ?
                    """;
        }

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());

            if (alterarSenha) {

                ps.setString(3, gerarHash(usuario.getSenha()));
                ps.setInt(4, usuario.getIdGrupo());
                ps.setBoolean(5, usuario.isAtivo());
                ps.setInt(6, usuario.getIdUsuario());

            } else {

                ps.setInt(3, usuario.getIdGrupo());
                ps.setBoolean(4, usuario.isAtivo());
                ps.setInt(5, usuario.getIdUsuario());
            }

            ps.executeUpdate();
        }
    }

    // EXCLUIR
    public void excluir(int id) throws SQLException {

        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // LISTAR GRUPOS
    public List<GrupoUsuario> listarGrupos() throws SQLException {

        List<GrupoUsuario> grupos = new ArrayList<>();

        String sql = """
                SELECT id_grupo, nome, descricao
                FROM grupos_usuarios
                ORDER BY id_grupo
                """;

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                GrupoUsuario grupo = new GrupoUsuario();

                grupo.setIdGrupo(rs.getInt("id_grupo"));
                grupo.setNome(rs.getString("nome"));
                grupo.setDescricao(rs.getString("descricao"));

                grupos.add(grupo);
            }
        }

        return grupos;
    }

    // GERAR HASH DA SENHA
    private String gerarHash(String senha) throws SQLException {

        try {

            byte[] salt = new byte[16];
            new SecureRandom().nextBytes(salt);

            PBEKeySpec spec = new PBEKeySpec(
                    senha.toCharArray(),
                    salt,
                    120000,
                    256
            );

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            byte[] hash = factory.generateSecret(spec).getEncoded();

            spec.clearPassword();

            return Base64.getEncoder().encodeToString(salt)
                    + "$"
                    + Base64.getEncoder().encodeToString(hash);

        } catch (Exception e) {
            throw new SQLException("Erro ao proteger a senha.", e);
        }
    }

    // AUTENTICAR USUÁRIO
    public Usuario autenticar(String email, String senha) throws SQLException {

        String sql = """
            SELECT u.id_usuario,
                   u.nome,
                   u.email,
                   u.senha,
                   u.id_grupo,
                   g.nome AS nome_grupo,
                   u.ativo
            FROM usuarios u
            INNER JOIN grupos_usuarios g
                ON u.id_grupo = g.id_grupo
            WHERE u.email = ?
              AND u.ativo = 1
            """;

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    String hashArmazenado = rs.getString("senha");

                    if (verificarSenha(senha, hashArmazenado)) {

                        Usuario usuario = new Usuario();

                        usuario.setIdUsuario(rs.getInt("id_usuario"));
                        usuario.setNome(rs.getString("nome"));
                        usuario.setEmail(rs.getString("email"));
                        usuario.setIdGrupo(rs.getInt("id_grupo"));
                        usuario.setNomeGrupo(rs.getString("nome_grupo"));
                        usuario.setAtivo(rs.getBoolean("ativo"));

                        return usuario;
                    }
                }
            }
        }

        return null;
    }

    // VERIFICAR SENHA
    private boolean verificarSenha(String senha, String hashArmazenado) {

        try {

            String[] partes = hashArmazenado.split("\\$");

            byte[] salt = Base64.getDecoder().decode(partes[0]);
            byte[] hashOriginal = Base64.getDecoder().decode(partes[1]);

            PBEKeySpec spec = new PBEKeySpec(
                    senha.toCharArray(),
                    salt,
                    120000,
                    256
            );

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            byte[] hashDigitado = factory.generateSecret(spec).getEncoded();

            spec.clearPassword();

            return java.security.MessageDigest.isEqual(
                    hashOriginal,
                    hashDigitado
            );

        } catch (Exception e) {
            return false;
        }
    }
}