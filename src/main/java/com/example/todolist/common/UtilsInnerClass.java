package com.example.todolist.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilsInnerClass {
    private static UtilsInnerClass instance;
    String dateStr = "2021-01-01";
    final Utils utils;
    final User user;

    private UtilsInnerClass() {
        this.utils = new Utils();
        this.user = new User("Alice", utils.str2date(dateStr));
    }

    public static synchronized UtilsInnerClass getInstance() {
        if (instance == null) {
            instance = new UtilsInnerClass();
        }
        return instance;
    }

    public void userUtils() {
        System.out.println(user.getName());
        System.out.println(user.getBirthDate());
    }

    private static class User {
        private String name;
        private Date birthDate;

        public User(String name, Date birthDate) {
            this.name = name;
            this.birthDate = birthDate;
        }

        public String getName() {
            return name;
        }

        public Date getBirthDate() {
            return birthDate;
        }
    }

    private class Utils {
        private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        public Date str2date(String s) {
            if (s == null || s.isEmpty()) {
                return null;
            }
            try {
                return sdf.parse(s);
            } catch (Exception e) {
                return null;
            }
        }
    }
}
