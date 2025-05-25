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

    private String mensaje = "";
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

                // Informamos al usuario si la tarea se ha insertado correctamente
                request.setAttribute("mensaje", mensaje);

                // Enviamos el mensaje al JSP de mensaje
                request.getRequestDispatcher("/WEB-INF/views/mensaje.jsp").forward(request, response);
                break;
            }
            case "eliminar": {
                opcionElegida(opcion, user, categoriaID, tituloTarea, descripcionTarea);

                // Informamos al usuario si la tarea se ha eliminado correctamente
                request.setAttribute("mensaje", mensaje);

                // Enviamos el mensaje al JSP de mensaje
                request.getRequestDispatcher("/WEB-INF/views/mensaje.jsp").forward(request, response);
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
            case "eliminar":
                eliminar(categoriaID, tituloTarea, user);
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

            // Obtenemos el id de la categoria que es la clave del mapa
            int catID = Integer.parseInt(categoriaID);
            // Generamos un id único para la tarea buscando el máximo id existente en la tabla
            int idTarea = 1;
            try (Connection connection = DriverManager.getConnection(url); PreparedStatement psMax = connection.prepareStatement("SELECT MAX(id) FROM tareas"); ResultSet rs = psMax.executeQuery()) {
                if (rs.next()) {
                    // Almacenamos el nuevo id de la tarea
                    idTarea = rs.getInt(1) + 1;
                }
            } catch (SQLException e) {
                System.out.println("Error obteniendo el id máximo de tareas: " + e.getMessage());
            }
            System.out.println("ID de la tarea: " + idTarea);

            try (Connection connection = DriverManager.getConnection(url); PreparedStatement ps = connection.prepareStatement("insert into tareas(id, usuario_id, categoria_id, titulo, descripcion, completada, fecha_creacion) values(?, ?, ?, ?, ?, 0, CURRENT_TIMESTAMP)")) {
                ps.setInt(1, idTarea);
                ps.setInt(2, Integer.parseInt(user));
                ps.setInt(3, catID);
                ps.setString(4, tituloTarea);
                ps.setString(5, descripcionTarea);

                // Insertamos los datos
                int resultado = ps.executeUpdate();
                if (resultado > 0) {
                    mensaje = "Tarea insertada correctamente";
                } else {
                    mensaje = "Error al insertar la tarea";
                }
            } catch (Exception e) {
                System.out.println("Error al insertar la tarea");
                System.out.println(e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Error cargando el driver de la base de datos");
        }
    }

    public void eliminar(String categoriaID, String tituloTarea, String user) {
        try {
            Class.forName("org.sqlite.JDBC");

            try (Connection connection = DriverManager.getConnection(url); PreparedStatement ps = connection.prepareStatement("delete from tareas where categoria_id = ? and titulo = ? and usuario_id = ?");) {

                int catID = Integer.parseInt(categoriaID);
                int userID = Integer.parseInt(user);

                ps.setInt(1, catID);
                ps.setString(2, tituloTarea);
                ps.setInt(3, userID);

                int resultado = ps.executeUpdate();

                if (resultado > 0) {
                    mensaje = "Tarea eliminada correctamente";
                } else {
                    mensaje = "Error al eliminar la tarea";
                }

            } catch (SQLException e) {

            }
        } catch (ClassNotFoundException e) {

        }
    }
}
