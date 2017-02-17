<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@page import="br.com.geodev.app.relatoriosweb.Constantes"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Subrelat&oacute;rios 2</title>
<link href="css/estilo.css" rel="stylesheet" type="text/css" />
</head>

<body>

<div style="padding: 1px; background-color: #848668;">&nbsp;</div>

<div style="padding: 1px; background-color: #eff0e2;">


<h3>Subrelat&oacute;rios2</h3>

    <a href="/relatorioswebmvn/subrelatorio.rel?op=5">Novo</a>
<form action="subrelatorio.rel" name="FrmLista" method="post">

<button onclick="javascript:novo()">Novo</button>
<button onclick="javascript:voltar()">Voltar</button>

<br/>
<br/>

    <table width="100%" border="0" cellspacing="1" cellpadding="2" bgcolor="#888888">
    <tr>
        <th style="background-color: #eff0e2;">T&iacute;tulo</th>
        <th style="background-color: #eff0e2;">Nome do Arquivo</th>
        <th style="background-color: #eff0e2;">&nbsp;</th>
    </tr>
        <c:forEach items="${consulta}" var="par" step="1" begin="0" varStatus="status">
            <tr>
                <td style="background-color: #eff0e2;">${par.titulo}</td>
                <td style="background-color: #eff0e2;">${par.nome}</td>
                <td style="background-color: #eff0e2;" align="center">
                <a href="javascript:excluir('${par.idRelatorio}')">Excluir</a>&nbsp;
                <a href="RelatorioJasper?id=${par.idRelatorio}">Fontes</a></td>
            </tr>
        </c:forEach>
</table>

<input type="hidden" name="id" value="<c:out value="${id}"/>" />
<input type="hidden" name="op" value="" />
<input type="hidden" name="id_mestre" value="${id_mestre}">

</form>

</div>

<div style="padding: 1px; background-color: #848668;">&nbsp;</div>

<script type="text/javascript">
var form=document.FrmLista;
function novo()
{
	form.op.value='<%=Constantes.CADASTRO %>'
	form.submit();
}

function voltar()
{
    form.action='relatorio.rel';
	form.op.value=''
	form.submit();
}

function editar(i)
{
	form.idParametro.value=i;
	form.op.value='<%=Constantes.SELECIONAR %>';
	form.submit();
}

function excluir(i)
{
	var sim=confirm('Deseja realmente excluir o subrelatorio?');
	if(sim){
		form.id.value=i;
		form.op.value='<%=Constantes.EXCLUIR %>';
		form.submit();
	}
}

</script>

</body>
</html>

