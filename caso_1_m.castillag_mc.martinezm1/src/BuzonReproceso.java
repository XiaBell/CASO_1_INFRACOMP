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

    //Si vacio es false, no se puede reprocesar un nuevo producto
    public synchronized boolean vacio() {
        return elementosReproceso.isEmpty();
    }

    //Si finProceso es true, se termina el proceso. Sucede cuando se recibe un "FIN" en el buzón de revisión 
    public synchronized boolean finProceso() {
        return "FIN".equals(elementosReproceso.get(0));
    }


}