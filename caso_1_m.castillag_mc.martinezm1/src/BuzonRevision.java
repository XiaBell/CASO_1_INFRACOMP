import java.util.ArrayList;

public class BuzonRevision {
    private final ArrayList<String> ElementosRevision = new ArrayList<String>();
    private final int capacidad;

    public BuzonRevision(int capacidad) {
        this.capacidad = capacidad;
    }

    //Método para agregar un producto al buzón de revisión. Responsabilidad de los trabajadores productores
    public synchronized void agregarElemento(String elemento) throws InterruptedException {
        while (ElementosRevision.size() >= capacidad) {
            wait(); // Espera hasta que haya espacio en el buzón
        }
        ElementosRevision.add(elemento);
        notifyAll(); // Notifica a los trabajadores de calidad que hay productos para revisar
        System.out.println("Hay un total de " + ElementosRevision.size() + " elementos en el buzón de revisión");
    }

    public synchronized String retirarElemento() throws InterruptedException {
        while (vacio()) {
            wait(); // Espera hasta que haya productos para revisar
        }
        ElementosRevision.remove(0);
        notifyAll(); // Notifica a los productores que hay espacio en el buzón
        return "Elemento revisado"; 
    }


    public synchronized String getElemento() {

        try{
        String elemento = ElementosRevision.get(0);
        ElementosRevision.remove(0);
        return elemento;
        }catch(Exception e){
            return null;
        }
    }

    public synchronized boolean lleno() {
        return ElementosRevision.size() >= capacidad;
    }


    public synchronized boolean vacio() {
        return ElementosRevision.size() <= 0;
    }
    
}
