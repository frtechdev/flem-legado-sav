<%@page contentType="text/html" errorPage="/erro.jsp"%> 
<%@page pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
            function submitRecebimento(){
                var recebimento = document.getElementsByName("ids_recebimento");
                var marcado = false;

                for (i=0; i<recebimento.length; i++){
                    if (recebimento[i].checked == true) { marcado = true; break; }
                 }

                if(marcado == true){
                    document.viagemForm.action = "<%=request.getContextPath()%>/PrestacaoContas.do?metodo=receber";
                    document.viagemForm.submit();
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
                        document.viagemForm.action = "<%=request.getContextPath()%>/PrestacaoContas.do?metodo=desfazerRecebimento";
                        document.viagemForm.submit();
                    }else{
                       alert("Nenhuma viagem selecionada para cancelamento!");
                    }
              }

                function submitGerarArquivo(){
                    if(isChecked()){
                        if(confirm('Ao confirmar as datas de pagamento das viagens, será definido o mês de referência para cálculo de verificação do limite de 50% do salário. Caso a data de pagamento de alguma viagem seja alterada no GEM, para um mês diferente, recomendamos cancelar/extornar o pagamento do GEM, alterar a data de pagamento e exportar um novo arquivo de carga.')){
                            document.viagemForm.action = "<%=request.getContextPath()%>/PrestacaoContas.do?metodo=exportar";
                            document.viagemForm.submit();
                        }
                    }else{
                        alert("Nenhuma viagem selecionada para gerar arquivo!");
                    }
                }

              function submitFinalizar(){
                 if(isChecked()){
                        document.viagemForm.action = "<%=request.getContextPath()%>/PrestacaoContas.do?metodo=finalizar";
                        document.viagemForm.submit();
                    }else{
                       alert("Nenhuma viagem selecionada para finalizar!");
                    }
              }

        </script>
    </head>
    <body>
        <div id="wrap">
            <jsp:include flush="false" page="/inc/header.jsp" />
            <jsp:include flush="false" page="/inc/sidebar.jsp" />
            <div id="content">

                <h2>Lista de Prestação de Contas a Receber</h2>
                <html:form action="/Viagem.do" method="post">
                    <div style="width:100%; text-align:right;">
                        <acesso:input tipo="select" action="Viagem.do" metodo="receber" chave="solicRecbPrestacao" styleClass="botao" onclick="submitRecebimento();" />

                        <acesso:input tipo="select" action="Viagem.do" metodo="Relatório" chave="solicRelatPrestacao" styleClass="botao" onclick="location.href='./PrestacaoContas.do?metodo=relatorioPrestacoesPendentes'" />
                    </div>

                    <div style="width:100%; height:200px; margin-bottom:5px; overflow:auto;">


                    <display:table id="viagensDTO" name="lista1" defaultsort="2" sort="list" defaultorder="descending" export="false" requestURI="./PrestacaoContas.do" class="tabelaDisplay">
                        <display:column title="<input type='checkbox' name='ids_recebimento_check' onclick=javascript:selecionar_tudo('ids_recebimento'); />" style="width:22px">
                                <input type="checkbox" name="ids_recebimento" value="${viagensDTO.viagem.id}"/>
                        </display:column>
                        <display:column property="viagem.id" title="<label title='Número da solicitação'>Num.</label>" style="text-align:center; width:40px" />
                        <display:column property="viagem.prestacaoContas.dataPrestacao" title="<label title='Data da prestação de contas'>Data prest.</label>" style="text-align:center; width:60px;" format="{0,date,dd/MM/yyyy}" />
                        <display:column property="nomeViajante" title="<label title='Viajante'>Viajante</label>" style="text-align:left; width:220px" />
                        <display:column property="periodo" title="<label title='Período da Viagem'>Período da<br>Viagem</label>" style="text-align:center; width:100px" />
                        <display:column property="viagem.prestacaoContas.totalDiarias" style="text-align:right; width:50px" title="<label title='Diárias'>Diárias</label>" format="{0,number,0.00}" />
                        <display:column property="viagem.valorAdiantamento" style="text-align:right; width:50px" title="<label title='Adiantamento'>Adiant.</label>" format="{0,number,0.00}" />
                        <display:column title="<label title='Número do processo'>Num. do<br>processo</label>" style="text-align:center; width:60px">
                            <c:if test="${viagensDTO.processo != 0}">
                                <a href="<fmt:message key="aplicacao.linkEtiquetaProcesso" />?id=${viagensDTO.processo}" target="_blank">
                                    ${viagensDTO.viagem.processo}
                                </a>
                            </c:if>
                            <c:if test="${viagensDTO.processo == 0}">
                                ${viagensDTO.viagem.processo}
                            </c:if>
                        </display:column>
                        <display:column title="<label title='Status'>Status</label>" style="text-align:center; width:80px" >
                            <c:if test="${viagensDTO.viagem.prestacaoContas.statusPrestacaoContas == 'PRESTACAO_INFORMADA'}">
                                <span title="Prestação de conta informada" style="cursor:default; color:#404040">Prest. info.</span>
                            </c:if>
                        </display:column>
                        <display:column title="<label title='Imprimir prestação de conta'>Imp.<br>Prest.</label>" style="text-align:center; width:40px" >
                            <!--a href="./PrestacaoContas.do?id=${viagensDTO.viagem.id}&metodo=relatorio">
                                <img align="middle" src="img/pdf.png" width="22" height="22" border="0" title="Imprimir prestação de conta" />
                            </a-->
                            <a href="./PrestacaoContas.do?id=${viagensDTO.viagem.id}&metodo=relatorioHtml" target="_blank">
                                <img align="middle" src="img/impressora.png" width="23" height="22" border="0" title="Imprimir prestação de conta" />
                            </a>
                        </display:column>
                        <!--display:column title="<label title='Porcentagem salário X Utilização diárias'>Porcent.</label>" style="width:50px;" property="porcentagem" format="{0,number,0.00}"/-->
                    </display:table>

                    </div>
                            
                    <div style="width:100%; text-align:left; font-size:12;">
                        <h2>Lista de Prestação de Contas Recebidas</h2>
                    </div>

                    <display:table id="viagensDTO" name="lista2" defaultsort="2" sort="list" defaultorder="descending" export="false" requestURI="./PrestacaoContas.do" class="tabelaDisplay">
                        <display:column title="<input type='checkbox' name='ids_exportacao_check' onclick=javascript:selecionar_tudo('ids_exportacao'); />" style="width:22px">
                                <input type="checkbox" name="ids_exportacao" value="${viagensDTO.viagem.id}"/>
                        </display:column>
                        <display:column title="<label title='Número da solicitação'>Num.</label>" style="text-align:center; width:40px">
                            <c:if test="${(viagensDTO.viagem.prestacaoContas.totalAdiantamento - viagensDTO.viagem.totalAdiantamento) <= 0}">*</c:if>${viagensDTO.viagem.id}
                        </display:column>
                        <display:column property="viagem.prestacaoContas.dataPrestacao" title="<label title='Data da prestação de contas'>Data prest.</label>" style="text-align:center; width:60px;" format="{0,date,dd/MM/yyyy}" />
                        <display:column property="nomeViajante" title="<label title='Viajante'>Viajante</label>" style="text-align:left; width:220px" />
                        <display:column property="periodo" title="<label title='Período da Viagem'>Período da<br>Viagem</label>" style="text-align:center; width:100px" />
                        <display:column title="<label title='Data de Pagamento'>Data de<br>Pagamento</label>" style="text-align:center; width:90px">
                            <c:if test="${(viagensDTO.viagem.prestacaoContas.totalDiarias > viagensDTO.viagem.totalDiarias) || (viagensDTO.viagem.prestacaoContas.totalAdiantamento > viagensDTO.viagem.totalAdiantamento)}">
                                <a href="./PrestacaoContas.do?id=${viagensDTO.viagem.id}&metodo=selecionarDataPagamento" target="_self">
                                    <fmt:formatDate pattern="dd/MM/yyyy" value="${viagensDTO.viagem.prestacaoContas.dataPagamento}"/>
                                    &nbsp;
                                    <img align="middle" src="img/box_calendario.gif" width="16" height="15" border="0" title="Alterar data de pagamento" />
                                </a>
                            </c:if>
                            <c:if test="${viagensDTO.viagem.prestacaoContas.totalAdiantamento <= viagensDTO.viagem.totalAdiantamento}">
                                -
                            </c:if>
                        </display:column>
                        <display:column property="viagem.prestacaoContas.totalDiarias" style="text-align:right; width:50px" title="<label title='Diárias'>Diárias</label>" format="{0,number,0.00}" />
                        <display:column property="viagem.valorAdiantamento" style="text-align:right; width:50px" title="<label title='Adiantamento'>Adiant.</label>" format="{0,number,0.00}" />
                        <display:column style="text-align:right; width:50px" title="<label title='Saldo'>Saldo</label>">
                            <fmt:formatNumber pattern="0.00" value="${viagensDTO.viagem.prestacaoContas.totalAdiantamento - viagensDTO.viagem.totalAdiantamento}"/>
                        </display:column>
                        <display:column title="<label title='Número do processo'>Num. do<br>processo</label>" style="text-align:center; width:60px">
                            <c:if test="${viagensDTO.processo != 0}">
                                <a href="<fmt:message key="aplicacao.linkEtiquetaProcesso" />?id=${viagensDTO.processo}" target="_blank">
                                    ${viagensDTO.viagem.processo}
                                </a>
                            </c:if>
                            <c:if test="${viagensDTO.processo == 0}">
                                ${viagensDTO.viagem.processo}
                            </c:if>
                        </display:column>
                        <display:column title="<label title='Status'>Status</label>" style="text-align:center; width:80px" >
                            <c:if test="${viagensDTO.viagem.prestacaoContas.statusPrestacaoContas == 'PRESTACAO_RECEBIDA'}">
                                <span title="Prestação de conta recebida" style="cursor:default; color:#404040">Prest. rec.</span>
                            </c:if>
                        </display:column>
                        <display:column title="<label title='Imprimir prestação de conta'>Imp.<br>Prest.</label>" style="text-align:center; width:40px" >
                            <!--a href="./PrestacaoContas.do?id=${viagensDTO.viagem.id}&metodo=relatorio">
                                <img align="middle" src="img/pdf.png" width="22" height="22" border="0" title="Imprimir prestação de conta" />
                            </a-->
                            <a href="./PrestacaoContas.do?id=${viagensDTO.viagem.id}&metodo=relatorioHtml" target="_blank">
                                <img align="middle" src="img/impressora.png" width="23" height="22" border="0" title="Imprimir prestação de conta" />
                            </a>
                        </display:column>
                        <!--display:column title="<label title='Porcentagem salário X Utilização diárias'>Porcent.</label>" style="width:50px;" property="porcentagem" format="{0,number,0.00}"/-->
                    </display:table>
                     <div style="float: left;">
                        <acesso:input tipo="select" action="Viagem.do" metodo="Gerar arquivo" styleInput="width: 90px;" chave="solicExportaPrestacao" styleClass="botao" onclick="submitGerarArquivo();" />
                        <acesso:input tipo="select" action="Viagem.do" metodo="Cancelar recebimento" styleInput="width: 135px;" chave="solicCancelaRecbPrestacao" styleClass="botao" onclick="submitDesfazerRecebimento();" />
                     </div>
                     <div style="float: right;">
                        <acesso:input tipo="select" action="Viagem.do" metodo="Finalizar viagem/prestação" styleInput="width:160px; float:right;" chave="*" styleClass="botao" onclick="if(existeSelecionado('ids_exportacao')){if(confirm('Deseja realmente finalizar esta viagem/prestação?')){submitFinalizar();}} return false;" />
                     </div>
                     <div style="clear: both">
                        * Viagens com Saldo menor ou igual a zero não são incluídas na geração do arquivo de exportação
                    </div>
                </html:form>
            </div>
            <jsp:include flush="false" page="/inc/footer.jsp" />
        </div>
        <msg:alert escopo="session"/>
    </body>
</html>
