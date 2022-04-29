<%@page contentType="text/html" errorPage="/erro.jsp"%>
<%@page pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://flem.org.br/acesso-tag" prefix="acesso"%>
<%@taglib uri="http://flem.org.br/mensagem-tag" prefix="msg"%>
<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
        <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
        <link rel="stylesheet" type="text/css" href="css/800px.css" />
        <link href="css/displaytag.css" rel="stylesheet" type="text/css" />

        <title><fmt:message key="aplicacao.nome" /></title>
        <html:javascript formName="relatorioForm" method="validar" page="2"/>
    </head>
    <body>
        <div id="wrap">
            <jsp:include flush="false" page="/inc/header.jsp" />
            <jsp:include flush="false" page="/inc/sidebar.jsp" />
            <div id="content">
                <h2>Relatório de prestação de contas em aberto</h2>
                <html:form action="/Relatorio.do?metodo=totalPrestaoContasEmAberto" onsubmit="return validar(this);" method="post" >
                    <table>

                        <tr>
                            <td>Centro de custo inicial:</td>
                            <td>
                                <html:select property="codigoCentroCustoInicial">
                                    <html:option value="">-- Selecione --</html:option>
                                    <html:optionsCollection name="listaCC" value="id" label="descricaoCompleta" />
                                </html:select>
                            </td>
                        </tr>
                         <tr>
                            <td>Centro de custo final:</td>
                            <td>
                                <html:select property="codigoCentroCustoFinal">
                                    <html:option value="">-- Selecione --</html:option>
                                    <html:optionsCollection name="listaCC" value="id" label="descricaoCompleta" />
                                </html:select>
                            </td>
                        </tr>

                        <tr>
                            <td colspan="2">
                                <acesso:input tipo="salvar" action="Relatorio.do" metodo="gerar" chave="*" styleClass="botao" />
                                &nbsp;
                                <acesso:input tipo="select" action="Relatorio.do" metodo="voltar" chave="*" styleClass="botao" onclick="document.location.href='./Relatorio.do'" />
                            </td>
                        </tr>

                    </table>
                </html:form>
                <jsp:include flush="false" page="/inc/footer.jsp" />
            </div>

        </div>
        <msg:alert escopo="session"/>
    </body>
</html>
