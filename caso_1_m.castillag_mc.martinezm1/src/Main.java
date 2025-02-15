import java.util.Scanner;
import java.util.logging.Logger;

public class Main {
    public static final Logger logger = Logger.getLogger(Main.class.getName());
    public static BuzonRevision buzonRevision;
    public static BuzonReproceso buzonReproceso;
    public static BuzonDeposito buzonDeposito;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese la cantidad de operarios: ");
        int cantidadTrabajador = scanner.nextInt();
        System.out.print("Ingrese la cantidad total de productos a producir: ");
        int productosTotales = scanner.nextInt();
        System.out.print("Ingrese el límite de productos en el buzón de revisión: ");
        int limiteBuzonRevision = scanner.nextInt();

        buzonRevision = new BuzonRevision(limiteBuzonRevision);
        buzonReproceso = new BuzonReproceso();
        buzonDeposito = new BuzonDeposito();

        for (int i = 0; i < cantidadTrabajador; i++) {
            new Thread(new TrabajadorProductor(i)).start();
            new Thread(new TrabajadorCalidad(buzonRevision, buzonReproceso, buzonDeposito, i, productosTotales)).start();
        }
    }
}
