class Cliente extends Thread {
    private final int id;
    private final Cinema cinema;

    public Cliente(int id, Cinema cinema) {
        this.id = id;
        this.cinema = cinema;
    }

    @Override
    public void run() {
        boolean reservado = false;
        while (!reservado) {
            reservado = cinema.reservarLugar(id);
            try {
                Thread.sleep(200); // simula tempo de decisão
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


