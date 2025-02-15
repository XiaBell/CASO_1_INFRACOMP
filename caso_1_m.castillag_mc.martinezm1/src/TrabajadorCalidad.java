import java.util.Random;

public class TrabajadorCalidad extends Thread {
    private int productosProcesados;
    private int productosRechazados;
    private final int maxProductos;
    private final int idTrabajador;
    private final Random random;

    public TrabajadorCalidad(int maxProductos, int idTrabajador) {
        this.productosProcesados = 0;
        this.productosRechazados = 0;
        this.maxProductos = maxProductos;
        this.idTrabajador = idTrabajador;
        this.random = new Random();
    }

    @Override
    public void run() {
        while (true) {
            if (Main.buzonDeposito.getCantidadElementos() >= maxProductos) {
                synchronized (Main.buzonReproceso) {
                    Main.buzonReproceso.agregarElemento("FIN");
                    Main.buzonReproceso.notifyAll();
                }
                procesarProductosRestantes();
                return;
            }

            synchronized (Main.buzonRevision) {
                while (Main.buzonRevision.lleno()) {
                    try {
                        Main.buzonRevision.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                String producto = null;
                try {
                    producto = Main.buzonRevision.retirarElemento();
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
            if (aceptarORechazar()) {
                productosRechazados++;
                rechazarProducto(producto);
            } else {
                aceptarProducto(producto);
            }
        }
    }

    private boolean aceptarORechazar() {
        return random.nextInt(100) + 1 % 7 == 0;
    }

    private void rechazarProducto(String producto) {
        synchronized (Main.buzonReproceso) {
            Main.buzonReproceso.agregarElemento(producto);
            Main.buzonReproceso.notifyAll();
        }
        System.out.println("Trabajador " + idTrabajador + " rechazó el producto: " + producto);
    }

    private void aceptarProducto(String producto) {
        synchronized (Main.buzonDeposito) {
            Main.buzonDeposito.agregarElemento(producto);
            Main.buzonDeposito.notifyAll();
        }
        System.out.println("Trabajador " + idTrabajador + " aceptó el producto: " + producto);
    }

    private void procesarProductosRestantes() {
        while (true) {
            synchronized (Main.buzonRevision) {
                if (!Main.buzonRevision.lleno()) {
                    break;
                }
                String producto = null;
                try {
                    producto = Main.buzonRevision.retirarElemento();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                if (producto != null) {
                    procesarProducto(producto);
                }
            }
        }
    }
}
