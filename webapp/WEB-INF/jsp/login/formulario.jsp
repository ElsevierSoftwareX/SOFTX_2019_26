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
		 
		 <div class="container" style="width: 500px; border: 0px solid #ddd;">
		 	
		 	<jsp:include page="../include/alerta.jsp"></jsp:include>
		 	
		 	<div class="bs-example bs-example-tabs" data-example-id="togglable-tabs"> 
				<ul id="myTabs" class="nav nav-tabs" role="tablist"> 
					<li role="presentation" class="active">
						<a href="#formLogin" id="home-tab"  role="tab" data-toggle="tab" aria-controls="home" aria-expanded="true"> <fmt:message key="view.login.titulo"/></a>
					</li> 
					<li role="presentation">
						<a href="#formCadastroUsuario" role="tab" id="profile-tab" data-toggle="tab" aria-controls="profile"> <fmt:message key ="view.novo.usuario.titulo"/></a>
					</li>  
				</ul> 
			</div>	
		
			<div id="myTabContent" class="tab-content" > 
				
				<div role="tabpanel" class="tab-pane fade in active" id="formLogin"  aria-labelledby="home-tab"> 
					
					<form id="formLogin" action="<c:url value='logar' />" method="POST" >
						
						<!-- <div class="row">
							<div class="col-xs-12">
								<input type="text" name="email" class="form-control" placeholder= <fmt:message key="view.login.email"/> required>
							</div>
						</div> -->
						 <div class="row">
							<div class="col-xs-12">
								<input type="tel" name="login.telefone" class="form-control" placeholder= <fmt:message key="view.login.telefone"/> required>
							</div>
						</div>
						<div class="row" style="margin-top: 5px;">
							<div class="col-xs-12">
								<input type="password" name="login.password" class="form-control" placeholder= <fmt:message key="view.login.senha"/> required>
							</div>
						</div>
							
						<div class="form-group" style="margin-bottom: 20px; margin-top: 10px;">
							<input type="checkbox">
							<label for="remember"> <fmt:message key="view.login.lembrarsenha"/>  </label>
						</div>							
						<div class="row">
							<div class="col-sm-6 col-sm-offset-3">
								<input type="submit" class="form-control btn-success btn-block" value="<fmt:message key="view.login.entrar"/>">
							</div>
						</div>
						<div class="row">
							<div class="col-lg-12">
								<div class="text-center">
								<!--	<a href="" class="forgot-password"> <fmt:message key="view.login.esquecisenha"/></a>
								!--></div>
							</div>
						</div>						
				  	</form>

			 	</div> 

				<div role="tabpanel" class="tab-pane fade" id="formCadastroUsuario" aria-labelledby="profile-tab"> 
				    
				   	<form id="formCadastroUsuario" action="<c:url value='cadastrar' />" method="POST" >
				    
					    <div class="row">
							<div class="col-xs-12">	
				 				<input type="text" name="usuario.nome" class="form-control" placeholder= <fmt:message key="view.novo.usuario.nome"/> required>
					   		</div>
					   	</div>
					   	 <div class="row" style="margin-top: 5px;">
							<div class="col-xs-12">	
						        <input type="tel" name="usuario.telefone"  class="form-control" placeholder= <fmt:message key="view.novo.usuario.telefone"/> required>
					   		</div>
					   	</div>
					   	<div class="row" style="margin-top: 5px;">
							<div class="col-xs-12">	
						        <input type="email" name="usuario.email"  class="form-control" placeholder= <fmt:message key="view.novo.usuario.email"/> required>
					   		</div>
					   	</div>
					   	<div class="row" style="margin-top: 5px;">
							<div class="col-xs-12">		
								<input type="password" name="usuario.senha" class="form-control" placeholder= <fmt:message key="view.novo.usuario.senha"/> required>
				   	  		</div>
					   	</div>					
						<!-- <div class="row" style="margin-top: 5px;">
							<div class="col-xs-12">	
						        <input type="text" name="usuario.endereco" class="form-control" placeholder= <fmt:message key="view.novo.usuario.endereco"/> required> 
					   	    </div>
					   	</div>
						<div class="row" style="margin-top: 5px;">
							<div class="col-xs-12">	
						        <input type="text" name="usuario.numero" class="form-control" placeholder= <fmt:message key="view.novo.usuario.numero"/> required> 
				            </div>
					   	</div>
						<div class="row" style="margin-top: 5px;">
							<div class="col-xs-12">						
								<input type="text" name="usuario.complemento" class="form-control" placeholder= <fmt:message key="view.novo.usuario.complemento"/>> 
					   	    </div>
					   	</div>
						<div class="row" style="margin-top: 5px;">
							<div class="col-xs-12">		
								<input type="text" name="usuario.bairro" class="form-control" placeholder= <fmt:message key="view.novo.usuario.bairro"/> required>
					      	</div>
					   	</div>					 -->
						 <div class="form-group">
							<div class="row" style="margin-top: 10px;">
								<div class="col-sm-6 col-sm-offset-3">
									<input type="submit" name="register.submit" class="form-control btn-success btn-block" value="<fmt:message key="view.login.cadastrar"/>">
								</div>
							</div>
						</div> 
					</form>
				</div> 				
			</div> 				
		</div> 		 
		
		<jsp:include page="../include/script.jsp"></jsp:include>
                <script type="text/javascript">
                   
         
                    $(document).ready(function () {
                        document.getElementById("menuinicial").style.display = 'block';
                          $('#home').hide();
                    });
                    </script>
	
	</body>
</html>