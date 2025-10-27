package Exerc_Extra.Cyber;

import java.sql.Timestamp;

// Classe Pagamento com todas as propriedades necessárias
class Pagamento implements Comparable<Pagamento> {
    private int id;
    private String nome;
    private TipoPagamento tipoPagamento;
    private double valor;
    private Timestamp tempo;
    private Prioridade prioridade;

    public Pagamento(int id, String nome, double valor, TipoPagamento tipoPagamento, Timestamp tempo) {
        this.id = id;
        this.nome = nome;
        this.valor = valor;
        this.tipoPagamento = tipoPagamento;
        this.tempo = tempo;
        // Define a prioridade baseada no valor
        this.prioridade = definirPrioridade(valor);
    }

    // Método para definir prioridade baseada no valor
    private Prioridade definirPrioridade(double valor) {
        if (valor > 1000) return Prioridade.ALTA;
        else if (valor > 100) return Prioridade.MEDIA;
        else return Prioridade.BAIXA;
    }

    // Implementação do compareTo para ordenação na PriorityBlockingQueue
    @Override
    public int compareTo(Pagamento outro) {
        // Primeiro compara por prioridade (ALTA > MÉDIA > BAIXA)
        int comparacaoPrioridade = outro.prioridade.ordinal() - this.prioridade.ordinal();
        if (comparacaoPrioridade != 0) {
            return comparacaoPrioridade;
        }
        // Se mesma prioridade, compara por tempo (mais antigo primeiro)
        return this.tempo.compareTo(outro.tempo);
    }

    public enum Prioridade {
        ALTA, MEDIA, BAIXA;
    }

    public enum TipoPagamento {
        PIX, CARTAO, BOLETO;
    }

    // Getters e Setters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public TipoPagamento getTipoPagamento() { return tipoPagamento; }
    public double getValor() { return valor; }
    public Timestamp getTempo() { return tempo; }
    public Prioridade getPrioridade() { return prioridade; }
    
    @Override
    public String toString() {
        return String.format("Pagamento{id=%d, cliente=%s, valor=%.2f, prioridade=%s, tipo=%s}", 
                           id, nome, valor, prioridade, tipoPagamento);
    }
}