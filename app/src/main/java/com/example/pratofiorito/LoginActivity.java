package com.example.pratofiorito;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class LoginActivity extends AppCompatActivity {
    private TextView username;
    private TextView password;
    private DAOUser dao;
    private User u;

    //creo l'oggetto utile alla comunicazione con il database
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dao = new DAOUser(this);
        username = findViewById(R.id.uname);
        password = findViewById(R.id.password);
    }

    //alla pressione del tasto login
    //controllo se il nome utente è valido ed esistente e in caso faccio partire la partita online
    public void login(View view) {
        setUser();
        if (invalidUser()) {
            return;
        }
        switch (dao.userExist(u)) {
            case 0:
                //utente inesistente
                Toast.makeText(getBaseContext(), "Utente inesistente", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                if (!u.getPassword().equals(dao.getPassword(u.getName()))) {
                    Toast.makeText(getBaseContext(), "Utente e password non corrispondono", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveUser();
                nextPage();
                break;
            default:
                //Errore di connessione
                Toast.makeText(getBaseContext(), "Impossibile completare l'operazione, controllare la connessione", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //ottengo dal layout il nome utente e la password e con quelle creo un utente
    private void setUser() {
        String name = username.getText().toString();
        String pass = password.getText().toString();
        u = new User(name, pass);
    }

    //alla pressione del tasto register
    //controllo se il nome utente è valido e non esistente e in caso faccio partire la partita online
    public void register(View view) {
        setUser();
        if (invalidUser()) {
            return;
        }
        switch (dao.userExist(u)) {
            case 0:
                createUser();
                saveUser();
                nextPage();
                break;
            case 1:
                Toast.makeText(getBaseContext(), "Utente già registrato", Toast.LENGTH_SHORT).show();
                break;
            default:
                //Errore di connessione
                Toast.makeText(getBaseContext(), "Impossibile completare l'operazione, controllare la connessione", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //restituisco se l'utente non sia valido
    private boolean invalidUser() {
        return !validName() || !validPassword();
    }

    //restituisco se il nome utente sia valido
    private boolean validName() {
        int len = u.getName().length();
        if (len < 3) {
            username.setError("Nome troppo corto");
            return false;
        } else if (len > 12) {
            username.setError("Nome troppo lungo");
            return false;
        }
        username.setError(null);
        return true;
    }

    //restituisco se la password dell'utente sia valida
    private boolean validPassword() {
        int len = u.getPassword().length();
        if (len < 3) {
            password.setError("Password troppo corta");
            return false;
        }
        password.setError(null);
        return true;
    }

    //avvio la partita online
    private void nextPage() {
        Intent i = new Intent(LoginActivity.this, MenuActivity.class);
        startActivity(i);
        finish();
    }

    //creo nel database un nuovo utente
    private void createUser() {
        dao.add(u);
    }

    //salvo localmente il nome utente
    private void saveUser() {
        String filePath = getFilesDir() + "user.txt";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            try {
                String nome = username.getText().toString();
                writer.write(nome);
            } catch (Exception e) {
                e.printStackTrace();
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //alla pressione del tasto indietro torno alla MainActivity
    @Override
    public void onBackPressed() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}