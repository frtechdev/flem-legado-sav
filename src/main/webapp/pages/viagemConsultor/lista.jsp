<%@page contentType="text/html" errorPage="/erro.jsp"%> 
<%@page pageEncoding="ISO-8859-1" buffer = "16kb"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@taglib uri="http://flem.org.br/mensagem-tag" prefix="msg"%>
<%@taglib uri="http://flem.org.br/acesso-tag" prefix="acesso" %>
<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
        <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
        <link rel="stylesheet" type="text/css" href="css/800px.css" />
        <link href="css/displaytag.css" rel="stylesheet" type="text/css" />
        <title><fmt:message key="aplicacao.nome" /></title>

        <script language="JavaScript">
            function submitFiltro(){
                document.forms[0].action = "<%=request.getContextPath()%>/ViagemConsultor.do?metodo=filtrar";
                document.forms[0].submit();
                document.forms[0].action = "<%=request.getContextPath()%>/ViagemConsultor.do";
            }
        </script>

    </head>
    <body>
        <div id="wrap">
            <jsp:include flush="false" page="/inc/header.jsp" />
            <jsp:include flush="false" page="/inc/sidebar.jsp" />

            <div id="content">
                <h2>Lista de Solicitação de Viagem para Consultor</h2>
                
                <html:form action="/ViagemConsultor.do" method="post">
                    <div style="width:100%; text-align:right;">
                        <table>
                            <tr>
                                <td width="450" colspan="3">
                                    <strong>Selecione o nome do consultor:</strong>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <html:select property="viagem.codigoConsultorViajante" styleId="consultViajId" value="${codigoConsultor}" onchange="submitFiltro()">
                                        <html:option value="">-- Selecione --</html:option>
                                        <html:optionsCollection name="listaConsultor" value="codigo" label="nome"/>
                                    </html:select>
                                </td>
                                <td style="width: 100%; text-align: right;">
                                    <c:if test="${novaSolicitacao==true}">
                                        <!--html:submit property="metodo" value="novo" styleClass="botao" /-->
                                        <acesso:input tipo="select" nameInput="metodo" typeInput="submit" action="ViagemConsultor.do" metodo="novo" chave="solicNovoViagConsultor" styleClass="botao" />
                                    </c:if>
                                </td>
                            </tr>



                        <c:if test="${novaSolicitacao==false}">
                        <tr>
                            <td colspan="2">
                                <fieldset style="color: #FF0000;">
                                    <legend>Bloqueio de Viagem</legend>
                                    ${novaSolicitacaoTexto}
                                </fieldset>
                            </td>
                        </tr>
                        </c:if>
                        </table>
                    </div>

                    <display:table id="viagensDTO" name="lista" defaultsort="2" sort="list" export="true" excludedParams="metodo" requestURI="./ViagemConsultor.do" class="tabelaDisplay" defaultorder="descending" pagesize="30">
                        <display:column property="viagem.id" title="<label title='Número da solicitação'>Num.</label>" style="text-align:center; width:40px" />
                        <display:column property="viagem.dataSolicitacao" title="<label title='Data da solicitação'>Data solic.</label>" style="text-align:center; width:60px;" format="{0,date,dd/MM/yyyy}" />
                        <display:column property="nomeViajante" title="<label title='Viajante'>Viajante</label>" style="text-align:left; width:220px" />
                        <display:column property="periodo" title="<label title='Período da Viagem'>Período da<br>Viagem</label>" style="text-align:center; width:100px" />
                        <display:column property="viagem.totalDiarias" style="text-align:right; width:50px" title="<label title='Diárias'>Diárias</label>" format="{0,number,0.00}" />
                        <display:column property="viagem.valorAdiantamento" style="text-align:right; width:50px" title="<label title='Adiantamento'>Adiant.</label>" format="{0,number,0.00}" />

                        <display:column title="<label title='Ação'>Ação</label>" style="text-align:center; width:40px" paramId="id" paramProperty="viagem.id" >
                            <c:if test="${viagensDTO.viagem.statusViagem == 'VIAGEM_ABERTA'}">
                                <a href="./ViagemConsultor.do?id=${viagensDTO.viagem.id}&metodo=selecionar"><img align="middle" src="img/editS.png" width="22" height="22" border="0" title="Editar solicitação de viagem" /></a>
                            </c:if>
                            <c:if test="${viagensDTO.viagem.statusViagem == 'VIAGEM_RECEBIDA' && viagensDTO.prestouConta == '0'}">
                                <a href="./PrestacaoContas.do?id=${viagensDTO.viagem.id}&metodo=novo"><img align="middle" src="img/editP.png" width="22" height="22" border="0" title="Criar prestação de contas" /></a>
                            </c:if>
                            <c:if test="${viagensDTO.viagem.statusViagem == 'VIAGEM_CANCELADA'}">
                                <span title="Viagem cancelada" style="cursor:default; color:#404040">Viag. cancel.</span>
                            </c:if>
                            <c:if test="${viagensDTO.prestouConta == '1' && viagensDTO.viagem.statusViagem != 'VIAGEM_CANCELADA' && viagensDTO.viagem.prestacaoContas.statusPrestacaoContas == 'PRESTACAO_INFORMADA'}">
                                <a href="./PrestacaoContas.do?id=${viagensDTO.viagem.id}&metodo=selecionar"><img align="middle" src="img/editP.png" width="22" height="22" border="0" title="Editar prestação de contas" /></a>
                            </c:if>
                        </display:column>

                        <display:column title="<label title='Status'>Status</label>" style="text-align:center; width:80px" >
                            <c:if test="${viagensDTO.viagem.statusViagem == 'VIAGEM_ABERTA'}">
                                <span title="Viagem aberta" style="cursor:default; color:#404040">Viag. aberta</span>
                            </c:if>
                            <c:if test="${viagensDTO.viagem.statusViagem == 'VIAGEM_RECEBIDA'}">
                                <span title="Viagem recebida" style="cursor:default; color:#404040">Viag. receb.</span>
                            </c:if>
                            <c:if test="${viagensDTO.viagem.statusViagem == 'VIAGEM_CANCELADA'}">
                                <span title="Viagem cancelada" style="cursor:default; color:#404040">Viag. cancel.</span>
                            </c:if>
                            <c:if test="${viagensDTO.viagem.statusViagem == 'VIAGEM_FINALIZADA'}">
                                <span title="Viagem finalizada" style="cursor:default; color:#404040">Viag. final.</span>
                            </c:if>
                            <c:if test="${viagensDTO.viagem.prestacaoContas.statusPrestacaoContas == 'PRESTACAO_INFORMADA'}">
                                <span title="Prestação informada" style="cursor:default; color:#404040">Prest. inform.</span>
                            </c:if>
                            <c:if test="${viagensDTO.viagem.prestacaoContas.statusPrestacaoContas == 'PRESTACAO_RECEBIDA'}">
                                <span title="Prestação recebida" style="cursor:default; color:#404040">Prest. receb.</span>
                            </c:if>
                            <c:if test="${viagensDTO.viagem.prestacaoContas.statusPrestacaoContas == 'PRESTACAO_FINALIZADA'}">
                                <span title="Prestação finalizada" style="cursor:default; color:#404040">Prest. final.</span>
                            </c:if>
                            <c:if test="${viagensDTO.viagemComAgendamento.statusAgendamento == 'AGENDAMENTO_ABERTO'}">
                                <span title="Agendamento aberto" style="cursor:default; color:#404040">Agend. aberto</span>
                            </c:if>
                            <c:if test="${viagensDTO.viagemComAgendamento.statusAgendamento == 'AGENDAMENTO_APROVADO'}">
                                <span title="Agendamento aprovado" style="cursor:default; color:#404040">Agend. aprov.</span>
                            </c:if>
                            <c:if test="${viagensDTO.viagemComAgendamento.statusAgendamento == 'AGENDAMENTO_REPROVADO'}">
                                <span title="Agendamento reprovado" style="cursor:default; color:#404040">Agend. reprov.</span>
                            </c:if>
                        </display:column>

                        <display:column title="<label title='Imprimir solicitação de viagem'>Imp.<br>Solic.</label>" style="text-align:center; width:40px" >
                            <a href="./Viagem.do?id=${viagensDTO.viagem.id}&metodo=relatorioHtml" target="_blank">
                                <img align="middle" src="img/impressora.png" width="23" height="22" border="0" title="Imprimir solicitação de viagem" />
                            </a>
                        </display:column>

                        <display:column title="<label title='Imprimir prestação de conta'>Imp.<br>Prest.</label>" style="text-align:center; width:40px" >
                            <c:if test="${viagensDTO.prestouConta == '1'}">
                                <a href="./PrestacaoContas.do?id=${viagensDTO.viagem.id}&metodo=relatorioHtml" target="_blank">
                                    <img align="middle" src="img/impressora.png" width="23" height="22" border="0" title="Imprimir prestação de conta" />
                                </a>
                            </c:if>
                        </display:column>

                        <display:column title="<label title='Excluir solicitação de viagem'>Excluir</label>" style="text-align:center; width:40px" paramId="id" paramProperty="viagem.id" >
                            <c:if test="${viagensDTO.viagem.statusViagem == 'VIAGEM_ABERTA'}">
                                <a href="./ViagemConsultor.do?id=${viagensDTO.viagem.id}&metodo=excluir" onclick="if(confirm('Deseja realmente excluir o(s) registro(s) selecionado(s)?')){return true;} return false;"><img align="middle" src="img/delete.png" width="22" height="22" border="0" title="Excluir solicitação de viagem" /></a>
                            </c:if>
                        </display:column>

                    </display:table>
                </html:form>
            </div>
            <jsp:include flush="false" page="/inc/footer.jsp" />
        </div>
        <msg:alert escopo="session"/>
    </body>
</html>
