package com.example.pratofiorito;

public class Chronometer {

    private long begin;
    private long end;
    //salvo il tempo del sistema all'interno di begin
    public void start(){
        begin = System.currentTimeMillis();
    }
    //salvo il tempo del sistema all'interno di end
    public void stop(){
        end = System.currentTimeMillis();
    }
    //ottengo il tempo che è passato tra il richiamo di start e stop in secondi
    public double getSeconds() {
        return (end - begin) / 1000.0;
    }
}
