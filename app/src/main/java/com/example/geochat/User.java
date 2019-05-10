package com.example.geochat;

class User {
    private String token;
    private String nick;
    private String name;
    private String password;

    public User(String _nick, String _name, String _token, String _pass){
        this.nick = _nick;
        this.name = _name;
        this.token = _token;
        this.password = _pass;

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
