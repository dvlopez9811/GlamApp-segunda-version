package proyectohastafinal.almac.myapplication.model;

public class BusquedaSalonDeBelleza {

    private String nombreSalonDeBelleza;
    private String direccionSalonDeBelleza;
    private String distanciaASalonDeBelleza;

    public BusquedaSalonDeBelleza() {
    }

    public BusquedaSalonDeBelleza(String nombreSalonDeBelleza, String direccionSalonDeBelleza, String distanciaASalonDeBelleza) {
        this.nombreSalonDeBelleza = nombreSalonDeBelleza;
        this.direccionSalonDeBelleza = direccionSalonDeBelleza;
        this.distanciaASalonDeBelleza = distanciaASalonDeBelleza;
    }

    public String getNombreSalonDeBelleza() {
        return nombreSalonDeBelleza;
    }

    public void setNombreSalonDeBelleza(String nombreSalonDeBelleza) {
        this.nombreSalonDeBelleza = nombreSalonDeBelleza;
    }

    public String getDireccionSalonDeBelleza() {
        return direccionSalonDeBelleza;
    }

    public void setDireccionSalonDeBelleza(String direccionSalonDeBelleza) {
        this.direccionSalonDeBelleza = direccionSalonDeBelleza;
    }

    public String getDistanciaASalonDeBelleza() {
        return distanciaASalonDeBelleza;
    }

    public void setDistanciaASalonDeBelleza(String distanciaASalonDeBelleza) {
        this.distanciaASalonDeBelleza = distanciaASalonDeBelleza;
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof BusquedaSalonDeBelleza) {
            BusquedaSalonDeBelleza busquedaSalonDeBelleza = (BusquedaSalonDeBelleza) object;
            return this.nombreSalonDeBelleza.equals(busquedaSalonDeBelleza.getNombreSalonDeBelleza());
        }
        return false;
    }
}
