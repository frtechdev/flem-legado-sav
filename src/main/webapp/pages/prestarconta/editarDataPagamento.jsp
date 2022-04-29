<%@page contentType="text/html" errorPage="/erro.jsp"%>
<%@page pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://flem.org.br/acesso-tag" prefix="acesso"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@taglib uri="http://flem.org.br/flem-struts-tag" prefix="flemst" %>
<html:html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
        <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
        <link rel="stylesheet" type="text/css" href="css/800px.css" />
        <link rel="stylesheet" type="text/css" href="css/displaytag.css" />

        <script src="<%=request.getContextPath()%>/js/jquery-1.4.2.min.js"></script>
        <script src="<%=request.getContextPath()%>/js/jquery.maskedinput.js"></script>
        <script src="<%=request.getContextPath()%>/js/jquery.maskMoney.js"></script>

        <html:javascript formName="prestacaoContasForm" method="validar" page="2"/>

        <title><fmt:message key="aplicacao.nome" /></title>

        <script language="JavaScript">
            jQuery.noConflict();
            jQuery(document).ready(function(){
                jQuery("input[class*=data]").mask("99/99/9999", {placeholder: " "});
                jQuery("input[class*=data]").change(function() { if (this.value.indexOf(" ") == -1) ValidarDATA(this, '/'); else this.value = ""; });
            });

        </script>

        <script language="JavaScript" src="js/util.js"></script>
        <link href="<%=request.getContextPath()%>/css/calendario.css" rel="stylesheet" type="text/css" />
        <script language="JavaScript" src="<%=request.getContextPath()%>/js/calendar.js"  type="text/javascript" ></script>
        <script language="JavaScript" src="<%=request.getContextPath()%>/js/calendar-pt.js"  type="text/javascript" ></script>
        <script language="JavaScript" src="<%=request.getContextPath()%>/js/calendar-setup.js"  type="text/javascript" ></script>

    </head>
    <body>
        <div id="wrap">
            <jsp:include flush="false" page="/inc/header.jsp" />
            <jsp:include flush="false" page="/inc/sidebar.jsp" />
            <div id="content">
                <h2>Editar Data de Pagamento da Prestação de Contas</h2>
                <html:form action="/PrestacaoContas.do?metodo=alterarDataPagamento" onsubmit="return validar(this);" method="post"  >
                    <html:hidden property="viagem.id" styleId="id_viagem"/>

                    <table border="0" width="100%">
                        <tr bgcolor="#EEEEEE">
                            <td colspan="4"><strong>Data de Pagamento da Prestação de Contas</strong></td>
                        </tr>

                        <tr><td colspan="4" height="10"></td></tr>

                        <tr>
                            <td width="250px">
                               <html:text styleId="dataPagamento" property="dataPagamento" styleClass="data" size="12" maxlength="10" style="float: left;"/><div style="float: left;"><img border="0" title="Selecione uma data" src="<%=request.getContextPath()%>/img/box_calendario.gif" onclick="return showCalendar('dataPagamento', '%d/%m/%Y');"/></div>
                            </td>
                        </tr>

                         <tr>
                            <td colspan="4">
                                <html:submit value="alterar" styleClass="botao" />
                                &nbsp;
                                <html:button property="cancelar" value="cancelar" styleClass="botao" onclick="document.location.href='PrestacaoContas.do?metodo=listaRecebimento'" />
                            </td>
                        </tr>
                    </table>

                </html:form>
                <jsp:include flush="false" page="/inc/footer.jsp" />
            </div>

        </div>
    </body>
</html:html>