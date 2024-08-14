package Modelo;

public class Profesor extends Persona {
    private String despacho;

    public Profesor() {
    }
    
    

    public Profesor(String despacho, String nombre, String apellido, int cedula, Direccion direccion) {
        super(nombre, apellido, cedula, direccion);
        this.despacho = despacho;
    }

    // Getters y Setters
    public String getDespacho() {
        return despacho;
    }

    public void setDespacho(String despacho) {
        this.despacho = despacho;
    }

    @Override
    public String identificacion() {
        return super.identificacion() + ", Despacho: " + despacho;
    }

    @Override
    public String toString() {
        return "Profesor{" + "despacho=" + despacho + '}';
    }
}
