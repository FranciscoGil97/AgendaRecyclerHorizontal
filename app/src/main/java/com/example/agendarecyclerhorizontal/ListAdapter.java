package com.example.agendarecyclerhorizontal;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListAdapter extends SeleccionableAdapter {

    private List<Usuario> mData;
    private LayoutInflater mInflater;
    private Context context;
    private onClickListnerMiInterfaz onclicklistner;

    private View.OnTouchListener listenerTouch;


    public ListAdapter(ArrayList<Usuario> itemList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
//        Log.e("ListAdapte",itemList.get(1).getNombre());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<Usuario> usuarios) {
        mData = usuarios;
    }

    @Override
    public ListAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cardview, parent, false);
        return new ListAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(final ListAdapter.Holder holder, final int position) {
        holder.bindData(mData.get(position), position);
    }

    public interface onClickListnerMiInterfaz {
        void onItemLongClick(int position, View v);

        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(onClickListnerMiInterfaz onclicklistner) {
        this.onclicklistner = onclicklistner;
    }

    public void setOnItemTouchListener(View.OnTouchListener listenerTouch) {
        this.listenerTouch = listenerTouch;
    }

    void eliminarItemsSeleccionados(List<Integer> items) {
        Collections.sort(items);
        Collections.reverse(items);
        ArrayList<Usuario> aux = new ArrayList<>(mData);
        for (int i : items) mData.remove(i);

//        mData = new ArrayList<>(aux);

        for (int i : items) notifyItemRemoved(i);
    }

    void desactivarSeleccion() {
        for (Usuario i : mData) i.setSeleccionado(false);
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener, View.OnTouchListener {
        TextView nombre, apellido, email, telefono;
        View view;

        Holder(View itemView) {
            super(itemView);
            view = itemView;
            nombre = itemView.findViewById(R.id.nombreTextView);
            apellido = itemView.findViewById(R.id.apellidoTextView);
            email = itemView.findViewById(R.id.emailTextView);
            telefono = itemView.findViewById(R.id.numeroTelefonoTextView);

            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
            ((ImageButton) itemView.findViewById(R.id.imageButton)).setOnClickListener(this);
            itemView.setOnTouchListener(this);
        }

        void bindData(final Usuario item, int i) {
            if (item.isSeleccionado())
                view.setBackgroundColor(context.getResources().getColor(R.color.oscuro, null));
            else view.setBackgroundColor(context.getResources().getColor(R.color.claro, null));
            nombre.setText(item.getNombre());
            apellido.setText(item.getApellido());
            email.setText(item.getEmail());
            telefono.setText(item.getTelefono());
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == itemView.findViewById(R.id.imageButton).getId()) {

                LayoutInflater factory = LayoutInflater.from(context);
                final View view = factory.inflate(R.layout.imagen_dialogo, null);
                ((TextView) (view.findViewById(R.id.nombreTextView))).setText(nombre.getText());
                Dialog dialog = new Dialog(context);

                dialog.setContentView(view);
                dialog.show();
            } else if (v.getId() == itemView.getId())
                onclicklistner.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            onclicklistner.onItemLongClick(getAdapterPosition(), v);
            return true;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (listenerTouch != null)
                listenerTouch.onTouch(v, event);
            return false;
        }
    }
}
