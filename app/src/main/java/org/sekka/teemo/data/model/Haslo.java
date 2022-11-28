package org.sekka.teemo.data.model;

import java.util.ArrayList;

public class Haslo {
    private String name;
    private String haslo;
    private String opis;

    public Haslo(String name, String haslo, String opis) {
        this.name = name;
        this.haslo = haslo;
        this.opis = opis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    private static int lastHasloId = 0;

    public static ArrayList<Haslo> createHasloList(int numHaslos) {
        ArrayList<Haslo> haslos = new ArrayList<>(numHaslos);

        for(int i =1; i <= numHaslos; i++) {
            haslos.add(new Haslo("test", "testPwd", "testOpis"));
        }

        return haslos;
    }
}
