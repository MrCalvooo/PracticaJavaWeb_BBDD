/*
 * Copyright 2020 Diego Silva <diego.silva at apuntesdejava.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.damut12.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {

    // URL de la base de datos para windows
    private final String url = "jdbc:sqlite:D:\\Usuarios\\calvo\\Desktop\\DAM\\JavaWeb\\ConexionesBBDD\\PracticaEvaluable\\Recursos\\tareas.db";
    private String mensaje = "";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Class.forName("org.sqlite.JDBC");

            // Intentamos conectarnos y preparamos una consulta
            try (Connection connection = DriverManager.getConnection(url); PreparedStatement ps = connection.prepareStatement("select nombre_usuario, password, id from usuarios where nombre_usuario = ? and password = ?")) {
                // Ponemos los parametros de la consulta
                ps.setString(1, request.getParameter("usuario"));
                ps.setString(2, request.getParameter("pass"));

                // Ejecutamos la consulta
                try (ResultSet rs = ps.executeQuery()) {
                    String user = "", pass = "";
                    int id = 0;
                    // Si retorna datos los almacenamos
                    if (rs.next()) {
                        user = rs.getString(1);
                        pass = rs.getString(2);
                        id = rs.getInt(3);

                        System.out.println(user + " " + pass + " " + id);

                        // Preparamos los datos para mandarlos a la siguiente pagina
                        request.setAttribute("user", user);
                        request.setAttribute("pass", pass);
                        request.setAttribute("id", id);

                        request.getRequestDispatcher("/WEB-INF/views/opciones.jsp").forward(request, response);
                    } else {
                        // Al no encontrarse los datos se informa al usuario
                        mensaje = "Usuario o contraseña incorrectos";
                        request.setAttribute("mensaje", mensaje);
                        request.getRequestDispatcher("/WEB-INF/views/mensaje.jsp").forward(request, response);
                    }

                } catch (SQLException e) {
                    mensaje = "No se realizo la consulta";
                    request.getRequestDispatcher("/WEB-INF/views/mensaje.jsp").forward(request, response);
                    System.out.println("No se realizo la consulta");
                    System.out.println(e.getMessage());
                }
            } catch (SQLException e) {
                System.out.println("Error al conectar a la base de datos");
                System.out.println(e.getMessage());
                mensaje = "Error al conectar a la base de datos";
                request.setAttribute("mensaje", mensaje);
                request.getRequestDispatcher("/WEB-INF/views/mensaje.jsp").forward(request, response);
            }
        } catch (ClassNotFoundException e) {

        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // los .jsp incluidos en /WEB-INF/views no son visibles desde el navegador por
        // cuestiones de seguridad
        // el servidor Tomcat no permite el acceso directo a ellos
        // por eso, si queremos redirigir desde esos.jsp, tendremos que hacerlo pasando
        // por el Servlet
        // esas redirecciones el Servlet las gestiona desde el método doGet
        // debes controlar aquí si quieres enviar algo a otro .jsp
        // y el .forward al .jsp que corresponda

        request.getRequestDispatcher("index.jsp").forward(request, response);

    }

}
