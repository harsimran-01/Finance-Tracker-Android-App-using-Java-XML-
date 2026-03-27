package eu.tutorials.financemanagement.views.fragments;
//import androidx.fragment.app.FragmentTransactions;

//import android.app.FragmentTransaction;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eu.tutorials.financemanagement.R;
import eu.tutorials.financemanagement.databinding.FragmentTransactionsBinding;


public class TransactionsFragment extends Fragment {

    FragmentTransactionsBinding binding;
    public TransactionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTransactionsBinding.inflate(inflater);

        return binding.getRoot();
    }
}