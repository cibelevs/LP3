


public class Cliente {
    private int id;
    private String nome;

    public Cliente(int id, String nome){
        this.id = id;
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


}