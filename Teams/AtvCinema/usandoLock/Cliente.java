



public class Cliente implements Runnable {

    private String nomeCliente;
    private Ingresso ingressoCliente;
    SalaCinema salaCinema;

   

    public Cliente(String nomeCliente, SalaCinema salaCinema) {
        this.nomeCliente = nomeCliente;
        this.salaCinema = salaCinema;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public Ingresso getIngressoCliente() {
        return ingressoCliente;
    }

    public void setIngressoCliente(Ingresso ingressoCliente) {
        this.ingressoCliente = ingressoCliente;
    }

    @Override
    public void run() {

        try {
             
            this.salaCinema.lugarCliente(this);
            Thread.sleep(4000);
            System.out.println("O cliente " + this.nomeCliente + " saiu da sala");

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    
}
