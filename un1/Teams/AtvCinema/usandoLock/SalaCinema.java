import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class SalaCinema{
   
    List<Ingresso> lugares;
    Lock lock;
       
    public SalaCinema() {

        this.lugares = new ArrayList<>();
        iniciarSala();    
        this.lock = new ReentrantLock();
    }
    public void iniciarSala(){

        for (int i = 0; i < 10; i++) {
            lugares.add(new Ingresso(i));
        }
    }
    public void lugares(){

        try {
            lock.lock();
            for(Ingresso ingresso: lugares){
                System.out.println("acento "+ ingresso.getNumeroLugar()+" esta " + (ingresso.getDisponivel().get() ? " disponivel " : "não disponível"));
            }            
        } finally {
            lock.unlock();
        }

    }

    public void relatorioSala(Cliente cliente){

        System.out.println("o cliente "+cliente.getNomeCliente()+
        "sentou no acento"+cliente.getIngressoCliente().getNumeroLugar());
    }

    public void lugarCliente(Cliente cliente){//da um lugar para o cliente

        lugares();
         try{
            lock.lock();
            Random random = new Random();
            List<Ingresso> lugaresVagos = lugares.stream()
                            .filter(i->i.getDisponivel().get())
                            .collect(Collectors.toList());                                  
            Ingresso ingressoCliente = lugaresVagos.get( random.nextInt(0, lugaresVagos.size()));
            ingressoCliente.getDisponivel().set(false);
            cliente.setIngressoCliente(ingressoCliente); 
            //if()
        } finally {
            lock.unlock();
        }    
      
    }
    
}
