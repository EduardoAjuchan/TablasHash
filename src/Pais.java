import java.util.ArrayList;
import java.util.List;
import java.lang.Override;
public class Pais {
    private int id;
    private String nombre;
    private String isoCode;
    private int poblacion;
    private String capital;


    public Pais(String nombre, String isoCode, int poblacion, String capital) {
        this.nombre = nombre;
        this.isoCode = isoCode;
        this.poblacion = poblacion;
        this.capital = capital;
    }


    public Pais(int id, String nombre, String isoCode, int poblacion, String capital) {
        this.id = id;
        this.nombre = nombre;
        this.isoCode = isoCode;
        this.poblacion = poblacion;
        this.capital = capital;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }


    public int getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(int poblacion) {
        this.poblacion = poblacion;
    }


    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }


    @Override
    public String toString() {
        return "País{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", isoCode='" + isoCode + '\'' +
                ", población=" + poblacion +
                ", capital='" + capital + '\'' +
                '}';
    }

}
