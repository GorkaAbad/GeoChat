package com.example.geochat;

import java.util.ArrayList;

class Grupo {
    private String id;
    private ArrayList<User> users;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Grupo(String _id, String _name, ArrayList<User> _users){

        this.id = _id;
        this.users = _users;
        this.name = _name;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
