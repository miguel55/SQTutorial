package com.example.sqtutorial;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private ArrayList lista;
    private TextView t1, t2, t3, t4, t5, t6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1 = (TextView) findViewById(R.id.textView);
        t2 = (TextView) findViewById(R.id.textView2);
        t3 = (TextView) findViewById(R.id.textView3);
        t4 = (TextView) findViewById(R.id.textView4);
        t5 = (TextView) findViewById(R.id.textView5);
        t6 = (TextView) findViewById(R.id.textView6);

        //Se abre la base de datos en modo lectura/escritura
        AlumnosSQLiteHelper bdAlumnos =
                new AlumnosSQLiteHelper(this, "Alumnos", null, 2);
        SQLiteDatabase dbw = bdAlumnos.getWritableDatabase();
        // Se genera el ArrayList con 3 alumnos
        lista = new ArrayList();
        lista.add("INSERT INTO Alumnos (numero,dni,nombre) " +
                "VALUES (" + 1 + ",'11111111A','Juan')");
        lista.add("INSERT INTO Alumnos (numero,dni,nombre) " +
                "VALUES (" + 2 + ",'22222222B','Antonio')");
        lista.add("INSERT INTO Alumnos (numero,dni,nombre) " +
                "VALUES (" + 3 + ",'33333333C','Jose')");

        //Se insertan los alumnos mediante una transacción, si la base de datos se ha creado con éxito
        if (lista != null && lista.size() > 0) {
            if (dbw != null) {
                dbw.beginTransaction();
                for (int i = 0; i < lista.size(); i++) {
                    dbw.execSQL((String) lista.get(i));
                }
                dbw.setTransactionSuccessful();
                dbw.endTransaction();
            }
        }

        // Consulta del DNI del alumno Juan
        String[] consultar = new String[]{"dni"};
        String[] args = new String[]{"Juan"};
        Cursor var1 = dbw.query("Alumnos", consultar, "nombre=?", args, null, null, null);

        // Lectura de la consulta mediante el cursor correspondiente
        String nombre = "", dni = "";
        Integer code = 0;
        //int numEl=var1.getCount(); // Obtención el número de elementos del cursor
        if (var1 != null){
            if (var1.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    dni = var1.getString(0);
                } while (var1.moveToNext());
            }
        }

        // Se muestra el resultado de la consulta en el cuadro de texto correspondiente
        t1.setText("DNI de Juan");
        t2.setText(dni);

        // INSERTAR, ACTUALIZAR, ELIMINAR. Método 1.
        // Inserción del alumno Otro
        dbw.execSQL("INSERT INTO Alumnos (numero,dni,nombre) VALUES (4,'44444444D','Otro')");
        // Borrado del alumno Juan
        dbw.execSQL("DELETE FROM Alumnos WHERE nombre='Juan'");
        // Actualización del DNI de Jose
        dbw.execSQL("UPDATE Alumnos SET dni='55555555E' WHERE nombre='Jose'");

        // Consulta de todos los campos del alumno Jose
        Cursor var2 = dbw.rawQuery(" SELECT numero,dni,nombre FROM Alumnos WHERE nombre='Jose' ",null);
        if (var2.moveToFirst()) {
            do {
                code = var2.getInt(0);
                dni = var2.getString(1);
                nombre = var2.getString(2);
            } while(var2.moveToNext());
        }

        // Muestra de resultados
        t3.setText("Nuevos Datos de Jose:");
        t4.setText(nombre+" "+dni+" "+ Integer.toString(code));

        // INSERTAR, ACTUALIZAR, ELIMINAR. Método 2.
        // Inserción del alumno Francisco
        ContentValues nuevo = new ContentValues();
        nuevo.put("numero",5);
        nuevo.put("dni","666666666F");
        nuevo.put("nombre", "Francisco");

        //Insertamos el registro en la base de datos
        dbw.insert("Alumnos", null, nuevo);

        // Actualización del nombre de Jose por Jose Javier
        ContentValues actualizar = new ContentValues();
        actualizar.put("nombre","Jose Javier");
        //Actualizamos el registro en la base de datos
        dbw.update("Alumnos", actualizar, "nombre='Jose'", null);

        // Borrado de Jose Javier
        dbw.delete("Alumnos", "nombre='Jose Javier'",null);

        // Consulta del alumno que se ha insertado
        Cursor var3 = dbw.rawQuery(" SELECT numero,dni,nombre FROM Alumnos WHERE nombre='Francisco'",null);
        if (var3.moveToFirst()) {
            do {
                code = var3.getInt(0);
                dni = var3.getString(1);
                nombre = var3.getString(2);
            } while(var3.moveToNext());
        }

        // Muestra de resultados
        t5.setText("Nueva Entrada, Francisco:");
        t6.setText(nombre+" "+dni+" "+Integer.toString(code));

        // Borrado de todas las entradas de la tabla Alumnos. Necesario si se quiere usar el ejemplo
        // repetidas veces sin errores
        dbw.execSQL("DELETE FROM Alumnos");
        // Vaciado de la memoria correspondiente
        dbw.execSQL("VACUUM");
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
