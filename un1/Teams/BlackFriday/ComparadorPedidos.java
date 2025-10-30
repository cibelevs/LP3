import java.util.Comparator;

class ComparadorPedidos implements Comparator<Pedido> {
    @Override
    public int compare(Pedido p1, Pedido p2) {
        return p1.compareTo(p2);
    }
}