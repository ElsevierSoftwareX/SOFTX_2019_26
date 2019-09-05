var map;
var overlay;
var element = document.getElementById('popup');
var content = document.getElementById('popup-content');
var closer = document.getElementById('popup-closer');
var vectorLayerAreas;
var vectorColeta;
var vectorLayer;
var vectorLayerInterpolacao;
var idmapa;
var contLinha = 5;
var legenda;
var cores;
var dex;
var atex;
var corx;
var tamanhox;
var descricaox;
var datax;
var atributox;
var siglax;
var unidadex;
var valorx;
var parametrox;
$(document).ready(function () {
    document.getElementById("menuinicial").style.display = 'block';
    var camadaBase1 = new ol.layer.Tile({
        title: "Camada Base - Satelite",
        camadaBase: true,
        source: new ol.source.BingMaps({
            key: 'AqBgyUHxs7_rEy3YhBQUI2CQqiA_nZ3ktBGI5gdB77OZSFUZqFOhfOEXWtYJ9lbx',
            imagerySet: 'Aerial'
        })
    });
    var layerStm = new ol.layer.Group({
        layers: [camadaBase1],
        camadaBase: true,
        title: 'Camadas Bases'

    });
    overlay = new ol.Overlay(/** @type {olx.OverlayOptions} */ ({
        element: element,
        positioning: 'bottom-center',
        autoPan: true,
        autoPanAnimation: {
            duration: 250
        },
        offset: [0, -10]
    }));
    map = new ol.Map({
        target: 'mapa',
        layers: [
            layerStm
                    /*layerStm*/
        ],
        view: new ol.View({
            center: ol.proj.transform([-53.0978, -24.6924], 'EPSG:4326', 'EPSG:3857'),
            zoom: 3
        }),
        overlays: [overlay]
    });
    addMousePositionControl(4, "EPSG:4326", document.getElementById("mousePosition"));
    $(".listaInterpolacaoSelecionado").on('click', function () {

        $.ajax({
            url: 'interpolacao',
            type: 'GET',
            dataType: 'json',
            data: {
                'id': this.attributes.getNamedItem('data-interpoladorId').value
            },
            success: function (json) {

                map.removeLayer(vectorLayerAreas);
                map.removeLayer(vectorLayer);
                map.removeLayer(vectorLayerInterpolacao);
                var format = new ol.format.GeoJSON();
                vectorLayerInterpolacao = new ol.layer.Vector({
                    source: new ol.source.StaticVector({
                        format: format,
                        projection: 'EPSG:3857'
                    }),
                    style: setNewStyle
                });
                var sourceInterpolacao = vectorLayerInterpolacao.getSource();
                var minimo, maximo, intervalo;
                idmapa = json.id;
                for (var i = 0; i < json.pixelMapasTrans.length; i++) {

                    var ponto = JSON.parse(json.pixelMapasTrans[i].geom);
                    var features = sourceInterpolacao.readFeatures(json.pixelMapasTrans[i].geom);
                    sourceInterpolacao.addFeatures(features);
                    minimo = ponto.properties.minimo;
                    maximo = ponto.properties.maximo;
                    intervalo = ponto.properties.intervalo;
                }

                map.addLayer(vectorLayerInterpolacao);
                $("#myModalListaInterpolacao").modal("hide");
                htmlLegenda(minimo, maximo, intervalo, json.classesTran);
                map.getView().fitExtent(sourceInterpolacao.getExtent(), map.getSize());
                setAreaProjetoPontoAmostra(json.area.id, json.amostra.id, false);
            }
        });

        legenda = document.getElementById("leg").value;
        cores = document.getElementById("cores").value;
        corx = document.getElementById("corx").value;
        dex = document.getElementById("dex").value;
        atex = document.getElementById("atex").value;
        tamanhox = document.getElementById("tamanhox").value;
        descricaox = document.getElementById("descricaox").value;
        datax = document.getElementById("datax").value;
        parametrox = document.getElementById("parametrox").value;
        atributox = document.getElementById("atributox").value;
        siglax = document.getElementById("siglax").value;
        unidadex = document.getElementById("unidadex").value;
        valorx = document.getElementById("valorx").value;
        document.getElementById("movido2").style.display = 'block';

        //alert(legenda);
        // alert(this.attributes.getNamedItem('data-mensagem').value);
    });
    $(".listaProjetoSelecionado").on('click', function () {

        $("#legendaInterpolacao").html('');
        map.removeLayer(vectorLayerAreas);
        map.removeLayer(vectorLayer);
        map.removeLayer(vectorLayerInterpolacao);
        setAreaProjetoPontoAmostra(this.attributes.getNamedItem('data-areaId').value, this.attributes.getNamedItem('data-amostraId').value, true, this.attributes.getNamedItem('data-atributoId').value);
    });


    $(".botaomapa").on('click', function () {

        map.removeLayer(vectorLayerAreas);
        map.removeLayer(vectorLayer);
        map.removeLayer(vectorLayerInterpolacao);
        map.getLayers().clear();
        map.addLayer(layerStm);
        
        document.getElementById("movido2").style.display = 'none';
        legenda = document.getElementById("leg").value;
        cores = document.getElementById("cores").value;
        corx = document.getElementById("corx").value;
        dex = document.getElementById("dex").value;
        atex = document.getElementById("atex").value;
        tamanhox = document.getElementById("tamanhox").value;
        descricaox = document.getElementById("descricaox").value;
        datax = document.getElementById("datax").value;
        parametrox = document.getElementById("parametrox").value;
        atributox = document.getElementById("atributox").value;
        siglax = document.getElementById("siglax").value;
        unidadex = document.getElementById("unidadex").value;
        valorx = document.getElementById("valorx").value;
        var selectedData = document.getElementById("camposmarcados").value;

        //setAreaProjetoPontoAmostra2(21, null);
        // setAreaProjetoPontoAmostra2(22, 55);
        // setAreaProjetoPontoAmostra2(22, 49);
        //console.log("campos marcados js "+document.getElementById("camposmarcados").value);
        //console.log("selectData.length js "+selectedData.length);
        var resultado = new Array();
        var arrayareas = new Array();
        var arrayamostras = new Array();
        var arraygrades = new Array();
        var arraymapas = new Array();
        resultado = selectedData.split(",");

        for (i = 0; i < resultado.length; i++) {
            var verificaproj = resultado[i];
            if (verificaproj.includes("projeto")) {
            } else
            if (verificaproj.includes("area")) {
                if (verificaproj.includes("gradesamostraisarea")) {
                } else {
                    if (verificaproj.includes("amostrasarea")) {
                    } else {
                        if (verificaproj.includes("mapasarea")) {

                        } else {
                            arrayareas.push(i);
                        }
                    }
                }
            } else {
                if (verificaproj.includes("amostra")) {
                    if (resultado[i] === "amostrasarea") {

                    } else {
                        arrayamostras.push(i);
                    }
                    //var idarea = resultado[i].substr(4); 
                } else {
                    if (verificaproj.includes("grade")) {
                        if (resultado[i] === "gradesamostraisarea") {
                            //console.log("entrou no if certo!");
                        } else {
                            //console.log("entrou no else " + resultado[i] + " i: " + i);
                            arraygrades.push(i);
                        }

                    } else {
                        if (verificaproj.includes("mapa")) {
                            if (resultado[i] === "mapasarea") {
                                //console.log("entrou no if certo!");
                            } else {
                                //console.log("entrou no else " + resultado[i] + " i: " + i);
                                arraymapas.push(i);
                            }
                        }
                    }
                }
            }
        }
        if (arrayareas.length > 0) {
            for (i = 0; i < arrayareas.length; i++) {
                var posicao = arrayareas[i];
                var descricaoarea = resultado[posicao];
                var idarea = descricaoarea.substr(4);
                console.log("arrayarea: "+idarea);
                setAreaProjetoPontoAmostra2(idarea, null, null);
            }
        }
        if (arrayamostras.length > 0) {
            for (j = 0; j < arrayamostras.length; j++) {
                var idamostra = resultado[arrayamostras[j]].substr(7);
                  console.log("arrayamostra: "+idamostra);
                setAreaProjetoPontoAmostra2(null, idamostra, null);
            }
        }

        if (arraygrades.length > 0) {
            for (k = 0; k < arraygrades.length; k++) {
                var idgrade = resultado[arraygrades[k]].substr(5);
                  console.log("arraygrade: "+idgrade);
                setAreaProjetoPontoAmostra2(null, null, idgrade);
            }
        }
        if (arraymapas.length > 0) {
            for (k = 0; k < arraymapas.length; k++) {
                var idmapa = resultado[arraymapas[k]].substr(4);
                  console.log("arraymapa: "+idmapa);
                setMapa(idmapa);
            }
        }

    });


    $(".listaClassificacaoSelecionado").on('click', function () {

        $.ajax({
            url: 'trocaclassificacao',
            type: 'GET',
            dataType: 'json',
            data: {
                'id': this.attributes.getNamedItem('data-classificacaoId').value,
                'idmapa': idmapa
            },
            success: function (json) {

                map.removeLayer(vectorLayerAreas);
                map.removeLayer(vectorLayer);
                map.removeLayer(vectorLayerInterpolacao);
                var format = new ol.format.GeoJSON();
                vectorLayerInterpolacao = new ol.layer.Vector({
                    source: new ol.source.StaticVector({
                        format: format,
                        projection: 'EPSG:3857'
                    }),
                    style: setNewStyle
                });
                var sourceInterpolacao = vectorLayerInterpolacao.getSource();
                var minimo, maximo, intervalo;
                for (var i = 0; i < json.pixelMapasTrans.length; i++) {

                    var ponto = JSON.parse(json.pixelMapasTrans[i].geom);
                    var features = sourceInterpolacao.readFeatures(json.pixelMapasTrans[i].geom);
                    sourceInterpolacao.addFeatures(features);
                    minimo = ponto.properties.minimo;
                    maximo = ponto.properties.maximo;
                    intervalo = ponto.properties.intervalo;
                }

                map.addLayer(vectorLayerInterpolacao);
                $("#myModalListaClassificacao").modal("hide");
                htmlLegenda(minimo, maximo, intervalo, json.classesTran);
                map.getView().fitExtent(sourceInterpolacao.getExtent(), map.getSize());
                setAreaProjetoPontoAmostra(json.area.id, json.amostra.id, false);
            }
        });
    });


});

function setMapa(codmapa) {
    $.ajax({
        url: 'interpolacao',
        type: 'GET',
        dataType: 'json',
        data: {
            'id': codmapa
        },
        success: function (json) {

            //map.removeLayer(vectorLayerAreas);
            //map.removeLayer(vectorLayer);
            //map.removeLayer(vectorLayerInterpolacao);
            var format = new ol.format.GeoJSON();
            vectorLayerInterpolacao = new ol.layer.Vector({
                source: new ol.source.StaticVector({
                    format: format,
                    projection: 'EPSG:3857'
                }),
                style: setNewStyle
            });
            var sourceInterpolacao = vectorLayerInterpolacao.getSource();
            var minimo, maximo, intervalo;
            idmapa = json.id;
            for (var i = 0; i < json.pixelMapasTrans.length; i++) {

                var ponto = JSON.parse(json.pixelMapasTrans[i].geom);
                var features = sourceInterpolacao.readFeatures(json.pixelMapasTrans[i].geom);
                sourceInterpolacao.addFeatures(features);
                minimo = ponto.properties.minimo;
                maximo = ponto.properties.maximo;
                intervalo = ponto.properties.intervalo;
            }

            map.addLayer(vectorLayerInterpolacao);
            //$("#myModalListaInterpolacao").modal("hide");
            htmlLegenda(minimo, maximo, intervalo, json.classesTran);
            map.getView().fitExtent(sourceInterpolacao.getExtent(), map.getSize());
            setAreaProjetoPontoAmostra(json.area.id, json.amostra.id, false);
        }
    });

    legenda = document.getElementById("leg").value;
    cores = document.getElementById("cores").value;
    corx = document.getElementById("corx").value;
    dex = document.getElementById("dex").value;
    atex = document.getElementById("atex").value;
    tamanhox = document.getElementById("tamanhox").value;
    descricaox = document.getElementById("descricaox").value;
    datax = document.getElementById("datax").value;
    parametrox = document.getElementById("parametrox").value;
    atributox = document.getElementById("atributox").value;
    siglax = document.getElementById("siglax").value;
    unidadex = document.getElementById("unidadex").value;
    valorx = document.getElementById("valorx").value;
    document.getElementById("movido2").style.display = 'block';

}

function setAreaProjetoPontoAmostra(idArea, idAmostra, projeto) {

    $("#myModalListaProjeto").modal('hide');
    vectorColeta = new ol.source.Vector({});
    vectorLayer = new ol.layer.Vector({
        source: vectorColeta
    });
    $.ajax({
        url: 'mapa/projeto',
        dataType: 'json',
        type: 'GET',
        data: {'idArea': idArea, 'idAmostra': idAmostra},
        success: function (data) {

            var format = new ol.format.GeoJSON();
            var styleLayerAreas = gerarNovoStyle("FFFFFF", "0", "5496cf", "", "1.5", "");
            if (projeto === true) {
                styleLayerAreas = gerarNovoStyle("FFFFFF", "0.3", "5496cf", "", "1.5", "");
            }

            vectorLayerAreas = new ol.layer.Vector({
                source: new ol.source.StaticVector({
                    format: format,
                    projection: 'EPSG:3857'
                }),
                style: styleLayerAreas
            });
            var sourceAreas = vectorLayerAreas.getSource();
            var featuresAreas = sourceAreas.readFeatures(data.geom);
            sourceAreas.addFeatures(featuresAreas);
            map.addLayer(vectorLayerAreas);
            for (var k = 0; k < data.amostra.pixelAmostrasTrans.length; k++) {

                var ponto = JSON.parse(data.amostra.pixelAmostrasTrans[k].geom);
                var iconFeature = new ol.Feature({geometry: new ol.geom.Point(ol.proj.transform(ponto.geometry.coordinates, 'EPSG:4326', 'EPSG:3857')), population: 4000, rainfall: 500,
                    //nome      : data.nome,
                    tamanho: '',
                    descricao: data.amostra.descricao,
                    data: data.amostra.dataFormatada,
                    //grade     : data.amostra.pixelX + ' X / ' + data.amostra.pixelX + ' Y',
                    atributo: data.amostra.atributo.descricaoPT,
                    sigla: data.amostra.atributo.siglaPT,
                    unidade: data.amostra.atributo.unidadeMedidaPT.sigla,
                    valor: data.amostra.pixelAmostrasTrans[k].valor});
                vectorColeta.addFeature(iconFeature);
            }

            map.addLayer(vectorLayer);
            map.getView().fitExtent(sourceAreas.getExtent(), map.getSize());
            addPopup();
        }
    });
}




function setAreaProjetoPontoAmostra2(idArea, idAmostra, idGrade) {

    vectorColeta = new ol.source.Vector({});
    vectorLayer = new ol.layer.Vector({
        source: vectorColeta
    });
    $.ajax({
        url: 'mapa/checkbox',
        dataType: 'json',
        type: 'GET',
        data: {'idArea': idArea, 'idAmostra': idAmostra, 'idGrade': idGrade},
        success: function (data) {

            var format = new ol.format.GeoJSON();
            //var styleLayerAreas = gerarNovoStyle("FFFFFF", "0", "5496cf", "", "1.5", "");
            styleLayerAreas = gerarNovoStyle("FFFFFF", "0.3", "5496cf", "", "1.5", "");


            vectorLayerAreas = new ol.layer.Vector({
                source: new ol.source.StaticVector({
                    format: format,
                    projection: 'EPSG:3857'
                }),
                style: styleLayerAreas
            });
            var sourceAreas = vectorLayerAreas.getSource();
            var featuresAreas = sourceAreas.readFeatures(data.geom);
            sourceAreas.addFeatures(featuresAreas);
            map.addLayer(vectorLayerAreas);
            if (idAmostra !== null) {
                for (var k = 0; k < data.amostra.pixelAmostrasTrans.length; k++) {

                    var ponto = JSON.parse(data.amostra.pixelAmostrasTrans[k].geom);
                    var iconFeature = new ol.Feature({geometry: new ol.geom.Point(ol.proj.transform(ponto.geometry.coordinates, 'EPSG:4326', 'EPSG:3857')), population: 4000, rainfall: 500,
                        //nome      : data.nome,
                        tamanho: '',
                        descricao: data.amostra.descricao,
                        data: data.amostra.dataFormatada,
                        //grade     : data.amostra.pixelX + ' X / ' + data.amostra.pixelX + ' Y',
                        atributo: data.amostra.atributo.descricaoPT,
                        sigla: data.amostra.atributo.siglaPT,
                        unidade: data.amostra.atributo.unidadeMedidaPT.sigla,
                        valor: data.amostra.pixelAmostrasTrans[k].valor});
                    vectorColeta.addFeature(iconFeature);
                }

                map.addLayer(vectorLayer);
                addPopup();

            }
            if (idGrade !== null) {
                for (var k = 0; k < data.gradeAmostral.pontoAmostralTrans.length; k++) {

                    var ponto = JSON.parse(data.gradeAmostral.pontoAmostralTrans[k].geom);
                    var iconFeature = new ol.Feature({geometry: new ol.geom.Point(ol.proj.transform(ponto.geometry.coordinates, 'EPSG:4326', 'EPSG:3857')), population: 4000, rainfall: 500,
                        //nome      : data.nome,
                        tamanho: '',
                        descricao: data.gradeAmostral.descricao,
                        data: data.gradeAmostral.dataFormatada,
                        grade: data.gradeAmostral.tamx + ' X / ' + data.gradeAmostral.tamx + ' Y'});
                    //atributo: data.amostra.atributo.descricaoPT,
                    //sigla: data.amostra.atributo.siglaPT,
                    //unidade: data.amostra.atributo.unidadeMedidaPT.sigla,
                    //valor: data.amostra.pixelAmostrasTrans[k].valor});
                    vectorColeta.addFeature(iconFeature);
                }

                map.addLayer(vectorLayer);
                addPopup();
            }
            map.getView().fitExtent(sourceAreas.getExtent(), map.getSize());

        }
    });
}

function setNewStyle(feature) {

    var style = new ol.style.Style({
        fill: new ol.style.Fill({
            color: feature.getProperties().cor,
        }),
        stroke: new ol.style.Stroke({
            color: feature.getProperties().cor,
        })
    });
    feature.setStyle(style);
}

function addPopup() {

    closer.onclick = function () {
        overlay.setPosition(undefined);
        closer.blur();
        return false;
    };
    map.on('click', function (evt) {

        var feature = map.forEachFeatureAtPixel(evt.pixel,
                function (feature, layer) {
                    return feature;
                }
        );
        if (feature && feature.get('descricao') !== undefined) {

            var geometry = feature.getGeometry();
            var coordinate = geometry.getCoordinates();
            if (feature.get('valor') === undefined) {
                popupHTML2(feature, coordinate);
            } else {
                popupHTML(feature, coordinate);
            }
        }
    });
}

function popupHTML2(feature, coordinate) {
    var html = '';
    html += '<div class="table-responsive" style="min-width: 230px;">';
    html += '	<table class="table table-hover" style="margin-bottom: 0px;">';
    html += '		<thead style="background: #fff;">';
    html += '			<tr>';
    html += '				<th class="text-left"> ' + parametrox + ' </th>';
    html += '				<th class="text-center"> ' + valorx + '     </th>';
    html += '			</tr>';
    html += '		</thead>';
    html += '	    <tbody style="font-size: 10px;">';
    html += '	    	<tr>';
    html += '				<td class="text-left"  > ' + descricaox + ' </td>';
    html += '				<td class="text-center"> ' + feature.get('descricao') + ' </td>';
    html += '			</tr>';
    html += '	    	<tr>';
    html += '				<td class="text-left"  > ' + datax + ' </td>';
    html += '				<td class="text-center"> ' + feature.get('data') + ' </td>';
    html += '			</tr>';
    html += '	    	<tr>';
    html += '				<td class="text-left"  > ' + tamanhox + ' </td>';
    html += '				<td class="text-center"> ' + feature.get('grade') + ' </td>';
    html += '			</tr>';
    html += '	    </tbody>';
    html += '	</table>';
    html += '</div>';
    content.innerHTML = html;
    overlay.setPosition(coordinate);
}

function popupHTML(feature, coordinate) {
    var html = '';
    html += '<div class="table-responsive" style="min-width: 230px;">';
    html += '	<table class="table table-hover" style="margin-bottom: 0px;">';
    html += '		<thead style="background: #fff;">';
    html += '			<tr>';
    html += '				<th class="text-left"> ' + parametrox + ' </th>';
    html += '				<th class="text-center"> ' + valorx + '     </th>';
    html += '			</tr>';
    html += '		</thead>';
    html += '	    <tbody style="font-size: 10px;">';
    //html += '	    	<tr>';
    //html += '				<td class="text-left"  > Nome </td>';
    //html += '				<td class="text-center"> ' + feature.get('nome') + ' </td>';
    //html += '			</tr>';
    html += '	    	<tr>';
    html += '				<td class="text-left"  > ' + descricaox + ' </td>';
    html += '				<td class="text-center"> ' + feature.get('descricao') + ' </td>';
    html += '			</tr>';
    html += '	    	<tr>';
    html += '				<td class="text-left"  > ' + datax + ' </td>';
    html += '				<td class="text-center"> ' + feature.get('data') + ' </td>';
    html += '			</tr>';
    //html += '	    	<tr>';
    //html += '				<td class="text-left"  > Grade </td>';
    //html += '				<td class="text-center"> ' + feature.get('grade') + ' </td>';
    //html += '			</tr>';
    html += '	    	<tr>';
    html += '				<td class="text-left"  > ' + atributox + ' </td>';
    html += '				<td class="text-center"> ' + feature.get('atributo') + ' </td>';
    html += '			</tr>';
    html += '	    	<tr>';
    html += '				<td class="text-left"  > ' + siglax + ' </td>';
    html += '				<td class="text-center"> ' + feature.get('sigla') + ' </td>';
    html += '			</tr>';
    html += '	    	<tr>';
    html += '				<td class="text-left"  > ' + valorx + ' </td>';
    html += '				<td class="text-center"> ' + feature.get('valor') + ' ' + feature.get('unidade') + ' </td>';
    html += '			</tr>';
    html += '	    </tbody>';
    html += '	</table>';
    html += '</div>';
    //console.log("***");
    //console.log(feature);
    //console.log(coordinate);
    //console.log("***");
    content.innerHTML = html;
    overlay.setPosition(coordinate);

}

function htmlLegenda(minimo, maximo, intervalo, arrayIntervalos) {

    var html = '';
    var num = 1;
    html += '<div style="background: #ddd; color: #ffffff; margin-top: 5px; padding: 5px; border-radius: 4px;">';
    if (arrayIntervalos.length > 0) {
        html += '<table class="table-responsive" style="min-width: 250px;">';
        html += '<thead>';
        html += '<tr> <th class="text-center" style="width: 30px"> ' + dex + ' </th>';
        html += '<th class="text-center" style="width: 80px"> ' + atex + ' </th>';
        html += '<th class="text-center" style="width: 80px"> ' + corx + ' </th>';
        html += '<th class="text-center" style="width: 80px"> ';
        html += ' <div class="btn btn-xs btn-success" style="width: 80px" id="classificadorNovaLinha"  onclick="novalinha(' + maximo + ',' + minimo + ',' + intervalo + ')">';
        html += '     <span class="glyphicon glyphicon-plus"></span>';
        html += '  </div>';
        html += '</th>';
        html += '</tr> ';
        html += '</thead>';
        html += '<tbody id="bodyListaCorClassificador"> ';
        contLinha = arrayIntervalos.length;
        for (var i = 0; i < arrayIntervalos.length; i++) {
            console.log(arrayIntervalos.toString());
            html += ' <tr id="linha-' + (i + 1) + '">';
            //html += '	<div class="row" style="margin-left: 0px; margin-right: 0px;">';
            html += '    <td class="text-left">   <input  pattern="[0-9]*[.]?[0-9]*" type="text" id="minlinha-' + (i + 1) + '" class="form-control" style="width: 80px" value="' + (arrayIntervalos[i].valorMinimo).toFixed(2) + '"> </td>';
            // html += '		<div class="col-md-3"> <div class="text-center"> ' + arrayIntervalos[i].valorMinimo + ' </div></div>';
            //html += '		<div class="col-md-1"> <div class="text-center"> - </div></div>';
            // html += '		<div class="col-md-3"> <div class="text-center"> ' + arrayIntervalos[i].valorMaximo + ' </div></div>';
            html += '    <td class="text-center"> <input  pattern="[0-9]*[.]?[0-9]*" type="text" id="maxlinha-' + (i + 1) + '" class="form-control" style="width: 80px" value="' + (arrayIntervalos[i].valorMaximo).toFixed(2) + '" > </td>';
            // html += '		<div class="col-md-4"> <div style="width: 100%; height: 20px; border-radius: 5px; background: ' + arrayIntervalos[i].cor + ';"></div> </div>';
            html += '     <td class="text-center"> <input type="color" id="corlinha-' + (i + 1) + '" class="form-control" style="width: 80px" value="' + arrayIntervalos[i].cor + '"> </td>';
            html += '     <td class="text-center"> ';
            html += '        <div class="btn btn-xs btn-danger style="width: 80px" classificadorRemoverLinha" style="width: 80px" onclick="controleClickMenos(' + (i + 1) + ')" data-id="' + (i + 1) + '" style="margin-top: 6px;">';
            html += '             <span class="glyphicon glyphicon-minus"></span>';
            html += '	</div>';
            html += '    </td>';
            html += ' </tr>';

        }
        html += '</tbody>';
        html += '<tr> <td   colspan="2" class="text-center" width="30px"><div class="btn btn-xs btn-primary classificador" style="width: 160px" onclick="controleClassificador()">';
        html += legenda;
        html += '          </div> </td>';
        html += '<td   colspan="2" class="text-center" width="30px"><div class="btn btn-xs btn-primary classificadorgenerico" style="width: 160px" onclick="controleClassificadorGenerico()">';
        html += cores;
        html += '          </div> </td>';
        html += '</tr>';
        html += '</table>';
    } else {

        html += '<table class="table-responsive" style="min-width: 250px;">';
        html += '<thead>';
        html += '<tr> <th class="text-center" style="width: 30px"> ' + dex + '</th>';
        html += '<th class="text-center" style="width: 80px"> ' + atex + ' </th>';
        html += '<th class="text-center" style="width: 80px"> ' + corx + ' </th>';
        html += '<th class="text-center" style="width: 80px"> ';
        html += ' <div class="btn btn-xs btn-success" style="width: 80px" id="classificadorNovaLinha"  onclick="novalinha(' + maximo + ',' + minimo + ',' + intervalo + ')">';
        html += '     <span class="glyphicon glyphicon-plus"></span>';
        html += '  </div>';
        html += '</th>';
        html += '</tr> ';
        html += '</thead>';
        html += '<tbody id="bodyListaCorClassificador"> ';
        html += ' <tr id="linha-1">';
        html += '    <td class="text-left">   <input  pattern="[0-9]*[.]?[0-9]*" type="text" id="minlinha-1" class="form-control" style="width: 80px" value="' + (minimo + (0)).toFixed(2) + '"> </td>';
        html += '    <td class="text-center"> <input  pattern="[0-9]*[.]?[0-9]*" type="text" id="maxlinha-1" class="form-control" style="width: 80px" value="' + (minimo + (intervalo * 1)).toFixed(2) + '" > </td>';
        html += '     <td class="text-center"> <input type="color" id="corlinha-1" class="form-control" style="width: 80px" value="#FFEEDE"> </td>';
        html += '     <td class="text-center"> ';
        html += '        <div class="btn btn-xs btn-danger classificadorRemoverLinha" style="width: 80px" onclick="controleClickMenos(' + num + ')" data-id="1" style="margin-top: 6px;">';
        html += '             <span class="glyphicon glyphicon-minus"></span>';
        html += '          </div>';
        html += '    </td>';
        html += ' </tr>';
        html += ' <tr id="linha-2">';
        html += '    <td class="text-left">   <input  pattern="[0-9]*[.]?[0-9]*" type="text" id="minlinha-2" class="form-control" style="width: 80px" value="' + (minimo + (intervalo * 1)).toFixed(2) + '"> </td>';
        html += '    <td class="text-center"> <input  pattern="[0-9]*[.]?[0-9]*" type="text" id="maxlinha-2" class="form-control" style="width: 80px" value="' + (minimo + (intervalo * 2)).toFixed(2) + '" > </td>';
        html += '     <td class="text-center"> <input type="color" id="corlinha-2" class="form-control" style="width: 80px" value="#FFD0AA"> </td>';
        html += '     <td class="text-center"> ';
        html += '        <div class="btn btn-xs btn-danger classificadorRemoverLinha" style="width: 80px" onclick="controleClickMenos(' + (num + 1) + ')" data-id="2" style="margin-top: 6px;">';
        html += '             <span class="glyphicon glyphicon-minus"></span>';
        html += '          </div>';
        html += '    </td>';
        html += ' </tr>';
        html += ' <tr id="linha-3">';
        html += '    <td class="text-left">   <input  pattern="[0-9]*[.]?[0-9]*" type="text" id="minlinha-3" class="form-control" style="width: 80px" value="' + (minimo + (intervalo * 2)).toFixed(2) + '"> </td>';
        html += '    <td class="text-center"> <input  pattern="[0-9]*[.]?[0-9]*" type="text" id="maxlinha-3" class="form-control" style="width: 80px" value="' + (minimo + (intervalo * 3)).toFixed(2) + '" > </td>';
        html += '     <td class="text-center"> <input type="color" id="corlinha-3" class="form-control" style="width: 80px" value="#FF8E32"> </td>';
        html += '     <td class="text-center"> ';
        html += '        <div class="btn btn-xs btn-danger classificadorRemoverLinha" style="width: 80px" onclick="controleClickMenos(' + (num + 2) + ')" data-id="3" style="margin-top: 6px;">';
        html += '             <span class="glyphicon glyphicon-minus"></span>';
        html += '          </div>';
        html += '    </td>';
        html += ' </tr>';
        html += ' <tr id="linha-4">';
        html += '    <td class="text-left">   <input  pattern="[0-9]*[.]?[0-9]*" type="text" id="minlinha-4" class="form-control" style="width: 80px" value="' + (minimo + (intervalo * 3)).toFixed(2) + '"> </td>';
        html += '    <td class="text-center"> <input  pattern="[0-9]*[.]?[0-9]*" type="text" id="maxlinha-4" class="form-control" style="width: 80px" value="' + (minimo + (intervalo * 4)).toFixed(2) + '" > </td>';
        html += '     <td class="text-center"> <input type="color" id="corlinha-4" class="form-control" style="width: 80px" value="#FB520F"> </td>';
        html += '     <td class="text-center"> ';
        html += '        <div class="btn btn-xs btn-danger classificadorRemoverLinha" style="width: 80px" onclick="controleClickMenos(' + (num + 3) + ')" data-id="4" style="margin-top: 6px;">';
        html += '             <span class="glyphicon glyphicon-minus"></span>';
        html += '          </div>';
        html += '    </td>';
        html += ' </tr>';
        html += ' <tr id="linha-5">';
        html += '    <td class="text-left">   <input  pattern="[0-9]*[.]?[0-9]*" type="text" id="minlinha-5" class="form-control" style="width: 80px" value="' + (minimo + (intervalo * 4)).toFixed(2) + '"> </td>';
        html += '    <td class="text-center"> <input  pattern="[0-9]*[.]?[0-9]*" type="text" id="maxlinha-5" class="form-control" style="width: 80px" value="' + (minimo + (intervalo * 5)).toFixed(2) + '" > </td>';
        html += '     <td class="text-center"> <input type="color" id="corlinha-5" class="form-control" style="width: 80px" value="#AD3A01"> </td>';
        html += '     <td class="text-center"> ';
        html += '        <div class="btn btn-xs btn-danger classificadorRemoverLinha" style="width: 80px" onclick="controleClickMenos(' + (num + 4) + ')" data-id="5" style="margin-top: 6px;">';
        html += '             <span class="glyphicon glyphicon-minus"></span>';
        html += '          </div>';
        html += '    </td>';
        html += ' </tr>';
//        html += ' <tr id="linha-6">';
//        html += '    <td class="text-left">   <input  pattern="[0-9]*[.]?[0-9]*" type="text" id="minlinha-6" class="form-control" style="width: 80px" value="' + (minimo + (intervalo * 5)).toFixed(2) + '"> </td>';
//        html += '    <td class="text-center"> <input  pattern="[0-9]*[.]?[0-9]*" type="text" id="maxlinha-6"  class="form-control" style="width: 80px" value="' + (minimo + (intervalo * 6)).toFixed(2) + '" > </td>';
//        html += '     <td class="text-center"> <input type="color"  id="corlinha-6" class="form-control" style="width: 80px" value="#FB520F"> </td>';
//        html += '     <td class="text-center"> ';
//        html += '        <div class="btn btn-xs btn-danger classificadorRemoverLinha" style="width: 80px" onclick="controleClickMenos(' + (num + 5) + ')" data-id="6" style="margin-top: 6px;">';
//        html += '             <span class="glyphicon glyphicon-minus"></span>';
//        html += '          </div>';
//        html += '    </td>';
//        html += ' </tr>';
        html += '</tbody>';
        html += '<tr> <td  colspan="2" class="text-center" width="30px"><div class="btn btn-xs btn-primary classificador" style="width: 160px" onclick="controleClassificador()">';
        html += legenda;
        html += '          </div> </td>';
        html += '<td colspan="2" class="text-center" width="30px"><div class="btn btn-xs btn-primary classificadorgenerico" style="width: 160px"  onclick="controleClassificadorGenerico()">';
        html += cores;
        html += '          </div> </td>';
        html += '</tr>';
        html += '</table>';
//        html += '	<div class="row" style="margin-left: 0px; margin-right: 0px;">';
//        html += '		<div class="col-md-3"> <div class="text-center"> ' + (minimo + (intervalo * 1)).toFixed(2) + ' </div></div>';
//        html += '		<div class="col-md-1"> <div class="text-center"> -    </div></div>';
//        html += '		<div class="col-md-3"> <div class="text-center"> ' + (minimo + (intervalo * 2)).toFixed(2) + ' </div></div>';
//        html += '		<div class="col-md-4"> <div style="width: 100%; height: 20px; border-radius: 5px; background: #FFDAB9;"></div> </div>';
//        html += '	</div>';
//        html += '	<div class="row" style="margin-left: 0px; margin-right: 0px;">';
//        html += '		<div class="col-md-3"> <div class="text-center"> ' + (minimo + (intervalo * 2)).toFixed(2) + ' </div></div>';
//        html += '		<div class="col-md-1"> <div class="text-center"> -    </div></div>';
//        html += '		<div class="col-md-3"> <div class="text-center"> ' + (minimo + (intervalo * 3)).toFixed(2) + ' </div></div>';
//        html += '		<div class="col-md-4"> <div style="width: 100%; height: 20px; border-radius: 5px; background: #FFD0AA;"></div> </div>';
//        html += '	</div>';
//        html += '	<div class="row" style="margin-left: 0px; margin-right: 0px;">';
//        html += '		<div class="col-md-3"> <div class="text-center"> ' + (minimo + (intervalo * 3)).toFixed(2) + ' </div></div>';
//        html += '		<div class="col-md-1"> <div class="text-center"> -    </div></div>';
//        html += '		<div class="col-md-3"> <div class="text-center"> ' + (minimo + (intervalo * 4)).toFixed(2) + ' </div></div>';
//        html += '		<div class="col-md-4"> <div style="width: 100%; height: 20px; border-radius: 5px; background: #FFBB81;"></div> </div>';
//        html += '	</div>';
//        html += '	<div class="row" style="margin-left: 0px; margin-right: 0px;">';
//        html += '		<div class="col-md-3"> <div class="text-center"> ' + (minimo + (intervalo * 4)).toFixed(2) + ' </div></div>';
//        html += '		<div class="col-md-1"> <div class="text-center"> -    </div></div>';
//        html += '		<div class="col-md-3"> <div class="text-center"> ' + (minimo + (intervalo * 5)).toFixed(2) + ' </div></div>';
//        html += '		<div class="col-md-4"> <div style="width: 100%; height: 20px; border-radius: 5px; background: #FF8E32;"></div> </div>';
//        html += '	</div>';
//        html += '	<div class="row" style="margin-left: 0px; margin-right: 0px;">';
//        html += '		<div class="col-md-3"> <div class="text-center"> ' + (minimo + (intervalo * 5)).toFixed(2) + ' </div></div>';
//        html += '		<div class="col-md-1"> <div class="text-center"> -    </div></div>';
//        html += '		<div class="col-md-3"> <div class="text-center"> ' + (minimo + (intervalo * 6)).toFixed(2) + ' </div></div>';
//        html += '		<div class="col-md-4"> <div style="width: 100%; height: 20px; border-radius: 5px; background: #FF7B16;"></div> </div>';
//        html += '	</div>';
//        html += '	<div class="row" style="margin-left: 0px; margin-right: 0px;">';
//        html += '		<div class="col-md-3"> <div class="text-center"> ' + (minimo + (intervalo * 6)).toFixed(2) + ' </div></div>';
//        html += '		<div class="col-md-1"> <div class="text-center"> -    </div></div>';
//        html += '		<div class="col-md-3"> <div class="text-center"> ' + (minimo + (intervalo * 7)).toFixed(2) + ' </div></div>';
//        html += '		<div class="col-md-4"> <div style="width: 100%; height: 20px; border-radius: 5px; background: #FB520F;"></div> </div>';
//        html += '	</div>';
//        html += '	<div class="row" style="margin-left: 0px; margin-right: 0px;">';
//        html += '		<div class="col-md-3"> <div class="text-center"> ' + (minimo + (intervalo * 7)).toFixed(2) + ' </div></div>';
//        html += '		<div class="col-md-1"> <div class="text-center"> -    </div></div>';
//        html += '		<div class="col-md-3"> <div class="text-center"> ' + (minimo + (intervalo * 8)).toFixed(2) + ' </div></div>';
//        html += '		<div class="col-md-4"> <div style="width: 100%; height: 20px; border-radius: 5px; background: #EE5801;"></div> </div>';
//        html += '	</div>';
//        html += '	<div class="row" style="margin-left: 0px; margin-right: 0px;">';
//        html += '		<div class="col-md-3"> <div class="text-center"> ' + (minimo + (intervalo * 8)).toFixed(2) + ' </div></div>';
//        html += '		<div class="col-md-1"> <div class="text-center"> -    </div></div>';
//        html += '		<div class="col-md-3"> <div class="text-center"> ' + (minimo + (intervalo * 9)).toFixed(2) + ' </div></div>';
//        html += '		<div class="col-md-4"> <div style="width: 100%; height: 20px; border-radius: 5px; background: #AD3A01;"></div> </div>';
//        html += '	</div>';
//        html += '	<div class="row" style="margin-left: 0px; margin-right: 0px;">';
//        html += '		<div class="col-md-3"> <div class="text-center"> ' + (minimo + (intervalo * 9)).toFixed(2) + ' </div></div>';
//        html += '		<div class="col-md-1"> <div class="text-center"> -    </div></div>';
//        html += '		<div class="col-md-3"> <div class="text-center"> ' + (maximo + (0)).toFixed(2) + ' </div></div>';
//        html += '		<div class="col-md-4"> <div style="width: 100%; height: 20px; border-radius: 5px; background: #7C2801;"></div> </div>';
//        html += '	</div>';
    }
    //$("#buttonClassificador").html(html2);
    $("#legendaInterpolacao").html(html);
}

function controleClassificador() {
    //$("#myModalListaClassificacao #idmapa").val(idmapa);
    $("#myModalListaClassificacao").modal('show');

    //$('#classificador').html('<a class="btn btn-success" href="<c:url value="testaChamadoScript"/>"> </a> ');
}



function controleClassificadorGenerico() {
    var arraymin = new Array();
    var arraymax = new Array();
    var arraycor = new Array();
    console.log("contlinha dentro do classificadorGenerico " + contLinha);
    for (var i = 0; i < contLinha; i++) {
        var minimolinha = $("#minlinha-" + (i + 1)).val();
        console.log("entrou no classificador generico" + minimolinha);
        //arraymin[i] = $("#minlinha-" + (i + 1)).val();
        arraymin.push(minimolinha);
        arraymax.push($("#maxlinha-" + (i + 1)).val());
        arraycor.push($("#corlinha-" + (i + 1)).val());
        // arraymax[i] = $("#maxlinha-" + (i + 1)).val();
        //arraycor[i] = $("#corlinha-" + (i + 1)).val();
    }
    $.ajax({
        url: 'trocaclassificacaogenerica',
        type: 'GET',
        dataType: 'json',
        data: {
            'arraymin': arraymin,
            'arraymax': arraymax,
            'arraycor': arraycor,
            'idmapa': idmapa
        },
        success: function (json) {

            map.removeLayer(vectorLayerAreas);
            map.removeLayer(vectorLayer);
            map.removeLayer(vectorLayerInterpolacao);
            var format = new ol.format.GeoJSON();
            vectorLayerInterpolacao = new ol.layer.Vector({
                source: new ol.source.StaticVector({
                    format: format,
                    projection: 'EPSG:3857'
                }),
                style: setNewStyle
            });
            var sourceInterpolacao = vectorLayerInterpolacao.getSource();
            var minimo, maximo, intervalo;
            for (var i = 0; i < json.pixelMapasTrans.length; i++) {

                var ponto = JSON.parse(json.pixelMapasTrans[i].geom);
                var features = sourceInterpolacao.readFeatures(json.pixelMapasTrans[i].geom);
                sourceInterpolacao.addFeatures(features);
                minimo = ponto.properties.minimo;
                maximo = ponto.properties.maximo;
                intervalo = ponto.properties.intervalo;
            }

            map.addLayer(vectorLayerInterpolacao);
            $("#myModalListaClassificacao").modal("hide");
            $("#legendaInterpolacao").html('');
            htmlLegenda(minimo, maximo, intervalo, json.classesTran);
            map.getView().fitExtent(sourceInterpolacao.getExtent(), map.getSize());
            setAreaProjetoPontoAmostra(json.area.id, json.amostra.id, false);
        }
    });
}



function novalinha(maximo, minimo, intervalo) {
    var x = (parseFloat(intervalo)) * (parseFloat((contLinha + 1)));
    if (contLinha < 5) {
        var html = '';
        html += '<tr id="linha-' + (contLinha + 1) + '">';
        html += '	<td class="text-left">   <input  pattern="[0-9]*[.]?[0-9]*" type="text" id="minlinha-' + (contLinha + 1) + '" class="form-control" style="width: 80px" value=" "></td>';
        html += '	<td class="text-center"> <input  pattern="[0-9]*[.]?[0-9]*" type="text" id="maxlinha-' + (contLinha + 1) + '" class="form-control" style="width: 80px" value=" "> </td>';
        html += '	<td class="text-center"> <input type="color" id="corlinha-' + (contLinha + 1) + '" class="form-control" style="width: 80px"> </td>';
        html += '	<td class="text-center">';
        html += '		<div class="btn btn-xs btn-danger classificadorRemoverLinha" style="width: 80px" onclick="controleClickMenos(' + (contLinha + 1) + ')" data-id="' + (contLinha + 1) + '" style="margin-top: 6px;">';
        html += '		<span class="glyphicon glyphicon-minus"></span>';
        html += '	</div>';
        html += '	</td>';
        html += '</tr>';
        $("#bodyListaCorClassificador").append(html);
        //controleClickMenos();
        contLinha++;
    }
}
//controleClickMenos();
function controleClickMenos(num) {

// $(".classificadorRemoverLinha").unbind("click");
//$(".classificadorRemoverLinha").on('click', function () {
    $("#linha-" + num).remove();
    contLinha--;
    //});
}


function gerarNovoStyle(fillColor, fillOpacidade, strokeColor, strokeOpacidade, strokeWidth, imageRadius) {
    if (fillColor == "") {
        fillColor = '255, 255, 255';
    } else {
        fillColor = hexToRgb(fillColor);
    }
    if (fillOpacidade == "") {
        fillOpacidade = '0.4';
    }
    if (strokeColor == "") {
        strokeColor = '255, 204, 51';
    } else {
        strokeColor = hexToRgb(strokeColor);
    }
    if (strokeOpacidade == "") {
        strokeOpacidade = '1';
    }
    if (strokeWidth == "") {
        strokeWidth = 2;
    }
    if (imageRadius == "") {
        imageRadius = 7;
    }
    fillColor = 'rgba(' + fillColor + ', ' + fillOpacidade + ')';
    strokeColor = 'rgba(' + strokeColor + ', ' + strokeOpacidade + ')';
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