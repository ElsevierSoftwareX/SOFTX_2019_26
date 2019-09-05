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
        <script type="text/javascript" src="<c:url value='/resources/dist/jquery.min.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/ferramenta/bootstrap.min.js' />"></script>

        <title><fmt:message key="nome.projeto" /> <fmt:message key="view.login.titulo" /></title>

        <jsp:include page="../include/link.jsp"></jsp:include>

            <style type="text/css">
                .ol-attribution, .ol-zoom {
                    display: none;
                }
            </style>
        </head>

        <body>

        <jsp:include page="../menu/menu.jsp"></jsp:include>

            <div class="container" style="width: 900px; border: 1px solid #ddd;">

                <form id="formularioMapa" action="<c:url value='persiste/areaPoligono' />" method="POST">

                <input type="hidden" value="${idProjeto}" name="area.projeto.id" id="idProjeto">
                <input type="hidden" value='${area.geom}' name="area.geom" id="poligonoProjetoArea">
                <input type="hidden" value="${area.id}"   name="area.id"   id="idAreaEdicao">
                <input type="hidden" value="${area.usuario.codigo}" name="area.usuario.codigo" id="idUsuario">
                <input type="hidden" name="editar" id="editar" value="${editar}">

                <h4 class="text-center">
                    <fmt:message key="view.area.nova.mapa" />
                </h4>

                <hr style="margin-top: 10px; margin-bottom: 10px;">

                <jsp:include page="../include/alerta.jsp"></jsp:include>

                    <div class="row">
                        <div class="col-md-12">
                            <label> 
                            <fmt:message key="view.filtro.nome.area" /> 
                            <span class="required-class"> * </span>
                        </label> 
                        <input type="text" class="form-control" value="${area.descricao}" name="area.descricao" id="area.descricao" maxlength="100" minlength="4" required="required" >
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-10">
                        <label>
                            <fmt:message key="modal.area.solo" /> 
                            <span class="required-class"> * </span>
                        </label>
                        <select name="area.tipoSolo.codigo" class="form-control" required="required" >
                            <c:forEach items="${listaSolos}" var="s">
                                <option value="${s.codigo}" <c:if test="${s.codigo eq area.tipoSolo.codigo}">selected="selected"</c:if> >${s.descricaoPT}</option>
                            </c:forEach> 
                        </select>
                    </div>
                    <div class="col-md-2">
                        <a style="margin-top: 19px;" class="btn btn-default btn-block"  href="<c:url value='/tipoSolo/formulario?codigo=${idUsuario}' />"> 
                            <i class="glyphicon glyphicon-plus-sign"></i> 
                            <fmt:message key="view.novo" />
                        </a>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12">
                        <div id="map" class="map"></div>
                    </div>
                </div>	

                <hr>
                <div id="divSalvarAPI" class="text-center"> <input type="checkbox" name="salvarAPI" id="salvarAPI" class="text-center" value="nao" onchange="verificaChecks('salvarAPI')"/>
                    <label class="text-center"> <fmt:message key="modal.salvarareaapi" /></label>
                </div>
                <input id="botaosalvar" style="float: right;" type="submit" class="btn btn-success" value=<fmt:message key="cadastrar"/> />
                 <input id="botaoeditar" style="float: right; display: none;" type="submit" class="btn btn-success" value=<fmt:message key="salvar"/> />
                <a href="lista?id=${idProjeto}" class="btn btn-primary"> 
                    <span class="glyphicon glyphicon-menu-left"></span>
                    <fmt:message key="voltar"/>
                </a>

            </form>
        </div>

        <jsp:include page="../include/script.jsp"></jsp:include>

        <script type="text/javascript">
            document.getElementById("menuinicial").style.display = 'block';
            $(document).ready(function () {

                if (document.getElementById('editar').value === "editar") {
                    document.getElementById("botaosalvar").style.display = 'none';
                    document.getElementById("botaoeditar").style.display = 'block';
                } else {
                    document.getElementById("botaosalvar").style.display = 'block';
                    document.getElementById("botaoeditar").style.display = 'none';
                }
                
                window.onload = function ()
                {
                    if ((document.getElementById('editar').value) == "editar")
                    {
                        document.getElementById('divSalvarAPI').style.display = 'none';
                    } else {
                        document.getElementById('divSalvarAPI').style.display = 'block';
                    }
                };

                var formatGeoJSON = new ol.format.GeoJSON();


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

                if ($('#idAreaEdicao').val() === "") {

                    var vectorSource = new ol.source.Vector();
                    var vectorLayer = new ol.layer.Vector({source: vectorSource});

                    map.addLayer(vectorLayer);

                    drawInteraction = new ol.interaction.Draw({source: vectorSource, type: "Polygon"});
                    map.addInteraction(drawInteraction);

                    drawInteraction.on('drawend', function (e) {

                        var feature = e.feature;
                        map.removeInteraction(drawInteraction);

                        selectInteraction = new ol.interaction.Select();
                        map.addInteraction(selectInteraction);

                        selectInteraction.getFeatures().push(feature);
                        var featureClone = feature.clone();

                        featureClone.getGeometry().transform('EPSG:3857', 'EPSG:4326');

                        var geojson = formatGeoJSON.writeFeature(featureClone);

                        setPoligono(geojson);

                        modifyInteraction = new ol.interaction.Modify({features: selectInteraction.getFeatures()});
                        map.addInteraction(modifyInteraction);

                        modifyInteraction.on('modifyend', function (evt) {

                            var collection = evt.features;
                            var featureClone = collection.item(0).clone();

                            featureClone.getGeometry().transform('EPSG:3857', 'EPSG:4326');

                            var geojson = formatGeoJSON.writeFeature(featureClone);

                            setPoligono(geojson);
                        });
                    });

                } else {

                    var vectorSource = new ol.source.Vector({
                        features: (new ol.format.GeoJSON()).readFeatures($('#poligonoProjetoArea').val(), {featureProjection: 'EPSG:3857'})
                    });

                    //console.log("vectorSource area: " + vectorSource);

                    var vectorLayer = new ol.layer.Vector({
                        source: vectorSource
                    });

                    map.addLayer(vectorLayer);

                    var selectInteraction = new ol.interaction.Select();
                    map.addInteraction(selectInteraction);

                    var modifyInteraction = new ol.interaction.Modify({features: selectInteraction.getFeatures()});
                    map.addInteraction(modifyInteraction);

                    map.getView().fit(vectorSource.getExtent(), map.getSize());

                    modifyInteraction.on('modifyend', function (evt) {

                        var collection = evt.features;
                        var featureClone = collection.item(0).clone();

                        featureClone.getGeometry().transform('EPSG:3857', 'EPSG:4326');

                        var geojson = formatGeoJSON.writeFeature(featureClone);

                        setPoligono(geojson);

                    });
                }

                function setPoligono(geometria) {

                    geometria = JSON.parse(geometria);

                    var poligonoNovo = 'POLYGON((';

                    for (var i = 0; i < geometria.geometry.coordinates[0].length; i++) {

                        poligonoNovo += geometria.geometry.coordinates[0][i][0];
                        poligonoNovo += ' ';
                        poligonoNovo += geometria.geometry.coordinates[0][i][1];

                        if ((i + 1) !== geometria.geometry.coordinates[0].length) {
                            poligonoNovo += ', ';
                        }
                    }

                    poligonoNovo += '))';

                    //console.log(poligonoNovo);

                    $('#poligonoProjetoArea').val(poligonoNovo);
                }

            });


            function verificaChecks(salvarAPI) {
                if (document.getElementById(salvarAPI).checked == true) {
                    $('#salvarAPI').val("sim");
                    //alert( $('#salvarAPI').val());
                } else {
                    $('#salvarAPI').val("nao");
                }

            }

        </script>	

    </body>
</html>