
public class TrabajadorProductor {

    private final BuzonDeposito buzonDeposito;
    private BuzonReproceso buzonReproceso;
    private BuzonRevision buzonRevision;

    public TrabajadorProductor(BuzonReproceso buzonReproceso, BuzonDeposito buzonDeposito) {
        this.buzonReproceso = buzonReproceso;
        this.buzonDeposito = buzonDeposito;
    }

 

}
