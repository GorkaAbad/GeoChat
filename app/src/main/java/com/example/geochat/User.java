package com.example.geochat;

class User {
    private String token;
    private String nick;
    private String name;
    public User(String _nick, String _name, String _token){
        this.nick = _nick;
        this.name = _name;
        this.token = _token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
