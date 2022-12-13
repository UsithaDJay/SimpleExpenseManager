package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;

public class PersistanceExpenseManager extends ExpenseManager{
    private Context context;

    public PersistanceExpenseManager(Context context) {
        super();
        this.context = context;
        setup();
    }

    @Override
    public void setup() {
        TransactionDAO transactionDAO=new PersistantTransactionDAO(context);
        setTransactionsDAO(transactionDAO);
        AccountDAO accountDAO = new PersistentAccountDAO(context);
        setAccountsDAO(accountDAO);

    }
}
