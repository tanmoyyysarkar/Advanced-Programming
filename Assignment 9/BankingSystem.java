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

class Main {
    public static void main(String[] args) {
        Account acc1 = new Account();
        acc1.display();
    }
}
