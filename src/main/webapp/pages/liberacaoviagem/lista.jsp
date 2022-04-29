<%@page contentType="text/html" errorPage="/erro.jsp"%> 
<%@page pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://flem.org.br/mensagem-tag" prefix="msg"%>
<%@taglib uri="http://flem.org.br/acesso-tag" prefix="acesso" %>
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

                <h2>Lista de Liberações de Viagens</h2>
                <html:form action="/LiberacaoViagem.do" method="post">
                    <div style="width:100%; text-align:right;">
                        <acesso:input tipo="select" action="LiberacaoViagem.do" metodo="novo" chave="admNovoLiberViagem" styleClass="botao" />
                        &nbsp;
                        <acesso:input tipo="delete" action="LiberacaoViagem.do" metodo="excluir" chave="admExcluirLiberViagem" styleClass="botao" />
                    </div>

                    <display:table id="viagensDTO" name="lista" export="false" excludedParams="metodo" requestURI="./LiberacaoViagem.do" pagesize="30" class="tabelaDisplay">
                        <display:column style="width:22px;">
                            <c:if test="${viagensDTO.liberacaoViagem.valido}">
                                <input type="checkbox" name="ids_exclusao" value="${viagensDTO.liberacaoViagem.id}"/>
                            </c:if>
                        </display:column>
                        <display:column property="nomeSolicitante" title="<label title='Autorizador'>Autorizador</label>" />
                        <display:column property="nomeViajante" title="<label title='Viajante'>Viajante</label>" />
                        <display:column property="liberacaoViagem.descricao" title="<label title='Descrição'>Descrição</label>" />
                        <display:column property="liberacaoViagem.dataLiberacao" title="<label title='Liberação'>Liberação</label>" format="{0,date,dd/MM/yyyy}" style="text-align:center; width:60px" />
                        <display:column title="<label title='Status'>Status</label>" style="text-align:center; width:90px" >
                            <c:if test="${viagensDTO.liberacaoViagem.valido}">
                                Liberado
                            </c:if>
                            <c:if test="${!viagensDTO.liberacaoViagem.valido}">
                                Liberação usada
                            </c:if>
                        </display:column>
                    </display:table>
                </html:form>
            </div>
            <jsp:include flush="false" page="/inc/footer.jsp" />
        </div>
        <msg:alert escopo="session"/>
    </body>
</html>
