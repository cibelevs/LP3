import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

class GerenciadorAssentos {
    private final int totalAssentos;
    private final Set<Integer> assentosReservados;
    private final ReentrantLock lock;
    
    public GerenciadorAssentos(int totalAssentos) {
        this.totalAssentos = totalAssentos;
        this.assentosReservados = new HashSet<>();
        this.lock = new ReentrantLock();
    }
    
    public boolean reservar(int numeroAssento, String nomeUsuario) {
        if (!isAssentoValido(numeroAssento)) {
            System.out.println("[" + nomeUsuario + "] Assento " + numeroAssento + 
                               " inválido! Escolha entre 1 e " + totalAssentos);
            return false;
        }
        
        lock.lock();
        try {
            if (assentosReservados.contains(numeroAssento)) {
                return false;
            }
            
            assentosReservados.add(numeroAssento);
            return true;
        } finally {
            lock.unlock();
        }
    }
    
    public Integer sugerirAssentoProximo(int assentoDesejado) {
        lock.lock();
        try {
            for (int distancia = 1; distancia <= totalAssentos; distancia++) {
                int direita = assentoDesejado + distancia;
                if (direita <= totalAssentos && !assentosReservados.contains(direita)) {
                    return direita;
                }
                
                int esquerda = assentoDesejado - distancia;
                if (esquerda >= 1 && !assentosReservados.contains(esquerda)) {
                    return esquerda;
                }
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    public boolean temAssentosDisponiveis() {
        lock.lock();
        try {
            return assentosReservados.size() < totalAssentos;
        } finally {
            lock.unlock();
        }
    }
    
    public int getAssentosDisponiveis() {
        lock.lock();
        try {
            return totalAssentos - assentosReservados.size();
        } finally {
            lock.unlock();
        }
    }
    
    public String getMapaAssentos() {
        lock.lock();
        try {
            StringBuilder mapa = new StringBuilder();
            for (int i = 1; i <= totalAssentos; i++) {
                if (assentosReservados.contains(i)) {
                    mapa.append("[X] ");
                } else {
                    mapa.append("[ ] ");
                }
                if (i == 5) mapa.append("  ");
            }
            return mapa.toString();
        } finally {
            lock.unlock();
        }
    }
    
    private boolean isAssentoValido(int numeroAssento) {
        return numeroAssento >= 1 && numeroAssento <= totalAssentos;
    }
    
    public int getTotalAssentos() {
        return totalAssentos;
    }
    
    public int getAssentosReservados() {
        lock.lock();
        try {
            return assentosReservados.size();
        } finally {
            lock.unlock();
        }
    }
}