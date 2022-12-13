package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private Context context;
    private static String TABLE_NAME="accounts";
    //columns
    private static String ACCOUNT_NUMBER="accountNumber";
    private static String BANK_NAME="bankName";
    private static String ACCOUNT_HOLDERS_NAME="holderName";
    private static String BALANCE="balance";
    private DBHandler dbHandler;
    public PersistentAccountDAO(Context context) {
        this.context = context;
        dbHandler=DBHandler.getInstance(context);
    }

    @Override
    public List<String> getAccountNumbersList() {
        SQLiteDatabase sqLiteDatabase= dbHandler.getReadableDatabase();
        String query = "Select "+ACCOUNT_NUMBER+" FROM "+TABLE_NAME+";";
        Cursor cursor = sqLiteDatabase.rawQuery(query,null,null);
        ArrayList<String> returnList= new ArrayList<String>();
        if(cursor.moveToFirst()){
            do{
                returnList.add(cursor.getString(0));
            }while(cursor.moveToNext());
        }
        sqLiteDatabase.close();
        cursor.close();
        return returnList;
    }

    @Override
    public List<Account> getAccountsList() {
        SQLiteDatabase sqLiteDatabase= dbHandler.getReadableDatabase();
        String query = "Select "+ACCOUNT_NUMBER+" FROM "+TABLE_NAME+";";
        Cursor cursor = sqLiteDatabase.rawQuery(query,null,null);
        ArrayList<Account> returnList= new ArrayList<Account>();
        if(cursor.moveToFirst()){
            do{
                Account newAccount= new Account(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getDouble(3));
                returnList.add(newAccount);
            }while(cursor.moveToNext());
        }
        sqLiteDatabase.close();
        cursor.close();
        return returnList;
    }



    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase sqLiteDatabase=dbHandler.getReadableDatabase();
        String query ="SELECT * FROM "+TABLE_NAME +" WHERE "+ACCOUNT_NUMBER+"=?";
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{accountNo}, null);
            cursor.moveToFirst();
            Account newAccount=new Account(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getDouble(3));
            cursor.close();
            sqLiteDatabase.close();
            return newAccount;
        }catch (Exception e){
            throw new InvalidAccountException("INVALID ACCOUNT NUMBER "+accountNo);
        }

    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase sqLiteDatabase=dbHandler.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ACCOUNT_NUMBER,account.getAccountNo());
        cv.put(BANK_NAME,account.getBankName());
        cv.put(ACCOUNT_HOLDERS_NAME,account.getAccountHolderName());
        cv.put(BALANCE,account.getBalance());
        sqLiteDatabase.insert(TABLE_NAME,null,cv);
        sqLiteDatabase.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase sqLiteDatabase = dbHandler.getWritableDatabase();
        int error= sqLiteDatabase.delete(TABLE_NAME,ACCOUNT_NUMBER+"=?",new String[]{accountNo});
        if (error==-1){
            throw new InvalidAccountException("ACCOUNT NUMBER INVALID"+accountNo);
        }
        sqLiteDatabase.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        ContentValues cv = new ContentValues();
        Account account= getAccount(accountNo);
        if (expenseType==ExpenseType.EXPENSE) {
            cv.put(BALANCE, account.getBalance() - amount);
        }else if (expenseType==ExpenseType.INCOME){
            cv.put(BALANCE, account.getBalance() + amount);
        }
        SQLiteDatabase sqLiteDatabase=dbHandler.getWritableDatabase();
        cv.put(ACCOUNT_HOLDERS_NAME,account.getAccountHolderName());
        cv.put(ACCOUNT_HOLDERS_NAME,account.getAccountHolderName());
        int x=sqLiteDatabase.update(TABLE_NAME,cv,ACCOUNT_NUMBER+"=?",new String[]{accountNo});
        sqLiteDatabase.close();
        if (x==-1){
            throw  new InvalidAccountException("ACCOUNT INVALID "+accountNo);
        }
    }

    public static String getCreateQuery(){
        String query="CREATE TABLE " + TABLE_NAME +" (" +
                ACCOUNT_NUMBER + " TEXT PRIMARY KEY, "+
                BANK_NAME +" TEXT, "+
                ACCOUNT_HOLDERS_NAME+" TEXT, "+
                BALANCE+" DECIMAL(20, 2));";
        return query;

    }
    public static String getDropQuery(){
        String query="DROP TABLE IF EXISTS "+TABLE_NAME+";";
        return query;
    }
}
