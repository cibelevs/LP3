package Exerc_Extra.entrega;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


enum Prioridade {
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

// ------------------------------------------------------------
// CLASSE Entrega
// Representa um pedido de entrega com dados do cliente,
// endereço, prioridade e timestamp.
// ------------------------------------------------------------
class Entrega {
    private final int id;
    private final String cliente;
    private final String endereco;
    private final int distanciaKm;
    private final Prioridade prioridade;
    private final LocalDateTime timestamp;

    public Entrega(int id, String cliente, String endereco, int distanciaKm, Prioridade prioridade) {
        this.id = id;
        this.cliente = cliente;
        this.endereco = endereco;
        this.distanciaKm = distanciaKm;
        this.prioridade = prioridade;
        this.timestamp = LocalDateTime.now(); // marca a hora em que foi criada
    }

    public int getId() { return id; }
    public String getCliente() { return cliente; }
    public String getEndereco() { return endereco; }
    public int getDistanciaKm() { return distanciaKm; }
    public Prioridade getPrioridade() { return prioridade; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("Entrega#%d [%s] %s - %s (%dkm, %s)",
                id, prioridade, cliente, endereco, distanciaKm,
                timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")));
    }
}