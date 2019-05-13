package proyectohastafinal.almac.myapplication.model;

import java.util.ArrayList;
import java.util.HashMap;

public class SalonDeBelleza {

    private String nombreCompletoDuenho;
    private String nombreSalonDeBelleza;
    private String correo;
    private String contrasenha;
    private String direccion;
    private float latitud;
    private float longitud;
    private int calificacion;
    private int personascalificadoras;



    private HashMap<String, Boolean> servicios;


    public SalonDeBelleza(String nombreCompletoDuenho, String nombreSalonDeBelleza, String correo, String contrasenha, String direccion, float latitud, float longitud) {
        this.nombreCompletoDuenho = nombreCompletoDuenho;
        this.nombreSalonDeBelleza = nombreSalonDeBelleza;
        this.correo = correo;
        this.contrasenha = contrasenha;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.calificacion = 0;
        personascalificadoras = 0;
        servicios = new HashMap<>();
    }

    public SalonDeBelleza () {}

    public String getNombreCompletoDuenho() {
        return nombreCompletoDuenho;
    }

    public void setNombreCompletoDuenho(String nombreCompletoDuenho) {
        this.nombreCompletoDuenho = nombreCompletoDuenho;
    }

    public String getNombreSalonDeBelleza() {
        return nombreSalonDeBelleza;
    }

    public void setNombreSalonDeBelleza(String nombreSalonDeBelleza) {
        this.nombreSalonDeBelleza = nombreSalonDeBelleza;
    }

    public String getContrasenha() {
        return contrasenha;
    }

    public void setContrasenha(String contrasenha) {
        this.contrasenha = contrasenha;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    public HashMap<String, Boolean> getServicios() {
        return servicios;
    }

    public void setServicios(HashMap<String, Boolean> servicios) {
        this.servicios = servicios;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public int getPersonascalificadoras() {
        return personascalificadoras;
    }

    public void setPersonascalificadoras(int personascalificadoras) {
        this.personascalificadoras = personascalificadoras;
    }
}
