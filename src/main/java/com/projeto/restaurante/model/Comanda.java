package com.projeto.restaurante.model;
import java.time.LocalDateTime;

public class Comanda {
    private int id;
    private int numeroMesa;
    private String status; // "ABERTA", "FECHADA", "PAGA"
    private LocalDateTime dataHoraAbertura;
    private double valorTotal;

    public Comanda() {}

    public Comanda(int id, int numeroMesa, String status, LocalDateTime dataHoraAbertura, double valorTotal) {
        this.id = id;
        this.numeroMesa = numeroMesa;
        this.status = status;
        this.dataHoraAbertura = dataHoraAbertura;
        this.valorTotal = valorTotal;
    }

    public Comanda(int numeroMesa) {
        this.numeroMesa = numeroMesa;
        this.status = "ABERTA";
        this.dataHoraAbertura = LocalDateTime.now();
        this.valorTotal = 0.0;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getNumeroMesa() { return numeroMesa; }
    public void setNumeroMesa(int numeroMesa) { this.numeroMesa = numeroMesa; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getDataHoraAbertura() { return dataHoraAbertura; }
    public void setDataHoraAbertura(LocalDateTime dataHoraAbertura) { this.dataHoraAbertura = dataHoraAbertura; }

    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }
}