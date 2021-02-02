package com.example.agendarecyclerhorizontal;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FragmentEditarUsuario extends Fragment implements View.OnClickListener {
    DatosViewModel model;
    Usuario usuario;
    SwipeDetector swipeDetector = new SwipeDetector();
    FloatingActionButton fab;
    EditText nombre, apellido, email, telefono;
    CheckBox familia, trabajo, amigo;
    ArrayList<Usuario> usuarios = MainActivity.usuarios;
    int posUsuario=0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.editar_usuario, container, false);

        nombre = view.findViewById(R.id.nombreEditText);
        apellido = view.findViewById(R.id.apellidoEditText);
        email = view.findViewById(R.id.emailEditText);
        telefono = view.findViewById(R.id.telefonoEditText);
        familia = view.findViewById(R.id.familiaCheckBox);
        trabajo = view.findViewById(R.id.trabajoCheckBox);
        amigo = view.findViewById(R.id.amigoCheckBox);
        fab = view.findViewById(R.id.fabGuardar);

        usuario = new Usuario();

        model = ViewModelProviders.of(requireActivity()).get(DatosViewModel.class);
        model.getData().observe(getViewLifecycleOwner(), new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario u) {
                if (u != null) {
                    posUsuario = buscaUsuario(u);
                    nombre.setText(u.getNombre());
                    apellido.setText(u.getApellido());
                    email.setText(u.getEmail());
                    telefono.setText(u.getTelefono());
                    familia.setChecked(u.isFamilia());
                    trabajo.setChecked(u.isFamilia());
                    amigo.setChecked(u.isAmigo());
                    usuario.copyTo(u);
                }
            }
        });

        fab.setOnClickListener(this);
        view.setOnClickListener(this);
        view.setOnTouchListener(swipeDetector);

        return view;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == fab.getId()) {
            usuario.setNombre(nombre.getText().toString());
            usuario.setApellido(apellido.getText().toString());
            usuario.setEmail(email.getText().toString());
            usuario.setTelefono(telefono.getText().toString());
            usuario.setFamilia(familia.isChecked());
            Log.e("es familiar",""+familia.isChecked());
            usuario.setTrabajo(trabajo.isChecked());
            usuario.setAmigo(amigo.isChecked());
            model.setData(usuario);
            FragmentUsuario.listAdapter.notifyItemChanged(posUsuario);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                getActivity().onBackPressed();

        } else {
            Log.e("swip Editar","se ha dectectado? "+swipeDetector.swipeDetected());
            if (swipeDetector.swipeDetected()) {
                Log.e("swip Editar","se ha dectectado");
                if (swipeDetector.getAction() == SwipeDetector.Action.LR) {
                    if (posUsuario > 0) {
                        posUsuario--;
                        actualizaVista();
                    }
                } else if (swipeDetector.getAction() == SwipeDetector.Action.RL) {
                    if (posUsuario < usuarios.size() - 1) {
                        posUsuario++;
                        actualizaVista();
                    }
                }
            }
        }
    }

    private void actualizaVista() {
        nombre.setText(usuarios.get(posUsuario).getNombre());
        apellido.setText(usuarios.get(posUsuario).getApellido());
        email.setText(usuarios.get(posUsuario).getEmail());
        telefono.setText(usuarios.get(posUsuario).getTelefono());
        familia.setChecked(usuarios.get(posUsuario).isFamilia());
        trabajo.setChecked(usuarios.get(posUsuario).isTrabajo());
        amigo.setChecked(usuarios.get(posUsuario).isAmigo());
    }

    private int buscaUsuario(Usuario u) {
        for (int i = 0; i <usuarios.size() ; i++)
            if(usuarios.get(i).getId()==u.getId())
                return i;

        return -1;
    }
}
