package com.example.geochat;

import java.util.ArrayList;

class Grupo {
    private int id;
    private ArrayList<User> users;

    public Grupo(int _id, ArrayList<User> _users){

        this.id = _id;
        this.users = _users;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
