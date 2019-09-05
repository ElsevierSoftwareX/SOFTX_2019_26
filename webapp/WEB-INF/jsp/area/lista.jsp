<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="<c:url value='/resources/dist/jquery.min.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/ferramenta/bootstrap.min.js' />"></script>

        <title><fmt:message key="nome.projeto" /> - <fmt:message key="view.login.titulo" /></title>

        <jsp:include page="../include/link.jsp"></jsp:include>

        </head>
        <body>
            <input type="hidden" value="${idUsuario}" name="idUsuario" id="idUsuario">
        <input type="hidden" value="${idProjeto}" name="idProjeto" id="idProjeto">

        <jsp:include page="../menu/menu.jsp"></jsp:include>

            <div class="container">

                <h3 class="text-center"> <fmt:message key="view.lista.area" /> </h3>

            <hr style="margin-bottom: 5px; margin-top: 5px;">

            <div class="row">	
                <div class="col-md-3">
                    <div class="btn btn-block btn-default" data-toggle="modal" id="chamaModalBuscaArea">
                        <span class="glyphicon glyphicon-download-alt"> </span>
                        <fmt:message key="view.lista.area.cadastrada.area.importar" />
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="btn btn-block btn-default" data-toggle="modal" id="chamaModalNovaArea">
                        <span class="glyphicon glyphicon-file"> </span>
                        <fmt:message key="view.lista.area.cadastrada.area.importarTXT" />
                    </div>
                </div>	
                <div class="col-md-3">	
                    <a class="btn btn-block btn-default" href="formulario?id=${idProjeto}">				
                        <i class="glyphicon glyphicon-pencil" > </i>
                        <fmt:message key="view.lista.area.cadastrada.area.manualmente" />
                    </a>
                </div>
                <div class="col-md-3">	
                    <div class="btn btn-block btn-default" data-toggle="modal" id="chamaModalSincronizar">				
                        <span class="glyphicon glyphicon-cloud-upload" > </span>
                        <fmt:message key="view.lista.area.sincronizar" />
                    </div>
                </div>
            </div>	

            <hr>					

            <jsp:include page="../include/alerta.jsp"></jsp:include>

            <%-- 
            <form id="formularioFiltroArea" action="<c:url value='filtro' />" method="GET">
                    <div class="row" style="margin-bottom: 10px;">
                            <div class="col-md-12">
                                    <div class="input-group">
                                            <input id="filtroArea" type="text" class="form-control" name="filtro" value="${filtro}" placeholder="<fmt:message key="view.filtro.nome.area" /> ">
                                            <span class="input-group-addon btn-primary" style="color: #FFF;" onClick="document.forms['formularioFiltroArea'].submit();">
                                                    <span class="glyphicon glyphicon-search"></span> <fmt:message key="view.filtrar" />
                                            </span>
                                    </div>
                            </div>
                    </div>
            </form> --%>

            <div class="table-responsive">
                <table class="table table-hover table-bordered ">
                    <thead>
                        <tr>
                            <th class="text-center"><fmt:message key="view.area.nome.area" /></th>
                            <th class="text-center"><fmt:message key="view.datacadastro" /></th>
                            <th class="text-center"><fmt:message key="view.opcao" /></th>
                        </tr>
                    </thead>
                    <tbody id="bodyListaArea">
                        <c:forEach items="${listaAreaProjeto}" var="ap">
                            <tr>
                                <td class="text-left">${ap.descricao}</td>
                                <td class="text-center"> <fmt:formatDate pattern="dd/MM/yyyy" value="${ap.dataCadastro}" /> </td>
                                <td class="text-center">


                                    <a href='<c:url value="/selecaosensor/lista?id=${ap.id}" />'"> 
                                        <span class="btn btn-xs btn-primary">
                                            <span class="glyphicon  glyphicon-list"></span>
                                            <fmt:message key="view.area.lista.selecaoensor" />
                                    </a>

                                    <a href='<c:url value="/amostra/lista?id=${ap.id}" />'"	>
                                        <span class="btn btn-xs btn-primary">	
                                            <span class="glyphicon glyphicon-list" ></span>
                                            <fmt:message key="view.area.lista.amostra" />
                                        </span>
                                    </a>

                                    <a href='<c:url value="/gradeamostral/lista?id=${ap.id}" />'"	>
                                        <span class="btn btn-xs btn-primary">	
                                            <span class="glyphicon glyphicon-list" ></span>
                                            <fmt:message key="view.area.lista.grade" />
                                        </span>
                                    </a>

                                    <%-- <a href='<c:url value="/grade/lista?id=${ap.id}" />'"	>
                                            <span class="btn btn-xs btn-primary">	
                                                    <span class="glyphicon glyphicon-list" ></span>
                                                    <fmt:message key="view.area.lista.grade" />
                                            </span>
                                    </a> --%>
                                        
                                    <a href="editar?id=${ap.id}"	>
                                        <span class="btn btn-warning btn-xs">	
                                            <span class="glyphicon glyphicon-pencil" ></span>
                                            <fmt:message key="view.lista.area.editar" />
                                        </span>
                                    </a>	
                                        
  
                                        <a href="<c:url value='exportartxt?id=${ap.id}'/>" class="btn btn-default btn-xs">
                                            <span class="glyphicon glyphicon-export" > </span>
                                                <fmt:message key="lista.exportar" />
                                        </a>
                 

                                    <div class="btn btn-xs btn-danger btn-md modalExcluir" id="${ap.id}" data-codigo="${ap.codigo}"
                                         data-titulo="<fmt:message key="view.area.excluir.titulo"/> ${ap.descricao}"
                                         data-mensagem=" <fmt:message key="view.area.excluir.mensagem"/>"
                                         data-excluir=" <fmt:message key="view.area.excluir"/> ">
                                        <span class="glyphicon glyphicon-trash"></span>
                                        <fmt:message key="view.lista.area.excluir" />
                                        
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>

                    </tbody>
                </table>

                <hr>
                <a href='<c:url value="/projeto/lista" />' class="btn btn-primary"> 
                    <span class="glyphicon glyphicon-menu-left"></span>
                    <fmt:message key="voltar"/>
                </a>
            </div>
        </div>

        <jsp:include page="../include/script.jsp"></jsp:include>
        <jsp:include page="../modal/cadastrarAreaServer.jsp"></jsp:include>
        <jsp:include page="../modal/sincronizarServer.jsp"></jsp:include>
        <jsp:include page="../modal/cadastrarNovaAreaTXT.jsp"></jsp:include>
        <jsp:include page="../modal/excluir.jsp"></jsp:include>

            <script type="text/javascript">
                document.getElementById("menuinicial").style.display = 'block';
                 $(document).ready(function () {
                      document.getElementById("menuinicial").style.display = 'block';
                 });
                
                $('#chamaModalBuscaArea').on('click', function () {
                    $('#modalBuscaArea').modal('show');
                });

                $('#chamaModalSincronizar').on('click', function () {
                    $('#modalSincronizar').modal('show');
                });

                $('#chamaModalNovaArea').on('click', function () {
                    $('#modalNovaArea').modal('show');
                });


                $(".modalExcluir").on('click', function () {
                    if (this.attributes.getNamedItem('data-codigo').value === "") {
                        document.getElementById("checkapi").style.display = 'none';
                    } else {
                        document.getElementById("checkapi").style.display = 'block';
                    }
                    var msg = "sim";
                    $('#teste').html(' <input type="checkbox" name="salvarAPI" id="salvarAPI" style="margin-top: 0px;" value="nao" onchange="verificaChecks()"> ');
                    $('#modalExcluirTitulo').text(this.attributes.getNamedItem('data-titulo').value);
                    $('#modalExcluirMensagem').text(this.attributes.getNamedItem('data-mensagem').value);
                    $('#excluirConfirmar1').html('<a class="btn btn-success" href="<c:url value="excluir?id='+ this.attributes.getNamedItem('id').value+'&excluirapi='+$(salvarAPI).val()+'"/>">' + this.attributes.getNamedItem('data-excluir').value + '</a> ');
                    $('#excluirConfirmar2').html('<a class="btn btn-success" href="<c:url value="excluir?id='+this.attributes.getNamedItem('id').value+'&excluirapi='+msg+'"/>">' + this.attributes.getNamedItem('data-excluir').value + ' </a> ');
                    //$('#excluirConfirmar').html('<a class="btn btn-success" href="<c:url value="excluir?id='+ this.attributes.getNamedItem('id').value+'"/>">' + this.attributes.getNamedItem('data-excluir').value + '</a> ');
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