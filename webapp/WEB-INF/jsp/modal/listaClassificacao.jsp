<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="modal" id="myModalListaClassificacao" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">

			<div class="modal-header" style="border-bottom: 0px solid #e5e5e5;">
		  		<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
				</button>				
				<h4 class="modal-title"> <fmt:message key="modal.interpolacao.classificador" /> </h4>
			</div>
                        <input type="hidden" name="idmapa" id="idmapa" value="${idmapa}">
			<div class="modal-body">
				<div class="row">
					
					<ul class="list-group" style="max-height: 400px;">
						<c:forEach items="${classificacoes}" var="i">
							<li class="list-group-item listaClassificacaoSelecionado" data-classificacaoId="${i.id}" data-entidadeId="${i.entidade.codigo}" > 
								<span class="glyphicon glyphicon-triangle-right"></span> <b> <fmt:message key="view.codigo.classificacao" /> </b> : ${i.id} <span class="glyphicon glyphicon-chevron-right" style="float: right;"></span>
                                                                <br />
                                                                <span style="margin-left: 100px;"></span> <b> <fmt:message key="modal.area.tipo.solo" /> </b> :  ${i.tipoSolo.descricaoPT}
								<br />
                                                                <span style="margin-left: 100px;"></span> <b> <fmt:message key="modal.amostra.atributo" /> </b> :  ${i.atributo.descricaoPT}
								<br/>
                                                                <span style="margin-left: 100px;"></span> <b> <fmt:message key="view.classificador.entidade" /> </b> : ${i.entidade.descricao}
                                                        </li>			
						</c:forEach>
					</ul>
					
				</div>
			</div>
		</div>
	</div>
</div>