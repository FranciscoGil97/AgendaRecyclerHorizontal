package com.example.agendarecyclerhorizontal;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.agendarecyclerhorizontal.utilidades.DAOUsuario;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    static ArrayList<Usuario> usuarios = new ArrayList<>();
    public static FragmentUsuario fragmentUsuario;
    public static DAOUsuario daoUsuario;
    FragmentTransaction FT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_main);
        onRestart();
        fragmentUsuario = new FragmentUsuario(this);
        FT = getSupportFragmentManager().beginTransaction();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            FT.add(R.id.listaContactosFragment, fragmentUsuario);
            FT.add(R.id.editarContactoFragment, new FragmentEditarUsuario());
        } else {
            FT.replace(R.id.fragmentContainer, fragmentUsuario);
        }
        FT.commit();
    }
    
    @Override
    public void onBackPressed() {
        if (!(getSupportFragmentManager().findFragmentById(R.id.fragmentContainer) instanceof FragmentUsuario))
            cargarFragment(new FragmentUsuario(this));
        else finish();
    }

    private void cargarFragment(Fragment f) {
        FragmentTransaction FT = getSupportFragmentManager().beginTransaction();
        FT.replace(R.id.fragmentContainer, f);
        FT.commit();
    }
}

