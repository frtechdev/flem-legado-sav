<%@page contentType="text/html" errorPage="/erro.jsp"%> 
<%@page pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@taglib uri="http://flem.org.br/acesso-tag" prefix="acesso" %>
<%@taglib uri="http://flem.org.br/mensagem-tag" prefix="msg"%>
<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
        <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
        <link rel="stylesheet" type="text/css" href="css/800px.css" />
        <link href="css/displaytag.css" rel="stylesheet" type="text/css" />
        <title><fmt:message key="aplicacao.nome" /></title>

        <script src="<%=request.getContextPath()%>/js/functions.js"></script>
        
    </head>
    <body>
        <div id="wrap">
            <jsp:include flush="false" page="/inc/header.jsp" />
            <jsp:include flush="false" page="/inc/sidebar.jsp" />
            <div id="content">
                
                <h2>Lista de Companhias</h2>
                <html:form action="/Companhia.do" method="post">
                    <div style="width:100%; text-align:right;">
                        <acesso:input tipo="select" action="Companhia.do" metodo="novo" chave="admNovoCompViagem" styleClass="botao"  />
                        &nbsp;
                        <acesso:input tipo="delete" action="Companhia.do" metodo="excluir" chave="admExcluirCompViagem" styleClass="botao" />
                    </div>
                        <display:table id="companhia" name="lista" defaultsort="1" sort="list" export="false" excludedParams="metodo" requestURI="./Companhia.do" pagesize="30" class="tabelaDisplay">
                            <display:column style="width:22px;">
                                <input type="checkbox" name="ids_exclusao" value="${companhia.id}"/>
                            </display:column>
                            <display:column property="nome" title="<label title='Nome'>Nome</label>" />
                            <display:column property="descricao" title="<label title='Descrição'>Descrição</label>" />
                            <display:column title="<label title='Ação'>Ação</label>" style="width:40px; text-align:center;" href="./Companhia.do?metodo=selecionar" paramId="id" paramProperty="id" >
                                <img align="middle" src="img/edit.png" width="22" height="22" border="0" title="Editar companhia"/>
                            </display:column>
                        </display:table>
                    </html:form>
                </div>
            <jsp:include flush="false" page="/inc/footer.jsp" />
        </div>
        <msg:alert escopo="session"/>
    </body>
</html>
