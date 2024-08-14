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

        System.out.print("Ingrese Calle de la Dirección: ");
        String calle = scanner.nextLine();
        System.out.print("Ingrese Ciudad de la Dirección: ");
        String ciudad = scanner.nextLine();
        System.out.print("Ingrese Código Postal de la Dirección: ");
        int codPostal = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer
        System.out.print("Ingrese Nombre del País: ");
        String nombrePais = scanner.nextLine();

        String insertPersona = "INSERT INTO persona (nombre, apellido, cedula) VALUES (?, ?, ?)";
        String insertPais = "INSERT INTO pais (nombrePais) VALUES (?)";
        String selectPaisId = "SELECT idPais FROM pais WHERE nombrePais = ?";
        String insertDireccion = "INSERT INTO direccion (calle, ciudad, codPostal, idPais) VALUES (?, ?, ?, ?)";
        String selectDireccionId = "SELECT idDireccion FROM direccion WHERE calle = ? AND ciudad = ? AND codPostal = ? AND idPais = ?";
        String insertProfesor = "INSERT INTO profesor (idPersona, despacho) VALUES (?, ?)";

        try (Connection connection = conexionBDD.getConnection()) {
            // Insertar País
            int idPais;
            try (PreparedStatement stmt = connection.prepareStatement(insertPais, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, nombrePais);
                stmt.executeUpdate();
                
                // Obtener idPais generado
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        idPais = rs.getInt(1);
                    } else {
                        throw new SQLException("Error al obtener el ID del país.");
                    }
                }
            }

            // Insertar Dirección
            int idDireccion;
            try (PreparedStatement stmt = connection.prepareStatement(insertDireccion, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, calle);
                stmt.setString(2, ciudad);
                stmt.setInt(3, codPostal);
                stmt.setInt(4, idPais);
                stmt.executeUpdate();
                
                // Obtener idDireccion generado
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        idDireccion = rs.getInt(1);
                    } else {
                        throw new SQLException("Error al obtener el ID de la dirección.");
                    }
                }
            }

            // Insertar Persona
            int idPersona;
            try (PreparedStatement stmt = connection.prepareStatement(insertPersona, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, nombre);
                stmt.setString(2, apellido);
                stmt.setString(3, cedula);
                stmt.executeUpdate();
                
                // Obtener idPersona generado
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        idPersona = rs.getInt(1);
                    } else {
                        throw new SQLException("Error al obtener el ID de la persona.");
                    }
                }
            }

            // Asociar Dirección con Persona
            try (PreparedStatement stmt = connection.prepareStatement("UPDATE persona SET idDireccion = ? WHERE idPersona = ?")) {
                stmt.setInt(1, idDireccion);
                stmt.setInt(2, idPersona);
                stmt.executeUpdate();
            }

            // Insertar Profesor
            try (PreparedStatement stmt = connection.prepareStatement(insertProfesor)) {
                stmt.setInt(1, idPersona);
                stmt.setString(2, despacho);
                stmt.executeUpdate();
            }

            System.out.println("Profesor insertado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al insertar profesor: " + e.getMessage());
        }
    }
    
    public void listarProfesores() {
        String sql = "SELECT p.nombre, p.apellido, p.cedula, pr.despacho, d.calle, d.ciudad, d.codPostal, pa.nombrePais "
                   + "FROM profesor pr "
                   + "JOIN persona p ON pr.idPersona = p.idPersona "
                   + "JOIN direccion d ON p.idDireccion = d.idDireccion "
                   + "JOIN pais pa ON d.idPais = pa.idPais";

        try (Connection connection = conexionBDD.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                System.out.println("Nombre: " + rs.getString("nombre"));
                System.out.println("Apellido: " + rs.getString("apellido"));
                System.out.println("Cédula: " + rs.getString("cedula"));
                System.out.println("Despacho: " + rs.getString("despacho"));
                System.out.println("Calle: " + rs.getString("calle"));
                System.out.println("Ciudad: " + rs.getString("ciudad"));
                System.out.println("Código Postal: " + rs.getInt("codPostal"));
                System.out.println("País: " + rs.getString("nombrePais"));
                System.out.println("------------------------------");
            }

        } catch (SQLException e) {
            System.err.println("Error al listar profesores: " + e.getMessage());
        }
    }
}
