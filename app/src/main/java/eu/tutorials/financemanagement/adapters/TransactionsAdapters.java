package eu.tutorials.financemanagement.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import eu.tutorials.financemanagement.R;
import eu.tutorials.financemanagement.databinding.RowTransactionBinding;
import eu.tutorials.financemanagement.models.Category;
import eu.tutorials.financemanagement.models.Transactions;
import eu.tutorials.financemanagement.utils.Constants;
import eu.tutorials.financemanagement.utils.Helper;
import eu.tutorials.financemanagement.views.activities.MainActivity;
import io.realm.RealmResults;

public class TransactionsAdapters extends RecyclerView.Adapter<TransactionsAdapters.TransactionViewHolder>{
    Context context;
    RealmResults<Transactions> transactions;

    public TransactionsAdapters(Context context, RealmResults<Transactions> transactions){
        this.context = context;
        this.transactions = transactions;

    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(context).inflate(R.layout.row_transaction,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transactions transaction = transactions.get(position);
        holder.binding.transactionamount.setText(String.valueOf(transaction.getAmount()));
        holder.binding.accountLbl.setText(transaction.getAccount());
        holder.binding.transactiondate.setText(Helper.formatDate(transaction.getDate()));
        holder.binding.transactioncategory.setText(transaction.getCategory());

        Category transactionCategory = Constants.getCategoryDetails(transaction.getCategory());
        holder.binding.categoryicon.setImageResource(transactionCategory.getCategoryImage());
        holder.binding.categoryicon.setBackgroundTintList(context.getColorStateList(transactionCategory.getCategoryColor()));

        holder.binding.accountLbl.setBackgroundTintList(context.getColorStateList(Constants.getAccountsColor(transaction.getAccount())));

        if(transaction.getType().equals(Constants.INCOME)){
            holder.binding.transactionamount.setTextColor(context.getColor(R.color.greencolor));
        }else if(transaction.getType().equals(Constants.EXPENSE)){
            holder.binding.transactionamount.setTextColor(context.getColor(R.color.redcolor));
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog deletedialog = new AlertDialog.Builder(context).create();
                deletedialog.setTitle("Delete Dialog");
                deletedialog.setMessage("Are you sure you want to delete");
                deletedialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", (dialogInterface, i) -> {
                    ((MainActivity)context).viewModel.deleteTransaction(transaction);

                });
                deletedialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", (dialogInterface, i) -> {
                    deletedialog.dismiss();
                });
                deletedialog.show();
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder{
        RowTransactionBinding binding;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowTransactionBinding.bind(itemView);
        }
    }
}
