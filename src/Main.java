import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        // Создаем ExecutorService с фиксированным пулом потоков (10 потоков)
        ExecutorService executor = Executors.newFixedThreadPool(10);

        // Создаем два атомарных счетчика
        AtomicInteger counter1 = new AtomicInteger(0);
        AtomicInteger counter2 = new AtomicInteger(0);

        // Инициируем предварительно определенное число потоков (100_000)
        final int totalThreads = 100_000;

        // Отправляем задания на увеличение счетчиков в пул потоков
        for (int i = 0; i < totalThreads; i++) {
            executor.submit(() -> {
                // Увеличиваем оба счетчика атомарно
                synchronized (counter1) {
                    counter1.incrementAndGet();
                }
                synchronized (counter2) {
                    counter2.incrementAndGet();
                }
            });
        }

        // Ждем завершения всех заданий в пуле потоков
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            System.err.println("Ошибка при ожидании завершения потоков: " + e.getMessage());
        }

        // Выводим значения счетчиков
        System.out.println("Значение counter1: " + counter1.get());
        System.out.println("Значение counter2: " + counter2.get());

        // Останавливаем ExecutorService
        executor.shutdownNow();
    }
}
