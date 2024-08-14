package Modelo;

public class Pais {
    private String nombrePais;
    private Continente continente;

    public Pais() {
    }
    
    

    public Pais(String nombrePais, Continente continente) {
        this.nombrePais = nombrePais;
        this.continente = continente;
    }

    // Getters y Setters
    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }

    public Continente getContinente() {
        return continente;
    }

    public void setContinente(Continente continente) {
        this.continente = continente;
    }
}
