<%@page contentType="text/html" errorPage="/erro.jsp"%>
<%@page pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://flem.org.br/acesso-tag" prefix="acesso"%>
<%@taglib uri="http://flem.org.br/mensagem-tag" prefix="msg"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display"%>

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
                if(op == "Filtrar"){
                    form.action = "./Relatorio.do?metodo=filtrar";
                    
                }else{
                    form.action = "./Relatorio.do?metodo=relatorio";
                   
                }
                form.submit();
 
            }
            
            function liberarBotao(){
                var form = relatorioForm;
                
                //alert("liberarBotao");
                
                setStatusBotao(true);
                
                
                if(form.mesAno.value == "" || form.ano.value == "" || form.mesAno.value == "" || form.ano.value == ""){
                    setStatusBotao(false);
                }
                }
           
            
            function setStatusBotao(status){// true = ativo | false = inativo
                var form = relatorioForm;
                
                //alert("setStatusBotao = " + status);
                
                if(status){
                    form.Filtrar.className = "botao";
                    form.Filtrar.disabled = false;


                }else{
                    form.Filtrar.className = "botaoDesabilitado";
                    form.Filtrar.disabled = true;


                }
            }
        </script>
        
    </head>
    
    
    <body>
        <div id="wrap">
            <jsp:include flush="false" page="/inc/header.jsp" />
            <jsp:include flush="false" page="/inc/sidebar.jsp" />
            <div id="content">
                <h2>Relatório Diária x Salários dos Colaboradores</h2>
                
                    <html:form action="/Relatorio.do" onsubmit="return validar(this);" method="post" >
                     <table>
                        <tr>
                           <td>Mês:</td>
                            <td>
                                <html:text property="mesAno"  styleClass="data" size="2" maxlength="2" onchange="liberarBotao()" />
                            </td>
                            <tr>
                        <td>Ano:</td>
                            <td>
                                <html:text property="ano"  styleClass="data" size="4" maxlength="4" onchange="liberarBotao()"/>
                            </td>
                            </tr>
                        </tr>    
                        </table>
                            
                        <table>   
                           <tr>
                            <td >
                                <acesso:input nameInput="Filtrar" tipo="select" action="Relatorio.do" metodo="filtro" chave="*" styleClass="botao" onclick="escolherModelo('Filtrar');" />
                                &nbsp;
                                <acesso:input nameInput="Relatorio"  tipo="select" action="Relatorio.do" metodo="relatorio" chave="*" styleClass="botao" onclick="escolherModelo('relatorio');"  />
                                &nbsp;
                                <acesso:input tipo="select" action="Relatorio.do" metodo="voltar" chave="*" styleClass="botao" onclick="document.location.href='./Relatorio.do'" />
                                <script> setStatusBotao(false)</script>
                            </td>
                        </tr>
                            </table>                        
                        <display:table id="rel" name="lista" defaultsort="2" sort="list"  export="false" excludedParams="metodo" requestURI="./Relatorio.do?metodo=filtrar" pagesize="30"  class="tabelaDisplay">
                            <display:column property="matricula" title="Matricula" style="width:5%;text-align:right"/>
                            <display:column property="nome" title="Nome"/>
                            <display:column property="ano" title="Ano" style="width:5%;text-align:right"/>
                            <display:column property="mes" title="Mês" style="width:5%;text-align:right"/>
                            <display:column property="vlTotal" title="Valor Total Diárias"   style="width:5%;text-align:right"  />
                            <display:column property="vlRecebido" title="Valor Recebido acima de 50%" style="width:5%;text-align:right"/>
                            <display:column property="bSalario" title="Base 50% Salário" style="width:5%;text-align:right" />
                            <display:column property="percentualDiariaSalario" title="Percentual diária/salário" style="width:5%;text-align:right" />
                        </display:table>
                        <div style="width:100%; text-align:right;">
                                
                         </div>
                    </html:form>
            </div>
         <jsp:include flush="false" page="/inc/footer.jsp" />
        </div>
        <msg:alert escopo="session"/>
    </body>
</html>
