import java.util.ArrayList;
import java.util.List;
import java.lang.Override;

public class Municipio {
    private int id;
    private int idEstado;
    private String nombre;


    public Municipio() {
    }


    public Municipio(int id, int idEstado, String nombre) {
        this.id = id;
        this.idEstado = idEstado;
        this.nombre = nombre;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    @Override
    public String toString() {
        return "Municipio{" +
                "id=" + id +
                ", idEstado=" + idEstado +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
