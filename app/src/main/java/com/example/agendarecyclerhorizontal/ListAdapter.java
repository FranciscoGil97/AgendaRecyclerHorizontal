package com.example.agendarecyclerhorizontal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    private boolean vistaGrid;
    private int itemSelected;


    public ListAdapter(ArrayList<Usuario> itemList, Context context, boolean vistaGrid) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        itemSelected = 0;
        this.vistaGrid = vistaGrid;
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
        View view = null;
        if (vistaGrid) {
            view = mInflater.inflate(R.layout.cardview, parent, false);
        } else{
            view = mInflater.inflate(R.layout.cardview_grid, parent, false);
        }

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

        for (int i : items) mData.remove(i);
        for (int i : items) notifyItemRemoved(i);
    }

    void desactivarSeleccion() {
        for (Usuario i : mData) i.setSeleccionado(false);
    }

    public boolean isVistaGrid() {
        return vistaGrid;
    }

    public void setVistaGrid(boolean vistaGrid) {
        this.vistaGrid = vistaGrid;
    }

    public int getItemSelected() {
        return itemSelected;
    }

    public void setItemSelected(int itemSelected) {
        this.itemSelected = itemSelected;
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener, View.OnTouchListener, View.OnCreateContextMenuListener {
        TextView nombre, apellido, email, telefono;
        View view;
        public ImageView imagenContacto;


        Holder(View itemView) {
            super(itemView);
            view = itemView;
            nombre = itemView.findViewById(R.id.nombreTextView);
            apellido = itemView.findViewById(R.id.apellidoTextView);
            email = itemView.findViewById(R.id.emailTextView);
            telefono = itemView.findViewById(R.id.numeroTelefonoTextView);
            imagenContacto = itemView.findViewById(R.id.imageButton);

            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
            ((ImageButton) itemView.findViewById(R.id.imageButton)).setOnCreateContextMenuListener(this);
            itemView.setOnTouchListener(this);
        }

        public void setImagen(Bitmap imagen) {
            imagenContacto.setImageBitmap(imagen);

        }

        void bindData(final Usuario item, int i) {
            if (item.isSeleccionado())
                view.setBackgroundColor(context.getResources().getColor(R.color.oscuro, null));
            else view.setBackgroundColor(context.getResources().getColor(R.color.claro, null));
            nombre.setText(item.getNombre());
            apellido.setText(item.getApellido());
            email.setText(item.getEmail());
            telefono.setText(item.getTelefono());
            byte[] imagen=item.getImagen();
            if(imagen.length>0)
                imagenContacto.setImageBitmap(BitmapFactory.decodeByteArray(imagen, 0, imagen.length));
            else
                imagenContacto.setImageDrawable(itemView.getResources().getDrawable(R.drawable.icono_contacto));

        }

        @Override
        public void onClick(View v) {
            onclicklistner.onItemClick(getAdapterPosition(), v);
            itemSelected = getAdapterPosition();
        }


        @Override
        public boolean onLongClick(View v) {
            onclicklistner.onItemLongClick(getAdapterPosition(), v);
            itemSelected = getAdapterPosition();
            return true;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (listenerTouch != null)
                listenerTouch.onTouch(v, event);
            itemSelected = getAdapterPosition();
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {
            itemSelected = getAdapterPosition();
            MenuInflater menuInflater = new MenuInflater(v.getContext());
            menuInflater.inflate(R.menu.menu_contextual_imagen, menu);
        }
    }
}
