public class TrabajadorProductor extends Thread {

    private static final String TRABAJADOR = "Trabajador ";
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
                            System.out.println(TRABAJADOR + id + " recibió 'FIN'. Terminando thread.");
                            break;
                        }
                        String productoId = "PRODUCTO" + (System.currentTimeMillis() % 100000);
                        System.out.println(TRABAJADOR + id + " reprocesa: " + productoId);
                        synchronized (Main.buzonRevision) {
                            while (Main.buzonRevision.lleno()) {
                                Main.buzonRevision.wait();
                            }
                            Main.buzonRevision.agregarElemento(productoReproceso);
                            Main.buzonRevision.notifyAll();
                        }
                    } else {
                        String productoId = "PRODUCTO" + (System.currentTimeMillis() % 100000);
                        System.out.println(TRABAJADOR + id + " crea: " + productoId);
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
