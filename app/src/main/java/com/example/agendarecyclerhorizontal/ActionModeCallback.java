package com.example.agendarecyclerhorizontal;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.view.ActionMode;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.agendarecyclerhorizontal.FragmentUsuario.listAdapter;

class ActionModeCallback implements ActionMode.Callback {
    private final MainActivity mainActivity;

    public ActionModeCallback(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_layout, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.eliminar:
                List<Integer> usuariosSeleccionados=FragmentUsuario.listAdapter.getSelectedItems();
                Collections.sort(usuariosSeleccionados);
                Collections.reverse(usuariosSeleccionados);

                for (int i : usuariosSeleccionados){
                    FragmentUsuario.daoUsuario.eliminaRegistro(FragmentUsuario.usuarios.get(i).getId());
                }
                FragmentUsuario.listAdapter.eliminarItemsSeleccionados(FragmentUsuario.listAdapter.getSelectedItems());

                mode.finish();
                mode = null;
                return true;
            case R.id.cancelar:
                FragmentUsuario.listAdapter.desactivarSeleccion();
                mode.finish();
                mode = null;
                Log.e("CANCELAR", (mode == null) + "");
                return true;
            case R.id.posicionar:
                Snackbar.make(mainActivity.getWindow().getDecorView().getRootView(), "Está por implementar...", Snackbar.LENGTH_LONG).show();
                break;
        }
        mode = null;
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        listAdapter.clearSelection();
        listAdapter.desactivarSeleccion();
        MainActivity.fragmentUsuario.actionMode = null;
        mode = null;
    }
}
