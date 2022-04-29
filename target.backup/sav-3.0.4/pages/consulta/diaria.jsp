<%@page contentType="text/html" errorPage="/erro.jsp"%> 
<%@page pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@taglib uri="http://flem.org.br/mensagem-tag" prefix="msg"%>
<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
        <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
        <link rel="stylesheet" type="text/css" href="css/800px.css" />
        <link href="css/displaytag.css" rel="stylesheet" type="text/css" />

        <script language="JavaScript" src="js/util.js"></script>
        <link href="<%=request.getContextPath()%>/css/calendario.css" rel="stylesheet" type="text/css" />
        <script src="<%=request.getContextPath()%>/dwr/interface/Funcoes.js"></script>
        <script src="<%=request.getContextPath()%>/dwr/engine.js"></script>
        <script src="<%=request.getContextPath()%>/dwr/util.js"></script>

        <script type="text/javascript">
            function calculaSalarioViagens(matricula){
            if(matricula != ""){
               Funcoes.calculaPorcentagemViagensSalario(matricula, {callback:calculaPorcentagem, async:false});
               //Funcoes.obterSalario(matricula, {callback:calculaValorDisponivel, async:false});
            }
            else
              document.getElementById("dadosSalarioViagens").style.display = 'none';
            }

            function calculaPorcentagem(porcentagem){
            document.getElementById("porcentagem").value = porcentagem.toFixed(2);
            document.getElementById("dadosSalarioViagens").style.display = 'block';
            }
/*
            function calculaValorDisponivel(salario){
                document.getElementById("valorDisponivel").value = (salario*((50-(document.getElementById("porcentagem").value))/100)).toFixed(2);
            }
*/


        </script>

        <title><fmt:message key="aplicacao.nome" /></title>
    </head>
    <body>
        <div id="wrap">
            <jsp:include flush="false" page="/inc/header.jsp" />
            <jsp:include flush="false" page="/inc/sidebar.jsp" />

            <div id="content">
                <h2>Consulta de Gasto com Diárias X Salário</h2>
                 
                <html:form action="/ConsultaDiaria.do?metodo=filtrar" method="post">
                    <table>
                        <tr>
                            <td>Funcionario:</td>
                            <td>
                                <html:select property="matricula" onchange="calculaSalarioViagens(this.value)">
                                    <html:option value="">-- Selecione --</html:option>
                                    <html:optionsCollection name="listaFuncionarios" value="matricula" label="nome"/>
                                </html:select>
                                 
                            </td>
                        </tr>

                    </table><br>
                        <div id="dadosSalarioViagens" style="display:none;">
                            
                            <fieldset style="width: 280px;height:60px;">
                                <legend>Percentual de Gasto de Viagens</legend>
                                
                                <strong>Percentagem gasta:&nbsp;</strong> <input type="text" id="porcentagem" size="5" readonly/>% &nbsp;&nbsp;
                                <!--strong>Valor disponível:&nbsp;</strong--><input type="hidden" id="valorDisponivel" size="5" readonly/>
                                
                            </fieldset>
                            <br><br>
                           Obs:. O funcionário pode ter como gasto de viagens no máximo 50% do valor do seu sálario
                        </div>




                </html:form>
                 
            </div>
            <jsp:include flush="false" page="/inc/footer.jsp" />
        </div>
        <msg:alert escopo="session"/>
    </body>
</html>