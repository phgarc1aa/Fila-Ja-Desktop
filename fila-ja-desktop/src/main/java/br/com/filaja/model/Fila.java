package br.com.filaja.model;

import java.time.LocalDateTime;

public class Fila {

    private int idFila;
    private int idPaciente;
    private int idUbs;
    private int idServico;
    private int idUsuario;

    private String senha;
    private String paciente;
    private String ubs;
    private String servico;
    private String status;

    private LocalDateTime dataEntrada;

    public Fila() {
    }

    public int getIdFila() {
        return idFila;
    }

    public void setIdFila(int idFila) {
        this.idFila = idFila;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public int getIdUbs() {
        return idUbs;
    }

    public void setIdUbs(int idUbs) {
        this.idUbs = idUbs;
    }

    public int getIdServico() {
        return idServico;
    }

    public void setIdServico(int idServico) {
        this.idServico = idServico;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getUbs() {
        return ubs;
    }

    public void setUbs(String ubs) {
        this.ubs = ubs;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    // OPÇÕES PARA OS CAMPOS DE SELEÇÃO
    public static class ItemCombo {

        private final int id;
        private final String nome;

        public ItemCombo(int id, String nome) {
            this.id = id;
            this.nome = nome;
        }

        public int getId() {
            return id;
        }

        public String getNome() {
            return nome;
        }

        @Override
        public String toString() {
            return nome;
        }
    }
}