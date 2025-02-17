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
            synchronized (buzonReproceso) {
                if (seguirTrabajando()) {
                    trabajar();
                } else {
                    try {
                        buzonReproceso.wait(); // Espera hasta recibir notificación
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
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

        int nextId = productosProducidos.incrementAndGet();
        System.out.println("Producto con id "+ nextId + " generado");

        try {
            buzonRevision.agregarElemento(String.valueOf(nextId));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    // Método para reprocesar un producto
    public void reprocesarProducto() {
        try {
            String producto = buzonReproceso.retirarElemento(id);
            buzonRevision.agregarElemento(producto);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
    }
    //Sigue produciendo si el buzón de revisión no está lleno y no hay fin
    public boolean seguirTrabajando() {
        return !buzonReproceso.hayFin() && !buzonRevision.lleno();
    }
}
