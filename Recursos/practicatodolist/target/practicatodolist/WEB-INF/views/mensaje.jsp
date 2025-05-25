<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

    <!DOCTYPE html>
    <html>

    <head>
        <title>Inserción</title>
        <link rel="stylesheet" type="text/css" href="css/estilos.css" />
    </head>

    <body>
        <div class="container">
            <!--RECUPERO LOS DATOS QUE ME MANDA EL SERVLET-->
            <h1>RESULTADO DE LA OPERACIÓN</h1>
            <h2><a href=<!--a dónde me envía el enlace--><!--el texto será volver a inicio o volver a opciones--></a>
            </h2>
            <div class="mensaje">
                <%String mensaje=(String) request.getAttribute("mensaje");%>
                    <h2>
                        <%=mensaje %>
                    </h2>
            </div>
        </div>
    </body>

    </html>