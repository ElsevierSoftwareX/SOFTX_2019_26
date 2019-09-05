<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- LISTA DE PROJETOS CADASTRADOS -->
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title> <fmt:message key="nome.projeto"/> - <fmt:message key="view.login.titulo"/> </title>

        <jsp:include page="../include/link.jsp"></jsp:include>
        </head>

        <body>
            <input type="hidden" name="idProjeto" id="idProjeto" value="${p.id}" />
        <jsp:include page="../menu/menu.jsp"></jsp:include>

            <div class="container">

                <h3 class="text-center"><fmt:message key="view.lista.projetos"/></h3>

            <hr style="margin-bottom: 5px; margin-top: 5px;">

            <div class="row">	
                <div class="col-md-12">
                    <a href="<c:url value='/projeto/formulario'/>" class="btn btn-block btn-default">
                        <spam class="glyphicon glyphicon-download-alt"> </spam>
                            <fmt:message key="view.lista.projeto.cadastrada.novo.projeto" />
                    </a>
                </div>
            </div>	

            <hr>

            <jsp:include page="../include/alerta.jsp"></jsp:include>

            <%-- 
            <form id="formularioFiltroProjeto" action="<c:url value='filtro' />" method="GET" >
                    <div class="row" style="margin-bottom: 10px;">
                            <div class="col-md-12">
                                    <div class="input-group"> 		
                                            <input id="filtroProjeto" type="text" class="form-control" name="filtro" value="${filtro}" placeholder="<fmt:message key="view.filtro.nome.projeto" /> ">
                                            <span class="input-group-addon btn-primary" style="color: #FFF;" onClick="document.forms['formularioFiltroProjeto'].submit();"> 
                                                    <span class="glyphicon glyphicon-search"></span> 
                                                    <fmt:message key="view.filtrar" /> 
                                            </span>
                                    </div>
                            </div>
                    </div>
            </form> --%>	

            <div class="table-responsive"> 
                <table class="table table-hover table-bordered ">

                    <thead> 
                        <tr> 
                            <th class="text-left">   <fmt:message key="view.projeto.nome" /> </th> 
                            <th class="text-center"> <fmt:message key="view.projeto.data" /> </th> 
                            <th class="text-center"> <fmt:message key="view.opcao" /></th> 
                        </tr> 
                    </thead> 

                    <tbody id="bodyListaProjeto"> 
                        <c:forEach items="${projetos}" var="p">

                            <tr>
                                <td  class="text-left"> ${fn:substring(p.nome, 0, 50)} </td>
                                <td class="text-center"> <fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${p.data}" /> </td>
                                <td class="text-center"> 

                                    <a href="/sdum-web/mapa?id=${p.id}" id="teste">
                                        <span class="btn btn-success btn-xs">	
                                            <span class="glyphicon glyphicon-folder-open" ></span>
                                            <fmt:message key="lista.projeto.abrir" />
                                        </span>
                                    </a>	

                                    <a href="area/lista?id=${p.id}">
                                        <span class="btn btn-primary btn-xs">	
                                            <span class="glyphicon glyphicon-list"></span>
                                            <fmt:message key="lista.projeto.listar.areas" />
                                        </span>
                                    </a>

                                    <a href="editar?id=${p.id}"	>
                                        <span class="btn btn-warning btn-xs">	
                                            <span class="glyphicon glyphicon-pencil" ></span>
                                            <fmt:message key="lista.projeto.editar" />
                                        </span>
                                    </a>	

                                    <div class="btn btn-danger btn-xs modalExcluir" id="${p.id}" 
                                         data-titulo="<fmt:message key="view.projeto.excluir.titulo"/> ${p.nome}"
                                         data-mensagem="<fmt:message key="view.projeto.excluir.mensagem"/>"
                                         data-excluir="<fmt:message key="view.projeto.excluir"/>">
                                        <span class= "glyphicon glyphicon-trash"></span>	 
                                        <fmt:message key="lista.projeto.excluir" />						
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
                 $(document).ready(function () {
                    $('#menuinicial').show(); 
                    $('#sdum').hide();
                    $('#home').hide(); 
                    $('#sair').show(); 
                 });
          
                document.getElementById('teste').addEventListener('click', function (e) {

                  
                    document.getElementById("menuinicial").style.display = 'block';
          
                });

//                function atribuiprojeto(idproj)
//                 {
//
//                     $('#idProjeto').val(idproj);
//                     console.log("ENTROU EM ABRIR PROEJTO!"+idproj);
//                 }

                $(".modalExcluir").on('click', function () {

                    $('#modalExcluirTitulo').text(this.attributes.getNamedItem('data-titulo').value);
                    $('#modalExcluirMensagem').text(this.attributes.getNamedItem('data-mensagem').value);
                    $('#excluirConfirmar1').html('<a class="btn btn-success" href="<c:url value="excluir?id='+ this.attributes.getNamedItem('id').value+'"/>">' + this.attributes.getNamedItem('data-excluir').value + '</a> ');
                    //$('#excluirConfirmar2').html('<a class="btn btn-success" href="<c:url value="excluir?id='+this.attributes.getNamedItem('id').value+'"/>">' + this.attributes.getNamedItem('data-excluir').value + ' </a> ');
                    //$('#excluirConfirmar').html('<a class="btn btn-success" href="<c:url value="excluir?id='+ this.attributes.getNamedItem('id').value +'"/>">' + this.attributes.getNamedItem('data-excluir').value + '</a> ');
                    $('#myModalExcluir').modal('show');
                });

                /* $("#buttonBuscarMaisProjetos").on('click', function(){
                 
                 var filtro = $("#filtroProjeto").val();
                 var pagina = $("#paginaTabela" ).val();
                 
                 $.getJSON('/sdum/projeto/buscar?filtro='+filtro+'&pagina='+pagina, function( data ) {
                 
                 $("#paginaTabela" ).val( parseInt(pagina) + 1 );
                 
                 if(data.list.length < 10){
                 $("#buttonBuscarMaisProjetos").hide();
                 }
                 
                 for (var i = 0; i < data.list.length; i++) {
                 
                 var row  = data.list[i];
                 var html = '';
                 
                 html += '<tr>';
                 html += '	<td class="text-center"> '+ formatData(row.data.$)     +' </td>';
                 html += '	<td  class="text-left">  '+ row.nome.slice(0, 50)      +' </td>';
                 html += '	<td class="text-center">';
                 html += '		<div class="btn btn-warning btn-xs">';	
                 html += '			<span class="glyphicon glyphicon-pencil" ></span>';
                 html += '		</div>';
                 html += '		<div class="btn btn-danger btn-xs">';
                 html += '			<span class= "glyphicon glyphicon-trash"></span>';						
                 html += '		</div>';
                 html += '	</td>';
                 html += '</tr>';
                 
                 $("#bodyListaProjeto").append(html);
                 }
                 }).fail(function (){
                 console.log("Não foi possivel buscar mais projetos!!");
                 });
                 }); */

                function formatData(data) {

                    var d = new Date(data);

                    return ("00" + d.getDate()).slice(-2) + "/" + ("00" + (d.getMonth() + 1)).slice(-2) + "/" + d.getFullYear() + " " +
                            ("00" + d.getHours()).slice(-2) + ":" + ("00" + d.getMinutes()).slice(-2);
                }

        </script>

    </body>
</html>