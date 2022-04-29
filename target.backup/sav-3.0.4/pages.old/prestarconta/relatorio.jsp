<%@page contentType="text/html" errorPage="/erro.jsp"%>
<%@page pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://flem.org.br/acesso-tag" prefix="acesso"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@taglib uri="http://flem.org.br/flem-struts-tag" prefix="flemst" %>
<html:html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
        <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">

        <script src="<%=request.getContextPath()%>/js/functions.js"></script>
        <script src="<%=request.getContextPath()%>/js/solicitacao_viagem.js" charset="utf-8"></script>
        <script src="<%=request.getContextPath()%>/js/jquery-1.4.2.min.js"></script>
        <script src="<%=request.getContextPath()%>/js/jquery.maskedinput.js"></script>
        <script src="<%=request.getContextPath()%>/js/jquery.maskMoney.js"></script>

        <title>Prestação de Contas</title>

        <script language="JavaScript">
            jQuery.noConflict();
            jQuery(document).ready(function(){
                jQuery("input[class*=data]").mask("99/99/9999", {placeholder: " "});
                jQuery("input[class*=hora]").mask("99:99", {placeholder: " "});
                jQuery("input[class*=real]").maskMoney({symbol: "", decimal: ",", thousands: "."});

                jQuery("input[class*=data]").change(function() { if (this.value.indexOf(" ") == -1) ValidarDATA(this, '/'); else this.value = ""; });
                jQuery("input[class*=hora]").change(function() { if (this.value.indexOf(" ") == -1) ValidarHORA(this, ':'); else this.value = ""; });
            });
        </script>

        <script language="JavaScript" src="js/util.js"></script>
        <link href="<%=request.getContextPath()%>/css/relatorio.css" rel="stylesheet" type="text/css" />
    </head>
    <body>
        <div class="relatorio">

            <!--
            INÍCIO DO CABEÇALHO
            -->
            <table id="grupo" border="0" cellpadding="0" cellspacing="0" width="800">
                <tr>
                    <td rowspan="4" width="220" align="center" id="cabecalho02">Fundação Luís Eduardo Magalhães</td>
                    <td rowspan="4" width="260" align="center" id="cabecalho01">PRESTAÇÃO DE CONTAS</td>
                    <td width="320" id="campo">Solicitação</td>
                </tr>
                <tr>
                    <!--td align="center" id="var">&nbsp;${relatorio.viagem.id}</td-->
                    <td align="center" id="var">${relatorio.viagem.id}</td>
                </tr>
                <tr>
                    <td id="campo">Centro de Custo/Origem Recurso</td>
                </tr>
                <tr>
                    <td id="var" align="center">&nbsp;${relatorio.centroCusto}</td>
                </tr>
            </table>
            <!--
            FIM DO CABEÇALHO
            -->

            <!--
            INÍCIO DO SOLICITANTE
            -->
            <table id="grupo" border="0" cellpadding="0" cellspacing="0" width="800">
                <tr>
                    <td align="center" colspan="6" id="titulo">SOLICITANTE</td>
                </tr>
                <tr>
                    <td align="left" id="campo" colspan="2">Nome</td>
                    <td align="left" id="campo">CPF</td>
                </tr>
                <tr>
                    <td align="center" id="var" colspan="2" width="600">&nbsp;${relatorio.nomeViajante}</td>
                    <td align="center" id="var" width="200">&nbsp;${relatorio.cpfViajante}</td>
                </tr>
                <tr>
                    <td align="left" id="campo">Departamento</td>
                    <td align="left" id="campo">Cargo</td>
                    <td align="left" id="campo">Telefone</td>
                </tr>
                <tr>
                    <td align="center" id="var" width="300">&nbsp;${relatorio.departamentoViajante}</td>
                    <td align="center" id="var" width="300">&nbsp;${relatorio.cargoViajante}</td>
                    <td align="center" id="var" width="200">&nbsp;${relatorio.telViajante}</td>
                </tr>
            </table>
            <!--
            FIM DO SOLICITANTE
            -->

            <!--
            INÍCIO DO CARACTERÍSTICAS DA VIAGEM
            -->
            <table id="grupo" border="0" cellpadding="0" cellspacing="0" width="800">
                <tr>
                    <td align="center" colspan="6" id="titulo">CARACTERÍSTICAS DA VIAGEM</td>
                </tr>
                <tr>
                    <td align="center" id="campo" colspan="2">Saída Efetiva</td>
                    <td align="center" id="campo" colspan="2">Retorno Efetivo</td>
                    <td align="center" id="campo">Qtd. Diárias</td>
                    <td align="center" id="campo">Valor Diária ${tipoMoeda}</td>
                </tr>
                <tr>
                    <td align="left" id="campo" width="135">Data</td>
                    <td align="left" id="campo" width="135">Horário</td>
                    <td align="left" id="campo" width="135">Data</td>
                    <td align="left" id="campo" width="135">Horário</td>
                    <td align="center" id="var" rowspan="2" width="135">&nbsp;${relatorio.qtdDiariasEfetivo}</td>
                    <td align="center" id="var" rowspan="2" width="135">
                        <input type="text" class="real" id="input" value="<fmt:formatNumber minFractionDigits='2' maxFractionDigits='2' value='${relatorio.diariaPadrao}'/>" style="width: 50px;">
                        <%--c:if test="${(sizeTrecho > 0 && (relatorio.tipoDiaria == 2 || relatorio.tipoDiaria == 3)) || (temPeriodo == true && relatorio.tipoDiaria !=2 && relatorio.tipoDiaria !=3)}"--%>
                        <c:if test="${(sizeTrecho > 0 && (relatorio.tipoDiaria == 2 || relatorio.tipoDiaria == 3)) || (temPeriodo == true && relatorio.tipoDiaria == 1)}">
                            **
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td align="center" id="var">&nbsp;${relatorio.dataSaidaPrevista}</td>
                    <td align="center" id="var">&nbsp;${relatorio.horaSaidaPrevista}</td>
                    <td align="center" id="var">&nbsp;${relatorio.dataRetornoPrevista}</td>
                    <td align="center" id="var">&nbsp;${relatorio.horaRetornoPrevista}</td>
                </tr>
                <tr>
                    <td align="left" colspan="6" id="campo">Descrição do Serviço/Treinamento</td>
                </tr>
                <tr>
                    <td align="left" colspan="6" id="var">&nbsp;${relatorio.descricaoServico}</td>
                </tr>
            </table>
            <!--
            FIM DO CARACTERÍSTICAS DA VIAGEM
            -->

            <!--
            INÍCIO DO Viagem considerando os valores e períodos abaixo
            -->
            <%--c:if test="${(sizeTrecho > 0 && (relatorio.tipoDiaria == 2 || relatorio.tipoDiaria == 3)) || (temPeriodo == true && relatorio.tipoDiaria !=2 && relatorio.tipoDiaria !=3)}"--%>
            <c:if test="${(sizeTrecho > 0 && (relatorio.tipoDiaria == 2 || relatorio.tipoDiaria == 3)) || (temPeriodo == true && relatorio.tipoDiaria == 1)}">
                <table id="grupo" border="0" cellpadding="0" cellspacing="0" width="800">
                    <tr>
                        <td align="center" colspan="5" id="titulo">**Viagem considerando os valores e períodos abaixo</td>
                    </tr>
                    <c:if test="${relatorio.tipoDiaria == '0' || relatorio.tipoDiaria == '1'}">
                        <tr>
                            <td align="center" id="campo2" width="200">Período</td>
                            <td align="center" id="campo2" width="100">Qtd. Diárias</td>
                            <td align="center" id="campo2" width="100">Valor Diária ${tipoMoeda}</td>
                            <td align="center" id="campo2" width="100">Total Diárias ${tipoMoeda}</td>
                            <td align="center" id="campo2" width="300">Observações</td>
                        </tr>
                        <tr>
                            <td align="center" id="var2" width="200">&nbsp;${relatorio.dataEfetivo}</td>
                            <td align="center" id="var2" width="100"><input type="text" class="real" value="${relatorio.qtdDiariasEfetivo}"></td>
                            <td align="center" id="var2" width="100"><input type="text" class="real" value="${relatorio.diariaEspecial}"></td>
                            <td align="center" id="var2" width="100"><input type="text" class="real" value="<fmt:formatNumber minFractionDigits='2' maxFractionDigits='2' value='${relatorio.totalDiariaEfetivo}'/>"></td>
                            <td align="center" id="var2" width="300">&nbsp;${relatorio.obsDiariaEspecial}</td>
                        </tr>
                    </c:if>
                    <c:if test="${relatorio.tipoDiaria == '2'}">
                        <tr>
                            <td align="center" id="campo2" width="230">Período</td>
                            <td align="center" id="campo2" width="80">Qtd. Diárias</td>
                            <td align="center" id="campo2" width="80">Valor Diária ${tipoMoeda}</td>
                            <td align="center" id="campo2" width="80">Total Diárias ${tipoMoeda}</td>
                            <td align="center" id="campo2" width="300">Observações</td>
                        </tr>

                            <logic:iterate name="itensTrecho" id="itensT">
                                <tr>
                                    <td align="center" id="var2" width="230">
                                        &nbsp;
                                        <fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${itensT.dataInicio}"/>
                                        a
                                        <fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${itensT.dataFim}"/>
                                    </td>
                                    <td align="center" id="var2" width="80"><input type="text" class="real" value="${itensT.qtDiaria}" /></td>
                                    <td align="center" id="var2" width="80"><input type="text" class="real" value="${itensT.diaria}" /></td>
                                    <td align="center" id="var2" width="80"><input type="text" class="real" value="<fmt:formatNumber minFractionDigits='2' maxFractionDigits='2' value='${itensT.qtDiaria * itensT.diaria}'/>" /></td>
                                    <td align="center" id="var2" width="300">&nbsp;${itensT.observacao}</td>
                                </tr>
                            </logic:iterate>

                    </c:if>
                    <c:if test="${relatorio.tipoDiaria == '3'}">
                        <tr>
                            <td align="center" id="campo2" width="250">Período</td>
                            <td align="center" id="campo2" width="100">Valor Diária ${tipoMoeda}</td>
                            <td align="center" id="campo2" width="450">Observações</td>
                        </tr>
                        <logic:iterate name="itensTrecho" id="itensT">
                            <tr>
                                <td align="center" id="var2" width="250">
                                    &nbsp;
                                    <fmt:formatDate pattern="dd/MM/yyyy" value="${itensT.dataInicio}"/>
                                    a
                                    <fmt:formatDate pattern="dd/MM/yyyy" value="${itensT.dataFim}"/>
                                </td>
                                <td align="center" id="var2" width="100"><input type="text" class="real" value="<fmt:formatNumber minFractionDigits='2' maxFractionDigits='2' value='${itensT.diaria}'/>" /></td>
                                <td align="center" id="var2" width="450">&nbsp;${itensT.observacao}</td>
                            </tr>
                        </logic:iterate>
                    </c:if>
                </table>
            </c:if>
            <!--
            FIM DO Viagem considerando os valores e períodos abaixo
            -->

            <!--
            INÍCIO DO ITINERÁRIO
            -->
            <table id="grupo" border="0" cellpadding="0" cellspacing="0" width="800">
                <tr>
                    <td align="center" colspan="5" id="titulo">ITINERÁRIO</td>
                </tr>
                <tr>
                    <td align="center" id="campo2" width="100">Data</td>
                    <td align="center" id="campo2" width="200">Origem</td>
                    <td align="center" id="campo2" width="200">Destino</td>
                    <td align="center" id="campo2" width="300">Observações</td>
                </tr>
                <script>
                    <logic:iterate name="itensItinerario" id="itens">
                        dta = "${itens.data}";
                        dta = dta.substr(8,2)+"/"+dta.substr(5,2)+"/"+dta.substr(0,4);

                        document.writeln("<tr>");
                        document.writeln("<td align='center' id='var2' width='100'>&nbsp;"+dta+"</td>");
                        document.writeln("<td align='center' id='var2' width='200'>&nbsp;${itens.origem}</td>");
                        document.writeln("<td align='center' id='var2' width='200'>&nbsp;${itens.destino}</td>");
                        document.writeln("<td align='center' id='var2' width='300'>&nbsp;${itens.observacoes}</td>");
                        document.writeln("</tr>");
                    </logic:iterate>
                </script>
            </table>
            <!--
            FIM DO ITINERÁRIO
            -->

            <!--
            INÍCIO DO OBSERVAÇÕES
            -->
            <table id="grupo" border="0" cellpadding="0" cellspacing="0" width="800">
                <tr>
                    <td align="center" id="titulo">OBSERVAÇÕES</td>
                </tr>
                <tr>
                    <td align="left" id="var2">
                        &nbsp;${relatorio.viagem.prestacaoContas.observacao}
                        <%--c:if test="${relatorio.tipoDiaria == '3'}">
                            ${relatorio.viagem.prestacaoContas.observacao}
                        </c:if>
                        <c:if test="${relatorio.tipoDiaria == '0' || relatorio.tipoDiaria == '1' || relatorio.tipoDiaria == '2'}">
                            ${relatorio.viagem.prestacaoContas.observacao}
                        </c:if--%>
                    </td>
                </tr>
            </table>
            <!--
            FIM DO OBSERVAÇÕES
            -->

            <!--
            INÍCIO DO Despesas com Adiantamento (R$ ou US$)
            -->
            <div style="float: left; width: 340px;">
                <table id="grupo" border="0" cellpadding="0" cellspacing="0" width="340">
                    <tr>
                        <td align="center" id="titulo" colspan="4">Despesas com Adiantamento (${tipoMoeda})</td>
                    </tr>
                    <script>
                        <logic:iterate name="listaGastos" id="gastos">
                            document.writeln("<tr>");
                            document.writeln("<td align='left' id='var2' width='180' colspan='2'>&nbsp;${gastos.tipoGasto.descricao}</td>");
                            document.writeln("<td align='right' id='var2' width='180' colspan='2'><input type='text' style='text-align:right; margin-right:8px;' class='real' value='"+${gastos.valor}+"' /></td>");
                            document.writeln("</tr>");
                        </logic:iterate>
                    </script>
                    <tr>
                        <td align="left" id="var2" width="85">&nbsp;Qtde. Doc</td>
                        <td align="center" id="var2" width="85">&nbsp;${relatorio.qtdDoc}</td>
                        <td align="left" id="var2" width="85">&nbsp;TOTAL</td>
                        <td align="left" id="var2" width="85"><input type="text" class="real" style="text-align:right; margin-right:8px; width: 85px;" value="<fmt:formatNumber minFractionDigits='2' maxFractionDigits='2' value='${totalGasto}'/>" /></td>
                    </tr>
                </table>
            </div>
            <!--
            FIM DO Despesas com Adiantamento (R$)
            -->

            <!--
            INÍCIO DO Resumo Viagem (R$)
            -->
            <div style="float: left; width: 440px; margin-left: 20px;">
                <table id="grupo" border="0" cellpadding="0" cellspacing="0" width="440">
                    <tr>
                        <td align="center" id="titulo" width="110">Itens</td>
                        <td align="center" id="titulo" width="110">Concedido</td>
                        <td align="center" id="titulo" width="110">Efetivo</td>
                        <td align="center" id="titulo" width="110">Saldo</td>
                    </tr>
                    <tr>
                        <td align="left" id="campo" width="110">Valor das Diárias</td>
                        <td align="left" id="var2" width="110"><input type="text" style="text-align:right;" value="<fmt:formatNumber minFractionDigits='2' maxFractionDigits='2' value='${relatorio.totalDiariaPadrao}'/>" /></td>
                        <td align="left" id="var2" width="110"><input type="text" style="text-align:right;" value="<fmt:formatNumber minFractionDigits='2' maxFractionDigits='2' value='${relatorio.totalDiariaEfetivo}'/>" /></td>
                        <td align="left" id="var2" width="110"><input type="text" style="text-align:right;" value="<fmt:formatNumber minFractionDigits='2' maxFractionDigits='2' value='${relatorio.totalDiariaSaldo}'/>" /></td>
                    </tr>
                    <tr>
                        <td align="left" id="campo" width="100">Valor Adiantamento</td>
                        <td align="left" id="var2" width="110"><input type="text" style="text-align:right;" value="<fmt:formatNumber minFractionDigits='2' maxFractionDigits='2' value='${relatorio.valorAdiantamento}'/>" /></td>
                        <td align="left" id="var2" width="110"><input type="text" style="text-align:right;" value="<fmt:formatNumber minFractionDigits='2' maxFractionDigits='2' value='${totalGasto}'/>" /></td>
                        <td align="left" id="var2" width="110"><input type="text" style="text-align:right;" value="<fmt:formatNumber minFractionDigits='2' maxFractionDigits='2' value='${relatorio.adiantamentoSaldo}'/>" /></td>
                    </tr>
                    <tr>
                        <td align="left" id="campo2" width="100">Total</td>
                        <td align="left" id="var2" width="110"><input type="text" style="text-align:right;" value="<fmt:formatNumber minFractionDigits='2' maxFractionDigits='2' value='${relatorio.totalPrevisto}'/>" /></td>
                        <td align="left" id="var2" width="110"><input type="text" style="text-align:right;" value="<fmt:formatNumber minFractionDigits='2' maxFractionDigits='2' value='${relatorio.totalEfetivo}'/>" /></td>
                        <td align="left" id="var2" width="110"><input type="text" style="text-align:right;" value="<fmt:formatNumber minFractionDigits='2' maxFractionDigits='2' value='${relatorio.totalSaldo}'/>" /></td>
                    </tr>
                    <tr>
                        <td align="center" id="var2" colspan="4">${relatorio.textoResumo}</td>
                    </tr>
                </table>
            </div>
            <!--
            FIM DO Resumo Viagem (R$)
            -->


            <!--
            INÍCIO DO ASSINATURAS
            -->
            <table id="grupo" border="0" cellpadding="0" cellspacing="0" width="800">
                <tr>
                    <td align="center" colspan="2" id="titulo">ASSINATURAS</td>
                </tr>
                <tr>
                    <td align="left" id="campo" width="400">Assinatura do Funcionário</td>
                    <td align="left" id="campo" width="400">Aprovação da Superintendência</td>
                </tr>
                <tr>
                    <td align="left" id="var3" width="400">_____/_____/_____,_______________________</td>
                    <td align="left" id="var3" width="400">_____/_____/_____,_______________________</td>
                </tr>
            </table>
            <!--
            FIM DO ASSINATURAS
            -->

            <!--
            INÍCIO DO Uso exclusivo da SUPAF
            -->
            <table id="grupo" border="0" cellpadding="0" cellspacing="0" width="800">
                <tr>
                    <td align="center" colspan="2" id="titulo">Uso exclusivo da SUPAF</td>
                </tr>
                <tr>
                    <td align="left" id="campo" width="400">Coordenação Financeira</td>
                    <td align="left" id="campo" width="400">Contabilidade</td>
                </tr>
                <tr>
                    <td align="left" id="var3" width="400">_____/_____/_____,_______________________</td>
                    <td align="left" id="var3" width="400">_____/_____/_____,_______________________</td>
                </tr>
                <tr>
                    <td align="left" id="campo" colspan="2">Contas a Pagar</td>
                </tr>
                <tr>
                    <td align="left" id="var3" colspan="2">_____/_____/_____,_______________________</td>
                </tr>
            </table>
            <!--
            FIM DO Uso exclusivo da SUPAF
            -->

            <!--
            INÍCIO DO Rodapé
            -->
            <table id="grupo" border="0" cellpadding="0" cellspacing="0" width="800">
                <tr>
                    <td align="left" id="campo2">
                        Prestação de Contas impressa em ${dataGeracao} Controle: ${relatorio.viagem.id} - ${relatorio.dataSolicitacao} -
                        Alterado por: ${relatorio.nomeSolicitante}
                    </td>
                </tr>
            </table>
            <!--
            FIM DO Rodapé
            -->
        </div>
    </body>
</html:html>