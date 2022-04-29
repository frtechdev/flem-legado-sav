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
                
                <h2>Lista de Percentual Diárias</h2>
                <html:form action="/PercentualDiaria.do" method="post">
                    <div style="width:100%; text-align:right;">
                        <acesso:input tipo="select" action="PercentualDiaria.do" metodo="novo" chave="admNovoPercentualDiaria" styleClass="botao" />
                        &nbsp;
                        <acesso:input tipo="delete" action="PercentualDiaria.do" metodo="excluir" chave="admExcluirPercentualDiaria" styleClass="botao" />
                    </div>

                    <display:table id="percentualDiaria" name="lista" defaultsort="3" sort="list" export="false" excludedParams="metodo" requestURI="./PercentualDiaria.do" pagesize="30" class="tabelaDisplay">
                        <display:column style="width:22px;">
                            <c:if test="${percentualDiaria.departamentoDominio != 0}">
                                <input type="checkbox" name="ids_exclusao" value="${percentualDiaria.id}"/>
                            </c:if>
                        </display:column>
                        <display:column title="<label title='Porcentagem Quebra de Diaria'>Porcentagem Quebra de Diaria</label>">
                            Primeira Faixa ${percentualDiaria.minQuebraDiariasUm} - ${percentualDiaria.maxQuebraDiariasUm} : ${percentualDiaria.quebraDiariasUm} </br>
                            Segunda Faixa ${percentualDiaria.minQuebraDiariasDois} - ${percentualDiaria.maxQuebraDiariasDois} : ${percentualDiaria.quebraDiariasDois}
                        </display:column>
                        <display:column title="<label title='Lotação'>Lotação</label>">
                            ${percentualDiaria.departamentoDominio eq 0 ? 'PADRÃO' : mapdepartamento[percentualDiaria.departamentoDominio.toString()].nome}
                        </display:column>
                        <display:column title="<label title='Ação'>Ação</label>" style="width:40px; text-align:center;" href="./PercentualDiaria.do?metodo=selecionar" paramId="id" paramProperty="id" >
                            <img align="middle" src="img/edit.png" width="22" height="22" border="0" title="Editar Percentual Diaria"/>
                        </display:column>
                    </display:table>
                </html:form>
            </div>
            <jsp:include flush="false" page="/inc/footer.jsp" />
        </div>
        <msg:alert escopo="session"/>
    </body>
</html>
