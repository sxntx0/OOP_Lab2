import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FileProcessor {

    // Чтение CSV файла
    public List<City> readCsvFile(String filePath) throws IOException {
        List<City> cities = new ArrayList<>();
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath))
                .withCSVParser(new com.opencsv.CSVParserBuilder().withSeparator(';').build())
                .build()) {

            String[] line;
            boolean firstLine = true; // флаг для пропуска первой строки
            while ((line = reader.readNext()) != null) {
                // Пропускаем первую строку (заголовки)
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                if (line.length > 0) {
                    String city = line[0];
                    String street = line[1];

                    // Обработка ошибок при преобразовании в int для house и floor
                    int house = 0;
                    int floor = 0;

                    try {
                        house = Integer.parseInt(line[2]);
                    } catch (NumberFormatException e) {
                        System.err.println("Ошибка преобразования house: " + line[2]);
                        continue; // Пропускаем строку, если house не числовое
                    }

                    try {
                        floor = Integer.parseInt(line[3]);
                    } catch (NumberFormatException e) {
                        System.err.println("Ошибка преобразования floor: " + line[3]);
                        continue; // Пропускаем строку, если floor не числовое
                    }

                    cities.add(new City(city, street, house, floor));
                }
            }
        } catch (CsvValidationException e) {
            System.err.println("Ошибка валидации CSV: " + e.getMessage());
        }
        return cities;
    }

    // Чтение XML файла
    public List<City> readXmlFile(String filePath) {
        List<City> cities = new ArrayList<>();
        try {
            File xmlFile = new File(filePath);
            javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            org.w3c.dom.NodeList nodeList = doc.getElementsByTagName("item");
            for (int i = 0; i < nodeList.getLength(); i++) {
                org.w3c.dom.Node node = nodeList.item(i);
                if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    String city = element.getAttribute("city");
                    String street = element.getAttribute("street");

                    int house = 0;
                    int floor = 0;

                    try {
                        house = Integer.parseInt(element.getAttribute("house"));
                    } catch (NumberFormatException e) {
                        System.err.println("Ошибка преобразования house: " + element.getAttribute("house"));
                        continue; // Пропускаем запись с некорректным значением house
                    }

                    try {
                        floor = Integer.parseInt(element.getAttribute("floor"));
                    } catch (NumberFormatException e) {
                        System.err.println("Ошибка преобразования floor: " + element.getAttribute("floor"));
                        continue; // Пропускаем запись с некорректным значением floor
                    }

                    cities.add(new City(city, street, house, floor));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cities;
    }

    // Вывод статистики
    public void printStatistics(List<City> cities) {
        if (cities == null || cities.isEmpty()) {
            System.out.println("Нет данных для вывода статистики.");
            return;
        }

        // Статистика по дублирующимся записям
        Map<City, Integer> cityCountMap = new HashMap<>();
        for (City city : cities) {
            cityCountMap.put(city, cityCountMap.getOrDefault(city, 0) + 1);
        }
        System.out.println("Дублирующиеся записи с количеством повторений:");
        for (Map.Entry<City, Integer> entry : cityCountMap.entrySet()) {
            if (entry.getValue() > 1) {
                System.out.println(entry.getKey() + " - " + entry.getValue() + " раз");
            }
        }

        // Статистика по этажам
        Map<Integer, Integer> floorCountMap = new HashMap<>();
        for (City city : cities) {
            floorCountMap.put(city.getFloor(), floorCountMap.getOrDefault(city.getFloor(), 0) + 1);
        }
        System.out.println("Количество зданий по этажам:");
        for (Map.Entry<Integer, Integer> entry : floorCountMap.entrySet()) {
            System.out.println(entry.getKey() + "-этажных зданий: " + entry.getValue());
        }
    }
}
