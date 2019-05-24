package proyectohastafinal.almac.myapplication.model;

import java.util.ArrayList;
import java.util.Date;

public class Agenda {

    private ArrayList<Horario> horarios;

    public Agenda(ArrayList<Horario> horarios) {
        this.horarios = horarios;
    }

    public Agenda(){}

    public ArrayList<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(ArrayList<Horario> horarios) {
        this.horarios = horarios;
    }
}
