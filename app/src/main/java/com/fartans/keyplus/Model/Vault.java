package com.fartans.keyplus.Model;

public class Vault {

    String Name;
    int keyNumber;
    int pin;
    int id;
    long passcode;
    int isSecure;

    public static final int SECURE_VAULT = 1;

    public long getPasscode() {
        return passcode;
    }

    public void setPasscode(long passcode) {
        this.passcode = passcode;
    }


    public int getIsSecure() {
        return isSecure;
    }

    public void setIsSecure(int isSecure) {
        this.isSecure = isSecure;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Vault()
    {

    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }




    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getKeyNumber() {
        return keyNumber;
    }

    public void setKeyNumber(int keyNumber) {
        this.keyNumber = keyNumber;
    }
}
