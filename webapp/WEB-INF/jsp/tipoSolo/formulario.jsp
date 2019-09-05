<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="<c:url value='/resources/dist/jquery.min.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/ferramenta/bootstrap.min.js' />"></script>

        <title> <fmt:message key="nome.projeto"/> - <fmt:message key="view.login.titulo"/> </title>

        <jsp:include page="../include/link.jsp"></jsp:include>

        </head>
        <body>

        <jsp:include page="../menu/menu.jsp"></jsp:include>

            <div class="container" style="border: 1px solid #ddd; width: 500px;">

                <form id="formularioImovel" action="<c:url value='persiste' />" method="POST" >

                <h4 class="text-center"> <fmt:message key="view.solo.novo"/></h4> 	

                <hr>

                <jsp:include page="../include/alerta.jsp"></jsp:include>

                    <input type="hidden" name="tipoSolo.id"     value="${tipoSolo.id}">
                <input type="hidden" name="tipoSolo.codigo" value="${tipoSolo.codigo}">
                <input type="hidden" value="${idUsuario}" name="idUsuario" id="idUsuario" >

                <div class="row">
                    <div class="col-md-12">
                        <label>
                            <fmt:message key="view.solo.descricao" /> 
                            <span class="required-class">*</span>
                        </label>
                        <input type="text" class="form-control" name="tipoSolo.descricaoPT" value="${tipoSolo.descricaoPT}"> 			 			
                    </div>
                </div>

                <hr>

                <input style="float: right;" type="submit" class="btn btn-success" value=<fmt:message key="cadastrar"/>>

            </form>
        </div>
        <script type="text/javascript">
            document.getElementById("menuinicial").style.display = 'block';
        </script>	

        <jsp:include page="../include/script.jsp"></jsp:include>

    </body>
</html>