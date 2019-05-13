package proyectohastafinal.almac.myapplication.model;

import java.util.Date;

public class Cita {

    public static final String FINALIZADA = "Finalizada";
    public static final String RESERVADA = "Reservada";
    public static final String APLAZADA = "Aplazada";
    public static final String RECHAZADA = "Rechazada";

    private Date fecha;
    private Cliente cliente;
    private String informacion;
    private String estado;
    private String horainicio;


    public Cita(Date fecha, Cliente cliente, String informacion, String estado) {
        this.fecha = fecha;
        this.cliente = cliente;
        this.informacion = informacion;
        this.estado = estado;
    }

    public Cita() {

    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
}
