package Controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ProfesorControlador {
    private ConexionBDD conexionBDD;

    public ProfesorControlador() {
        conexionBDD = new ConexionBDD();
    }

    public void insertarProfesor() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese Nombre del Profesor: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese Apellido del Profesor: ");
        String apellido = scanner.nextLine();
        System.out.print("Ingrese Cédula del Profesor: ");
        String cedula = scanner.nextLine();
        System.out.print("Ingrese Despacho del Profesor: ");
        String despacho = scanner.nextLine();

        String insertPersona = "INSERT INTO persona (nombre, apellido, cedula) VALUES (?, ?, ?)";
        String insertProfesor = "INSERT INTO profesor (idPersona, despacho) VALUES ((SELECT idPersona FROM persona WHERE cedula = ?), ?)";

        try (Connection connection = conexionBDD.getConnection()) {
            // Insertar Persona
            try (PreparedStatement stmt = connection.prepareStatement(insertPersona)) {
                stmt.setString(1, nombre);
                stmt.setString(2, apellido);
                stmt.setString(3, cedula);
                stmt.executeUpdate();
            }

            // Insertar Profesor
            try (PreparedStatement stmt = connection.prepareStatement(insertProfesor)) {
                stmt.setString(1, cedula);
                stmt.setString(2, despacho);
                stmt.executeUpdate();
            }

            System.out.println("Profesor insertado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al insertar profesor: " + e.getMessage());
        }
    }

    public void listarProfesores() {
        String sql = "SELECT p.nombre, p.apellido, p.cedula, pr.despacho "
                   + "FROM profesor pr "
                   + "JOIN persona p ON pr.idPersona = p.idPersona";

        try (Connection connection = conexionBDD.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                System.out.println("Nombre: " + rs.getString("nombre"));
                System.out.println("Apellido: " + rs.getString("apellido"));
                System.out.println("Cédula: " + rs.getString("cedula"));
                System.out.println("Despacho: " + rs.getString("despacho"));
                System.out.println("------------------------------");
            }

        } catch (SQLException e) {
            System.err.println("Error al listar profesores: " + e.getMessage());
        }
    }
}
