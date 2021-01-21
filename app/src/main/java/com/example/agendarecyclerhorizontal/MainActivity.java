package com.example.agendarecyclerhorizontal;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static ArrayList<Usuario> usuarios=new ArrayList<>();
    public static FragmentUsuario fragmentUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);

        setContentView(R.layout.activity_main);
        Usuario.getSamples(usuarios);

        FragmentTransaction FT=getSupportFragmentManager().beginTransaction();
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            fragmentUsuario=new FragmentUsuario(this);
            FT.add(R.id.listaContactosFragment,fragmentUsuario);
            FT.add(R.id.editarContactoFragment,new FragmentEditarUsuario());

        }
        else{
            fragmentUsuario=new FragmentUsuario(this);
            FT.replace(R.id.fragmentContainer,fragmentUsuario);
        }

        FT.commit();

    }



    @Override
    public void onBackPressed(){
        if (getSupportFragmentManager().findFragmentById(R.id.fragmentContainer)instanceof FragmentEditarUsuario)
            cargarFragment(new FragmentUsuario(this));
        else finish();
    }

    private void cargarFragment(Fragment f) {
        FragmentTransaction FT=getSupportFragmentManager().beginTransaction();
        FT.replace(R.id.fragmentContainer,f);
        FT.commit();
    }
}
