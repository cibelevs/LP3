package ProdutoConsumidor;

public class Pedido {
    private int id;
    private String cliente;
    private float valor;
    private String origem;

    public Pedido(int id, String cliente, float valor, String origem) {
        this.id = id;
        this.cliente = cliente;
        this.valor = valor;
        this.origem = origem;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }



    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    

    
}
