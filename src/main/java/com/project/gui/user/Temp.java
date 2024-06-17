package com.project.gui.user;


import com.google.gson.Gson;

public class Temp {
    //just implemennt gson serialization and deserialization with the help of UserRegistrationRequest class

    public static void main(String[] args) {
        //create a UserRegistrationRequest object
        Gson gson = new Gson();
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest("register", "email", "password");
        //convert the object to json string
        String json = gson.toJson(userRegistrationRequest);
        //print the json string
        System.out.println(json);
        //convert the json string back to object
        UserRegistrationRequest userRegistrationRequest1 = gson.fromJson(json, UserRegistrationRequest.class);
        //print the object
        System.out.println(userRegistrationRequest1);

}}
