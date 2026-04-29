import java.util.ArrayList;

class Account {
    private String accountNumber;
    private String ownerName;
    private double balance;

    protected void updateBalance(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setBalance(double balance) {
        if (balance >= 0) {
            updateBalance(balance);
        } else {
            updateBalance(0);
        }
    }

    public void setOwnerName(String name) {
        if (name == null || name.isBlank()) {
            this.ownerName = "Unknown";
        } else {
            this.ownerName = name;
        }
    }

    public void setAccountNumber(String accNo) {
        this.accountNumber = accNo;
    }

    public Account() {
        this("0000", "Unknown", 0.0);
    }

    public Account(String accNo, String name, double balance) {
        setAccountNumber(accNo);
        setOwnerName(name);
        setBalance(balance);
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid deposit");
        }
        this.balance += amount;
    }

    public void withdraw(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Invalid withdrawal");
        } else if (amount > this.balance) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        this.balance -= amount;
    }

    public void display() {
        System.out.printf("%-15s | %-20s | %12s%n", "Account No.", "Owner Name", "Balance");
        System.out.println("---------------------------------------------------------");
        System.out.printf("%-15s | %-20s | $%,11.2f%n\n",
                this.accountNumber,
                this.ownerName,
                this.balance);

    }
}

class SavingsAccount extends Account {
    private float interestRate;

    public void setInterestRate(float rate) {
        if (rate < 0) {
            throw new IllegalArgumentException("Invalid interest rate");
        }
        this.interestRate = rate;
    }

    public double getInterestRate() {
        return this.interestRate;
    }

    public SavingsAccount() {
        super();
        setInterestRate(0);
    }

    public SavingsAccount(String accNo, String name, double balance, float rate) {
        super(accNo, name, balance);
        setInterestRate(rate);
    }

    public double calculateInterest() {
        return getBalance() * this.interestRate / 100;
    }

    @Override
    public void display() {
        System.out.printf("%-15s | %-20s | %12s | %13s%n",
                "Account No.", "Owner Name", "Balance", "Interest Rate");
        System.out.println("-----------------------------------------------------------------------");
        System.out.printf("%-15s | %-20s | $%,11.2f | %12.2f%%%n\n",
                this.getAccountNumber(),
                this.getOwnerName(),
                this.getBalance(),
                this.getInterestRate());
    }
}

class CurrentAccount extends Account {
    private double overdraftLimit;

    public CurrentAccount() {
        super();
        this.overdraftLimit = 0;
    }

    public CurrentAccount(String accNo, String name, double balance, double overdraftLimit) {
        super(accNo, name, balance);
        this.overdraftLimit = overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        if (overdraftLimit < 0) {
            throw new IllegalArgumentException("Invalid overdraft limit");
        }
        this.overdraftLimit = overdraftLimit;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    @Override
    public void withdraw(double amount) {
        if (amount > getBalance() + overdraftLimit) {
            throw new IllegalArgumentException("Overdraft exceeded");
        } else if (amount < 0) {
            throw new IllegalArgumentException("Invalid withdrawal");
        }
        setBalance(getBalance() - amount);
    }

    @Override
    public void setBalance(double balance) {
        if (balance < -overdraftLimit) {
            throw new IllegalArgumentException("Overdraft exceeded");
        }
        updateBalance(balance);
    }

    @Override
    public void display() {
        System.out.printf("%-15s | %-20s | %12s | %13s%n",
                "Account No.", "Owner Name", "Balance", "Overdraft Limit");
        System.out.println("-----------------------------------------------------------------------");
        System.out.printf("%-15s | %-20s | $%,11.2f | %12.2f%n\n",
                this.getAccountNumber(),
                this.getOwnerName(),
                this.getBalance(),
                this.getOverdraftLimit());
    }
}

class Main {
    public static void main(String[] args) {
        SavingsAccount savings = new SavingsAccount("SA1001", "Aarav", 5000, 4.5f);
        CurrentAccount current = new CurrentAccount("CA2001", "Ishita", 2000, 1500);

        System.out.println("INITIAL ACCOUNT DETAILS");
        savings.display();
        current.display();

        System.out.println("SAVINGS ACCOUNT OPERATIONS");
        savings.deposit(1000);
        savings.withdraw(700);
        double interest = savings.calculateInterest();
        savings.deposit(interest);
        System.out.printf("Interest credited: $%,.2f%n", interest);
        savings.display();

        System.out.println("CURRENT ACCOUNT OPERATIONS");
        current.withdraw(3000);
        current.display();

        System.out.println("EXCEPTION HANDLING DEMO");
        try {
            savings.withdraw(100000);
        } catch (IllegalArgumentException e) {
            System.out.println("Savings withdrawal error: " + e.getMessage());
        }

        try {
            current.withdraw(1000);
            current.withdraw(3000);
        } catch (IllegalArgumentException e) {
            System.out.println("Current withdrawal error: " + e.getMessage() + "\n");
        }

        ArrayList<Account> accounts = new ArrayList<>();
        accounts.add(savings);
        accounts.add(current);

        System.out.println("FINAL ACCOUNT SNAPSHOT");
        for (Account account : accounts) {
            account.display();
        }
    }
}
