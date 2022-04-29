<%@page contentType="text/html" errorPage="/erro.jsp"%> 
<%@page pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://flem.org.br/acesso-tag" prefix="acesso"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@taglib uri="http://flem.org.br/flem-struts-tag" prefix="flemst" %>
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
        <script src="<%=request.getContextPath()%>/js/functions.js?n=1"></script>
        <script src="<%=request.getContextPath()%>/js/solicitacao_viagem.js" charset="utf-8"></script>
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
        </script>

        <script language="JavaScript">

            function verificarDiarias() {
                if(DWRUtil.getValue("dataSaidaPrevista").length > 0 && DWRUtil.getValue("dataRetornoPrevista").length > 0){
                    return true;
                }else{
                    alert("Informe a Programação da Viagem.");
                    return false;
                }
            }

            function verificarPeriodo() {
                if(DWRUtil.getValue("dataSaidaPrevista").length > 0 && DWRUtil.getValue("dataRetornoPrevista").length > 0){
                    var dadosOk = "true";
                    Funcoes.verificarPeriodo(0,0,DWRUtil.getValue("consultViajId"),DWRUtil.getValue("dataSaidaPrevista"),DWRUtil.getValue("dataRetornoPrevista"),
                    {callback:function(permiteAdicionar) {
                        dadosOk = permiteAdicionar;
                        }, async:false});

                    if(dadosOk == "false"){
                        alert("Já existe uma viagem para este Consultor no período informado.");
                        return false;
                    }
                    return true;
                }else{
                    alert("Informe a Programação da Viagem.");
                    return false;
                }
            }

        </script>

        <html:javascript formName="viagemForm" method="validar"/>

        <title><fmt:message key="aplicacao.nome" /></title>

        <script language="JavaScript" src="js/util.js"></script>
        <link href="<%=request.getContextPath()%>/css/calendario.css" rel="stylesheet" type="text/css" />
        <script language="JavaScript" src="<%=request.getContextPath()%>/js/calendar.js"  type="text/javascript" ></script>
        <script language="JavaScript" src="<%=request.getContextPath()%>/js/calendar-pt.js"  type="text/javascript" ></script>
        <script language="JavaScript" src="<%=request.getContextPath()%>/js/calendar-setup.js"  type="text/javascript" ></script>

    </head>
    <body onload="atualizarTipoMoeda();">
        <div id="wrap">
            <jsp:include flush="false" page="/inc/header.jsp" />
            <jsp:include flush="false" page="/inc/sidebar.jsp" />
            <div id="content">
                <h2>Nova Solicitação de Viagem para Consultor</h2>
                <html:form action="/ViagemConsultor.do?metodo=adicionar" onsubmit="return (verificarDiarias() && verificarPeriodo() && validar(this) && validarReserva() && validarTrecho() && validarIntinerario());" method="post"  >
                    <html:hidden property="viagem.codigoConsultorViajante" styleId="consultViajId" value="${viajante.codigo}"/>
                    <table border="0" width="100%">

                        <tr bgcolor="#EEEEEE">
                            <td colspan="4"><strong>Viajante</strong></td>
                        </tr>

                        <tr><td height="10"></td></tr>

                        <tr>
                            <td colspan="4">
                                <table>
                                    <tr>
                                        <td width="600" colspan="3"><strong>Nome:</strong> ${viajante.nome}</td>
                                    </tr>
                                    <tr>
                                        <td width="200"><strong>Banco:</strong> ${viajante.banco}<c:if test="${viajante.banco == null}"><span style="color: #FF0000">N&Atilde;O INFORMADO!</span></c:if><html:hidden property="viagem.banco" value="${viajante.banco}"/></td>
                                        <td width="200"><strong>Agência:</strong> ${viajante.agencia}<c:if test="${viajante.agencia == null}"><span style="color: #FF0000">N&Atilde;O INFORMADO!</span></c:if><html:hidden property="viagem.agencia" value="${viajante.agencia}"/></td>
                                        <td width="200"><strong>Conta:</strong> ${viajante.conta}<c:if test="${viajante.conta == null}"><span style="color: #FF0000">N&Atilde;O INFORMADO!</span></c:if><html:hidden property="viagem.conta" value="${viajante.conta}"/></td>
                                    </tr>
                                    <tr>
                                        <td width="600" colspan="3">
                                            <strong>Destino:</strong>
                                            <%--  <logic:iterate name="listaDestinoViagem"  id="destino">
                                                <html:radio property="destino" styleId="destino" value="${destino}" onchange="obterCidadesConsultor()"/>${destino}
                                            </logic:iterate>
                                              <logic:iterate name="listaDestinoViagem" id="destino">
                                                <html:radio property="destino" styleId="destino" value="${destino}" onchange="obterCidades(); atualizarTipoMoeda();"/><label for="${destino}">${destino}</label>
                                            </logic:iterate> --%>
                                            <html:select property="destino" styleId="destino" onchange="obterCidadesConsultor(); atualizarTipoMoeda();">
                                                <html:option value="">-- Selecione --</html:option>
                                                <<html:options name="listaDestinoViagem" />
                                            </html:select>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>

                        <tr><td colspan="4" height="10"></td></tr>

                        <tr bgcolor="#EEEEEE">
                            <td colspan="4"><strong>Características da Viagem</strong></td>
                        </tr>

                        <tr><td colspan="4" height="10"></td></tr>

                        <tr>
                            <td>Centro de Custo</td>
                            <td colspan="3">
                                <flemst:fatherSelect dependent="viagemCResp" method="obterCentroResponsabilidadePorCc" remoteProxy="Funcoes" property="viagem.codigoCentroCusto" styleId="viagemCc" dtoId="id" dtoValue="descricao" emptySelect="true" >
                                    <html:option value="">-- Selecione --</html:option>
                                    <html:optionsCollection name="listaCentrosCusto" value="id" label="descricaoCompleta" />
                                </flemst:fatherSelect>
                            </td>
                        </tr>

                        <tr>
                            <td>Centro de Responsabilidade</td>
                            <td colspan="3">
                                <html:select property="viagem.codigoCentroResponsabilidade" styleId="viagemCResp" value="">
                                    <html:option value="">-- Selecione --</html:option>
                                    <html:optionsCollection name="listaCentrosResponsabilidade" value="id" label="descricao"/>
                                </html:select>

                            </td>
                        </tr>

                        <tr>
                            <td>Natureza</td>
                            <td colspan="3">
                                <html:select property="natureza" value="">
                                    <html:option value="">-- Selecione --</html:option>
                                    <html:optionsCollection name="listaNaturezas" value="codigo" label="nome"/>
                                </html:select>
                            </td>
                        </tr>

                        <tr>
                            <td colspan="4">
                                Descrição
                                <br />
                                <html:textarea property="viagem.descricao" cols="80" rows="2"/>
                            </td>
                        </tr>

                        <tr><td colspan="4" height="10"></td></tr>

                        <tr bgcolor="#EEEEEE">
                            <td colspan="4"><strong>Programação da Viagem</strong></td>
                        </tr>

                        <tr><td colspan="4" height="10"></td></tr>

                        <tr>
                            <td width="185px">
                                <fieldset style="width:185px;">
                                    <legend>Saída Prevista</legend>
                                    <table border="0">
                                        <tr>
                                            <td>Data</td>
                                            <td>Hora</td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <script>liberacaoRetroativo = ${liberacaoRetroativo}</script>
                                                <html:text styleId="dataSaidaPrevista" property="dataSaidaPrevista" styleClass="data" size="8" maxlength="10" onchange="calcularQtdDiarias(); setarDataItinerario();" style="float: left;" /><div style="float: left;"><img border="0" alt="Selecione uma data" src="<%=request.getContextPath()%>/img/box_calendario.gif" onclick="return showCalendar('dataSaidaPrevista', '%d/%m/%Y', 'calcularQtdDiarias(); setarDataItinerario();');"/></div>
                                            </td>
                                            <td>
                                                <html:text property="horaSaidaPrevista" styleId="horaSaidaPrevista" styleClass="hora" size="5" maxlength="5" onchange="calcularQtdDiarias();" />
                                            </td>
                                        </tr>
                                    </table>
                                </fieldset>
                            </td>
                            <td width="185px">
                                <fieldset style="width:185px;">
                                    <legend>Retorno Previsto</legend>
                                    <table>
                                        <tr>
                                            <td>Data</td>
                                            <td>Hora</td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <html:text property="dataRetornoPrevista" styleId="dataRetornoPrevista" styleClass="data" size="8" maxlength="10" onchange="calcularQtdDiarias();" style="float: left;" /><div style="float: left;"><img border="0" alt="Selecione uma data" src="<%=request.getContextPath()%>/img/box_calendario.gif" onclick="return showCalendar('dataRetornoPrevista', '%d/%m/%Y', 'calcularQtdDiarias()')"/></div>
                                            </td>
                                            <td>
                                                <html:text property="horaRetornoPrevista" styleId="horaRetornoPrevista" styleClass="hora" size="5" maxlength="5" onchange="calcularQtdDiarias();"/>
                                            </td>
                                        </tr>
                                    </table>
                                </fieldset>
                            </td>
                            <td colspan="2">
                                <fieldset style="width:100px;"><!-- width:280px -->
                                    <legend>Passagem</legend>
                                    <table>
                                        <tr>
                                            <td width="70" colspan="2">Reserva</td>
                                            <!--td>Código</td-->
                                        </tr>
                                        <tr>
                                            <td colspan="2">
                                                <!--html:select property="temReserva" styleId="temReserva" onchange="ativaInativaReserva()"-->
                                                <html:select property="temReserva" styleId="temReserva"> <!-- onchange="ativaInativaReserva()"-->
                                                    <html:option value="0">Não</html:option>
                                                    <html:option value="1">Sim</html:option>
                                                </html:select>
                                            </td>
                                            <!--td>
                                                html:text property="codigoReserva" value="" styleId="codigoReserva" disabled="true" size="10"/>
                                            </td-->
                                        </tr>
                                        <!--tr style="width: 100%">
                                            <td colspan="2">Companhia</td>
                                        </tr>
                                        <tr>
                                            <td colspan="2">
                                                html:select property="companhiaId" value="" styleId="companhiaReserva" disabled="true" style="width: 260px;">
                                                    html:option value="">-- Selecione --/html:option>
                                                    html:options collection="listaCompanhias" property="id" labelProperty="nome"/>
                                                /html:select>
                                            </td>
                                        </tr-->
                                    </table>
                                </fieldset>
                            </td>
                        </tr>

                        <tr><td colspan="4" height="10"></td></tr>

                        <tr bgcolor="#EEEEEE">
                            <td colspan="4"><strong>Diária</strong></td>
                        </tr>

                        <tr><td colspan="4" height="10"></td></tr>

                        <tr>
                            <td colspan="4">
                                Tipo de Diária:
                                <html:select property="tipoDiaria" styleId="tipoDiariaId" onchange="alterarTipoDiaria();" value="0">
                                    <html:optionsCollection name="listaTiposDiaria" label="nome" value="id"/>
                                </html:select>
                            </td>
                        </tr>

                        <tr>
                            <td colspan="4">
                                <table>
                                    <tr>
                                        <td width="200">
                                            <span id="tituloFCM" style="color:#FF0000;">Informe a Diária Especial <span id="tipoDiariaE" style="color: #FF0000"></span>:</span>
                                            <br />
                                            <html:text property="diariaE" styleId="diariaE" size="10" style="text-align:right;" styleClass="real" onkeyup="obterDiaria(this.value,'ESPECIAL')" />
                                        </td>
                                        <td colspan="2" >
                                            <span id="tituloDesc" style="color:#FF0000;">Informe a Descrição:</span>
                                            <br />
                                            <html:text property="descricaoE" styleId="descricaoE" size="40" />
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>

                        <tr>
                            <td colspan="4">
                                <table width="100%">
                                    <tr>
                                        <td>
                                            <div id="valorDiariaLabel1" style="display: block;">
                                                Valor Diária <span id="tipoValorDiaria" style="color: #404040"></span>:
                                                <br />
                                                <html:text property="diaria" styleId="valorDiaria" style="text-align:right; border-style:none;" styleClass="real" readonly="true" size="10" />
                                                <html:hidden property="diariaP" styleId="valorDiariaP" styleClass="real" />
                                            </div>
                                            <div id="valorDiariaLabel2" style="display: none; width: 98px;">
                                                <br />
                                                <html:hidden property="diaria" styleId="valorDiaria" styleClass="real" />
                                                <html:hidden property="diariaP" styleId="valorDiariaP" styleClass="real" />
                                            </div>
                                        </td>
                                        <td>
                                            Qtde. Diárias:
                                            <br />
                                            <input name="qtDiaria" id="qtDiaria" style="text-align:right; border-style:none;" class="real" readonly size="10">
                                            <input type="hidden" id="qtDiariaAux" class="real">
                                        </td>
                                        <td>
                                            Total em Diárias <span id="tipoTotalDiarias" style="color: #404040"></span>:
                                            <br />
                                            <input name="totalDiarias" id="totalDiarias" style="text-align:right; border-style:none;" class="real" readonly size="10">
                                        </td>
                                        <td>
                                            Valor Adiant. <span id="tipoValorAdiantamento" style="color: #404040"></span>:
                                            <br />
                                            <html:text property="valorAdiantamento" styleId="valorAdiantamento" style="text-align:right;" size="10" styleClass="real" onkeyup="setTotalAdiantamento()" />
                                        </td>
                                        <td>
                                            Total <span id="tipoTotalAdiantamento" style="color: #404040"></span> (Diárias + Adiant.):
                                            <br />
                                            <html:text property="totalAdiantamento" styleId="totalAdiantamento" style="text-align:right; border-style:none;" styleClass="real" readonly="true" size="10" />
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>

                        <tr>
                            <td colspan="4">
                                <div id="tipoDet" style="display: none;">
                                <fieldset style="width: 940px;">
                                    <legend style="color:#FF0000;">Viagem considerando os valores e períodos abaixo</legend>
                                    <table widt="900" border="0">
                                        <tr>
                                            <td align="center" width="110">Data Inicio</td>
                                            <td align="center" width="60">Hora</td>
                                            <td align="center" width="105">Data Fim</td>
                                            <td align="center" width="75">Hora</td>
                                            <td align="center" width="68">Qtde. Diárias</td>
                                            <td align="center" width="57">Valor Diária</td>
                                            <td align="center" width="65">Total em Diárias</td>
                                            <td align="center" width="226">Observações</td>
                                            <td align="center" width="65">&nbsp;</td>
                                        </tr>
                                    </table>
                                    <table widt="900" border="0">
                                        <thead id="trecho"></thead>
                                        <tr>
                                            <td width="110">
                                                <input type="text" name="dataInicio" id="dataInicio" class="data" size="8" maxlength="10" onchange="calcularQtdDiariasTrecho();" style="float: left;" /><div style="float: left;"><img border="0" alt="Selecione uma data" src="<%=request.getContextPath()%>/img/box_calendario.gif" onclick="return showCalendar('dataInicio', '%d/%m/%Y', 'calcularQtdDiariasTrecho()')"/></div>
                                            </td>
                                            <td width="60">
                                                <input type="text" name="horaInicio" id="horaInicio" class="hora" size="5" maxlength="5" onchange="calcularQtdDiariasTrecho();"/>
                                            </td>
                                            <td width="105">
                                                <input type="text" name="dataFim" id="dataFim" class="data" size="8" maxlength="10" onchange="calcularQtdDiariasTrecho();" style="float: left;" /><div style="float: left;"><img border="0" alt="Selecione uma data" src="<%=request.getContextPath()%>/img/box_calendario.gif" onclick="return showCalendar('dataFim', '%d/%m/%Y', 'calcularQtdDiariasTrecho()')"/></div>
                                            </td>
                                            <td width="65">
                                                <input type="text" name="horaFim" id="horaFim" class="hora" size="5" maxlength="5" onchange="calcularQtdDiariasTrecho();"/>
                                            </td>
                                            <td width="50">
                                                <input name="qtDiariaTrecho" id="qtDiariaTrecho" style="text-align:right; border-style:none;" class="real" readonly size="6">
                                            </td>
                                            <td width="50">
                                                <input name="valorDiariaTrecho" id="valorDiariaTrecho" style="text-align:right;" class="real" size="6" onkeyup="setTotalDiariasTrecho()">
                                                <script>document.getElementById("valorDiariaTrecho").value=document.getElementById("valorDiariaP").value;</script>
                                            </td>
                                            <td width="50">
                                                <input name="totalDiariasTrecho" id="totalDiariasTrecho" style="text-align:right; border-style:none;" class="real" readonly size="6">
                                            </td>
                                            <td width="50">
                                                <input name="descricaoTrecho" id="descricaoTrecho" size="26">
                                            </td>
                                            <td width="50"><a href="javascript: adicionarLinhaTrecho();">Adicionar</a></td>
                                        </tr>
                                    </table>
                                </fieldset>
                                </div>
                            </td>
                        </tr>

                        <tr><td colspan="4" height="10"></td></tr>

                        <tr bgcolor="#EEEEEE">
                            <td colspan="4"><strong>Itinerário</strong></td>
                        </tr>

                        <tr><td colspan="4" height="10"></td></tr>

                        <tr>
                            <td colspan="4">
                                <table>
                                    <tr>
                                        <td width="110">Data</td>
                                        <td width="220">Cidade Origem</td>
                                        <td width="220">Cidade Destino</td>
                                        <td width="200">Observação</td>
                                    </tr>
                                </table>
                                <table id="tabelaItinerario">
                                    <thead id="itinerario"></thead>
                                    <tr>
                                        <td width="110"><input id="dataLinhaItinerario" class="data" size="8" maxlength="10" style="float: left;" ><div style="float: left;"><img border="0" alt="Selecione uma data" src="<%=request.getContextPath()%>/img/box_calendario.gif" onclick="return showCalendar('dataLinhaItinerario', '%d/%m/%Y');"/></div></td>
                                        <td id="cidOri" width="220">
                                            <select name="cidadeOrigemId" id="cidadeOrigemId" style="width: 170px;">
                                                <option value="">-- Selecione --</option>
                                            </select>
                                        </td>
                                        <td id="cidDes" width="220">
                                            <select name="cidadeDestinoId" id="cidadeDestinoId" style="width: 170px;">
                                                <option value="">-- Selecione --</option>
                                            </select>
                                        </td>
                                        <td width="200"><input id="obsLinhaItinerario" size="22"></td>
                                        <td><a href="javascript: adicionarLinhaItinerario();">Adicionar</a></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>

                        <tr>
                            <td colspan="4">
                                Observações
                                <br />
                                <html:textarea property="observacao" cols="80" rows="2" value=""/>
                            </td>
                        </tr>

                        <tr>
                            <td colspan="4">
                                <acesso:input tipo="salvar" action="ViagemConsultor.do" metodo="salvar" chave="solicSalvarViagConsultor" styleClass="botao" />
                                &nbsp;
                                <acesso:input tipo="select" action="ViagemConsultor.do" metodo="cancelar" chave="*" styleClass="botao" onclick="document.location.href='ViagemConsultor.do'" />
                            </td>
                        </tr>
                    </table>
                </html:form>
                <jsp:include flush="false" page="/inc/footer.jsp" />
            </div>

        </div>
    </body>
</html:html>
