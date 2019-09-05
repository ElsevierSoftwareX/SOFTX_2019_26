<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- LISTA DE amostras CADASTRADOS -->
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title> <fmt:message key="nome.projeto"/> - <fmt:message key="view.login.titulo"/> </title>

        <jsp:include page="../include/link.jsp"></jsp:include>
        </head>

        <body>

        <jsp:include page="../menu/menu.jsp"></jsp:include>

            <div class="container">

                <h3 class="text-center">
                <fmt:message key="view.lista.amostras"/>
            </h3>

            <hr style="margin-bottom: 5px; margin-top: 5px;">

            <div class="row">	
                <div class="col-md-12">
                    <a href="<c:url value='/amostra/formulario?id=${idArea}'/>" class="btn btn-block btn-default">
                        <spam class="glyphicon glyphicon-download-alt"> </spam>
                            <fmt:message key="view.lista.amostra.cadastrada.novo.amostra" />
                    </a>
                </div>
            </div>	

            <hr>				

            <jsp:include page="../include/alerta.jsp"></jsp:include>

            <%-- 
            <form id="formularioFiltroAmostra" action="<c:url value='filtro' />" method="GET" >
                    <div class="row" style="margin-bottom: 10px;">
                            <div class="col-md-12">
                                    <div class="input-group"> 		
                                            <input id="filtroProjeto" type="text" class="form-control" name="filtro" value="${filtro}" placeholder="<fmt:message key="view.filtro.nome.projeto" /> ">
                                            <span class="input-group-addon btn-primary" style="color: #FFF;" onClick="document.forms['formularioFiltroProjeto'].submit();"> 
                                                    <span class="glyphicon glyphicon-search"></span> 
                                                    <fmt:message key="view.filtrar" /> 
                                            </span>
                                    </div>
                            </div>
                    </div>
            </form> --%>	

            <div class="table-responsive"> 
                <table class="table table-hover table-bordered ">

                    <thead> 
                        <tr> 
                            <th class="text-center"> <fmt:message key="view.amostra.descricao" /> </th> 
                            <th class="text-center"> <fmt:message key="view.amostra.data" /> </th> 
                            <th class="text-center"> <fmt:message key="view.opcao" /></th> 
                        </tr> 
                    </thead> 

                    <tbody id="bodyListaAmostra"> 

                        <c:forEach items="${listaAmostra}" var="am">
                            <tr>
                                <td class="text-left">   ${fn:substring(am.descricao, 0, 50)} </td>
                                <td class="text-center"> <fmt:formatDate pattern="dd/MM/yyyy" value="${am.dataCadastro}" /> </td>
                                <td class="text-center"> 


                                    <a href="editar?id=${am.id}">
                                        <span class="btn btn-warning btn-xs">	
                                            <span class="glyphicon glyphicon-pencil" ></span>
                                            <fmt:message key="view.lista.amostra.editar" />
                                        </span>
                                    </a>	

                                    <a href="<c:url value='exportartxt?id=${am.id}'/>" class="btn btn-default btn-xs">
                                            <span class="glyphicon glyphicon-export" > </span>
                                                <fmt:message key="lista.exportar" />
                                        </a>    
                                        
                                    <div class="btn btn-xs btn-danger modalExcluir" id="${am.id}" data-codigo="${am.codigo}"
                                         data-titulo="<fmt:message key="view.amostra.excluir.titulo"/> ${am.descricao}"
                                         data-mensagem=" <fmt:message key="view.amostra.excluir.mensagem"/>"
                                         data-excluir=" <fmt:message key="view.amostra.excluir"/> ">
                                        <span class="glyphicon glyphicon-trash"></span>
                                        <fmt:message key="view.lista.amostra.excluir" />
                                    </div>

                                    <a href="sincronizar?id=${am.id}">
                                        <span class="btn btn-primary btn-xs">	
                                            <span class="glyphicon glyphicon-cloud-upload" ></span>
                                            <fmt:message key="menu.projeto.sincronizar" />
                                        </span>
                                    </a>

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

                    if (this.attributes.getNamedItem('data-codigo').value === "") {
                        document.getElementById("checkapi").style.display = 'none';
                    } else {
                        document.getElementById("checkapi").style.display = 'block';
                    }
                    $('#modalExcluirTitulo').text(this.attributes.getNamedItem('data-titulo').value);
                    $('#modalExcluirMensagem').text(this.attributes.getNamedItem('data-mensagem').value);
                    var msg = "sim";
                    $('#teste').html(' <input type="checkbox" name="salvarAPI" id="salvarAPI" style="margin-top: 0px;" value="nao" onchange="verificaChecks()"> ');
                    $('#excluirConfirmar1').html('<a class="btn btn-success" href="<c:url value="excluir?id='+ this.attributes.getNamedItem('id').value+'&excluirapi='+$(salvarAPI).val()+'"/>">' + this.attributes.getNamedItem('data-excluir').value + '</a> ');
                    $('#excluirConfirmar2').html('<a class="btn btn-success" href="<c:url value="excluir?id='+this.attributes.getNamedItem('id').value+'&excluirapi='+msg+'"/>">' + this.attributes.getNamedItem('data-excluir').value + ' </a> ');             
                    //$('#excluirConfirmar').html('<a class="btn btn-success" href="<c:url value="excluir?id='+ this.attributes.getNamedItem('id').value + '"/>">' + this.attributes.getNamedItem('data-excluir').value + '</a> ');
                    $('#myModalExcluir').modal('show');

                });
                
                function verificaChecks() {
                    if (document.getElementById("salvarAPI").value === "nao") {
                        document.getElementById("salvarAPI").value = "sim";
                        document.getElementById("excluirConfirmar1").style.display = 'none';
                        document.getElementById("excluirConfirmar2").style.display = 'block';
                    } else {
                        document.getElementById("salvarAPI").value = "nao";
                        document.getElementById("excluirConfirmar2").style.display = 'none';
                        document.getElementById("excluirConfirmar1").style.display = 'block';
                    }
                }
        </script>

    </body>
</html>