package Controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        String insertEstudiante = "INSERT INTO estudiante (idPersona) VALUES (?)";

        try (Connection connection = conexionBDD.getConnection()) {
            // Insertar Continente si no existe
            try (PreparedStatement stmt = connection.prepareStatement(insertContinente)) {
                stmt.setString(1, nombreContinente);
                stmt.executeUpdate();
            }

            // Obtener idContinente
            int idContinente;
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

            // Obtener o Insertar País
            int idPais;
            try (PreparedStatement stmt = connection.prepareStatement(selectPaisId)) {
                stmt.setString(1, nombrePais);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        idPais = rs.getInt("idPais");
                    } else {
                        try (PreparedStatement stmt2 = connection.prepareStatement(insertPais, Statement.RETURN_GENERATED_KEYS)) {
                            stmt2.setString(1, nombrePais);
                            stmt2.setInt(2, idContinente);
                            stmt2.executeUpdate();
                            
                            // Obtener el idPais generado
                            try (ResultSet rs2 = stmt2.getGeneratedKeys()) {
                                if (rs2.next()) {
                                    idPais = rs2.getInt(1);
                                } else {
                                    throw new SQLException("Error al obtener el ID del país.");
                                }
                            }
                        }
                    }
                }
            }

            // Insertar Persona
            int idPersona;
            try (PreparedStatement stmt = connection.prepareStatement(insertPersona, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, nombre);
                stmt.setString(2, apellido);
                stmt.setString(3, cedula);
                stmt.executeUpdate();
                
                // Obtener el idPersona generado
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        idPersona = rs.getInt(1);
                    } else {
                        throw new SQLException("Error al obtener el ID de la persona.");
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

            // Asociar Dirección con Persona
            try (PreparedStatement stmt = connection.prepareStatement("UPDATE persona SET idDireccion = (SELECT idDireccion FROM direccion WHERE calle = ? AND ciudad = ? AND codPostal = ? AND idPais = ?) WHERE idPersona = ?")) {
                stmt.setString(1, calle);
                stmt.setString(2, ciudad);
                stmt.setInt(3, codPostal);
                stmt.setInt(4, idPais);
                stmt.setInt(5, idPersona);
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
        String sql = "SELECT p.nombre, p.apellido, d.calle, d.ciudad, d.codPostal, pa.nombrePais "
                   + "FROM estudiante e "
                   + "JOIN persona p ON e.idPersona = p.idPersona "
                   + "JOIN direccion d ON p.idDireccion = d.idDireccion "
                   + "JOIN pais pa ON d.idPais = pa.idPais";

        try (Connection connection = conexionBDD.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            boolean resultsFound = false;
            while (rs.next()) {
                resultsFound = true;
                System.out.println("Nombre: " + rs.getString("nombre"));
                System.out.println("Apellido: " + rs.getString("apellido"));
                System.out.println("Calle: " + rs.getString("calle"));
                System.out.println("Ciudad: " + rs.getString("ciudad"));
                System.out.println("Código Postal: " + rs.getInt("codPostal"));
                System.out.println("País: " + rs.getString("nombrePais"));
                System.out.println("------------------------------");
            }

            if (!resultsFound) {
                System.out.println("No se encontraron estudiantes.");
            }

        } catch (SQLException e) {
            System.err.println("Error al listar estudiantes: " + e.getMessage());
        }
    }
}
