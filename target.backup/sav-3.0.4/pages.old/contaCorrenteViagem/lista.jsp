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

                <h2>Lista de Conta Corrente</h2>
                <html:form action="/ContaCorrenteViagem.do" method="post">
                    <div style="width:100%; text-align:right;">

                        <html:submit property="metodo" value="filtrar" styleClass="botao" />
                        &nbsp;
                        <acesso:input tipo="select" action="ContaCorrenteViagem.do" metodo="novo" chave="admNovoCadCc" styleClass="botao" />
                        &nbsp;
                        <acesso:input tipo="delete" action="ContaCorrenteViagem.do" metodo="excluir" chave="admExcluirCadCc" styleClass="botao" />
                    </div>
                    
                    <table>
                        <tr>
                            <td>Nome ou Matrícula:</td>
                            <td><html:text property="busca" size="50" maxlength="50"/></td>
                             
                        </tr>
                    </table>

                    <display:table id="conta" name="lista" defaultsort="2" sort="list" export="false" excludedParams="metodo" requestURI="./ContaCorrenteViagem.do" pagesize="30" class="tabelaDisplay">
                        <display:column style="width:22px;">
                            <input type="checkbox" name="ids_exclusao" value="${conta.id}"/>
                        </display:column>
                        <display:column property="codigoDominioFuncionarioViajante" title="<label title='Código Dominio'>Código Dominio</label>"/>
                        <display:column property="nomeFuncionario" title="<label title='Funcionário'>Funcionário</label>"/>
                        <display:column title="<label title='Banco'>Banco</label>" style="width:250px;">
                            ${conta.banco.codigo} - ${conta.banco.nome}
                        </display:column>
                        <display:column property="agencia" title="<label title='Agência'>Agência</label>" style="width:60px;"/>
                        <display:column property="conta" title="<label title='Conta'>Conta</label>" style="width:60px;"/>
                        <display:column title="<label title='Ação'>Ação</label>" style="width:40px; text-align:center;" href="./ContaCorrenteViagem.do?metodo=selecionar" paramId="id" paramProperty="id" >
                            <img align="middle" src="img/edit.png" width="22" height="22" border="0" title="Editar conta corrente"/>
                        </display:column>
                    </display:table>
                </html:form>
            </div>
            <jsp:include flush="false" page="/inc/footer.jsp" />
        </div>
        <msg:alert escopo="session"/>
    </body>
</html>
