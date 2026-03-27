package eu.tutorials.financemanagement.views.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import eu.tutorials.financemanagement.R;
import eu.tutorials.financemanagement.adapters.TransactionsAdapters;
import eu.tutorials.financemanagement.models.Category;
import eu.tutorials.financemanagement.models.Transactions;
import eu.tutorials.financemanagement.utils.Constants;
import eu.tutorials.financemanagement.utils.Helper;
import eu.tutorials.financemanagement.viewmodel.MainViewModel;
import eu.tutorials.financemanagement.views.fragments.addTransactionFragement;
import eu.tutorials.financemanagement.databinding.ActivityMainBinding;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Calendar calendar;


    public MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);



        setSupportActionBar(binding.Toolbar);
        getSupportActionBar().setTitle("Transactions.");

        Constants.setCategories();

        calendar = Calendar.getInstance();
        updateDate();

        binding.nextDatebtn.setOnClickListener(c->{
            if(Constants.SELECTED_TAB ==Constants.DAILY){
                calendar.add(Calendar.DATE,1);
            }else if(Constants.SELECTED_TAB ==Constants.MONTHLY){
                calendar.add(Calendar.MONTH,1);
            }
            updateDate();
        });

        binding.previousDatebtn.setOnClickListener(c->{
            if(Constants.SELECTED_TAB == Constants.DAILY){
                calendar.add(Calendar.DATE,-1);
            }else if(Constants.SELECTED_TAB ==Constants.MONTHLY){
                calendar.add(Calendar.MONTH,-1);
            }
            updateDate();
        });

        binding.floatingActionButton2.setOnClickListener(c->{
            new addTransactionFragement().show(getSupportFragmentManager(),null);
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().equals("Monthly")){
                    Constants.SELECTED_TAB = 1;
                    updateDate();
                }else if(tab.getText().equals("Daily")){
                    Constants.SELECTED_TAB = 0;
                    updateDate();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        binding.transactionslist.setLayoutManager(new LinearLayoutManager(this));

        viewModel.transactions.observe(this, new Observer<RealmResults<Transactions>>() {
            @Override
            public void onChanged(RealmResults<Transactions> transactions) {

                TransactionsAdapters transactionsAdapters = new TransactionsAdapters(MainActivity.this,transactions);

                binding.transactionslist.setAdapter(transactionsAdapters);
                if(transactions.size()>0){
                    binding.emptyState.setVisibility(View.GONE);

                }else{
                    binding.emptyState.setVisibility(View.VISIBLE);
                }



            }
        });

        viewModel.totalIncome.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.incomeLbl.setText(String.valueOf(aDouble));

            }
        });

        viewModel.totalExpense.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.expenseLbl.setText(String.valueOf(aDouble));

            }
        });

        viewModel.totalAmount.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.totalLbl.setText(String.valueOf(aDouble));

            }
        });

        viewModel.getTransactions(calendar);





        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void getTransactions(){
        viewModel.getTransactions(calendar);

    }

    void updateDate(){
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        if(Constants.SELECTED_TAB == Constants.DAILY){
            binding.currdate.setText(Helper.formatDate(calendar.getTime()));
        }else if(Constants.SELECTED_TAB == Constants.MONTHLY){
            binding.currdate.setText(Helper.formatDateByMonth(calendar.getTime()));
        }

        viewModel.getTransactions(calendar);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}