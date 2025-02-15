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
        this.productosTotales = productosTotales;
        this.max_productos = (int) (this.productosTotales * 0.1);
    }

    @Override
    public void run() {
        while (!debeParar()) {
            synchronized (buzonRevision) {
                while (buzonRevision.vacio()) {
                    try {
                        //Trabajador de calidad espera hasta que haya productos para revisar
                        buzonRevision.wait();
                        System.out.println("Trabajador de calidad con id "+this.id+" espera hasta que haya productos para revisar");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                
                try {
                    String producto = buzonRevision.getElemento();
                    procesarProducto(producto);
                    System.out.println("Trabajador de calidad con id "+this.id+" revisa el producto "+producto);
                    buzonRevision.retirarElemento();

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

            }
            Thread.yield(); // Espera semiactiva
        }
        synchronized (buzonReproceso) {
            buzonReproceso.agregarElemento("FIN");
            buzonReproceso.notifyAll();
        }
    }

    private void procesarProducto(String producto) {
        synchronized (TrabajadorCalidad.class) {
            productosTotalesProcesados++;
        }

        productosProcesados++;
        boolean rechazar = debeRechazarProducto();

        if (rechazar) {
            productosRechazados++;
            rechazarProducto(producto);
        } else {
            aceptarProducto(producto);
        }

    }

    private static final Random random = new Random();
    private boolean debeRechazarProducto() {
        int numeroAleatorio = random.nextInt(100) + 1;
        return numeroAleatorio % 7 == 0;
    }

    private boolean debeParar() {
        synchronized (TrabajadorCalidad.class) {
            return productosTotalesProcesados >= productosTotales;
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
