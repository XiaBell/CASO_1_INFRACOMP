import java.util.ArrayList;

public class BuzonReproceso {

    private ArrayList<String> elementosReproceso = new ArrayList<>();

    public synchronized void agregarElemento(String elemento) {
        elementosReproceso.add(elemento);
        notifyAll(); // Notifica a los productores que hay productos para reprocesar
    }

    public synchronized ArrayList<String> getListaElementos() {
        ArrayList<String> copiaElementos = new ArrayList<>(elementosReproceso);
        notifyAll();
        return copiaElementos;
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

    public synchronized String obtenerElemento() throws InterruptedException {
        while (elementosReproceso.isEmpty()) {
            wait(); // Espera hasta que haya productos para reprocesar
        }
        return elementosReproceso.get(0);
    }

    public synchronized boolean hayFin() {
        try {
            if (elementosReproceso.get(0).equals("FIN")) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
