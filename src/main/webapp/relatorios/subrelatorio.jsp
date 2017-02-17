<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@page import="br.com.geodev.app.relatoriosweb.Constantes"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Cadastro de Subrelat&oacute;rios</title>
<link href="css/estilo.css" rel="stylesheet" type="text/css" />
</head>

<body>

<div style="border: solid 1px; padding: 2px; background-color: #eff0e2;">

<h3>Cadastro de Subrelat&oacute;rios</h3>

<form action="subrelatorio.rel" method="post" name="FrmRelatorio" enctype="multipart/form-data">

<input type="hidden" name="id" value="">
<input type="hidden" name="id_mestre" value="${id_mestre}">
<input type="hidden" name="op" value="<%=Constantes.SALVAR %>">

<table width="100%" border="0" cellspacing="1" cellpadding="2" bgcolor="#888888">
<tr>
    <td style="background-color: #eff0e2;">T&iacute;tulo:</td>
    <td style="background-color: #eff0e2;">
        <input type="text" name="titulo" maxlength="250" size="30" value="" />
    </td>
</tr>
    <tr>
        <td class="required" width="20%">Arquivo Jasper:</td>
        <td class="tabcamlat" width='80%'>
            <input type="file" name="arquivo" />
        </td>
    </tr>
    <tr>
        <td class="required" width="20%">Arquivo Jrxml:</td>
        <td class="tabcamlat" width='80%'>
            <input type="file" name="arquivo_jrxml" />
        </td>
    </tr>
    <tr>
    <td colspan="2" style="background-color: #eff0e2;">
        <input type="button" value="Salvar" onclick="enviar()" />
        <button onclick="javascript:voltar()">Voltar</button>
    </td>
</tr>
</table>

</form>

</div>

<div style="padding: 1px; background-color: #848668;">&nbsp;</div>

<script type="text/javascript">
	var form=document.FrmRelatorio;
	function enviar()
	{
		if(form.titulo.value == '')
		{
			alert ('Campos obrigat&oacute;rios n&otilde;o preenchido');
			return false;
		}
		form.submit();	
	}

function voltar()
{
    form.action='subrelatorio.rel';
	form.op.value='<%=Constantes.LISTAR %>';
	form.submit();
}
</script>

</body>
</html>
