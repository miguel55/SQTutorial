package com.example.sqtutorial;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Miguel on 01/04/2015.
 */
public class AlumnosSQLiteHelper extends SQLiteOpenHelper {
    // Sentencia SQL para la creación de la base de datos, crea la tabla si no existe
    String creaBBDD = "CREATE TABLE IF NOT EXISTS Alumnos (numero integer primary key, dni text not null, nombre text not null)";

    // Se sobrescribe el constructor de la clase
    public AlumnosSQLiteHelper(Context contexto, String nombre,
                               SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
    //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(creaBBDD);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior,
                          int versionNueva) {
        if (versionAnterior<versionNueva) {
            //Se elimina la versión anterior de la tabla, si existe
            db.execSQL("DROP TABLE IF EXISTS Alumnos");
            //Se crea la nueva versión de la tabla
            db.execSQL(creaBBDD);
        }
    }
}

