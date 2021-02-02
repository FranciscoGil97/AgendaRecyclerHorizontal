package com.example.agendarecyclerhorizontal;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.agendarecyclerhorizontal.utilidades.DAOUsuario;
import com.example.agendarecyclerhorizontal.utilidades.Utilidades;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    static ArrayList<Usuario> usuarios = new ArrayList<>();
    public static FragmentUsuario fragmentUsuario;
    Button cambiarVista;
    public static DAOUsuario daoUsuario;
    boolean vistaGrid=false;
    FragmentTransaction FT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_main);
        onRestart();
        daoUsuario = new DAOUsuario(this, Utilidades.DATABASE, null, 1);

//        daoUsuario.inicializaTabla();
        Cursor cursor = daoUsuario.getUsuarios();
        rellenaArrayList(cursor);
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);


        FT = getSupportFragmentManager().beginTransaction();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            fragmentUsuario = new FragmentUsuario(this,vistaGrid);

            FT.add(R.id.listaContactosFragment, fragmentUsuario);
            FT.add(R.id.editarContactoFragment, new FragmentEditarUsuario());
        } else {
            fragmentUsuario = new FragmentUsuario(this,vistaGrid);
            FT.replace(R.id.fragmentContainer, fragmentUsuario);
            cambiarVista = findViewById(R.id.cambiarVistaBoton);
            cambiarVista.setOnClickListener(this);
        }

        FT.commit();

    }


    @Override
    public void onBackPressed() {
        if (!(getSupportFragmentManager().findFragmentById(R.id.fragmentContainer) instanceof FragmentUsuario))
            cargarFragment(new FragmentUsuario(this,vistaGrid));
        else finish();
    }

    private void cargarFragment(Fragment f) {
        FragmentTransaction FT = getSupportFragmentManager().beginTransaction();
        FT.replace(R.id.fragmentContainer, f);
        FT.commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Cursor cursor = null;
        switch (item.getItemId()) {
            case R.id.todos:
                cursor = daoUsuario.getUsuarios();
                break;
            case R.id.familia:
                cursor = daoUsuario.getTipoContacto(Utilidades.CAMPO_FAMILA);
                break;
            case R.id.trabajo:
                cursor = daoUsuario.getTipoContacto(Utilidades.CAMPO_TRABAJO);
                break;
            case R.id.amigo:
                cursor = daoUsuario.getTipoContacto(Utilidades.CAMPO_AMIGO);
                break;
        }
        usuarios = new ArrayList<>();
        rellenaArrayList(cursor);
        FragmentUsuario.listAdapter.setData(usuarios);
        FragmentUsuario.listAdapter.notifyDataSetChanged();
        FragmentUsuario.usuarios = usuarios;
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == findViewById(R.id.cambiarVistaBoton).getId()) {
            vistaGrid=!vistaGrid;
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, new FragmentUsuario(this, vistaGrid));
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    }
    public static void actualizaListaUsuarios(){
        Cursor cursor = daoUsuario.getUsuarios();
        rellenaArrayList(cursor);
    }
    public static  void rellenaArrayList(Cursor cursor) {
        usuarios = new ArrayList<>();
        while (cursor.moveToNext()) {
            Usuario u = new Usuario();
            u.setId(cursor.getInt(0));
            u.setNombre(cursor.getString(1));
            u.setApellido(cursor.getString(2));
            u.setEmail(cursor.getString(3));
            u.setTelefono(cursor.getString(4));
            u.setFamilia(cursor.getInt(5) > 0);
            u.setTrabajo(cursor.getInt(6) > 0);
            u.setAmigo(cursor.getInt(7) > 0);

            usuarios.add(u);
        }
        cursor.close();
        daoUsuario.desconecta();
    }
}
