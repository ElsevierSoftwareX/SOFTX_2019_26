<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<form id="formularioAreaServer" action="<c:url value='persiste/areaServer' />" method="POST" >

    <input type="hidden" value="${idProjeto}" name="idProjeto">

    <div class="modal fade" id="modalBuscaArea" tabindex="-1" role="dialog" aria-labelledby="modalBuscaAreaLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title" id="modalBuscaAreaLabel">
                        <fmt:message key="modal.area.buscar" />
                    </h4>
                </div>
                <div class="modal-body">
                    <div class="table-responsive">
                        <table class="table table-hover table-bordered ">
                            <thead>
                                <tr>
                                    <th class="text-center"><fmt:message key="view.area.nome" />
                                    </th>
                                    <th class="text-center"><fmt:message key="view.projeto.selecionar" /></th>
                                </tr>
                            </thead>

                            <c:forEach items="${listaAreaAPI}" var="a" varStatus="ar">
                                <tr>
                                    <td class="text-left">${a.codigo} - ${a.descricao}</td>
                                    <td class="text-center">
                                        <input type="checkbox" class="text-center" id="camposMarcados" 
                                               name="camposMarcados[]" value="${a.codigo}"  />
                                    </td>
                                </tr>
                            </c:forEach>

                            </tbody>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-warning" data-dismiss="modal">
                            <fmt:message key="view.cancelar" />
                        </button>
                        <input type="submit" class="btn btn-primary" value=<fmt:message key="modal.area.importar" />>

                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<jsp:include page="../include/script.jsp"></jsp:include>

<script type="text/javascript">
    $(document)
            .ready(
                    function () {
                        var camposMarcados = new Array();

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
                                            console.log("Campos Marcados: "
                                                    + camposMarcados);
                                        }
                                    });
                        }
                    });
</script>