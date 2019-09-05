function createVector(conf){
	
	var vector = "";
	
	if ( conf.title == null ) { 
		conf.title = "Camada"; 
	}
	
	if ( conf.source == null ) {
		console.log("Source é uma parametro importante. ex ( var exemplo = new ol.source.Vector(); )");
	}
	
	var fillColor   = 'rgba(255, 255, 255, 0.4)';
	var circleColor = '#ffcc33';
	var strokeColor = '#ffcc33';
	
	if(conf.tipo == "raio") {
		fillColor   = 'rgba(0, 255, 0, 0.4)';
		strokeColor = 'rgba(0, 255, 0, 1.0)';
	}
	else {
		if(conf.tipo == "distancia") {
			fillColor   = 'rgba(255, 0, 0, 0.4)';
			strokeColor = 'rgba(255, 0, 0, 1.0)';
		}
	}
	
	var visible = true;
	if ( !conf.visibleLayerSwitch ){
		visible = false;
	}
	
	vector = new ol.layer.Vector({
		title: conf.title,
		source: conf.source,
		visibleLayerSwitch: visible,
		style: new ol.style.Style({
		    fill: new ol.style.Fill({
		    	color: fillColor,
		    }),
		    stroke: new ol.style.Stroke({
		    	color: strokeColor,
		    	width: 2
		    }),
		    image: new ol.style.Circle({
		    	radius: 7,
		    	fill: new ol.style.Fill({
		    		color: circleColor
		    	})
		    })
		})
	});
	
	return vector;
	
}	