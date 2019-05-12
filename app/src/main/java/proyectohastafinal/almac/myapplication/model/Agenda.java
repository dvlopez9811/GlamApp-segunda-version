package proyectohastafinal.almac.myapplication.model;

import java.util.ArrayList;
import java.util.Date;

public class Agenda {

    private ArrayList<Cita> citas;

    public Agenda(Date horaInicio, Date horaFinal, ArrayList<Cita> citas) {
        this.citas = citas;
    }

    public Agenda(){}

    public void setCitas(ArrayList<Cita> citas) {
        this.citas = citas;
    }
}
