import java.util.Random;

public class TrabajadorCalidad extends Thread {

    private static BuzonRevision buzonRevision;
    private static BuzonReproceso buzonReproceso;
    private static BuzonDeposito buzonDeposito;
    private int id;
    private int productosProcesados;
    private int productosRechazados;
    private int max_productos;
    private static int productosTotalesProcesados = 0;
    private static int productosTotales;

    public TrabajadorCalidad(BuzonRevision buzonRevision, BuzonReproceso buzonReproceso, BuzonDeposito buzonDeposito, int id, int productosTotales) {
        TrabajadorCalidad.buzonRevision = buzonRevision;
        TrabajadorCalidad.buzonReproceso = buzonReproceso;
        TrabajadorCalidad.buzonDeposito = buzonDeposito;
        this.id = id;
        this.productosProcesados = 0;
        this.productosRechazados = 0;
        this.max_productos = (int) (productosTotales * 0.1);

    }

    public void run() {

        synchronized (buzonReproceso) {
            productosTotalesProcesados++;  
        
            if (productosTotalesProcesados == productosTotales) {  
                System.out.println("Se han revisado todos los productos necesarios.");
                buzonReproceso.agregarElemento("FIN");
                buzonReproceso.notifyAll();
            }
    }

    private boolean debeParar() {
        synchronized (TrabajadorCalidad.class) {
            boolean shouldStop = productosTotalesProcesados == productosTotales;
            if (shouldStop) {
                System.out.println("Debe parar la ejecución");
                synchronized (buzonRevision) {
                    buzonRevision.notifyAll(); // Notificar hilos en espera
                }
            }
            
            return shouldStop;
        }
    }

    private void rechazarProducto(String producto) {
        synchronized (buzonReproceso) {
            buzonReproceso.agregarElemento(producto);
            buzonReproceso.notifyAll();
        }
    }

    private void aceptarProducto(String producto) {
        synchronized (buzonDeposito) {
            buzonDeposito.agregarElemento(producto);
            buzonDeposito.notifyAll();
        }
    }
}
