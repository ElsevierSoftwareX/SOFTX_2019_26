<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title> <fmt:message key="nome.projeto"/> - <fmt:message key="view.classificador"/> </title>

        <jsp:include page="../include/link.jsp"></jsp:include>
        </head>

        <body>
            <input type="hidden" value="${idProjeto}" name="idProjeto" id="idProjeto">
        <jsp:include page="../menu/menu.jsp"></jsp:include>

            <div class="container">

                <h3 class="text-center">
                <fmt:message key="view.lista.classificador"/>
            </h3>

            <hr style="margin-bottom: 5px; margin-top: 5px;">

            <div class="row">	
                <div class="col-md-12">
                    <a href="<c:url value='/classificacao/formulario?idProjeto=${idProjeto}'/>" class="btn btn-block btn-default">
                        <spam class="glyphicon glyphicon-download-alt"> </spam>
                            <fmt:message key="view.lista.classificador.cadastrada.novo.classificador" />
                    </a>
                </div>
            </div>	

            <hr>				

            <jsp:include page="../include/alerta.jsp"></jsp:include>


                <div class="table-responsive"> 
                    <table class="table table-hover table-bordered ">

                        <thead> 
                            <tr> 
                                <th class="text-center"> <fmt:message key="view.classificador.codigo" /> </th>
                            <th class="text-center"> <fmt:message key="view.classificador.atributo" /> </th>
                            <th class="text-center"> <fmt:message key="view.classificador.solo" /> </th> 
                            <th class="text-center"> <fmt:message key="view.entidade.descricao.class" /> </th> 
                            <th class="text-center"> <fmt:message key="view.opcao" /></th> 
                        </tr> 
                    </thead> 

                    <tbody id="bodyListaClassificador"> 

                        <c:forEach items="${listaClassificador}" var="lc">
                            <tr>
                                <td class="text-center"> ${lc.codigo} </td>
                                <td class="text-center"> ${fn:substring(lc.atributo.descricaoPT, 0, 50)} </td>
                                <td class="text-center"> ${fn:substring(lc.tipoSolo.descricaoPT, 0, 50)} </td>
                                <td class="text-center"> ${fn:substring(lc.entidade.descricao, 0, 50)} </td>
                                <td class="text-center"> 

                                    <%-- <a href="editar?id=${am.id}">
                                            <span class="btn btn-warning btn-xs">	
                                                    <span class="glyphicon glyphicon-pencil" ></span>
                                                    <fmt:message key="view.lista.classificador.editar" />
                                            </span>
                                    </a>	 --%>

                                    <div class="btn btn-xs btn-danger modalExcluir" id="${lc.codigo}"
                                         data-titulo="<fmt:message key="view.classificador.excluir.titulo"/> ${lc.codigo}"
                                         data-mensagem=" <fmt:message key="view.classificador.excluir.mensagem"/>"
                                         data-excluir=" <fmt:message key="view.classificador.excluir"/> ">
                                        <span class="glyphicon glyphicon-trash"></span>
                                        <fmt:message key="view.lista.classificador.excluir" />
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody> 
                </table>   				     				  
            </div>	

        </div> 		 

        <jsp:include page="../include/script.jsp"></jsp:include>
        <jsp:include page="../modal/excluir.jsp"></jsp:include>

            <script type="text/javascript">
                document.getElementById("menuinicial").style.display = 'block';
                $(".modalExcluir").on('click', function () {

                    $('#modalExcluirTitulo').text(this.attributes.getNamedItem('data-titulo').value);
                    $('#modalExcluirMensagem').text(this.attributes.getNamedItem('data-mensagem').value);

                    $('#excluirConfirmar1').html('<a class="btn btn-success" href="<c:url value="excluir?codigo='+ this.attributes.getNamedItem('id').value +'&idProjeto='+$(idProjeto).val()+'"/>">' + this.attributes.getNamedItem('data-excluir').value + '</a> ');
                    $('#myModalExcluir').modal('show');

                });

        </script>

    </body>
</html>