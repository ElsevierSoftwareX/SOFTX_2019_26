<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!-- LISTA DE GRADES AMOSTRAIS CADASTRADAS -->
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title><fmt:message key="nome.projeto" /> - <fmt:message
                key="view.login.titulo" /></title>

        <jsp:include page="../include/link.jsp"></jsp:include>
        </head>

        <body>

        <jsp:include page="../menu/menu.jsp"></jsp:include>

            <div class="container">

                <h3 id="h3" class="text-center">
                <fmt:message key="view.lista.grades" />
            </h3>

            <hr style="margin-bottom: 5px; margin-top: 5px;">

            <div class="row">
                <div class="col-md-12">
                    <a id="titulo" href="<c:url value='/gradeamostral/formulario?id=${idArea}'/>" class="btn btn-block btn-default"> 
                        <spam class="glyphicon glyphicon-download-alt"> </spam> 
                            <fmt:message key="view.lista.gradeamostral.cadastrada.novo" />
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



            <form action="<c:url value='adicionaramostra' />" method="POST">

                <input type="hidden" name="formCriaAmostra" value="false"
                       id="controleCriaAmostra">
                <input type="hidden" name="gradeAmostral.id" value="" id="controleIdGradeAmostral">
                <input type="hidden" name="gradeAmostral.area.id" value="${idArea}" /> 

                <div id="divFormCriaAmostra" style="display: none;">

                    <h4 class="text-center">
                        <fmt:message key="view.cadastrar.amostra.apartir.grade" />
                    </h4>

                    <hr style="margin-bottom: 5px; margin-top: 5px;">

                    <div class="row">
                        <div class="col-md-12">
                            <label> <fmt:message key="view.area.amostra.descricao" />
                                <span class="required-class"> * </span>
                            </label> <input type="text" class="form-control" name="amostra.descricao"
                                            value="${amostra.descricao}" maxlength="100" minlength="4"
                                            required>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <label> <fmt:message key="view.area.amostra.atributo" />
                                <span class="required-class"> * </span>
                            </label> <select name="amostra.atributo.codigo" class="form-control">
                                <c:forEach items="${listaAtributo}" var="a">
                                    <option value="${a.codigo}"
                                            <c:if test="${a.codigo eq amostra.atributo.codigo}">selected="selected"</c:if>>${a.descricaoPT}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <hr>
                    <input style="float: right;" type="submit" class="btn btn-success"
                           value=<fmt:message key="cadastrar"/> />

                    <hr>
                    <div class="btn btn-primary" id="buttonVoltarListaGrades" >
                        <span class="glyphicon glyphicon-menu-left"></span> <fmt:message key="voltar" />
                    </div>

                </div>
            </form>


            <div class="table-responsive" id="principal">
                <table class="table table-hover table-bordered ">

                    <thead>
                        <tr>
                            <th class="text-center"><fmt:message
                                    key="view.grade.descricao" /></th>
                            <th class="text-center"><fmt:message key="view.grade.data" />
                            </th>
                            <th class="text-center"><fmt:message key="view.opcao" /></th>
                        </tr>
                    </thead>

                    <tbody id="bodyListaGradeAmostral">

                        <c:forEach items="${listaGradeAmostral}" var="am">
                            <tr>
                                <td class="text-left">${fn:substring(am.descricao, 0, 60)}</td>
                                <td class="text-center"><fmt:formatDate pattern="dd/MM/yyyy"
                                                value="${am.dataCadastro}" /></td>
                                <td class="text-center">


                                    <div class="btn btn-xs btn-success btn-sm buttonAddAmostra" data-id="${am.id}">
                                        <span class="glyphicon glyphicon-plus-sign"></span>
                                        <fmt:message key="view.lista.gradeamostral.addAmostra" />
                                    </div> <a href="editar?id=${am.id}"> <span
                                            class="btn btn-warning btn-xs"> <span
                                                class="glyphicon glyphicon-pencil"></span> <fmt:message
                                                key="view.lista.gradeamostral.editar" />
                                        </span>
                                    </a>
                                    <a href="<c:url value='exportartxt?id=${am.id}'/>" class="btn btn-default btn-xs">
                                        <span class="glyphicon glyphicon-export" > </span>
                                        <fmt:message key="lista.exportar" />
                                    </a>   

                                    <div class="btn btn-xs btn-danger modalExcluir" id="${am.id}" data-codigo="${am.codigo}"
                                         data-titulo="<fmt:message key="view.grade.excluir.titulo"/> ${am.descricao}"
                                         data-mensagem=" <fmt:message key="view.grade.excluir.mensagem"/>"
                                         data-excluir=" <fmt:message key="view.grade.excluir"/> ">
                                        <span class="glyphicon glyphicon-trash"></span>
                                        <fmt:message key="view.lista.grade.excluir" />
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

                <a href='<c:url value="/projeto/area/lista?id=${idProjeto}" />'
                   " class="btn btn-primary"> <span
                        class="glyphicon glyphicon-menu-left"></span> <fmt:message
                        key="voltar" />
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

                $(".buttonAddAmostra").on('click', function () {

                    $("#controleCriaAmostra").val("true");
                    $("#controleIdGradeAmostral").val(this.attributes.getNamedItem('data-id').value);

                    $("#divFormCriaAmostra").show();
                    $("#principal").hide();
                    $("#titulo").hide();
                    $("#h3").hide();

                });

                $("#buttonVoltarListaGrades").on('click', function () {

                    $("#divFormCriaAmostra").hide();
                    $("#principal").show();
                    $("#titulo").show();
                    $("#h3").show();

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