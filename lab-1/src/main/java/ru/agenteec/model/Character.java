package ru.agenteec.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Character {
    private int id;
    private String name;
    private String status;
    private String species;
    private String type;
    private String gender;
    private String origin;
    private String location;
    private String created;
}