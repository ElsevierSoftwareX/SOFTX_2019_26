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
		 
		 <div class="container" style="width: 800px; border: 1px solid #ddd;">
		 	
		 	<form id="formularioImovel" action="<c:url value='persiste' />" method="POST" >
		 	
		 		<h4 style="margin-left: 10px;"> <fmt:message key="view.imovel.novo"/></h4> 	
		 	
		 		<hr>
		 
		 		<jsp:include page="../include/alerta.jsp"></jsp:include>
		 			 	
		 	 	<div class="modal-body">
		        	
		        	<input type="hidden" name="imovel.id" value="${imovel.codigo}">
		        	  <div class="form-group">
                        <label for="imovel.nome"><fmt:message key="view.imovel.nome" /> </label>
                        <input type="text" class="form-control" name="imovel.nome" value="${imovel.nome}"  >   </div><br>
                    
                    <div class="form-group">
                        <label for="imovel.incra"><fmt:message key="view.imovel.incra" /></label>
                        <input type="text" class="form-control" name="imovel.incra" value="${imovel.incra}"  >
                    </div><br>
                    
                    <div class="form-group">
                        <label for="imovel.nrf"><fmt:message key="view.imovel.NRF" /></label>
                        <input type="text" class="form-control" name="imovel.nrf" value="${imovel.nrf}"  >
                    </div><br>
                    
                      <div class="form-group">
                        <label for="imovel.pais"><fmt:message key="view.imovel.pais" /></label>
                        <input type="text" class="form-control" name="imovel.pais" value="${imovel.pais} " >
                    </div>
                    <br>
                    <div class="form-group">
                        <label for="estado"> <fmt:message key="view.imovel.estado" /></label>
                        <div class="row">
                            <div class="col-xs-12">
                                <select id="imovel.estado" name="estado" class="form-control" required="required" onchange="updateCidade(this);">
                                    <option disabled selected><fmt:message key="view.imovel.selecione.estado" /></option>                                 
                                      
										<option value="ac">Acre</option> 
										<option value="al">Alagoas</option> 
										<option value="am">Amazonas</option> 
										<option value="ap">Amapá</option> 
										<option value="ba">Bahia</option> 
										<option value="ce">Ceará</option> 
										<option value="df">Distrito Federal</option> 
										<option value="es">Espírito Santo</option> 
										<option value="go">Goiás</option> 
										<option value="ma">Maranhão</option> 
										<option value="mt">Mato Grosso</option> 
										<option value="ms">Mato Grosso do Sul</option> 
										<option value="mg">Minas Gerais</option> 
										<option value="pa">Pará</option> 
										<option value="pb">Paraíba</option> 
										<option value="pr">Paraná</option> 
										<option value="pe">Pernambuco</option> 
										<option value="pi">Piauí</option> 
										<option value="rj">Rio de Janeiro</option> 
										<option value="rn">Rio Grande do Norte</option> 
										<option value="ro">Rondônia</option> 
										<option value="rs">Rio Grande do Sul</option> 
										<option value="rr">Roraima</option> 
										<option value="sc">Santa Catarina</option> 
										<option value="se">Sergipe</option> 
										<option value="sp">São Paulo</option> 
										<option value="to">Tocantins</option> 
						                                 
                                </select>
                            </div>
                        </div>
                    </div><br>
                    
                    <div class="form-group">
                        <label for="imovel.cidade.codigo"> <fmt:message key="view.imovel.selecione.cidade" /></label>
               			<input type="text" class="form-control" name="view.imovel.selecione.cidade" id="imovel.cidade" >						                                   
                    </div>
                   <br>                                       
                    <div class="form-group">
                        <label for="imovel.endereco"><fmt:message key="view.novo.usuario.endereco" /> </label>
                        <input type="text" class="form-control" name="imovel.endereco" id="imovel.endereco"  >
                    </div>
                    <div class="form-group"><br>
                        <label for="imovel.bairro"><fmt:message key="view.novo.usuario.bairro" /></label>
                        <input type="text" class="form-control" name="imovel.bairro" id="imovel.bairro"  >
                    </div>
                    <div class="form-group"><br>
                        <label for="imovel.complemento"><fmt:message key="view.novo.usuario.complemento" /></label>
                        <input type="text" class="form-control" name="imovel.complemento" id="imovel.complemento" >
                    </div>

                    <div class="form-group"><br>
                        <label for="imovel.numero"><fmt:message key="view.novo.usuario.numero" /></label>
                        <input type="text" class="form-control" name="imovel.numero" id="imovel.numero" >
                    </div>

                    <div class="form-group"><br>
                        <label for="imovel.proprietario.codigo"><fmt:message key="view.imovel.proprietario" /></label>
                          <input type="text" class="form-control" name="imovel.proprietario.codigo" id="imovel.proprietario.codigo"  >                                                  
                    </div>
                  
   
		        			        	
		        	
	      		
	      		<hr>
	      		
	      		
	      		<input style="float: right;" type="submit" class="btn btn-success" value=<fmt:message key="cadastrar"/> />
      		
      		</form>
		</div> 		 
	    </div>	
		<jsp:include page="../include/script.jsp"></jsp:include>
	
	</body>
	

	<script type="text/javascript">	
	
   </script>
		
</html>
