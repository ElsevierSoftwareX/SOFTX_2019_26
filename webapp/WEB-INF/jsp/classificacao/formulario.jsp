<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="<c:url value='/resources/dist/jquery.min.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/ferramenta/bootstrap.min.js' />"></script>

        <title><fmt:message key="nome.projeto" /> - <fmt:message key="view.classificador" /></title>

        <jsp:include page="../include/link.jsp"></jsp:include>

            <style type="text/css">
                .ol-attribution, .ol-zoom {
                    display: none;
                }
            </style>
        </head>

        <body>

        <jsp:include page="../menu/menu.jsp"></jsp:include>

            <div class="container" style="width: 900px; border: 1px solid #ddd;">

                <form id="formularioClassificador" action="<c:url value='persiste' />" method="POST" enctype="multipart/form-data">
                <input type="hidden" value="${idProjeto}" name="idProjeto" id="idProjeto">
                <input type="hidden" name="classificacao.codigo" value="${classificacao.codigo}" /> 
                <input type="hidden" id="intervalosClassificacao" name="classes" value="${classes}" /> 

                <h4 class="text-center">
                    <fmt:message key="view.classificador.titulo" />
                </h4>

                <hr style="margin-top: 10px; margin-bottom: 10px;">

                <jsp:include page="../include/alerta.jsp"></jsp:include>

                    <div class="row">
                        <!--<div class="col-md-6"> 
                                <label> <fmt:message key="view.classificador.nome" /> <span class="required-class"> * </span> </label> 
                                <input type="text" class="form-control" name="classificacao.descricao" value="${classificacao.descricao}" maxlength="100" minlength="4" required>
                        </div>!-->
                    <div class="col-md-12">
                        <label> <fmt:message key="view.classificador.entidade" /> <span class="required-class"> * </span> </label>
                        <select class="form-control" name="classificacao.entidade.codigo">
                            <c:forEach items="${listaEntidade}" var="ls">
                                <option value="${ls.codigo}" <c:if test="${ls.codigo eq classificacao.entidade.codigo}">selected="selected"</c:if> >${ls.descricao}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <label> <fmt:message key="view.classificador.atributo" /> <span class="required-class"> * </span> </label>	
                        <select class="form-control" name="classificacao.atributo.codigo">
                            <c:forEach items="${listaAtributo}" var="la">
                                <option value="${la.codigo}" <c:if test="${la.codigo eq classificacao.atributo.codigo}">selected="selected"</c:if> >${la.descricaoPT}</option>
                            </c:forEach>
                        </select>					
                    </div>
                    <div class="col-md-6">
                        <label> <fmt:message key="view.classificador.solo" /> <span class="required-class"> * </span> </label>
                        <select class="form-control" name="classificacao.tipoSolo.codigo">
                            <c:forEach items="${listaSolo}" var="ls">
                                <option value="${ls.codigo}" <c:if test="${ls.codigo eq classificacao.classes.codigo}">selected="selected"</c:if> >${ls.descricaoPT}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <hr>

                <table class="table">
                    <thead> 
                        <tr> 
                            <th class="text-center"> <fmt:message key="view.classificador.de" /> </th> 
                            <th class="text-center"> <fmt:message key="view.classificador.ate" /> </th>
                            <th class="text-center"> <fmt:message key="view.classificador.cor" /> </th>
                            <th class="text-center"> <fmt:message key="view.classificador.nivel" /> </th>
                            <th class="text-center"> 
                                <div class="btn btn-xs btn-success" style="margin-top: 6px;" id="classificadorNovaLinha">
                                    <span class="glyphicon glyphicon-plus"></span>
                                </div>
                            </th> 
                        </tr> 
                    </thead>
                    <tbody id="bodyListaCorClassificador"> 

                        <tr id="linha-1">
                            <td class="text-left">   <input pattern="[0-9]*[.]?[0-9]*" type="text" class="form-control" style="width: 200px"> </td>
                            <td class="text-center"> <input pattern="[0-9]*[.]?[0-9]*" type="text" class="form-control" style="width: 200px"> </td>
                            <td class="text-center"> <input type="color" class="form-control" style="width: 100px"> </td>
                            <td class="text-center"> <input type="text" class="form-control" style="width: 200px"> </td>
                            <td class="text-center"> 

                                <div class="btn btn-xs btn-danger classificadorRemoverLinha" data-id="1" style="margin-top: 6px;">
                                    <span class="glyphicon glyphicon-minus"></span>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>

                <hr>

                <input style="float: right;" type="submit" class="btn btn-success" value=<fmt:message key="cadastrar"/> />

                <a href="lista?idProjeto=${idProjeto}" class="btn btn-primary"> 
                    <span class="glyphicon glyphicon-menu-left"></span>
                    <fmt:message key="voltar"/>
                </a>

            </form>
        </div>

        <jsp:include page="../include/script.jsp"></jsp:include>

        <script type="text/javascript">
            //console.log("chamo o methodo");
            document.getElementById("menuinicial").style.display = 'block';
            $("#formularioClassificador").submit(function () {

                var elemento = $("#bodyListaCorClassificador tr td input");

                var elementosArray = new Array();
                for (var i = 0; i < elemento.length; i = i + 4) {

                    console.log("# " + elementosArray.length);

                    elementosArray.push({"valorMinimo": elemento[i].value, "valorMaximo": elemento[i + 1].value, "cor": elemento[i + 2].value, "nivel": elemento[i + 3].value});

                }

                console.log(JSON.stringify(elementosArray));
                console.log(elementosArray);

                $("#intervalosClassificacao").val(JSON.stringify(elementosArray));

                return true;
            });

            var contLinha = 2;
            $("#classificadorNovaLinha").on('click', function () {

                if (contLinha < 7) {
                    var html = '';

                    html += '<tr id="linha-' + contLinha + '">';
                    html += '	<td class="text-left">   <input type="text" class="form-control" style="width: 200px"> </td>';
                    html += '	<td class="text-center"> <input type="text" class="form-control" style="width: 200px"> </td>';
                    html += '	<td class="text-center"> <input type="color" class="form-control" style="width: 100px"> </td>';
                    html += '	<td class="text-center"> <input type="text" class="form-control" style="width: 200px"> </td>';
                    html += '	<td class="text-center">';
                    html += '		<div class="btn btn-xs btn-danger classificadorRemoverLinha" data-id="' + contLinha + '" style="margin-top: 6px;">';
                    html += '		<span class="glyphicon glyphicon-minus"></span>';
                    html += '	</div>';
                    html += '	</td>';
                    html += '</tr>';

                    $("#bodyListaCorClassificador").append(html);

                    controleClickMenos();

                    contLinha++;
                }
            });

            controleClickMenos();

            function controleClickMenos() {

                $(".classificadorRemoverLinha").unbind("click");

                $(".classificadorRemoverLinha").on('click', function () {

                    $("#linha-" + this.attributes.getNamedItem('data-id').value).remove();
                    contLinha--;
                });
            }

        </script>
    </body>
</html>