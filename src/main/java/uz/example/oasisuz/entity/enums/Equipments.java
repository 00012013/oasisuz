package uz.example.oasisuz.entity.enums;

public enum Equipments {
    PING_PONG("Ping Pong"), PLAYSTATION("Playstation"), KARAOKE("Karaoke"), BILLIARD("Billiard"), WINTER_POOL("Winter pool"), OUTDOOR_POOL("Outdoor pool"), FOOTBALL_PITCH("Football Pitch");

    private String name;

    Equipments(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return name().replace("_", " ");
    }
}
