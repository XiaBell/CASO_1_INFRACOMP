public class TrabajadorProductor extends Thread {
    private static BuzonRevision buzonRevision;
    private static BuzonReproceso buzonReproceso;
    private final String producto;

    public TrabajadorProductor(BuzonRevision buzonRevision, BuzonReproceso buzonReproceso, String producto) {
        TrabajadorProductor.buzonRevision = buzonRevision;
        TrabajadorProductor.buzonReproceso = buzonReproceso;
        this.producto = producto;
    }

    /// Aún no aplicado todo


    public void run() {
        try {
            while (true) {
                synchronized (buzonRevision) {
                    while (buzonRevision.lleno()) {
                        buzonRevision.wait();
                    }
                    buzonRevision.agregarElemento(producto);
                    buzonRevision.notifyAll();
                }
                synchronized (buzonReproceso) {
                    while (!buzonReproceso.vacio()) {
                        buzonReproceso.wait();
                    }
                    buzonReproceso.agregarElemento(producto);
                    buzonReproceso.notifyAll();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
