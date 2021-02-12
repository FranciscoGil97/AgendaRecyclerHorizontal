package com.example.agendarecyclerhorizontal;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ActionMode;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class FragmentUsuario extends Fragment implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
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
    public static DAOUsuario daoUsuario;
    boolean vistaGrid;
    Button cambiarVista;
    FloatingActionButton fab;
    private View view;

    public FragmentUsuario(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.vistaGrid = true;
        
        daoUsuario = new DAOUsuario(mainActivity, Utilidades.DATABASE, null, 1);

//        daoUsuario.inicializaTabla();
        Cursor cursor = daoUsuario.getUsuarios();
        rellenaArrayList(cursor);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.usuarios_recycler, container, false);
        this.view=view;
        NavigationView navigationView = view.findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        fab = view.findViewById(R.id.FAB);
        cambiarVista = view.findViewById(R.id.cambiarVistaBoton);
        fab.setOnClickListener(this);
        cambiarVista.setOnClickListener(this);

        actionModeCallback = new ActionModeCallback(mainActivity);

        listAdapter = new ListAdapter(usuarios, view.getContext(), vistaGrid);
        recyclerView = view.findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
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
                            daoUsuario.actualizaRegistro(usuario);
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

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.hacerFoto:
                Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camaraIntent, 1);
                break;
            case R.id.abrirGaleria:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
                break;
            case R.id.borrarFoto:
                deleteImage();
                break;
            case R.id.cancelar:
                item.collapseActionView();
                break;
        }

        return true;
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        byte[] imagen = convertBitmaoToByteArray((Bitmap) data.getExtras().get("data"));
                        int itemSelected = FragmentUsuario.listAdapter.getItemSelected();

                        Usuario u = FragmentUsuario.usuarios.get(itemSelected);
                        u.setImagen(imagen);

                        daoUsuario.actualizaRegistro(u);
                        FragmentUsuario.listAdapter.notifyItemChanged(itemSelected);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 2:
                if (requestCode == 2) {
                    if (resultCode == Activity.RESULT_OK) {
                        if (data != null) {
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(mainActivity.getContentResolver(), data.getData());
                                bitmap = Bitmap.createScaledBitmap(bitmap, 170, 200, true);
                                byte[] imagen = convertBitmaoToByteArray(bitmap);

                                int itemSelected = FragmentUsuario.listAdapter.getItemSelected();

                                Usuario u = FragmentUsuario.usuarios.get(itemSelected);
                                u.setImagen(imagen);

                                daoUsuario.actualizaRegistro(u);
                                FragmentUsuario.listAdapter.notifyItemChanged(itemSelected);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
        }
    }

    private void deleteImage() {
        //Para eliminar la foto del contacto y que salga la imagen predeterminada
        //lo que hago es dejar vacio el campo imagen de la base de datos

        int itemSelected = FragmentUsuario.listAdapter.getItemSelected();

        Usuario u = FragmentUsuario.usuarios.get(itemSelected);
        u.setImagen(new byte[0]);

        daoUsuario.actualizaRegistro(u);
        FragmentUsuario.listAdapter.notifyItemChanged(itemSelected);
    }

    private byte[] convertBitmaoToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        return bos.toByteArray();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==cambiarVista.getId()){
            if (vistaGrid)
                recyclerView.setLayoutManager(new GridLayoutManager(mainActivity.getApplicationContext(), 2));
            else
                recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity.getApplicationContext()));

            vistaGrid = !vistaGrid;
            listAdapter.setVistaGrid(vistaGrid);
            recyclerView.setAdapter(listAdapter);
        }
        else if(v.getId()==fab.getId()){
            if (actionModeCallback != null && actionMode != null)
                actionModeCallback.onDestroyActionMode(actionMode); // quitar el menu de la toolbar antes de abrir este fragment

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, new FragmentAddUsuario());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
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
        listAdapter.setData(usuarios);
        listAdapter.notifyDataSetChanged();
        return true;
    }

    public void rellenaArrayList(Cursor cursor) {
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
            u.setImagen(cursor.getBlob(8));

            usuarios.add(u);
        }
        cursor.close();
        daoUsuario.desconecta();
    }
}
