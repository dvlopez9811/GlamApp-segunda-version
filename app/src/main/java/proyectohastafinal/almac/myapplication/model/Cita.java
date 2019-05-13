package proyectohastafinal.almac.myapplication.model;

import java.util.Date;

public class Cita {

    //ESTADO
    public static final String FINALIZADA = "Finalizada";
    public static final String RESERVADA = "Reservada";
    public static final String APLAZADA = "Aplazada";
    public static final String RECHAZADA = "Rechazada";

    //SERVICIO
    public static final String PELUQUERIA = "Peluquería";
    public static final String UNAS = "Uñas";
    public static final String MAQUILLAJE = "Maquillaje";
    public static final String DEPILACION = "Depilación";
    public static final String MASAJE = "Masaje";


    private Date fecha;
    private String informacion;
    private String estado;
    private String horainicio;
    private String horafin;
    private String servicio;
    private String nombreSalon;


    public Cita(Date fecha, String informacion, String estado, String horainicio, String horafin,String servicio,String nombreSalon) {
        this.fecha = fecha;
        this.informacion = informacion;
        this.estado = estado;
        this.horainicio = horainicio;
        this.horafin = horafin;
        this.servicio = servicio;
        this.nombreSalon = nombreSalon;
    }

    public Cita() {

    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
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
}
