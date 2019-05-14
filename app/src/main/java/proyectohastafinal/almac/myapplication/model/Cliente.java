package proyectohastafinal.almac.myapplication.model;

import java.util.ArrayList;

public class Cliente {

    private ArrayList<String> citas;
    private String contrasenha;
    private String correo;
    private String nombreCompleto;
    private String telefono;



    public Cliente(String contrasenha,String correo, String nombreCompleto,String telefono) {

        citas = new ArrayList<>();
        this.correo = correo;
        this.nombreCompleto = nombreCompleto;
        this.contrasenha = contrasenha;
        this.telefono = telefono;

    }

    public Cliente() {}

    public String getNombreUsuario() {
        return correo;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getContrasenha() {
        return contrasenha;
    }

    public void setNombreUsuario(String correo) {
        this.correo = correo;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setContrasenha(String contrasenha) {
        this.contrasenha = contrasenha;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public ArrayList<String> getCitas() {
        return citas;
    }

    public void setCitas(ArrayList<String> citas) {
        this.citas = citas;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
