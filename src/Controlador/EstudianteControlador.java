package Controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class EstudianteControlador {
    private ConexionBDD conexionBDD;

    public EstudianteControlador() {
        conexionBDD = new ConexionBDD();
    }

    public void insertarEstudiante() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese Nombre del Estudiante: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese Apellido del Estudiante: ");
        String apellido = scanner.nextLine();
        System.out.print("Ingrese Cédula del Estudiante: ");
        String cedula = scanner.nextLine();
        System.out.print("Ingrese Calle de la Dirección: ");
        String calle = scanner.nextLine();
        System.out.print("Ingrese Ciudad de la Dirección: ");
        String ciudad = scanner.nextLine();
        System.out.print("Ingrese Código Postal de la Dirección: ");
        int codPostal = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer
        System.out.print("Ingrese Nombre del País: ");
        String nombrePais = scanner.nextLine();
        System.out.print("Ingrese Nombre del Continente: ");
        String nombreContinente = scanner.nextLine();

        String insertPersona = "INSERT INTO persona (nombre, apellido, cedula) VALUES (?, ?, ?)";
        String insertContinente = "INSERT IGNORE INTO continente (nombreContinente) VALUES (?)";
        String selectContinenteId = "SELECT idContinente FROM continente WHERE nombreContinente = ?";
        String insertPais = "INSERT INTO pais (nombrePais, idContinente) VALUES (?, ?)";
        String selectPaisId = "SELECT idPais FROM pais WHERE nombrePais = ?";
        String insertDireccion = "INSERT INTO direccion (calle, ciudad, codPostal, idPais) VALUES (?, ?, ?, ?)";
        String insertEstudiante = "INSERT INTO estudiante (idPersona) VALUES ((SELECT idPersona FROM persona WHERE cedula = ?))";

        try (Connection connection = conexionBDD.getConnection()) {
            // Insertar Continente si no existe
            try (PreparedStatement stmt = connection.prepareStatement(insertContinente)) {
                stmt.setString(1, nombreContinente);
                stmt.executeUpdate();
            }

            // Obtener idContinente
            int idContinente = 0;
            try (PreparedStatement stmt = connection.prepareStatement(selectContinenteId)) {
                stmt.setString(1, nombreContinente);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        idContinente = rs.getInt("idContinente");
                    } else {
                        throw new SQLException("Continente no encontrado.");
                    }
                }
            }

            // Insertar País si no existe
            int idPais = 0;
            try (PreparedStatement stmt = connection.prepareStatement(selectPaisId)) {
                stmt.setString(1, nombrePais);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        idPais = rs.getInt("idPais");
                    } else {
                        try (PreparedStatement stmt2 = connection.prepareStatement(insertPais)) {
                            stmt2.setString(1, nombrePais);
                            stmt2.setInt(2, idContinente);
                            stmt2.executeUpdate();
                        }
                        // Obtener idPais después de insertar
                        try (ResultSet rs2 = stmt.executeQuery()) {
                            if (rs2.next()) {
                                idPais = rs2.getInt("idPais");
                            } else {
                                throw new SQLException("Error al obtener el idPais.");
                            }
                        }
                    }
                }
            }

            // Insertar Persona
            int idPersona = 0;
            try (PreparedStatement stmt = connection.prepareStatement(insertPersona)) {
                stmt.setString(1, nombre);
                stmt.setString(2, apellido);
                stmt.setString(3, cedula);
                stmt.executeUpdate();
                
                // Obtener idPersona
                try (PreparedStatement stmt2 = connection.prepareStatement("SELECT idPersona FROM persona WHERE cedula = ?")) {
                    stmt2.setString(1, cedula);
                    try (ResultSet rs = stmt2.executeQuery()) {
                        if (rs.next()) {
                            idPersona = rs.getInt("idPersona");
                        } else {
                            throw new SQLException("Error al obtener el idPersona.");
                        }
                    }
                }
            }

            // Insertar Dirección
            try (PreparedStatement stmt = connection.prepareStatement(insertDireccion)) {
                stmt.setString(1, calle);
                stmt.setString(2, ciudad);
                stmt.setInt(3, codPostal);
                stmt.setInt(4, idPais);
                stmt.executeUpdate();
            }

            // Insertar Estudiante
            try (PreparedStatement stmt = connection.prepareStatement(insertEstudiante)) {
                stmt.setInt(1, idPersona);
                stmt.executeUpdate();
            }

            System.out.println("Estudiante insertado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al insertar estudiante: " + e.getMessage());
        }
    }

    public void listarEstudiantes() {
        String sql = "SELECT p.nombre, p.apellido, p.cedula "
                   + "FROM estudiante e "
                   + "JOIN persona p ON e.idPersona = p.idPersona";

        try (Connection connection = conexionBDD.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                System.out.println("Nombre: " + rs.getString("nombre"));
                System.out.println("Apellido: " + rs.getString("apellido"));
                System.out.println("Cédula: " + rs.getString("cedula"));
                System.out.println("------------------------------");
            }

        } catch (SQLException e) {
            System.err.println("Error al listar estudiantes: " + e.getMessage());
        }
    }
}
