package eu.tutorials.financemanagement.adapters;

import android.accounts.Account;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import eu.tutorials.financemanagement.R;
import eu.tutorials.financemanagement.databinding.RowAccountsBinding;
import eu.tutorials.financemanagement.models.Accounts;

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.AccountViewHolder> {
    Context context;
    ArrayList<Accounts> accountArrayList;

    public interface AccountClickListener{
        void onAccountSelected(Accounts accounts);
    }

    AccountClickListener accountClickListener;

    public AccountsAdapter(Context context, ArrayList<Accounts> accountArrayList,AccountClickListener accountClickListener){
        this.context = context;
        this.accountArrayList = accountArrayList;
        this.accountClickListener = accountClickListener;

    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AccountViewHolder(LayoutInflater.from(context).inflate(R.layout.row_accounts,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        Accounts accounts = accountArrayList.get(position);
        holder.binding.accountName.setText(accounts.getAccountName());
        holder.itemView.setOnClickListener(c->{
            accountClickListener.onAccountSelected(accounts);
        });

    }

    @Override
    public int getItemCount() {
        return accountArrayList.size();
    }

    public class AccountViewHolder extends RecyclerView.ViewHolder{
        RowAccountsBinding binding;


        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowAccountsBinding.bind(itemView);
        }
    }
}
