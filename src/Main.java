import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {

        Sistema sistema = new Sistema();
        sistema.cargarInformacionDesdeBD();
        mostrarMenuBusqueda(sistema);
    }
    private static void mostrarMenuBusqueda(Sistema sistema) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("¿Qué tipo de búsqueda deseas realizar?");
        System.out.println("1. Búsqueda por país");
        System.out.println("2. Búsqueda por estado");
        System.out.println("3. Búsqueda por municipio");
        int opcion = scanner.nextInt();

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
            default:
                System.out.println("Opción inválida.");
        }
    }
}