package eu.tutorials.financemanagement.models;

public class Accounts {
    private double accountAmount;
    private String accountName;

    public Accounts() {
    }

    public Accounts(String accountName, double accountAmount) {
        this.accountName = accountName;
        this.accountAmount = accountAmount;
    }

    public double getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(double accountAmount) {
        this.accountAmount = accountAmount;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
