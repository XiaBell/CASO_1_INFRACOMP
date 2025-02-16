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
                String productoReproceso = null;
                synchronized (Main.buzonReproceso) {
                    if (!Main.buzonReproceso.vacio()) {
                        productoReproceso = Main.buzonReproceso.obtenerElemento();

                        if (Main.buzonReproceso.hayFin()) {
                            System.out.println(TRABAJADOR + id + " recibió 'FIN'. Terminando thread.");
                            break;
                        }
                    }
                }
                if (productoReproceso != null) {
                    System.out.println(TRABAJADOR + id + " reprocesa: " + productoReproceso);

                    synchronized (Main.buzonRevision) {
                        while (Main.buzonRevision.lleno()) {
                            Main.buzonRevision.wait();
                        }
                        Main.buzonRevision.agregarElemento(productoReproceso);
                        Main.buzonRevision.notifyAll();
                    }
                } else {
                    if (!Main.buzonReproceso.hayFin()) {
                        String productoId = "PRODUCTO" + (System.currentTimeMillis() % 100000);
                        System.out.println(TRABAJADOR + id + " crea: " + productoId);
                        synchronized (Main.buzonRevision) {
                            while (Main.buzonRevision.lleno()) {
                                Main.buzonRevision.wait();
                            }
                            Main.buzonRevision.agregarElemento(productoId);
                            Main.buzonRevision.notifyAll();
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
