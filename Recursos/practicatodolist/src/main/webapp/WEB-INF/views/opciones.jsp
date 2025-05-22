<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

    <!DOCTYPE html>
    <html>

    <head>
        <title>Opciones</title>
        <link rel="stylesheet" type="text/css" href="css/estilos.css" />
    </head>

    <body>
        <div class="container">
            <h1>¿QUÉ QUIERES HACER?</h1>

            <%String user=(String) request.getAttribute("user"); int id=(int) request.getAttribute("id");%>
                <h3>Hola <%=user%>
                </h3>

                <h2><a href="<%=request.getContextPath()%>/hello-servlet">Volver a la página de inicio</a></h2>
                <!--RECUPERO LOS DATOS QUE ME MANDA EL SERVLET-->

                <form action="opcionesL" method="post">
                    <input type="hidden" name="opcion" value="ver" />
                    <!--en value debes poner el id del usuario que habrás podido obtener en el servlet al iniciar sesión-->
                    <!--este id no puedes perderlo porque determina cualquier consulta a la base de datos-->
                    <input type="hidden" id="usuario" name="usuario" readonly value="<%= id%>" />
                    <input type="submit" value="Ver Tareas" />
                </form>
                <form action="opcionesL" method="post">
                    <input type="hidden" name="opcion" value="insertar" />
                    <label for="idU">Identificador Usuario:</label>
                    <!--en value debes poner el id del usuario que habrás podido obtener en el servlet al iniciar sesión-->
                    <!--este id no puedes perderlo porque determina cualquier consulta a la base de datos-->

                    <input type="text" id="usuario" name="usuario" readonly value="<%= id%>" />
                    <label for="categoria">Número categoría:</label>
                    <input type="text" id="categoria" name="categoria" />
                    <label for="titulo">Título:</label>
                    <input type="text" id="titulo" name="titulo" />
                    <label for="descripcion">Descripción:</label>
                    <input type="text" id="descripcion" name="descripcion" />

                    <input type="submit" value="Insertar Tarea" />
                </form>
                <form action="opcionesL" method="post">
                    <input type="hidden" name="opcion" value="eliminar" />
                    <label for="idU">Identificador Usuario:</label>
                    <!--en value debes poner el id del usuario que habrás podido obtener en el servlet al iniciar sesión-->
                    <!--este id no puedes perderlo porque determina cualquier consulta a la base de datos-->
                    <input type="text" id="usuario" name="usuario" readonly value="<%= id%>" />
                    <label for="categoria">Número categoría:</label>
                    <input type="text" id="categoria" name="categoria" />
                    <label for="titulo">Título:</label>
                    <input type="text" id="titulo" name="titulo" />
                    <input type="submit" value="Eliminar Tarea" />
                </form>
        </div>
    </body>

    </html>