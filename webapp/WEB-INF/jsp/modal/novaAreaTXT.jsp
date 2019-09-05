<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<form id="formularioAreaServer" action="<c:url value='novaAreaTxt' />" method="POST" enctype="multipart/form-data">
	
	
	<%--ESSE MODAL NÃO É UTILIZADO, O UTILIZADO É CADASTRARNOVAAREATXT
	<%-- <input type="hidden" id="modaAreaId"  name="area.id" value="${area.id}" />  --%>
 	
  <input type="hidden" value="${idProjeto}" name="idProjeto">
   <input type="hidden" value="${idArea}" name="idArea">
		
	<div class="modal fade" id="modalNovaArea" tabindex="-1" role="dialog" aria-labelledby="modalnovaAreaLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="modalnovaAreaLabel">
						<fmt:message key="view.area.importarTXT" />
					</h4>
				</div>
				<div class="modal-body">				
					<div class="table-responsive">
						<div style="margin-top: 8x;">
							<label> 
								<fmt:message key="view.filtro.nome.area" /> 
							<span class="required-class"> * </span>
						 </label> <input type="text" class="form-control" name="area.descricao"
								id="nomeArea" maxlength="100" minlength="4">
						</div>
					</div>					
					
			 	<%--	<div style="margin-top: 13px;">	<label> <fmt:message key="modal.area.solo" /> <span
							class="required-class"> * </span>
				 	</label>
						<div style="float: inherit;" style="margin: 5px">							
								 <select id="area.solo.codigo" name="area.solo.codigo"  class="form-control">
									<option> </option>
									 <c:forEach items="${solos}" var="solo">
                                    
                            		<option value="10">Selecione tipo de solo</option>
									<option value="11">Argiloso</option>
									<option value="12">Arenoso</option>										
									<option value="13">Calcário</option>
									<option value="14">Humoso</option>
									<option value="15">Humífero(Terra preta)</option>
									<option value="16">Terra roxa</option>
									<option value="17">Massapé  </option>
									<option value="18">Salmorão</option>
									<option value="19">Aluviais</option>
									<option> </option>
									<option> </option>
									<option> </option>
									<option> </option>
									<option>Outro</option>	 
									</c:forEach>						
							</select> 
						</div>
					</div> --%> 

					  <%--  <div class="form-group" style="margin-top: 13px;">
                        <label for="area.imovel.codigo"><fmt:message key="modal.area.imóvel" /> <span
							class="required-class"> * </span></label>
                        <div class="row">
                            <div class="col-xs-10">
                               <select id="area.imovel.codigo" name="area.imovel.codigo" class="form-control" >
                                  
	                       		  <option value="0">${usuarioSession.usuario.nome}</option>
	
	                              <c:forEach items="${imoveis}" var="i">
	                                <option></option>
									</c:forEach> 
                                    
                
                                    
                                </select> 
                          <%--       <select id="area.imovel.codigo" name="area.imovel.codigo" class="form-control">
									<option>${usuarioSession.usuario.nome}</option>
									<c:forEach items="${imoveis}" var="i">
										<option value="${i.id}">${i.nome}</option>
									</c:forEach>
								</select> --%>
                           <%--  </div>
                            <div class="col-xs-0,5">
                              <a class="btn btn-default btn-md"  href="<c:url value='/imovel/novoImovel'/>"> <i class="glyphicon glyphicon-plus-sign"></i> <fmt:message key="view.novo" /></a>
                            </div>
                        </div>	 --%>					
					

					<div class="col-md-13" style="margin-top: 15px;">
					<label>
								<fmt:message key="view.modal.gerar.area.txt" /> 
								<span class="required-class"> * </span> 
							</label>
							<input type="file" class="form-control" id="files" name="uploadedFile" required="required" />
						</div>
						
						<div class="form-group" id="conteudo-wrapper" style="padding-top: 20px;">
							
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