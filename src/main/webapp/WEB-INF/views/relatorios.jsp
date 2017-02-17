 <%@page contentType="text/html" pageEncoding="UTF-8"%>
 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

 <%@page import="br.com.geodev.relatoriosweb.Constantes"%>

 <html>
 <head>
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 <title>Relat&oacute;rios</title>
 <link href="css/estilo.css" rel="stylesheet" type="text/css" />
 </head>

 <body>

 <div style="padding: 1px; background-color: #848668;">&nbsp;</div>

 <div style="padding: 1px; background-color: #eff0e2;">

     <h3>Relat&oacute;rios</h3>

     <form action="relatorio.rel" name="FrmLista" method="post">
     <button onclick="javascript:novo()">Novo</button>
     <br/>
     <br/>

     <table width="100%" border="0" cellspacing="1" cellpadding="2" bgcolor="#888888">
     <tr>
         <th style="background-color: #eff0e2;">T&iacute;tulo</th>
         <th style="background-color: #eff0e2;">Nome do Arquivo</th>
         <th style="background-color: #eff0e2;">&nbsp;</th>
     </tr>

     <c:forEach items="${relatorios}" var="rel" step="1" begin="0" varStatus="status">
         <tr>
             <td style="background-color: #eff0e2;"><c:out value="${rel.titulo}" /></td>
             <td style="background-color: #eff0e2;"><c:out value="${fn:substring(rel.nome, 0, 30)}" /></td>
             <td style="background-color: #eff0e2;" align="center">
             <a href="<c:url value='/app/relatorio/${rel.idRelatorio}'/>">Sub-relatarios</a>&nbsp;&nbsp;
             <a href="javascript:param('${rel.idRelatorio}')">Par&acirc;metros</a>&nbsp;&nbsp;
             <a href="<c:url value='/app/relatorio/${rel.idRelatorio}'/>">Editar</a>&nbsp;&nbsp;
             <a href="javascript:excluir('${rel.idRelatorio}')">Excluir</a>&nbsp;&nbsp;
             <a href="javascript:gerar_pagina('${rel.idRelatorio}')">Gerar pagina</a>&nbsp;&nbsp;
             <a href="RelatorioJasper?id=${rel.idRelatorio}">Fontes</a></td>
         </tr>
     </c:forEach>

     </table>

     <input type="hidden" name="id" value="0" />
     <input type="hidden" name="op" value="" />
     <input type="hidden" name="id_mestre" value="" />

     </form>

 <script type="text/javascript">
 var form=document.FrmLista;
 function novo()
 {
     form.op.value='<%=Constantes.CADASTRO %>'
     form.submit();
 }

 function editar(i)
 {
     form.id.value=i;
     form.op.value='<%=Constantes.SELECIONAR %>';
     form.submit();
 }

 function excluir(i)
 {
     var sim=confirm('Deseja realmente excluir o relatorio?');
     if(sim){
         form.id.value=i;
         form.op.value='<%=Constantes.EXCLUIR %>';
         form.submit();
     }
 }

 function param(i)
 {
     form.id.value=i;
     form.op.value='<%=Constantes.PARAMETRO %>';
     form.submit();
 }

 function subrel(i)
 {
     form.id.value=i;
     form.id_mestre.value=i;
     form.op.value='<%=Constantes.SUBRELATORIO %>';
     form.submit();
 }

 function gerar_pagina(i)
 {
     form.id.value=i;
     form.id_mestre.value=i;
     form.action='gerarRelatorio.rel';
     form.op.value='<%=Constantes.GERAR_PAGINA %>';
     form.submit();
 }

 function gerar_relatorio_fonte(i)
 {
     form.id.value=i;
     form.id_mestre.value=i;
     form.action='gerarRelatorio.rel';
     form.op.value='<%=Constantes.GERAR_RELATORIO_FONTE %>';
     form.submit();
 }
 </script>
 </div>

 <div style="padding: 1px; background-color: #848668;">&nbsp;</div>

 </body>
 </html>

