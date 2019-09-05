<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<!DOCTYPE html>
<html lang="pt-br" style="overflow: hidden;">

    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
      
        <title>SDUM</title>

        <jsp:include page="../include/link.jsp"    ></jsp:include>
        <jsp:include page="../include/link-mapa.jsp"></jsp:include>

            <style type="text/css">
                .popover {
                    max-width: none;
                }

                .ui-menu { width: 150px; }

            </style>

        </head>

        <body>

            
            <jsp:include page="../menu/menu.jsp"></jsp:include>	
           

            <section>
                <input type="hidden" value="${classificacoes}" name="classificacoes"  id="classificacoes" /> 
            <input type="hidden" value="${jsontree}" name="jsontree"  id="jsontree" /> 
            <input type="hidden" name="jsfields" id="jsfields" value="" />
            <input type="hidden" name="camposmarcados" id="camposmarcados" value=""/>
            <input type="hidden" value="${idProjeto}" name="idProjeto" id="idProjeto">
            <div style="position: absolute; z-index: 1; margin-top: 60px; margin-left: 10px;">

                <!--  <div class="btn" id="buttonModalProjeto" style="background: #979b9e; color: #ffffff;"> 
                      <span class="glyphicon glyphicon-file"></span> 
                <fmt:message key="view.mapa.projeto" />
            </div>

            <div class="btn" id="buttonModalInterpolacao" style="background: #979b9e; color: #ffffff;"> 
                <span class="glyphicon glyphicon-grain"></span> 
                <fmt:message key="view.mapa.interpolacao" />
            </div>!-->

                <div id='movido2' style='border: 1px solid black; background: #979b9e; color: #ffffff; display: none'>
                    <div class="text-center" id='movedor2' style='border: 1px solid black; overflow: auto;'><fmt:message key="view.mapa.legenda" /></div> 
                    <div id="legendaInterpolacao" > </div>
                </div>


            </div>

            <div style="position: absolute; z-index: 1; margin-top: 60px; margin-left: 70%;" >

                <div class="myScrollableBlock" id='movido' style='border: 1px solid black; background: #979b9e; color: #ffffff;'>
                    <div id='movedor' style='border: 1px solid black; overflow: auto;'><fmt:message key="view.mapa.projeto" /></div>
                    <div id="jstree_demo_div"> </div>
                    <div class="text-center">
                        <div style="width: 150px" class="btn btn-default glyphicon glyphicon-triangle-bottom botaomapa"></div>
                    </div>
                </div>

            </div>

            <div id="mapa">
                <div id="popup" class="ol-popup">
                    <a href="#" id="popup-closer" class="ol-popup-closer"></a>
                    <div id="popup-content"></div>
                </div>
            </div>

            <div id="mousePosition"></div>
            <div id="ferramenta"   ></div>

        </section>



            <jsp:include page="../include/script-mapa.jsp"></jsp:include>
            <jsp:include page="../modal/listaInterpolacao.jsp"></jsp:include>
            <jsp:include page="../modal/listaProjetos.jsp"></jsp:include>
            <jsp:include page="../modal/listaClassificacao.jsp"></jsp:include>
            


                <script type="text/javascript">
                      document.getElementById("menuinicial").style.display = 'block';
                    //$(document).ready(function () {
                     //   document.getElementById("menuinicial").style.display = 'block';
                        //var jsfields = $('#jstree_demo_div').jstree('get_selected');
                        //$('#jsfields').val(JSON.stringify([jsfields]));
                        //document.getElementById('userlogoff').addEventListener('click', function (e) {
                        //    document.getElementById("userlogoff").setAttribute("aria-expanded", "true");
                        //});
                   // });



                    dragdrop('movedor', 'movido');
                    function parseNumber(num) {
                        return parseFloat(num.replace(/[^\d]/)) || 0;
                    }

                    dragdrop('movedor2', 'movido2');
                    function parseNumber(num) {
                        return parseFloat(num.replace(/[^\d]/)) || 0;
                    }


                    var movePopUp = (function () {

                        var startX;
                        var startY;

                        var currentPopUp = null;
                        var currentWidth = 0;
                        var currentHeight = 0;
                        var currentLeft = 0;
                        var currentTop = 0;
                        var callMoveOnPopUp = null;
                        var callMoveStopPopUp = null;

                        var contentMove = '.popup .title';
                        var move = false;

                        var marginStop = 30;
                        var maxWidth = window.innerWidth - marginStop;
                        var maxHeight = window.innerHeight - marginStop;

                        jQuery(contentMove).on('mousedown', function (e) {
                            currentPopUp = this.parentNode.parentNode;
                            currentLeft = parseNumber(currentPopUp.style.left);
                            currentTop = parseNumber(currentPopUp.style.top);

                            startX = e.clientX;
                            startY = e.clientY;
                            if (typeof (callMoveOnPopUp) == 'function')
                                callMoveOnPopUp(currentPopUp);
                            move = true;
                        });

                        jQuery(document).on('mouseup', function () {
                            if (currentPopUp == null)
                                return;
                            if (typeof (callMoveStopPopUp) == 'function')
                                callMoveStopPopUp(currentPopUp);
                            currentPopUp = null;
                            move = false;
                        })

                        jQuery(document).on('mousemove', function (e) {
                            if (move == true) {
                                var newX = currentLeft + e.clientX - startX;
                                var newY = currentTop + e.clientY - startY;

                                if (marginStop > e.clientX)
                                    return;
                                if (marginStop > e.clientY)
                                    return;
                                if (maxWidth < e.clientX)
                                    return;
                                if (maxHeight < e.clientY)
                                    return;

                                jQuery(currentPopUp).css({
                                    'left': newX,
                                    'top': newY,
                                });
                            }
                        });

                        return function (func1, func2) {
                            callMoveOnPopUp = func1;
                            callMoveStopPopUp = func2;
                        }
                    })();

                <fmt:message key="view.escolher.classificador" var="prop1"/>
                    $("#buttonModalInterpolacao").on('click', function () {
                        $("#myModalListaInterpolacao").modal('show');
                        $('#leg').val("${prop1}");
                        //console.log($('#leg').val());
                    });

                    $("#buttonModalProjeto").on('click', function () {
                        $("#myModalListaProjeto").modal('show');
                    });

                    var objSelecionado = null;
                    var mouseOffset = null;
                    function addEvent(obj, evType, fn) {
                        if (typeof obj == "string") {
                            if (null == (obj = document.getElementById(obj))) {
                                throw new Error("Elemento HTML não encontrado. Não foi possível adicionar o evento.");
                            }
                        }
                        if (obj.attachEvent) {
                            return obj.attachEvent(("on" + evType), fn);
                        } else if (obj.addEventListener) {
                            return obj.addEventListener(evType, fn, true);
                        } else {
                            throw new Error("Seu browser não suporta adição de eventos. Senta, chora e pega um navegador mais recente.");
                        }
                    }
                    function mouseCoords(ev) {
                        if (typeof (ev.pageX) !== "undefined") {
                            return {x: ev.pageX, y: ev.pageY};
                        } else {
                            return {
                                x: ev.clientX + document.body.scrollLeft - document.body.clientLeft,
                                y: ev.clientY + document.body.scrollTop - document.body.clientTop
                            };
                        }
                    }
                    function getPosition(e, ev) {
                        var ev = ev || window.event;
                        if (e.constructor == String) {
                            e = document.getElementById(e);
                        }
                        var left = 0, top = 0;
                        var coords = mouseCoords(ev);

                        while (e.offsetParent) {
                            left += e.offsetLeft;
                            top += e.offsetTop;
                            e = e.offsetParent;
                        }
                        left += e.offsetLeft;
                        top += e.offsetTop;
                        return {x: coords.x - left, y: coords.y - top};
                    }

                    function dragdrop(local_click, caixa_movida) {
                        //local click indica quem é o cara que quando movido, move o caixa_movida
                        if (local_click.constructor == String) {
                            local_click = document.getElementById(local_click);
                        }
                        if (caixa_movida.constructor == String) {
                            caixa_movida = document.getElementById(caixa_movida);
                        }

                        local_click.style.cursor = 'move';
                        if (!caixa_movida.style.position || caixa_movida.style.position == 'static') {
                            caixa_movida.style.position = 'relative'
                        }
                        local_click.onmousedown = function (ev) {
                            objSelecionado = caixa_movida;
                            mouseOffset = getPosition(objSelecionado, ev);
                            if (mouseOffset < 0) {
                                objSelecionado.style.margin = '50px';
                            }
                        };
                        document.onmouseup = function () {
                            objSelecionado = null;
                        }
                        document.onmousemove = function (ev) {
                            if (objSelecionado) {
                                var ev = ev || window.event;
                                var mousePos = mouseCoords(ev);
                                var pai = objSelecionado.parentNode;



                                //as variáveis w e h definem a posição do(s) objeto(s) movido(s)

                                var w = (mousePos.x - mouseOffset.x - pai.offsetLeft);
                                var h = (mousePos.y - mouseOffset.y - pai.offsetTop);



                                //as variáveis areaHorizontal e areaVertical definem a área maxima, onde o objeto será movido (nesse caso é a area maxima do browser)

                                var areaHorizontal = document.body.clientWidth - objSelecionado.clientWidth - 2;
                                var areaVertical = document.body.clientHeight - objSelecionado.clientHeight - 2;

                                objSelecionado.style.left = w + 'px';
                                objSelecionado.style.top = h + 'px';



                                //essa é a estrutura de condição que reposiciona o objeto movido para não ultrapassar a área máxima
                                if (w >= areaHorizontal) {
                                    objSelecionado.style.left = areaHorizontal;
                                }
                                if (w <= 0) {
                                    objSelecionado.style.left = 0;
                                }
                                if (h >= areaVertical) {
                                    objSelecionado.style.top = areaVertical;
                                }
                                if (h <= 0) {
                                    objSelecionado.style.top = 0;
                                }



                                objSelecionado.style.margin = '0px';
                                return false;
                            }
                        }
                    }

                   // $(function () {
                    //    $("#menu").menu();
                    //});

                <fmt:message key="view.amostra" var="propamo"/>
                <fmt:message key="view.grade" var="propgrade"/>
                <fmt:message key="view.mapa" var="propmapa"/>
                    var amostraleg = "\"text\":\"" + "${propamo}" + "\"";
                    var gradeleg = "\"text\":\"" + "${propgrade}" + "\"";
                    var mapaleg = "\"text\":\"" + "${propmapa}" + "\"";
                    // console.log(${jsontree});
                    var recebejson = JSON.stringify(${jsontree});
                    //console.log(recebejson);
                    recebejson = recebejson.replace(/\"text\":\"Amostrassubstituir\"/g, amostraleg);
                    recebejson = recebejson.replace(/\"text\":\"GradesAmostraissubstituir\"/g, gradeleg);
                    recebejson = recebejson.replace(/\"text\":\"Mapassubstituir\"/g, mapaleg);
                    //recebejson = recebejson.substr(1,(recebejson.length - 1));
                    //console.log(recebejson);
                    //console.log(JSON.parse(recebejson));
                    $(function () {

                        $('#jstree_demo_div').jstree({
                            "plugins": ["wholerow", "checkbox"],
                            'core': {
                                'data': [
                                    JSON.parse(recebejson)
                                ]

                            }

                        });
                    });


                    $(".botaomapa").on('click', function () {
                        var selectedData = new Array();
                        for (i = 0; i < $('#jstree_demo_div').jstree().get_checked(true).length; i++) {
                            var auxiliar = $('#jstree_demo_div').jstree().get_checked(true)[i].id;
                            selectedData.push(auxiliar);
                            //console.log("val: "+$('#jstree_demo_div').jstree().get_checked(true)[i].id);
                            // console.log("auxiliar "+auxiliar);
                        }
                        //console.log("selectedData "+selectedData);
                        //console.log("ID PROJETO EM MAPA.JSP " + $(idProjeto).attr("value"));
                        $('#camposmarcados').val(selectedData);
                        //console.log("camposmarcados "+ $('#camposmarcados').val());
                    });


            </script>

    </body>
</html>