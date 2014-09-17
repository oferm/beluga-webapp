package com.cybercom.dao.objects;

/**
 * User: Oskar Ferm <oskar.ferm@cybercom.com>
 * Date: 5/8/13
 * Time: 12:27 PM
 */
public class Property {
    private int id;
    private String name;
    private String value;


    public Property() {
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Property{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", value='" + value + '\''
                + '}';
    }
}
