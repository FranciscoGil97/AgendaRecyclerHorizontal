package com.example.agendarecyclerhorizontal;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.view.ActionMode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendarecyclerhorizontal.utilidades.DAOUsuario;
import com.example.agendarecyclerhorizontal.utilidades.Utilidades;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;


public class FragmentUsuario extends Fragment {
    static FragmentTransaction fragmentTransaction;
    DatosViewModel model;
    public static RecyclerView recyclerView;
    public static ListAdapter listAdapter;
    SwipeDetector swipeDetector;
    public static ArrayList<Usuario> usuarios = MainActivity.usuarios;
    public int posicionUsuario = 0;
    ActionModeCallback actionModeCallback;
    public static ActionMode actionMode;
    MainActivity mainActivity;
    boolean vistaGrid;

    public FragmentUsuario(MainActivity mainActivity, boolean vistaGrid) {
        this.mainActivity = mainActivity;
        this.vistaGrid = vistaGrid;
    }
    public ListAdapter getListAdapter(){
        return listAdapter;
    }

    public void cambiaImagen(Bitmap imagen){
        View v=recyclerView.getChildAt(1);
        ((ImageButton)v.findViewById(R.id.imageButton)).setImageBitmap(imagen);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.usuarios_recycler, container, false);

        FloatingActionButton fab = view.findViewById(R.id.FAB);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, new FragmentAddUsuario());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        actionModeCallback = new ActionModeCallback(mainActivity);

        actionMode = null;

        listAdapter = new ListAdapter(usuarios, view.getContext(), vistaGrid);
        recyclerView = view.findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        if (vistaGrid) {
            recyclerView.setLayoutManager(new GridLayoutManager(mainActivity.getApplicationContext(), 2));
        } else
            recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity.getApplicationContext()));


        recyclerView.setAdapter(listAdapter);
        swipeDetector = new SwipeDetector();
        listAdapter.setOnItemTouchListener(swipeDetector);
        listAdapter.desactivarSeleccion();
        model = ViewModelProviders.of(requireActivity()).get(DatosViewModel.class);
        model.getData().observe(getViewLifecycleOwner(), new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                //seteamos los datos del alumno
                if (usuario != null)
                    for (int i = 0; i < usuarios.size(); i++)
                        if (usuarios.get(i).equals(usuario)) {
                            usuarios.get(i).copyTo(usuario);
                            mainActivity.daoUsuario.actualizaRegistro(usuario);
                            posicionUsuario = i;
                        }
            }
        });
        listAdapter.setOnItemClickListener(new ListAdapter.onClickListnerMiInterfaz() {
            @Override
            public void onItemLongClick(final int position, View v) {
                if (actionMode == null)
                    actionMode = mainActivity.startSupportActionMode(actionModeCallback);

                usuarios.get(position).setSeleccionado(intercambiarSeleccion(position));
            }

            @Override
            public void onItemClick(final int position, View v) {
                int count = listAdapter.getSelectedItemCount();

                if (count > 0 && actionMode != null) {
                    usuarios.get(position).setSeleccionado(intercambiarSeleccion(position));
                } else {
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
                        posicionUsuario = position;
                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragmentContainer, new FragmentEditarUsuario());
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    }

                }
            }
        });
        return view;
    }

    private boolean intercambiarSeleccion(int posicion) {
        boolean seleccionado = listAdapter.toggleSelection(posicion);
        int count = listAdapter.getSelectedItemCount();
        if (count == 0) {
            actionMode.finish();
            actionMode = null;
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }

        return seleccionado;
    }

}