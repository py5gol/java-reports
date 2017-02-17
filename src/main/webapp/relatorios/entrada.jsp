<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@page import="br.com.geodev.app.relatoriosweb.Constantes"%>

<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
  <title>${titulo}</title>
  <link href="css/estilo.css" rel="stylesheet" type="text/css" />
</head>

<body>

    <div style="border: solid 1px; padding: 2px; background-color: #eff0e2;">
    
        <h3><c:out value="${titulo}"/></h3>

        <form action="gerarRelatorio.rel" method="post" name="FrmParametro">

        <input type="hidden" id="extensao_id" name="extensao_id" value="<%=Constantes.PDF%>">

        <c:out value="${html}" escapeXml="false"/><br/>
        
        <input type="button" value="Gerar Relat&oacute;rio PDF"
               onclick="enviar(<%=Constantes.PDF%>);" /><br/>

        <input type="button" value="Gerar Relat&oacute;rio XLS"
               onclick="enviar(<%=Constantes.XLS%>);" /><br/>

        <input type="button" value="Gerar Relat&oacute;rio RTF"
               onclick="enviar(<%=Constantes.RTF%>);" /><br/>

        <input type="hidden" name="id"
            value="<c:out value="${id}"/>">

        <input
            type="hidden" name="op" value="<%=Constantes.GERAR_RELATORIO%>">

        </form>

        <script type="text/javascript">
            var form = document.FrmParametro;
            function enviar(extensao_id) {
                form.extensao_id.value = extensao_id;
                form.submit();
            }
        </script>
    </div>
</body>
</html>