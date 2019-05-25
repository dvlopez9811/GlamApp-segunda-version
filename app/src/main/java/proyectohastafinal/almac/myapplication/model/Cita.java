package proyectohastafinal.almac.myapplication.model;

import java.util.Date;

public class Cita {

    //ESTADO
    public static final String FINALIZADA = "Finalizada";
    public static final String RESERVADA = "Reservada";
    public static final String APLAZADA = "Aplazada";
    public static final String SOLICITADA = "Solicitada";
    public static final String RECHAZADA = "Rechazada";

    //SERVICIO
    public static final String PELUQUERIA = "Peluqueria";
    public static final String UNAS = "Uñas";
    public static final String MAQUILLAJE = "Maquillaje";
    public static final String DEPILACION = "Depilacion";
    public static final String MASAJE = "Masaje";


    private String fecha;
    private String informacion;
    private String estado;
    private String horainicio;
    private String horafin;
    private String servicio;
    private String nombreSalon;
    private String telefonoCliente;
    private String telefonoEstilista;


    public Cita(String estado,String fecha,String horafin,String horainicio, String informacion, String nombreSalon,String servicio,
                String telefonoCliente,String telefonoEstilista) {
        this.fecha = fecha;
        this.informacion = informacion;
        this.estado = estado;
        this.horainicio = horainicio;
        this.horafin = horafin;
        this.servicio = servicio;
        this.nombreSalon = nombreSalon;
        this.telefonoCliente = telefonoCliente;
        this.telefonoEstilista = telefonoEstilista;
    }

    public Cita() {

    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getHorainicio() {
        return horainicio;
    }

    public void setHorainicio(String horainicio) {
        this.horainicio = horainicio;
    }

    public String getHorafin() {
        return horafin;
    }

    public void setHorafin(String horafin) {
        this.horafin = horafin;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getNombreSalon() {
        return nombreSalon;
    }

    public void setNombreSalon(String nombreSalon) {
        this.nombreSalon = nombreSalon;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public String getTelefonoEstilista() {
        return telefonoEstilista;
    }

    public void setTelefonoEstilista(String telefonoEstilista) {
        this.telefonoEstilista = telefonoEstilista;
    }
}
