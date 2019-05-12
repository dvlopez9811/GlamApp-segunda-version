package proyectohastafinal.almac.myapplication.model;

public class Cliente {

    private String correo;
    private String nombreCompleto;
    private String contrasenha;

    public Cliente(String correo, String nombreCompleto, String contrasenha) {
        this.correo = correo;
        this.nombreCompleto = nombreCompleto;
        this.contrasenha = contrasenha;
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
}
