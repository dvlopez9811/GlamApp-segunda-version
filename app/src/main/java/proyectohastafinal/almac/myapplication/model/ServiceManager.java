package proyectohastafinal.almac.myapplication.model;

import com.google.gson.Gson;

import java.io.IOException;

public class ServiceManager {

    public static final String CITAS_URL = "https://proyecto-glam.firebaseio.com/Citas.json";
    public static final String USUARIO_URL = "https://proyecto-glam.firebaseio.com/usuario/";
    public static final String ESTILISTA_URL = "https://proyecto-glam.firebaseio.com/Estilista/";


    public static class BorrarCitas{

        OnResponseListener listener;

        public BorrarCitas(String id,OnResponseListener listener){
            this.listener = listener;
            HTTPSWebUtilDomi util = new HTTPSWebUtilDomi();
            try {
                Gson g = new Gson();
                String response = util.DELETErequest(CITAS_URL,g.toJson(id));
                listener.onResponse(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public interface OnResponseListener{
            void onResponse(String response);
        }
    }

    public static class BorrarCitasUsuario{

        OnResponseListener listener;

        public BorrarCitasUsuario(String idusuario,String idcita,OnResponseListener listener){
            this.listener = listener;
            HTTPSWebUtilDomi util = new HTTPSWebUtilDomi();
            try {
                Gson g = new Gson();
                String response = util.DELETErequest(USUARIO_URL+idusuario+"/citas.json",g.toJson(idcita));
                listener.onResponse(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        public interface OnResponseListener{
            void onResponse(String response);
        }
    }

    public static class BorrarCitasEstilista{

        OnResponseListener listener;

        public BorrarCitasEstilista(String idestilista,String idcita,OnResponseListener listener){
            this.listener = listener;
            HTTPSWebUtilDomi util = new HTTPSWebUtilDomi();
            try {
                Gson g = new Gson();
                String response = util.DELETErequest(ESTILISTA_URL+idestilista+"/citas.json",g.toJson(idcita));
                listener.onResponse(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        public interface OnResponseListener{
            void onResponse(String response);
        }
    }

    public static class BorrarHorarioEstilista{

        OnResponseListener listener;

        public BorrarHorarioEstilista(String idestilista,String fecha,int hora,OnResponseListener listener){
            this.listener = listener;
            HTTPSWebUtilDomi util = new HTTPSWebUtilDomi();
            try {
                Gson g = new Gson();
                String response = util.DELETErequest(ESTILISTA_URL+idestilista+"/agenda/+"+fecha+"/horas.json",g.toJson(hora));
                listener.onResponse(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        public interface OnResponseListener{
            void onResponse(String response);
        }
    }

}
