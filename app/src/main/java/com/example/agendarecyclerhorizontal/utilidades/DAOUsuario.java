package com.example.agendarecyclerhorizontal.utilidades;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.agendarecyclerhorizontal.Usuario;

import java.util.ArrayList;

public class DAOUsuario extends SQLiteOpenHelper {
    private Context context;
    private SQLiteDatabase db = null;
    ArrayList<Usuario> usuarios = new ArrayList<>();

    public DAOUsuario(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utilidades.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Utilidades.DROP_TABLE);
        onCreate(db);
    }

    public void inicializaTabla() {
        Usuario.getSamples(usuarios);
        conecta();
//        onUpgrade(db, 1, 1);
        for (Usuario u : usuarios) {
            if (!existeUsuario(u.getId())) {
                insertaUsuario(u);
            }
        }

        desconecta();
    }

    private void conecta() {
        db = this.getWritableDatabase();
    }

    public void desconecta() {
        db.close();
    }

    public Cursor getUsuarios() {
        conecta();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLE_USUARIOS, null);
        return cursor;
    }

    public void insertaUsuario(Usuario u) {
        conecta();
        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_ID, u.getId());
        values.put(Utilidades.CAMPO_NOMBRE, u.getNombre());
        values.put(Utilidades.CAMPO_APELLIDO, u.getApellido());
        values.put(Utilidades.CAMPO_EMAIL, u.getEmail());
        values.put(Utilidades.CAMPO_TELEFONO, u.getTelefono());
        values.put(Utilidades.CAMPO_FAMILA, u.isFamilia());
        values.put(Utilidades.CAMPO_AMIGO, u.isAmigo());
        values.put(Utilidades.CAMPO_TRABAJO, u.isTrabajo());

        db.insert(Utilidades.TABLE_USUARIOS, Utilidades.CAMPO_ID, values);

        desconecta();
    }

    public void actualizaRegistro(Usuario u) {
        conecta();
        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_NOMBRE, u.getNombre());
        values.put(Utilidades.CAMPO_APELLIDO, u.getApellido());
        values.put(Utilidades.CAMPO_EMAIL, u.getEmail());
        values.put(Utilidades.CAMPO_TELEFONO, u.getTelefono());
        values.put(Utilidades.CAMPO_FAMILA, u.isFamilia());
        values.put(Utilidades.CAMPO_AMIGO, u.isAmigo());
        values.put(Utilidades.CAMPO_TRABAJO, u.isTrabajo());

        db.update(Utilidades.TABLE_USUARIOS, values, Utilidades.CAMPO_ID + "=" + u.getId(), null);

        desconecta();
    }

    public void eliminaRegistro(int id) {
        conecta();

        int registrosEliminados=db.delete(Utilidades.TABLE_USUARIOS, Utilidades.CAMPO_ID + "=" + id, null);
        Toast.makeText(context, "Registros eliminados: "+registrosEliminados, Toast.LENGTH_SHORT).show();
        desconecta();
    }

    public boolean existeUsuario(int id) {
        conecta();
        boolean existe;
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLE_USUARIOS + " WHERE " + Utilidades.CAMPO_ID + "=" + id, null);
        existe = cursor.getCount() > 0;
        cursor.close();
        desconecta();
        return existe;
    }


}
