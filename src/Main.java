import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int arrSize = 10_000_000;
        String[] unsorted = new String[arrSize];

        Random random = new Random();

        for (int tryNumber = 0; tryNumber < 5; tryNumber++) {
            for (int i = 0; i < unsorted.length; i++) {
                unsorted[i] = Integer.toString(random.nextInt(Integer.MAX_VALUE));
            }


            List<Future> futures = new ArrayList<>();
            int coreCount = Runtime.getRuntime().availableProcessors();

            for (int cores = 1; cores <= coreCount; cores++) {
                int batchSize = arrSize / cores;
                long start = System.currentTimeMillis();
                final ExecutorService executorService = Executors.newFixedThreadPool(cores);
                ArrayList<MultiThreadSort> multiThreadSorts = new ArrayList<>();
                for (int i = 0; i < cores; i++) {
                    String[] part = new String[batchSize];

                    System.arraycopy(unsorted, i * batchSize, part, 0, batchSize);
                    MultiThreadSort multiThreadSort = new MultiThreadSort(part);

                    futures.add(executorService.submit(multiThreadSort));
                    multiThreadSorts.add(multiThreadSort);
                }
                for (Future future : futures) {
                    future.get();
                }
                executorService.shutdown();
                int tries = 0;

                String[] mergered = new String[arrSize];

                for (MultiThreadSort multiThreadSort : multiThreadSorts) {
                    if (tries == 0) {
                        mergered = multiThreadSort.getSorted();
                        tries++;
                    } else {
                        String[] part = multiThreadSort.getSorted();
                        mergered = PartOfArray.merge(mergered, part);
                    }
                }
                long time = System.currentTimeMillis() - start;
                System.out.println("Программа сортировки выполнила задачу за " + time + " миллисекунд с использованием " + cores + " ядер");
            }

            long start = System.currentTimeMillis();
            Arrays.sort(unsorted);
            long time = System.currentTimeMillis() - start;
            System.out.println("Сортировка дефолтной функцией SDK заняла " + time + " миллисекунд");
        }
    }
}
