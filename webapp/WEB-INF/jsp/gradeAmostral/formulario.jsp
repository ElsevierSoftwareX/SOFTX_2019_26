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

        <title><fmt:message key="nome.projeto" /> <fmt:message key="view.login.titulo" /></title>

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

                <form id="formularioGradeAmostral" action="<c:url value='persiste' />" method="POST" enctype="multipart/form-data">

                <input type="hidden" name="gradeAmostral.id"      value="${gradeAmostral.id}" /> 
                <input type="hidden" name="gradeAmostral.area.id" value="${idArea}"     />
                <input type="hidden" name="idAreaAPI" value="${idAreaAPI}"  id="idAreaAPI"   />
                <input type="hidden" name="idProjeto"       value="${idProjeto}"  /> 
                <input type="hidden" name="salvarAPI"       value="${salvarAPI}"  ID="salvarAPI"/> 

                <h4 class="text-center">
                    <fmt:message key="view.area.gradeamostral.titulo" />
                </h4>

                <hr style="margin-top: 10px; margin-bottom: 10px;">

                <jsp:include page="../include/alerta.jsp"></jsp:include>

                    <div class="row">
                        <div class="col-md-12">
                            <label> 
                            <fmt:message key="view.area" /> 
                        </label> 
                        <input type="text" class="form-control" value="${nomeProjeto}" disabled="disabled">
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12">
                        <label> 
                            <fmt:message key="view.area.grade.descricao" /> 
                            <span class="required-class"> * </span>
                        </label> 
                        <input type="text" class="form-control" name="gradeAmostral.descricao" value="${gradeAmostral.descricao}" maxlength="60" minlength="4" required>
                    </div>
                </div>

                <hr id="hr">

                <div id="buttongrade" class="row">
                    <div class="col-md-6">
                        <div class="btn btn-primary btn-block" id="buttonGradeAmostral">
                            <fmt:message key="view.inserir.gradeamostralxy" />
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="btn btn-primary btn-block" id="buttonAmostra">
                            <fmt:message key="view.inserir.gradeamostraltxt" />
                        </div>
                    </div>
                </div>

                <input type="hidden" name="formGrade" value="true" id="controleFormGrade">

                <input type="hidden" name="formAmostra" value="true" id="controleFormAmostra">

                <hr id="hr2">

                <div id="divFormAmostra" style="display: none;">
                    <div class="row">
                        <div class="col-md-12">
                            <label>
                                <fmt:message key="view.modal.gerar.area.txt" /> 
                                <span class="required-class"> * </span> 
                            </label>
                            <input type="file" class="form-control" id="files" name="uploadedFile" />
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group" id="conteudo-wrapper">

                                <label> 
                                    <fmt:message key="modal.amostra.ordem.dados" /> 
                                    <span class="required-class"> * </span>
                                </label>

                                <br />

                                <label class="checkbox-inline">
                                    <input type="radio" name="latlong" value="latlong" checked="checked"> 
                                    <fmt:message key="modal.amostra.latitude.longitude" />
                                </label>
                                <label class="checkbox-inline">
                                    <input type="radio" name="latlong" value="longlat" style="margin-top: 0px;"> 
                                    <fmt:message key="modal.amostra.longitude.latitude" />
                                </label>
                            </div>
                        </div>
                    </div>
                </div>	

                <div id="divFormGrade" style="display: none;">

                    <div class="row">
                        <div class="col-md-6">
                            <label> 
                                <fmt:message key="view.amostra.pixel.x.metros" /> 
                                <span class="required-class"> * </span>
                            </label> 
                            <input type="text" pattern="[0-9]*[.]?[0-9]*" class="form-control" name="gradeAmostral.tamx" value="${gradeAmostral.tamx}" maxlength="5" minlength="1">
                        </div>
                        <div class="col-md-6">
                            <label> 
                                <fmt:message key="view.amostra.pixel.y.metros" /> 
                                <span class="required-class"> * </span>
                            </label> 
                            <input type="text" pattern="[0-9]*[.]?[0-9]*" maxlength="5" minlength="1" class="form-control" name="gradeAmostral.tamy" value="${gradeAmostral.tamy}" >
                        </div>
                    </div>

                </div>

                <hr>
                <div id="divSalvarAPI" class="text-center" style="display: none"> <input type="checkbox" name="salvarAPIaux" id="salvarAPIaux" class="text-center" value="nao" onchange="verificaChecks()"/>
                    <label class="text-center"> <fmt:message key="modal.salvargradeaapi" /></label>
                </div>


                <input style="float: right;" type="submit" class="btn btn-success" value=<fmt:message key="cadastrar"/> />

                <a href="lista?id=${idArea}" class="btn btn-primary"> 
                    <span class="glyphicon glyphicon-menu-left"></span>
                    <fmt:message key="voltar"/>
                </a>

            </form>
        </div>

        <jsp:include page="../include/script.jsp"></jsp:include>

        <script type="text/javascript">
            document.getElementById("menuinicial").style.display = 'block';
            $("#buttonAmostra").on('click', function () {

                $("#controleFormAmostra").val("false");

                $("#divFormGrade").hide();
                $("#buttongrade").hide();
                $("#hr").hide();
                $("#hr2").hide();
                $("#divFormAmostra").show();

            });

            $("#buttonGradeAmostral").on('click', function () {

                $("#controleFormGrade").val("false");

                $("#divFormAmostra").hide();
                $("#buttongrade").hide();
                $("#hr").hide();
                $("#hr2").hide();
                $("#divFormGrade").show();

            });

            $(document).ready(function () {


                window.onload = function ()
                {
                    if (document.getElementById('idAreaAPI').value === "")
                    {
                        document.getElementById('divSalvarAPI').style.display = 'none';
                        $('#salvarAPI').val("nao");
                    } else {
                        document.getElementById('divSalvarAPI').style.display = 'block';
                         $('#salvarAPI').val("nao");
                    }
                };

                
            });
            
            function verificaChecks() {
                    if (document.getElementById("salvarAPIaux").checked === true) {
                        $('#salvarAPI').val("sim");
                        //alert( $('#salvarAPI').val());
                    } else {
                        $('#salvarAPI').val("nao");
                    }

                }


        </script>

    </body>
</html>