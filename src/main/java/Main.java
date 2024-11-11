import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class Main {
    public static ArrayBlockingQueue<String> maxCountA = new ArrayBlockingQueue<>(100);
    public static ArrayBlockingQueue<String> maxCountB = new ArrayBlockingQueue<>(100);
    public static ArrayBlockingQueue<String> maxCountC = new ArrayBlockingQueue<>(100);
    private static final int COUNT_STRING = 10_000;

    public static void main(String[] args) {
        Thread creatureString = new Thread(() -> {
            for (int i = 0; i < COUNT_STRING; i++) {
                String string = generateText("abc", 10_000);
                try {
                    maxCountA.put(string);
                    maxCountB.put(string);
                    maxCountC.put(string);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Генерация завершена!");
        });
        creatureString.start();

        Thread maxSymbolA = new Thread(() -> {
            int max = 0;
            for (int i = 0; i < COUNT_STRING; i++) {
                try {
                    max = Math.max(max, countSymbol(maxCountA.take(), 'a'));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Максимальное количество символов 'a' - " + max);
        });
        maxSymbolA.start();

        Thread maxSymbolB = new Thread(() -> {
            int max = 0;
            for (int i = 0; i < COUNT_STRING; i++) {
                try {
                    max = Math.max(max, countSymbol(maxCountB.take(), 'b'));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Максимальное количество символов 'b' - " + max);
        });
        maxSymbolB.start();

        Thread maxSymbolC = new Thread(() -> {
            int max = 0;
            for (int i = 0; i < COUNT_STRING; i++) {
                try {
                    max = Math.max(max, countSymbol(maxCountC.take(), 'c'));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Максимальное количество символов 'c' - " + max);
        });
        maxSymbolC.start();

        try {

            creatureString.join();
            maxSymbolA.join();
            maxSymbolB.join();
            maxSymbolC.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int countSymbol(String text, char symbol) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == symbol) {
                count++;
            }
        }
        return count;
    }
}
