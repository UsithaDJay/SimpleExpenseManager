package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistantTransactionDAO implements TransactionDAO {
    private Context context;
    //columns
    private static String TABLE_NAME="transactions";
    private static String ID="id";
    private static String DATE="date";
    private static String EXPENSE_Type="expenseType";
    private static String ACCOUNT_NUMBER="accountNumber";
    private static  String AMOUNT="amount";
    private DBHandler dbHandler;
    public PersistantTransactionDAO(Context context) {
        this.context = context;
        dbHandler=DBHandler.getInstance(context);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase sqLiteDatabase=dbHandler.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DATE,date.getTime());
        cv.put(EXPENSE_Type,String.valueOf(expenseType));
        cv.put(ACCOUNT_NUMBER,accountNo);
        cv.put(AMOUNT,amount);
        sqLiteDatabase.insert(TABLE_NAME,null,cv);
        sqLiteDatabase.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        String query="SELECT * FROM "+TABLE_NAME+";";
        SQLiteDatabase sqLiteDatabase = dbHandler.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query,null,null);
        ArrayList<Transaction> returnList= new ArrayList<Transaction>();
        if (cursor.moveToFirst()){
            do {
                Date date= new Date(cursor.getLong(1));
                ExpenseType expenseType= ExpenseType.valueOf(cursor.getString(2));
                Transaction newTransaction = new Transaction(date,cursor.getString(3),expenseType,cursor.getDouble(4));
                returnList.add(newTransaction);
            }while(cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return returnList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions=getAllTransactionLogs();
        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactions.subList(size - limit, size);
    }
    public static String getCreateQuery(){
        String query="CREATE TABLE " + TABLE_NAME +" (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                DATE +" TEXT, "+
                EXPENSE_Type+" TEXT, "+
                ACCOUNT_NUMBER+" TEXT, "+
                AMOUNT+" DECIMAL(20,2), "+
                "FOREIGN KEY("+ACCOUNT_NUMBER+") REFERENCES accounts(accountNumber));";
        return query;

    }
    public static String getDropQuery(){
        String query="DROP TABLE IF EXISTS "+TABLE_NAME+";";
        return query;
    }
}
