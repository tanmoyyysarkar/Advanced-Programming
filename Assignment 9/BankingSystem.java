class Account {
    private String accountNumber;
    private String ownerName;
    private double balance;

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
            this.balance = balance;
        } else {
            this.balance = 0;
        }
    }

    public void setOwnerName(String name) {
        if (name == "") {
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

    public void deposit(double ammount) {
        if (ammount < 0) {
            throw new IllegalArgumentException("Invalid deposit");
        }
        this.balance += ammount;
    }

    public void withdraw(double ammount) {
        if (ammount < 0) {
            throw new IllegalArgumentException("Invalid withdrawal");
        } else if (ammount > this.balance) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        this.balance -= ammount;
    }

    public void display() {
        System.out.printf("\n%-15s | %-20s | %12s%n", "Account No.", "Owner Name", "Balance");
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
        System.out.printf("\n%-15s | %-20s | %12s | %13s%n",
                "Account No.", "Owner Name", "Balance", "Interest Rate");
        System.out.println("-----------------------------------------------------------------------");
        System.out.printf("%-15s | %-20s | $%,11.2f | %12.2f%%%n\n",
                this.getAccountNumber(),
                this.getOwnerName(),
                this.getBalance(),
                this.getInterestRate());
    }
}

class Main {
    public static void main(String[] args) {
        Account acc1 = new Account();
        acc1.display();
        SavingsAccount acc2 = new SavingsAccount();
        acc2.display();
    }
}
