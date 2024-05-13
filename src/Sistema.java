import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Sistema {
//Se crean los mapas para almacenar los paises, estados y municipios
    private Map<String, Pais> paisesMap;
    private Map<Integer, Estado> estadosMap;
    private Map<Integer, Municipio> municipiosMap;
    private static final String URL = "jdbc:mysql://localhost:3306/catalogo_paises";
    private static final String USER = "root";
    private static final String PASSWORD = "edo3284";

//Se crea el constructor de la clase Sistema
    public Sistema() {
        this.paisesMap = new HashMap<>();
        this.estadosMap = new HashMap<>();
        this.municipiosMap = new HashMap<>();
    }

//Se crean los métodos para obtener la información de la base de datos
    private List<Pais> obtenerPaisesDesdeBD() {
        List<Pais> paises = new ArrayList<>(); //Se crea una lista de paises
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT id, nombre, iso_code, poblacion, capital FROM paises");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {  //Se recorre el ResultSet
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String isoCode = rs.getString("iso_code");
                int poblacion = rs.getInt("poblacion");
                String capital = rs.getString("capital");
                paises.add(new Pais(id, nombre, isoCode, poblacion, capital));
            }
        } catch (SQLException e) {
            e.printStackTrace(); //Se imprime la traza de la excepción
        }
        return paises; //Se retorna la lista de paises
    }

//
    private List<Estado> obtenerEstadosDesdeBD() {
        List<Estado> estados = new ArrayList<>(); //Se crea una lista de estados
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT id, id_pais, nombre FROM estados");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) { //Se recorre el ResultSet
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
        List<Municipio> municipios = new ArrayList<>(); //Se crea una lista de municipios
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

    //Se crea el método cargarInformacionDesdeBD para cargar la información de la base de datos
    public void cargarInformacionDesdeBD() {
        List<Pais> paises = obtenerPaisesDesdeBD(); //Se llama al método obtenerPaisesDesdeBD
        cargarPaises(paises); //Se llama al método cargarPaises

        List<Estado> estados = obtenerEstadosDesdeBD();
        cargarEstados(estados); //Se llama al método cargarEstados

        List<Municipio> municipios = obtenerMunicipiosDesdeBD();
        cargarMunicipios(municipios);//Se llama al método cargarMunicipios
    }

    public void cargarPaises(List<Pais> paises) { //Se crea el método cargarPaises
        for (Pais pais : paises) { //Se recorre la lista de paises
            paisesMap.put(pais.getIsoCode(), pais); //Se agrega el pais al mapa de paises
        }
    }


    public void cargarEstados(List<Estado> estados) {
        for (Estado estado : estados) {
            estadosMap.put(estado.getId(), estado); //Se agrega el estado al mapa de estados
        }
    }


    public void cargarMunicipios(List<Municipio> municipios) {
        for (Municipio municipio : municipios) {
            municipiosMap.put(municipio.getId(), municipio); //
        }
    }

    //Se crean los métodos para buscar un país, estado o municipio
    public Pais buscarPais(String isoCode) {
    long startTime = System.nanoTime(); //Se obtiene el tiempo de inicio de la búsqueda

    Pais pais = paisesMap.get(isoCode); //Se busca el país en el mapa de paises

    long endTime = System.nanoTime(); //Se obtiene el tiempo de fin de la búsqueda
    long elapsedTime = endTime - startTime; //Se calcula el tiempo de búsqueda

    System.out.println("Tiempo de búsqueda de país: " + elapsedTime / 1000000.0 + " milisegundos");

    if (pais != null) {
        System.out.println("País encontrado: " + pais.getNombre());
        boolean hasStates = false; //Variable para saber si el país tiene estados
        for (Estado estado : estadosMap.values()) { //Se recorren los estados
            if (estado.getIdPais() == pais.getId()) { //Se verifica si el estado pertenece al país
                if (!hasStates) { //Si el país tiene estados
                    System.out.println("Estados:");
                    hasStates = true;
                }
                System.out.println(" - " + estado.getNombre()); //Se imprime el nombre del estado
                boolean hasMunicipios = false; //Variable para saber si el estado tiene municipios
                for (Municipio municipio : municipiosMap.values()) { //Se recorren los municipios
                    if (municipio.getIdEstado() == estado.getId()) { //Se verifica si el municipio pertenece al estado
                        if (!hasMunicipios) {
                            System.out.println("   Municipios:");
                            hasMunicipios = true;
                        }
                        System.out.println("     - " + municipio.getNombre());
                    }
                }
            }
        }
        if (!hasStates) { //Si el país no tiene estados
            System.out.println("No se encontraron estados para este país.");
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("¿Desea exportar los resultados a un archivo Excel? (S/N)");
        String respuesta = scanner.nextLine();
        if (respuesta.equalsIgnoreCase("S")) {
            String filePath = "C:\\Users\\eduar\\Desktop\\Prueba\\resultados.xlsx"; //Se define la ruta del archivo Excel
            exportarResultadosBusqueda(filePath, pais, elapsedTime); //Se llama al método exportarResultadosBusqueda
        }

        return pais;
    } else {
        System.out.println("País no encontrado.");
        return null;
    }
}
    public Estado buscarEstado(int idEstado) {
        long startTime = System.nanoTime(); //Se obtiene el tiempo de inicio de la búsqueda

        Estado estado = estadosMap.get(idEstado); //Se busca el estado en el mapa de estados

        long endTime = System.nanoTime(); //Se obtiene el tiempo de fin de la búsqueda
        long elapsedTime = endTime - startTime; //Se calcula el tiempo de búsqueda

        System.out.println("Tiempo de búsqueda de estado: " + elapsedTime / 1000000.0 + " milisegundos");

        if (estado != null) {
            System.out.println("Estado encontrado: " + estado.getNombre());
            // Buscar el país asociado con el estado
            Pais pais = null;
            for (Pais p : paisesMap.values()) { //Se recorren los paises
                if (p.getId() == estado.getIdPais()) { //Se verifica si el país pertenece al estado
                    pais = p; //Se asigna el país
                    System.out.println("Pertenece al país: " + pais.getNombre());
                    break;
                }
            }
            // Buscar los municipios asociados con el estado
            System.out.println("Municipios:");
            for (Municipio municipio : municipiosMap.values()) { //Se recorren los municipios
                if (municipio.getIdEstado() == estado.getId()) { //Se verifica si el municipio pertenece al estado
                    System.out.println(" - " + municipio.getNombre());
                }
            }
            Scanner scanner = new Scanner(System.in);
            System.out.println("¿Desea exportar los resultados a un archivo Excel? (S/N)");
            String respuesta = scanner.nextLine();
            if (respuesta.equalsIgnoreCase("S")) {
                String filePath = "C:\\Users\\eduar\\Desktop\\Prueba\\resultadosEstado.xlsx";
                exportarResultadosBusquedaEstado(filePath, estado, pais, elapsedTime); //Se llama al método exportarResultadosBusquedaEstado
            }
        } else {
            System.out.println("Estado no encontrado.");
        }

        return estado;
    }

    public Municipio buscarMunicipio(int idMunicipio) {
        long startTime = System.nanoTime(); //Se obtiene el tiempo de inicio de la búsqueda

        Municipio municipio = municipiosMap.get(idMunicipio); //Se busca el municipio en el mapa de municipios

        long endTime = System.nanoTime(); //Se obtiene el tiempo de fin de la búsqueda
        long elapsedTime = endTime - startTime; //Se calcula el tiempo de búsqueda

        System.out.println("Tiempo de búsqueda de municipio: " + elapsedTime / 1000000.0 + " milisegundos");

        if (municipio != null) {
            System.out.println("Municipio encontrado: " + municipio.getNombre());
            // Buscar el estado asociado con el municipio
            Estado estado = null;
            for (Estado e : estadosMap.values()) { //Se recorren los estados
                if (e.getId() == municipio.getIdEstado()) {  //Se verifica si el estado pertenece al municipio
                    estado = e; //Se asigna el estado
                    System.out.println("Pertenece al estado: " + estado.getNombre());
                    break;
                }
            }
            // Buscar el país asociado con el estado
            Pais pais = null;
            if (estado != null) { //Si el estado no es nulo
                for (Pais p : paisesMap.values()) {  //Se recorren los paises
                    if (p.getId() == estado.getIdPais()) { //Se verifica si el país pertenece al estado
                        pais = p; //Se asigna el país
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
                exportarResultadosBusquedaMunicipio(filePath, municipio, estado, pais, elapsedTime); //Se llama al método exportarResultadosBusquedaMunicipio
            }
        } else {
            System.out.println("Municipio no encontrado.");
        }

        return municipio;
    }
    public void exportarResultadosBusqueda(String filePath, Pais pais, long elapsedTime) { //Se crea el método exportarResultadosBusqueda
        Workbook workbook = new XSSFWorkbook(); //Se crea un libro de Excel
        Sheet sheet = workbook.createSheet("Resultados de la búsqueda"); //Se crea una hoja de Excel

        // Crear encabezados
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("País");
        headerRow.createCell(1).setCellValue("Estado");
        headerRow.createCell(2).setCellValue("Municipio");
        headerRow.createCell(3).setCellValue("Tiempo de búsqueda (ms)");

        // Crear filas con los resultados de la búsqueda
        int rowNum = 1;
        for (Map.Entry<Integer, Estado> estadoEntry : estadosMap.entrySet()) { //Se recorren los estados
            Estado estado = estadoEntry.getValue();
            if (estado.getIdPais() == pais.getId()) {
                boolean hasMunicipios = false;
                for (Map.Entry<Integer, Municipio> municipioEntry : municipiosMap.entrySet()) {
                    Municipio municipio = municipioEntry.getValue();
                    if (municipio.getIdEstado() == estado.getId()) {
                        Row row = sheet.createRow(rowNum++); //Se crea una fila
                        row.createCell(0).setCellValue(pais.getNombre()); //Se agrega el nombre del país
                        row.createCell(1).setCellValue(estado.getNombre()); //Se agrega el nombre del estado
                        row.createCell(2).setCellValue(municipio.getNombre()); //Se agrega el nombre del municipio
                        hasMunicipios = true;
                    }
                }
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
        for (Map.Entry<Integer, Municipio> municipioEntry : municipiosMap.entrySet()) { //Se recorren los municipios
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

        try (FileOutputStream fos = new FileOutputStream(filePath)) { //Se crea el archivo Excel
            workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String toString() {
        return "Sistema{" +
                "paisesMap=" + paisesMap + //Se imprime el mapa de paises
                ", estadosMap=" + estadosMap + //Se imprime el mapa de estados
                ", municipiosMap=" + municipiosMap + //Se imprime el mapa de municipios
                '}';
    }
}
