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
            <% Map<Integer, List<Tarea>> mapa = (Map<Integer, List<Tarea>>) request.getAttribute("mapaTareas");
                    if (mapa == null || mapa.isEmpty()) { %>
                    <tr>
                        <td colspan="4">No hay tareas para mostrar.</td>
                    </tr>
                    <% } else { %>
                        <h2><a href="<%=request.getContextPath()%>/hello-servlet">Volver a opciones</a></h2>
                        <% for (Map.Entry<Integer, List<Tarea>> entry : mapa.entrySet()) {
                            Integer catId = entry.getKey();
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