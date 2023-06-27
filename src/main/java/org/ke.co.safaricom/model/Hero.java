package org.javasparkips.model;

public class Hero {
    private int id;
    private String name;
    private int age;
    private String power;
    private String weakness;
    private int squadId;
    private int power_score;
    private int weakness_score;
    private boolean active;

    public Hero(String name, int age, String power, String weakness) {
        this.name = name;
        this.age = age;
        this.power = power;
        this.weakness = weakness;

    }

    // Add getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getWeakness() {
        return weakness;
    }

    public void setWeakness(String weakness) {
        this.weakness = weakness;
    }

    public int getSquadId() {
        return squadId;
    }

    public void setSquadId(int squadId) {
        this.squadId = squadId;
    }

    public int getPower_score() {
        return power_score;
    }

    public void setPower_score(int power_score) {
        this.power_score = power_score;
    }

    public int getWeakness_score() {
        return weakness_score;
    }

    public void setWeakness_score(int weakness_score) {
        this.weakness_score = weakness_score;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
