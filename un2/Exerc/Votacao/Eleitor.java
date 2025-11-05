package un2.Exerc.Votacao;

import java.io.Serializable;

public class Eleitor implements Serializable {
    private static final long serialVersionUID = 1L; //pq isso?
    private String cpf, senhaHash;
    private boolean admin;


    public Eleitor(boolean admin, String cpf, String senhaHash) {
        this.admin = admin;
        this.cpf = cpf;
        this.senhaHash = senhaHash;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    
}
