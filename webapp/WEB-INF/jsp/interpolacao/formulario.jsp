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

        <title> <fmt:message key="nome.projeto"/> - <fmt:message key="view.interpolacao"/> </title>

        <jsp:include page="../include/link.jsp"></jsp:include>
        </head>
        <body>

        <jsp:include page="../menu/menu.jsp"></jsp:include>

            <div class="container" style="border: 1px solid #ddd;">

                <form id="formularioInterpolacao" action="<c:url value='persiste' />" method="POST" >
                <input type="hidden" value="${idProjeto}" name="idProjeto" id="idProjeto">    
                <input type="hidden" value="${classificacao.codigo}" name="classificacao.codigo"  id="classificacao.codigo" /> 
                <input type="hidden" name="option" id="option" value="${option}">
                <h3 class="text-center"> <fmt:message key="view.cadastrar.interpolacao"/></h3> 	

                <hr>

                <jsp:include page="../include/alerta.jsp"></jsp:include>

                    <div class="row">
                        <div class="col-md-4">
                            <label> <fmt:message key="modal.interpolacao.nome" /> <span class="required-class"> * </span> </label>
                        <input type="text" class="form-control" id="formInterpolacaoNome" required="required" name="mapa.descricao">
                    </div>	
                    <!--<div class="col-md-3">
                       <label> <fmt:message key="modal.interpolacao.projeto" /> <span class="required-class"> * </span> </label>
                      <select class="form-control" id="formInterpolacaoProjeto" required="required" onchange="setProjetoArea(this, 'formInterpolacaoArea', 0);">
                           <option value="0"> Selecionar </option>
                    <c:forEach items="${projetos}" var="p">
                        <option value="${p.id}"> ${p.descricao} </option>			
                    </c:forEach>                        
                </select>
            </div>!-->


                    <div class="col-md-4">
                        <label> <fmt:message key="modal.interpolacao.area" /> <span class="required-class"> * </span> </label>
                        <select class="form-control" id="formamostra" required="required"  name="mapa.area.id" onchange="setAmostra(this, 'formInterpolacaoAmostra', 0)" >
                            <option value="0"> <fmt:message key="modal.interpolacao.option" />  </option>
                            <c:forEach items="${areasprojeto}" var="a">
                                <option value="${a.id}">${a.descricao}</option>
                            </c:forEach> 
                        </select>
                    </div>
                    <div class="col-md-4">
                        <label> <fmt:message key="modal.interpolacao.amostra" /> <span class="required-class"> * </span> </label>
                        <select class="form-control" id="formInterpolacaoAmostra" required="required"  name="mapa.amostra.id">
                            <option> <fmt:message key="modal.interpolacao.option" /> </option>
                        </select>
                    </div>
                    <!--<div class="col-md-3">
                        <label> <fmt:message key="modal.interpolacao.classificador" /> <span class="required-class"> * </span> </label>
                        <select class="form-control" id="formInterpolacaoClassificador" required="required" name="mapa.classificacao.codigo" >
                            <option value="0"> Selecionar </option>
                        </select>
                    </div>	!-->				
                </div>

                <div class="row">
                    <div class="col-md-4">
                        <label> <fmt:message key="modal.interpolacao.tipo.interpolador" /> <span class="required-class"> * </span> </label>
                        <select class="form-control" id="formInterpolacaoInterpolador" required="required" name="mapa.tipoInterpolador" onchange="camposidw(this.value)">
                            <option value="IDP"> <fmt:message key="modal.interpolacao.tipo.interpolador.idp" /> </option>
                            <option value="KRI" > <fmt:message key="modal.interpolacao.tipo.interpolador.kri" /> </option>
                            <option value="MM" > <fmt:message key="modal.interpolacao.tipo.interpolador.mm" /> </option>
                        </select>
                    </div>
                    <div class="col-md-4">
                        <label> <fmt:message key="modal.interpolacao.tamanho.pixel.x" /> <span class="required-class"> * </span> </label>
                        <input type="text" class="form-control" id="formInterpolacaoPixelX" required="required" name="mapa.tamanhoX">
                    </div>
                    <div class="col-md-4">
                        <label> <fmt:message key="modal.interpolacao.tamanho.pixel.y" /> <span class="required-class"> * </span> </label>
                        <input type="text" class="form-control" id="formInterpolacaoPixelY" required="required" name="mapa.tamanhoY">
                    </div>					
                </div>
                <div class="row" id="divCamposIdw">
                    <div class="col-md-4" id="expoente">
                        <label> <fmt:message key="modal.interpolacao.expoente" /> <span class="required-class"> * </span> </label>
                        <input type="text" class="form-control" id="formInterpolacaoExpoente" value="1" required="required" name="mapa.expoente" >
                    </div>
                    <div class="col-md-4">
                        <label> <fmt:message key="modal.interpolacao.numero.vizinho" /> <span class="required-class"> * </span> </label>
                        <input type="text" class="form-control" id="formInterpolacaoVizinho" value="10" required="required" name="mapa.numeroPontos">
                    </div>
                    <div class="col-md-4">
                        <label> <fmt:message key="modal.interpolacao.raio" /> <span class="required-class"> * </span> </label>
                        <input type="text" class="form-control" id="formInterpolacaoRaio" value="0" required="required" name="mapa.raio">
                    </div>					
                </div>

                <br />

                <hr style="margin-top: 0px;">
                <a href="lista?idProjeto=${idProjeto}" class="btn btn-primary" class="glyphicon glyphicon-menu-left">
                    <span class="glyphicon glyphicon-menu-left"></span>
                    <fmt:message key="voltar" />
                </a>

                <input onclick="loader()" style="float: right;" type="submit" class="btn btn-success" value=<fmt:message key="modal.interpolar"/> />
                
            </form>
            <div id="loader" class="text-center" style="display: none">
                <div class="loader"> </div>
                <fmt:message key="carregando.interpolacao" />
            </div>
        </div> 		 

        <jsp:include page="../include/script.jsp"></jsp:include>

            <script type="text/javascript" src="<c:url value='/resources/js/controller/interpolacao.js' />"></script>

        <script type="text/javascript">
                   
            <fmt:message key="modal.interpolacao.option" var="propoption"/>
                    $(document).ready(function () {
                         document.getElementById("menuinicial").style.display = 'block';
                        $("#formInterpolacaoClassificador").change(function () {
                            $("#classificacao.codigo").val($(this).val());
                        });


                        window.onload = function ()
                        {
                            $('#option').val("${propoption}");
                        };
                    });

                    function camposidw(interpolador) {
                        var thing = interpolador;
                        if ("KRI" !== thing)
                        {
                            if ("MM" === thing) {
                                $("#divCamposIdw").show();
                                $("#expoente").hide();
                            } else {
                                $("#expoente").show();
                                $("#divCamposIdw").show();
                            }
                        } else {
                            $("#divCamposIdw").hide();
                        }
                    }

                    function loader () {
                         document.getElementById("loader").style.display = 'block';
                    }

        </script>


    </body>
</html>