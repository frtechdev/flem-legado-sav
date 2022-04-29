<%@page contentType="text/html" errorPage="/erro.jsp"%> 
<%@page pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://flem.org.br/mensagem-tag" prefix="msg"%>
<%@taglib uri="http://flem.org.br/acesso-tag" prefix="acesso"%>
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

                <h2>Lista de Diárias</h2>
                <html:form action="/CargoDiaria.do" method="post">
                    <div style="width:100%; text-align:right;">
                        <acesso:input tipo="select" action="CargoDiaria.do" metodo="novo" chave="admNovoGerDiaria" styleClass="botao" />
                        &nbsp;
                        <acesso:input tipo="delete" action="CargoDiaria.do" metodo="excluir" chave="admExcluirGerDiaria" styleClass="botao" />
                    </div>
                        
                        <display:table id="cargoDiaria" name="lista" defaultsort="2" sort="list" export="false" excludedParams="metodo" requestURI="./CargoDiaria.do" pagesize="30" class="tabelaDisplay">

                            <display:column title="<input type='checkbox' name='ids_exclusao_check' onclick=javascript:selecionar_tudo('ids_exclusao'); />" style="width:22px;" >
                                <input type="checkbox" name="ids_exclusao" value="${cargoDiaria.id}"/>
                            </display:column>
                            <display:column property="cargoNome" title="<label title='Nome'>Nome</label>" />
                            <display:column property="descricao" title="<label title='Descrição'>Descrição</label>" />
                            <display:column property="valorBahia" style="text-align:right;" title="<label title='Estado da Bahia (R$)'>Estado da<br>Bahia (R$)</label>" format="{0,number,0.00}" />
                            <display:column property="valorBrasil" style="text-align:right;" title="<label title='Outros Estados (R$)'>Outros<br>Estados (R$)</label>" format="{0,number,0.00}" />
                            <display:column property="valorExterior" style="text-align:right;" title="<label title='Exterior (U$)'>Exterior<br>(U$)</label>" format="{0,number,0.00}"/>
                            <display:column title="<label title='Ação'>Ação</label>" style="width:40px; text-align:center;" href="./CargoDiaria.do?metodo=selecionar" paramId="id" paramProperty="id" >
                                <img align="middle" src="img/edit.png" width="22" height="22" border="0" title="Editar diária"/>
                            </display:column>
                        </display:table>
                </html:form>
            </div>
            <jsp:include flush="false" page="/inc/footer.jsp" />
        </div>
        <msg:alert escopo="session"/>
    </body>
</html>
