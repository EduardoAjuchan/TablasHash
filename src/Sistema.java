import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Sistema {

    private Map<String, Pais> paisesMap;
    private Map<Integer, Estado> estadosMap;
    private Map<Integer, Municipio> municipiosMap;
    private static final String URL = "jdbc:mysql://localhost:3306/catalogo_paises";
    private static final String USER = "root";
    private static final String PASSWORD = "edo3284";


    public Sistema() {
        this.paisesMap = new HashMap<>();
        this.estadosMap = new HashMap<>();
        this.municipiosMap = new HashMap<>();
    }


    private List<Pais> obtenerPaisesDesdeBD() {
        List<Pais> paises = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT id, nombre, iso_code, poblacion, capital FROM paises");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String isoCode = rs.getString("iso_code");
                int poblacion = rs.getInt("poblacion");
                String capital = rs.getString("capital");
                paises.add(new Pais(id, nombre, isoCode, poblacion, capital));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paises;
    }


    private List<Estado> obtenerEstadosDesdeBD() {
        List<Estado> estados = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT id, id_pais, nombre FROM estados");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                int idPais = rs.getInt("id_pais");
                String nombre = rs.getString("nombre");
                estados.add(new Estado(id, idPais, nombre));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estados;
    }

    private List<Municipio> obtenerMunicipiosDesdeBD() {
        List<Municipio> municipios = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT id, id_estado, nombre FROM municipios");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                int idEstado = rs.getInt("id_estado");
                String nombre = rs.getString("nombre");
                municipios.add(new Municipio(id, idEstado, nombre));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return municipios;
    }

    public void cargarInformacionDesdeBD() {
        List<Pais> paises = obtenerPaisesDesdeBD();
        cargarPaises(paises);

        List<Estado> estados = obtenerEstadosDesdeBD();
        cargarEstados(estados);

        List<Municipio> municipios = obtenerMunicipiosDesdeBD();
        cargarMunicipios(municipios);
    }

    public void cargarPaises(List<Pais> paises) {
        for (Pais pais : paises) {
            paisesMap.put(pais.getIsoCode(), pais);
        }
    }


    public void cargarEstados(List<Estado> estados) {
        for (Estado estado : estados) {
            estadosMap.put(estado.getId(), estado);
        }
    }


    public void cargarMunicipios(List<Municipio> municipios) {
        for (Municipio municipio : municipios) {
            municipiosMap.put(municipio.getId(), municipio);
        }
    }

    public Pais buscarPais(String isoCode) {
    long startTime = System.nanoTime();

    Pais pais = paisesMap.get(isoCode);

    long endTime = System.nanoTime();
    long elapsedTime = endTime - startTime;

    System.out.println("Tiempo de búsqueda de país: " + elapsedTime / 1000000.0 + " milisegundos");

    if (pais != null) {
        System.out.println("País encontrado: " + pais.getNombre());
        boolean hasStates = false;
        for (Estado estado : estadosMap.values()) {
            if (estado.getIdPais() == pais.getId()) {
                if (!hasStates) {
                    System.out.println("Estados:");
                    hasStates = true;
                }
                System.out.println(" - " + estado.getNombre());
                boolean hasMunicipios = false;
                for (Municipio municipio : municipiosMap.values()) {
                    if (municipio.getIdEstado() == estado.getId()) {
                        if (!hasMunicipios) {
                            System.out.println("   Municipios:");
                            hasMunicipios = true;
                        }
                        System.out.println("     - " + municipio.getNombre());
                    }
                }
            }
        }
        if (!hasStates) {
            System.out.println("No se encontraron estados para este país.");
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("¿Desea exportar los resultados a un archivo Excel? (S/N)");
        String respuesta = scanner.nextLine();
        if (respuesta.equalsIgnoreCase("S")) {
            String filePath = "C:\\Users\\eduar\\Desktop\\Prueba\\resultados.xlsx";
            exportarResultadosBusqueda(filePath, pais);
        }

        return pais;
    } else {
        System.out.println("País no encontrado.");
        return null;
    }
}
    public Estado buscarEstado(int idEstado) {
        long startTime = System.nanoTime();

        Estado estado = estadosMap.get(idEstado);

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;

        System.out.println("Tiempo de búsqueda de estado: " + elapsedTime / 1000000.0 + " milisegundos");

        if (estado != null) {
            System.out.println("Estado encontrado: " + estado.getNombre());
            // Buscar el país asociado con el estado
            Pais pais = null;
            for (Pais p : paisesMap.values()) {
                if (p.getId() == estado.getIdPais()) {
                    pais = p;
                    System.out.println("Pertenece al país: " + pais.getNombre());
                    break;
                }
            }
            // Buscar los municipios asociados con el estado
            System.out.println("Municipios:");
            for (Municipio municipio : municipiosMap.values()) {
                if (municipio.getIdEstado() == estado.getId()) {
                    System.out.println(" - " + municipio.getNombre());
                }
            }
            Scanner scanner = new Scanner(System.in);
            System.out.println("¿Desea exportar los resultados a un archivo Excel? (S/N)");
            String respuesta = scanner.nextLine();
            if (respuesta.equalsIgnoreCase("S")) {
                String filePath = "C:\\Users\\eduar\\Desktop\\Prueba\\resultadosEstado.xlsx";
                exportarResultadosBusquedaEstado(filePath, estado, pais, elapsedTime);
            }
        } else {
            System.out.println("Estado no encontrado.");
        }

        return estado;
    }

    public Municipio buscarMunicipio(int idMunicipio) {
        long startTime = System.nanoTime();

        Municipio municipio = municipiosMap.get(idMunicipio);

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;

        System.out.println("Tiempo de búsqueda de municipio: " + elapsedTime / 1000000.0 + " milisegundos");

        if (municipio != null) {
            System.out.println("Municipio encontrado: " + municipio.getNombre());
            // Buscar el estado asociado con el municipio
            Estado estado = null;
            for (Estado e : estadosMap.values()) {
                if (e.getId() == municipio.getIdEstado()) {
                    estado = e;
                    System.out.println("Pertenece al estado: " + estado.getNombre());
                    break;
                }
            }
            // Buscar el país asociado con el estado
            Pais pais = null;
            if (estado != null) {
                for (Pais p : paisesMap.values()) {
                    if (p.getId() == estado.getIdPais()) {
                        pais = p;
                        System.out.println("Pertenece al país: " + pais.getNombre());
                        break;
                    }
                }
            }
            Scanner scanner = new Scanner(System.in);
            System.out.println("¿Desea exportar los resultados a un archivo Excel? (S/N)");
            String respuesta = scanner.nextLine();
            if (respuesta.equalsIgnoreCase("S")) {
                String filePath = "C:\\Users\\eduar\\Desktop\\Prueba\\resultadosMunicipio.xlsx";
                exportarResultadosBusquedaMunicipio(filePath, municipio, estado, pais, elapsedTime);
            }
        } else {
            System.out.println("Municipio no encontrado.");
        }

        return municipio;
    }
 public void exportarResultadosBusqueda(String filePath, Pais pais) {
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Resultados de la búsqueda");

    // Crear encabezados
    Row headerRow = sheet.createRow(0);
    headerRow.createCell(0).setCellValue("País");
    headerRow.createCell(1).setCellValue("Estado");
    headerRow.createCell(2).setCellValue("Municipio");
    headerRow.createCell(3).setCellValue("Tiempo de búsqueda (ms)");

    int rowNum = 1;
    for (Map.Entry<Integer, Estado> estadoEntry : estadosMap.entrySet()) {
        Estado estado = estadoEntry.getValue();
        if (estado.getIdPais() == pais.getId()) {
            long startTime = System.nanoTime();
            boolean hasMunicipios = false;
            for (Map.Entry<Integer, Municipio> municipioEntry : municipiosMap.entrySet()) {
                Municipio municipio = municipioEntry.getValue();
                if (municipio.getIdEstado() == estado.getId()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(pais.getNombre());
                    row.createCell(1).setCellValue(estado.getNombre());
                    row.createCell(2).setCellValue(municipio.getNombre());
                    hasMunicipios = true;
                }
            }
            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            if (!hasMunicipios) {
                // Si el estado no tiene municipios, aún así agregamos una fila para el estado
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(pais.getNombre());
                row.createCell(1).setCellValue(estado.getNombre());
                row.createCell(2).setCellValue("N/A"); // No hay municipios para este estado
            }
            // Tiempo de búsqueda para este estado
            sheet.getRow(rowNum - 1).createCell(3).setCellValue(elapsedTime / 1000000.0);
        }
    }

    try (FileOutputStream fos = new FileOutputStream(filePath)) {
        workbook.write(fos);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    public void exportarResultadosBusquedaEstado(String filePath, Estado estado, Pais pais, long elapsedTime) {
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Resultados de la búsqueda");

    // Crear encabezados
    Row headerRow = sheet.createRow(0);
    headerRow.createCell(0).setCellValue("País");
    headerRow.createCell(1).setCellValue("Estado");
    headerRow.createCell(2).setCellValue("Municipio");
    headerRow.createCell(3).setCellValue("Tiempo de búsqueda (ms)");

    int rowNum = 1;
    boolean hasMunicipios = false;
    for (Map.Entry<Integer, Municipio> municipioEntry : municipiosMap.entrySet()) {
        Municipio municipio = municipioEntry.getValue();
        if (municipio.getIdEstado() == estado.getId()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(pais.getNombre());
            row.createCell(1).setCellValue(estado.getNombre());
            row.createCell(2).setCellValue(municipio.getNombre());
            row.createCell(3).setCellValue(elapsedTime / 1000000.0); // Tiempo de búsqueda en milisegundos
            hasMunicipios = true;
        }
    }
    if (!hasMunicipios) {
        // Si el estado no tiene municipios, aún así agregamos una fila para el estado
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(pais.getNombre());
        row.createCell(1).setCellValue(estado.getNombre());
        row.createCell(2).setCellValue("N/A"); // No hay municipios para este estado
        row.createCell(3).setCellValue(elapsedTime / 1000000.0); // Tiempo de búsqueda en milisegundos
    }

    try (FileOutputStream fos = new FileOutputStream(filePath)) {
        workbook.write(fos);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    public void exportarResultadosBusquedaMunicipio(String filePath, Municipio municipio, Estado estado, Pais pais, long elapsedTime) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Resultados de la búsqueda");

        // Crear encabezados
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("País");
        headerRow.createCell(1).setCellValue("Estado");
        headerRow.createCell(2).setCellValue("Municipio");
        headerRow.createCell(3).setCellValue("Tiempo de búsqueda (ms)");

        Row row = sheet.createRow(1);
        row.createCell(0).setCellValue(pais.getNombre());
        row.createCell(1).setCellValue(estado.getNombre());
        row.createCell(2).setCellValue(municipio.getNombre());
        row.createCell(3).setCellValue(elapsedTime / 1000000.0); // Tiempo de búsqueda en milisegundos

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String toString() {
        return "Sistema{" +
                "paisesMap=" + paisesMap +
                ", estadosMap=" + estadosMap +
                ", municipiosMap=" + municipiosMap +
                '}';
    }
}
