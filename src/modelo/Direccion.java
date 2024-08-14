package Modelo;

public class Direccion {
    private String calle;
    private String ciudad;
    private int codPostal;
    private Pais pais;

    public Direccion() {
    }
    
    

    public Direccion(String calle, String ciudad, int codPostal, Pais pais) {
        this.calle = calle;
        this.ciudad = ciudad;
        this.codPostal = codPostal;
        this.pais = pais;
    }

    // Getters y Setters
    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public int getCodPostal() {
        return codPostal;
    }

    public void setCodPostal(int codPostal) {
        this.codPostal = codPostal;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }
}
