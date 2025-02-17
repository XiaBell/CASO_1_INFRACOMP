
import java.util.ArrayList;

//Depósito único con todos los productos
public class BuzonDeposito {
    private ArrayList<String> ElementosDeposito = new ArrayList<String>();

    public synchronized void agregarElemento(int idTrabajador, String elemento) {
        System.out.println("El trabajador de calidad con id " + idTrabajador + " agrega el producto " + elemento+" al depósito");
        ElementosDeposito.add(elemento);
    }

     public synchronized ArrayList<String> getElementos() {
        return ElementosDeposito;
    }

}
