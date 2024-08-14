package Controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBDD {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_personas?autoReconnect=true&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "0984315441k";

    private Connection connection;

    public ConexionBDD() {
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver MySQL cargado correctamente.");
        } catch (ClassNotFoundException e) {
            System.err.println("Error al cargar el driver de MySQL.");
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}
