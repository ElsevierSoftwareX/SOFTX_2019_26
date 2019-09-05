<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="modal" id="myModalListaInterpolacao" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">

            <div class="modal-header" style="border-bottom: 0px solid #e5e5e5;">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
                </button>				
                <input type="hidden" name="leg" id="leg" value="${leg}">
                <input type="hidden" name="cores" id="cores" value="${cores}">
                <input type="hidden" name="dex" id="dex" value="${dex}">
                <input type="hidden" name="atex" id="atex" value="${atex}">
                <input type="hidden" name="corx" id="corx" value="${corx}">
                <input type="hidden" name="tamanhox" id="tamanhox" value="${tamanhox}">
                <input type="hidden" name="datax" id="datax" value="${datax}">
                <input type="hidden" name="descricaox" id="descricaox" value="${descricaox}">
                <input type="hidden" name="parametrox" id="parametrox" value="${parametrox}">
                <input type="hidden" name="atributox" id="atributox" value="${atributox}">
                <input type="hidden" name="siglax" id="siglax" value="${siglax}">
                <input type="hidden" name="unidadex" id="unidadex" value="${unidadex}">
                <input type="hidden" name="valorx" id="valorx" value="${valorx}">
                <h4 class="modal-title"> <fmt:message key="view.lista.interpolacao" /> </h4>
            </div>

            <div class="modal-body">
                <div class="row">

                    <ul class="list-group" style="max-height: 400px;">
                        <c:forEach items="${interpolacoes}" var="i">
                            <li class="list-group-item listaInterpolacaoSelecionado" data-projetoId="${p.area.id}" data-interpoladorId="${i.id}" > 
                                <span class="glyphicon glyphicon-grain"></span> 
                                ${i.descricao} 
                                <span class="glyphicon glyphicon-chevron-right" style="float: right;"></span>
                            </li>			
                        </c:forEach>
                    </ul>

                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    <fmt:message key="view.escolher.classificador" var="prop1"/>
    <fmt:message key="view.trocar.cor" var="prop2"/>
    <fmt:message key="view.classificador.de" var="prop3"/>
    <fmt:message key="view.classificador.ate" var="prop4"/>
    <fmt:message key="view.classificador.cor" var="prop5"/>
    <fmt:message key="modal.interpolacao.tamanho" var="prop6"/>
    <fmt:message key="view.amostra.descricao" var="prop7"/>
    <fmt:message key="view.amostra.data" var="prop8"/>
    <fmt:message key="view.parametro" var="prop9"/>
    <fmt:message key="view.amostra.atributo" var="prop10"/>
    <fmt:message key="view.atributo.sigla" var="prop11"/>    
    <fmt:message key="view.atributo.unidade" var="prop12"/>  
    <fmt:message key="view.valor" var="prop13"/>  
    $(document).ready(function () {


        window.onload = function ()
        {
            $('#leg').val("${prop1}");
            $('#cores').val("${prop2}");
            $('#dex').val("${prop3}");
            $('#atex').val("${prop4}");
            $('#corx').val("${prop5}");
            $('#tamanhox').val("${prop6}");
            $('#descricaox').val("${prop7}");
            $('#datax').val("${prop8}");
            $('#parametrox').val("${prop9}");
            $('#atributox').val("${prop10}");
            $('#siglax').val("${prop11}");
            $('#unidadex').val("${prop12}");
            $('#valorx').val("${prop13}");
        };


    });





</script>
