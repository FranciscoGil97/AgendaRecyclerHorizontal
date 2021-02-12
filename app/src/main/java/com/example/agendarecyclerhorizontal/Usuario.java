package com.example.agendarecyclerhorizontal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Usuario {
    private int id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private boolean seleccionado;
    private boolean familia;
    private boolean trabajo;
    private boolean amigo;
    private byte[] imagen;

    public Usuario( String nombre, String apellido, String email, String telefono, boolean seleccionado, boolean familia, boolean trabajo, boolean amigo) {
        this.id = 0;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.seleccionado = seleccionado;
        this.familia = familia;
        this.trabajo = trabajo;
        this.amigo = amigo;
        imagen=new byte[0];
    }

    public Usuario() {
        this.id = 0;
        this.nombre = "";
        this.apellido = "";
        this.email = "";
        this.telefono = "";
        this.seleccionado = false;
        this.familia = false;
        this.trabajo = false;
        this.amigo = false;
        imagen=new byte[0];
    }
    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public boolean isFamilia() {
        return familia;
    }

    public void setFamilia(boolean familia) {
        this.familia = familia;
    }

    public boolean isTrabajo() {
        return trabajo;
    }

    public void setTrabajo(boolean trabajo) {
        this.trabajo = trabajo;
    }

    public boolean isAmigo() {
        return amigo;
    }

    public void setAmigo(boolean amigo) {
        this.amigo = amigo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public static void getSamples(ArrayList<Usuario> elements) {
        elements.add(new Usuario("Adrian", "Gonzalez", "665200454", "adriangonzalez@gmail.com",false,false,false,false));
        elements.add(new Usuario("Alberto", "Garcia", "665000000", "albertogarcia@gmail.com",false,false,false,false));
        elements.add(new Usuario("Alejandro", "Llinares", "661000111", "alejandrollinares@gmail.com",false,false,false,false));
        elements.add(new Usuario("Carlos", "Marin", "665777222", "carlosMarin@gmail.com",false,false,false,false));
        elements.add(new Usuario("Roberto", "Morantes", "966919395", "robertomorantes@gmail.com",false,false,false,false));
        elements.add(new Usuario("Jesus", "Gil", "123456789", "jesusgil@gmail.com",false,false,false,false));
        elements.add(new Usuario( "Ana", "Amador", "665200454", "anaamador@gmail.com",false,false,false,false));
    }

    public void copyTo(Usuario usuario) {
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.apellido = usuario.getApellido();
        this.telefono = usuario.getTelefono();
        this.email = usuario.getEmail();
        this.familia=usuario.isFamilia();
        this.trabajo=usuario.isTrabajo();
        this.amigo=usuario.isAmigo();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        boolean isEquals = false;
        Usuario u = (Usuario) obj;
        if (u.getId() == id)
                isEquals = true;

        return isEquals;
    }
}
