<%@page contentType="text/html" errorPage="/erro.jsp"%> 
<%@page pageEncoding="ISO-8859-1" buffer = "16kb"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@taglib uri="http://flem.org.br/acesso-tag" prefix="acesso" %>
<%@taglib uri="http://flem.org.br/mensagem-tag" prefix="msg"%>
<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
        <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
        <link rel="stylesheet" type="text/css" href="css/800px.css" />
        <link href="css/displaytag.css" rel="stylesheet" type="text/css" />

        <script language="JavaScript">
            function submitFiltro(){
                document.forms[0].action = "<%=request.getContextPath()%>/ConsultoresInativos.do?metodo=filtrar";
                document.forms[0].submit();
                document.forms[0].action = "<%=request.getContextPath()%>/ConsultoresInativos.do";
            }
        </script>

        <title><fmt:message key="aplicacao.nome" /></title>
    </head>
    <body>
        <div id="wrap">
            <jsp:include flush="false" page="/inc/header.jsp" />
            <jsp:include flush="false" page="/inc/sidebar.jsp" />

            <div id="content">
                <h2>Lista de consultores inativos com prestação de contas em aberto</h2>

                <html:form action="/ConsultoresInativos.do" method="post">
                    <display:table id="viagensDTO" name="lista" defaultsort="3"  sort="list" export="false" excludedParams="metodo" requestURI="./Viagem.do" class="tabelaDisplay" defaultorder="descending" pagesize="100" >
                        <display:column property="viagem.id" title="<label title='Número da solicitação'>Num.</label>" style="text-align:center; width:40px" />
                        <display:column property="viagem.dataSolicitacao" title="<label title='Data da solicitação'>Data solic.</label>" style="text-align:center; width:60px;" format="{0,date,dd/MM/yyyy}" />
                        <display:column property="nomeViajante" title="<label title='Viajante'>Viajante</label>" style="text-align:left; width:210px" />
                        <display:column property="periodo" title="<label title='Período da Viagem'>Período da<br>Viagem</label>" style="text-align:center; width:100px" />
                        <display:column property="viagem.totalDiarias" style="text-align:right; width:50px" title="<label title='Diárias'>Diárias</label>" format="{0,number,0.00}" />
                        <display:column property="viagem.valorAdiantamento" style="text-align:right; width:50px" title="<label title='Adiantamento'>Adiant.</label>" format="{0,number,0.00}" />
                        <display:column title="<label title='Ação'>Ação</label>" style="text-align:center; width:40px" paramId="id" paramProperty="viagem.id" >
                            <c:if test="${solicCriarEditarPrestFuncionario == '1'}">
                                <c:if test="${viagensDTO.viagem.statusViagem == 'VIAGEM_RECEBIDA' && viagensDTO.prestouConta == '0'}">
                                    <a href="./PrestacaoContas.do?id=${viagensDTO.viagem.id}&metodo=novo"><img align="middle" src="img/editP.png" width="22" height="22" border="0" title="Criar prestação de contas" /></a>
                                    </c:if>
                                    <c:if test="${viagensDTO.prestouConta == '1' && viagensDTO.viagem.prestacaoContas.statusPrestacaoContas == 'PRESTACAO_INFORMADA'}">
                                    <a href="./PrestacaoContas.do?id=${viagensDTO.viagem.id}&metodo=selecionar"><img align="middle" src="img/editP.png" width="22" height="22" border="0" title="Editar prestação de contas" /></a>
                                    </c:if>
                                </c:if>
                        </display:column>

                        <display:column title="<label title='Imprimir prestação de conta'>Imp.<br>Prest.</label>" style="text-align:center; width:40px" >
                            <c:if test="${viagensDTO.prestouConta == '1'}">
                                <a href="./PrestacaoContas.do?id=${viagensDTO.viagem.id}&metodo=relatorioHtml" target="_blank">
                                    <img align="middle" src="img/impressora.png" width="23" height="22" border="0" title="Imprimir prestação de conta" />
                                </a>
                            </c:if>
                        </display:column>

                        <display:column title="<label title='Excluir solicitação de viagem'>Excluir</label>" style="text-align:center; width:40px" paramId="id" paramProperty="viagem.id" >
                            <c:if test="${solicEditarViagFuncionario == '1'}">
                                <c:if test="${viagensDTO.viagem.statusViagem == 'VIAGEM_ABERTA'}">
                                    <a href="./Viagem.do?ids_exclusao=${viagensDTO.viagem.id}&metodo=excluir" onclick="if(confirm('Deseja realmente excluir o(s) registro(s) selecionado(s)?')){return true;} return false;"><img align="middle" src="img/delete.png" width="22" height="22" border="0" title="Excluir solicitação de viagem" /></a>
                                    </c:if>
                                </c:if>
                            </display:column>
                            <%--/c:if--%>
                        </display:table>
                    </html:form>
            </div>
            <jsp:include flush="false" page="/inc/footer.jsp" />
        </div>
        <msg:alert escopo="session"/>
    </body>
</html>
