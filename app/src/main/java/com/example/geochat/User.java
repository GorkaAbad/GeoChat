package com.example.geochat;

class User {
    private String token;
    private String nick;
    private String name;
    private String lat;
    private String longitude;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public User(String _nick, String _name, String _token, String _lat, String _longitude) {
        this.nick = _nick;
        this.name = _name;
        this.token = _token;
        this.lat = _lat;
        this.longitude = _longitude;
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
