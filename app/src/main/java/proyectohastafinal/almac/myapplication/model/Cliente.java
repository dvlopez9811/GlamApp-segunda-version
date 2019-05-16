package proyectohastafinal.almac.myapplication.model;

import java.util.ArrayList;

public class Cliente {

    private String correo;
    private String nombreYApellido;
    private String usuario;
    private String telefono;
    private String contrasenha;
    private ArrayList<String> citas;

    public Cliente(String correo, String usuario, String nombreYApellido, String telefono, String contrasenha, ArrayList<String> citas) {
        this.correo = correo;
        this.nombreYApellido = nombreYApellido;
        this.usuario = usuario;
        this.telefono = telefono;
        this.contrasenha = contrasenha;
        this.citas = citas;
    }

    public Cliente(String correo, String usuario, String nombreYApellido, String telefono,String contrasenha) {
        this.correo = correo;
        this.nombreYApellido = nombreYApellido;
        this.usuario = usuario;
        this.telefono = telefono;
        this.contrasenha = contrasenha;
        this.citas = new ArrayList<>();
    }

    public Cliente() {}

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombreYApellido() {
        return nombreYApellido;
    }

    public void setNombreYApellido(String nombreYApellido) {
        this.nombreYApellido = nombreYApellido;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContrasenha() {
        return contrasenha;
    }

    public void setContrasenha(String contrasenha) {
        this.contrasenha = contrasenha;
    }

    public ArrayList<String> getCitas() {
        return citas;
    }

    public void setCitas(ArrayList<String> citas) {
        this.citas = citas;
    }
}
