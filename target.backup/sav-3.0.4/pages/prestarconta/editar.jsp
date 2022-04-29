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
        <script src="<%=request.getContextPath()%>/js/prestacao_contas.js?n=1" charset="utf-8"></script>
        <script src="<%=request.getContextPath()%>/js/jquery-1.4.2.min.js"></script>
        <script src="<%=request.getContextPath()%>/js/jquery.maskedinput.js"></script>
        <script src="<%=request.getContextPath()%>/js/jquery.maskMoney.js"></script>

        <html:javascript formName="prestacaoContasForm" method="validar"/>

        <title><fmt:message key="aplicacao.nome" /></title>

        <script>

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
                    adicionarLinhaItinerarioPrestacao();
            </logic:iterate>
                }

                function setGastos(){
            <logic:iterate name="listaGastos" id="gasto">
                    document.getElementById("gastoId").value = ${gasto.tipoGasto.id}
                    document.getElementById("valorGasto").value = float2moeda(${gasto.valor});
                    adicionarLinhaGasto();
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
                    adicionarLinhaTrechoPrestacaoInicial();
            </logic:iterate>
                }
                    
                function enviarForm(){
                    var dataRetorno = document.getElementById("dataRetornoEfetiva").value;
                    var horaRetorno = document.getElementById("horaRetornoEfetiva").value;
                    var retorno = true;
                    Funcoes.verificarDataAtualIgualOuSuperiorDataRetorno(dataRetorno, horaRetorno, 
                    {callback:function(permiteAdicionar) {
                            retorno = permiteAdicionar;
                        }, async:false});
                    if (!retorno) {
                        alert("Não é possível salvar uma prestação de contas antes da data de retorno da viagem.");
                       retorno = false;
                    }
                    return retorno;
                }
        </script>

        <script language="JavaScript" src="<%=request.getContextPath()%>/js/util.js"></script>
        <link href="<%=request.getContextPath()%>/css/calendario.css" rel="stylesheet" type="text/css" />
        <script language="JavaScript" src="<%=request.getContextPath()%>/js/calendar.js"  type="text/javascript" ></script>
        <script language="JavaScript" src="<%=request.getContextPath()%>/js/calendar-pt.js"  type="text/javascript" ></script>
        <script language="JavaScript" src="<%=request.getContextPath()%>/js/calendar-setup.js"  type="text/javascript" ></script>

    </head>
    <body onload="setPercentualDiaria(); carregarDadosPrestacaoEfetivo();">
        <div id="wrap">
            <jsp:include flush="false" page="/inc/header.jsp" />
            <jsp:include flush="false" page="/inc/sidebar.jsp" />
            <div id="content">
                <h2>Editar Prestação de Conta</h2>
                <html:form action="/PrestacaoContas.do?metodo=alterar" onsubmit="return (validar(this) && validarTrecho() && validarIntinerarioPrestacao() && enviarForm())" method="post">
                    <html:hidden property="viagem.id"/>
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
                                ${viagensDTO.nomeViajante}
                            </td>
                        </tr>

                        <tr><td colspan="4" height="10"></td></tr>

                        <tr bgcolor="#EEEEEE">
                            <td colspan="4"><strong>Características da Viagem</strong></td>
                        </tr>

                        <tr><td colspan="4" height="10"></td></tr>

                        <tr>
                            <td width="200">
                                <fieldset style="width:200px;">
                                    <legend>Saída Efetiva</legend>
                                    <table border="0">
                                        <tr>
                                            <td>Data</td>
                                            <td>Hora</td>
                                        </tr>
                                        <tr>
                                            <td width="110">
                                                <html:text styleId="dataSaidaEfetiva" property="dataSaidaEfetiva" size="8" maxlength="10" styleClass="data" onchange="calcularQtdDiariasPrestacao(); atualizarTotalGastos();" style="float: left;" /><div style="float: left;"><img border="0" alt="Selecione uma data" src="<%=request.getContextPath()%>/img/box_calendario.gif" onclick="return showCalendar('dataSaidaEfetiva', '%d/%m/%Y', 'calcularQtdDiariasPrestacao(); atualizarTotalGastos();');"/></div>
                                                    <html:hidden styleId="dataSaidaPrevista" property="dataSaidaPrevista"/>
                                            </td>
                                            <td width="60">
                                                <html:text property="horaSaidaEfetiva" styleId="horaSaidaEfetiva" size="5" maxlength="5" styleClass="hora" onchange="calcularQtdDiariasPrestacao(); atualizarTotalGastos();"/>
                                                <html:hidden styleId="horaSaidaPrevista" property="horaSaidaPrevista"/>
                                            </td>
                                        </tr>
                                    </table>
                                </fieldset>
                            </td>
                            <td width="200">
                                <fieldset style="width:200px;">
                                    <legend>Retorno Efetivo</legend>
                                    <table border="0">
                                        <tr>
                                            <td>Data</td>
                                            <td>Hora</td>
                                        </tr>
                                        <tr>
                                            <td width="110">
                                                <html:text property="dataRetornoEfetiva" styleId="dataRetornoEfetiva" size="8" maxlength="10" styleClass="data" onchange="calcularQtdDiariasPrestacao(); atualizarTotalGastos();" style="float: left;" /><div style="float: left;"><img border="0" alt="Selecione uma data" src="<%=request.getContextPath()%>/img/box_calendario.gif" onclick="return showCalendar('dataRetornoEfetiva', '%d/%m/%Y', 'calcularQtdDiariasPrestacao(); atualizarTotalGastos();');"/></div>
                                                    <html:hidden styleId="dataRetornoPrevista" property="dataRetornoPrevista"/>
                                            </td>
                                            <td width="60">
                                                <html:text property="horaRetornoEfetiva" styleId="horaRetornoEfetiva" size="5" maxlength="5" styleClass="hora" onchange="calcularQtdDiariasPrestacao(); atualizarTotalGastos();"/>
                                                <html:hidden styleId="horaRetornoPrevista" property="horaRetornoPrevista"/>
                                            </td>
                                        </tr>
                                    </table>
                                </fieldset>
                            </td>
                        </tr>

                        <tr><td colspan="4" height="10"></td></tr>

                        <tr>
                            <td>
                                Destino: <html:text property="destino" styleId="destino" style="border-style:none;" readonly="true" size="15" />
                            </td>
                            <td colspan="3">
                                Tipo de Diária: 
                                <html:select property="tipoDiaria" styleId="tipoDiariaId" onchange="alterarTipoDiariaPrestacao();" value="${tipoDiaria}">
                                    <html:optionsCollection name="listaTiposDiaria" label="nome" value="id"/>
                                </html:select>
                                <input type="hidden" id="tipoDiariaNumero" value="${tipoDiaria}" />
                                
                                <!--html:text property="tipoDiaria" styleId="tipoDiariaId" style="border-style:none;" readonly="true" value="${viagensDTO.viagem.prestacaoContas.tipoDiaria}" size="15"/>
                                <input type="hidden" id="tipoDiariaNumero" value="${tipoDiaria}" /-->
                            </td>
                        </tr>

                        <tr><td colspan="4" height="10"></td></tr>

                        <tr>
                            <td id="valorDiariaLabel1" style="display: block;">
                                Valor Diária (R$):
                                <br />

                                <c:if test="${tipoDiaria != 1}">
                                    <span id="tituloFCM" style="color:#DDDDDD;">Informe a Diária Especial <span id="tipoDiariaE" style="color: #DDDDDD;"></span>:</span>
                                    <br />
                                    <html:text property="diaria" styleId="valorDiaria" size="10" style="text-align:right;" styleClass="real" onkeyup="atualizarDiaria()" readonly="true" />
                                    <br />
                                    <span id="tituloDesc" style="color:#DDDDDD;">Informe a Descrição:</span>
                                    <br />
                                    <html:text property="descricaoE" styleId="descricaoE" size="40" disabled="true" value="" />
                                </c:if>
                                <c:if test="${tipoDiaria == 1}">
                                    <span id="tituloFCM" style="color:#FF0000;">Informe a Diária Especial <span id="tipoDiariaE" style="color: #FF0000;"></span>:</span>
                                    <br />
                                    <html:text property="diaria" styleId="valorDiaria" size="10" style="text-align:right;" styleClass="real" onkeyup="atualizarDiaria()" readonly="false" />
                                    <br />
                                    <span id="tituloDesc" style="color:#FF0000;">Informe a Descrição:</span>
                                    <br />
                                    <html:text property="descricaoE" styleId="descricaoE" size="40" disabled="false" />
                                </c:if>
                                <html:hidden property="diariaP" styleId="diariaP" styleClass="real" />
                                <html:hidden property="totalDiariaOriginal" styleId="totalDiariaOriginal" styleClass="real" />
                                <html:hidden property="descricaoE" styleId="descricaoE" />
                                <html:hidden property="descricaoEOriginal" styleId="descricaoEOriginal" />
                            </td>
                            <td id="valorDiariaLabel2" style="display: none;">
                                <br />
                                <html:hidden property="diaria" styleClass="real" />
                                <html:hidden property="diariaP" styleId="diariaP" styleClass="real" />
                            </td>
                            <td>
                                Qtde. Diárias:
                                <br />
                                <html:text property="qtDiaria" styleId="qtDiaria" style="text-align:right; border-style: none;" styleClass="real" readonly="true" size="10"/>
                            </td>
                            <td>
                                Valor Adiantamento (R$):
                                <br />
                                <!-- Valores concedidos -->
                                <html:text property="valorAdiantamento" styleId="valorAdiantamento" styleClass="real" style="text-align:right; border-style:none;" readonly="true" size="10" />
                            </td>
                            <td>
                                Total (R$) (Diárias + Adiant.):
                                <br />
                                <html:hidden property="totalDiarias" styleId="totalDiarias" styleClass="real"/>
                                <html:hidden property="totalAdiantamento" styleId="totalAdiantamento" styleClass="real" />

                                <!-- Valores concedidos -->
                                <input type="hidden" id="qtDiariaConc" class="real"/>
                                <input type="hidden" id="totalDiariasConc" class="real"/>
                                <input type="text" name="totalAdiantamentoConc" id="totalAdiantamentoConc" class="real" style="text-align:right; border-style:none;" readonly="true" size="10" />
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
                                                    <input type="text" name="dataInicio" id="dataInicio" class="data" size="8" maxlength="10" onchange="calcularQtdDiariasTrechoPrestacao();" style="float: left;"/><div style="float: left;"><img border="0" alt="Selecione uma data" src="<%=request.getContextPath()%>/img/box_calendario.gif" onclick="return showCalendar('dataInicio', '%d/%m/%Y', 'calcularQtdDiariasTrechoPrestacao()')"/></div>
                                                </td>
                                                <td width="60">
                                                    <input type="text" name="horaInicio" id="horaInicio" class="hora" size="5" maxlength="5" onchange="calcularQtdDiariasTrechoPrestacao();"/>
                                                </td>
                                                <td width="105">
                                                    <input type="text" name="dataFim" id="dataFim" class="data" size="8" maxlength="10" onchange="calcularQtdDiariasTrechoPrestacao();" style="float: left;"/><div style="float: left;"><img border="0" alt="Selecione uma data" src="<%=request.getContextPath()%>/img/box_calendario.gif" onclick="return showCalendar('dataFim', '%d/%m/%Y', 'calcularQtdDiariasTrechoPrestacao()')"/></div>
                                                </td>
                                                <td width="65">
                                                    <input type="text" name="horaFim" id="horaFim" class="hora" size="5" maxlength="5" onchange="calcularQtdDiariasTrechoPrestacao();"/>
                                                </td>
                                                <td width="50">
                                                    <input name="qtDiariaTrecho" id="qtDiariaTrecho" style="text-align:right; border-style:none;" class="real" readonly size="6">
                                                </td>
                                                <td width="50">
                                                    <input name="valorDiariaTrecho" id="valorDiariaTrecho" style="text-align:right;" class="real" size="6" onkeyup="setTotalDiariasTrecho()">
                                                </td>
                                                <td width="50">
                                                    <input name="totalDiariasTrecho" id="totalDiariasTrecho" style="text-align:right; border-style:none;" class="real" readonly size="6">
                                                </td>
                                                <td width="50">
                                                    <input name="descricaoTrecho" id="descricaoTrecho" size="26" maxlength="255">
                                                </td>
                                                <td width="50"><a href="javascript: adicionarLinhaTrechoPrestacao();">Adicionar</a></td>
                                            </tr>
                                        </table>
                                    </fieldset>
                                </div>
                            </td>
                        </tr>

                        <tr><td colspan="4" height="10"></td></tr>

                        <tr bgcolor="#EEEEEE">
                            <td colspan="4"><strong>Itinerário Efetivo</strong></td>
                        </tr>

                        <tr><td colspan="4" height="10"></td></tr>

                        <tr>
                            <td colspan="4">
                                <table border="0">
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
                                        <td width="110"><input id="dataLinhaItinerario" size="8" maxlength="10" style="float: left;"><div style="float: left;"><img border="0" alt="Selecione uma data" src="<%=request.getContextPath()%>/img/box_calendario.gif" onclick="return showCalendar('dataLinhaItinerario', '%d/%m/%Y');"/></div></td>
                                        <td id="cidOri" width="220">
                                            <select name="cidadeOrigemId" id="cidadeOrigemId" style="width: 210px;">
                                                <option value="">-- Selecione --</option>
                                            </select>
                                        </td>
                                        <td id="cidDes" width="220">
                                            <select name="cidadeDestinoId" id="cidadeDestinoId" style="width: 210px;">
                                                <option value="">-- Selecione --</option>
                                            </select>
                                        </td>
                                        <td width="200"><input id="obsLinhaItinerario" size="22" maxlength="255"></td>
                                        <td><a href="javascript: adicionarLinhaItinerarioPrestacao();">Adicionar</a></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>

                        <tr>
                            <td colspan="4">
                                Observações
                                <br />
                                <html:textarea property="observacao" cols="80" rows="2"/>
                            </td>
                        </tr>

                        <tr><td colspan="4" height="10"></td></tr>

                        <tr bgcolor="#EEEEEE">
                            <td colspan="4"><strong>Resumo</strong></td>
                        </tr>

                        <tr><td colspan="4" height="10"></td></tr>

                        <tr>
                            <td colspan="2">
                                <fieldset style="float:left; height:auto; width: 300px;">
                                    <legend>Despesas com Adiantamento (R$)</legend>

                                    <table width="100%">
                                        <tr>
                                            <td width="50%">Descrição</td>
                                            <td width="50%">Valor</td>
                                        </tr>
                                    </table>
                                    <table id="tabelaGasto">
                                        <thead id="gasto"></thead>
                                        <tr>
                                            <td id="descricaoGasto">
                                                <select name="gastoId" id="gastoId" >
                                                    <option value="">-- Selecione --</option>
                                                    <logic:iterate name="listaTipoGastos" id="obj">
                                                        <option value="${obj.id}">${obj.descricao}</option>
                                                    </logic:iterate>
                                                </select>
                                            </td>
                                            <td><input name="valorGasto" id="valorGasto" size="15" class="real"></td>
                                            <td><a href="javascript: adicionarLinhaGasto();">Adicionar</a></td>
                                        </tr>
                                    </table>
                                    <table border="0">
                                        <tr>
                                            <td>Qtde. Doc</td>
                                            <td>
                                                <html:text property="qtdeDoc" styleId="qtdeDoc" style="text-align:right; float:right;" size="4" />
                                            </td>
                                            <td><strong>Total</strong></td>
                                            <td>
                                                <html:text property="totalR" styleId="totalR" style="text-align:right; float:right;" readonly="true" value="0,00" size="8" />
                                            </td>
                                        </tr>
                                    </table>
                                </fieldset>
                            </td>
                            <td colspan="2">
                                <fieldset style="float:left; height:145px; margin-left:10px; width: 400px;">
                                    <legend>Resumo Viagem (R$)</legend>
                                    <table border="0">
                                        <tr>
                                            <td style="text-align:center;"></td>
                                            <td style="text-align:center;">Concedido</td>
                                            <td style="text-align:center;">Efetivo</td>
                                            <td style="text-align:center;">Saldo</td>
                                        </tr>
                                        <tr>
                                            <td>Diárias</td>
                                            <td>
                                                <html:text property="vd1" styleId="vd1" style="text-align:right; float:right;" readonly="true" value="0,00" size="8" />
                                            </td>
                                            <td>
                                                <html:text property="vd2" styleId="vd2" style="text-align:right; float:right;" readonly="true" value="0,00" size="8" />
                                            </td>
                                            <td>
                                                <html:text property="vd3" styleId="vd3" style="text-align:right; float:right;" readonly="true" value="0,00" size="8" />
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>Adiantamento</td>
                                            <td>
                                                <html:text property="va1" styleId="va1" style="text-align:right; float:right;" readonly="true" value="0,00" size="8" />
                                            </td>
                                            <td>
                                                <html:text property="va2" styleId="va2" style="text-align:right; float:right;" readonly="true" value="0,00" size="8" />
                                            </td>
                                            <td>
                                                <html:text property="va3" styleId="va3" style="text-align:right; float:right;" readonly="true" value="0,00" size="8" />
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>Total</td>
                                            <td>
                                                <html:text property="tot1" styleId="tot1" style="text-align:right; float:right;" readonly="true" value="0,00" size="8" />
                                            </td>
                                            <td>
                                                <html:text property="tot2" styleId="tot2" style="text-align:right; float:right;" readonly="true" value="0,00" size="8" />
                                            </td>
                                            <td>
                                                <html:text property="tot3" styleId="tot3" style="text-align:right; float:right;" readonly="true" value="0,00" size="8" />
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="3" id="visor" style="text-align: right;"></td>
                                            <td><html:text property="saldoTotal" styleId="saldoTotal" style="text-align:right; float:right;" readonly="true" value="0,00" size="8" /></td>
                                        </tr>
                                    </table>
                                </fieldset>
                            </td>
                        </tr>

                        <tr><td colspan="4" height="10"></td></tr>

                        <tr>
                            <td colspan="3">
                                <acesso:input tipo="salvar" action="PrestacaoContas.do" metodo="salvar" chave="solicSalvarPrestacaoViagem" styleClass="botao" />
                                &nbsp;
                                <acesso:input tipo="select" action="PrestacaoContas.do" metodo="cancelar" chave="*" styleClass="botao" onclick="document.location.href='Viagem.do'" />
                            </td>
                            <td>
                                <c:if test="${statusPrestacao eq 'PRESTACAO_INFORMADA'}">
                                    <input value="Excluir Prest. contas" style="width:160px; float:right; text-align: center;" class="botao" onclick="if(confirm('Deseja realmente excluir esta Prestação de Contas?')){document.location.href='PrestacaoContas.do?metodo=excluir&id=${viagensDTO.viagem.id}';} return false;" />
                                </c:if>
                            </td>
                        </tr>
                    </table>
                </html:form>
                <jsp:include flush="false" page="/inc/footer.jsp" />
            </div>

        </div>
    </body>
</html:html>