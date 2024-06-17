package com.project.gui.user;

import com.project.constants.Constants;
import java.net.Socket;

public class Client1 {
    private static final String SERVER_IP = Constants.serverIp;
    private static final int SERVER_PORT = Constants.serverPort;
    //send data to server
    public static void main(String[] args) {
       //just connect to server
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
            System.out.println("Connected to server");
        } catch (Exception e) {
            e.printStackTrace();
        }

}}
