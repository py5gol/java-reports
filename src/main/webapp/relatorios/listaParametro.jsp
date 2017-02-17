<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@page import="br.com.geodev.app.relatoriosweb.Constantes"%>

<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Par&acirc;metros</title>
  <link href="css/estilo.css" rel="stylesheet" type="text/css" />
</head>

<body>

<div style="border: solid 1px; padding: 2px; background-color: #eff0e2;">

<h3>Par&acirc;metros</h3>

<form action="parametro.rel" name="FrmLista" method="post">
    <input type="hidden" name="id" value="<c:out value="${id}"/>" />
    <input type="hidden" name="op" value="" />
    <input type="hidden" name="idParametro" value="0" />

    <button onclick="javascript:novo()">Novo</button><br/>

    <c:url var="link1" value="/relatorio.rel"></c:url>
    <a href="${link1}">Voltar</a>
    
    <br/>

    <table width="100%" border="1" cellspacing="1" cellpadding="2">
    <tr>
        <th style="background-color: #eff0e2;">Relat&oacute;rio</th>
        <th style="background-color: #eff0e2;">Nome do Par&acirc;metro</th>
        <th style="background-color: #eff0e2;">Tipo do Par&acirc;metro</th>
        <th style="background-color: #eff0e2;">&nbsp;</th>
    </tr>
    <c:forEach items="${parametros}" var="par" step="1" begin="0" varStatus="status">
        <tr>
            <td width="20%"><c:out value="${par.nomeRelatorio}" /></td>
            <td width="30%"><c:out value="${par.nome}" /></td>
            <td width="30%"><c:out value="${par.tipo}" /></td>
            <td width="20%" align="center">
            <a href="javascript:editar('${par.idParametro}')">Editar</a>&nbsp;&nbsp;
            <a href="javascript:excluir('${par.idParametro}')">Excluir</a>&nbsp;&nbsp;</td>
        </tr>
    </c:forEach>
    </table>
</form>
</div>

<script type="text/javascript">
var form=document.FrmLista;
function novo() {
    form.op.value='<%=Constantes.CADASTRO %>'
    form.submit();
}

function editar(i) {
    form.idParametro.value=i;
    form.op.value='<%=Constantes.SELECIONAR %>';
    form.submit();
}

function excluir(i) {
    var sim=confirm('Deseja realmente excluir o par&acirc;metro?');
    if (sim) {
        form.idParametro.value=i;
        form.op.value='<%=Constantes.EXCLUIR %>';
        form.submit();
    }
}

function voltar() {
    form.action='relatorio.rel';
    form.op.value=''
    form.submit();
}
</script>

</body>
</html>

