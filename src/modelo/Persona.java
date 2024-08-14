package Modelo;

public abstract class Persona implements Humano {
    private String nombre;
    private String apellido;
    private int cedula;
    private Direccion direccion;

    public Persona() {
    }
    
    

    public Persona(String nombre, String apellido, int cedula, Direccion direccion) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.direccion = direccion;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getCedula() {
        return cedula;
    }

    public void setCedula(int cedula) {
        this.cedula = cedula;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    @Override
    public String identificacion() {
        return "Cédula: " + cedula + ", Nombre: " + nombre + " " + apellido;
    }
}
