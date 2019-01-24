package com.jahnelgroup.spel;

public class AbstractUser {

    protected Long id;

    public AbstractUser() {

    }

    public AbstractUser(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AbstractUser{" +
                "id=" + id +
                '}';
    }
}
