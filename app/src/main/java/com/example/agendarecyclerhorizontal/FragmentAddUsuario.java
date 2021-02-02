package com.example.agendarecyclerhorizontal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentAddUsuario extends Fragment implements View.OnClickListener {

    EditText nombreEditText,apellidoEditText, emailEditText, telefonoEditText;
    CheckBox familiaCheckBox, trabajoCheckBox, amigoCheckBox;
    Button addUsuarioButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.add_usuario, container, false);

        nombreEditText=view.findViewById(R.id.nombreEditText);
        apellidoEditText=view.findViewById(R.id.apellidoEditText);
        emailEditText=view.findViewById(R.id.emailEditText);
        telefonoEditText=view.findViewById(R.id.telefonoEditText);
        familiaCheckBox=view.findViewById(R.id.familiaCheckBox);
        trabajoCheckBox=view.findViewById(R.id.trabajoCheckBox);
        amigoCheckBox=view.findViewById(R.id.amigoCheckBox);
        addUsuarioButton=view.findViewById(R.id.addUsuarioButton);

        addUsuarioButton.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {
        Usuario u=new Usuario();

        int id=MainActivity.daoUsuario.maxId();
        u.setId(id);
        u.setNombre(nombreEditText.getText().toString());
        u.setApellido(apellidoEditText.getText().toString());
        u.setEmail(emailEditText.getText().toString());
        u.setTelefono(telefonoEditText.getText().toString());
        u.setTrabajo(trabajoCheckBox.isChecked());
        u.setFamilia(familiaCheckBox.isChecked());
        u.setAmigo(amigoCheckBox.isChecked());

        MainActivity.daoUsuario.insertaUsuario(u);
        MainActivity.usuarios.add(u);

        FragmentUsuario.listAdapter.notifyDataSetChanged();
        getActivity().onBackPressed();
    }
}
