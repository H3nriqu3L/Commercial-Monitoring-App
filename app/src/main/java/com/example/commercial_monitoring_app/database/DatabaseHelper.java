package com.example.commercial_monitoring_app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.commercial_monitoring_app.MyApp;

public final class DatabaseHelper {
    private SQLiteDatabase db;
    private final String DATABASE_NAME = "default_db";
    private static DatabaseHelper INSTANCE;

    private final String[] SCRIPT_DATABASE_CREATE = new String[] {
            "CREATE TABLE Client (idClient INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    " name TEXT NOT NULL, email TEXT NOT NULL, phoneNumber TEXT,"+
                    " cpf TEXT NOT NULL, birthDate TEXT);",
            "INSERT INTO Client (name, email, phoneNumber, cpf, birthDate) VALUES"+
                    " ('João Silva', 'joao.silva@email.com', '37999123456', '12345678901', '1985-03-15');",
            "INSERT INTO Client (name, email, phoneNumber, cpf, birthDate) VALUES"+
                    " ('Maria Santos', 'maria.santos@email.com', '37988765432', '98765432109', '1990-07-22');",
            "INSERT INTO Client (name, email, phoneNumber, cpf, birthDate) VALUES"+
                    " ('Pedro Oliveira', 'pedro.oliveira@email.com', '37977654321', '11122233344', '1978-12-10');"
    };

    private DatabaseHelper(){
        Context ctx = MyApp.getAppContext();

        //Open db or create a new one
        db = ctx.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);

        Cursor c = null;
        try {
            c = search("sqlite_master", null, "type = 'table'", "");

            if (c.getCount() == 1) {
                for (int i = 0; i < SCRIPT_DATABASE_CREATE.length; i++) {
                    db.execSQL(SCRIPT_DATABASE_CREATE[i]);
                }
                Log.i("DATABASE", "Created database tables and populated them");
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }

        Log.i("DATABASE", "Opened connection to the database");
    }

    public long insert(String table, ContentValues values) {
        long id = db.insert(table, null, values);
        Log.i("DATABASE", "Registered record with id [" + id + "]");
        return id;
    }

    public int update(String table, ContentValues values, String where) {
        int count = db.update(table, values, where, null);
        Log.i("DATABASE", "Updated [" + count + "] records");
        return count;
    }

    public int delete(String table, String where) {
        int count = db.delete(table, where, null);
        Log.i("DATABASE", "Deleted [" + count + "] records");
        return count;
    }

    public Cursor search(String table, String columns[], String where, String orderBy) {
        Cursor c;
        if(!where.isEmpty())
            c = db.query(table, columns, where, null, null, null, orderBy);
        else
            c = db.query(table, columns, null, null, null, null, orderBy);
        Log.i("DATABASE", "Performed a search and returned [" + c.getCount() + "] records.");
        return c;
    }

    private void open() {
        Context ctx = MyApp.getAppContext();
        if(!db.isOpen()){
            // Opens the existing database
            db = ctx.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
            Log.i("DATABASE", "Opened connection to the database.");
        }else{
            Log.i("DATABASE", "Connection to the database was already open.");
        }
    }

    public static DatabaseHelper getInstance(){
        if(INSTANCE == null)
            INSTANCE = new DatabaseHelper();
        INSTANCE.open();
        return INSTANCE;
    }


    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
            Log.i("DATABASE", "Closed connection to the Database.");
        }


    }
}
