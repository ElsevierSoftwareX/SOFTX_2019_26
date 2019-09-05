var popoverAtivo  = null;
var elementoClick = null;

$(document).ready(function() {


});

function addLayerSwitch(div, auto){

	var html = '';

	html += '<div id="conteiner-layerSwitch" title="Camadas">';
	html += '	<div id="layertree" class="tree"></div>';
	html += '	<hr style="margin-top: -10px; margin-bottom: 10px;">';
	
	html += '	<div class="form-group">';
	html += '	 	<div class="input-group">';
	html += '			<input type="text" class="form-control" placeholder="Filtro..." id="filtrarLayerSwitch">';
	html += '			<span class="input-group-addon">';
	html += '				<span class="glyphicon glyphicon-search"     id="executarFiltro" style="margin-right: 30px;"></span>';
	html += '				<span class="glyphicon glyphicon-arrow-up"   id="layerUpLayerSwitch"  ></span>';
	html += '				<span class="glyphicon glyphicon-arrow-down" id="layerDownLayerSwitch"></span>';
	html += '			</div>';
	html += '		</div>';
	html += '	</div>';
	html += '</div>';

	div.html(html);

	$("#conteiner-layerSwitch").dialog({ position : [150, 150], width : 500, autoOpen : auto, resizable: false });

	$("#executarFiltro").click(function(){

		atualizaLayerSwitch( $("#filtrarLayerSwitch").val() );
	
	});
	
	$("#layerUpLayerSwitch").click(function(){
		
		var layerId = $(".selectedCamadaLayerSwitch").data('layerid');
		var layer   = findBy( map.getLayerGroup(), 'id', layerId ); 
		
		var layers = map.getLayers();
        var index = indexOf(layers, layer);
        
        if (index < layers.getLength() - 1) {
            
        	var next = layers.item(index + 1);
            
        	layers.setAt(index + 1, layer);
            layers.setAt(index, next);
            
            var elem = $('ul.layerstack li[data-layerid="' + layer.get('id') + '"]');
            
            elem.prev().before(elem);
            
            atualizaLayerSwitch();
        }
        
        $('div[data-layerid="'+ layer.get('id') +'"]').addClass("selectedCamadaLayerSwitch");
        
	});	
	
	$("#layerDownLayerSwitch").click(function(){
		
		var layerId = $(".selectedCamadaLayerSwitch").data('layerid');
		var layer   = findBy( map.getLayerGroup(), 'id', layerId ); 
		
		var layers = map.getLayers();
        var index = indexOf(layers, layer);
		
		if (index > 0) {
            
			var prev = layers.item(index - 1);
           
			if ((prev.get('title') != 'Camadas Bases') && (prev.get('visibleLayerSwitch') != false)){
			
				layers.setAt(index - 1, layer);
	            layers.setAt(index, prev);
	
	            var elem = $('ul.layerstack li[data-layerid="' + layer.get('id') + '"]');
	            
	            elem.next().after(elem);
	            
	            atualizaLayerSwitch();
	            
	            $('div[data-layerid="'+ layer.get('id') +'"]').addClass("selectedCamadaLayerSwitch");
			}
		}
	});
}

function atualizaLayerSwitch(filtro){

	initializeTree(filtro);
	
	$("#layertree .glyphicon-unchecked, #layertree .glyphicon-check").on('click', function(){
		
		if( $(this).attr("class").search('camadaBase') < 0 ){
		
			var layerId = $(this).data('layerid');
			var layer   = findBy( map.getLayerGroup(), 'id', layerId );
	
			layer.setVisible(!layer.getVisible());
	
			if (layer.getVisible()) {
				$(this).removeClass('glyphicon-unchecked').addClass('glyphicon-check');
			} 
			else {
				$(this).removeClass('glyphicon-check').addClass('glyphicon-unchecked');
			}
		}
		else {
			
			var listaCamadaBase = $(".camadaBase");

			for (var i = 0; i < listaCamadaBase.length; i++) {

				var layerId = listaCamadaBase[i].attributes.getNamedItem('data-layerid').value;
				var layer   = findBy(map.getLayerGroup(), 'id', layerId);

				listaCamadaBase.removeClass('glyphicon-check').addClass('glyphicon-unchecked');
				layer.setVisible(false);

				if ( listaCamadaBase[i] === $(this)[0] ) {

					var layername = $(this).data('layerid');
					var layer     = findBy(map.getLayerGroup(), 'id', layername);

					layer.setVisible(true);

					try{
						$(this).removeClass('glyphicon-unchecked').addClass('glyphicon-check');
					}catch (e) {}

					break;
				}
			}
		}
	});
	
	$("#layertree .glyphicon-comment").on('click', function(){
		
		var legenda  = $(this).parent().find('img');
		
		if (legenda.is(":visible")){
			legenda.hide('fast');
		}
		else{
			legenda.show('fast');
		}		
	});	
	
	$("#layertree .glyphicon-search").on('click', function(){
		
		var layerid = $(this).data('layerid');
		var layer   = findBy( map.getLayerGroup(), 'id', layerid );
		
		var extent;
		try{
			extent = layer.getLayers().getArray()[0].getSource().getExtent();
		}catch (e) {
			extent = layer.getExtent()
		}
		
		map.getView().fitExtent(extent, map.getSize());		
	});
	
	$("#layertree .layerstack").on('click', function(){
		
		$("#layertree .layerstack").removeClass('selectedCamadaLayerSwitch');
		$(this).addClass('selectedCamadaLayerSwitch');
		
	});
	
	
	$(".menuLayerSwitch").on('click', function(){

		if ( popoverAtivo !== null) {
			popoverAtivo.popover('hide');
			popoverAtivo = null;
		}

		if (elementoClick === null || elementoClick !== this.attributes.getNamedItem('data-layerNome').value) {
			popoverAtivo = $(this);
			$(this).popover('show');
			elementoClick = this.attributes.getNamedItem('data-layerNome').value;
		} else {
			elementoClick = null;
		}	

		$('.opacity').slider().on('slide', function(ev){ 

			$("#valorOpacity").text((ev.value).toFixed(1));
			var layername = $(this).data('layerid');
			var layer = findBy(map.getLayerGroup(), 'id', layername);
			layer.setOpacity(ev.value);

		});	

		$( ".popover" ).mouseleave(function() {
			$(this).popover('hide');
			popoverAtivo = null;
			elementoClick = null;
		});

	});
}

var selectCB = true;

var controler = 1;
var arrayControler = new Array();

function buildLayerTree(layer, filtro) {

	var elem, div = '', title = layer.get('title') ? layer.get('title') : "Camadas";
	
	var idCamada    = layer.get('id')   ? layer.get('id')   : "";
	var nameCamada  = layer.get('name') ? layer.get('name') : "";
	var urlServidor = layer.get('url')  ? layer.get('url')  : "";
		
	controler ++;

	if ((filtro == null || filtro == "") || title.toLowerCase().indexOf(filtro.toLowerCase()) >= 0) {	
		
		if (layer.getLayers) {
			
			if ( (layer.get('camadaBase')) && ((layer.getLayers().getArray()).length != 1) ) {
				div += '<li>'; 
				div += '	<span>';
				div += '		<i data-layerid="' + idCamada + '" class="glyphicon glyphicon-globe"> </i>';
				div += '		<div class="inlineblock" title="'+ title +'">' + tamanhoMaximo(title, 60) + '</div>';
				div += '	</span>';
			}
			else {
				if (title == 'Camadas') {
					div += '<li>'; 
					div += '	<span>';
					div += '		<i data-layerid="' + idCamada + '" class="glyphicon glyphicon-globe allCamadas"></i>'; 
					div += '		<div class="inlineblock" title="'+ title +'">' + tamanhoMaximo(title, 60) + '</div>';
					div += '	</span>';

				} 
				else {
					div += '<li>'; 
					div += '	<span>'; 
					div += '		<i data-layerid="' + idCamada + '" class="glyphicon glyphicon-check" />'; 
					div += '		<i data-toggle="tooltip" data-placement="top" title="Alto Zoom" data-layerid="' + idCamada + '" class="glyphicon glyphicon-search" />';
					div += '		<i data-toggle="tooltip" data-placement="top" title="Legenda"   data-layerid="' + idCamada + '" class="glyphicon glyphicon-comment" />';					
					div += '		<div class="inlineblock layerstack" title="'+ title +'" data-layerid="' + idCamada + '"> ' + tamanhoMaximo(title, 60) + '</div>';
					div += '	</span>';	
				}
			}	
		}
		else {
			if ( layer.get('camadaBase') ) {
				if ( selectCB ) {
					
					div += '<li>'; 
					div += '	<span>';
					div += '		<i data-layerid="' + idCamada + '" class="glyphicon glyphicon-check camadaBase"></i>';
					div += '		<div class="inlineblock" title="'+ title +'">' + tamanhoMaximo(title, 60) + '</div>';
					div += '	</span>';
					
					layer.setVisible(selectCB);
					selectCB = false;
				}
				else {
					div += '<li>';
					div += '	<span>';
					div += '		<i data-layerid="' + idCamada + '" class="glyphicon glyphicon-unchecked camadaBase"></i>';
					div += '		<div class="inlineblock" title="'+ title +'">' + tamanhoMaximo(title, 60) + '</div>';
					div += '	</span>';
					layer.setVisible(selectCB);
				}
			}
			else {
				if(layer.getVisible()){
					
					div += '<li>';
					div += '	<span>';
					div += '		<i data-layerid="' + idCamada + '" class="glyphicon glyphicon-check" />';
					div += '		<i data-toggle="tooltip" data-placement="top" title="Alto Zoom" data-layerid="' + idCamada + '" class="glyphicon glyphicon-search" />';
					div += '		<i data-toggle="tooltip" data-placement="top" title="Legenda"   data-layerid="' + idCamada + '" class="glyphicon glyphicon-comment" />';
					div += '		<div class="inlineblock layerstack" title="'+ title +'" data-layerid="' + idCamada + '">' + tamanhoMaximo(title, 60) + '</div>';
					div += '		<br />';
					div += '		<img style="margin-left: 30px; display: none;" src="'+ urlServidor + legendaBase + nameCamada +'" />';
					div += '	</span>';
					
				}
				else{
					div += '<li>';
					div += '	<span>';
					div += '		<i data-layerid="' + idCamada + '" class="glyphicon glyphicon-unchecked" />';
					div += '		<i data-toggle="tooltip" data-placement="top" title="Alto Zoom" data-layerid="' + idCamada + '" class="glyphicon glyphicon-search" />';
					div += '		<i data-toggle="tooltip" data-placement="top" title="Legenda"   data-layerid="' + idCamada + '" class="glyphicon glyphicon-comment" />';
					div += '		<div class="inlineblock layerstack" title="'+ title +'" data-layerid="' + idCamada + '">'+ tamanhoMaximo(title, 60) +'</div>';
					div += '		<br />';
					div += '		<img style="margin-left: 30px; display: none;" src="'+ urlServidor + legendaBase + nameCamada +'" />';
					div += '	</span>';
					layer.setVisible(false);
				}
			}		
		}
	}	

	if (layer.getLayers) {

		var sublayersElem = ''; 
		var layers = layer.getLayers().getArray();
		var len    = layers.length;

		controler = 1;
		
		arrayControler.push(len);

		for (var i = len - 1; i >= 0; i--) {
			if ( layers[i].get('visibleLayerSwitch') != false ){
				sublayersElem += buildLayerTree(layers[i], filtro);
			}
		}

		controler = 1;
		arrayControler = new Array();

		selectCB = true;
		
		elem = div + ' <ul class="ul-lista-camada-ordenacao">' + sublayersElem + '</ul> </li>';

	} 
	else {
		elem = div + " </li>";
	}

	return elem;
}

function initializeTree(filtro) {

	var elem = buildLayerTree(map.getLayerGroup(), filtro);

	$('#layertree').empty().append(elem);

	$('.menuLayerSwitch').popover({html: true, trigger: "click", placement: 'right'});

	$('.tree li:has(ul)').addClass('parent_li').find(' > span').attr('title', 'Collapse this branch');

	$('.inlineblock').on('click', function(e) {
		
		var children = $(this).parent().parent().find('> ul > li');
		
		if (children.is(":visible")) {
			children.hide('fast');

			$(this).attr('title', 'Expand this branch').find(' > i').addClass('glyphicon-plus').removeClass('glyphicon-minus');
		} 
		else {
			children.show('fast');
			
			$(this).attr('title', 'Collapse this branch').find(' > i').addClass('glyphicon-minus').removeClass('glyphicon-plus');
		}
		 
		e.stopPropagation();
	});	
}

function findBy(layer, key, value) {

	if (layer.get(key) === value) {
		return layer;
	}

	if (layer.getLayers) {

		var layers = layer.getLayers().getArray(),
		len = layers.length, result;

		for (var i = 0; i < len; i++) {
			result = findBy(layers[i], key, value);

			if (result) {
				return result;
			}
		}
	}
	return null;
}

function openLayerSwitch(){
	
	$("#conteiner-layerSwitch").dialog("open");

}

function tamanhoMaximo(title, tamanho){
	
	if(title.length > tamanho){ 
		return title.slice(0, tamanho) + '...'; 
	}
	else{
		return title;
	}
}

function indexOf(layers, layer) {
    var length = layers.getLength();
    for (var i = 0; i < length; i++) {
        if (layer === layers.item(i)) {
            return i;
        }
    }
    return -1;
}