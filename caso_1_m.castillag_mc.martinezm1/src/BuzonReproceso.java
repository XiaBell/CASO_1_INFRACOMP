import java.util.ArrayList;

public class BuzonReproceso {

    private ArrayList<String> elementosReproceso = new ArrayList<String>();

    public synchronized void agregarElemento(String elemento) {
        elementosReproceso.add(elemento);
        notifyAll(); // Notifica a los productores que hay productos para reprocesar
    }

    public synchronized String retirarElemento() throws InterruptedException {
        while (elementosReproceso.isEmpty()) {
            wait(); // Espera hasta que haya productos para reprocesar
        }
        return elementosReproceso.remove(0);    
    }

    public synchronized boolean vacio() {
        return elementosReproceso.isEmpty();
    }













}