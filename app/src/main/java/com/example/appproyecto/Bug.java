package com.example.appproyecto;

public class Bug {
    public int code;
    public String title;
    public String fecha;
    public String descripcion;
    public String adjunto;
    public int estado; //0=abierto, 1=procesando, 2=cerrado

    public static String[] estados = {"Abierto", "Procesando", "Cerrado"};

    public Bug(int code, String title, String fecha, String descripcion, String adjunto, int estado){
        this.code = code;
        this.title = title;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.adjunto = adjunto;
        this.estado = estado;
    }
}
