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

        <html:javascript formName="companhiaForm" method="validar"/>
        
        <title><fmt:message key="aplicacao.nome" /></title>
        
    </head>
    <body>
        <div id="wrap">
            <jsp:include flush="false" page="/inc/header.jsp" />
            <jsp:include flush="false" page="/inc/sidebar.jsp" />
            <div id="content">
                <h2>Nova Companhia de Viagem</h2>
                <html:form action="/Companhia.do?metodo=adicionar" onsubmit="return validar(this);" method="post"  >
                    <table>
                        
                        <tr>
                            <td>Nome:</td>
                            <td><html:text size="30" property="companhia.nome"/> </td>
                        </tr>
                        
                        <tr>
                            <td>Descrição:</td>
                            <td><html:text size="80" property="companhia.descricao"/> </td>
                        </tr>
                        
                        <tr>
                            <td colspan="2">
                                <acesso:input tipo="salvar" action="Companhia.do" metodo="salvar" chave="admSalvarCompViagem" styleClass="botao" />
                                &nbsp;
                                <acesso:input tipo="select" action="Companhia.do" metodo="cancelar" chave="*" styleClass="botao" onclick="document.location.href='Companhia.do'" />
                            </td>
                        </tr>                        
                    
                </table> 
                </html:form>
                <jsp:include flush="false" page="/inc/footer.jsp" />            
            </div> 
            
        </div>
    </body>
</html>
