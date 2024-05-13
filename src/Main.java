import java.util.Scanner;


public class Main {
    public static void main(String[] args) {

        Sistema sistema = new Sistema();
        sistema.cargarInformacionDesdeBD();
        mostrarMenuBusqueda(sistema);
    }
private static void mostrarMenuBusqueda(Sistema sistema) {
    Scanner scanner = new Scanner(System.in);
    int opcion;
    String continuar;

    do {
        System.out.println("¿Qué tipo de búsqueda deseas realizar?");
        System.out.println("1. Búsqueda por país");
        System.out.println("2. Búsqueda por estado");
        System.out.println("3. Búsqueda por municipio");
        System.out.println("4. Salir");

        opcion = scanner.nextInt();

        switch (opcion) {
            case 1:
                System.out.println("Ingrese el ISO code del país:");
                String isoCode = scanner.next();
                Pais pais = sistema.buscarPais(isoCode);
                if (pais != null) {
                    System.out.println("País encontrado: " + pais.getNombre());
                } else {
                    System.out.println("País no encontrado.");
                }
                break;
            case 2:
                System.out.println("Ingrese el ID del estado:");
                int idEstado = scanner.nextInt();
                Estado estado = sistema.buscarEstado(idEstado);
                if (estado != null) {
                    System.out.println("Estado encontrado: " + estado.getNombre());
                } else {
                    System.out.println("Estado no encontrado.");
                }
                break;
            case 3:
                System.out.println("Ingrese el ID del municipio:");
                int idMunicipio = scanner.nextInt();
                Municipio municipio = sistema.buscarMunicipio(idMunicipio);
                if (municipio != null) {
                    System.out.println("Municipio encontrado: " + municipio.getNombre());
                } else {
                    System.out.println("Municipio no encontrado.");
                }
                break;
            case 4:
                System.out.println("Saliendo del programa...");
                return;
            default:
                System.out.println("Opción inválida.");
        }

        System.out.println("¿Desea volver al menú? (S/N)");
        continuar = scanner.next();
    } while (continuar.equalsIgnoreCase("S"));
    System.out.println("Saliendo del programa...");
}
}