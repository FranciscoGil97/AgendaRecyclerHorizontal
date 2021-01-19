package com.example.agendarecyclerhorizontal;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class FragmentUsuario extends Fragment {
    DatosViewModel model;
    static FragmentTransaction fragmentTransaction;
    RecyclerView recyclerView;
    public static ListAdapter listAdapter;
    SwipeDetector swipeDetector;
    public ArrayList<Usuario> usuarios;
    public int posicionUsuario=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.usuarios_recycler, container, false);

        usuarios = MainActivity.usuarios;

        FloatingActionButton fab = view.findViewById(R.id.FAB);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Añadir nuevo usuario", Toast.LENGTH_SHORT).show();
            }
        });

        listAdapter = new ListAdapter(usuarios, view.getContext());
        recyclerView = view.findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listAdapter);
        swipeDetector = new SwipeDetector();
        listAdapter.setOnItemTouchListener(swipeDetector);

        model = ViewModelProviders.of(requireActivity()).get(DatosViewModel.class);
        model.getData().observe(getViewLifecycleOwner(), new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                //seteamos los datos del alumno
                if (usuario != null)
                    for (int i = 0; i < usuarios.size(); i++)
                        if (usuarios.get(i).equals(usuario)){
                            usuarios.get(i).copyTo(usuario);
                            posicionUsuario=i;
                        }
            }
        });

        listAdapter.setOnItemClickListener(new ListAdapter.onClickListnerMiInterfaz() {
            @Override
            public void onItemLongClick(final int position, View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("¿Eliminar contacto " + usuarios.get(position).getNombre() + " " + usuarios.get(position).getApellido() + "?")
                        .setCancelable(true)
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                usuarios.remove(position);
                                listAdapter.notifyItemRemoved(position);
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
            }

            @Override
            public void onItemClick(final int position, View v) {
                if (swipeDetector.swipeDetected()) {
                    if (swipeDetector.getAction() == SwipeDetector.Action.LR) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setMessage("¿Llamar a " + usuarios.get(position).getNombre() + " " + usuarios.get(position).getApellido() + "?")
                                .setCancelable(true)
                                .setPositiveButton("Llamar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", usuarios.get(position).getTelefono(), null));
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .create().show();
                    } else if (swipeDetector.getAction() == SwipeDetector.Action.RL) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setMessage("¿Enviar mensaje " + usuarios.get(position).getNombre() + " " + usuarios.get(position).getApellido() + "?")
                                .setCancelable(true)
                                .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Intent.ACTION_SEND);
                                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{usuarios.get(position).getEmail()});
                                        intent.putExtra(Intent.EXTRA_SUBJECT, "Asunto email");
                                        intent.putExtra(Intent.EXTRA_TEXT, "mensaje email");
                                        intent.setType("message/rfc822");

                                        startActivity(intent);

                                    }
                                })
                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .create().show();
                    }
                } else {
                    model.setData(usuarios.get(position));
                    posicionUsuario=position;
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentContainer, new FragmentEditarUsuario());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
            }
        });
        return view;
    }
}