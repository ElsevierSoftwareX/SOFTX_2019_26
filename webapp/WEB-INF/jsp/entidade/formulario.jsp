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

                <form id="formularioentidade" action="<c:url value='persiste' />" method="POST" >
                <input type="hidden" value="${idProjeto}" name="idProjeto" id="idProjeto">
                <input type="hidden" id="editar" name="editar" value="${editar}">
                <h4 class="text-center"> <fmt:message key="view.entidade.novo"/></h4> 	

                <hr>

                <jsp:include page="../include/alerta.jsp"></jsp:include>

                    <input type="hidden" name="entidade.codigo"     value="${entidade.codigo}">

                <div class="row">
                    <div class="col-md-12">
                        <label>
                            <fmt:message key="view.entidade.descricao" /> 
                            <span class="required-class">*</span>
                        </label>
                        <input type="text" class="form-control" name="entidade.descricao" value="${entidade.descricao}"> 			 			
                    </div>
                    <div class="col-md-12">
                        <label>
                            <fmt:message key="view.entidade.cidade" /> 
                            <span class="required-class">*</span>
                        </label>
                        <input type="text" class="form-control" name="entidade.cidade" value="${entidade.cidade}"> 			 			
                    </div>
                    <div class="col-md-12">
                        <label>
                            <fmt:message key="view.entidade.estado" /> 
                            <span class="required-class">*</span>
                        </label>
                        <input type="text" class="form-control" name="entidade.estado" value="${entidade.estado}" maxlength="3"> 			 			
                    </div>
                </div>

                <hr>

                <input  id="botaosalvar" style="float: right;" type="submit" class="btn btn-success" value=<fmt:message key="cadastrar"/> />
                <input id="botaoeditar" style="float: right; display: none;" type="submit" class="btn btn-success" value=<fmt:message key="salvar"/> />
                <a href="lista?idProjeto=${idProjeto}" class="btn btn-primary"> 
                    <span class="glyphicon glyphicon-menu-left"></span>
                    <fmt:message key="voltar"/>
                </a>
            </form>
        </div>
        <script type="text/javascript">
            $(document).ready(function () {
                if (document.getElementById('editar').value === "editar") {
                    document.getElementById("botaosalvar").style.display = 'none';
                    document.getElementById("botaoeditar").style.display = 'block';
                } else {
                    document.getElementById("botaosalvar").style.display = 'block';
                    document.getElementById("botaoeditar").style.display = 'none';
                }
            });

            document.getElementById("menuinicial").style.display = 'block';
        </script>	


        <jsp:include page="../include/script.jsp"></jsp:include>

    </body>
</html>