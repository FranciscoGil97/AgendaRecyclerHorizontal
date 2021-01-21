package com.example.smultiple;

import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;

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
                mainActivity.adaptador.eliminarItemsSeleccionados(mainActivity.adaptador.getSelectedItems());
                mode.finish();
                return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mainActivity.adaptador.clearSelection();
        mainActivity.adaptador.desactivarSeleccion();
        mainActivity.actionMode = null;
    }
}
