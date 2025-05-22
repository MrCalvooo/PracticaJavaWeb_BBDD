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
            <%List<Tarea> listaTareas = (List) request.getAttribute("listaTareas");%>
                <% if (listaTareas==null || listaTareas.isEmpty()) { %>
                    <tr>
                        <td colspan="4">No hay tareas para mostrar.</td>
                    </tr>
                    <% } else {%>
                        <h2><a href="<%=request.getContextPath()%>/hello-servlet">Volver a opciones</a></h2>


                        <div class="container">

                            <h2 class="categoria-titulo"><!--nombre categoria--> : <!--identificador categoria--></h2>
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
                                    <% for (Tarea tarea : listaTareas) {%>
                                        <tr>
                                            <td>
                                                <%=tarea.getTitulo()%>
                                            </td>

                                            <td>
                                                <%=tarea.getDescripcion()%>
                                            </td>

                                            <td>
                                                <%= tarea.isCompletada() ? "Completada" : "Pendiente" %>
                                            </td>

                                            <td>
                                                <%=tarea.getFechaCreacion()%>
                                            </td>
                                        </tr>
                                        <%} }%>
                                </tbody>
                            </table>
                        </div>
        </body>

        </html>