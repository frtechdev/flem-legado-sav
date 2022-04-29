<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt-rt" %>
<%@taglib uri="http://flem.org.br/autentica-tag" prefix="acesso"%>
<acesso:autentica sistema="sav" />
<div id="header">
    <a href="index.jsp"><img align="left"
    src="img/logo.gif" alt="<fmt:message key="aplicacao.nome" /> - Página Inicial "/></a>
     <img align="right" src="img/flemlogo.gif" alt="Flem Web"/>
 </div>
 
 <div id="subHeader" style="text-align:right; font-size:7pt; color:#6B94CD; font-weight:bold;">     
    <jsp:useBean id="hoje" class="java.util.Date"/>
    <c-rt:set var="usuario" value="<%=((br.org.flem.fw.service.IUsuario) 
    session.getAttribute(br.org.flem.fw.util.Constante.USUARIO_LOGADO))%>" />
    <fmt-rt:formatDate value="${hoje}" type="DATE" pattern="dd/MM/yyyy"/>, Olá <c-rt:out value="${usuario.nome}"/>
    &nbsp;
    <a href="<%=request.getContextPath()%>/logout" style="color:red;font-weight:bold;">Sair</a>
    &nbsp;&nbsp;
 </div>
