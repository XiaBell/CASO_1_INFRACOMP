public class TrabajadorCalidad {

    private BuzonRevision buzonRevision;
    private BuzonReproceso buzonReproceso;
    private BuzonDeposito buzonDeposito;
    private int ElementosTotales;
    private int ElementosRevisados;

    public TrabajadorCalidad(BuzonRevision buzonRevision, BuzonReproceso buzonReproceso, BuzonDeposito buzonDeposito, int elementosTotales) {
        this.buzonRevision = buzonRevision;
        this.buzonReproceso = buzonReproceso;
        this.buzonDeposito = buzonDeposito;
        this.ElementosTotales = elementosTotales;
    }
}
