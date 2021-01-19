package com.example.agendarecyclerhorizontal;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final static ArrayList<Usuario> usuarios=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Usuario.getSamples(usuarios);

        FragmentTransaction FT=getSupportFragmentManager().beginTransaction();
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            FT.add(R.id.listaContactosFragment,new FragmentUsuario());
            FT.add(R.id.editarContactoFragment,new FragmentEditarUsuario());
        }
        else
            cargarFragment(new FragmentUsuario());

        FT.commit();
    }


    @Override
    public void onBackPressed(){
        if (getSupportFragmentManager().findFragmentById(R.id.fragmentContainer)instanceof FragmentEditarUsuario)
            cargarFragment(new FragmentUsuario());
        else finish();
    }

    private void cargarFragment(Fragment f) {
        FragmentTransaction FT=getSupportFragmentManager().beginTransaction();
        FT.replace(R.id.fragmentContainer,f);
        FT.commit();
    }
}
