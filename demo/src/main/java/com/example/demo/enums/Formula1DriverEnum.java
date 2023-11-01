package com.example.demo.enums;

public enum Formula1DriverEnum {
    VERSTAPPEN("Verstappen",1),
    PEREZ("Perez",11),
    HAMILTON("Hamilton", 44),
    ALONSO("Alonso", 14),
    NORRIS("Norris", 4),
    LECLERC("Leclerc", 16),
    SAINZ("Sainz", 55),
    RUSSEL("Russel", 63),
    PIASTRI("Piastri", 81),
    GASLY("Gasly", 10),
    STROLL("Stroll", 18),
    OCON("Ocon", 31),
    ALBON("Albon", 23),
    BOTTAS("Bootas", 77),
    HULKENBERG("Hulkenber", 27),
    TSUNODA("Tsunoda", 22),
    RICCIARDO("Ricciardo", 3),
    ZHOU("Zhou", 24),
    MAGNUSSEN("Magnussen", 20),
    SARGENT("Sargent", 2)
    ;

    private String name;
    private Integer number;


    Formula1DriverEnum(String name, Integer number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
