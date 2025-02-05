package com.example.lifeease;

public class UserData {
    private static String username;
    private static String phone;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String name) {
        username = name;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String number) {
        phone = number;
    }
}