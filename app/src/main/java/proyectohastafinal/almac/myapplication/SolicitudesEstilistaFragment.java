package proyectohastafinal.almac.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class SolicitudesEstilistaFragment extends Fragment {


    private static SolicitudesEstilistaFragment instance;

    public static SolicitudesEstilistaFragment getInstance(){
        instance = instance == null ? new SolicitudesEstilistaFragment() : instance;
        return instance;
    }

    public SolicitudesEstilistaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_solicitudes_estilista, container, false);
    }
}
