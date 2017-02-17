<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@page import="br.com.geodev.app.relatoriosweb.Constantes"%>

<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
  <title>Cadastro de Par&acirc;metros</title>
  <link href="css/estilo.css" rel="stylesheet" type="text/css" />
</head>

<!--<body onload="loadMetodos()">-->
<body>

<div style="border: solid 1px; padding: 2px; background-color: #eff0e2;">
    <h3>Cadastro de Par&acirc;metros</h3>
    <form action="parametro.rel" method="post" name="FrmParametro">
    <table width="100%" border="0">
    <tr>
        <td class="required" width="20%">Nome:</td>
        <td class="tabcamlat" width='80%'><input type="text"
            name="nome" maxlength="100" size="30"
            value="${ parametro.nome }" /></td>
    </tr>
    <tr>
        <td class="required" width="20%">Tipo:</td>
        <td class="tabcamlat" width='80%'>
            <select name="tipo" size="1">
            <c:choose>
                <c:when test="${parametro.tipo == 'String'}">
                    <option value="String" selected="true">String</option>
                </c:when>
                <c:otherwise>
                    <option value="String">String</option>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${parametro.tipo == 'Integer'}">
                    <option value="Integer" selected="true">Integer</option>
                </c:when>
                <c:otherwise>
                    <option value="Integer">Integer</option>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${parametro.tipo == 'Long'}">
                    <option value="Long" selected="true">Long</option>
                </c:when>
                <c:otherwise>
                    <option value="Long">Long</option>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${parametro.tipo == 'Date'}">
                    <option value="Date" selected="true">Date</option>
                </c:when>
                <c:otherwise>
                    <option value="Date">Date</option>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${parametro.tipo == 'Boolean'}">
                    <option value="Boolean" selected="true">Boolean</option>
                </c:when>
                <c:otherwise>
                    <option value="Boolean">Boolean</option>
                </c:otherwise>
            </c:choose>
            </select>
        </td>
    </tr>
    <tr>
        <td colspan="2" class="RodapeTabela">
            <input type="submit" value="Salvar"/>
        </td>
    </tr>
    </table>

    <input type="hidden" name="idParametro" value="${parametro.idParametro}">
    <input type="hidden" name="id" value="${id}">
    <input type="hidden" name="op" value="<%=Constantes.SALVAR%>">

    </form>
</div>

    <script type="text/javascript">
	var form = document.FrmParametro;
	var metodoParametro = '<c:out value="${parametro.metodo}"/>';




	function getAjax()
	{
		if (window.XMLHttpRequest)
	    	return new XMLHttpRequest();
	    else
	    	if (window.ActiveXObject)
	    	{
	           isIE = true;
	           return new ActiveXObject("Microsoft.XMLHTTP");
	       	}
	}

	function parseMessages(responseXML)
	{
		var metodos = responseXML.getElementsByTagName("Metodos")[0];
		for (loop = 0; loop < metodos.childNodes.length; loop++)
		{
	    	var metodo = metodos.childNodes[loop];
			addOptionToSelect(form.metodo,metodo.childNodes[0].nodeValue,metodo.childNodes[0].nodeValue, metodoParametro);
	    }
	}

	function addOptionToSelect(select, text, value, marcado)
	{
		var optionElm = document.createElement('option');
	 	var optionText = document.createTextNode(text);
	 	if (value!='' && value!=null) optionElm.value = value;
		if(value==marcado)
		 	optionElm.setAttribute('selected','selected');
	 	return select.appendChild(optionElm.appendChild(optionText).parentNode)
	}
</script>

</body>
</html>
