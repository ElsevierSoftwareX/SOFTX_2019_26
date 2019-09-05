<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- LISTA DE SELECAO DE SENSORES CADASTRADOS -->
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title> <fmt:message key="nome.projeto"/> - <fmt:message key="view.login.titulo"/> </title>

        <jsp:include page="../include/link.jsp"></jsp:include>
        </head>

        <body>
            <input type="hidden" name="gradeAmostral" value="${gradeAmostral}" id=gradeAmostral />
           

        <jsp:include page="../menu/menu.jsp"></jsp:include>

            <div class="container">

                <h3 class="text-center">
                <fmt:message key="view.lista.selecaosensores"/>
            </h3>

            <hr style="margin-bottom: 5px; margin-top: 5px;">

            <div class="row">	
                <div class="col-md-12">
                    <a href="<c:url value='/selecaosensor/formulario?id=${idArea}'/>" class="btn btn-block btn-default">
                        <spam class="glyphicon glyphicon-download-alt"> </spam>
                            <fmt:message key="view.lista.selecaosensor.cadastrado.novo.selecaosensor" />
                    </a>
                </div>
            </div>	

            <hr>				

            <jsp:include page="../include/alerta.jsp"></jsp:include>


                <div class="table-responsive"> 
                    <table class="table table-hover table-bordered ">
                        <thead> 
                            <tr> 
                                <th class="text-center"> <fmt:message key="view.selecaosensor.descricao" /> </th> 
                            <th class="text-center"> <fmt:message key="view.opcao" /></th> 
                        </tr> 
                    </thead> 

                    <tbody id="bodyListaSelecaoSensor"> 

                        <c:forEach items="${listaSelecaoSensor}" var="ls">
                            <tr>
                                <td class="text-left">   ${fn:substring(ls.descricao, 0, 50)} </td>
                                <td class="text-center"> 


                                    <a href='<c:url value="visualizar?id=${ls.id}" />' > 
                                        <span class="btn btn-warning btn-xs">	
                                            <span class="glyphicon glyphicon-eye-open"></span>
                                            <fmt:message key="visualizar"/>
                                        </span>
                                    </a>

                                    <a href="<c:url value='exportartxt?id=${ls.id}'/>" class="btn btn-default btn-xs">
                                        <span class="glyphicon glyphicon-export" > </span>
                                        <fmt:message key="lista.exportar" />
                                    </a>   

                                    <div class="btn btn-xs btn-danger modalExcluir" id="${ls.id}"
                                         data-titulo="<fmt:message key="view.selecaosensor.excluir.titulo"/> ${ls.descricao}"
                                         data-mensagem=" <fmt:message key="view.selecaosensor.excluir.mensagem"/>"
                                         data-excluir=" <fmt:message key="view.selecaosensor.excluir"/> ">
                                        <span class="glyphicon glyphicon-trash"></span>
                                        <fmt:message key="view.lista.selecaosensor.excluir" />
                                    </div>

                                </td>
                            </tr>
                        </c:forEach>

                    </tbody> 
                </table>   	

                <hr>

                <a href='<c:url value="/projeto/area/lista?id=${idProjeto}" />'" class="btn btn-primary"> 
                    <span class="glyphicon glyphicon-menu-left"></span>
                    <fmt:message key="voltar"/>
                </a>
            </div>	

        </div> 		 

        <jsp:include page="../include/script.jsp"></jsp:include>
        <jsp:include page="../modal/excluir.jsp"></jsp:include>

            <script type="text/javascript">
                
                document.getElementById("menuinicial").style.display = 'block';

                $(".modalExcluir").on('click', function () {

                    $('#modalExcluirTitulo').text(this.attributes.getNamedItem('data-titulo').value);
                    $('#modalExcluirMensagem').text(this.attributes.getNamedItem('data-mensagem').value);

                    $('#excluirConfirmar1').html('<a class="btn btn-success" href="<c:url value="excluir?id='+ this.attributes.getNamedItem('id').value + '"/>">' + this.attributes.getNamedItem('data-excluir').value + '</a> ');
                    $('#myModalExcluir').modal('show');

                });
        </script>

    </body>
</html>