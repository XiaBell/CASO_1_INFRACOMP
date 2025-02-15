import java.util.Random;

public class TrabajadorCalidad extends Thread {
    private final BuzonRevision buzonRevision;
    private final BuzonReproceso buzonReproceso;
    private final BuzonDeposito buzonDeposito;
    private int productosProcesados;
    private int productosRechazados;
    private final int max_productos;

    public TrabajadorCalidad(BuzonRevision buzonRevision, BuzonReproceso buzonReproceso, BuzonDeposito buzonDeposito) {
        Random random = new Random();
        max_productos = (int) (random.nextInt(100) * 0.1);
        this.buzonRevision = buzonRevision;
        this.buzonReproceso = buzonReproceso;
        this.buzonDeposito = buzonDeposito;
        this.productosProcesados = 0;
        this.productosRechazados = 0;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (buzonRevision) {
                while (buzonRevision.lleno()) {
                    try {
                        buzonRevision.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                String producto = null;
                try {
                    producto = buzonRevision.retirarElemento();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                if (producto != null) {
                    procesarProducto(producto);
                }
            }
            Thread.yield(); // Espera semiactiva
        }
    }

    private void procesarProducto(String producto) {
        productosProcesados++;
        if (productosRechazados >= productosProcesados * 0.10) {
            aceptarProducto(producto);
        } else {
            if (debeRechazarProducto()) {
                productosRechazados++;
                rechazarProducto(producto);
            } else {
                aceptarProducto(producto);
            }
        }
        if (productosProcesados >= max_productos) {
            synchronized (buzonReproceso) {
                buzonReproceso.agregarElemento("FIN");
                buzonReproceso.notifyAll();
            }
        }
    }

    private boolean debeRechazarProducto() {
        Random random = new Random();
        int numeroAleatorio = random.nextInt(100) + 1;
        return numeroAleatorio % 7 == 0;
    }

    private void rechazarProducto(String producto) {
        synchronized (buzonReproceso) {
            buzonReproceso.agregarElemento(producto);
            buzonReproceso.notifyAll();
        }
    }

    private void aceptarProducto(String producto) {
        synchronized (buzonDeposito) {
            buzonDeposito.AgregarElemento(producto);
            buzonDeposito.notifyAll();
        }
    }
}

