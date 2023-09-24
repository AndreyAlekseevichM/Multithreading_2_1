import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static final SortedMap<Integer, Integer> sizeToFreq = new TreeMap<>(Comparator.reverseOrder());

    public static void main(String[] args) {
        int numberOfRoutes = 1000;
        String pattern = "RLRFR";
        int stringLength = 100;
        char searchedSymbol = 'R';
        ExecutorService volume;
        volume = Executors.newFixedThreadPool(numberOfRoutes);

        Runnable runnable = () -> {
            String route = generateRoute(pattern, stringLength);
            int amountSearchedSymbol = 0;
            for (int i = 0; i < route.length(); i++) {
                if (route.charAt(i) == searchedSymbol) {
                    amountSearchedSymbol++;
                }
            }
            synchronized (sizeToFreq) {
                if (sizeToFreq.containsKey(amountSearchedSymbol)) {
                    sizeToFreq.put(amountSearchedSymbol, sizeToFreq.get(amountSearchedSymbol) + 1);
                } else {
                    sizeToFreq.put(amountSearchedSymbol, 1);
                }
            }
        };

        for (int i = 0; i < numberOfRoutes; i++) {
            volume.submit(new Thread(runnable));
        }

        volume.shutdown();

        System.out.println("Самое частое количество повторений " +
                sizeToFreq.firstKey() +
                " встретилось " +
                sizeToFreq.get(sizeToFreq.firstKey()) +
                " раз"
        );
        System.out.println("Другие размеры:");
        sizeToFreq.forEach((x, y) -> System.out.println(" - " + x + " (" + y + " раз)"));
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}