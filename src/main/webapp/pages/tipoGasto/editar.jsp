<%@page contentType="text/html" errorPage="/erro.jsp"%> 
<%@page pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://flem.org.br/acesso-tag" prefix="acesso"%>
<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
        <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
        <link rel="stylesheet" type="text/css" href="css/800px.css" />
        <link href="css/displaytag.css" rel="stylesheet" type="text/css" />
        <html:javascript formName="tipoGastoForm" method="validar"/>

        <title><fmt:message key="aplicacao.nome" /></title>

    </head>
    <body>
        <div id="wrap">
            <jsp:include flush="false" page="/inc/header.jsp" />
            <jsp:include flush="false" page="/inc/sidebar.jsp" />
            <div id="content">
                <h2>Alterar Tipo de Gasto</h2>

                <html:form action="/TipoGasto.do?metodo=alterar" onsubmit="return validar(this);" method="post">
                    <html:hidden property="tipoGasto.id"/>
                    <table>

                        <tr>
                            <td>Descrição:</td>
                            <td><html:text size="40" property="tipoGasto.descricao" maxlength="40"/> </td>
                        </tr>
                        <tr>
                            <td>Status:</td>
                            <td>
                                <html:select property="tipoGasto.ativo">
                                    <html:option value="true">Ativo</html:option>
                                    <html:option value="false">Inativo</html:option>
                                </html:select>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <acesso:input tipo="salvar" action="TipoGasto.do" metodo="salvar" chave="admSalvarTipoGasto" styleClass="botao" />
                                &nbsp;
                                <acesso:input tipo="select" action="TipoGasto.do" metodo="cancelar" chave="*" styleClass="botao" onclick="document.location.href='TipoGasto.do'" />
                            </td>
                        </tr>                        

                    </table>
                </html:form>
                <jsp:include flush="false" page="/inc/footer.jsp" />            
            </div> 

        </div>
    </body>
</html>
