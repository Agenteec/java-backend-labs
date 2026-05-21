package ru.agenteec.repository;

import ru.agenteec.model.Character;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CharacterRepository {
    public List<Character> loadRecipesFromResources(String fileName) throws IOException {
        List<Character> characters = new ArrayList<>();


        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) {
                throw new FileNotFoundException("Resource not found: " + fileName);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                String line;
                reader.readLine();
                while ((line = reader.readLine()) != null) {
                    if (line.isBlank()) continue;
                    characters.add(parseRow(line));
                }
            }
        }
        return characters;
    }
    public List<Character> readFromFile(String filePath) {
        List<Character> characters = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))){
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