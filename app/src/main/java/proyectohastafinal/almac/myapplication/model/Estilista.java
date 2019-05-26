package proyectohastafinal.almac.myapplication.model;

import java.util.ArrayList;

public class Estilista {

    private String correo;
    private String nombreYApellido;
    private String usuario;
    private String telefono;
    private String contrasenha;
    private String nombreSalonDeBelleza;

    private ArrayList<Horario> horarios;
    private ArrayList<Agenda> agenda;
    private ArrayList<String> citas;

    public Estilista(String correo, String nombreYApellido, String usuario, String telefono,String contrasenha) {
        this.correo = correo;
        this.nombreYApellido = nombreYApellido;
        this.usuario = usuario;
        this.telefono = telefono;
        this.contrasenha = contrasenha;
    }

    public Estilista() {}

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

    public String getNombreSalonDeBelleza() {
        return nombreSalonDeBelleza;
    }

    public void setNombreSalonDeBelleza(String nombreSalonDeBelleza) {
        this.nombreSalonDeBelleza = nombreSalonDeBelleza;
    }

    public ArrayList<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(ArrayList<Horario> horarios) {
        this.horarios = horarios;
    }

    public ArrayList<Agenda> getAgenda() {
        return agenda;
    }

    public void setAgenda(ArrayList<Agenda> agenda) {
        this.agenda = agenda;
    }

    public ArrayList<String> getCitas() {
        return citas;
    }

    public void setCitas(ArrayList<String> citas) {
        this.citas = citas;
    }
}