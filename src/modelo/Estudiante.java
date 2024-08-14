package Modelo;

public class Estudiante extends Persona {
    private int idEstudiante;

    public Estudiante() {
        super(null, null, 0, null);
    }
    
    

    public Estudiante(int idEstudiante, String nombre, String apellido, int cedula, Direccion direccion) {
        super(nombre, apellido, cedula, direccion);
        this.idEstudiante = idEstudiante;
    }

    // Getters y Setters
    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    @Override
    public String identificacion() {
        return super.identificacion() + ", ID Estudiante: " + idEstudiante;
    }

    @Override
    public String toString() {
        return "Estudiante{" + "idEstudiante=" + idEstudiante + '}';
    }
}
