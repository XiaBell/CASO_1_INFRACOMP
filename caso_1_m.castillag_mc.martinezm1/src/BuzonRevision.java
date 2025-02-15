import java.util.ArrayList;

public class BuzonRevision {
    private final ArrayList<String> ElementosRevision = new ArrayList<String>();
    private final int capacidad;

    public BuzonRevision(int capacidad) {
        this.capacidad = capacidad;
    }

    public synchronized void agregarElemento(String elemento) throws InterruptedException {
        while (ElementosRevision.size() >= capacidad) {
            wait(); // Espera hasta que haya espacio en el buzón
        }
        ElementosRevision.add(elemento);
        notifyAll(); // Notifica a los consumidores que hay productos para revisar
    }

    public synchronized String retirarElemento() throws InterruptedException {
        while (ElementosRevision.isEmpty()) {
            wait(); // Espera hasta que haya productos para revisar
        }
        ElementosRevision.remove(0);
        notifyAll(); // Notifica a los productores que hay espacio en el buzón
        return "Elemento revisado"; 
    }

    //Si conElemento es true, no se puede producir un nuevo producto
    public synchronized boolean conElemento() {
        return ElementosRevision.size() > 0;
    }

    //Si finProceso es true, se termina el proceso. Sucede cuando se recibe un "FIN" en el buzón de revisión 
    public synchronized boolean finProceso() {
        return "FIN".equals(ElementosRevision.get(0));
    }
}
