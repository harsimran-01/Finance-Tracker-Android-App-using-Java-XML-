package eu.tutorials.financemanagement.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.Calendar;
import java.util.Date;

import eu.tutorials.financemanagement.models.Transactions;
import eu.tutorials.financemanagement.utils.Constants;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainViewModel extends AndroidViewModel {

    Realm realm;
    Calendar calendar;

    public MutableLiveData<RealmResults<Transactions>> transactions = new MutableLiveData<>();

    public MutableLiveData<Double> totalIncome = new MutableLiveData<>();
    public MutableLiveData<Double> totalExpense = new MutableLiveData<>();
    public MutableLiveData<Double> totalAmount = new MutableLiveData<>();


    public MainViewModel(@NonNull Application application) {
        super(application);
        Realm.init(application);
        setupDatabase();
    }

    public void getTransactions(Calendar calendar){
        this.calendar = calendar;
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        double income = 0;
        double expense = 0;
        double total = 0;
        RealmResults<Transactions> newTransactions = null;

        if(Constants.SELECTED_TAB == Constants.DAILY){



        newTransactions = realm.where(Transactions.class)
                .greaterThanOrEqualTo("date",calendar.getTime())
                .lessThan("date",new Date(calendar.getTime().getTime()+(24*60*60*1000)))
                .findAll();

        income = realm.where(Transactions.class)
                .greaterThanOrEqualTo("date",calendar.getTime())
                .lessThan("date",new Date(calendar.getTime().getTime()+(24*60*60*1000)))
                .equalTo("type",Constants.INCOME)
                        .sum("amount")
                                .doubleValue();

        expense = realm.where(Transactions.class)
                .greaterThanOrEqualTo("date",calendar.getTime())
                .lessThan("date",new Date(calendar.getTime().getTime()+(24*60*60*1000)))
                .equalTo("type",Constants.EXPENSE)
                .sum("amount")
                .doubleValue();

        total = realm.where(Transactions.class)
                .greaterThanOrEqualTo("date",calendar.getTime())
                .lessThan("date",new Date(calendar.getTime().getTime()+(24*60*60*1000)))
                .sum("amount")
                .doubleValue();



            transactions.setValue(newTransactions);
        } else if (Constants.SELECTED_TAB == Constants.MONTHLY) {
            calendar.set(Calendar.DAY_OF_MONTH,0);

            Date startTime = calendar.getTime();

            calendar.add(Calendar.MONTH,1);
            Date endTime = calendar.getTime();

            newTransactions = realm.where(Transactions.class)
                    .greaterThanOrEqualTo("date",startTime)
                    .lessThan("date",endTime)
                    .findAll();

            income = realm.where(Transactions.class)
                    .greaterThanOrEqualTo("date",startTime)
                    .lessThan("date",endTime)
                    .equalTo("type",Constants.INCOME)
                    .sum("amount")
                    .doubleValue();

            expense = realm.where(Transactions.class)
                    .greaterThanOrEqualTo("date",startTime)
                    .lessThan("date",endTime)
                    .equalTo("type",Constants.EXPENSE)
                    .sum("amount")
                    .doubleValue();

            total = realm.where(Transactions.class)
                    .greaterThanOrEqualTo("date",startTime)
                    .lessThan("date",endTime)
                    .sum("amount")
                    .doubleValue();



        }
        totalIncome.setValue(income);
        totalExpense.setValue(expense);
        totalAmount.setValue(total);
        transactions.setValue(newTransactions);

//        RealmResults<Transactions> newTransactions = realm.where(Transactions.class)
//                .equalTo("date",calendar.getTime())
//                .findAll();

    }

    public void addTransactions(Transactions transactions){
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(transactions);
        realm.commitTransaction();
    }

    public void deleteTransaction(Transactions transactions){
        realm.beginTransaction();
        transactions.deleteFromRealm();
        realm.commitTransaction();
        getTransactions(calendar);
    }



//    public void addTransactions(){
//        realm.beginTransaction();
//        realm.copyToRealmOrUpdate(new Transactions(Constants.INCOME,"Business","Cash","Some note here",new Date(),500.0,new Date().getTime()));
//        realm.copyToRealmOrUpdate(new Transactions(Constants.EXPENSE,"Investment","Card","Some note here",new Date(),-900.0,new Date().getTime()));
//        realm.copyToRealmOrUpdate(new Transactions(Constants.INCOME,"Business","Bank","Some note here",new Date(),500.0,new Date().getTime()));
//        realm.copyToRealmOrUpdate(new Transactions(Constants.INCOME,"Salary","Cash","Some note here",new Date(),500.0,new Date().getTime()));
//        realm.commitTransaction();
//    }
    void setupDatabase(){

        realm = Realm.getDefaultInstance();
    }
}
