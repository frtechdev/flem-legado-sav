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
        <link href="<%=request.getContextPath()%>/css/calendario.css" rel="stylesheet" type="text/css" />
        <script language="JavaScript" src="<%=request.getContextPath()%>/js/calendar.js"  type="text/javascript" ></script>
        <script language="JavaScript" src="<%=request.getContextPath()%>/js/calendar-pt.js"  type="text/javascript" ></script>
        <script language="JavaScript" src="<%=request.getContextPath()%>/js/calendar-setup.js"  type="text/javascript" ></script>

        <title><fmt:message key="aplicacao.nome" /></title>
        <html:javascript formName="relatorioForm" method="validar" page="4"/>
        
        <script>
            function escolherModelo(op){
                var form = relatorioForm;
                if(op == "PDF"){
                    form.action = "./Relatorio.do?metodo=percentualFuncionarioDiariaDetalhada";
                }else{
                    form.action = "./Relatorio.do?metodo=percentualFuncionarioDiariaDetalhadaExcel";
                }
                form.submit();
            }
            
            function validarData(valor){
                var reDate1 = /^\d{2}\/\d{4}$/;
                return reDate1.test(valor);
            }
            
            
            function liberarBotao(){
                var form = relatorioForm;
                
                //alert("liberarBotao");
                
                setStatusBotao(true);
                
                
                if(form.codigoCentroCustoInicial.value == "" || form.codigoCentroCustoFinal.value == "" || form.dataInicial.value == "" || form.dataFinal.value == ""){
                    setStatusBotao(false);
                }else{
                    if(!validarData(form.dataInicial.value)){
                        alert("Data inicial inválida!");
                        setStatusBotao(false);
                    }
                    if(!validarData(form.dataFinal.value)){
                        alert("Data final inválida!");
                        setStatusBotao(false);
                    }
                }
            }
            
            function setStatusBotao(status){// true = ativo | false = inativo
                var form = relatorioForm;
                
                //alert("setStatusBotao = " + status);
                
                if(status){
                    form.btPDF.className = "botao";
                    form.btExcel.className = "botao";
                    form.btPDF.disabled = false;
                    form.btExcel.disabled = false;
                }else{
                    form.btPDF.className = "botaoDesabilitado";
                    form.btExcel.className = "botaoDesabilitado";
                    form.btPDF.disabled = true;
                    form.btExcel.disabled = true;
                }
            }
        </script>
        
    </head>
    <body>
        <div id="wrap">
            <jsp:include flush="false" page="/inc/header.jsp" />
            <jsp:include flush="false" page="/inc/sidebar.jsp" />
            <div id="content">
                <h2>Relatório detalhado de percentual de diarias no mês e ano</h2>
                <html:form action="/Relatorio.do?metodo=percentualFuncionarioDiariaDetalhada" onsubmit="return validar(this);" method="post" >
                    <html:hidden property="modelo" value="PDF" />
                    <table>

                        <tr>
                            <td>Centro de custo inicial:</td>
                            <td>
                                <html:select property="codigoCentroCustoInicial" onchange="liberarBotao()" style="width: 400px;">
                                    <html:option value="">-- Selecione --</html:option>
                                    <html:optionsCollection name="listaCC" value="id" label="descricaoCompleta" />
                                </html:select>
                            </td>
                        </tr>
                        <tr>
                            <td>Centro de custo final:</td>
                            <td>
                                <html:select property="codigoCentroCustoFinal" onchange="liberarBotao()" style="width: 400px;">
                                    <html:option value="">-- Selecione --</html:option>
                                    <html:optionsCollection name="listaCC" value="id" label="descricaoCompleta" />
                                </html:select>
                            </td>
                        </tr>
                        <tr>
                            <td>Situação do Funcionário:</td>
                            <td>
                                <html:select property="situacaoFuncionario" style="width: 400px;">
                                    <html:option value="2">Ativo/Desligado</html:option>
                                    <html:option value="1">Ativo</html:option>
                                    <html:option value="0">Desligado</html:option>
                                </html:select>
                            </td>
                        </tr>
                        <tr>
                            <td>Período:</td>
                            <td>
                                <html:text property="dataInicial" styleId="dataInicial" styleClass="data" size="10" maxlength="7" onchange="liberarBotao()" />
                                &nbsp;
                                a:
                                &nbsp;
                                <html:text property="dataFinal" styleId="dataFinal" styleClass="data" size="10" maxlength="7" onchange="liberarBotao()" /> ex: 01/2013
                            </td>
                        </tr>

                        <tr>
                            <td colspan="2">
                                <acesso:input nameInput="btPDF" tipo="select" action="Relatorio.do" metodo="salvar PDF" chave="*" styleClass="botao" onclick="escolherModelo('PDF');" />
                                &nbsp;
                                <acesso:input nameInput="btExcel" tipo="select" action="Relatorio.do" metodo="salvar EXCEL" chave="*" styleClass="botao" onclick="escolherModelo('EXCEL');" />
                                &nbsp;
                                <acesso:input tipo="select" action="Relatorio.do" metodo="voltar" chave="*" styleClass="botao" onclick="document.location.href='./Relatorio.do'" />
                                <script>setStatusBotao(false);</script>
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
