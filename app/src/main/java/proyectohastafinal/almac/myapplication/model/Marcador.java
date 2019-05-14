package proyectohastafinal.almac.myapplication.model;

public class Marcador {

    private double latitud;
    private double longitud;
    private String IDSalonDeBelleza;

    public Marcador() {
    }

    public Marcador(double latitud, double longitud, String IDSalonDeBelleza) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.IDSalonDeBelleza = IDSalonDeBelleza;
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

    public String getIDSalonDeBelleza() {
        return IDSalonDeBelleza;
    }

    public void setIDSalonDeBelleza(String IDSalonDeBelleza) {
        this.IDSalonDeBelleza = IDSalonDeBelleza;
    }
}
