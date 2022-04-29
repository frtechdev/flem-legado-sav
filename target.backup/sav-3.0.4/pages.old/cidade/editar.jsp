<%@page contentType="text/html" errorPage="/erro.jsp"%> 
<%@page pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://flem.org.br/flem-struts-tag" prefix="flemst" %>
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
        <script type="text/javascript" src="<%=request.getContextPath()%>/dwr/interface/Funcoes.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/dwr/engine.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/dwr/util.js"></script>
        <html:javascript formName="cidadeForm" method="validar"/>
        <script type="text/javascript" >
            function validarBrasil() {
                var stringVazia = "";
               if ($("paisId").value == '3' && $("estadoId").value == stringVazia) { // 3 = BRASIL
                   alert("Para o Brasil é necessário selecionar o estado");
                   return false;
               }
               return true;
            }
        </script>

        <title><fmt:message key="aplicacao.nome" /></title>

    </head>
    <body>
        <div id="wrap">
            <jsp:include flush="false" page="/inc/header.jsp" />
            <jsp:include flush="false" page="/inc/sidebar.jsp" />
            <div id="content">
                <h2>Nova Cidade</h2>

                <html:form action="/Cidade.do?metodo=alterar" onsubmit="return (validar(this) && validarBrasil());" method="post">
                    <html:hidden property="cidade.id" />
                    <table>
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
                                    <html:optionsCollection name="listaEstados" value="id" label="nome" />
                                </html:select>
                            </td>
                        </tr>
                        <tr>
                            <td>Descrição:</td>
                            <td><html:text size="40" property="cidade.nome" maxlength="100"/> </td>
                        </tr>

                        <tr>
                            <td colspan="2">
                                <acesso:input tipo="salvar" action="Cidade.do" metodo="salvar" chave="admSalvarCidade" styleClass="botao" />
                                &nbsp;
                                <acesso:input tipo="select" action="Cidade.do" metodo="cancelar" chave="*" styleClass="botao" onclick="document.location.href='Cidade.do'" />
                            </td>
                        </tr>

                    </table>
                </html:form>
                <jsp:include flush="false" page="/inc/footer.jsp" />
            </div>

        </div>
    </body>
</html>
