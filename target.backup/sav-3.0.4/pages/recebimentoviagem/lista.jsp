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
        <script src="<%=request.getContextPath()%>/dwr/interface/Funcoes.js"></script>
        <script src="<%=request.getContextPath()%>/dwr/engine.js"></script>
        <script src="<%=request.getContextPath()%>/dwr/util.js"></script>
        <html:javascript formName="viagemForm" method="validar"/>

        <title><fmt:message key="aplicacao.nome" /></title>

        <script>
            function submitRecebimento() { 
                var idsViagens = new Array();
                var indice = 0;
                var recebimento = document.getElementsByName("ids_recebimento");
                for (var i=0; i<recebimento.length; i++){
                    if (recebimento[i].checked == true) { 
                        idsViagens[indice++] = recebimento[i].value;
                    }
                }
                Funcoes.verificarCentroCustoFonteDeRecurso(idsViagens, $("idFonteRecurso").value, verificaRecebimento);       
            }
            
            function isChecked(name){
                var exportacao = document.getElementsByName(name);
                var marcado = false;

                for (i=0; i<exportacao.length; i++){
                    if (exportacao[i].checked == true) { marcado = true; break; }
                }

                return marcado;
            }
            
            function verificaRecebimento(dtoRecebimento){
                if(document.viagemForm.codigoFonteRecurso.value == ""){
                    alert("Informe a Fonte de Recurso.");
                }else{
                    if (!dtoRecebimento.valido) {
                        alert(dtoRecebimento.mensagens);
                        return false;
                    }
                    else if(isChecked("ids_recebimento")/*marcado == true*/){
                        document.viagemForm.action = "<%=request.getContextPath()%>/Viagem.do?metodo=receber";
                        document.viagemForm.submit();
                    }else{
                        alert("Nenhuma viagem selecionada para recebimento!");
                    }
                }
            }

            function  submitDesfazerRecebimento(){
                if(isChecked("ids_exportacao")){
                    document.viagemForm.action = "<%=request.getContextPath()%>/Viagem.do?metodo=desfazerRecebimento";
                    document.viagemForm.submit();
                }else{
                    alert("Nenhuma viagem selecionada para cancelamento!");
                }
            }

            function submitGerarArquivo(){
                if(isChecked("ids_exportacao")){
                    if(confirm('Ao confirmar as datas de pagamento das viagens, será definido o mês de referência para cálculo de verificação do limite de 50% do salário. Caso a data de pagamento de alguma viagem seja alterada no GEM, para um mês diferente, recomendamos cancelar/extornar o pagamento do GEM, alterar a data de pagamento e exportar um novo arquivo de carga.')){
                        document.viagemForm.action = "<%=request.getContextPath()%>/Viagem.do?metodo=exportar";
                        document.viagemForm.submit();
                    }
                }else{
                    alert("Nenhuma viagem selecionada para gerar arquivo!");
                }
            }

        </script>
    </head>
    <body>
        <div id="wrap">
            <jsp:include flush="false" page="/inc/header.jsp" />
            <jsp:include flush="false" page="/inc/sidebar.jsp" />
            <div id="content">

                <h2>Lista de Solicitações a Receber</h2>
                <html:form action="/Viagem.do" method="post">

                    <div style="width:100%; height:200px; margin-bottom:5px; overflow:auto;">
                        <display:table id="viagensDTO" name="lista" defaultsort="2" sort="list" defaultorder="descending" export="false" requestURI="./Viagem.do" class="tabelaDisplay">
                            <display:column title="<input type='checkbox' name='ids_recebimento_check' onclick=javascript:selecionar_tudo('ids_recebimento'); />" style="width:22px">
                                <input type="checkbox" name="ids_recebimento" value="${viagensDTO.viagem.id}"/>
                            </display:column>
                            <display:column property="viagem.id" title="<label title='Número da solicitação'>Num.</label>" style="text-align:center; width:40px" />
                            <display:column property="viagem.dataSolicitacao" title="<label title='Data da solicitação'>Data solic.</label>" style="text-align:center; width:60px;" format="{0,date,dd/MM/yyyy}" />
                            <display:column property="nomeViajante" title="<label title='Viajante'>Viajante</label>" style="text-align:left; width:220px" />
                            <display:column property="periodo" title="<label title='Período da Viagem'>Período da<br>Viagem</label>" style="text-align:center; width:90px" />
                            <display:column property="viagem.totalDiarias" style="text-align:right; width:50px" title="<label title='Diárias'>Diárias</label>" format="{0,number,0.00}" />
                            <display:column property="viagem.valorAdiantamento" style="text-align:right; width:50px" title="<label title='Adiantamento'>Adiant.</label>" format="{0,number,0.00}" />
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

                    <div>
                        Fonte de Recursos: &nbsp;
                        <html:select property="codigoFonteRecurso" styleId="idFonteRecurso">
                            <html:option value="">-- Selecione --</html:option>
                            <html:optionsCollection name="listaFonteRecurso" value="codigo" label="descricaoCompleta"/>
                        </html:select>

                        <acesso:input tipo="select" action="Viagem.do" metodo="receber" chave="solicRecbViagem" styleClass="botao" onclick="submitRecebimento();" />
                    </div>

                    <div style="margin-top:25px;font-size:12;">
                        <h2>Lista de Solicitações Recebidas</h2>
                    </div>

                    <div style="width:100%; height:200px; margin-bottom:5px; overflow:auto;">
                        <display:table id="viagensDTO" name="listaR" defaultsort="2" sort="list" defaultorder="descending" export="false" requestURI="./Viagem.do" class="tabelaDisplay">
                            <display:column title="<input type='checkbox' name='ids_exportacao_check' onclick=javascript:selecionar_tudo('ids_exportacao'); />" style="width:22px">
                                <input type="checkbox" name="ids_exportacao" value="${viagensDTO.viagem.id}"/>
                            </display:column>
                            <display:column property="viagem.id" title="<label title='Número da solicitação'>Num.</label>" style="text-align:center; width:40px" />
                            <display:column property="viagem.dataSolicitacao" title="<label title='Data da solicitação'>Data solic.</label>" style="text-align:center; width:60px;" format="{0,date,dd/MM/yyyy}" />
                            <display:column property="nomeViajante" title="<label title='Viajante'>Viajante</label>" style="text-align:left; width:220px" />
                            <display:column property="periodo" title="<label title='Período da Viagem'>Período da<br>Viagem</label>" style="text-align:center; width:100px" />
                            <display:column title="<label title='Data de Pagamento'>Data de<br>Pagamento</label>" style="text-align:center; width:90px">
                                <a href="./Viagem.do?id=${viagensDTO.viagem.id}&metodo=selecionarDataPagamento" target="_self">
                                    <fmt:formatDate pattern="dd/MM/yyyy" value="${viagensDTO.viagem.dataPagamento}"/>
                                    &nbsp;
                                    <img align="middle" src="img/box_calendario.gif" width="16" height="15" border="0" title="Alterar data de pagamento" />
                                </a>
                            </display:column>
                            <display:column property="viagem.totalDiarias" style="text-align:right; width:50px" title="<label title='Diárias'>Diárias</label>" format="{0,number,0.00}" />
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
                            <display:column property="fonteRecurso.descricao" title="<label title='Fonte de recurso'>Fonte de<br>recurso" style="text-align:center; width:90px;" />
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

                    <div>
                        <acesso:input tipo="select" action="Viagem.do" metodo="Gerar arquivo" styleInput="width: 90px;" chave="solicExportaViagem" styleClass="botao" onclick="submitGerarArquivo();" />
                        <acesso:input tipo="select" action="Viagem.do" metodo="Cancelar recebimento" styleInput="width: 135px; float:right;" chave="solicCancelaRecbViagem" styleClass="botao" onclick="if(confirm('Deseja realmente cancelar o(s) recebimento(s) selecionado(s)?')){ submitDesfazerRecebimento(); }" />
                    </div>

                </html:form>
            </div>
            <jsp:include flush="false" page="/inc/footer.jsp" />
        </div>
        <msg:alert escopo="session"/>
    </body>
</html>
