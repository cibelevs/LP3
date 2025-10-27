
public class Veiculo {
    private int id;
    private boolean prioritario;
    private boolean estacionado;

    public Veiculo(int id, boolean isPrioritario) {
        this.id = id;
        this.prioritario = isPrioritario;
        this.estacionado = false;
    }

    public int getId() {
        return this.id;
    }   

    public void setId(int id) {
        this.id = id;
    }

    public boolean getPrioritario(){
        return this.prioritario;
    }

    public void setPrioritario(boolean status){
        this.prioritario = status;
    }

    public boolean getEstacionado(){
        return this.estacionado;
    }

    public void setEstacionado(boolean status){
        this.estacionado = status;
    }



}