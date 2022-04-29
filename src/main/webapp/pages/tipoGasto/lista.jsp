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

                <h2>Lista de Tipos de gasto</h2>
                <html:form action="/TipoGasto.do" method="post">
                    <table>
                        <tr>
                            <td>Descrição</td>
                            <td><html:text property="tipoGasto.descricao"/></td>
                        </tr>
                        <tr>
                            <td>Status</td>
                            <td>
                                <html:select property="tipoGasto.ativo">
                                    <html:option value="true">Ativo</html:option>
                                    <html:option value="false">Inativo</html:option>
                                </html:select>
                            </td>
                        </tr>
                    </table>
                    <div style="width:100%; text-align:right;">
                        <html:submit property="metodo" value="filtrar" styleClass="botao" />
                        &nbsp;
                        <acesso:input tipo="select" action="TipoGasto.do" metodo="novo" chave="admNovoTipoGasto" styleClass="botao" />
                        &nbsp;
                        <acesso:input tipo="delete" action="TipoGasto.do" metodo="excluir" chave="admExcluirTipoGasto" styleClass="botao" />
                    </div>
                    <display:table id="tipoGasto" name="lista" defaultsort="3" defaultorder="descending" sort="list" export="false" requestURI="./TipoGasto.do" pagesize="15" class="tabelaDisplay">
                        <display:column style="width:22px;">
                            <input type="checkbox" name="ids_exclusao" value="${tipoGasto.id}"/>
                        </display:column>
                        <display:column property="descricao" title="<label title='Descrição'>Descrição</label>" />
                        <display:column title="<label title='Status'>Status</label>" >
                            <c:if test="${tipoGasto.ativo}">
                                Ativo
                            </c:if>
                            <c:if test="${!tipoGasto.ativo}">
                                Inativo
                            </c:if>
                        </display:column>
                        <display:column title="<label title='Ação'>Ação</label>" style="width:40px; text-align:center;" href="./TipoGasto.do?metodo=selecionar" paramId="id" paramProperty="id" >
                            <img align="middle" src="img/edit.png" width="22" height="22" border="0" title="Editar Tipo de gasto"/>
                        </display:column>
                    </display:table>
                </html:form>
            </div>
            <jsp:include flush="false" page="/inc/footer.jsp" />
        </div>
        <msg:alert escopo="session"/>
    </body>
</html>
