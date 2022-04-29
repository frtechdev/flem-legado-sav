<%@page contentType="text/html" errorPage="/erro.jsp"%> 
<%@page pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://flem.org.br/acesso-tag" prefix="acesso"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
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
        <script src="<%=request.getContextPath()%>/js/functions.js"></script>
        <script src="<%=request.getContextPath()%>/js/solicitacao_viagem.js?n=1" charset="utf-8"></script>
        <script src="<%=request.getContextPath()%>/js/jquery-1.4.2.min.js"></script>
        <script src="<%=request.getContextPath()%>/js/jquery.maskedinput.js"></script>
        <script src="<%=request.getContextPath()%>/js/jquery.maskMoney.js"></script>

        <html:javascript formName="viagemComAgendamentoForm" method="validar"/>

        <title><fmt:message key="aplicacao.nome" /></title>

        <script language="JavaScript">

            jQuery.noConflict();
            jQuery(document).ready(function(){
                jQuery("input[class*=data]").mask("99/99/9999", {placeholder: " "});
                jQuery("input[class*=hora]").mask("99:99", {placeholder: " "});
                jQuery("input[class*=real]").maskMoney({symbol: "", decimal: ",", thousands: "."});

                jQuery("input[class*=data]").change(function() { if (this.value.indexOf(" ") == -1) ValidarDATA(this, '/'); else this.value = ""; });
                jQuery("input[class*=hora]").change(function() { if (this.value.indexOf(" ") == -1) ValidarHORA(this, ':'); else this.value = ""; });
            });

            function setItinerario(){
            <logic:iterate name="itensItinerario" id="itens">
                    dta = "${itens.data}";
                    dta = dta.substr(8,2)+"/"+dta.substr(5,2)+"/"+dta.substr(0,4);
                    document.getElementById("dataLinhaItinerario").value = dta;
                    document.getElementById("cidadeOrigemId").value = ${itens.origem.id}
                    document.getElementById("cidadeDestinoId").value = ${itens.destino.id}
                    document.getElementById("obsLinhaItinerario").value = "${itens.observacoes}";
                    adicionarLinhaItinerario();
            </logic:iterate>
                }

                function setTrecho(){
            <logic:iterate name="itensTrecho" id="itensT">
                    dtaI = "${itensT.dataInicio}";
                    hI = dtaI.substr(11,5);
                    dtaI = dtaI.substr(8,2)+"/"+dtaI.substr(5,2)+"/"+dtaI.substr(0,4);
                    dtaF = "${itensT.dataFim}";
                    hF = dtaF.substr(11,5);
                    dtaF = dtaF.substr(8,2)+"/"+dtaF.substr(5,2)+"/"+dtaF.substr(0,4);

                    document.getElementById("dataInicio").value = dtaI;
                    document.getElementById("horaInicio").value = hI;
                    document.getElementById("dataFim").value = dtaF;
                    document.getElementById("horaFim").value = hF;
                    document.getElementById("qtDiariaTrecho").value = float2moeda(${itensT.qtDiaria});
                    document.getElementById("valorDiariaTrecho").value = float2moeda(${itensT.diaria});
                    document.getElementById("totalDiariasTrecho").value = float2moeda(${itensT.qtDiaria}*${itensT.diaria});
                    document.getElementById("descricaoTrecho").value = "${itensT.observacao}";
                    adicionarLinhaTrechoInicial();
            </logic:iterate>
                }

                function verificarPeriodo() {
                    if(DWRUtil.getValue("dataSaidaPrevista").length > 0 && DWRUtil.getValue("dataRetornoPrevista").length > 0){
                        var dadosOk = "true";
                        Funcoes.verificarPeriodo(
                        DWRUtil.getValue("id_viagem"),
                        DWRUtil.getValue("funcViajId"),
                        "0",
                        DWRUtil.getValue("dataSaidaPrevista"),
                        DWRUtil.getValue("horaSaidaPrevista"),
                        DWRUtil.getValue("dataRetornoPrevista"), 
                        DWRUtil.getValue("horaRetornoPrevista"),
                        {callback:function(permiteAdicionar) {
                                dadosOk = permiteAdicionar;
                            }, async:false});

                        if(dadosOk == "false"){
                            alert("J� existe uma viagem agendada para este Funcion�rio no per�odo informado.");
                            return false;
                        }
                        return true;
                    }else{
                        alert("Informe a Programa��o da Viagem.");
                        return false;
                    }
                }



                function verificarDiarias() {
                    if(DWRUtil.getValue("dataSaidaPrevista").length > 0 && DWRUtil.getValue("dataRetornoPrevista").length > 0){
                        var dadosOk = true;
                        Funcoes.verificarDiarias(DWRUtil.getValue("funcViajId"),DWRUtil.getValue("dataSaidaPrevista"), DWRUtil.getValue("horaSaidaPrevista"), DWRUtil.getValue("dataRetornoPrevista"), DWRUtil.getValue("horaRetornoPrevista"), DWRUtil.getValue("qtDiaria"),DWRUtil.getValue("valorDiaria"),DWRUtil.getValue("id_viagem"),DWRUtil.getValue("destino"), DWRUtil.getValue("tipoDiaria"),DWRUtil.getValue("totalDiarias"),
                        {callback:function(permiteAdicionar) {
                                dadosOk = permiteAdicionar;
                            }, async:false});

                        if(dadosOk == false){
                            alert("O valor total de Viagens do Funcion�rio informado ultrapassou 50% do sal�rio. Por favor, refa�a a Programa��o da Viagem.");
                            return false;
                        }
                        return true;
                    }else{
                        alert("Informe a Programa��o da Viagem.");
                        return false;
                    }
                }

                function addTrecho(){
                    var datasS = new Array();
                    var datasR = new Array();
                    var valorDiariaTrecho = new Array();
                    var dadosOk = false;

                    verificarDiariasTrecho(datasS, datasR, valorDiariaTrecho);

                    Funcoes.verificarDiariasTrecho(DWRUtil.getValue("id_viagem"), DWRUtil.getValue("funcViajId"), datasS, datasR, valorDiariaTrecho,
                    {callback:function(permiteAdicionar) {
                            dadosOk = permiteAdicionar;
                        }, async:false});

                    if(dadosOk){
                        adicionarLinhaTrecho();
                    }else{
                        alert("O valor total de Viagens do Funcion�rio informado ultrapassou 50% do sal�rio. Por favor, refa�a a Programa��o da Viagem.");
                    }
                }
            
            function verificaPeriodoFerias(dtini, dtfim){
                if(DWRUtil.getValue("dataSaidaPrevista").length > 0 && DWRUtil.getValue("dataRetornoPrevista").length > 0 && dtini != '0'){
                    datInicio = new Date(DWRUtil.getValue("dataSaidaPrevista").substring(6,10),
                                        DWRUtil.getValue("dataSaidaPrevista").substring(3,5),
                                        DWRUtil.getValue("dataSaidaPrevista").substring(0,2));
                    datInicio.setMonth(datInicio.getMonth() - 1);

                    datFim = new Date(DWRUtil.getValue("dataRetornoPrevista").substring(6,10),
                                     DWRUtil.getValue("dataRetornoPrevista").substring(3,5),
                                     DWRUtil.getValue("dataRetornoPrevista").substring(0,2));
                    datFim.setMonth(datFim.getMonth() - 1);
                    
                    datInicioF = new Date(dtini.substring(6,10),
                                        dtini.substring(3,5),
                                        dtini.substring(0,2));
                    datInicioF.setMonth(datInicioF.getMonth() - 1);

                    datFimF = new Date(dtfim.substring(6,10),
                                     dtfim.substring(3,5),
                                     dtfim.substring(0,2));
                    datFimF.setMonth(datFimF.getMonth() - 1);
                   
                    if(datInicio <= datInicioF && datFim >= datInicioF){
                        alert("O Funcion�rio estar� afastado no per�odo informado.");
                        return false;
                    }
                    if(datInicio >= datInicioF && datInicio <= datFimF){
                        alert("O Funcion�rio estar� afastado no per�odo informado.");
                        return false;
                    }
                    if(datInicio <= datInicioF && datFim >= datFimF){
                        alert("O Funcion�rio estar� afastado no per�odo informado.");
                        return false;
                    }
                }
                return true;
            }
        </script>

        <link href="<%=request.getContextPath()%>/css/calendario.css" rel="stylesheet" type="text/css" />
        <script language="JavaScript" src="<%=request.getContextPath()%>/js/util.js"></script>
        <script language="JavaScript" src="<%=request.getContextPath()%>/js/calendar.js"  type="text/javascript" ></script>
        <script language="JavaScript" src="<%=request.getContextPath()%>/js/calendar-pt.js"  type="text/javascript" ></script>
        <script language="JavaScript" src="<%=request.getContextPath()%>/js/calendar-setup.js"  type="text/javascript" ></script>
    </head>
    <body onload="setPercentualDiaria(); atualizarTipoMoedaAgendamento(); carregarDadosEditarAgendamento();">
        <div id="wrap">
            <jsp:include flush="false" page="/inc/header.jsp" />
            <jsp:include flush="false" page="/inc/sidebar.jsp" />
            <div id="content">
                <h2>Editar Agendamento de Viagem</h2>
                <html:form action="/ViagemComAgendamento.do?metodo=alterar" onsubmit="return (verificarDiarias() && verificarPeriodo() && validar(this) && validarIntinerario() && verificaPeriodoFerias('${iniFerias}', '${fimFerias}') );" method="post"  >
                    <html:hidden property="viagem.id" styleId="id_viagem"/>
                    <html:hidden property="viagem.codigoDominioUsuarioViajante" styleId="funcViajId" value="${viajante.codigoDominio}"/>
                    <input type="hidden" id="qDUm" value="${percentualDiaria.quebraDiariasUm}"/>
                    <input type="hidden" id="minQuebraUm" value="${percentualDiaria.minQuebraDiariasUm}"/>
                    <input type="hidden" id="maxQuebraUm" value="${percentualDiaria.maxQuebraDiariasUm}"/>
                    <input type="hidden" id="qDDois" value="${percentualDiaria.quebraDiariasDois}"/>
                    <input type="hidden" id="minQuebraDois" value="${percentualDiaria.minQuebraDiariasDois}"/>
                    <input type="hidden" id="maxQuebraDois" value="${percentualDiaria.maxQuebraDiariasDois}"/>
                    
                    <table border="0" width="100%">

                        <tr bgcolor="#EEEEEE">
                            <td colspan="4"><strong>Viajante</strong></td>
                        </tr>

                        <tr><td colspan="4" height="10"></td></tr>

                        <tr>
                            <td colspan="4">
                                <table>
                                    <tr>
                                        <td width="600" colspan="3"><strong>Nome:</strong> ${viajante.nome}</td>
                                    </tr>
                                    <tr>
                                        <td width="600" colspan="3"><strong>Departamento:</strong> ${viajante.departamento.nome}</td>
                                    </tr>
                                    <tr>
                                        <td width="600" colspan="3"><strong>Cargo:</strong> ${viajante.cargo.nome}</td>
                                    </tr>
                                    <tr>
                                        <td width="300"><strong>Banco:</strong> <html:text property="viagem.banco" style="border-style:none;" readonly="true"/></td>
                                        <td width="150"><strong>Ag�ncia:</strong> <html:text property="viagem.agencia" size="8" style="border-style:none;" readonly="true"/></td>
                                        <td width="150"><strong>Conta:</strong> <html:text property="viagem.conta" size="8" style="border-style:none;" readonly="true"/></td>
                                    </tr>
                                    <tr>
                                        <td width="600" colspan="3">
                                            <strong>Destino:</strong>
                                            <html:text property="destino" styleId="destino" style="border-style:none;" readonly="true"/>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>

                        <tr><td colspan="4" height="10"></td></tr>

                        <tr bgcolor="#EEEEEE">
                            <td colspan="4"><strong>Caracter�sticas da Viagem</strong></td>
                        </tr>

                        <tr><td colspan="4" height="10"></td></tr>

                        <tr>
                            <td colspan="4">
                                <table>
                                    <tr>
                                        <td width="85px">Natureza</td>
                                        <td>
                                            <html:select property="natureza">
                                                <html:option value="">-- Selecione --</html:option>
                                                <html:optionsCollection name="listaNaturezas" value="codigo" label="nome"/>
                                            </html:select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="4">
                                            Descri��o
                                            <br />
                                            <html:textarea property="viagem.descricao" cols="80" rows="2"/>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>

                        <tr><td colspan="4" height="10"></td></tr>

                        <tr bgcolor="#EEEEEE">
                            <td colspan="4"><strong>Programa��o da Viagem</strong></td>
                        </tr>

                        <tr><td colspan="4" height="10"></td></tr>

                        <tr>
                            <td width="185">
                                <fieldset style="width:185px;">
                                    <legend>Sa�da Prevista</legend>
                                    <table border="0">
                                        <tr>
                                            <td>Data</td>
                                            <td>Hora</td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <html:text styleId="dataSaidaPrevista" property="dataSaidaPrevista" styleClass="data" size="8" maxlength="10" onchange="calcularQtdDiariasAgendamento(); setarDataItinerario(); verificaPeriodoFerias('${iniFerias}', '${fimFerias}');" style="float: left;" /><div style="float: left;"><img border="0" title="Selecione uma data" src="<%=request.getContextPath()%>/img/box_calendario.gif" onclick="return showCalendar('dataSaidaPrevista', '%d/%m/%Y', 'calcularQtdDiariasAgendamento(); setarDataItinerario();');"/></div>
                                            </td>
                                            <td>
                                                <html:text property="horaSaidaPrevista" styleId="horaSaidaPrevista" styleClass="hora" size="5" maxlength="5" onchange="calcularQtdDiariasAgendamento();"/>
                                            </td>
                                        </tr>
                                    </table>
                                </fieldset>
                            </td>
                            <td width="185">
                                <fieldset style="width:185px;">
                                    <legend>Retorno Previsto</legend>
                                    <table>
                                        <tr>
                                            <td>Data</td>
                                            <td>Hora</td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <html:text property="dataRetornoPrevista" styleId="dataRetornoPrevista" styleClass="data" size="8" maxlength="10" onchange="calcularQtdDiariasAgendamento(); verificaPeriodoFerias('${iniFerias}', '${fimFerias}');" style="float: left;" /><div style="float: left;"><img border="0" title="Selecione uma data" src="<%=request.getContextPath()%>/img/box_calendario.gif" onclick="return showCalendar('dataRetornoPrevista', '%d/%m/%Y', 'calcularQtdDiariasAgendamento()');"/></div>
                                            </td>
                                            <td>
                                                <html:text property="horaRetornoPrevista" styleId="horaRetornoPrevista" styleClass="hora" size="5" maxlength="5" onchange="calcularQtdDiariasAgendamento();"/>
                                            </td>
                                        </tr>
                                    </table>
                                </fieldset>
                            </td>
                            <td colspan="2">&nbsp;</td>
                        </tr>

                        <tr><td colspan="4" height="10"></td></tr>

                        <tr bgcolor="#EEEEEE">
                            <td colspan="4"><strong>Di�ria</strong></td>
                        </tr>

                        <tr><td colspan="4" height="10"></td></tr>

                        <tr>
                            <td colspan="4">
                                Tipo de Di�ria:
                                <html:select property="tipoDiaria" styleId="tipoDiariaId" onchange="alterarTipoDiariaAgendamento();">
                                    <html:optionsCollection name="listaTiposDiaria" label="nome" value="id"/>
                                </html:select>
                            </td>
                        </tr>

                        <tr>
                            <td colspan="4">
                                <table width="100%">
                                    <tr>
                                        <td>
                                            <div id="valorDiariaLabel1" style="display: block;">
                                                Valor Di�ria <span id="tipoValorDiaria" style="color: #404040"></span>:
                                                <br />
                                                <html:text property="diaria" styleId="valorDiaria" style="text-align:right; border-style: none;" styleClass="real" readonly="true" size="10"/>
                                                <html:hidden property="diariaP" styleId="valorDiariaP" styleClass="real" />
                                            </div>
                                            <div id="valorDiariaLabel2" style="display: none; width: 98px;">
                                                <br />
                                                <html:hidden property="diaria" styleId="valorDiaria" styleClass="real" />
                                                <html:hidden property="diariaP" styleId="valorDiariaP" styleClass="real" />
                                            </div>
                                        </td>
                                        <td>
                                            Qtde. Di�rias:
                                            <br />
                                            <html:text property="qtDiaria" styleId="qtDiaria" style="text-align:right; border-style: none;" styleClass="real" readonly="true" size="10"/>
                                            <input type="hidden" id="qtDiariaAux" class="real">
                                        </td>
                                        <td>
                                            Total em Di�rias <span id="tipoTotalDiarias" style="color: #404040"></span>:
                                            <br />
                                            <!--input name="totalDiarias" id="totalDiarias" style="text-align:right; border-style: none;" class="real" readonly size="10"-->
                                            <html:text property="totalDiarias" styleId="totalDiarias" style="text-align:right; border-style: none;" styleClass="real" readonly="true" size="10"/>
                                        </td>
                                        <td>
                                            Valor Adiant. <span id="tipoValorAdiantamento" style="color: #404040"></span>:
                                            <br />
                                            <html:text property="valorAdiantamento" styleId="valorAdiantamento" style="text-align:right;" size="10" styleClass="real" onkeyup="setTotalAdiantamento()" />
                                        </td>
                                        <td>
                                            Total <span id="tipoTotalAdiantamento" style="color: #404040"></span> (Di�rias + Adiant.):
                                            <br />
                                            <html:text property="totalAdiantamento" styleId="totalAdiantamento" style="text-align:right; border-style: none;" styleClass="real" readonly="true" size="10" />
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>

                        <tr>
                            <td colspan="4">
                                <div id="tipoDet" style="display: none;">
                                    <fieldset style="width: 940px;">
                                        <legend style="color:#FF0000;">Viagem considerando os valores e per�odos abaixo</legend>
                                        <table widt="900" border="0">
                                            <tr>
                                                <td align="center" width="110">Data Inicio</td>
                                                <td align="center" width="60">Hora</td>
                                                <td align="center" width="105">Data Fim</td>
                                                <td align="center" width="75">Hora</td>
                                                <td align="center" width="68">Qtde. Di�rias</td>
                                                <td align="center" width="57">Valor Di�ria</td>
                                                <td align="center" width="65">Total em Di�rias</td>
                                                <td align="center" width="226">Observa��es</td>
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
                                                <td width="50"><a href="javascript: addTrecho();">Adicionar</a></td>
                                            </tr>
                                        </table>
                                    </fieldset>
                                </div>
                            </td>
                        </tr>

                        <tr><td colspan="4" height="10"></td></tr>

                        <tr bgcolor="#EEEEEE">
                            <td colspan="4"><strong>Itiner�rio</strong></td>
                        </tr>

                        <tr><td colspan="4" height="10"></td></tr>

                        <tr>
                            <td colspan="4">
                                <table>
                                    <tr>
                                        <td width="110">Data</td>
                                        <td width="220">Cidade Origem</td>
                                        <td width="220">Cidade Destino</td>
                                        <td width="200">Observa��o</td>
                                    </tr>
                                </table>
                                <table id="tabelaItinerario">
                                    <thead id="itinerario"></thead>
                                    <tr>
                                        <td width="110"><input id="dataLinhaItinerario" styleClass="data" size="8" maxlength="10" style="float: left;" ><div style="float: left;"><img border="0" title="Selecione uma data" src="<%=request.getContextPath()%>/img/box_calendario.gif" onclick="return showCalendar('dataLinhaItinerario', '%d/%m/%Y');"/></div></td>
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
                                Observa��es
                                <br />
                                <html:textarea property="observacao" cols="80" rows="2"/>
                            </td>
                        </tr>

                        <tr>
                            <td colspan="4">
                                <acesso:input tipo="salvar" action="ViagemComAgendamento.do" metodo="salvar" chave="agendViagSalvarFuncionario" styleClass="botao" />
                                &nbsp;
                                <acesso:input tipo="select" action="ViagemComAgendamento.do" metodo="cancelar" chave="*" styleClass="botao" onclick="document.location.href='ViagemComAgendamento.do'" />
                            </td>
                        </tr>
                    </table>
                </html:form>
                <jsp:include flush="false" page="/inc/footer.jsp" />
            </div>

        </div>
    </body>
</html:html>
