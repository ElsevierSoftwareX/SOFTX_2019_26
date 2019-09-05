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

                <form id="formularioGradeAmostral" action="<c:url value='editar/persiste' />" method="POST" >

                <input type="hidden" name="gradeAmostral.area.geom"  value='${gradeAmostral.area.geom}' id="poligonoProjetoArea">
                <input type="hidden" name="pontosAmostrais"  value='${pontoAmostral}' id="pontoAmostral">
                <input type="hidden" name="gradeAmostral.codigo" value="${codigoGradeAmostralAPI}"  id="codigoGradeAmostralAPI"   />
                <input type="hidden" name="gradeAmostral.id"      value="${gradeAmostral.id}" /> 
                <input type="hidden" name="gradeAmostral.area.id" value="${idArea}" /> 
                <input type="hidden" name="salvarAPI"       value="${salvarAPI}"  ID="salvarAPI"/>   
                <input type="hidden" name="gradeAmostral.tamx" value="${gradeAmostral.tamx}" /> 
                <input type="hidden" name="gradeAmostral.tamy" value="${gradeAmostral.tamy}" /> 		 



                <h4 class="text-center">
                    <fmt:message key="view.area.grade.editar.titulo" />
                </h4>

                <hr style="margin-top: 10px; margin-bottom: 10px;">

                <jsp:include page="../include/alerta.jsp"></jsp:include>

                    <div class="row">
                        <div class="col-md-12">
                            <label> 
                            <fmt:message key="view.area.grade.descricao" /> 
                            <span class="required-class"> * </span>
                        </label> 
                        <input type="text" class="form-control" name="gradeAmostral.descricao" value="${gradeAmostral.descricao}" maxlength="100" minlength="4" required>
                    </div>
                </div>

                <div style="position: absolute; z-index: 1; margin-top: 20px; margin-left: 10px;"> 

                    <div class="btn btn-danger" id="buttonExcluirGradeAmostral"> 
                        <span class="glyphicon glyphicon-remove"></span> 
                        <fmt:message key="view.excluir" />
                    </div>

                </div>

                <div class="row">
                    <div class="col-md-12">
                        <div id="map" class="map"></div>
                    </div>
                </div>	

                <hr>
                <div id="divSalvarAPI" class="text-center"> <input type="checkbox" name="salvarAPIaux" id="salvarAPIaux" class="text-center" value="nao" onchange="verificaChecks()"/>
                    <label class="text-center"> <fmt:message key="modal.salvargradeamostralapi" /></label>
                </div>
                <input style="float: right;" type="submit" class="btn btn-success" value="<fmt:message key="salvar"/>" />

                <a href="lista?id=${gradeAmostral.area.id}" class="btn btn-primary"> 
                    <span class="glyphicon glyphicon-menu-left"></span>
                    <fmt:message key="voltar"/>
                </a>

            </form>
        </div>

        <jsp:include page="../include/script.jsp"></jsp:include>

        <script type="text/javascript">
            document.getElementById("menuinicial").style.display = 'block';
            $(document).ready(function () {


                window.onload = function ()
                {
                    if (document.getElementById('codigoGradeAmostralAPI').value === "")
                    {
                        document.getElementById('divSalvarAPI').style.display = 'none';
                        $('#salvarAPI').val("nao");
                    } else {
                        document.getElementById('divSalvarAPI').style.display = 'block';
                        $('#salvarAPI').val("nao");
                    }
                };


            });

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


            var listPontoAmostral = JSON.parse($('#pontoAmostral').val());
            var listFinalGradeAmostral = new Array();
            var geojsonObject = {
                'type': 'FeatureCollection',
                'features': []
            };

            for (var i = 0; i < listPontoAmostral.length; i++) {
                var jsonAux = JSON.parse(listPontoAmostral[i]);
                //console.log("jsonAux: "+jsonAux);
                //console.log("jsonAuxID: "+jsonAux.id);
                //console.log(jsonAux.geometry.coordinates[0],jsonAux.geometry.coordinates[1]);
                var auxObj = {
                    'id': jsonAux.id,
                    //'codigo': jsonAux.codigo,
                    'geom': 'POINT(' + jsonAux.geometry.coordinates[0] + ' ' + jsonAux.geometry.coordinates[1] + ')'
                }
                listFinalGradeAmostral.push(auxObj);
                geojsonObject.features.push(jsonAux);
            }
            //console.log("pontoAmostral.val(json.stringify) **"+$("#pontoAmostral").val());
            $("#pontoAmostral").val(JSON.stringify(listFinalGradeAmostral));



            var vectorSourcePontoAmostra = new ol.source.Vector({
                features: (new ol.format.GeoJSON()).readFeatures(geojsonObject, {featureProjection: 'EPSG:3857'})
            });

            var vectorLayerPontoAmostra = new ol.layer.Vector({
                source: vectorSourcePontoAmostra
            });

            map.addLayer(vectorLayerPontoAmostra);

            var selectInteraction = new ol.interaction.Select();
            map.addInteraction(selectInteraction);

            var modifyInteraction = new ol.interaction.Modify({features: selectInteraction.getFeatures()});
            map.addInteraction(modifyInteraction);

            map.getView().fit(vectorSource.getExtent(), map.getSize());



            $("#buttonExcluirGradeAmostral").on('click', function () {
                map.getLayers().getArray()[2].getSource().removeFeature(featuresSelecionada);

                var arrayAux = new Array();
                var idselecionada = featuresSelecionada.getId();
                for (var i = 0; i < listFinalGradeAmostral.length; i++) {
                    if (listFinalGradeAmostral[i].id !== idselecionada) {
                        arrayAux.push(listFinalGradeAmostral[i]);
                    }
                }

                map.removeInteraction(selectInteraction);

                selectInteraction = new ol.interaction.Select();
                map.addInteraction(selectInteraction);

                listFinalGradeAmostral = arrayAux;

                $("#pontoAmostral").val(JSON.stringify(listFinalGradeAmostral));

                //featuresSelecionada = null;

                controlarFuncaoSelect();
                modifyInteraction = new ol.interaction.Modify({features: selectInteraction.getFeatures()});
                map.addInteraction(modifyInteraction);

                //$('#valorGradeAmostral').attr('data-id', '');

            });


            controlarFuncaoSelect();
            mificarAmostraSelecionada();
            modifyInteraction = new ol.interaction.Modify({features: selectInteraction.getFeatures()});
            map.addInteraction(modifyInteraction);

            modifyInteraction.on('modifyend', function (evt) {

                featuresSelecionada = evt.features.getArray();

                //$('#valorGradeAmostral').attr('data-id', featuresSelecionada.getId());

                var collection = evt.features;
                var featureClone = collection.item(0).clone();

                featureClone.getGeometry().transform('EPSG:3857', 'EPSG:4326');

                var geojson = formatGeoJSON.writeFeature(featureClone);

                mificarAmostraSelecionada(featuresSelecionada[0].getId(), geojson);

            });


            function mificarAmostraSelecionada(idGradeAmostral, geometry) {

                for (var i = 0; i < listFinalGradeAmostral.length; i++) {

                    if (listFinalGradeAmostral[i].id === idGradeAmostral) {

                        if (geometry === undefined) {

                            //listFinalGradeAmostral[i].geom = geometry;

                            $("#pontoAmostral").val(JSON.stringify(listFinalGradeAmostral));
                        } else {

                            var geo = JSON.parse(geometry);

                            listFinalGradeAmostral[i].geom = ('POINT(' + geo.geometry.coordinates[0] + ' ' + geo.geometry.coordinates[1] + ')');

                            $("#pontoAmostral").val(JSON.stringify(listFinalGradeAmostral));
                        }
                    }
                }
            }

            function controlarFuncaoSelect() {

                selectInteraction.on('select', function (evt) {

                    try {
                        featuresSelecionada = evt.selected[0];
                        //var valorAmostraSelecionada = obterValorAmostra(featuresSelecionada.getId()); 


                        //$('#valorGradeAmostral').attr('data-id', featuresSelecionada.getId());
                        //console.log("TRY *************** : " + $('#valorGradeAmostral').attr('data-id', featuresSelecionada.getId()));

                    } catch (e) {

                        //$('#valorGradeAmostral').attr('data-id', '');
                        console.log("CATCH *************** : " + e + $('#valorGradeAmostral').attr('data-id'));

                    }
                });
            }

            function verificaChecks() {
                if (document.getElementById("salvarAPIaux").checked === true) {
                    $('#salvarAPI').val("sim");
                    //alert( $('#salvarAPI').val());
                } else {
                    $('#salvarAPI').val("nao");
                }

            }

        </script>
    </body>
</html>