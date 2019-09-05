<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<form id="formularioAreaServer" action="<c:url value='persiste/areaTxt' />" method="POST" enctype="multipart/form-data">

    <input type="hidden" value="${idProjeto}" name="idProjeto" id="idProjeto">
    <input type="hidden" value="${idArea}" name="idArea">
    <input type="hidden" value="${idUsuario}" name="idUsuario" id="idUsuario">

    <div class="modal fade" id="modalNovaArea" tabindex="-1" role="dialog" aria-labelledby="modalnovaAreaLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title" id="modalnovaAreaLabel">
                        <fmt:message key="view.nova.area.importar.TXT" />
                    </h4>
                </div>
                <div class="modal-body">				

                    <div class="row">
                        <div class="col-md-12">
                            <label> 
                                <fmt:message key="view.filtro.nome.area" /> 
                                <span class="required-class"> * </span>
                            </label> 
                            <input type="text" class="form-control" name="area.descricao"  id="nomeArea" maxlength="100" minlength="4" required="required">
                        </div>
                    </div>

                    <%--<div class="row">
                            <div class="col-md-10">
            
            <label for="area.imovel.codigo">
                    <fmt:message key="modal.area.imovel" /> 
                    <span class="required-class"> * </span>
            </label>
            
            <select name="area.imovel.id" class="form-control" required="required" >
                                            <c:forEach items="${listaImoveis}" var="i">
                    <option value="${i.id}">${i.nome}</option>
                                            </c:forEach> 
                                    </select>
                                    
    </div>
                            <div class="col-md-2" style="margin-top: 29px; margin-left: -7px;">
            <a class="btn btn-default"  href="<c:url value='/imovel/lista'/>"> 
                    <i class="glyphicon glyphicon-plus-sign"></i> 
                    <fmt:message key="view.novo" />
            </a>
    </div>
    </div> --%>

                    <div class="row">
                        <div class="col-md-10">

                            <label for="area.imovel.codigo">
                                <fmt:message key="modal.area.tipo.solo" /> 
                                <span class="required-class"> * </span>
                            </label>

                            <select name="area.tipoSolo.codigo" class="form-control" required="required" >
                                <c:forEach items="${listaSolos}" var="s">
                                    <option value="${s.codigo}">${s.descricaoPT}</option>
                                </c:forEach> 
                            </select>

                        </div>
                        <div class="col-md-2" style="margin-top: 29px; margin-left: -7px;">
                            <a class="btn btn-default"  href="<c:url value='/tipoSolo/formulario?id=${idUsuario}'/>">
                                <i class="glyphicon glyphicon-plus-sign"></i> 
                                <fmt:message key="view.novo" />
                            </a>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-12">
                            <label>
                                <fmt:message key="view.modal.gerar.area.txt" /> 
                                <span class="required-class"> * </span> 
                            </label>
                            <input type="file" class="form-control" id="files" name="uploadedFile" required="required" />
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
                                    <input type="radio" name="latlong" value="latlong" required="required" checked="checked"> 
                                    <fmt:message key="modal.amostra.latitude.longitude" />
                                </label>
                                <label class="checkbox-inline">
                                    <input type="radio" name="latlong" value="longlat" required="required" style="margin-top: 0px;"> 
                                    <fmt:message key="modal.amostra.longitude.latitude" />
                                </label>
                                <label class="checkbox-inline">
                                    <input type="checkbox" name="salvarAPI" id="salvarAPI" style="margin-top: 0px;" value="nao" onchange="verificaChecks('salvarAPI')"> 
                                    <fmt:message key="modal.salvarareaapi" />
                                </label>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-warning" data-dismiss="modal">
                        <fmt:message key="view.cancelar" />
                    </button>
                    <input style="float: right;" type="submit" class="btn btn-success" id="addConfirmar" value=<fmt:message key="cadastrar"/> />
                </div>

            </div>
        </div>
    </div>
</form>
<jsp:include page="../include/script.jsp"></jsp:include>

<script type="text/javascript">

    function verificaChecks(salvarAPI) {
        if (document.getElementById(salvarAPI).checked == true) {
            $('#salvarAPI').val("sim");
            //alert( $('#salvarAPI').val());
        }else{
            $('#salvarAPI').val("nao");          
        }
    }

</script>