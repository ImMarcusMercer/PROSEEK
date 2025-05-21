package com.example.proseekservices;

import java.util.ArrayList;
import java.util.List;

public class User {

    String username, email, location, userid;
    String number;
    int rating;
    boolean availability;

    List<String> services;

    public User(){}

    public User(String username, String email, String location, String userid, String number, int rating, boolean availability, List<String> services){
        this.username = username;
        this.email = email;
        this.location = location;
        this.userid = userid;
        this.number = number;
        this.rating = rating;
        this.availability = availability;
        this.services = services != null ? services : new ArrayList<>();

    }
    public String getUsername(){
        return username;
    }
    public String getEmail(){
        return email;
    }
    public String getLocation(){
        return location;
    }
    public String getUserid(){
        return userid;
    }
    public String getNumber(){
        return this.number;
    }
    public int getRating(){
        return this.rating;
    }
    public boolean getAvailability(){
        return availability;
    }
    public List<String> getServices(){
        return services;
    }
    public void addService(String service){
        services.add(service);
    }
    public void removeService(String service){
        services.remove(service);
    }

}
