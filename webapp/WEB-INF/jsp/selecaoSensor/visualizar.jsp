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

        <jsp:include page="../include/link.jsp"></jsp:include>

            <style type="text/css">
                .ol-attribution, .ol-zoom {
                    display: none;
                }


                .ol-popup {
                    position: absolute;
                    background-color: white;
                    -webkit-filter: drop-shadow(0 1px 4px rgba(0,0,0,0.2));
                    filter: drop-shadow(0 1px 4px rgba(0,0,0,0.2));
                    padding: 15px;
                    border-radius: 10px;
                    border: 1px solid #cccccc;
                    bottom: 12px;
                    left: -50px;
                    min-width: 280px;
                }
                .ol-popup:after, .ol-popup:before {
                    top: 100%;
                    border: solid transparent;
                    content: " ";
                    height: 0;
                    width: 0;
                    position: absolute;
                    pointer-events: none;
                }
                .ol-popup:after {
                    border-top-color: white;
                    border-width: 10px;
                    left: 48px;
                    margin-left: -10px;
                }
                .ol-popup:before {
                    border-top-color: #cccccc;
                    border-width: 11px;
                    left: 48px;
                    margin-left: -11px;
                }
                .ol-popup-closer {
                    text-decoration: none;
                    position: absolute;
                    top: 2px;
                    right: 8px;
                }
                .ol-popup-closer:after {
                    content: "✖";
                }

            </style>
        </head>

        <body>

        <jsp:include page="../menu/menu.jsp"></jsp:include>

            <div class="container" style="width: 900px; border: 1px solid #ddd;">

                <input type="hidden" value="${idProjeto}" name="idProjeto" id="idProjeto">
            <input type="hidden" name="selecaoSensor.area.geom"  value='${selecaoSensor.area.geom}' id="poligonoProjetoArea">
            <input type="hidden" name="selecaoSensor.area.id" value="${selecaoSensor.area.id}" id="idArea" />
            <input type="hidden" name="amostras"  value='${amostras}' id="amostras">
            <input type="hidden" name="pontosSensores" value='${pontosSensores}' id="pontosSensores">
            <input type="hidden" name="selecaoSensor.id"      value="${selecaoSensor.id}" /> 



            <h4 class="text-center">
                <fmt:message key="view.selecaosensor.titulo" />
            </h4>

            <hr style="margin-top: 10px; margin-bottom: 10px;">

            <jsp:include page="../include/alerta.jsp"></jsp:include>

                <div class="row">
                    <div class="col-md-12">
                    <fmt:message key="view.area.selecaosensor.descricao" /> 
                    <input type="text" class="form-control" name="selecaoSensor.descricao" value="${selecaoSensor.descricao}" readonly="readonly">
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <fmt:message key="view.area.selecaosensor.fpi" /> 
                    <input type="text" class="form-control" name="selecaoSensor.fpi" value="${selecaoSensor.fpi}" readonly="readonly">
                </div>
                <div class="col-md-6">
                    <fmt:message key="view.area.selecaosensor.mpe" /> 
                    <input type="text" class="form-control" name="selecaoSensor.mpe" value="${selecaoSensor.mpe}" readonly="readonly">
                </div>
            </div>



            <div class="row">
                <div class="col-md-12" >
                    <div id="map" class="map" style="height: 400px;" >

                    </div>
                </div>
            </div>

            <div id="popup" class="ol-popup">
                <a href="#" id="popup-closer" class="ol-popup-closer"></a>
                <div id="popup-content"></div>
            </div>

            <hr>

            <div class="col-md-12" style=" padding-left: 40px;">
                <div id="amostras" style="display: block;" class="col-md-6">
                    <label> <fmt:message key="view.amostras.selecaosensor" />
                    </label>
                    <div style="overflow: auto; width: 350px; height: 200px; border: 1px solid #336699; padding-left: 5px;">
                        <c:forEach items="${selecaoSensor.amostras}" var="a">
                            ${a.descricao}<br>
                        </c:forEach>
                    </div>
                    <br><br>
                </div>
                <div id="divFormAmostra" style="display: block;" class="col-md-6">
                    <label> <fmt:message key="view.pontossensores.selecaosensor" />
                    </label>
                    <div style="overflow: auto; width: 350px; height: 200px; border: 1px solid #336699;  padding-left: 5px;">
                        <c:forEach items="${selecaoSensor.pontoSensor}" var="a">
                            ${a.pontoAmostral.the_geom}<br>
                        </c:forEach>
                    </div>
                    <br><br>
                </div>
            </div>


            <a href="lista?id=${selecaoSensor.area.id}" class="btn btn-primary"> 
                <span class="glyphicon glyphicon-menu-left"></span>
                <fmt:message key="voltar"/>
            </a>

            <div class="btn btn-danger modalExcluir" style="float: right;" id="${selecaoSensor.id}" data-titulo="<fmt:message key="view.selecaosensor.excluir.titulo"/> "${selecaoSensor.descricao}"
                 data-mensagem=" <fmt:message key="view.selecaosensor.excluir.mensagem"/>" data-excluir="<fmt:message key="view.selecaosensor.excluir"/> "> 
                <span class="glyphicon glyphicon-remove"></span> 
                <fmt:message key="view.excluir"/>
            </div>


        </div>

        <jsp:include page="../include/script.jsp"></jsp:include>
        <jsp:include page="../modal/excluir.jsp"></jsp:include>

            <script type="text/javascript">
                document.getElementById("menuinicial").style.display = 'block';

                var formatGeoJSON = new ol.format.GeoJSON();
                var featuresSelecionada;


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

                var vectorSource = new ol.source.Vector({
                    features: (new ol.format.GeoJSON()).readFeatures($('#poligonoProjetoArea').val(), {featureProjection: 'EPSG:3857'})
                });

                var vectorLayer = new ol.layer.Vector({
                    source: vectorSource
                });

                map.addLayer(vectorLayer);

                map.getView().fit(vectorSource.getExtent(), map.getSize());


                var listPontoSensor = JSON.parse($('#pontosSensores').val());
                var listFinalPontoSensor = new Array();

                var geojsonObject = {
                    'type': 'FeatureCollection',
                    'features': []
                };

                for (var i = 0; i < listPontoSensor.length; i++) {
                    var jsonAux = JSON.parse(listPontoSensor[i]);
                    //console.log("jsonAux: "+jsonAux);
                    //console.log("jsonAuxID: "+jsonAux.id);
                    //console.log(jsonAux.geometry.coordinates[0],jsonAux.geometry.coordinates[1]);
                    var auxObj = {
                        'id': jsonAux.id,
                        'the_geom': 'POINT(' + jsonAux.geometry.coordinates[0] + ' ' + jsonAux.geometry.coordinates[1] + ')'
                    }
                    geojsonObject.features.push(jsonAux);
                }
                //console.log("pontoAmostral.val(json.stringify) **"+$("#pontoAmostral").val());
                $("#pontosSensores").val(JSON.stringify(listFinalPontoSensor));



                var vectorSourcePontosSensores = new ol.source.Vector({
                    features: (new ol.format.GeoJSON()).readFeatures(geojsonObject, {featureProjection: 'EPSG:3857'})
                });

                var vectorLayerPontosSensores = new ol.layer.Vector({
                    source: vectorSourcePontosSensores
                });

                map.addLayer(vectorLayerPontosSensores);


                var container = document.getElementById('popup');
                var content = document.getElementById('popup-content');
                var closer = document.getElementById('popup-closer');


                /**
                 * Create an overlay to anchor the popup to the map.
                 */
                var overlay = new ol.Overlay({
                    element: container,
                    autoPan: true,
                    autoPanAnimation: {
                        duration: 250
                    }
                });

                map.addOverlay(overlay);

                /**
                 * Add a click handler to hide the popup.
                 * @return {boolean} Don't follow the href.
                 */
                closer.onclick = function () {
                    overlay.setPosition(undefined);
                    closer.blur();
                    return false;
                };

                /**
                 * Add a click handler to the map to render the popup.
                 */
                map.on('singleclick', function (evt) {
                    var coords = evt.coordinate;
                    var hdms = ol.coordinate.toStringXY(ol.proj.transform(coords, 'EPSG:3857',
                            'EPSG:4326'), 6);
                    content.innerHTML = '<p> <fmt:message key="longitudelatitude" /></p><code>' + hdms +
                            '</code>';
                    overlay.setPosition(coords);
                });

                $(".modalExcluir").on('click', function () {
                    $('#modalExcluirTitulo').text(this.attributes.getNamedItem('data-titulo').value);
                    $('#modalExcluirMensagem').text(this.attributes.getNamedItem('data-mensagem').value);
                    //$('#excluirConfirmar').html('<a class="btn btn-success" href="<c:url value="excluir?id='+ this.attributes.getNamedItem('id').value + '"/>">' + this.attributes.getNamedItem('data-excluir').value + '</a> ');
                    $('#excluirConfirmar1').html('<a class="btn btn-success" href="<c:url value="excluir?id='+ this.attributes.getNamedItem('id').value + '"/>">' + this.attributes.getNamedItem('data-excluir').value + '</a> ');
                    $('#myModalExcluir').modal('show');

                });
        </script>
    </body>
</html>