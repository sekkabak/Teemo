package org.sekka.teemo.data.model;

import java.util.ArrayList;

public class StoredCredential {
    private int ID;
    private String name;
    private String password;
    private String description;

    public StoredCredential(int ID, String name, String password, String description) {
        this.ID = ID;
        this.name = name;
        this.password = password;
        this.description = description;
    }

    public StoredCredential(String name, String password, String description) {
        this.ID = -1;
        this.name = name;
        this.password = password;
        this.description = description;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private static int lastStoredCredentialId = 0;

    public static ArrayList<StoredCredential> createStoredCredentialList(int numStoredCredential) {
        ArrayList<StoredCredential> storedCredentials = new ArrayList<>(numStoredCredential);

        for(int i =1; i <= numStoredCredential; i++) {
            storedCredentials.add(new StoredCredential(-1, "test", "testPwd", "testOpis"));
        }

        return storedCredentials;
    }
}
