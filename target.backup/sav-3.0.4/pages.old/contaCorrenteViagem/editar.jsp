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

        <html:javascript formName="contaCorrenteViagemForm" method="validar"/>

        <title><fmt:message key="aplicacao.nome" /></title>

        <script>
            function mudarBanco(codigo){
                document.getElementById("bancoCodigo").value = codigo;
            }

            function setBanco(codigo){
                document.getElementById("bancoNome").value = codigo;
            }

        </script>

    </head>
    <body>
        <div id="wrap">
            <jsp:include flush="false" page="/inc/header.jsp" />
            <jsp:include flush="false" page="/inc/sidebar.jsp" />
            <div id="content">
                <h2>Editar Conta Corrente</h2>
                <html:form action="/ContaCorrenteViagem.do?metodo=alterar" onsubmit="return validar(this);" method="post"  >
                <html:hidden property="contaCorrenteViagem.id"/>
                <html:hidden property="contaCorrenteViagem.codigoDominioFuncionarioViajante"/>

                    <table border="0" width="775px">

                        <tr bgcolor="#EEEEEE">
                            <td colspan="4"><strong>Viajante: </strong> ${viajante.nome}</td>
                            
                        </tr>

                        <tr><td height="10"></td></tr>

                        <tr>
                            <td width="100px">
                                Banco:
                            </td>
                            <td colspan="3">
                                <html:text property="banco.codigo" styleId="bancoCodigo" size="5" tabindex="1" onchange="setBanco(this.value);"/>
                                &nbsp;
                                <html:select property="banco.codigo" styleId="bancoNome" style="width: 300px;" tabindex="2" onchange="mudarBanco(this.value);">
                                    <html:option value="">-- Selecione --</html:option>
                                    <html:optionsCollection name="listaBancos" value="codigo" label="nome"/>
                                </html:select>
                            </td>
                        </tr>

                        <tr>
                            <td width="100px">
                               Agência:
                            </td>
                            <td colspan="3">
                                <html:text property="contaCorrenteViagem.agencia"  size="10" tabindex="3"/>
                            </td>
                        </tr>
                        <tr>
                            <td width="100px">
                                Conta Corrente:
                            </td>
                            <td colspan="3">
                                <html:text property="contaCorrenteViagem.conta" size="10" tabindex="4"/>
                            </td>
                        </tr>

                        <tr>
                            <td colspan="4">
                                <acesso:input tipo="salvar" action="ContaCorrenteViagem.do" metodo="salvar" chave="admSalvarCadCc" styleClass="botao" />
                                &nbsp;
                                <acesso:input tipo="select" action="ContaCorrenteViagem.do" metodo="cancelar" chave="*" styleClass="botao" onclick="document.location.href='ContaCorrenteViagem.do'" />
                            </td>
                        </tr>
                </table>
                </html:form>
                <jsp:include flush="false" page="/inc/footer.jsp" />
            </div>

        </div>
    </body>
</html:html>