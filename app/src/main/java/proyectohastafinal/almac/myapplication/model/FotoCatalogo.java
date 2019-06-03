package proyectohastafinal.almac.myapplication.model;

public class FotoCatalogo {
    private String nombreFoto;
    private String descripcionFoto;
    private String precioFoto;
    private String servicio;
    private String nombreSalon;

    public FotoCatalogo() {
    }

    public FotoCatalogo(String nombreFoto, String descripcionFoto, String precioFoto, String servicio, String nombreSalon) {
        this.nombreFoto = nombreFoto;
        this.descripcionFoto = descripcionFoto;
        this.precioFoto = precioFoto;
        this.servicio = servicio;
        this.nombreSalon = nombreSalon;
    }

    public String getNombreFoto() {
        return nombreFoto;
    }

    public void setNombreFoto(String nombreFoto) {
        this.nombreFoto = nombreFoto;
    }

    public String getDescripcionFoto() {
        return descripcionFoto;
    }

    public void setDescripcionFoto(String descripcionFoto) {
        this.descripcionFoto = descripcionFoto;
    }

    public String getPrecioFoto() {
        return precioFoto;
    }

    public void setPrecioFoto(String precioFoto) {
        this.precioFoto = precioFoto;
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
