import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    System.out.println("Tiempo de búsqueda de país: " + elapsedTime / 1000000 + " milisegundos");

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
    } else {
        System.out.println("País no encontrado.");
    }

    return pais;
}

    public Estado buscarEstado(int idEstado) {
    long startTime = System.nanoTime();

    Estado estado = estadosMap.get(idEstado);

    long endTime = System.nanoTime();
    long elapsedTime = endTime - startTime;

    System.out.println("Tiempo de búsqueda de estado: " + elapsedTime / 1000000 + " milisegundos");

    if (estado != null) {
        System.out.println("Estado encontrado: " + estado.getNombre());
        // Buscar el país asociado con el estado
        for (Pais pais : paisesMap.values()) {
            if (pais.getId() == estado.getIdPais()) {
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

    System.out.println("Tiempo de búsqueda de municipio: " + elapsedTime / 1000000 + " milisegundos");

    if (municipio != null) {
        System.out.println("Municipio encontrado: " + municipio.getNombre());
        // Buscar el estado asociado con el municipio
        for (Estado estado : estadosMap.values()) {
            if (estado.getId() == municipio.getIdEstado()) {
                System.out.println("Pertenece al estado: " + estado.getNombre());
                // Buscar el país asociado con el estado
                for (Pais pais : paisesMap.values()) {
                    if (pais.getId() == estado.getIdPais()) {
                        System.out.println("Pertenece al país: " + pais.getNombre());
                        break;
                    }
                }
                break;
            }
        }
    } else {
        System.out.println("Municipio no encontrado.");
    }

    return municipio;
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
