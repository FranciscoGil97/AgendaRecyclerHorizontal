package com.example.agendarecyclerhorizontal;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.view.ActionMode;

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

                FragmentUsuario.listAdapter.eliminarItemsSeleccionados(FragmentUsuario.listAdapter.getSelectedItems());
                mode.finish();
                mode=null;
                return true;
            case R.id.cancelar:
                FragmentUsuario.listAdapter.desactivarSeleccion();
                mode.finish();
                mode=null;
                Log.e("CANCELAR",(mode==null)+"");
                return true;
        }
        mode=null;
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        FragmentUsuario.listAdapter.clearSelection();
        FragmentUsuario.listAdapter.desactivarSeleccion();
        MainActivity.fragmentUsuario.actionMode = null;
        mode=null;
    }
}
