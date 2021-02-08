package com.example.agendarecyclerhorizontal;

import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.view.ActionMode;

import com.google.android.material.snackbar.Snackbar;

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

                for (int i : usuariosSeleccionados) MainActivity.daoUsuario.eliminaRegistro(FragmentUsuario.usuarios.get(i).getId());

                FragmentUsuario.listAdapter.eliminarItemsSeleccionados(FragmentUsuario.listAdapter.getSelectedItems());
                onDestroyActionMode(mode);
                return true;
            case R.id.cancelar:
                FragmentUsuario.listAdapter.desactivarSeleccion();
                onDestroyActionMode(mode);
                return true;
            case R.id.posicionar:
                Snackbar.make(mainActivity.getWindow().getDecorView().getRootView(), "Está por implementar...", Snackbar.LENGTH_LONG).show();
                break;
        }

        return false;
    }
    @Override
    public void onDestroyActionMode(ActionMode mode) {
        listAdapter.clearSelection();
        listAdapter.desactivarSeleccion();
        MainActivity.fragmentUsuario.actionMode = null;
        mode.finish();
        mode = null;
    }
}
