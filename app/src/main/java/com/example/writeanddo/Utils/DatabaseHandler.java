package com.example.writeanddo.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.writeanddo.Model.TodoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                                                       + TASK + " TEXT, "
                                                                                       + STATUS + " INTEGER)";

    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // deleta tabela se existir (todo)
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        // cria novamente
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertTask(TodoModel task){ // metodo para inserir itens da lista no DB
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask()); // chave (campo) task, valor medoto get do modelo
        cv.put(STATUS, 0); // chave (compo) status, valor 0 (CHECKBOX DESMARCADA)
        db.insert(TODO_TABLE, null, cv);
    }

    public List<TodoModel> getAllTasks(){ // Medodo que passa TODOS os dados da tabela para a List taskList
        List<TodoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction(); // evita bugar a tabela em caso de o app fechar antes da passagem de dados completar
        try{
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){ //joga p topo da "pilha"
                    do{
                        TodoModel taskDB = new TodoModel();
                        taskDB.setId(cur.getInt(cur.getColumnIndex(ID)));//add range
                        taskDB.setTask(cur.getString(cur.getColumnIndex(TASK)));//add range
                        taskDB.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));//add range
                        taskList.add(taskDB);
                    }
                    while(cur.moveToNext());//ate zerar registros retorna boolean
                }
            }
        }
        finally {
            db.endTransaction();
//            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);// chave (campo) status, valor
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }

}
