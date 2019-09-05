<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
	<head>
	 
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title> <fmt:message key="nome.projeto"/> - <fmt:message key="view.login.titulo"/> </title>
		
		<jsp:include page="../include/link.jsp"></jsp:include>

	</head>
	<body>
		 
		 <jsp:include page="../menu/menu.jsp"></jsp:include>
		 
		 <div class="container" style="border: 1px solid #ddd;">
		 	
		 	<form id="formularioImovel" action="<c:url value='persiste' />" method="POST" >
		 	
		 		<h4 class="text-center"> <fmt:message key="view.imovel.novo"/></h4> 	
		 	
		 		<hr>
		 
		 		<jsp:include page="../include/alerta.jsp"></jsp:include>
		 			 	
		 		<input type="hidden" name="imovel.id"     value="${imovel.id}">
		 		<input type="hidden" name="imovel.codigo" value="${imovel.codigo}">
		 		
		 		<div class="row">
		 			<div class="col-md-12">
	 				    <label for="imovel.nome">
	 				    	<fmt:message key="view.imovel.nome" /> 
 				    	</label>
                        <input type="text" class="form-control" name="imovel.nome" value="${imovel.nome}"> 			 			
		 			</div>
		 		</div>
		 		
		 		<div class="row">
		 			<div class="col-md-6">
	 				    <label for="imovel.incra">
	 				    	<fmt:message key="view.imovel.incra" />
 				    	</label>
                        <input type="text" class="form-control" name="imovel.incra" value="${imovel.incra}"> 			 			
		 			</div>
		 			<div class="col-md-6">
		 				<label for="imovel.nrf">
		 					<fmt:message key="view.imovel.NRF" />
		 				</label>
                        <input type="text" class="form-control" name="imovel.nrf" value="${imovel.nrf}">
                    </div>
		 		</div>			 	
                  
                <div class="row">
		 			<div class="col-md-12">  
                 		<label for="imovel.endereco">
                 			<fmt:message key="view.novo.usuario.endereco" /> 
               			</label>
                        <input type="text" class="form-control" name="imovel.endereco" value="${imovel.endereco}"> 
   					</div>
  				</div>
  				
  				<div class="row">
		 			<div class="col-md-12">  
                 		<label for="imovel.bairro">
                 			<fmt:message key="view.novo.usuario.bairro" />
                 		</label>
                        <input type="text" class="form-control" name="imovel.bairro" value="${imovel.bairro}">
   					</div>
  				</div>
		        
		        <div class="row">
		 			<div class="col-md-6">  
                 		<label for="imovel.numero">
                 			<fmt:message key="view.novo.usuario.numero" />
               			</label>
                        <input type="text" class="form-control" name="imovel.numero" value="${imovel.numero}">
   					</div>
  					<div class="col-md-6">  
                 		<label for="imovel.complemento">
                 			<fmt:message key="view.novo.usuario.complemento" />
               			</label>
                        <input type="text" class="form-control" name="imovel.complemento" value="${imovel.complemento}">
   					</div>
  				</div>
		        
	      		<hr>

	      		<input style="float: right;" type="submit" class="btn btn-success" value=<fmt:message key="cadastrar"/>>
      		
      		</form>
     	</div>

		<jsp:include page="../include/script.jsp"></jsp:include>
	
	</body>
</html>