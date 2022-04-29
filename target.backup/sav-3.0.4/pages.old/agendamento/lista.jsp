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

        <script language="JavaScript">
            function submitFiltro(){
                document.forms[0].action = "<%=request.getContextPath()%>/ViagemComAgendamento.do?metodo=filtrar";
                document.forms[0].submit();
                document.forms[0].action = "<%=request.getContextPath()%>/ViagemComAgendamento.do";
            }
        </script>

        <title><fmt:message key="aplicacao.nome" /></title>
    </head>
    <body>
        <div id="wrap">
            <jsp:include flush="false" page="/inc/header.jsp" />
            <jsp:include flush="false" page="/inc/sidebar.jsp" />
            <div id="content">

                <h2>Lista de Viagens Agendadas</h2>
                <html:form action="/ViagemComAgendamento.do" method="post">

                    <table>
                        <tr>
                            <td>
                                <html:select property="viagem.codigoDominioUsuarioViajante" styleId="funcViajId" value="${matriculaUsuario}" onchange="submitFiltro()">
                                    <html:option value="${matriculaUsuarioLogado}">${nmeUsuarioLogado}</html:option>
                                    <html:optionsCollection name="listaFuncionario" value="matricula" label="nome"/>
                                </html:select>
                            </td>
                            <td style="width: 100%; text-align: right;">
                                <acesso:input tipo="select" nameInput="metodo" typeInput="submit" action="ViagemComAgendamento.do" metodo="novo" chave="agendViagNovoFuncionario" styleClass="botao" />
                            </td>
                        </tr>
                        
                        <c:if test="${deFerias==true}">
                            <tr>
                                <td colspan="2">
                                    <fieldset style="color: #FF0000;">
                                        <legend>Aviso</legend>
                                        Funcionário afastado no momento!
                                    </fieldset>
                                </td>
                            </tr>
                        </c:if>
                            
                    </table>

                    <display:table id="viagensDTO" name="lista" defaultsort="1" sort="list" export="false" excludedParams="metodo" requestURI="./ViagemComAgendamento.do" pagesize="30" class="tabelaDisplay">
                        <display:column property="viagem.id" title="<label title='Número da solicitação'>Num.</label>" style="text-align:center; width:40px" />
                        <display:column property="viagem.dataSolicitacao" title="<label title='Data da solicitação'>Data solic.</label>" style="text-align:center; width:60px;" format="{0,date,dd/MM/yyyy}" />
                        <display:column property="nomeViajante" title="<label title='Viajante'>Viajante</label>"/>
                        <display:column property="periodo" title="<label title='Período da Viagem'>Período da<br>Viagem</label>" style="text-align:center; width:105px" />
                        <display:column property="viagem.totalDiarias" style="text-align:right; width:50px" title="<label title='Diárias'>Diárias</label>" format="{0,number,0.00}" />
                        <display:column property="viagem.valorAdiantamento" style="text-align:right; width:50px" title="<label title='Adiantamento'>Adiant.</label>" format="{0,number,0.00}" />
                        <display:column title="<label title='Ação'>Ação</label>" style="text-align:center; width:40px" href="./ViagemComAgendamento.do?metodo=selecionar" paramId="id" paramProperty="viagem.id" >
                            <c:if test="${viagensDTO.viagem.statusAgendamento != 'AGENDAMENTO_REPROVADO'}">
                                <img align="middle" src="img/edit.png" width="22" height="22" border="0" title="Editar agendamento"/>
                            </c:if>
                        </display:column>
                        <display:column title="<label title='Status'>Status</label>" style="text-align:center; width:80px">
                            <c:if test="${viagensDTO.viagem.statusAgendamento == 'AGENDAMENTO_ABERTO'}">
                                <span title="Agendamento aberto" style="cursor:default; color:#404040">Agend. aberto</span>
                            </c:if>
                            <c:if test="${viagensDTO.viagem.statusAgendamento == 'AGENDAMENTO_REPROVADO'}">
                                <span title="Agendamento reprovado" style="cursor:default; color:#404040">Agend. reprov.</span>
                            </c:if>
                            <c:if test="${viagensDTO.viagem.statusAgendamento == 'AGENDAMENTO_APROVADO'}">
                                <span title="Agendamento aprovado" style="cursor:default; color:#404040">Agend. aprov.</span>
                            </c:if>
                        </display:column>
                        <display:column title="<label title='Imprimir Solicitação de Viagem'>Imp.<br>Solic.</label>" style="text-align:center; width:40px" >
                            <!--a href="./Viagem.do?id=${viagensDTO.viagem.id}&metodo=relatorio">
                                <img align="middle" src="img/pdf.png" width="22" height="22" border="0" title="Imprimir solicitação de viagem" />
                            </a-->
                            <a href="./Viagem.do?id=${viagensDTO.viagem.id}&metodo=relatorioHtml" target="_blank">
                                <img align="middle" src="img/impressora.png" width="23" height="22" border="0" title="Imprimir solicitação de viagem" />
                            </a>
                        </display:column>
                        <display:column title="<label title='Excluir agendamento'>Excluir</label>" style="text-align:center; width:40px">
                            <c:if test="${viagensDTO.viagem.statusAgendamento == 'AGENDAMENTO_ABERTO'}">
                                  <a href="./ViagemComAgendamento.do?ids_exclusao=${viagensDTO.viagem.id}&metodo=excluir" onclick="if(confirm('Deseja realmente excluir o(s) registro(s) selecionado(s)?')){return true;} return false;"><img align="middle" src="img/delete.png" width="22" height="22" border="0" title="Excluir agendamento"/></a>
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
