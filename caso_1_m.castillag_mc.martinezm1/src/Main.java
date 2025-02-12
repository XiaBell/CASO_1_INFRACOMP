import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        int cantidadTrabajador = 0;
        int productosTotales = 0;
        int LímiteBuzónRevisión = 0;

        Scanner input = new Scanner(System.in);

        System.out.print("Ingrese la cantidad de operiarios de producción y de calidad: ");
        cantidadTrabajador = input.nextInt();

        System.out.println("Ingrese la cantidad de productos totales a producir: ");
        productosTotales = input.nextInt();

        System.out.println("Ingrese el límite de productos en el buzón de revisión: ");
        LímiteBuzónRevisión = input.nextInt();

        input.close();

        




    }
}