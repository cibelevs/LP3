public class Cliente {
    private int id;
    private String nome;
    private float maiorLance;

    public Cliente(int id, float maiorLance, String nome) {
        this.id = id;
        this.maiorLance = maiorLance;
        this.nome = nome;
    }
    
    public int getId() {
        return this.id;
    
    }
    
    public String getNome() {
        return this.nome;
    }
    
    public void setId(int novoId){
        this.id = novoId;
    }
    
    public void setNome(String name){
        this.nome = name;
    }

    public float getMaiorLance() {
        return maiorLance;
    }

    public void setMaiorLance(float maiorLance) {
        this.maiorLance = maiorLance;
    }


}