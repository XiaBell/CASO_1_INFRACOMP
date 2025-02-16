import java.util.concurrent.atomic.AtomicInteger;

public class TrabajadorProductor extends Thread {

    private final int id;
    private static BuzonRevision buzonRevision;
    private static BuzonReproceso buzonReproceso;
    public static  AtomicInteger productosProducidos = new AtomicInteger(0);

    public TrabajadorProductor(BuzonRevision buzonRevision, BuzonReproceso buzonReproceso, int id) {
        this.id = id;
        TrabajadorProductor.buzonRevision = buzonRevision;
        TrabajadorProductor.buzonReproceso = buzonReproceso;

    }

    @Override
    public void run() {
        while (!buzonReproceso.hayFin()) {
            if (seguirTrabajando()){
                trabajar();
            }      
        }
    }

    public void trabajar() {
        if (!buzonReproceso.vacio()) {
           reprocesarProducto();
        } else if (!buzonRevision.lleno()) {
            generarProducto();
        }
    }

    // Método para generar un producto
    public void generarProducto() {
        System.out.println("Producto con id "+ (productosProducidos.get() + 1) + " generado");

        try {
            buzonRevision.agregarElemento(String.valueOf(productosProducidos.get() + 1));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        productosProducidos.incrementAndGet();
    }
    // Método para reprocesar un producto
    public void reprocesarProducto() {
        try {
            buzonReproceso.retirarElemento(id);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
    }
    //Sigue produciendo si el buzón de revisión no está lleno y no hay fin
    public boolean seguirTrabajando() {
        return !buzonReproceso.hayFin() && !buzonRevision.lleno();
    }
}
