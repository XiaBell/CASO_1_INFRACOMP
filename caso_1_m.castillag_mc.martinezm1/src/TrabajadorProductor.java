import java.util.logging.Logger;

public class TrabajadorProductor extends Thread {

    private static final Logger logger = Logger.getLogger(TrabajadorProductor.class.getName());
    private final int id;

    public TrabajadorProductor(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        try {
            while (true) {
                synchronized (Main.buzonReproceso) {
                    if (!Main.buzonReproceso.vacio()) {
                        String productoReproceso = Main.buzonReproceso.obtenerElemento();
                        if ("FIN".equals(productoReproceso)) {
                            logger.info("Trabajador " + id + " recibió 'FIN'. Terminando thread.");
                            break;
                        }
                        String productoId = "PRODUCTO" + (System.currentTimeMillis() % 100000);
                        logger.info("Trabajador " + id + " reprocesa: " + productoId);
                        synchronized (Main.buzonRevision) {
                            while (Main.buzonRevision.lleno()) {
                                Main.buzonRevision.wait();
                            }
                            Main.buzonRevision.agregarElemento(productoReproceso);
                            Main.buzonRevision.notifyAll();
                        }
                    } else {
                        String productoId = "PRODUCTO" + (System.currentTimeMillis() % 100000);
                        logger.info("Trabajador " + id + " crea: " + productoId);
                        synchronized (Main.buzonRevision) {
                            while (Main.buzonRevision.lleno()) {
                                Main.buzonRevision.wait();
                            }
                            Main.buzonRevision.agregarElemento(productoId);
                            Main.buzonRevision.notifyAll();
                        }
                        break;
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
