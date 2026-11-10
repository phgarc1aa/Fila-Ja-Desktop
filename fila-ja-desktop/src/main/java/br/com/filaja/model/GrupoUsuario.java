package br.com.filaja.model;

public class GrupoUsuario {

    private int idGrupo;
    private String nome;
    private String descricao;

    public GrupoUsuario() {
    }

    public GrupoUsuario(int idGrupo, String nome, String descricao) {
        this.idGrupo = idGrupo;
        this.nome = nome;
        this.descricao = descricao;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return nome;
    }
}