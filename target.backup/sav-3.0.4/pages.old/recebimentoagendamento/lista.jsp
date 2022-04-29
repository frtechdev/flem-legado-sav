<%@page contentType="text/html" errorPage="/erro.jsp"%> 
<%@page pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://flem.org.br/mensagem-tag" prefix="msg"%>
<%@taglib uri="http://flem.org.br/acesso-tag" prefix="acesso" %>
<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
        <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
        <link rel="stylesheet" type="text/css" href="css/800px.css" />
        <link href="css/displaytag.css" rel="stylesheet" type="text/css" />
        <script src="<%=request.getContextPath()%>/js/functions.js"></script>
        <title><fmt:message key="aplicacao.nome" /></title>
        <script>
            function aprovarAgendamento(){
                var recebimento = document.getElementsByName("ids_selecionados");
                var marcado = false;

                for (i=0; i<recebimento.length; i++){
                    if (recebimento[i].checked == true) { marcado = true; break; }
                }

                if(marcado == true){
                    document.viagemComAgendamentoForm.action = "<%=request.getContextPath()%>/ViagemComAgendamento.do?metodo=aprovarAgendamento";
                    document.viagemComAgendamentoForm.submit();
                }else{
                    alert("Nenhuma viagem selecionada para recebimento!");
                }

            }

            function reprovarAgendamento(){
                var recebimento = document.getElementsByName("ids_selecionados");
                var marcado = false;

                for (i=0; i<recebimento.length; i++){
                    if (recebimento[i].checked == true) { marcado = true; break; }
                }

                if(marcado == true){
                    document.viagemComAgendamentoForm.action = "<%=request.getContextPath()%>/ViagemComAgendamento.do?metodo=reprovarAgendamento";
                    document.viagemComAgendamentoForm.submit();
                }else{
                    alert("Nenhuma viagem selecionada para recebimento!");
                }

            }

            function isChecked(){
                var exportacao = document.getElementsByName("ids_exportacao");
                var marcado = false;

                for (i=0; i<exportacao.length; i++){
                    if (exportacao[i].checked == true) { marcado = true; break; }
                }

                return marcado;
            }

            function  submitDesfazerRecebimento(){
                if(isChecked()){
                    document.ViagemComAgendamentoForm.action = "<%=request.getContextPath()%>/ViagemComAgendamento.do?metodo=desfazerRecebimento";
                    document.ViagemComAgendamentoForm.submit();
                }else{
                    alert("Nenhuma viagem selecionada para cancelamento!");
                }
            }

        </script>
    </head>
    <body>
        <div id="wrap">
            <jsp:include flush="false" page="/inc/header.jsp" />
            <jsp:include flush="false" page="/inc/sidebar.jsp" />
            <div id="content">

                <h2>Lista de Agendamentos a Aprovar</h2>
                <html:form action="/ViagemComAgendamento.do" method="post">
                    <div style="width:100%; text-align:right;">
                       <acesso:input tipo="select" action="ViagemComAgendamento.do" metodo="aprovar" chave="agendAprovViagem" onclick="aprovarAgendamento();" styleClass="botao" />
                       &nbsp;
                       <acesso:input tipo="select" action="ViagemComAgendamento.do" metodo="reprovar" chave="agendReprovViagem" onclick="reprovarAgendamento();" styleClass="botao" />
                    </div>

                    <div style="width:100%; height:200px; margin-bottom:5px; overflow:auto;">
                    <display:table id="viagensDTO" name="lista1" defaultsort="2" sort="list" defaultorder="ascending" export="false" excludedParams="metodo" requestURI="./ViagemComAgendamento.do" class="tabelaDisplay">
                        <display:column title="<input type='checkbox' name='ids_recebimento_check' onclick=javascript:selecionar_tudo('ids_recebimento'); />" style="width:22px">
                                <input type="checkbox" name="ids_selecionados" value="${viagensDTO.viagem.id}"/>
                        </display:column>
                        <display:column property="viagem.id" title="<label title='Número da solicitação'>Num.</label>" style="text-align:center; width:40px" />
                        <display:column property="viagem.dataSolicitacao" title="<label title='Data da solicitação'>Data solic.</label>" style="text-align:center; width:60px;" format="{0,date,dd/MM/yyyy}" />
                        <display:column property="nomeViajante" title="<label title='Viajante'>Viajante</label>" style="text-align:left; width:280px" />
                        <display:column property="periodo" title="<label title='Período da Viagem'>Período da<br>Viagem</label>" style="text-align:center; width:100px" />
                        <display:column value="${viagensDTO.viagem.diaria * viagensDTO.viagem.qtDiaria}" style="text-align:right; width:50px" title="<label title='Diárias'>Diárias</label>" format="{0,number,0.00}" />
                        <display:column property="viagem.valorAdiantamento" style="text-align:right; width:50px" title="<label title='Adiantamento'>Adiant.</label>" format="{0,number,0.00}" />
                        <display:column title="<label title='Status'>Status</label>" style="text-align:center; width:80px">
                            <c:if test="${viagensDTO.viagem.statusAgendamento == 'AGENDAMENTO_ABERTO'}">
                                <span title="Agendamento aberto" style="cursor:default; color:#404040">Agend. aberto</span>
                            </c:if>
                            <c:if test="${viagensDTO.viagem.statusAgendamento == 'AGENDAMENTO_REPROVADO'}">
                                <span title="Agendamento reprovado" style="cursor:default; color:#404040">Agend. reprov.</span>
                            </c:if>
                        </display:column>

                        <display:column title="<label title='Imprimir solicitação de viagem'>Imp.<br>Solic.</label>" style="text-align:center; width:40px" >
                            <!--a href="./Viagem.do?id=${viagensDTO.viagem.id}&metodo=relatorio">
                                <img align="middle" src="img/pdf.png" width="22" height="22" border="0" title="Imprimir solicitação de viagem" />
                            </a-->
                            <a href="./Viagem.do?id=${viagensDTO.viagem.id}&metodo=relatorioHtml" target="_blank">
                                <img align="middle" src="img/impressora.png" width="23" height="22" border="0" title="Imprimir solicitação de viagem" />
                            </a>
                        </display:column>
                    </display:table>

                    </div>

                    <div style="font-size:12; ">
                        <h2>Lista de Agendamentos Aprovados</h2>
                    </div>

                    <display:table id="viagensDTO" name="lista2" defaultsort="1" sort="list" defaultorder="ascending" export="false" excludedParams="metodo" requestURI="./ViagemComAgendamento.do" pagesize="30" class="tabelaDisplay">
                        <display:column property="viagem.id" title="<label title='Número da solicitação'>Num.</label>" style="text-align:center; width:40px" />
                        <display:column property="viagem.dataSolicitacao" title="<label title='Data da solicitação'>Data solic.</label>" style="text-align:center; width:60px;" format="{0,date,dd/MM/yyyy}" />
                        <display:column property="nomeViajante" title="<label title='Viajante'>Viajante</label>" style="text-align:left; width:220px;" />
                        <display:column property="periodo" title="<label title='Período da Viagem'>Período da<br>Viagem</label>" style="text-align:center; width:100px" />
                        <display:column value="${viagensDTO.viagem.diaria * viagensDTO.viagem.qtDiaria}" style="text-align:right; width:50px" title="<label title='Diárias'>Diárias</label>" format="{0,number,0.00}" />
                        <display:column property="viagem.valorAdiantamento" style="text-align:right; width:50px" title="<label title='Adiantamento'>Adiant.</label>" format="{0,number,0.00}" />
                        <display:column title="<label title='Status'>Status</label>" style="text-align:center; width:80px" >
                            <c:if test="${viagensDTO.viagem.statusAgendamento == 'AGENDAMENTO_APROVADO'}">
                                <span title="Agendamento aprovado" style="cursor:default; color:#404040">Agend. aprov.</span>
                            </c:if>
                        </display:column>

                        <display:column title="<label title='Imprimir solicitação de viagem'>Imp.<br>Solic.</label>" style="text-align:center; width:40px" >
                            <!--a href="./Viagem.do?id=${viagensDTO.viagem.id}&metodo=relatorio">
                                <img align="middle" src="img/pdf.png" width="22" height="22" border="0" title="Imprimir solicitação de viagem" />
                            </a-->
                            <a href="./Viagem.do?id=${viagensDTO.viagem.id}&metodo=relatorioHtml" target="_blank">
                                <img align="middle" src="img/impressora.png" width="23" height="22" border="0" title="Imprimir solicitação de viagem" />
                            </a>
                        </display:column>

                        <display:column title="<label>100%<br>Preenchido</label>" style="text-align:center; width:60px;" >
                            <span style="color:#404040">${viagensDTO.agendamentoOK}</span>
                        </display:column>

                    </display:table>
                </html:form>

            </div>
            <jsp:include flush="false" page="/inc/footer.jsp" />
        </div>
        <msg:alert escopo="session"/>
    </body>
</html>
