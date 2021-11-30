package com.example.pratofiorito;

import android.annotation.SuppressLint;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionHelper {
    Connection con;
    String user_name;
    String password;
    String ip;
    String database;
    String port;
    @SuppressLint("NewApi")
    public Connection connectionClass(){
        ip = "85.10.205.173";
        password = "E9R3DykgEt6ikFw";
        user_name="lorisdt03";
        database="prato_fiorito";
        port = "3306";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection c = null;
        String cUrl = null;
        try {
            cUrl = "jdbc:mysql://"+ip+":"+port+";databasename="+database+";user="+user_name+";password="+password+";";
            c= DriverManager.getConnection(cUrl);
        }catch (Exception e){
            e.printStackTrace();
        }

        return c;
    }
}
