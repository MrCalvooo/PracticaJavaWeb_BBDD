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
    private final Map<Integer, List<Tarea>> mapaTareas = new HashMap<>();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String opcion = request.getParameter("opcion");

        // Obtiene el id desde el parámetro 'id' o 'usuario'
        String user = request.getParameter("usuario");
        String categoriaID = request.getParameter("categoria");
        String tituloTarea = request.getParameter("titulo");
        String descripcionTarea = request.getParameter("descripcion");

        // Limpia la lista antes de cada consulta para evitar acumulación de tareas de peticiones anteriores
        listaTareas.clear();

        switch (opcion) {
            case "ver": {
                opcionElegida(opcion, user, categoriaID, tituloTarea, descripcionTarea);
                System.out.println(mapaTareas);

                // Establecemos como atributos tanto la lista como el mapa
                request.setAttribute("listaTareas", listaTareas);
                request.setAttribute("mapaTareas", mapaTareas);

                // Enviamos estos datos al JSP de verTareas
                request.getRequestDispatcher("/WEB-INF/views/verTareas.jsp").forward(request, response);
                break;
            }

            case "insertar": {
                opcionElegida(opcion, user, categoriaID, tituloTarea, descripcionTarea);
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
    public void opcionElegida(String opcion, String user, String categoriaID, String tituloTarea, String descripcionTarea) {
        switch (opcion) {
            case "ver":
                verTareas(user, mapaTareas);
                break;
            case "insertar":
                insertar(user, categoriaID, tituloTarea, descripcionTarea);
                break;
            default:
                break;
        }
    }

    public void verTareas(String user, Map<Integer, List<Tarea>> mapa) {
        try {
            Class.forName("org.sqlite.JDBC");

            // Conectamos a la base de datos y preparamos la consulta
            try (Connection connection = DriverManager.getConnection(url); PreparedStatement ps = connection.prepareStatement(
                    "SELECT T.titulo, T.descripcion, T.completada, T.fecha_creacion, T.categoria_id, C.nombre "
                    + "FROM TAREAS T "
                    + "INNER JOIN CATEGORIAS C ON T.categoria_id = C.id "
                    + "INNER JOIN USUARIOS U ON T.usuario_id = U.id "
                    + "WHERE U.id = ?"
            )) {

                System.out.println(user);

                // Ponemos como parametro el id del usuario
                ps.setString(1, user);

                try (ResultSet rs = ps.executeQuery()) {

                    // Y al ejecutar la consulta almacenamos en una lista todas las tareas que nos devuela el ResultSet
                    while (rs.next()) {
                        String titulo = rs.getString("titulo");
                        String descripcion = rs.getString("descripcion");
                        boolean completada = rs.getBoolean("completada");
                        String fechaCreacion = rs.getString("fecha_creacion");
                        int categoriaId = rs.getInt("categoria_id");
                        String nombreCategoria = rs.getString("nombre");

                        Tarea tarea = new Tarea(categoriaId, nombreCategoria, titulo, descripcion, completada, fechaCreacion);
                        listaTareas.add(tarea);

                        // Si conseguimos una tarea con un nombre de categoria que no se haya registrado anteriormente, creamos una nueva clave y asociamos a esa clave nuevas tareas
                        mapa.computeIfAbsent(categoriaId, k -> new ArrayList<>()).add(tarea);

                    }

                    System.out.println("Total tareas encontradas: " + listaTareas.size());

                } catch (SQLException e) {
                    System.out.println("Error al ejecutar la consulta");
                    System.out.println(e.getMessage());
                }
            } catch (SQLException e) {
                System.out.println("Error al conectar a la base de datos");
                System.out.println(e.getMessage());
            }
        } catch (ClassNotFoundException e) {
        }

    }

    public void insertar(String user, String categoriaID, String tituloTarea, String descripcionTarea) {
        try {
            Class.forName("org.sqlite.JDBC");

            // Conseguimos el numero de tareas con ese mismo ID de categoria
            // y le sumamos 1 para conseguir el nuevo ID de la tarea
            int idTarea = mapaTareas.get(categoriaID).size() + 1;

            System.out.println("ID de la tarea: " + idTarea);

            try (Connection connection = DriverManager.getConnection(url); PreparedStatement ps = connection.prepareStatement("insert into tareas(id, usuario_id, categoria_id, titulo, descripcion, completada, fecha_creacion) values(?, ?, ?, ?, ?, 0, CURRENT_TIMESTAMP)")) {
                ps.setInt(1, idTarea);
                ps.setInt(2, Integer.parseInt(user));
                ps.setInt(3, Integer.parseInt(categoriaID));
                ps.setString(4, tituloTarea);
                ps.setString(5, descripcionTarea);

            } catch (Exception e) {
                System.out.println("Error al insertar la tarea");
                System.out.println(e.getMessage());
            }
        } catch (ClassNotFoundException e) {

        }
    }
}
