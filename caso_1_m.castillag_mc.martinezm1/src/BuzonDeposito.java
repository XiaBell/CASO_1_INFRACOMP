
import java.util.ArrayList;

//Depósito único con todos los productos
public class BuzonDeposito {
    private ArrayList<String> ElementosDeposito = new ArrayList<String>();

    public synchronized void agregarElemento(String producto) {
        ElementosDeposito.add(producto);
    }

     public synchronized ArrayList<String> getElementos() {
        return ElementosDeposito;
    }

    public synchronized int getCantidadElementos() {
        return ElementosDeposito.size();
    }

}
