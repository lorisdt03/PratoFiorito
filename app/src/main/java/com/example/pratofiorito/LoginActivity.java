package com.example.pratofiorito;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;

public class LoginActivity extends AppCompatActivity {
    //String ConnectionResult;
    TextView username;
    TextView password;
    DAOUser dao;
    User u;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dao = new DAOUser();
        username = findViewById(R.id.uname);
        password = findViewById(R.id.password);
    }

    @Override
    public void onBackPressed() {}

    public void login(View view) {
        setUser();
        if(dao.userExist(u)){
            if(!u.getPassword().equals(dao.getPassword(u.getName()))){
                Toast.makeText(getBaseContext(), "Utente e password non corrispondono", Toast.LENGTH_SHORT).show();
                return;
            }
            saveUser();
            nextPage();
        }else{
            //utente inesistente
            Toast.makeText(getBaseContext(), "Utente inesistente", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUser() {
        String name = username.getText().toString();
        String pass = password.getText().toString();
        u= new User(name,pass);
    }

    public void register(View view) {
        setUser();
        if(!dao.userExist(u)){
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
        dao.add(u);
    }

    private void saveUser() {
        String filePath = getFilesDir()+"user.txt";
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            try{
                String nome = username.getText().toString();
                writer.write(nome);
            }catch (Exception e){
                e.printStackTrace();
            }
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}