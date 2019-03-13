package com.cz3002.shopfunding;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.cz3002.shopfunding.Adapter.TransactionHistoryAdapter;
import com.cz3002.shopfunding.Model.Transaction;

import java.util.ArrayList;

public class TransactionActivity extends BaseActivity {
    // RecyclerView
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

//    private GetTransactionHistoryTask mAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        super.onCreateDrawer();
        getSupportActionBar().setTitle("Transaction History");

        // Initialize RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_transaction);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // dummmy data
        Transaction t1 = new Transaction("Top up", "2019-02-01 13:00:50", 50);
        Transaction t2 = new Transaction("Contribution #001", "2019-02-01 15:30:30", -30);
        Transaction t3 = new Transaction("Top up", "2019-02-02 11:30:00", 30);
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(t1);
        transactions.add(t2);
        transactions.add(t3);

        adapter = new TransactionHistoryAdapter(transactions);
        recyclerView.setAdapter(adapter);

//        mAsyncTask = new GetBalanceTask(getApplicationContext(), this.userProfile);
//        mAsyncTask.execute((Void) null);
    }
}
