package Vista;

import Controlador.EstudianteControlador;
import Controlador.ProfesorControlador;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EstudianteControlador estudianteControlador = new EstudianteControlador();
        ProfesorControlador profesorControlador = new ProfesorControlador();

        while (true) {
            System.out.println("1. Insertar Estudiante");
            System.out.println("2. Listar Estudiantes");
            System.out.println("3. Insertar Profesor");
            System.out.println("4. Listar Profesores");
            System.out.println("5. Salir");
            System.out.print("Elija una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();  // Limpiar buffer

            switch (opcion) {
                case 1:
                    estudianteControlador.insertarEstudiante();
                    break;
                case 2:
                    estudianteControlador.listarEstudiantes();
                    break;
                case 3:
                    profesorControlador.insertarProfesor();
                    break;
                case 4:
                    profesorControlador.listarProfesores();
                    break;
                case 5:
                    System.out.println("Saliendo...");
                    return;
                default:
                    System.out.println("Opción no válida. Inténtelo de nuevo.");
            }
        }
    }
}
