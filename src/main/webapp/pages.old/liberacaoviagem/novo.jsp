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

        <title><fmt:message key="aplicacao.nome" /></title>

        <script>
            function selecionar(id){
                if(id == '0'){
                    document.getElementById("funcViajId").disabled = false;
                    document.getElementById("consultViajId").disabled = true;
                }else{
                    document.getElementById("funcViajId").disabled = true;
                    document.getElementById("consultViajId").disabled = false;
                }
            }

            function validar(){
                if(document.getElementById("funcViajId").value == "" && document.getElementById("consultViajId").value == ""){
                    alert("Informe o Funcionário ou Consultor!");
                    return false;
                }
                if(document.getElementById("descricao").value == ""){
                    alert("Informe a Descrição!");
                    return false;
                }
                return true;
            }

        </script>
    </head>
    <body>
        <div id="wrap">
            <jsp:include flush="false" page="/inc/header.jsp" />
            <jsp:include flush="false" page="/inc/sidebar.jsp" />
            <div id="content">
                <h2>Nova Liberação de Viagem</h2>
                <html:form action="/LiberacaoViagem.do?metodo=adicionar" onsubmit="return validar();" method="post"  >
                    <html:hidden property="tipoLiberacao" value="${tipoLiberacao}"/>
                    <table border="0" width="775px">

                        <tr><td height="10"></td></tr>

                        <tr>
                            <td>
                                <input type="radio" name="tipoPessoa" value="0" onclick="selecionar('0')" checked > Funcionário
                            </td>
                            <td colspan="3">
                                <html:select property="liberacaoViagem.codigoDominioUsuarioViajante" styleId="funcViajId" style="width: 300px;">
                                    <html:option value="">-- Selecione --</html:option>
                                    <html:optionsCollection name="listaFuncionario" value="codigoDominio" label="nome"/>
                                </html:select>
                            </td>
                        </tr>
                        
                        <tr>
                            <td>
                                <input type="radio" name="tipoPessoa" value="1" onclick="selecionar('1')"> Consultor
                            </td>
                            <td colspan="3">
                                <html:select property="liberacaoViagem.codigoConsultorViajante" styleId="consultViajId" style="width: 300px;" disabled="true">
                                    <html:option value="">-- Selecione --</html:option>
                                    <html:optionsCollection name="listaConsultor" value="codigo" label="nome"/>
                                </html:select>
                            </td>
                        </tr>

                        <tr><td height="10"></td></tr>

                        <tr>
                            <td colspan="4">
                                Descrição
                                <br />
                                <html:textarea property="liberacaoViagem.descricao" styleId="descricao" cols="80" rows="2" value=""/>
                            </td>
                        </tr>

                        <tr>
                            <td colspan="4">
                                <acesso:input tipo="salvar" action="LiberacaoViagem.do" metodo="salvar" chave="admSalvarLiberViagem" styleClass="botao" />
                                &nbsp;
                                <acesso:input tipo="select" action="LiberacaoViagem.do" metodo="cancelar" chave="*" styleClass="botao" onclick="document.location.href='LiberacaoViagem.do'" />
                            </td>
                        </tr>
                    </table>
                </html:form>
                <jsp:include flush="false" page="/inc/footer.jsp" />
            </div>

        </div>
    </body>
</html:html>