package com.cybercom.dao.objects;

/**
 * User: Oskar Ferm <oskar.ferm@cybercom.com>
 * Date: 5/30/13
 * Time: 1:55 PM
 */
public class MuleApplication {
    private int id;
    private String name;
    private int muleServerId;

    public MuleApplication(final String name) {
        this.name = name;
    }

    public MuleApplication() {

    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getMuleServerId() {
        return muleServerId;
    }

    public void setMuleServerId(final int muleServerId) {
        this.muleServerId = muleServerId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final MuleApplication that = (MuleApplication) o;

        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
