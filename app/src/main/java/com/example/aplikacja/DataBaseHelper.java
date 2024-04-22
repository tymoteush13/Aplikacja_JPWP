package com.example.aplikacja;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.SQLData;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String FLASHCARDS_TABLE = "FLASHCARDS_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_WORDS = "WORDS";
    public static final String COLUMN_TRANSLATIONS = "TRANSLATIONS";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "flashcards.db", null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableStatement = "CREATE TABLE "+FLASHCARDS_TABLE+ " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_WORDS + " TEXT, " + COLUMN_TRANSLATIONS + " TEXT)";

        //String createTableStatement = "CREATE TABLE "+FLASHCARDS_TABLE+ " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_WORDS + " TEXT, " + COLUMN_WORDS + " TEXT, " + COLUMN_ACTIVE_CUSTOMER + " TEXT)";

        db.execSQL(createTableStatement);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(ModelFlashcards modelFlashcards){

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_WORDS,modelFlashcards.getWord());
        cv.put(COLUMN_TRANSLATIONS,modelFlashcards.getTranslation());


        long insert = db.insert(FLASHCARDS_TABLE, null, cv);


        if (insert == -1){
            return false;
        }else {
            return true;
        }

    }

    public List<ModelFlashcards> getEveryone(){

        List<ModelFlashcards> returnList = new ArrayList<>();


        String queryString = "SELECT * FROM "+FLASHCARDS_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString,null);

        if (cursor.moveToFirst()){

            do {
                int flashcardId = cursor.getInt(0);
                String flashcardWord = cursor.getString(1);
                String flashcardTranslation = cursor.getString(2);

                ModelFlashcards newModelFlashcards = new ModelFlashcards(flashcardWord,flashcardTranslation,flashcardId);
                returnList.add(newModelFlashcards);

            }while (cursor.moveToNext());

        }else{

        }

        cursor.close();
        db.close();
        return returnList;
    }


    public boolean deleteOne(ModelFlashcards modelFlashcards){

        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM "+FLASHCARDS_TABLE+" WHERE " + COLUMN_ID + " = " + modelFlashcards.getId();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            return true;
        }else {
            return false;
        }


    }



}
