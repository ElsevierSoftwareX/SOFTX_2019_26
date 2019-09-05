function gerarNovoStyle(fillColor, fillOpacidade, strokeColor, strokeOpacidade, strokeWidth, imageRadius){

	if ( fillColor       == "" ) { fillColor       = '255, 255, 255'; } else { fillColor   = hexToRgb(fillColor);   }
	if ( fillOpacidade   == "" ) { fillOpacidade   = '0.4'; }
	if ( strokeColor     == "" ) { strokeColor     = '255, 204, 51';  } else { strokeColor = hexToRgb(strokeColor); } 
	if ( strokeOpacidade == "" ) { strokeOpacidade = '1'; }
	if ( strokeWidth     == "" ) { strokeWidth     = 2; }
	if ( imageRadius     == "" ) { imageRadius     = 7; }

	fillColor   = 'rgba('+ fillColor   + ', ' + fillOpacidade   + ')';
	strokeColor = 'rgba('+ strokeColor + ', ' + strokeOpacidade + ')';
	
	return new ol.style.Style({
		fill: new ol.style.Fill({
			color: fillColor,
		}),
		stroke: new ol.style.Stroke({
			color: strokeColor,
			width: strokeWidth
		}),
		image: new ol.style.Circle({
			fill: new ol.style.Fill({
				color: strokeColor
			}),
			radius: imageRadius
		})
	});
}

function hexToRgb(hex) {
    var bigint = parseInt(hex, 16);
    var r = (bigint >> 16) & 255;
    var g = (bigint >> 8) & 255;
    var b = bigint & 255;

    return r + "," + g + "," + b;
}