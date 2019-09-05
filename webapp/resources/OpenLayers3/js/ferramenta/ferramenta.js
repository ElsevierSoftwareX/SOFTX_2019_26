var sketch;
var sketchElement;
var interaction;

var sourceCadastrar, sourceDistancia, sourceRaio;
var vectorCadastrar, vectorDistancia, vectorRaio;

var styleLista;

var listaMedidas = new Array();
var listaDraw    = new Array();

var medindo = false;

var formatJSON = new ol.format.GeoJSON();

var msg = false;

/**
 * vectorDistancia.getSource().removeFeature(vectorDistancia.getSource().getFeatures()[0]);  remove uma featura;
 * 
 * */

$(document).ready(function(){

	sourceCadastrar = new ol.source.Vector();
	sourceDistancia = new ol.source.Vector();
	sourceRaio      = new ol.source.Vector();

	vectorCadastrar = createVector({title : "Camadas desenhadas",     source : sourceCadastrar, tipo : "cadastrar", visibleLayerSwitch : false});
	vectorDistancia = createVector({title : "Medir área e distância", source : sourceDistancia, tipo : "distancia", visibleLayerSwitch : false});
	vectorRaio      = createVector({title : "Área de abrangência"   , source : sourceRaio     , tipo : "raio",      visibleLayerSwitch : false});

	map.addLayer(vectorCadastrar);
	map.addLayer(vectorDistancia);
	map.addLayer(vectorRaio);

//	atualizaLayerSwitch();
	
	$("#conteiner-listaMedida").dialog({ position : [150,150], width : 400, autoOpen : false });
	$("#conteiner-listaDraw"  ).dialog({ position : [150,150], width : 450, autoOpen : false });
	$("#conteiner-listaRaio"  ).dialog({ position : [150,150], width : 350, autoOpen : false });

	$(".ui-dialog-titlebar-close").addClass("btn btn-default");

	$(".ui-dialog-titlebar-close").html('<span class="glyphicon glyphicon-remove"></span> ');

	$(document).ready(function(){
	    $(".AtivaTooltip").tooltip();   
	});	
	
});

function addDivFerramenta(componente, div){

	msg = componente.alerta;
	
	var html = '';
	
	html += '<div class="ferramenta">';
	html += '	<div class="btn-group conteiner">';

	html += '		<div class="btn btn-success" onclick="openLayerSwitch();">';
	html += '			<span class="glyphicon glyphicon-align-justify"></span>';
	html += '		</div>';
	
	html += '		<div class="btn btn-success" style="min-width: 100px;" onclick="addInteraction('+ "'" + 'None' + "'" + ');">';
	html += '			Navegação';
	html += '		</div>';
	html += '		<div class="btn-group">';
	html += '			<button type="button" style="min-width: 100px;" class="btn btn-success dropdown-toggle" data-toggle="dropdown" aria-expanded="false">';
	html += '				Medição'; 
	html += '				<span class="caret"></span>';
	html += '			</button>';
	html += '			<ul class="dropdown-menu" role="menu">';
	html += '				<li onclick="addInteraction('+ "'" + 'Length' + "'" + ');" ><div>Distância</div></li>';
	html += '				<li onclick="addInteraction('+ "'" + 'Area'   + "'" + ');" ><div>Área</div></li>';
	html += '				<li class="divider"></li>';
	html += '				<li onclick="addInteraction('+ "'" + 'Raio'   + "'" + ');" ><div>Raio</div></li>';
	html += '			</ul>';
	html += '		</div>';
	html += '	</div>';
	html += '</div>';
	
	if( componente.measure ){	
		html += '<div id="conteiner-listaMedida" title="Lista de Medida"></div>';
		$(map.getViewport()).on('mousemove', mouseMoveHandler);
	}	

	if ( componente.raio ) {
		html += '<div id="conteiner-listaRaio" title="Área de Abrangência"></div>';
		$(map.getViewport()).on('click', mouseClickRaio);
	}
	
	div.html(html);	

	$('[data-toggle="tooltip"]').tooltip();

}

function filtroMapa(){
	
	$("#myModalFiltroMapa").modal('show');
	
}

function addInteraction(type){
	
	map.removeInteraction(interaction);

	if ( type == "Area" || type == "Length" ) {

		/**
		 * $("#divMedida").dialog("open");
		 * 
		 * Lembrando que tenho que criar um js responsável por montar o dialog que vai receber as medidas
		 * 
		 */  

		$("#conteiner-listaMedida").dialog("open" );
		$("#conteiner-listaDraw"  ).dialog("close");
		$("#conteiner-listaRaio")  .dialog("close");

		type = (type == 'Area' ? 'Polygon' : 'LineString'); 

		interaction = new ol.interaction.Draw({
			source: sourceDistancia,
			type: ( type )
		});

		map.addInteraction(interaction);

		interaction.on('drawstart', function(evt) {

			medindo = true;

			sketch = evt.feature;
			sketchElement = document.createElement('div');
			sketchElement.className = "row listaMedida bs-callout bs-callout-primary";
			var outputList = document.getElementById('conteiner-listaMedida');

			if (outputList.childNodes) {
				outputList.insertBefore(sketchElement, outputList.firstChild);
			} 
			else {
				outputList.appendChild(sketchElement);
			}

		}, this);

		interaction.on('drawend', function(evt) {

			try {
				
				medindo = false;
	
				var geom   = (sketch.getGeometry());
	
				if (geom instanceof ol.geom.Polygon) {
					listaMedidas.push({ id : (listaMedidas.length + 1), tipo : "Área", medida : formatArea((geom)) });
				} else if (geom instanceof ol.geom.LineString) {
					listaMedidas.push({ id : (listaMedidas.length + 1), tipo : "Distância", medida : formatLength((geom)) });
				}
	
				$("#conteiner-listaMedida .listaMedida").remove();
	
				montaListaDistancia(listaMedidas);
	
				sketch = null;
				sketchElement = null;
				
			} catch (e) {
				mensagemAlerta("danger", "", "Medida não pode ser cadastrada.", true, true);
			}	
				
		}, this);
		
	} 
	else {

		$("#conteiner-listaMedida").dialog("close");

		if (type == "Point" || type == "LineString" || type == "Polygon"){
			
			$("#conteiner-listaRaio").dialog("close");
			$("#conteiner-listaDraw").dialog("open");

			$("#typeFeature").val(type);

			interaction = new ol.interaction.Draw({
				features: featureOverlay.getFeatures(),
				source: sourceCadastrar,
				type: ( type )
			});

			map.addInteraction(interaction);

			interaction.on('drawend', function(evt) {

				/**
				 * 
				 *  --- Problema que quando chama o modal continua a execução a função
				 *  --- Salvar o data {"id" : 0, "nome" : "novaCamada"};
				 *  --- Não salvei a data pq tem um atributo d e vou salvar quando fechar o modal... 
				 *      e para encontrar a feature cadastrada vou verificar qual feature esta null no campo "d"
				 *      
				 *      
				 *      
				 * getAlerta($("#alertaCIH"), "success", "Camada", "cadastrada com sucesso. Nome : " + evt.feature.nome, true, true);
				 * 
				 *  
				 * */

				$('#modalCancelarFeature').hide();
				$("#modalExcluirFeature") .show();

				$('#myModalCadastrarFeature').modal("show");

			}, this);		
		} 
		else {
			if (type == "Modify"){

				$("#conteiner-listaDraw").dialog("open");
				
				featureOverlay.setMap(map);
				
				interaction = new ol.interaction.Modify({ 
					source: sourceCadastrar,
					features: featureOverlay.getFeatures(),
						deleteCondition: function(event) {
							return ol.events.condition.shiftKeyOnly(event) && ol.events.condition.singleClick(event);
						}
				});
				
				map.addInteraction(interaction);
								
			}
			else{
				if (type == "None") {
					$("#conteiner-listaDraw")  .dialog("close");
					$("#conteiner-listaMedida").dialog("close");
					$("#conteiner-listaRaio")  .dialog("close");
				}
				else {
					if (type == "Raio") { 
						
						$("#conteiner-listaDraw")  .dialog("close");
						$("#conteiner-listaMedida").dialog("close");
						$("#conteiner-listaRaio")  .dialog("open" );
						
						click = 0;
						featureRaio = null;
						
						interaction = new ol.interaction.Draw({
							source: sourceRaio,
							type: ( "LineString" )
						});

						map.addInteraction(interaction);
						
						interaction.on('drawstart', function(evt) {
							featureRaio = evt.feature;
						}, this);
					}
				}
			}
		}
	}	
}

/**
 * Calcular as medidas conforme for arastado o mause no mapa;
 * */
var mouseMoveHandler = function(evt) {
	
	if (sketch) {

		var output = "", resultado = "", tipo = "";

		var geom   = (sketch.getGeometry());

		if (geom instanceof ol.geom.Polygon) {
			resultado = formatArea((geom));
			tipo      = "Área";
		} 
		else if (geom instanceof ol.geom.LineString) {
			resultado = formatLength((geom));
			tipo      = "Distância";
		}

		output += '	<div class="col-md-4 col-listaMedida">';

		if ( tipo == "Área" ) { 
			output += '<img src="resources/OpenLayers3/image/ferramenta/area.png" width="21" />';
		} 
		else { 
			output += '<img src="resources/OpenLayers3/image/ferramenta/distancia.png" width="21" />';
		}

		output += '	</div>';

		output += '	<div class="col-md-8 col-listaMedida">';
		output += '		<div class="text-right">' + resultado + '</div>';
		output += '	</div>';

		sketchElement.innerHTML = output;
	} 
};

/**
 * Calcular o raio de agrangéncia de um determinado ponto;
 * */
var click = 0;
var featureRaio;
var mouseClickRaio = function(evt) {
	
	if(featureRaio){

		click ++;
		
		if(click == 2){
			
			try{
				
				vectorRaio.getSource().clear();
			
				map.removeInteraction(interaction);
				
				var geom   = featureRaio.getGeometry();
				var geoLen = Math.round(geom.getLength() * 100) / 100;
				var circle = new ol.geom.Circle(geom.getCoordinates()[0], geoLen );
				var circleFeature = new ol.Feature(circle);
					
				sourceRaio.addFeatures([circleFeature]);
				
				addInteraction("Raio");
				
				var length = Math.round(geom.getLength() * 100) / 100;
				var raio, medicao;

				if (length > 100) {
					raio = (Math.round(length / 1000 * 100) / 100);
					medicao = ' ' + 'km';
				} 
				else {
					raio = (Math.round(length * 100) / 100);
					medicao = ' ' + 'm'; 
				}
				
				var diametro    = raio + raio;  
				var comprimento = 2 * 3.14 * raio;
				var area        = 3.14 * (raio * raio);
				
				var html = '';
				html += '<div style="max-height: 200px;" class="bs-callout bs-callout-success">';
				html += '	<div class="row listaRaio" style="padding: 5px;" >';
				html += '		<div class="col-md-5">Raio</div>';	
				html += '		<div class="col-md-7">'+ raio + medicao +'</div>';
				html += '	</div>';
				html += '	<div class="row listaRaio" style="padding: 5px;" >';
				html += '		<div class="col-md-5">Diâmetro</div>';	
				html += '		<div class="col-md-7">'+ diametro + medicao +'</div>';
				html += '	</div>';
				html += '	<div class="row listaRaio" style="padding: 5px;" >';
				html += '		<div class="col-md-5">Comprimento</div>';	
				html += '		<div class="col-md-7">'+ comprimento.toFixed(2) + medicao +'</div>';
				html += '	</div>';
				html += '	<div class="row listaRaio" style="padding: 5px;" >';
				html += '		<div class="col-md-5">Área</div>';	
				html += '		<div class="col-md-7">'+ area.toFixed(2) + medicao + '<sup>2</sup>' +'</div>';
				html += '	</div>';
				html += '</div>';
				html += '<div id="excluirFeatureRaio" class="btn btn-danger btn-block" > Excluir a Área de Abrangência </div>';
				
				$("#conteiner-listaRaio").html(html);
				
				$("#excluirFeatureRaio").click(function(){
					vectorRaio.getSource().clear();
					$("#conteiner-listaRaio").html('');
				});

			}catch (e) {
				mensagemAlerta("danger", "", "Não foi possivel calcular o Raio de Abrangência.", true, true);
			}	
		}
	}
};

/**
 *	Montar Listas de medidas 
 */
function montaListaDistancia(lista){

	var html  = '';
	var style = "#E7E7E7";

	if(lista.length > 0) {

		html += '<div style="max-height: 250px; margin-top: 5px;" class="bs-callout bs-callout-success">';

		for ( var i = 0 ; i < lista.length; i ++ ) {

			html += '<div id="'+ i +'" class="row listaMedida" style="background:'+style+'; padding: 5px;" >';

			html += '	<div class="col-md-1"> ';

			if ( lista[i].tipo == "Área" ) { 
				html += '<img src="resources/OpenLayers3/image/ferramenta/area.png" width="21" />';
			} 
			else { 
				html += '<img src="resources/OpenLayers3/image/ferramenta/distancia.png" width="21" />';
			}

			html += '	</div>';

			html += '	<div class="col-md-1"> ';
			html += '		<div class="text-center">' + (i + 1) + '</div>';
			html += '	</div>';
			html += '	<div class="col-md-8"> ';
			html += '		<div class="text-right">' + lista[i].medida + '</div>';
			html += ' 	</div>';
			html += '	<div class="col-md-2">';
			html += '		<div class="text-center">';
			html += '			<span style="margin-top: 2px;" class="glyphicon glyphicon-trash" id="'+ i +'" ></span>';
			html += '		</div>';
			html += '	</div>';
			html += '</div>';		

			if( style == "#E7E7E7" ) {
				style = "#FFFFFF";
			}
			else{
				style = "#E7E7E7";
			}		
		}

		html += '</div>';
	}	

	$("#conteiner-listaMedida").html(html);

	$(".listaMedida span").click(function(e){

		excluirElementoListaDistancia(lista, e.currentTarget.id);		

	});

}

function excluirElementoListaDistancia(lista, id){

	try{
		var auxListaMedidas = new Array();
	
		for (var i = 0; i < lista.length; i++) {
			if(i != id){
				auxListaMedidas.push(lista[i]);
			}
		}
	
		vectorDistancia.getSource().removeFeature(vectorDistancia.getSource().getFeatures()[id]);
	
		listaMedidas = auxListaMedidas;
	
		montaListaDistancia(listaMedidas);
	
		mensagemAlerta("success", "", "Medida excluida com sucesso.", true, true);
	
	}
	catch (e) {
		mensagemAlerta("danger", "", "Não foi possivel excluir a Medida.", true, true);
	}	
}

/**
 * Formata e cálcula a distância da feature;
 * */
var formatLength = function(line) {

	var length = Math.round(line.getLength() * 100) / 100;
	var output;

	if (length > 100) {
		output = (Math.round(length / 1000 * 100) / 100) +
		' ' + 'km';
	} 
	else {
		output = (Math.round(length * 100) / 100) +
		' ' + 'm';
	}

	return output;	
};

/**
 * Formata e cálculo da Área da feature;
 * */
var formatArea = function(polygon) {

	var area = polygon.getArea();
	var output;

	if (area > 10000) {
		output = (Math.round(area / 1000000 * 100) / 100) +
		' ' + 'km<sup>2</sup>';
	} 
	else {
		output = (Math.round(area * 100) / 100) +
		' ' + 'm<sup>2</sup>';
	}

	return output;	
};

/**
 * Estilo das features que serão cadastrados
 * */
var featureOverlay = new ol.FeatureOverlay({
	style: new ol.style.Style({
		fill: new ol.style.Fill({
			color: 'rgba(255, 255, 255, 0)'
		}),
		stroke: new ol.style.Stroke({
			color: 'rgba(255, 255, 255, 0)',
			width: 2
		}),
		image: new ol.style.Circle({
			radius: 7,
			fill: new ol.style.Fill({
				color: 'rgba(255, 255, 255, 0)'
			})
		})
	})
});

function mensagemAlerta(tipo, title, mensagem, icon, close){
	if (msg) {
		console.log("Mensagem " + " - " + tipo + " - " + mensagem);
		// getAlerta({tipo : tipo, title : title, mensagem : mensagem, icon : icon, close : close});
	}
}

function criarGeometria(geometry){
	
	switch(geometry.type) {
    case 'Point':
        
    	return "POINT (" + geometry.coordinates[0] + " " + geometry.coordinates[1] + ")";
    	
        break;
    case 'LineString':
        
    	var string = "LINESTRING (";
    	for (var i = 0; i < geometry.coordinates.length; i++) {
    		
    		if (geometry.coordinates.length - 1 == i) {
    			string += geometry.coordinates[i][0] + " " + geometry.coordinates[i][1] + ")";
    		}
    		else {
    			string += geometry.coordinates[i][0] + " " + geometry.coordinates[i][1] + ", ";
    		}
    	} 
    	
    	return string;
    	
        break;
    case 'Polygon':
        
    	var string = "POLYGON ((";
    	for (var i = 0; i < geometry.coordinates[0].length; i++) {
    		
    		if (geometry.coordinates[0].length - 1 == i) {
    			string += geometry.coordinates[0][i][0] + " " + geometry.coordinates[0][i][1] + "))";
    		}
    		else {
    			string += geometry.coordinates[0][i][0] + " " + geometry.coordinates[0][i][1] + ", ";
    		}
    	} 
    	
    	return string;
    	    	
        break;
    
    default:
        break;
	}
}