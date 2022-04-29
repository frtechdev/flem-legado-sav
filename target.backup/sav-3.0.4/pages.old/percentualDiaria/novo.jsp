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
        
        <html:javascript formName="percentualDiariaForm" method="validar"/>
        
        <title><fmt:message key="aplicacao.nome" /></title>

    </head>
    <body>
        <div id="wrap">
            <jsp:include flush="false" page="/inc/header.jsp" />
            <jsp:include flush="false" page="/inc/sidebar.jsp" />
            <div id="content">
                <h2>Novo Percentual</h2>
                <html:form action="/PercentualDiaria.do?metodo=adicionar" onsubmit="return validar(this);" method="post"  >
                    <table>
                        <tr>
                            <td colspan="2">Primeira faixa de horas:</td>
                            <td>Percentual primeira faixa:</td>
                        </tr>
                        <tr>
                            <td><html:text size="10" property="minQuebraDiariasUm"/>h </td>
                            <td><html:text size="10" property="maxQuebraDiariasUm"/> h</td>
                            <td><html:text size="10" property="quebraDiariasUm"/> </td>
                        </tr>
                        <tr>
                            <td colspan="2">Segunda faixa de horas:</td>
                            <td>Percentual da segunda faixa:</td>
                        </tr>
                        <tr>
                            <td><html:text size="10" property="minQuebraDiariasDois"/>h </td>
                            <td><html:text size="10" property="maxQuebraDiariasDois"/>h </td>
                            <td><html:text size="10" property="quebraDiariasDois"/> </td>
                        </tr>
                        <tr>
                            <td colspan="3">&nbsp;</td>
                        </tr>
                        <tr>
                            <td>Lotação:</td>
                            <td>
                                <html:select property="percentualDiaria.departamentoDominio" styleId="departamento" value="">
                                    <html:option value="">-- Selecione --</html:option>
                                    <html:optionsCollection name="departamentos" value="codigoDominio" label="nome"/>
                                </html:select>
                            </td>
                        </tr>
                        
                        <tr>
                            <td colspan="2">
                                <acesso:input tipo="salvar" action="PercentualDiaria.do" metodo="salvar" chave="admSalvarPercentualDiaria" styleClass="botao" />
                                &nbsp;
                                <acesso:input tipo="select" action="PercentualDiaria.do" metodo="cancelar" chave="*" styleClass="botao" onclick="document.location.href='PercentualDiaria.do'" />
                            </td>
                        </tr>                        
                    
                </table> 
                </html:form>
                <jsp:include flush="false" page="/inc/footer.jsp" />            
            </div> 
            
        </div>
    </body>
</html>
