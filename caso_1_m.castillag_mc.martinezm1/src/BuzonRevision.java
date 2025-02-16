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
        notifyAll(); // Notifica a los trabajadores de calidad que hay productos para revisar
    }

    public synchronized String retirarElemento() throws InterruptedException {
        while (ElementosRevision.isEmpty()) {
            wait(); // Espera hasta que haya productos para revisar
        }
        ElementosRevision.remove(0);
        notifyAll(); // Notifica a los productores que hay espacio en el buzón
        return "Elemento revisado"; 
    }


    public synchronized String getElemento() {
        return ElementosRevision.get(0);
    }

    public synchronized boolean lleno() {
        return ElementosRevision.size() >= capacidad;
    }


    public synchronized boolean vacio() {
        return ElementosRevision.size() <= 0;
    }
}
