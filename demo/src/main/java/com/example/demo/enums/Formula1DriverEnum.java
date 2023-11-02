package com.example.demo.enums;

public enum Formula1DriverEnum {
    VERSTAPPEN("Max_Verstappen",1),
    PEREZ("Perez",11),
    HAMILTON("Hamilton", 44),
    ALONSO("Alonso", 14),
    NORRIS("Norris", 4),
    LECLERC("Leclerc", 16),
    SAINZ("Sainz", 55),
    RUSSELL("Russell", 63),
    PIASTRI("Piastri", 81),
    GASLY("Gasly", 10),
    STROLL("Stroll", 18),
    OCON("Ocon", 31),
    ALBON("Albon", 23),
    BOTTAS("Bottas", 77),
    HULKENBERG("Hulkenberg", 27),
    TSUNODA("Tsunoda", 22),
    RICCIARDO("Ricciardo", 3),
    ZHOU("Zhou", 24),
    MAGNUSSEN("kevin_magnussen", 20),
    SARGEANT("Sargeant", 2)
    ;

    private String name;
    private Integer number;


    Formula1DriverEnum(String name, Integer number) {
        this.name = name;
        this.number = number;
    }

    public static String getNameByNumber(Integer driverNumber) {
        for(Formula1DriverEnum driver: Formula1DriverEnum.values()) {
            if (driver.getNumber().equals(driverNumber)) {
                return driver.getName();
            }
        }
        return null;
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
