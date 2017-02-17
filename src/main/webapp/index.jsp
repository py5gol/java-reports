<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Relatórios WEB</title>
    </head>
    <body>
        <h1>Relatórios WEB</h1>
        Sistema de controle e geração de relatórios.<br/>
        <c:url var="link1" value="/relatorio.rel"></c:url>
        <a href="${link1}">Cadastro de relatórios</a>
    </body>
</html>
