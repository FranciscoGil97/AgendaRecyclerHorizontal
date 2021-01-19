package com.example.agendarecyclerhorizontal;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Usuario {
    private int id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;

    public Usuario() {
        id = -1;
        nombre = "";
        apellido = "";
        telefono = "";
        email = "";
    }


    public Usuario(String nombre, String apellido, String telefono, String email) {
        id=100;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
    }

    private Usuario(int id, String nombre, String apellido, String telefono, String email) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
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
        elements.add(new Usuario(1, "Adrian", "Gonzalez", "665200454", "adriangonzalez@gmail.com"));
        elements.add(new Usuario(2, "Alberto", "Garcia", "665000000", "albertogarcia@gmail.com"));
        elements.add(new Usuario(3, "Alejandro", "Llinares", "661000111", "alejandrollinares@gmail.com"));
        elements.add(new Usuario(4, "Carlos", "Marin", "665777222", "carlosMarin@gmail.com"));
        elements.add(new Usuario(5, "Roberto", "Morantes", "966919395", "robertomorantes@gmail.com"));
        elements.add(new Usuario(6, "Jesus", "Gil", "123456789", "jesusgil@gmail.com"));
        elements.add(new Usuario(7, "Ana", "Amador", "665200454", "anaamador@gmail.com"));
    }

    public void copyTo(Usuario usuario) {
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.apellido = usuario.getApellido();
        this.telefono = usuario.getTelefono();
        this.email = usuario.getEmail();
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
