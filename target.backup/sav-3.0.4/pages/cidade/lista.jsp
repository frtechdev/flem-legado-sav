<%@page contentType="text/html" errorPage="/erro.jsp"%> 
<%@page pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@taglib uri="http://flem.org.br/acesso-tag" prefix="acesso" %>
<%@taglib uri="http://flem.org.br/mensagem-tag" prefix="msg"%>
<%@taglib uri="http://flem.org.br/flem-struts-tag" prefix="flemst" %>
<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
        <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
        <link rel="stylesheet" type="text/css" href="css/800px.css" />
        <link href="css/displaytag.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/dwr/interface/Funcoes.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/dwr/engine.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/dwr/util.js"></script>
        <title><fmt:message key="aplicacao.nome" /></title>

        <script src="<%=request.getContextPath()%>/js/functions.js"></script>

    </head>
    <body>
        <div id="wrap">
            <jsp:include flush="false" page="/inc/header.jsp" />
            <jsp:include flush="false" page="/inc/sidebar.jsp" />
            <div id="content">

                <h2>Lista de Cidades</h2>
                <html:form action="/Cidade.do" method="post">
                    <table>
                        <tr>
                            <td>Nome:</td>
                            <td><html:text property="cidade.nome"/></td>
                        </tr>
                        <tr>
                            <td>País:</td>
                            <td>
                                <flemst:fatherSelect property="cidade.pais.id" styleId="paisId" dependent="estadoId" method="obterEstadosPorPais" remoteProxy="Funcoes" dtoId="id" dtoValue="nome" emptySelect="true" styleClass="requerido" >
                                    <html:option value="">Selecione...</html:option>
                                    <html:optionsCollection name="listaPaises" value="id" label="nome" />
                                </flemst:fatherSelect>
                            </td>
                        </tr>
                        <tr>
                            <td>Estado:</td>
                            <td>
                                <html:select property="cidade.estado.id" styleId="estadoId">
                                    <html:option value="">Selecione...</html:option>
                                </html:select>
                            </td>
                        </tr>
                    </table>
                    <div style="width:100%; text-align:right;">
                        <html:submit property="metodo" value="filtrar" styleClass="botao" />
                        &nbsp;
                        <acesso:input tipo="select" action="Cidade.do" metodo="novo" chave="admNovoCidade" styleClass="botao" />
                        &nbsp;
                        <acesso:input tipo="delete" action="Cidade.do" metodo="excluir" chave="admExcluirCidade" styleClass="botao" />
                    </div>
                    <display:table id="cidade" name="lista" defaultsort="2" sort="list" export="false" requestURI="./Cidade.do" pagesize="15" class="tabelaDisplay">
                        <display:column style="width:22px;">
                            <input type="checkbox" name="ids_exclusao" value="${cidade.id}"/>
                        </display:column>
                        <display:column property="pais.nome" title="Pais"/>
                        <display:column property="estado.nome" title="Estado"/>
                        <display:column property="nome" title="cidade" />
                        <display:column  title="" style="width:40px; text-align:center;" href="./Cidade.do?metodo=selecionar" paramId="id" paramProperty="id" >
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
