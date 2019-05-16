package proyectohastafinal.almac.myapplication.model;

import android.app.Service;

public class Servicio{

    private String tipo;
    private SalonDeBelleza salonDeBelleza;
    private Estilista estilista;


    public Servicio(){}



    public Servicio(String pTipo,SalonDeBelleza salon){
        tipo=pTipo;
        salonDeBelleza=salon;
        estilista=null;


    }



    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public SalonDeBelleza getSalonDeBelleza() {
        return salonDeBelleza;
    }

    public void setSalonDeBelleza(SalonDeBelleza salonDeBelleza) {
        this.salonDeBelleza = salonDeBelleza;
    }

    public Estilista getEstilista() {
        return estilista;
    }

    public void setEstilista(Estilista estilista) {
        this.estilista = estilista;
    }



    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Servicio){
            return this.tipo.equals(((Servicio) obj).tipo);
        }
        return false;
    }
}
