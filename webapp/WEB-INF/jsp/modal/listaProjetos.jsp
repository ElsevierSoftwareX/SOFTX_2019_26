<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="modal" id="myModalListaProjeto" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">

			<div class="modal-header" style="border-bottom: 0px solid #e5e5e5;">
		  		<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
				</button>				
				<h4 class="modal-title"> <fmt:message key="view.mapa.linta.projetos" /> </h4>
			</div>
			
			<div class="modal-body">
				<div class="row">
					
					<ul class="list-group" style="max-height: 400px;">
						<c:forEach items="${amostras}" var="a">
							<li class="list-group-item listaProjetoSelecionado" data-areaId="${a.area.id}" data-amostraId="${a.id}"  data-atributoId="${a.atributo.codigo}" data-unidadeMedidaId="${a.atributo.unidadeMedidaPT.sigla}"> 
								<span class="glyphicon glyphicon-file"></span> <b> <fmt:message key="view.projeto" /> </b> :  ${a.area.projeto.descricao}
								<br />
								<span style="margin-left: 50px;"></span> <b> <fmt:message key="view.area.modal" /> </b> : ${a.area.descricao} <span class="glyphicon glyphicon-chevron-right" style="float: right;"></span>
								<br />
								<span style="margin-left: 100px;"></span> <b> <fmt:message key="view.amostra" /> </b> : ${a.descricao} 
								<br />
                                                                <span style="margin-left: 100px;"></span> <b> <fmt:message key="view.amostra.atributo" /> </b> : ${a.atributo.descricaoPT} 
								<br />
								<span style="margin-left: 100px;"></span> <b> <fmt:message key="view.unidademedida" /> </b> : ${a.atributo.unidadeMedidaPT.sigla} 
							</li>			
						</c:forEach>
					</ul>
					
				</div>
			</div>
		</div>
	</div>
</div>