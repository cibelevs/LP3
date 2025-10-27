package Exerc_Extra.entrega;

import java.sql.Time;





public class Entrega {
    public enum Prioridade {
        URGENTE(1),
        NORMAL(2),
        ECONOMICA(3);

        private final int nivel;
        Prioridade(int nivel) {
            this.nivel = nivel;
        }

        public int getNivel() {
            return nivel;
        }
    }
    private int id;
    private String nomeCliente;
    private float destino;
    private Prioridade prioridade;
    private Time tempoCriação;
    public Entrega(int id, String nomeCliente, float destino, Prioridade prioridade, Time tempoCriação) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.destino = destino;
        this.prioridade = prioridade;
        this.tempoCriação = tempoCriação;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNomeCliente() {
        return nomeCliente;
    }
    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }
    public float getDestino() {
        return destino;
    }
    public void setDestino(float destino) {
        this.destino = destino;
    }
    public Prioridade getPrioridade() {
        return prioridade;
    }
    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }
    public Time getTempoCriação() {
        return tempoCriação;
    }
    public void setTempoCriação(Time tempoCriação) {
        this.tempoCriação = tempoCriação;
    }


    
}
