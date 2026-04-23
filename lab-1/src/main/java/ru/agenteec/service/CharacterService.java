package ru.agenteec.service;

import ru.agenteec.model.Character;
import java.util.List;
import java.util.TreeMap;

public class CharacterService {


    public TreeMap<String, Integer> calculateGenderStatistics(List<Character> characters) {
        TreeMap<String, Integer> stats = new TreeMap<>();

        for (Character c : characters) {
            String gender = c.getGender();
            if (gender == null || gender.isBlank()) {
                gender = "unknown";
            }
            stats.put(gender, stats.getOrDefault(gender, 0) + 1);
        }

        return stats;
    }
}