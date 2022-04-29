<%@page contentType="text/html" errorPage="/erro.jsp"%> 
<%@page pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
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

        <script language="JavaScript" src="js/util.js"></script>
        <link href="<%=request.getContextPath()%>/css/calendario.css" rel="stylesheet" type="text/css" />
        <script language="JavaScript" src="<%=request.getContextPath()%>/js/calendar.js"  type="text/javascript" ></script>
        <script language="JavaScript" src="<%=request.getContextPath()%>/js/calendar-pt.js"  type="text/javascript" ></script>
        <script language="JavaScript" src="<%=request.getContextPath()%>/js/calendar-setup.js"  type="text/javascript" ></script>
        <script src="<%=request.getContextPath()%>/js/jquery-1.4.2.min.js"></script>
        <script src="<%=request.getContextPath()%>/js/jquery.maskedinput.js"></script>
        <script src="<%=request.getContextPath()%>/js/jquery.maskMoney.js"></script>

        <script type="text/javascript" >
            jQuery.noConflict();
            jQuery(document).ready(function(){
                jQuery("input[class*=data]").mask("99/99/9999", {placeholder: " "});
                jQuery("input[class*=hora]").mask("99:99", {placeholder: " "});
                jQuery("input[class*=real]").maskMoney({symbol: "", decimal: ",", thousands: "."});
 
                jQuery("input[class*=data]").change(function() { if (this.value.indexOf(" ") == -1) ValidarDATA(this, '/'); else this.value = ""; });
                jQuery("input[class*=hora]").change(function() { if (this.value.indexOf(" ") == -1) ValidarHORA(this, ':'); else this.value = ""; });
            });

            jQuery(document).load(function(){
                jQuery("input[class*=real]").maskMoney({symbol: "", decimal: ",", thousands: "."});
            });
        </script>
        <script type="text/javascript"  language="JavaScript">
            function validarFiltro() {
                var stringVazia = "";
                var retorno = true;
                var tipoFiltro = document.getElementById("tipoFiltro");
                var tipoFiltroValue = document.getElementById("tipoFiltroValue");
                var codigoCentroCusto = document.getElementById("codigoCentroCusto");
                var codigoCentroResponsabilidade = document.getElementById("codigoCentroResponsabilidade");
                var codigoFonteRecurso = document.getElementById("codigoFonteRecurso");
                var destino = document.getElementById("destino");
                var dataSaidaPrevista = document.getElementById("dataSaidaPrevista");
                var dataRetornoPrevista = document.getElementById("dataRetornoPrevista");
                if ((tipoFiltro[tipoFiltro.selectedIndex].value == stringVazia) &&
                    (tipoFiltroValue.value == stringVazia) &&
                    (codigoCentroCusto.value == stringVazia) &&
                    (codigoCentroResponsabilidade.value == stringVazia) &&
                    (codigoFonteRecurso.value == stringVazia) &&
                    (destino.value == stringVazia) &&
                    (dataSaidaPrevista.value == stringVazia) &&
                    (dataRetornoPrevista.value == stringVazia)) {
                    alert("Preencha um dos campos para filtrar!");
                    retorno = false;
                } else if (tipoFiltro[tipoFiltro.selectedIndex].value != stringVazia &&
                    tipoFiltroValue.value == stringVazia) {
                    tipoFiltroValue.focus();
                    alert("Digite um parâmetro para a pesquisa");
                    retorno = false;
                } else if (tipoFiltro[tipoFiltro.selectedIndex].value == stringVazia&&
                    tipoFiltroValue.value != stringVazia) {
                    tipoFiltro.focus();
                    alert("Selecione um tipo de filtragem");
                    retorno = false;
                }
                return retorno;

            }
        </script>
        <html:javascript formName="viagemForm" method="validar" page="1"/>
        <title><fmt:message key="aplicacao.nome" /></title>
    </head>
    <body>
        <div id="wrap">
            <jsp:include flush="false" page="/inc/header.jsp" />
            <jsp:include flush="false" page="/inc/sidebar.jsp" />

            <div id="content">
                <h2>Consulta de Viagens</h2>

                <html:form action="/ConsultaViagem.do?metodo=filtrar" method="post" onsubmit="return (validarFiltro() && validar(this))">
                    <table width="100%">
                        <tr>

                            <td width="200">
                                Filtrar por:

                            </td>
                            <td colspan="2">
                                <html:select property="tipoFiltro" styleId="tipoFiltro">
                                    <html:option value="">-- Selecione --</html:option>
                                    <html:optionsCollection name="listaTipoFiltro" value="id" label="descricao"/>
                                </html:select>&nbsp;
                                <html:text property="tipoFiltroValue" styleId="tipoFiltroValue" size="35" />
                                <script type="text/javascript" >
                                    if(document.getElementById("tipoFiltroValue").value == "0"){
                                        document.getElementById("tipoFiltroValue").value = "";
                                    }
                                </script>
                            </td>
                        </tr>

                        <tr>
                            <td>
                                Centro de Custo:
                            </td>
                            <td colspan="2">
                                <html:select property="viagem.codigoCentroCusto" styleId="codigoCentroCusto">
                                    <html:option value="">-- Selecione --</html:option>
                                    <html:optionsCollection name="listaCentrosCusto" value="id" label="descricaoCompleta"/>
                                </html:select>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                Centro de Responsabilidade:
                            </td>
                            <td colspan="2">
                                <html:select property="viagem.codigoCentroResponsabilidade"  styleId="codigoCentroResponsabilidade" >
                                    <html:option value="">-- Selecione --</html:option>
                                    <html:optionsCollection name="listaCentrosResponsabilidade" value="id" label="descricao"/>
                                </html:select>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                Fonte de Recurso:
                            </td>
                            <td colspan="2">
                                <html:select property="viagem.codigoFonteRecurso" styleId="codigoFonteRecurso">
                                    <html:option value="">-- Selecione --</html:option>
                                    <html:optionsCollection name="listaFonteRecurso" value="codigo" label="descricaoCompleta"/>
                                </html:select>
                            </td>
                        </tr>

                        <tr>
                            <td>
                                Destino:
                            </td>
                            <td width="100">
                                <html:select property="destino" styleId="destino" >
                                    <html:option value="">-- Selecione --</html:option>
                                    <html:options name="listaDestinos" />
                                </html:select>
                            </td>

                            <td>
                                Período:
                                <html:text property="dataSaidaPrevista" size="9" styleId="dataSaidaPrevista" styleClass="data"/><img border="0" alt="Selecione uma data" src="<%=request.getContextPath()%>/img/box_calendario.gif" onclick="return showCalendar('dataSaidaPrevista', '%d/%m/%Y')"/>
                                &nbsp;
                                à:
                                &nbsp;
                                <html:text property="dataRetornoPrevista" size="9" styleId="dataRetornoPrevista" styleClass="data"/><img border="0" alt="Selecione uma data" src="<%=request.getContextPath()%>/img/box_calendario.gif" onclick="return showCalendar('dataRetornoPrevista', '%d/%m/%Y')"/>
                            </td>
                        </tr>

                        <tr>
                            <td colspan="6">
                                <input type="submit" value="Filtrar" class="botao"/>
                            </td>
                        </tr>
                    </table>

                    <display:table id="viagensDTO" name="lista" defaultsort="2" sort="list" defaultorder="descending" export="false" requestURI="./ConsultaViagem.do" pagesize="40" class="tabelaDisplay">
                        <display:column property="viagem.id" title="<label title='Número da solicitação'>Num.</label>" style="text-align:center; width:40px" />
                        <display:column property="viagem.dataSolicitacao" title="<label title='Data da solicitação'>Data solic.</label>" style="text-align:center; width:60px;" format="{0,date,dd/MM/yyyy}" />
                        <display:column property="nomeViajante" title="<label title='Viajante'>Viajante</label>" style="text-align:left; width:350px" />
                        <display:column property="periodo" title="<label title='Período da Viagem'>Período da<br>Viagem</label>" style="text-align:center; width:115px" />
                        <display:column value="${viagensDTO.viagem.totalDiarias}" style="text-align:right; width:50px" title="<label title='Diárias'>Diárias</label>" format="{0,number,0.00}" />
                        <display:column property="viagem.valorAdiantamento"  style="text-align:right; width:50px" title="<label title='Adiantamento'>Adiant.</label>" format="{0,number,0.00}" />

                        <display:column title="<label title='Status'>Status</label>" style="text-align:center; width:100px" >
                            <c:if test="${viagensDTO.viagem.statusViagem == 'VIAGEM_ABERTA'}">
                                <span title="Viagem aberta" style="cursor:default; color:#404040">Viag. aberta</span>
                            </c:if>
                            <c:if test="${viagensDTO.viagem.statusViagem == 'VIAGEM_RECEBIDA'}">
                                <span title="Viagem recebida" style="cursor:default; color:#404040">Viag. receb.</span>
                            </c:if>
                            <c:if test="${viagensDTO.viagem.prestacaoContas.statusPrestacaoContas == 'PRESTACAO_INFORMADA'}">
                                <span title="Prestação informada" style="cursor:default; color:#404040">Prest. inform.</span>
                            </c:if>
                            <c:if test="${viagensDTO.viagem.prestacaoContas.statusPrestacaoContas == 'PRESTACAO_FINALIZADA'}">
                                <span title="Prestação finalizada" style="cursor:default; color:#404040">Prest. final.</span>
                            </c:if>
                            <c:if test="${viagensDTO.viagem.prestacaoContas.statusPrestacaoContas == 'PRESTACAO_RECEBIDA'}">
                                <span title="Prestação recebida" style="cursor:default; color:#404040">Prest. receb.</span>
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
                            <c:if test="${viagensDTO.viagem.codigoCentroCusto != null}">
                                <a href="./Viagem.do?id=${viagensDTO.viagem.id}&metodo=relatorioHtml" target="_blank">
                                    <img align="middle" src="img/impressora.png" width="22" height="22" border="0" title="Imprimir solicitação de viagem" />
                                </a>
                            </c:if>
                        </display:column>

                        <display:column title="<label title='Imprimir prestação de conta'>Imp.<br>Prest.</label>" style="text-align:center; width:40px" >
                            <c:if test="${viagensDTO.prestouConta == '1'}">
                                <a href="./PrestacaoContas.do?id=${viagensDTO.viagem.id}&metodo=relatorioHtml" target="_blank">
                                    <img align="middle" src="img/impressora.png" width="22" height="22" border="0" title="Imprimir prestação de conta" />
                                </a>
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
