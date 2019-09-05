<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<div class="modal" id="myModalSemivariograma" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">

            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
                </button>				
            </div>

            <input type="hidden" name="i.id"  value='${i.id}' id="i.id">

            <div class="modal-body">
                <table align="center" style="align-items: center">
                    <td class="text-center" colspan="2"> <h2> <fmt:message key="view.interpolacao.semivariogramas" /></h2></td>
                    <tr>
                        <th class="text-center" id="mostrarimagem"> </th> 
                        <td class="text-center" id="mostrarmelhorsemi"></td>
                    </tr>
                </table>
            </div>


            <div class="modal-body">
                <table align="center" style="align-items: center" border="1">
                    <td class="text-center" colspan="2"> <h2><fmt:message key="view.interpolacao.geoestatistica" /> </h2></td>
                    <tr>
                        <th class="text-center"> <fmt:message key="view.interpolacao.metodo" /> </th> 
                        <td class="text-center" id="modalmetodo"></td>
                    </tr>
                    <tr>
                        <th class="text-center"> <fmt:message key="view.interpolacao.modelo" /> </th> 
                        <td class="text-center" id="modalmodelo"></td>
                    </tr>
                    <tr>
                        <th class="text-center"> <fmt:message key="view.interpolacao.ice" /> </th>
                        <td class="text-center" id="modalice"></td>
                    </tr>
                    <tr>
                        <th class="text-center"> <fmt:message key="view.interpolacao.contribuicao" /> </th>
                        <td class="text-center" id="modalcontribuicao"></td>
                    </tr>
                    <tr>
                        <th class="text-center"> <fmt:message key="view.interpolacao.alcance" /> </th> 
                        <td class="text-center" id="modalalcance"></td>
                    </tr>
                    <tr>
                        <th class="text-center"> <fmt:message key="view.interpolacao.kappa" /> </th> 
                        <td class="text-center" id="modalkappa"></td>
                    </tr>
                    <tr>
                        <th class="text-center"> <fmt:message key="view.interpolacao.isi" /> </th> 
                        <td class="text-center" id="modalisi"></td>
                    </tr>
                    <tr>
                        <th class="text-center"> <fmt:message key="view.interpolacao.standarddeviationmeanerror" /> </th> 
                        <td class="text-center" id="modalstandard"></td>
                    </tr>
                    <tr>
                        <th class="text-center"> <fmt:message key="view.interpolacao.meanerror" /> </th> 
                        <td class="text-center" id="modalmean"></td>
                    </tr>
                </table>
            </div>

        </div>
    </div>
</div>