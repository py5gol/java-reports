<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@page import="br.com.geodev.relatoriosweb.Constantes"%>

<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Cadastro de Relat&oacute;rios</title>
  <link href="css/estilo.css" rel="stylesheet" type="text/css" />
</head>

<body>

<div style="border: solid 1px; padding: 2px; background-color: #eff0e2;">

<h3>Cadastro de Relat&oacute;rios</h3>

<form action="relatorio.rel" method="post" name="FrmRelatorio" enctype="multipart/form-data">

<table width="100%" border="0">
    <tr>
        <td class="required" width="20%">T&iacute;tulo:</td>
        <td class="tabcamlat" width='80%'><input type="text"
            name="titulo" maxlength="250" size="30"
            value="<c:out value="${ relatorio.titulo }"/>" /></td>
    </tr>
    <tr>
        <td class="required" width="20%">Nome:</td>
        <td class="tabcamlat" width='80%'><input type="text"
            name="titulo" maxlength="250" size="30"
            value="<c:out value="${ relatorio.nome }"/>" /></td>
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
        <td colspan="2" class="RodapeTabela">
            <button onclick="javascript:enviar()">Salvar</button>
        </td>
    </tr>
</table>

<input type="hidden" name="id" value="${rel.idRelatorio}">
<input type="hidden" name="op" value="<%=Constantes.SALVAR %>">

</form>

<c:url var="link1" value="/relatorio.rel"></c:url>
<a href="${link1}">Voltar</a>

</div>

<script type="text/javascript">
  var form=document.FrmRelatorio;
  function enviar() {
    if (form.titulo.value == '') {
      alert ('Campos obrigatorios nao preenchidos');
      return false;
    }
    form.submit();
  }
</script>

</body>
</html>