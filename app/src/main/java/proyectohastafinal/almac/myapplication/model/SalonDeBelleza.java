package proyectohastafinal.almac.myapplication.model;

import java.util.ArrayList;
import java.util.HashMap;

public class SalonDeBelleza {

    private String nombreCompletoDuenho;
    private String nombreSalonDeBelleza;
    private String correo;
    private String contrasenha;
    private String direccion;
    private double latitud;
    private double longitud;
    private int personascalificadoras;
    private int calificacion;

    private HashMap<String, Boolean> servicios;


    private ArrayList<Servicio> serviciosArrayList;
    private ArrayList<Estilista> estilistasArrayList;

    public ArrayList<Servicio> getServiciosArrayList(){
        return serviciosArrayList;
    }

    public ArrayList<Estilista> getEstilistasArrayList(){
        return estilistasArrayList;
    }

    public void setServiciosArrayList(ArrayList<Servicio> serviciosArrayList) {
        this.serviciosArrayList = serviciosArrayList;
    }

    public void setEstilistasArrayList(ArrayList<Estilista> estilistasArrayList) {
        this.estilistasArrayList = estilistasArrayList;
    }

    public SalonDeBelleza(String nombreCompletoDuenho, String nombreSalonDeBelleza, String correo, String contrasenha, String direccion, double latitud, double longitud) {
        this.nombreCompletoDuenho = nombreCompletoDuenho;
        this.nombreSalonDeBelleza = nombreSalonDeBelleza;
        this.correo = correo;
        this.contrasenha = contrasenha;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        personascalificadoras = calificacion = 0;
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

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
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
}
