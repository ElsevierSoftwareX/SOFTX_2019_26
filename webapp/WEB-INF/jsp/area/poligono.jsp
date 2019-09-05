<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
	<head>
	
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" href="https://openlayers.org/en/v4.6.5/css/ol.css" type="text/css">
		<script src="https://openlayers.org/en/v4.6.5/build/ol.js"></script>
		
		<title><fmt:message key="nome.projeto" /> <fmt:message key="view.login.titulo" /></title>
		
		<jsp:include page="../include/link-mapa.jsp"></jsp:include>
		<jsp:include page="../include/link.jsp"></jsp:include>
		
		<style type="text/css">
			#map {
				widthheight: 100%;
				margin: 0;
			}
			
		</style>
	</head>

	<body>
	
		<jsp:include page="../menu/menu.jsp"></jsp:include>
	
		<div class="container" style="width: 900px; border: 1px solid #ddd;">
	
			<form id="formularioMapa" action="<c:url value='poligono' />" method="POST">
				
			<input type="hidden" name="projeto.id" value="${projeto.id}">

				<input type="hidden" value="${idArea}"    name="idArea">
				
				<input type="hidden" value="${area.geom}" name="area.geom" id="poligonoProjetoArea">
				
				<h4 style="margin-left: 5px;">
					<fmt:message key="view.area.nova.mapa" />
				</h4>
	
				<hr>
	
				<jsp:include page="../include/alerta.jsp"></jsp:include>
	
				<div class="modal-body">
	
					<div id="novaAreaMapa" tabindex="-1" role="dialog" aria-labelledby="modalnovaAreaMapaLabel"></div>
	
					<div class="modal-body">
						<div class="table-responsive">
							<div style="margin-top: 5x;">
								<label> 
									<fmt:message key="view.filtro.nome.area" /> 
									<span class="required-class"> * </span>
								</label> 
								
								<input type="text" class="form-control" name="area.descricao" id="nomeArea" maxlength="100" minlength="4" >
							</div>
						</div>
	
						<div class="form-group" style="margin-top: 10px;">
							<%--<label for="area.imovel.codigo">
								<fmt:message key="modal.area.imóvel" /> 
									<span class="required-class"> * </span>
								</label> --%>
							
							<div class="row">
								<div class="col-xs-10">
									<%--<select id="area.imovel.codigo" name="area.imovel.codigo" class="form-control">
									 --%>
										<option value="0">${usuarioSession.usuario.nome}</option>
	
										<%--<c:forEach items="${imoveis}" var="i">
											<option>${imoveis}</option>
										</c:forEach>--%>
	
									
	
									</select>
								</div>
								<%--<div class="col-xs-0,5">
									<a class="btn btn-default btn-md" href="<c:url value='/imovel/novoImovel'/>"> 
										<i class="glyphicon glyphicon-plus-sign"></i> 
										<fmt:message key="view.novo" />
									</a>
								</div> --%>
							</div>
	
							<div class="col-md-13" style="margin-top: 15px;"></div>
						</div>
					</div>
	
	
					<div id="map" class="map"></div>
	
				</div>
	
				<input style="float: right;" type="submit" class="btn btn-success" value=<fmt:message key="cadastrar"/> />
		
			</form>
		</div>
	
		<jsp:include page="../include/script.jsp"></jsp:include>
	
		<script type="text/javascript">	
			 document.getElementById("menuinicial").style.display = 'block';	
			var raster = new ol.layer.Tile({
		    	source: new ol.source.BingMaps({
		        	key: 'AqBgyUHxs7_rEy3YhBQUI2CQqiA_nZ3ktBGI5gdB77OZSFUZqFOhfOEXWtYJ9lbx', imagerySet: 'AerialWithLabels'
		  		})
			});
		   
			var map = new ol.Map({
				layers: [raster],
		        target: 'map',
		        view: new ol.View({
		        	center: new ol.geom.Point([-6217890.205764902, -1910870.6048274133]).getCoordinates(),
		    		zoom: 4
		        })
			});  
			
			var vectorSource = new ol.source.Vector();
			var vectorLayer = new ol.layer.Vector({	source:vectorSource });
			
			map.addLayer(vectorLayer);
		
			drawInteraction = new ol.interaction.Draw({ source:vectorSource, type:"Polygon" });
			map.addInteraction(drawInteraction);
		
			var formatGeoJSON = new ol.format.GeoJSON();
		
			drawInteraction.on('drawend', function(e) {
			
				var feature = e.feature; 
				map.removeInteraction(drawInteraction);
			
				selectInteraction = new ol.interaction.Select();
				map.addInteraction(selectInteraction);
			
				selectInteraction.getFeatures().push(feature);
				var featureClone = feature.clone();
				
				featureClone.getGeometry().transform('EPSG:3857', 'EPSG:4326');
			
				var geojson = formatGeoJSON.writeFeature(featureClone);
				
				setPoligono(geojson);
		
				modifyInteraction = new ol.interaction.Modify({ features: selectInteraction.getFeatures() });
				map.addInteraction(modifyInteraction);  
			
				modifyInteraction.on('modifyend', function(evt) {
					
					var collection = evt.features;
					var featureClone = collection.item(0).clone();
					
					featureClone.getGeometry().transform('EPSG:3857', 'EPSG:4326');			
					
					var geojson = formatGeoJSON.writeFeature(featureClone);
			        
					setPoligono(geojson);
				});
			});
			
			function setPoligono(geometria){
				
				geometria = JSON.parse(geometria);
				
				var poligonoNovo = 'POLYGON((';
				
				for (var i = 0; i < geometria.geometry.coordinates[0].length; i++) {
					
					poligonoNovo += geometria.geometry.coordinates[0][i][0];
					poligonoNovo += ' ';
					poligonoNovo += geometria.geometry.coordinates[0][i][1];
					if( (i + 1) !== geometria.geometry.coordinates[0].length ){
					
						poligonoNovo += ', ';
					}				
				} 
				
				poligonoNovo += '))';
				
				//console.log(poligonoNovo);
				
				$('#poligonoProjetoArea').val(poligonoNovo);
				
			}
			
		</script>	
	
	</body>
</html>