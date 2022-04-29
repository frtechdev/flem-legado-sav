<%@page contentType="text/html" errorPage="/erro.jsp"%> 
<%@page pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://flem.org.br/acesso-tag" prefix="acesso"%>
<html:html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
        <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
        <link rel="stylesheet" type="text/css" href="css/800px.css" />
        <link rel="stylesheet" type="text/css" href="css/displaytag.css" />
        <link href="css/displaytag.css" rel="stylesheet" type="text/css" />

        <script src="<%=request.getContextPath()%>/dwr/interface/Funcoes.js"></script>
        <script src="<%=request.getContextPath()%>/dwr/engine.js"></script>
        <script src="<%=request.getContextPath()%>/dwr/util.js"></script>
        <script src="<%=request.getContextPath()%>/js/masterdetail.js"></script>
        <script src="<%=request.getContextPath()%>/js/functions.js"></script>
        <script src="<%=request.getContextPath()%>/js/jquery-1.4.2.min.js"></script>
        <script src="<%=request.getContextPath()%>/js/jquery.maskedinput.js"></script>
        <script src="<%=request.getContextPath()%>/js/jquery.maskMoney.js"></script>

        <script>

            jQuery.noConflict();
            jQuery(document).ready(function(){
                jQuery("input[class*=data]").mask("99/99/9999", {placeholder: " "});
                jQuery("input[class*=hora]").mask("99:99", {placeholder: " "});
                jQuery("input[class*=real]").maskMoney({symbol: "", decimal: ",", thousands: "."});

                jQuery("input[class*=data]").change(function() { if (this.value.indexOf(" ") == -1) ValidarDATA(this, '/'); else this.value = ""; });
                jQuery("input[class*=hora]").change(function() { if (this.value.indexOf(" ") == -1) ValidarHORA(this, ':'); else this.value = ""; });
            });
            
            function obterCargo() {
                if(DWRUtil.getValue("cdId") > 0){
                   //Funcoes.obterCargo(DWRUtil.getValue("cdId"), setCamposCargo);
                   $("cdNome").value = $("cdId").options[$("cdId").selectedIndex].innerHTML;
                   $("cdDescricao").value = $("cdId").options[$("cdId").selectedIndex].innerHTML;
                   $("cdCodigio").value = $("cdId").options[$("cdId").selectedIndex].value;
                }else{
                    $("cdNome").value = "";
                    $("cdDescricao").value = "";
                    $("cdCodigio").value = "";
                }
                
            }

            function validarValores(){
                if($("valorBahia").value == "0,00"){
                    alert("Informe Diária na Bahia");
                    $("valorBahia").focus();
                    return false;
                }
                if($("valorBrasil").value == "0,00"){
                    alert("Informe Diária em outros estados");
                    $("valorBrasil").focus();
                    return false;
                }
                if($("valorExterior").value == "0,00"){
                    alert("Informe Diária no Exterior");
                    $("valorExterior").focus();
                    return false;
                }
                return true;
            }

        </script>

        <html:javascript formName="cargoDiariaForm" method="validar"/>

        <title><fmt:message key="aplicacao.nome" /></title>

        <script language="JavaScript" src="js/util.js"></script>

    </head>
    <body>
        <div id="wrap">
            <jsp:include flush="false" page="/inc/header.jsp" />
            <jsp:include flush="false" page="/inc/sidebar.jsp" />
            <div id="content">
                <h2>Nova Diária</h2>
                <html:form action="/CargoDiaria.do?metodo=adicionar" onsubmit="return (validar(this) && validarValores());" method="post"  >

                    <table border="0" width="100%">
                        <tr bgcolor="#EEEEEE">
                            <td colspan="2"><strong>Cargos</strong></td>
                        </tr>

                        <tr><td height="10"></td></tr>

                        <tr>
                            <td colspan="2">
                                <html:select property="id" styleId="cdId" style="width: 300px;" onchange="obterCargo()" tabindex="1">
                                    <html:option value="">-- Selecione --</html:option>
                                    <html:optionsCollection name="listaCargos" value="id" label="nome"/>
                                </html:select>
                            </td>
                        </tr>

                        <tr><td height="10"></td></tr>

                        <tr bgcolor="#EEEEEE">
                            <td colspan="2"><strong>Informar Diária para o Cargo Selecionado</strong></td>
                        </tr>

                        <tr><td height="10"></td></tr>

                        <tr>
                            <td>
                                Nome:<br />
                                <html:text property="cargoDiaria.cargoNome" styleId="cdNome" size="30" readonly="true" style="background-color:#DDDDDD;" tabindex="2"/>
                                <html:hidden property="cargoDiaria.cargoDominio" styleId="cdCodigio" value=""/>
                            </td>
                            <td>
                                Diária na Bahia (R$):<br />
                                <html:text property="cargoDiaria.valorBahia" styleId="valorBahia" size="30" styleClass="real" style="text-align:right;" tabindex="4"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                Descrição:<br />
                                <html:text property="cargoDiaria.descricao" styleId="cdDescricao" size="30" tabindex="3"/>
                            </td>
                            <td>
                                Diária em outros estados (R$):<br />
                                <html:text property="cargoDiaria.valorBrasil" styleId="valorBrasil" size="30" styleClass="real" style="text-align:right;" tabindex="5"/>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td>
                                Diária no Exterior (U$)<br />
                                <html:text property="cargoDiaria.valorExterior" styleId="valorExterior" size="30" styleClass="real" style="text-align:right;" tabindex="6" />
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <acesso:input tipo="salvar" action="CargoDiaria.do" metodo="salvar" chave="admSalvarGerDiaria" styleClass="botao" />
                                &nbsp;
                                <acesso:input tipo="select" action="CargoDiaria.do" metodo="cancelar" chave="*" styleClass="botao" onclick="document.location.href='CargoDiaria.do'" />
                            </td>
                        </tr> 
                </table>
                </html:form>
                <jsp:include flush="false" page="/inc/footer.jsp" />
            </div>

        </div>
    </body>
</html:html>