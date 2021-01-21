package com.example.smultiple;

import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

abstract class SeleccionableAdapter extends RecyclerView.Adapter {
    private SparseBooleanArray selectedItems;

    public SeleccionableAdapter() {
        selectedItems = new SparseBooleanArray();
    }

    boolean isSelected(int position) {
        return getSelectedItems().contains(position);
    }

    boolean toggleSelection(int position) {
        boolean seleccionado;
        if (selectedItems.get(position, false)) {//devuelve el valor boolean de si está true en
            // la position indicada y si no lo encuentra devuelve false
            selectedItems.delete(position);
            seleccionado = false;
        } else {
            selectedItems.put(position, true);
            seleccionado = true;
        }
        notifyItemChanged(position);
        return seleccionado;
    }

    void clearSelection() {
        List<Integer> selection = getSelectedItems();
        selectedItems.clear();
        for (Integer i : selection) {
            notifyItemChanged(i);
        }
    }

    int getSelectedItemCount() {
        return selectedItems.size();
    }

    List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); ++i) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }
}

