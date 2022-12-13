package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME="200269J";
    private static final int version=1;
    private static DBHandler instance=null;
    private DBHandler(@Nullable Context context) {
        super(context, DB_NAME, null, version);
    }
    public static DBHandler getInstance(@Nullable Context context){
        if (instance==null){
            instance=new DBHandler(context);
        }
        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(PersistentAccountDAO.getCreateQuery());
        sqLiteDatabase.execSQL(PersistantTransactionDAO.getCreateQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(PersistantTransactionDAO.getDropQuery());
        sqLiteDatabase.execSQL(PersistentAccountDAO.getDropQuery());
        onCreate(sqLiteDatabase);
    }
}
