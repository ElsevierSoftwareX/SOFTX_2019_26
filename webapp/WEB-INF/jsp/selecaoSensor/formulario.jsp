<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="<c:url value='/resources/dist/jquery.min.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/ferramenta/bootstrap.min.js' />"></script>

        <link rel="stylesheet"
              href="https://openlayers.org/en/v4.6.5/css/ol.css" type="text/css">
        <script
        src="https://cdn.polyfill.io/v2/polyfill.min.js?features=requestAnimationFrame,Element.prototype.classList,URL"></script>
        <script src="https://openlayers.org/en/v4.6.5/build/ol.js"></script>

        <title><fmt:message key="nome.projeto" /> <fmt:message
                key="view.login.titulo" /></title>

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

                <form id="formularioSelecaoSensor" name="formularioSelecaoSensor"
                      action="<c:url value='persiste' />" method="POST"
                onsubmit="setPontosGrade()">

                <input type="hidden" name="idArea" value="${area.id}" id="idArea" />
                <input type="hidden" name="idProjeto" value="${area.projeto.id}"
                       id="idProjeto" /> <input type="hidden" value='${area.geom}'
                       name="geomArea" id="poligonoProjetoArea"> <input
                       type="hidden" name="formGrade" value="true" id="formGrade">
                <input type="hidden" name="selecaoSensor.area.id"
                       value="${selecaoSensor.amostra.area.id}" /> <input type="hidden"
                       name="gradeAmostral" value="${gradeAmostral}" id=gradeAmostral /> <input
                       type="hidden" name="pontosLong[]" id="pontosLong[]" value="[]">
                <input type="hidden" name="pontosLat[]" id="pontosLat[]" value="[]">

                <h3 class="text-center">
                    <fmt:message key="view.form.selecaosensores.titulo" />
                </h3>

                <hr style="margin-top: 10px; margin-bottom: 10px;">

                <jsp:include page="../include/alerta.jsp"></jsp:include>

                    <div class="row">
                        <div class="col-md-12">
                            <label> <fmt:message key="view.area" />
                        </label> <input type="text" class="form-control" value="${area.descricao}"
                                        disabled="disabled" id="descricaoarea">
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-4">
                        <label> <fmt:message key="view.selecaosensor.descricao" />
                            <span class="required-class"> * </span>
                        </label> <input type="text" class="form-control" name="descricaoSensor"
                                        value="${selecaoSensor.descricao}" id="descricaoSensor"
                                        maxlength="100" minlength="4" required>
                    </div>
                    <div class="col-md-4">
                        <label> <fmt:message key="view.sensor.grade" /> <span
                                class="required-class"> * </span>
                        </label> <input type="text" class="form-control" name="descricaoGrade"
                                        value="${gradeAmostral.descricao}" id="descricaoGrade"
                                        maxlength="100" minlength="4" required>
                    </div>
                    <div class="col-md-4">
                        <label> <fmt:message key="view.selecaosensor.quantidade" />
                            <span class="required-class"> * </span>
                        </label> <input type="number" class="form-control" name="numeroSensores"
                                        value="${quantidadeSensor}" id="numeroSensores" step="1"
                                        maxlength="1000" minlength="1" required>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-8">
                        <div id="map" class="map"></div>
                    </div>
                    <div id="longlat" class="col-md-4">
                        <h3 align="center">
                            <fmt:message key="longitudelatitude" />
                        </h3>
                        <h3 id="myposition" align="center" class="col-md-12"></h3>
                        <br><br>
                        <div style="overflow: auto; height: 200px; border: 0px solid #336699;  padding-left: 5px;">
                            <h4 id="plat"  align="center" class="col-md-12"></h4>
                        </div>
                    </div>
                </div>

                <hr>
                <div class="col-md-12" style=" padding-left: 40px;">
                    <div id="radioButtonGrade" style="display: block;" class="col-md-6">
                         <label> <fmt:message key="view.grade.sensor" /> <span
                                class="required-class"> * </span>
                        </label>

                        <div
                            style="overflow: auto; width: 350px; height: 200px; border: 1px solid #336699; padding-left: 5px;">
                            <input type="radio" name="radiograde" id="manual" value="manual"
                                   onchange="RadioGradeManual()">
                            <fmt:message key="view.novo.sensorGradeAmostralManual" />
                            <br> <input type="radio" name="radiograde" id="gradeporxy"
                                        value="gradeporxy" onchange="RadioGradeXY()">
                            <fmt:message key="view.novo.sensorGradeAmostralAutomatica" />
                            <br>
                            <div id="divFormGrade" style="display: none; width: 300px; ">
                                <label> <fmt:message key="view.amostra.pixel.x.metros" />
                                    <span class="required-class"> * </span>
                                </label> <input type="text" class="form-control" name="gradeAmostral.tamx"
                                                value="${gradeAmostral.tamx}" maxlength="10" minlength="1">
                                <br> <label> <fmt:message
                                        key="view.amostra.pixel.y.metros" /> <span
                                        class="required-class"> * </span>
                                </label> <input type="text" class="form-control" name="gradeAmostral.tamy"
                                                value="${gradeAmostral.tamy}" maxlength="10" minlength="1">
                            </div>

                        </div>


                    </div>

                    <div id="divFormAmostra" style="display: block;" class="col-md-6">
                        <label> <fmt:message key="view.area.amostra.sensor" /> <span
                                class="required-class"> * </span>
                        </label>
                        <div
                            style="overflow: auto; width: 350px; height: 200px; border: 1px solid #336699;  padding-left: 5px;">
                            <c:forEach items="${listaAmostra}" var="a">
                                <input type="checkbox" class="marcar" name="camposMarcados[]"
                                       value="${a.id}" id="camposMarcados" />${a.descricao }<br>
                            </c:forEach>
                        </div>
                    </div>
                </div>
                <div class="row">
                </div>

                <hr>

                <input style="float: right;" type="submit" class="btn btn-success"
                       value=<fmt:message key="cadastrar"/> /> <a
                       href="lista?id=${area.id}" class="btn btn-primary"> <span
                        class="glyphicon glyphicon-menu-left"></span> <fmt:message
                        key="voltar" />
                </a>
            </form>
        </div>

        <jsp:include page="../include/script.jsp"></jsp:include>

        <script type="text/javascript">

            document.getElementById("menuinicial").style.display = 'block';

            var draw, snap; // global so we can remove them later
            var featureClonePointLong = new Array();
            var featureClonePointLat = new Array();
            var camposMarcados = new Array();

            $(document)
                    .ready(
                            function () {

                                var formatGeoJSON = new ol.format.GeoJSON();

                                var raster = new ol.layer.Tile(
                                        {
                                            source: new ol.source.BingMaps(
                                                    {
                                                        key: 'AqBgyUHxs7_rEy3YhBQUI2CQqiA_nZ3ktBGI5gdB77OZSFUZqFOhfOEXWtYJ9lbx',
                                                        imagerySet: 'AerialWithLabels'
                                                    })
                                        });

                                var source = new ol.source.Vector();
                                var vector = new ol.layer.Vector({
                                    source: source,
                                    style: new ol.style.Style({
                                        fill: new ol.style.Fill({
                                            color: 'rgba(255, 255, 255, 0.2)'
                                        }),
                                        stroke: new ol.style.Stroke({
                                            color: '#ffcc33',
                                            width: 2
                                        }),
                                        image: new ol.style.Circle({
                                            radius: 5,
                                            fill: new ol.style.Fill({
                                                color: '#CC0000'
                                            })
                                        })
                                    })
                                });

                                var map = new ol.Map({
                                    layers: [raster, vector],
                                    target: 'map',
                                    view: new ol.View({
                                        center: new ol.geom.Point([
                                            -6217890.205764902,
                                            -1910870.6048274133])
                                                .getCoordinates(),
                                        zoom: 4
                                    })
                                });

                                function addInteractions() {
                                    draw = new ol.interaction.Draw({
                                        source: vector.getSource(),
                                        type: /** @type {ol.geom.GeometryType} */
                                                'Point'
                                    });
                                    map.addInteraction(draw);
                                    snap = new ol.interaction.Snap({
                                        source: vector.getSource()
                                    });
                                    map.addInteraction(snap);

                                }

                                addInteractions();

                                source.on('addfeature', function (evt) {
                                    var feature = evt.feature;
                                    var coords = feature.getGeometry()
                                            .getCoordinates();
                                    coords = ol.proj.transform(coords, 'EPSG:3857',
                                            'EPSG:4326');
                                    var lon = coords[0];
                                    var lat = coords[1];
                                    document.getElementById("plat").innerHTML = document.getElementById("plat").innerHTML + ol.coordinate.toStringXY(coords, 6) + "\n";
                                    //console.log("long: " + lon);
                                    //console.log("lat: " + lat);
                                    featureClonePointLong.push(lon);
                                    featureClonePointLat.push(lat);
                                    //console
                                    //        .log("coordenadas long: "
                                    //                + featureClonePointLong
                                    //                .toString()
                                    //                + " Lat: "
                                    //                + featureClonePointLat
                                    //                .toLocaleString());
                                });

                                var template = '{x}  {y}';

                                var mousePosition = new ol.control.MousePosition({
                                    coordinateFormat: function (coord) {
                                        return ol.coordinate.format(coord,
                                                template, 6);
                                    },
                                    projection: 'EPSG:4326',
                                    target: document.getElementById('myposition'),
                                    undefinedHTML: '&nbsp;'
                                });

                                map.addControl(mousePosition);

                                var vectorSource = new ol.source.Vector({
                                    features: (new ol.format.GeoJSON())
                                            .readFeatures($('#poligonoProjetoArea')
                                                    .val(), {
                                                featureProjection: 'EPSG:3857'
                                            })
                                });

                                var vectorLayer = new ol.layer.Vector({
                                    source: vectorSource
                                });

                                map.addLayer(vectorLayer);

                                var selectInteraction = new ol.interaction.Select();
                                map.addInteraction(selectInteraction);

                                var modifyInteraction = new ol.interaction.Modify({
                                    features: selectInteraction.getFeatures()
                                });
                                map.addInteraction(modifyInteraction);

                                map.getView().fit(vectorSource.getExtent(),
                                        map.getSize());

                                modifyInteraction.on('modifyend', function (evt) {

                                    var collection = evt.features;
                                    var featureClone = collection.item(0).clone();

                                    featureClone.getGeometry().transform(
                                            'EPSG:3857', 'EPSG:4326');

                                    var geojson = formatGeoJSON
                                            .writeFeature(featureClone);

                                    setPoligono(geojson);

                                });

                                function setPoligono(geometria) {

                                    geometria = JSON.parse(geometria);

                                    var poligonoNovo = 'POLYGON((';
                                    //console
                                    //        .log("coordenadas da area: "
                                    //                + geometria.geometry.coordinates[0].length);
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

                                function marcardesmarcar() {
                                    $('.marcar').each(
                                            function () {
                                                if ($(".marcar").prop("checked")) {
                                                    $(this).attr("checked", false);
                                                } else {
                                                    $(this).attr("checked", true);
                                                    camposMarcados.push($(this)
                                                            .val());
                                                    $('#camposMarcados[]').val(
                                                            camposMarcados);
                                                    //console.log("Campos Marcados: "
                                                    //        + camposMarcados);
                                                }
                                            });
                                }


                            });

            function setPontosGrade() {
                $("input[name='pontosLat[]']").val(featureClonePointLat);
                $("input[name='pontosLong[]']").val(featureClonePointLong);
            }

            function RadioGradeManual() {
                $("#divFormGrade").hide();
                $("#formGrade").val("true");
            }

            function RadioGradeXY() {
                $("#divFormGrade").show();
                $("#formGrade").val("false");
            }
        </script>

    </body>
</html>