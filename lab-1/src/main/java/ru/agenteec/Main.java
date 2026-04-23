package ru.agenteec;

import ru.agenteec.model.Character;
import ru.agenteec.repository.CharacterRepository;
import ru.agenteec.service.CharacterService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        String inputPath = null;
        String outputPath = "gender_stats.csv";

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-o") && i + 1 < args.length) {
                outputPath = args[i + 1];
                i++;
            } else {
                inputPath = args[i];
            }
        }

        if (inputPath == null) {
            System.out.println("err: не указан входной файл.");
            System.out.println("Использование: java -jar lab-1.jar <input.csv> [-o <output.csv>]");
            return;
        }

        CharacterRepository repository = new CharacterRepository();
        CharacterService service = new CharacterService();

        List<Character> characters = repository.readFromFile(inputPath);

        TreeMap<String, Integer> genderStats = service.calculateGenderStatistics(characters);

        writeStatisticsToFile(outputPath, genderStats);

        System.out.println("Сохранено в: " + outputPath);
    }

    private static void writeStatisticsToFile(String path, TreeMap<String, Integer> stats) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write("gender,count");
            writer.newLine();

            for (var entry : stats.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("err write (Main.writeStatisticsToFile): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}