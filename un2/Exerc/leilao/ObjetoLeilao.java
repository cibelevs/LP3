

public class ObjetoLeilao {
    private String nome;

    public ObjetoLeilao(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "ObjetoLeilao [nome=" + nome + "]";
    }

    
}
