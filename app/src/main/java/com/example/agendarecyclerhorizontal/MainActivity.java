package com.example.agendarecyclerhorizontal;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {
//    static ArrayList<Usuario> usuarios=new ArrayList<>();
    public static FragmentUsuario fragmentUsuario;
    Button cambiarVista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_main);
        onRestart();

        NavigationView navigationView=findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

//        Usuario.getSamples(usuarios);

        FragmentTransaction FT=getSupportFragmentManager().beginTransaction();
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){

            fragmentUsuario=new FragmentUsuario(this);

            FT.add(R.id.listaContactosFragment,fragmentUsuario);
            FT.add(R.id.editarContactoFragment,new FragmentEditarUsuario());
        }
        else{
            fragmentUsuario=new FragmentUsuario(this);
            FT.replace(R.id.fragmentContainer,fragmentUsuario);
            cambiarVista=findViewById(R.id.cambiarVistaBoton);
            cambiarVista.setOnClickListener(this);
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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.familia:
                Toast.makeText(this, "familia", Toast.LENGTH_SHORT).show();
                break;
            case R.id.trabajo:
                Toast.makeText(this, "compañeros", Toast.LENGTH_SHORT).show();
                break;
            case R.id.amigo:
                Toast.makeText(this,"amigos", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==findViewById(R.id.cambiarVistaBoton).getId()){
            FragmentUsuario.listAdapter.setVistaGrid(!FragmentUsuario.listAdapter.isVistaGrid());
            FragmentUsuario.listAdapter.notifyAll();
        }

    }
}
