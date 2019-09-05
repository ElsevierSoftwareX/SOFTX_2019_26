<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<form id="formularioNovaAmostra"
	action="<c:url value='/cadastrar/amostra/txt' />" method="POST"
	enctype="multipart/form-data">

	<input type="hidden" id="modalAmostraId"     name="amostra.id" value="${amostra.id}" /> 
	<input type="hidden" id="modalAmostraAreaId" name="amostra.area.id"    value="${amostra.area.id}" />

	<div class="modal fade" id="modalNovaAmostra" tabindex="-1"
		role="dialog" aria-labelledby="modalBuscaAmostraLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true"> &times; </span>
					</button>
					<h4 class="modal-title" id="modalBuscaAmostraLabel">
						<fmt:message key="modal.amostra.nova" />
					</h4>
				</div>
				<div class="modal-body">

					<div class="table-responsive">
						<div style="margin-top: 8x;">
							<label> <fmt:message key="modal.amostra.nome" /> <span
								class="required-class"> * </span>
							</label> <input type="text" class="form-control" name="amostra.descricao"
								value="${amostra.descricao}" maxlength="100" minlength="4"
								class="col-xs-12 selectContainer" required>
						</div>
					</div>
					<div style="margin-top: 13px;">
						<label> <fmt:message key="modal.amostra.atributo" /> <span
							class="required-class"> * </span>
						</label>
						<div style="float: inherit;" style="margin: 5px">
							<select id="amostra.atributo" name="amostra.atributo"
								class="form-control">
								<option value=""></option>
								<option>Argila</option>
								<option>Aluminio(Al)</option>
								<option>Areia</option>
								<option>Calcio(Ca)</option>
								<option>Cobre (Cu)</option>
								<option>Ferro(Fe)</option>
								<option>Fosforo (P)</option>
								<option>Hidrogenio(H)+Aluminio(Al)</option>
								<option>Indice SMP</option>
								<option>Manganês(Mn)</option>
								<option>Magnésio(Mg)</option>
								<option>Matéria organica(M.O)</option>
								<option>Potassio (K)</option>
								<option>pH</option>
								<option>Produtividade</option>
								<option>Resistência Mecânica a Penetração -RMP</option>
								<option>Zinco(Zn)</option>
								<option>Silte</option>
								<option>Umidade</option>
								<option>Outro</option>
							</select>
						</div>
					</div>
					<div style="margin-top: 13px;">
						<label> <fmt:message key="modal.amostra.Umedida" /> <span
							class="required-class"> * </span>
						</label>
						<div class="col-xs-13 selectContainer" style="float: inherit;"
							style="margin: 5px">
							<select id="amostra.unidadeDeMedida"
								name="amostra.unidadeDeMedida" class="form-control">
								<%--    <c:forEach items="${amostra.unidademedida}"> --%>
								<option></option>
								<option>mg/dm³</option>
								<option>g/dm³</option>
								<option>g/Kg</option>
								<option>t/ha</option>
								<option>mmolc/dm</option>
								<option>cmolc/dm³</option>
								<option>m3/ha</option>
								<option>N g</option>
								<option>K cmolc /dm3</option>
								<option>K g/dm3</option>
								<option>K Kg/ha</option>
								<option>K2O Kg/ha</option>
								<option>Ca g</option>
								<option>Ca Kg/ha</option>
								<option>CaO Kg</option>
								<option>CaO %</option>
								<option>MgO %</option>
								<option>Mg g</option>
								<option>Mg Kg/ha</option>
								<option>MgO Kg</option>
								<option>Kg/ha</option>
								<option>P2O5 Kg/ha</option>
								<option>SO4 g</option>
								<option>Kg/ha</option>
								<option>mm</option>
								<option>mmolc/dm3</option>
								<option>NO3 g</option>
								<option>K Kg/ha</option>
								<option>K2O Kg/ha</option>
								<option>NH4 g</option>
								<option>Ca Kg</option>
								<option>Ca mg/dm³</option>
								<option>Ca cmolc/dm³</option>
								<option>Mg cmolc/dm³</option>
								<option>Mg mg/dm³</option>
								<option>Mg Kg</option>
								<option>P2O5 Kg/ha</option>
								<option>P Kg/ha</option>
								<option>S g</option>
								<option>g/kg</option>
								<option>g/dm3</option>
								<option>g/L</option>
								<option>ppm</option>
								<option>mg/kg</option>
								<option>mg/dm³</option>
								<option>mg/L</option>
								<option>meg/100 cm3</option>
								<option>mmolc/dm³</option>
								<option>meq/100g</option>
								<option>mmolc/kg</option>
								<option>mmho/cm</option>
								<option>dS/m</option>
								<option>P2O5</option>
								<option>CaO</option>
								<option>Ca</option>
								<option>MgO</option>
								<option>Mg</option>
								<option>mg/Kg</option>
								<option>%</option>
								<option>Kpa</option>
								<option>Kg/ha</option>
								<option>t/ha-</option>
								<option>outro</option>
							</select>
						</div>
						<div class="col-md-13" style="margin-top: 15px;">
							<input type="file" class="form-control" id="files" name="uploadedFile" required="required"  />
						</div>
						<div class="form-group" id="conteudo-wrapper" style="margin-bottom: 30px">
							<label for="uploadedFile"> <br> 
								<fmt:message key="modal.amostra.ordem.dados" /> <br>
							</label>
							<div>
								<label class="radio-inline"> 
									<input type="radio" name="latlong" value="latlong" required="required"> 
									<fmt:message key="modal.amostra.latitude.longitude" />
								</label>
							</div>
							<label class="radio-inline"> 
								<input type="radio" name="latlong" value="longlat" required="required"> 
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