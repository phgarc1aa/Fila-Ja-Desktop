package br.com.filaja.model;

public class UBS {

    private int idUbs;
    private String nome;
    private String endereco;
    private String telefone;
    private boolean ativo;

    public UBS() {
    }

    public UBS(int idUbs, String nome, String endereco,
               String telefone, boolean ativo) {

        this.idUbs = idUbs;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.ativo = ativo;
    }

    public int getIdUbs() {
        return idUbs;
    }

    public void setIdUbs(int idUbs) {
        this.idUbs = idUbs;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}