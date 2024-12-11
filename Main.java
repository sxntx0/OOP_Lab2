import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        FileProcessor fileProcessor = new FileProcessor();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Введите путь до файла (CSV или XML), или введите 'exit' для завершения работы:");

            String filePath = scanner.nextLine();

            // Проверяем на команду выхода
            if (filePath.equalsIgnoreCase("exit")) {
                System.out.println("Завершаем работу...");
                running = false;
                continue;
            }

            // Проверяем, существует ли файл
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("Файл не существует. Попробуйте снова.");
                continue;
            }

            // Обработка CSV или XML в зависимости от расширения файла
            try {
                long startTime = System.currentTimeMillis();
                List<City> cities = null;

                if (filePath.endsWith(".csv")) {
                    cities = fileProcessor.readCsvFile(filePath);
                } else if (filePath.endsWith(".xml")) {
                    cities = fileProcessor.readXmlFile(filePath);
                } else {
                    System.out.println("Неподдерживаемый формат файла. Пожалуйста, выберите CSV или XML.");
                    continue;
                }

                // Выводим статистику
                fileProcessor.printStatistics(cities);

                long endTime = System.currentTimeMillis();
                System.out.println("Время обработки файла: " + (endTime - startTime) + " миллисекунд.");

            } catch (IOException e) {
                System.out.println("Ошибка при обработке файла: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
