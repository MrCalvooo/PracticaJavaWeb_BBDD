package es.damut12.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.damut12.model.Tarea;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(value = "/opcionesL")
public class logicaOpciones extends HttpServlet {

    private final String url = "jdbc:sqlite:D:\\Usuarios\\calvo\\Desktop\\DAM\\PracticaJavaWeb_BBDD\\Recursos\\tareas.db";
    private final List<Tarea> listaTareas = new ArrayList<>();
    private final Map<String, List<Tarea>> mapaTareas = new HashMap<>();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String opcion = request.getParameter("opcion");

        // Obtiene el id desde el parámetro 'id' o 'usuario'
        String user = request.getParameter("usuario");

        // Limpia la lista antes de cada consulta para evitar acumulación de tareas de peticiones anteriores
        listaTareas.clear();

        switch (opcion) {
            case "ver": {
                opcionElegida(opcion, user, mapaTareas);
                System.out.println(mapaTareas);
                request.setAttribute("listaTareas", listaTareas);
                request.setAttribute("mapaTareas", mapaTareas); // Pasa el mapa al JSP
                request.getRequestDispatcher("/WEB-INF/views/verTareas.jsp").forward(request, response);
                break;
            }

            case "insertar": {
                //recojo informacion de opciones.jsp
                //lógica relacionada con la inserción. Pensar en gestionar llamando a otros métodos para modularizar
                //envío al jsp de respuesta lo que quiera
                //llamo al jsp que quiero que se muestre
                break;
            }
            case "eliminar": {
                //recojo informacion de opciones.jsp
                //lógica relacionada con la eliminación. Pensar en gestionar llamando a otros métodos para modularizar
                //envío al jsp de respuesta lo que quiera
                //llamo al jsp que quiero que se muestre
                break;
            }
        }
    }

    // estructura métodos que modularizan las opciones
    public void opcionElegida(String opcion, String user, Map<String, List<Tarea>> mapa) {
        mapa.clear();
        listaTareas.clear(); // Asegura que también se limpie la lista
        try {
            Class.forName("org.sqlite.JDBC");

            // Intentamos conectar a la BBDD
            try (Connection connection = DriverManager.getConnection(url)) {
                System.out.println("Conectado a BBDD");

                switch (opcion) {
                    case "ver": {

                        try (PreparedStatement ps = connection.prepareStatement("SELECT C.ID, C.NOMBRE, T.TITULO, T.DESCRIPCION, T.COMPLETADA, T.FECHA_CREACION "
                                + "FROM TAREAS T "
                                + "JOIN CATEGORIAS C ON T.CATEGORIA_ID = C.ID "
                                + "JOIN USUARIOS U ON T.USUARIO_ID = U.ID "
                                + "WHERE U.NOMBRE_USUARIO = ?;")) {

                            // Establecemos el nombre del usuario
                            ps.setString(1, user);

                            try (ResultSet rs = ps.executeQuery()) {
                                while (rs.next()) {
                                    String titulo = rs.getString("titulo");
                                    System.out.println(titulo);
                                    String descripcion = rs.getString("descripcion");
                                    System.out.println(descripcion);
                                    boolean completada = rs.getBoolean("completada");
                                    System.out.println(completada);
                                    String fechaCreacion = rs.getString("fecha_creacion");
                                    System.out.println(fechaCreacion);
                                    String nombreCategoria = rs.getString("nombre");
                                    System.out.println(nombreCategoria);
                                    int idCategoria = rs.getInt("categoria_id");
                                    System.out.println(idCategoria);

                                    Tarea t = new Tarea(idCategoria, nombreCategoria, titulo, descripcion, completada, fechaCreacion);

                                    // Agrupar por nombre de categoría
                                    mapa.computeIfAbsent(nombreCategoria, k -> new ArrayList<>()).add(t);
                                }

                            } catch (SQLException e) {
                                System.out.println("No se realizo la consulta");
                            }
                        } catch (SQLException e) {
                            System.out.println("No se ha preparado la consulta");
                        }

                        break;
                    }

                    default:
                        break;
                }
            } catch (Exception e) {

            }
        } catch (ClassNotFoundException e) {

        }
    }
}
