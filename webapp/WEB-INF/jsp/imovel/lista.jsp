<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
	<head>
	
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title><fmt:message key="nome.projeto" /> - <fmt:message key="view.login.titulo" /></title>
		
		<jsp:include page="../include/link.jsp"></jsp:include>
	
	</head>
	<body>
		
		<jsp:include page="../menu/menu.jsp"></jsp:include>
		
		<div class="container">
			
			<h3 class="text-center"> <fmt:message key="view.lista.imoveis" /> </h3>
			
			<hr style="margin-bottom: 5px; margin-top: 5px;">
			
			<div class="row">	
				<div class="col-md-12">	
	                <a class="btn btn-block btn-default" href="formulario">				
						<i class="glyphicon glyphicon-file" > </i>
					 	<fmt:message key="view.lista.imoveis.cadastrada.imovel" />
					</a>
				</div>
			</div>	
			
			<hr>					
			
			<jsp:include page="../include/alerta.jsp"></jsp:include>
			
			<div class="table-responsive">
				<table class="table table-hover table-bordered ">
					<thead>
						<tr>
							<th class="text-center"><fmt:message key="view.nome.imovel" /></th>
							<th class="text-center"><fmt:message key="view.opcao" /></th>
						</tr>
					</thead>
					<tbody id="bodyListaImovel">
						
						<c:forEach items="${imoveis}" var="li">
							<tr>
								<td class="text-left">${fn:substring(li.nome, 0, 50)}</td>
								<td class="text-center">
									
									<a href="editar?id=${li.id}">
				 						<span class="btn btn-warning btn-xs">	
				 							<span class="glyphicon glyphicon-pencil" ></span>
				 							<fmt:message key="view.lista.imovel.editar" />
			 							</span>
				 					</a>	
									
									<div class="btn btn-xs btn-danger modalExcluir" id="${li.id}"
										data-titulo="<fmt:message key="view.imovel.excluir.titulo"/> ${li.nome}"
										data-mensagem=" <fmt:message key="view.imovel.excluir.mensagem"/>"
										data-excluir=" <fmt:message key="view.imovel.excluir"/> ">
										<span class="glyphicon glyphicon-trash"></span>
										<fmt:message key="view.lista.imovel.excluir" />
									</div>
								</td>
							</tr>
						</c:forEach>
						
					</tbody>
				</table>
			</div>
		</div>
	
		<jsp:include page="../include/script.jsp"></jsp:include>
		<jsp:include page="../modal/excluir.jsp"></jsp:include>
	
		<script type="text/javascript">
	
			$(".modalExcluir").on('click', function() {
	
				$('#modalExcluirTitulo').text(this.attributes.getNamedItem('data-titulo').value);
				$('#modalExcluirMensagem').text(this.attributes.getNamedItem('data-mensagem').value);
	
				$('#excluirConfirmar').html('<a class="btn btn-success" href="<c:url value="excluir?id='+ this.attributes.getNamedItem('id').value + '"/>">' + this.attributes.getNamedItem('data-excluir').value + '</a> ');
				$('#myModalExcluir').modal('show');
			
			});
			
		</script>
	</body>
</html>