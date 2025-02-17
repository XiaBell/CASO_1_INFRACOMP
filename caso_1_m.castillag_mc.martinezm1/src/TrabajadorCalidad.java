import java.util.Random;

public class TrabajadorCalidad extends Thread {

    private static BuzonRevision buzonRevision;
    private static BuzonReproceso buzonReproceso;
    private static BuzonDeposito buzonDeposito;
    private int id;
    private int productosProcesados;
    private int productosRechazados;
    private int maximosRechazados;
    private static int productosTotalesProcesados = 0;
    private static int productosTotales;

    public TrabajadorCalidad(BuzonRevision buzonRevision, BuzonReproceso buzonReproceso, BuzonDeposito buzonDeposito, int id, int productosTotales) {
        TrabajadorCalidad.buzonRevision = buzonRevision;
        TrabajadorCalidad.buzonReproceso = buzonReproceso;
        TrabajadorCalidad.buzonDeposito = buzonDeposito;
        TrabajadorCalidad.productosTotales = productosTotales;
        this.id = id;
        this.productosProcesados = 0;
        this.productosRechazados = 0;
        this.maximosRechazados = (int) (productosTotales * 0.1);

    }

    public void run() {
        while (!debeParar()) {
            String producto = null;
            while((producto = buzonRevision.getElemento()) == null){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            //
            clasificarProducto(producto);

            // Incrementar productos procesados de forma segura
            synchronized (TrabajadorCalidad.class) {
                productosTotalesProcesados++;
            }

            Thread.yield();
            
        }

    }

    //Método para avisar al buzón de reproceso que se debe parar la ejecución
    private boolean debeParar() {
        synchronized (TrabajadorCalidad.class) {
            boolean shouldStop = productosTotalesProcesados == productosTotales;
            if (shouldStop) {
                System.out.println("Debe parar la ejecución");
                buzonReproceso.agregarElemento(id, "FIN");
                synchronized (buzonReproceso) {
                    buzonReproceso.notifyAll(); // Notificar hilos en espera
                }
            }
            
            return shouldStop;
        }
    }
    //Método para trabajar en la clasificación de productos
    private void clasificarProducto(String producto) {
        Random random = new Random();
        int probabilidad = random.nextInt(100)+1;
        if (maximosRechazados > productosRechazados && probabilidad % 7 == 0) {
            rechazarProducto(producto);
            productosRechazados++;
        } else {
            aceptarProducto(producto);
        }
    }

    // Método para rechazar un producto y enviarlo al buzón de reproceso
    private void rechazarProducto(String producto) {
        synchronized (buzonReproceso) {
            buzonReproceso.agregarElemento(id, producto);
            buzonReproceso.notifyAll();
        }
    }
    // Método para aceptar un producto y enviarlo al buzón de depósito
    private void aceptarProducto(String producto) {
        synchronized (buzonDeposito) {
            buzonDeposito.agregarElemento(id, producto);
            buzonDeposito.notifyAll();
        }
    }
}
