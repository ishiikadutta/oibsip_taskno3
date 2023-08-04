import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class Transaction {
    private String type;
    private double amount;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }
}

class User {
    private String userId;
    private String pin;
    private double balance;
    private ArrayList<Transaction> transactionHistory;

    public User(String userId, String pin, double balance) {
        this.userId = userId;
        this.pin = pin;
        this.balance = balance;
        this.transactionHistory = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getPin() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void addTransaction(String type, double amount) {
        transactionHistory.add(new Transaction(type, amount));
    }

    public ArrayList<Transaction> getTransactionHistory() {
        return transactionHistory;
    }
}

public class ATMSystem {
    private static HashMap<String, User> usersDatabase = new HashMap<>();
    private static User currentUser = null;

    public static void main(String[] args) {
        initializeUsers();
        runATM();
    }

    private static void initializeUsers() {
        usersDatabase.put("user1", new User("user1", "1234", 1000.0));
        usersDatabase.put("user2", new User("user2", "5678", 1500.0));
        // Add more users as needed
    }

    private static void runATM() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the ATM!");

        while (true) {
            System.out.print("Enter User ID: ");
            String userId = scanner.nextLine();
            System.out.print("Enter PIN: ");
            String pin = scanner.nextLine();

            if (authenticateUser(userId, pin)) {
                System.out.println("Login successful!");
                currentUser = usersDatabase.get(userId);
                showMainMenu();
            } else {
                System.out.println("Invalid credentials. Please try again.");
            }
        }
    }

    private static boolean authenticateUser(String userId, String pin) {
        if (usersDatabase.containsKey(userId)) {
            User user = usersDatabase.get(userId);
            return user.getPin().equals(pin);
        }
        return false;
    }

    private static void showMainMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nMain Menu:");
        System.out.println("1. View Balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Transactions History");
        System.out.println("6. Quit");

        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                viewBalance();
                break;
            case 2:
                deposit();
                break;
            case 3:
                withdraw();
                break;
            case 4:
                transfer();
                break;
            case 5:
                transactionsHistory();
                break;
            case 6:
                System.out.println("Thank you for using the ATM. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    private static void viewBalance() {
        System.out.println("Your balance: $" + currentUser.getBalance());
    }

    private static void deposit() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter deposit amount: ");
        double amount = scanner.nextDouble();
        if (amount > 0) {
            currentUser.setBalance(currentUser.getBalance() + amount);
            currentUser.addTransaction("Deposit", amount);
            System.out.println("Deposit successful. Your new balance: $" + currentUser.getBalance());
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    private static void withdraw() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter withdrawal amount: ");
        double amount = scanner.nextDouble();
        if (amount > 0 && amount <= currentUser.getBalance()) {
            currentUser.setBalance(currentUser.getBalance() - amount);
            currentUser.addTransaction("Withdrawal", -amount);
            System.out.println("Withdrawal successful. Your new balance: $" + currentUser.getBalance());
        } else {
            System.out.println("Invalid withdrawal amount or insufficient balance.");
        }
    }

    private static void transfer() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter recipient's User ID: ");
        String recipientId = scanner.nextLine();

        if (usersDatabase.containsKey(recipientId)) {
            User recipient = usersDatabase.get(recipientId);
            System.out.print("Enter transfer amount: ");
            double amount = scanner.nextDouble();

            if (amount > 0 && amount <= currentUser.getBalance()) {
                currentUser.setBalance(currentUser.getBalance() - amount);
                recipient.setBalance(recipient.getBalance() + amount);
                currentUser.addTransaction("Transfer to " + recipient.getUserId(), -amount);
                recipient.addTransaction("Received from " + currentUser.getUserId(), amount);
                System.out.println("Transfer successful. Your new balance: $" + currentUser.getBalance());
            } else {
                System.out.println("Invalid transfer amount or insufficient balance.");
            }
        } else {
            System.out.println("Recipient not found.");
        }
    }

    private static void transactionsHistory() {
        ArrayList<Transaction> history = currentUser.getTransactionHistory();

        if (history.isEmpty()) {
            System.out.println("No transaction history available.");
        } else {
            System.out.println("\nTransaction History:");
            for (Transaction transaction : history) {
                System.out.println("Type: " + transaction.getType() + ", Amount: $" + transaction.getAmount());
            }
        }
    }
}
