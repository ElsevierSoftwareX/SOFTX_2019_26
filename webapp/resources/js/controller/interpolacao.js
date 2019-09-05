var codigoArea;


function setAmostraClassificador(amostra, select, chave) {

    var codigoAmostra;

    try {
        var selectIndex = amostra.selectedIndex;
        codigoAmostra = amostra.options[selectIndex].value;
    } catch (e) {
        codigoAmostra = amostra;
    }

    if (codigoAmostra > 0) {

        $.ajax({
            contentType: "application/x-www-form-urlencoded;charset=UTF-8",
            url: 'lista/classificacao',
            type: 'GET',
            dataType: 'json',
            data: {'idArea': codigoArea, 'idAmostra': codigoAmostra},
            success: function (json) {

                if (json != null) {

                    var html = '<option value="0" >Selecionar</option>';

                    for (var i = 0; i < json.length; i++) {

                        html += '<option value="' + json[i].codigo + '"';

                        if (chave == json[i].codigo) {
                            html += 'selected="true"';
                        }

                        html += '>' + json[i].codigo + '</option>';

                    }

                    $('#' + select).html(html);
                }
            }
        });
    } else {
        $('#' + select).html('<option value="0">Selecionar</option>');
    }
}

function setAreaAmostra(area, select, chave) {

    try {
        var selectIndex = area.selectedIndex;
        codigoArea = area.options[selectIndex].value;
    } catch (e) {
        codigoArea = area;
    }

    if (codigoArea > 0) {

        $.ajax({
            contentType: "application/x-www-form-urlencoded;charset=UTF-8",
            url: 'lista/amostra',
            type: 'GET',
            dataType: 'json',
            data: {'id': codigoArea},
            success: function (json) {

                if (json != null) {

                    $("#formInterpolacaoClassificador").html("<option> Selecionar </option>");

                    var html = '<option>Selecionar</option>';

                    for (var i = 0; i < json.length; i++) {

                        html += '<option value="' + json[i].id + '"';

                        if (chave == json[i].id) {
                            html += 'selected="true"';
                        }

                        html += '>' + json[i].descricao + '</option>';

                    }

                    $('#' + select).html(html);
                }
            }
        });
    } else {
        $('#' + select).html('<option>Selecionar</option>');
    }
}


function setAmostra(area, select, chave) {

    try {
        var selectIndex = area.selectedIndex;
        codigoArea = area.options[selectIndex].value;
    } catch (e) {
        codigoArea = area;
    }
    var option = document.getElementById("option").value;
    if (codigoArea > 0) {

        $.ajax({
            contentType: "application/x-www-form-urlencoded;charset=UTF-8",
            url: 'lista/amostra',
            type: 'GET',
            dataType: 'json',
            data: {'id': codigoArea},
            success: function (json) {

                if (json != null) {

                    $("#formInterpolacaoAmostra").html("");

                    var html = '<option>'+option+'</option>'; 

                    for (var i = 0; i < json.length; i++) {

                        html += '<option value="' + json[i].id + '"';

                        if (chave == json[i].id) {
                            html += 'selected="true"';
                        }

                        html += '>' + json[i].descricao + '</option>';

                    }

                    $('#' + select).html(html);
                }


            }
        });
    }
    //else {
    //$('#' + select).html('<option>Selecionar</option>');
    //}
}



function setProjetoArea(projeto, select, chave) {

    var codigoProjeto;

    try {
        var selectIndex = projeto.selectedIndex;
        codigoProjeto = projeto.options[selectIndex].value;
    } catch (e) {
        codigoProjeto = projeto;
    }

    if (codigoProjeto > 0) {

        $.ajax({
            contentType: "application/x-www-form-urlencoded;charset=UTF-8",
            url: 'lista/area',
            type: 'GET',
            dataType: 'json',
            data: {'id': codigoProjeto},
            success: function (json) {

                if (json != null) {

                    $("#formInterpolacaoAmostra").html("<option> Selecionar </option>");
                    $("#formInterpolacaoClassificador").html("<option> Selecionar </option>");

                    var html = '<option>Selecionar</option>';

                    for (var i = 0; i < json.length; i++) {

                        html += '<option value="' + json[i].id + '"';

                        if (chave == json[i].id) {
                            html += 'selected="true"';
                        }

                        html += '>' + json[i].descricao + '</option>';

                    }

                    $('#' + select).html(html);
                }
            }
        });
    } else {
        $('#' + select).html('<option>Selecionar</option>');
    }
}





/*
 * 
 * var vectorLayerInterpolacao;
 * 
 * $(document).ready(function(){
 
 $("#formInterpolacaoIntepolar").on('click', function(){
 
 var nome         = $("#formInterpolacaoNome")        .val();
 var projeto      = $("#formInterpolacaoProjeto")     .val();
 var area         = $("#formInterpolacaoArea")        .val();
 var amostra      = $("#formInterpolacaoAmostra")     .val();
 var interpolador = $("#formInterpolacaoInterpolador").val();
 var pixelX       = $("#formInterpolacaoPixelX")      .val();
 var pixelY       = $("#formInterpolacaoPixelY")      .val();
 var expoente     = $("#formInterpolacaoExpoente")    .val();
 var vizinho      = $("#formInterpolacaoVizinho")     .val();
 var raio         = $("#formInterpolacaoRaio")        .val();
 
 
 if(nome === "" || nome === null || projeto === 0 || area === 0 || amostra === 0 || interpolador === 0 || pixelX === "" || pixelX === null || pixelY === "" || pixelY === null || expoente === "" || expoente === null || vizinho === "" || vizinho === null || raio === "" || raio === null){
 
 $("#alertaDanferInterpolador").html('<div class="alert alert-danger" role="alert"> Campos obrigatorios (*) </div>');
 
 }
 else {
 
 $("#alertaDanferInterpolador").html('<div class="alert alert-info" role="alert"> Aguarde, o sistema está executando a interpolação. </div>');
 
 $.ajax({
 url        : 'interpolacao',
 type       : 'GET',
 dataType   : 'json',
 data       : { 
 'nome'         : nome,
 'projeto'      : projeto,
 'area'         : area,
 'amostra'      : amostra,
 'interpolador' : interpolador,
 'pixelX'       : pixelX,
 'pixelY'       : pixelY,
 'expoente'     : expoente,
 'vizinho'      : vizinho,
 'raio'         : raio
 },
 success: function (json) {
 
 var format = new ol.format.GeoJSON();
 
 vectorLayerInterpolacao = new ol.layer.Vector({
 source: new ol.source.StaticVector({
 format: format,
 projection: 'EPSG:3857'
 })
 });
 
 var sourceInterpolacao = vectorLayerInterpolacao.getSource();
 
 for (var i = 0; i < json.pixelMapasTrans.length; i++) {
 
 var features = sourceInterpolacao.readFeatures( json.pixelMapasTrans[i].geom );
 sourceInterpolacao.addFeatures(features);
 
 map.addLayer(vectorLayerInterpolacao);			    		
 }
 
 $("#modalInterpolacao").modal("hide");
 
 map.getView().fitExtent(sourceInterpolacao.getExtent(), map.getSize());
 },
 error: function(){
 
 console.log("ERROR");
 
 }
 });			
 }
 });
 });*/
