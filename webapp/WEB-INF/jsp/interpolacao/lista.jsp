<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- LISTA DE PROJETOS CADASTRADOS -->
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
       
        <title> <fmt:message key="nome.projeto"/> - <fmt:message key="view.interpolacao"/> </title>

        <jsp:include page="../include/link.jsp"></jsp:include>
        </head>

        <body>

        <jsp:include page="../menu/menu.jsp"></jsp:include>
      
        <input type="hidden" value="${idProjeto}" name="idProjeto" id="idProjeto">
        <div class="container">

            <h3 class="text-center"><fmt:message key="view.lista.interpolacao"/></h3>

            <hr style="margin-bottom: 5px; margin-top: 5px;">

            <div class="row">	
                <div class="col-md-12">
                    <a href="<c:url value='/interpolacao/formulario?idProjeto=${idProjeto}'/>" class="btn btn-block btn-default">
                        <spam class="glyphicon glyphicon-file"> </spam>
                            <fmt:message key="view.lista.interpolacao.cadastrada.novo.interpolacao" />
                    </a>
                </div>
            </div>	

            <hr>
              <jsp:include page="../include/alerta.jsp"></jsp:include>
            <div  id="divFormKRI">

                <div class="table"> 
                    <table class="table table-hover table-bordered ">

                        <thead> 
                            <tr> 
                                <th class="text-left col-md-2">   <fmt:message key="view.interpolacao.nome" /> </th> 
                                <th class="text-center col-md-2"> <fmt:message key="view.interpolacao.data" /> </th>
                                <th class="text-center col-md-1"> <fmt:message key="view.interpolacao.interpolador" /> </th>
                                <th class="text-center col-md-2"> <fmt:message key="view.interpolacao.xy" /> </th>
                                <th class="text-center col-md-1"> <fmt:message key="view.interpolacao.area" /> </th>
                                <th class="text-center col-md-2"> <fmt:message key="view.interpolacao.amostra" /> </th> 
                                <th class="text-center col-md-1"> <fmt:message key="view.interpolacao.geoestatistica" /> </th>
                                <th COLSPAN="2" class="text-center"> <fmt:message key="view.opcao" /></th> 
                            </tr> 
                        </thead> 

                        <div id="bodyListaInterpolacao" overflow: auto> 
                            <c:forEach items="${interpolacaokri}" var="i">
                                <tr>
                                    <td  class="text-left col-md-2"> ${fn:substring(i.descricao, 0, 50)} </td>
                                    <td class="text-center col-md-2">  <fmt:formatDate pattern="dd/MM/yyyy" value="${i.dataCriacao}" /> </td>
                                    <td class="text-center col-md-1"> ${i.tipoInterpolador} </td>
                                    <td class="text-center col-md-2"> ${i.tamanhoX} / ${i.tamanhoY} </td>
                                    <td class="text-center col-md-1"> ${fn:substring(i.area.descricao, 0, 30)} </td>
                                    <td class="text-center col-md-2"> ${fn:substring(i.amostra.descricao,0,30)} </td>

                                    <td class="text-center col-md-1"> <div class="btn btn-warning btn-xs text-center geoestat" id="${i.ice},${i.contribuicao},${i.alcance},${i.kappa},${i.isi},${i.standardDeviationdMeanError},${i.meanError},${i.usuario.id},${i.todossemivariogramas},${i.melhorsemivariograma},${i.metodo},${i.modelo}"  >
                                            <span class= "glyphicon glyphicon-eye-open"></span>	 
                                            <fmt:message key="visualizar" />	
                                        </div>
                                    </td>
                                    
                                          <td class="text-center"> 
                                        <a href="<c:url value='exportartxt?id=${i.id}'/>" class="btn btn-default btn-xs">
                                            <spam class="glyphicon glyphicon-export"> </spam>
                                                <fmt:message key="lista.exportar" />
                                        </a>
                                          </td>
                                          <td class="text-center">
                                        <div class="btn btn-danger btn-xs modalExcluir" id="${i.id}" 
                                             data-titulo="<fmt:message key="view.descricao.excluir.titulo"/> ${i.descricao}"
                                             data-mensagem="<fmt:message key="view.descricao.excluir.mensagem"/>"
                                             data-excluir="<fmt:message key="view.descricao.excluir"/>">
                                            <span class= "glyphicon glyphicon-trash"></span>	 
                                            <fmt:message key="lista.descricao.excluir" />						
                                        </div>
                                         
                                    </td>
                                </tr>
                            </c:forEach>

                            <c:forEach items="${interpolacaoidw}" var="i">
                                <tr>
                                    <td  class="text-left col-md-2"> ${fn:substring(i.descricao, 0, 50)} </td>
                                    <td class="text-center col-md-2"> <fmt:formatDate pattern="dd/MM/yyyy" value="${i.dataCriacao}" /> </td>
                                    <td class="text-center col-md-1"> ${i.tipoInterpolador} </td>
                                    <td class="text-center col-md-2"> ${i.tamanhoX} / ${i.tamanhoY} </td>
                                    <td class="text-center col-md-1"> ${fn:substring(i.area.descricao, 0, 30)} </td>
                                    <td class="text-center col-md-2"> ${fn:substring(i.amostra.descricao,0,30)} </td>
                                    <td class="text-center col-md-1"> <div class="btn btn-warning btn-xs text-center" >
                                            <span class= "glyphicon glyphicon-warning-sign"></span>	 
                                            <fmt:message key="visualizar.naoseaplica" />	
                                        </div>
                                    </td>
                                    
                                    <td class="text-center">    
                                        <a href="<c:url value='exportartxt?id=${i.id}'/>" class="btn btn-default btn-xs">
                                            <span class="glyphicon glyphicon-export"> </span>
                                                <fmt:message key="lista.exportar" />
                                        </a>
                                    </td>
                                   <td class="text-center">
                                        <div class="btn btn-danger btn-xs modalExcluir" id="${i.id}" 
                                             data-titulo="<fmt:message key="view.descricao.excluir.titulo"/> ${i.descricao}"
                                             data-mensagem="<fmt:message key="view.descricao.excluir.mensagem"/>"
                                             data-excluir="<fmt:message key="view.descricao.excluir"/>">
                                            <span class= "glyphicon glyphicon-trash"></span>	 
                                            <fmt:message key="lista.descricao.excluir" />						
                                        </div>
                                      
                                    </td>
                                </tr>
                            </c:forEach> 
                        </div> 
                    </table>   				     				  
                </div>	
            </div>


        </div> 		 

        <jsp:include page="../include/script.jsp"></jsp:include>
        <jsp:include page="../modal/excluir.jsp"></jsp:include>
        <jsp:include page="../modal/semivariograma.jsp"></jsp:include>

            <script type="text/javascript">
                document.getElementById("menuinicial").style.display = 'block';
                $(".modalExcluir").on('click', function () {

                    $('#modalExcluirTitulo').text(this.attributes.getNamedItem('data-titulo').value);
                    $('#modalExcluirMensagem').text(this.attributes.getNamedItem('data-mensagem').value);

                    $('#excluirConfirmar1').html('<a class="btn btn-success" href="<c:url value="excluir?id='+ this.attributes.getNamedItem('id').value +'&idProjeto='+$(idProjeto).val()+'"/>">' + this.attributes.getNamedItem('data-excluir').value + '</a> ');

                    $('#myModalExcluir').modal('show');
                });

                $(".geoestat").on('click', function () {
                    var id = $(this).attr('id');
                    var res = id.split(",");
                    $('#modalice').text(res[0]);
                    $('#modalcontribuicao').text(res[1]);
                    $('#modalalcance').text(res[2]);
                    $('#modalkappa').text(res[3]);
                    $('#modalisi').text(res[4]);
                    $('#modalstandard').text(res[5]);
                    $('#modalmean').text(res[6]);
                    $('#modalmetodo').text(res[10]);
                    $('#modalmodelo').text(res[11]);
                    $('#mostrarimagem').html('<img src="${pageContext.servletContext.contextPath}/file/' + res[7] + '/' + res[8] + '" width="425"/>');
                    $('#mostrarmelhorsemi').html('<img src="${pageContext.servletContext.contextPath}/file/' + res[7] + '/' + res[9] + '" width="425" />');
                    $('#myModalSemivariograma').modal('show');
                });


                function formatData(data) {

                    var d = new Date(data);

                    return ("00" + d.getDate()).slice(-2) + "/" + ("00" + (d.getMonth() + 1)).slice(-2) + "/" + d.getFullYear() + " " +
                            ("00" + d.getHours()).slice(-2) + ":" + ("00" + d.getMinutes()).slice(-2);
                }

        </script>

    </body>
</html>