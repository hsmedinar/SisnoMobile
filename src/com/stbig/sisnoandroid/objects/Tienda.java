package com.stbig.sisnoandroid.objects;

public class Tienda {
    private int id;
    private String name;
    private String ruc;
    private String email;
    private String telefono;
    private String latitud;
    private String longitud;

    public Tienda() {
        this.id = 0;
        this.name = "";
        this.ruc = "";
        this.email = "";
        this.telefono = "";
        this.latitud = "";
        this.longitud = "";
    }

    public Tienda(int id, String name, String ruc, String email, String telefono, String latitud, String longitud) {
        this.id = id;
        this.name = name;
        this.ruc = ruc;
        this.email = email;
        this.telefono = telefono;
        this.latitud = latitud;
        this.longitud = longitud;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRuc() {
        return ruc;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getLatitud() {
        return latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

}