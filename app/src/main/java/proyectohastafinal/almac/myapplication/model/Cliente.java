package proyectohastafinal.almac.myapplication.model;

import java.util.ArrayList;

public class Cliente {

    private String correo;
    private String nombreCompleto;
    private String contrasenha;

    private ArrayList<String> citas;

    public Cliente(String correo, String nombreCompleto, String contrasenha) {
        this.correo = correo;
        this.nombreCompleto = nombreCompleto;
        this.contrasenha = contrasenha;
        citas = new ArrayList<>();
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
}
