var mousePositionControl;

function addMousePositionControl(qtde, projection, target){
	
	mousePositionControl = new ol.control.MousePosition({
		coordinateFormat: ol.coordinate.createStringXY(qtde),
		      projection: projection,
		          target: target,
		       className: "custom-mouse-position",
		   undefinedHTML: "&nbsp;"
	});
	
	map.addControl(mousePositionControl);
	
}