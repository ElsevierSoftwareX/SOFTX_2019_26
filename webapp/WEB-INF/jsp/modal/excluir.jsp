<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<div class="modal" id="myModalExcluir" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">

            <div class="modal-header">
                <input type="hidden" value="${idProjeto}" name="idProjeto" id="idProjeto">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
                </button>				
                <h4 class="modal-title" id="modalExcluirTitulo"></h4>
            </div>

            <div class="modal-body">
                <div class="row">
                    <div class="col-xs-12 col-md-12">
                        <div class="text-center" style="margin-bottom: 0px;">
                            <img src="<c:url value='/resources/image/delete.png'/>" class="img-rounded" />
                            <h4 id="modalExcluirMensagem"></h4>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <div style="display: none" id="checkapi">
                   <div style="float: right;">  <fmt:message key="modal.excluirareaapi" /></div>
                    <div id="teste"><!--<input type="checkbox" name="salvarAPI" id="salvarAPI" style="margin-top: 0px;" value="nao" onclick="verificaChecks('salvarAPI')">  !--> 
                   </div> 
                    
                </div>
                <div style="float: right; display: block" id="excluirConfirmar1"></div>
                 <div style="float: right; display: none" id="excluirConfirmar2"></div>
                <div style="float: right; margin-right: 10px">
                    <button type="button" class="btn btn-warning" data-dismiss="modal"> <fmt:message key="view.cancelar"/> </button>
                </div>
                

            </div>

        </div>
    </div>
</div>

<jsp:include page="../include/script.jsp"></jsp:include>

<script type="text/javascript">
//    $(document).ready(function () {
//        $("#salvarAPI").on('click', function () {
//            if ($(this).is(':checked'))
//                //Retornar true ou false       
//                alert("CheckBox marcado.");
//            else
//                alert("CheckBox desmarcado.");
//        });
//    });



//    function verificaChecks() {
//        if (document.getElementById("salvarAPI").value === "nao") {
//            document.getElementById("salvarAPI").value = "sim";
//        } else {
//            document.getElementById("salvarAPI").value = "nao";
//        }
//    }

</script>