package eu.tutorials.financemanagement.views.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import eu.tutorials.financemanagement.R;
import eu.tutorials.financemanagement.adapters.AccountsAdapter;
import eu.tutorials.financemanagement.adapters.categoryAdapters;
import eu.tutorials.financemanagement.databinding.FragmentAddTransactionFragementBinding;
import eu.tutorials.financemanagement.databinding.ListDialogBinding;
import eu.tutorials.financemanagement.models.Accounts;
import eu.tutorials.financemanagement.models.Category;
import eu.tutorials.financemanagement.models.Transactions;
import eu.tutorials.financemanagement.utils.Constants;
import eu.tutorials.financemanagement.utils.Helper;
import eu.tutorials.financemanagement.views.activities.MainActivity;


public class addTransactionFragement extends BottomSheetDialogFragment {


    public addTransactionFragement() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FragmentAddTransactionFragementBinding binding;
    Transactions transactions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddTransactionFragementBinding.inflate(inflater);

        transactions = new Transactions();
        binding.incomebtn.setOnClickListener(view->{
            binding.incomebtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
            binding.Expensebtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.Expensebtn.setTextColor(getContext().getColor(R.color.textcolor));
            binding.incomebtn.setTextColor(getContext().getColor(R.color.greencolor));

            transactions.setType(Constants.INCOME);
        });

        binding.Expensebtn.setOnClickListener(view->{
            binding.incomebtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.Expensebtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
            binding.Expensebtn.setTextColor(getContext().getColor(R.color.redcolor));
            binding.incomebtn.setTextColor(getContext().getColor(R.color.textcolor));

            transactions.setType(Constants.EXPENSE);
        });

        binding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
                datePickerDialog.setOnDateSetListener((datePicker, i, i1, i2) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH,datePicker.getDayOfMonth());
                    calendar.set(Calendar.MONTH,datePicker.getMonth());
                    calendar.set(Calendar.YEAR,datePicker.getYear());

//                    SimpleDateFormat dateformat = new SimpleDateFormat("dd MMMM yyyy");
                    String dateToShow = Helper.formatDate(calendar.getTime());

                    binding.date.setText(dateToShow);
                    transactions.setDate(calendar.getTime());
                    transactions.setId(calendar.getTime().getTime());

                });
                datePickerDialog.show();
            }
        });

        binding.Category.setOnClickListener(c->{
            ListDialogBinding dialogBinding = ListDialogBinding.inflate(inflater);
            AlertDialog categoryDialog = new AlertDialog.Builder(getContext()).create();
            categoryDialog.setView(dialogBinding.getRoot());



            categoryAdapters categoryAdapters = new categoryAdapters(getContext(), Constants.categories, new
                    categoryAdapters.CategoryClickListener() {
                        @Override
                        public void onCategoryClicked(Category category) {
                            binding.Category.setText(category.getCategoryName());
                            transactions.setCategory(category.getCategoryName());
                            categoryDialog.dismiss();
                        }
                    });
            dialogBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
            dialogBinding.recyclerView.setAdapter(categoryAdapters);

            categoryDialog.show();

        });

        binding.account.setOnClickListener(c -> {
            ListDialogBinding dialogBinding = ListDialogBinding.inflate(inflater);
            AlertDialog accountsDialog = new AlertDialog.Builder(getContext()).create();
            accountsDialog.setView(dialogBinding.getRoot());

            ArrayList<Accounts> accounts = new ArrayList<>();
            accounts.add(new Accounts("Cash", 0));
            accounts.add(new Accounts("Bank", 0));
            accounts.add(new Accounts("PayTM", 0));
            accounts.add(new Accounts("EasyPaisa", 0));
            accounts.add(new Accounts("Other", 0));

            AccountsAdapter adapter = new AccountsAdapter(getContext(), accounts, new AccountsAdapter.AccountClickListener() {
                @Override
                public void onAccountSelected(Accounts accounts) {
                    binding.account.setText(accounts.getAccountName());
                    transactions.setAccount(accounts.getAccountName());
                    accountsDialog.dismiss();
                }
            });
            dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            dialogBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
            dialogBinding.recyclerView.setAdapter(adapter);

            accountsDialog.show();
        });

        binding.saveTransactionbtn.setOnClickListener(c->{
            double amount = Double.parseDouble(binding.Amount.getText().toString());
            String note = binding.Note.getText().toString();

            if(transactions.getType().equals(Constants.EXPENSE)){
                transactions.setAmount(amount*-1);
            }else{
                transactions.setAmount(amount);
            }


            transactions.setNote(note);

            ((MainActivity)getActivity()).viewModel.addTransactions(transactions);
            ((MainActivity)getActivity()).getTransactions();
            dismiss();
        });



        return binding.getRoot();
    }
}