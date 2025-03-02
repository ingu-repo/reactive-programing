package ingu.flux.model;

import lombok.Getter;
import java.util.Date;

@Getter
public class User {
    public static final int ID = 1;
    public static final int NAME = 2;
    public static final int LAST_NAME = 3;
    public static final int COUNTRY = 4;
    public static final int BIRTHDAY = 5;
    private int id;
    private String name;
    private String lastName;
    private String country;
    private Date birthday;

    public User(int id, String name, String lastName, String country, Date birthday) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.country = country;
        this.birthday = birthday;
    }
}
