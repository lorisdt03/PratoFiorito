package com.example.pratofiorito;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity {
    //String ConnectionResult;
    TextView user;
    TextView password;
    Connection conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user = findViewById(R.id.uname);
        password = findViewById(R.id.password);
        conn =new ConnectionHelper().connectionClass();
    }

    @Override
    public void onBackPressed() {}

    public void login(View view) {
        if(userExist()){
            saveUser();
            nextPage();
        }else{
            //utente inesistente
            Toast.makeText(getBaseContext(), "Utente inesistente", Toast.LENGTH_SHORT).show();
        }
    }

    public void register(View view) {
        if(!userExist()){
            createUser();
            saveUser();
            nextPage();
        }else{
            Toast.makeText(getBaseContext(), "Utente già registrato", Toast.LENGTH_SHORT).show();
        }
    }

    private void nextPage() {
        Intent i = new Intent(LoginActivity.this, MenuActivity.class);
        startActivity(i);
        finish();
    }

    private void createUser() {
        String query= "insert into user ()"+" values()";
        String name = user.getText().toString();
        String pass = password.getText().toString();
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString (1, name);
            preparedStmt.setString (2, pass);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void saveUser() {
        String filePath = getFilesDir()+"user.txt";
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            try{
                String nome = user.getText().toString();
                writer.write(nome);
            }catch (Exception e){
                e.printStackTrace();
            }
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean userExist() {
        return false;
        /*
        String name = user.getText().toString();
        try{
            PreparedStatement ps =
                    conn.prepareStatement("SELECT questid FROM completedQuests WHERE characterid = ? AND questid = ?");
            ps.setInt (1, name);
            ps.setInt (2, questId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;*/
        }
}