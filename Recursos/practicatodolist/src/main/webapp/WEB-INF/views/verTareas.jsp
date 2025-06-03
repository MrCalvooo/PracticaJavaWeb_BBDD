<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="es.damut12.model.Tarea, java.util.List, java.util.Map, java.util.HashMap, java.util.ArrayList" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Tareas del Usuario</title>
            <link rel="stylesheet" type="text/css" href="css/estilos.css">
        </head>

        <body>
            <h2><a href="<%=request.getContextPath()%>/hello-servlet">Volver a opciones</a></h2>
            <!-- Recuperamos el usuario y su ID desde los atributos de la solicitud -->
            <% Map<Integer, List<Tarea>> mapa = (Map<Integer, List<Tarea>>) request.getAttribute("mapaTareas");
                    // Si el mapa es nulo mostrmos que no hay tareas
                    if (mapa == null || mapa.isEmpty()) { %>
                    <tr>
                        <td colspan="4">No hay tareas para mostrar.</td>
                    </tr>
                    <% } else {%>
                        <!-- Recorremos el mapa de tareas -->
                        <%for (Map.Entry<Integer, List<Tarea>> entry : mapa.entrySet()) {

                            Integer catId = entry.getKey();

                            // Obtenemos la lista de tareas asociada a la categoría
                            List<Tarea> tareas = entry.getValue(); %>
                                <div class="container">

                                    <h2 class="categoria-titulo">
                                        Categoría ID: <%= catId %> : <%= tareas.get(0).getCategoria() %>
                                    </h2>
                                    <table>
                                        <thead>
                                            <tr>
                                                <th>Título</th>
                                                <th>Descripción</th>
                                                <th>Estado</th>
                                                <th>Fecha de creación</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <!--Recorremos la lista (que es el valor del mapa) y mostramos sus datos-->
                                            <% for (Tarea tarea : tareas) { %>
                                                <tr>
                                                    <td>
                                                        <%= tarea.getTitulo() %>
                                                    </td>
                                                    <td>
                                                        <%= tarea.getDescripcion() %>
                                                    </td>
                                                    <td>
                                                        <%= tarea.isCompletada() %>
                                                    </td>
                                                    <td>
                                                        <%= tarea.getFechaCreacion() %>
                                                    </td>
                                                </tr>
                                                <% } %>
                                        </tbody>
                                    </table>
                                </div>

                                <% } %>
                                    <% } %>
        </body>

        </html>