<%@ page isErrorPage="true"%>
<%@page contentType="text/html"%>
<%@page pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
        <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
        <link rel="stylesheet" type="text/css" href="css/800px.css" />
        <link href="css/displaytag.css" rel="stylesheet" type="text/css" />
        <title><fmt:message key="aplicacao.nome" /></title>
    </head>
    <body>
         <div id="wrap">
            <jsp:include flush="false" page="inc/header.jsp" />
            <jsp:include flush="false" page="inc/sidebar.jsp" />
            <div id="content">
                <h2><fmt:message key="aplicacao.nome" /></h2>
                 <table width="540px">
                    <tr>
                        <td >
                            <span class="erro">Entre em contato com o suporte.</span>
                        </td>
                    </tr>
                    <tr>
                        <td >
                            &nbsp;
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <b>Classe:</b>
                        </td>
                        </tr>
                    <tr>
                        <td>
                            <%= exception != null ? exception.getClass().getName() : "404" %>&nbsp;
                        </td>
                    </tr>
                    <tr>
                        <td >
                            &nbsp;
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <b>Descrição:</b>
                        </td>
                        </tr>
                    <tr>
                        <td>
                            <%= exception != null ? exception.getMessage() : "A página requisitada não existe." %>&nbsp;
                        </td>
                    </tr>
                    <tr>
                        <td >
                            &nbsp;
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <b>Pilha:</b>
                        </td>
                        </tr>
                    <tr>
                        <td>
                            <% if (exception != null) { exception.printStackTrace(new java.io.PrintWriter(out)); }%>&nbsp;
                        </td>
                    </tr>
                </table>
            </div>

            <jsp:include flush="false" page="inc/footer.jsp" />

        </div>
    </body>
</html>
