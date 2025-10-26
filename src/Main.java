import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Account account = new Account();
        DepThread depThread = new DepThread(account);
        depThread.start();

        try {
            account.withdraw(200);

            depThread.join();

            System.out.println("Итоговый баланс: " + account.getBalance() + " руб.");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Account {
    private int balance = 0;

    public synchronized void dep(int am) {
        balance += am;
        System.out.println("Пополнение: +" + am + " руб. Баланс: " + balance + " руб.");
        notifyAll();
    }

    public synchronized void withdraw(int am) throws InterruptedException {
        System.out.println("Запрос на снятие: " + am + " руб.");

        while (balance < am) {
            System.out.println("Недостаточно средств (" + balance + " руб.). Ожидание пополнения...");
            wait();
        }

        balance -= am;
        System.out.println("Снято: -" + am + " руб. Баланс: " + balance + " руб.");
    }


    public synchronized int getBalance() {
        return balance;
    }
}

class DepThread extends Thread {
    private final Account account;
    private final Random random = new Random();

    public DepThread(Account account) {
        this.account = account;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                int amount = random.nextInt(100) + 1;
                account.dep(amount);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
