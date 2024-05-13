import java.util.ArrayList;
import java.util.List;
import java.lang.Override;
public class Estado {
    private int id;
    private int idPais;
    private String nombre;


    public Estado(int idPais, String nombre) {
        this.idPais = idPais;
        this.nombre = nombre;
    }


    public Estado(int id, int idPais, String nombre) {
        this.id = id;
        this.idPais = idPais;
        this.nombre = nombre;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getIdPais() {
        return idPais;
    }

    public void setIdPais(int idPais) {
        this.idPais = idPais;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    @Override
    public String toString() {
        return "Estado{" +
                "id=" + id +
                ", idPais=" + idPais +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
