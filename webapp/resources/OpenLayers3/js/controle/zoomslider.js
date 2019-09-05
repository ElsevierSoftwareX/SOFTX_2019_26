var zoomslider;

$(document).ready(function(){
	
	zoomslider  = new ol.control.ZoomSlider();

	map.addControl(zoomslider);

});