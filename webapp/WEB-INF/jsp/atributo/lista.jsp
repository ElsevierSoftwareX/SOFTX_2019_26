<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title><fmt:message key="nome.projeto" /> - <fmt:message key="view.login.titulo" /></title>

        <jsp:include page="../include/link.jsp"></jsp:include>

        </head>
        <body>

        <jsp:include page="../menu/menu.jsp"></jsp:include>

            <div class="container">

                <h3 class="text-center"> <fmt:message key="view.lista.atributos" /> </h3>

            <hr style="margin-bottom: 5px; margin-top: 5px;">

            <div class="row">	
                <div class="col-md-12">	
                    <a class="btn btn-block btn-default" href="formulario?id=${idProjeto}">				
                        <i class="glyphicon glyphicon-file" > </i>
                        <fmt:message key="view.lista.atributo.cadastrada.atributo" />
                    </a>
                </div>
            </div>	

            <hr>					

            <jsp:include page="../include/alerta.jsp"></jsp:include>
            <input type="hidden" name="idProjeto"       value="${idProjeto}"  /> 
            <input type="hidden" name="atributo.descricaoEN" value="${atributo.descricaoEN}">
            <input type="hidden" name="atributo.siglaEN" value="${atributo.siglaEN}">
            <input type="hidden" name="atributo.descricaoES" value="${atributo.descricaoES}">
            <input type="hidden" name="atributo.siglaES" value="${atributo.siglaES}">

            <div class="table-responsive">
                <table class="table table-hover table-bordered ">
                    <thead>
                        <tr>
                            <th class="text-center"><fmt:message key="view.atributo.descricao" /></th>
                            <th class="text-center"><fmt:message key="view.atributo.sigla" /></th>
                            <th class="text-center"><fmt:message key="view.atributo.unidadeMedida" /></th>
                            <th class="text-center"><fmt:message key="view.opcao" /></th>
                        </tr>
                    </thead>
                    <tbody id="bodyListaSolo">

                        <c:forEach items="${listaAtributos}" var="a">
                            <tr>
                                <td class="text-left">${fn:substring(a.descricaoPT, 0, 60)}</td>
                                <td class="text-left">${fn:substring(a.siglaPT, 0, 5)}</td>
                                <td class="text-left">${fn:substring(a.unidadeMedidaPT.sigla, 0, 10)}</td>
                                <td class="text-center">

                                    <a href="editar?codigo=${a.codigo}&idProjeto=${idProjeto}">
                                        <span class="btn btn-warning btn-xs">	
                                            <span class="glyphicon glyphicon-pencil" ></span>
                                            <fmt:message key="view.lista.atributo.editar" />
                                        </span>
                                    </a>	

                                    <div class="btn btn-xs btn-danger modalExcluir" id="${a.codigo}"
                                         data-titulo="<fmt:message key="view.atributo.excluir.titulo"/> ${a.descricaoPT}"
                                         data-mensagem=" <fmt:message key="view.atributo.excluir.mensagem"/>"
                                         data-excluir=" <fmt:message key="view.atributo.excluir"/> ">
                                        <span class="glyphicon glyphicon-trash"></span>
                                        <fmt:message key="view.lista.atributo.excluir" />
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
                    //$('#excluirConfirmar').html('<a class="btn btn-success" href="<c:url value="excluir?codigo='+ this.attributes.getNamedItem('id').value +'&idProjeto='+$(idProjeto).val()+ '"/>">' + this.attributes.getNamedItem('data-excluir').value + '</a> ');
                    $('#myModalExcluir').modal('show');

                });

        </script>
    </body>
</html>
