package un2.Exerc.Votacao;

public class SessaoAutenticacao {
    private String cpf;
    private boolean admin;
    private long Timestamp;

    public SessaoAutenticacao(String cpf, boolean admin) {
        this.cpf = cpf;
        this.admin = admin;
        this.Timestamp = System.currentTimeMillis();
    }


    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public long getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(long Timestamp) {
        this.Timestamp = Timestamp;
    }
    
}
