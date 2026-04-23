package ru.agenteec.repository;

import ru.agenteec.model.Character;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CharacterRepository {

    public List<Character> readFromFile(String filePath) {
        List<Character> characters = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                characters.add(parseRow(line));
            }
        } catch (IOException e) {
            System.err.println("Err read file (CharacterRepository.readFromFile): " + e.getMessage());
            throw new RuntimeException(e);
        }
        return characters;
    }

    private Character parseRow(String row) {
        String[] cols = row.split(",", -1);
        return new Character(
                Integer.parseInt(cols[0]),
                cols[1], cols[2], cols[3], cols[4],
                cols[5], cols[6], cols[7], cols[8]
        );
    }
}