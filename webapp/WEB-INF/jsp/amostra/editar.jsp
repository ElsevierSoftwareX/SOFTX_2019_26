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

                <form id="formularioAmostra" action="<c:url value='editar/persiste' />" method="POST" >

                <input type="hidden"                           value='${amostra.area.geom}' id="poligonoProjetoArea">
                <input type="hidden" name="pontosDasAmostras"  value='${pontoAmostra}'      id="pontoAmostra">

                <input type="hidden" name="amostra.codigo" value="${codigoAmostraAPI}"  id="codigoAmostraAPI"   />
                <input type="hidden" name="amostra.id" value="${amostra.id}" /> 
                <input type="hidden" name="amostra.area.id" value="${amostra.area.id}" /> 
                <input type="hidden" name="salvarAPI"       value="${salvarAPI}"  ID="salvarAPI"/>                

                <%--<input type="hidden" name="amostra.pixelX" value="${amostra.pixelX}" /> 
                <input type="hidden" name="amostra.pixelY" value="${amostra.pixelY}" /> 		 
                --%>		


                <h4 class="text-center">
                    <fmt:message key="view.area.amostra.editar.titulo" />
                </h4>

                <hr style="margin-top: 10px; margin-bottom: 10px;">

                <jsp:include page="../include/alerta.jsp"></jsp:include>

                    <div class="row">
                        <div class="col-md-12">
                            <label> 
                            <fmt:message key="view.area.amostra.descricao" /> 
                            <span class="required-class"> * </span>
                        </label> 
                        <input type="text" class="form-control" name="amostra.descricao" value="${amostra.descricao}" maxlength="100" minlength="4" required pattern="([a-zA-Z]*([- -_]*[0-9]*[a-zA-Z- -_]*)|([a-zA-Z]*[- -_]*[0-9]*)[0-9]*)+">
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-10">
                        <label>
                            <fmt:message key="view.area.amostra.atributo" /> 
                            <span class="required-class"> * </span>
                        </label>

                        <select name="amostra.atributo.codigo" class="form-control" required="required" >
                            <c:forEach items="${listaAtributo}" var="a">
                                <option value="${a.codigo}" <c:if test="${a.codigo eq amostra.atributo.codigo}">selected="selected"</c:if> >${a.descricaoPT}</option>
                            </c:forEach> 
                        </select>

                    </div>
                    <div class="col-md-2">
                        <a style="margin-top: 19px;" class="btn btn-default btn-block"  href="<c:url value='/atributo/lista'/>"> 
                            <i class="glyphicon glyphicon-plus-sign"></i> 
                            <fmt:message key="view.novo" />
                        </a>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <label>
                            <fmt:message key="view.area.amostra.valor" /> 
                        </label>
                        <input type="text" pattern="[0-9]*[.]?[0-9]*" data-id="" class="form-control" id="valor">
                    </div>
                    <div class="col-md-6">
                        <div style="margin-top: 19px;" class="btn btn-success btn-block" id="atualizarValorAmostra" > 
                            <i class="glyphicon glyphicon-ok"></i> 
                            <fmt:message key="view.atualizar.valor.amostra"/>
                        </div>
                    </div>
                </div>

                <div style="position: absolute; z-index: 1; margin-top: 20px; margin-left: 10px;"> 

                    <div class="btn btn-danger" id="buttonExcluirAmostra"> 
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
                    <label class="text-center"> <fmt:message key="modal.salvaramostraapi" /></label>
                </div>

                <input style="float: right;" type="submit" class="btn btn-success" value="<fmt:message key="salvar"/>" />

                <a href="lista?id=${amostra.area.id}" class="btn btn-primary"> 
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
                    if (document.getElementById('codigoAmostraAPI').value === "")
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





            var listPontoAmostra = JSON.parse($('#pontoAmostra').val());
            var listFinalAmostra = new Array();

            var geojsonObject = {
                'type': 'FeatureCollection',
                'features': []
            };

            for (var i = 0; i < listPontoAmostra.length; i++) {
                var jsonAux = JSON.parse(listPontoAmostra[i]);

                var auxObj = {
                    'id': jsonAux.id,
                    //'codigo': jsonAux.codigo,
                    'geom': 'POINT(' + jsonAux.geometry.coordinates[0] + ' ' + jsonAux.geometry.coordinates[1] + ')',
                    'valor': jsonAux.valor
                }

                listFinalAmostra.push(auxObj);

                geojsonObject.features.push(jsonAux);
            }

            $("#pontoAmostra").val(JSON.stringify(listFinalAmostra));




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



            $("#buttonExcluirAmostra").on('click', function () {

                map.getLayers().getArray()[2].getSource().removeFeature(featuresSelecionada);

                var arrayAux = new Array();
                for (var i = 0; i < listFinalAmostra.length; i++) {
                    //console.log("AMOSTRA* : " + $('#valor').attr('data-id'));
                    if (listFinalAmostra[i].id !== $('#valor').attr('data-id')) {

                        arrayAux.push(listFinalAmostra[i]);
                    }
                }

                map.removeInteraction(selectInteraction);

                selectInteraction = new ol.interaction.Select();

                map.addInteraction(selectInteraction);

                listFinalAmostra = arrayAux;

                $("#pontoAmostra").val(JSON.stringify(listFinalAmostra));

                featuresSelecionada = null;

                controlarFuncaoSelect();

                $('#valor').val('0');
                $('#valor').attr('data-id', '');

            });

            controlarFuncaoSelect();

            modifyInteraction.on('modifyend', function (evt) {

                featuresSelecionada = evt.features.getArray();

                if (obterValorAmostra(featuresSelecionada[0].getId()) == 'null') {
                    $('#valor').val('0');
                } else {
                    $('#valor').val(obterValorAmostra(featuresSelecionada[0].getId()));
                }

                $('#valor').attr('data-id', featuresSelecionada[0].getId());


                var collection = evt.features;
                var featureClone = collection.item(0).clone();

                featureClone.getGeometry().transform('EPSG:3857', 'EPSG:4326');

                var geojson = formatGeoJSON.writeFeature(featureClone);

                mificarAmostraSelecionada(featuresSelecionada[0].getId(), geojson)

            });

            $("#atualizarValorAmostra").on('click', function () {

                mificarAmostraSelecionada($('#valor').attr('data-id'), undefined, $('#valor').val());

            });

            function obterValorAmostra(idAmostra) {

                for (var i = 0; i < listFinalAmostra.length; i++) {

                    var jsonAux = listFinalAmostra[i];

                    if (jsonAux.id === idAmostra) {
                        return jsonAux.valor;
                    }
                }
            }

            function mificarAmostraSelecionada(idAmostra, geometry, valor) {

                for (var i = 0; i < listFinalAmostra.length; i++) {

                    if (listFinalAmostra[i].id === idAmostra) {

                        if (geometry === undefined) {

                            listFinalAmostra[i].valor = valor;

                            $("#pontoAmostra").val(JSON.stringify(listFinalAmostra));
                        } else {

                            var geo = JSON.parse(geometry);

                            listFinalAmostra[i].geom = ('POINT(' + geo.geometry.coordinates[0] + ' ' + geo.geometry.coordinates[1] + ')');

                            $("#pontoAmostra").val(JSON.stringify(listFinalAmostra));
                        }
                    }
                }
            }

            function controlarFuncaoSelect() {

                selectInteraction.on('select', function (evt) {

                    try {
                        featuresSelecionada = evt.selected[0];
                        var valorAmostraSelecionada = obterValorAmostra(featuresSelecionada.getId());

                        if (valorAmostraSelecionada == 'null') {
                            valorAmostraSelecionada = '0';
                        }

                        $('#valor').val(valorAmostraSelecionada);
                        $('#valor').attr('data-id', featuresSelecionada.getId());

                    } catch (e) {
                        //console.log("testando se cai no catch");
                        $('#valor').val('0');
                        $('#valor').attr('data-id', '');
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