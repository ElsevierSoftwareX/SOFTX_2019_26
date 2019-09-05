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

            <div class="container" style="width: 500px; border: 1px solid #ddd;">

                <form id="formularioProjeto" action="<c:url value='persiste' />" method="POST" >

                <h3 class="text-center"> <fmt:message key="view.projeto.novo"/></h3> 	

                <hr>

                <jsp:include page="../include/alerta.jsp"></jsp:include>

                    <div class="modal-body">

                        <input type="hidden" name="projeto.id" value="${projeto.id}">
                    <input type="hidden" id="editar" name="editar" value="${editar}">

                    <div class="row" style="margin-top: -20px;">
                        <div class="col-xs-12">
                            <label><fmt:message key="view.projeto.nome"/> <span class="required-class">*</span></label> 
                            <input type="text" class="form-control" name="projeto.nome" value="${projeto.nome}" maxlength="100" minlength="5" required>								
                        </div>
                    </div>
                    <div class="row" style="margin-top: 10px;">
                        <div class="col-xs-12">
                            <label for="campo3"><fmt:message key="view.projeto.descricao"/> <span class="required-class">*</span></label> 
                            <textarea  class="form-control" name="projeto.descricao" maxlength="250" minlength="5" required >${projeto.descricao}</textarea>				
                        </div>
                    </div>
                </div>

                <hr>


                <input id="botaosalvar" style="float: right;" type="submit" class="btn btn-success" value=<fmt:message key="cadastrar"/> />
                <input id="botaoeditar" style="float: right; display: none;" type="submit" class="btn btn-success" value=<fmt:message key="salvar"/> />
                <a
                    href="lista" class="btn btn-primary"> <span
                        class="glyphicon glyphicon-menu-left"></span> <fmt:message
                        key="voltar" />
                </a>
            </form>
        </div> 		 

        <jsp:include page="../include/script.jsp"></jsp:include>
        <script type="text/javascript">
            $(document).ready(function () {
                $('#menuinicial').show();
                $('#sdum').hide();
                $('#home').hide();
                $('#sair').show();
                if (document.getElementById('editar').value === "editar") {
                    //console.log("editar");
                    document.getElementById("botaosalvar").style.display = 'none';
                    document.getElementById("botaoeditar").style.display = 'block';
                } else {
                   // console.log("salvar: "+document.getElementById('editar').value);
                    document.getElementById("botaosalvar").style.display = 'block';
                    document.getElementById("botaoeditar").style.display = 'none';
                }
            });

          //  document.getElementById("userlogoff").on('click', function () {
                //document.getElementById("userlogoff").attr("aria-expanded", "true");
          //  });
        </script>
    </body>
</html>
