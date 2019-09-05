<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
    <head>

    </head>
    <body>

        <jsp:include page="../menu/menu.jsp"></jsp:include>

            <form id="formularioNovaGrade" action="<c:url value='persiste/GradePixelXY' />" method="POST" >

            <input type="hidden" name="gradeAmostral" value="${gradeAmostral}" id="gradeAmostral"/> 
            <input type="hidden" name="area.codigo" value="${area.codigo}" />
            <input type="hidden" name="formGrade" value="true" id="controleFormGrade">


            <div class="modal fade" id="modalNovaGrade" tabindex="-1" role="dialog" aria-labelledby="modalGradeaAmostraLabel">
                <div  class= "modal-dialog "  role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                            <h4 class="modal-title" id="modalGradeaAmostraLabel">
                                <fmt:message key="modal.grade.nova" />
                            </h4>
                        </div>
                        <div class="modal-body">
                            <div class="table-responsive">
                                <table class="table table-hover table-bordered ">														
                                    <tbody>
                                        <tr>
                                    <div class="col-md-12"> 
                                        <label><fmt:message key="modal.grade.descricao" />  
                                            <span class="required-class"> * </span>
                                        </label>   
                                        <input type="text"  class="form-control" name="gradeAmostral.descricao" value="${gradeAmostral.descricao}" maxlength="100" minlength="4" required> 
                                    </div>	
                                    <br><br><br><br>	
                                    <div class="form-group"> 	                  
                                        <div class="col-xs-6">
                                            <label> 
                                                <fmt:message key="view.amostra.pixel.x.metros" /> 
                                                <span class="required-class"> * </span>
                                            </label> 
                                            <input type="text" class="form-control" name="gradeAmostral.tamx" id="gradeAmostral.tamx" placeholder="<fmt:message key="view.amostra.pixel.x.metros" /> "  required="required" maxlength="10" minlength="1">
                                        </div> 
                                        <div class="col-xs-6">
                                            <label> 
                                                <fmt:message key="view.amostra.pixel.y.metros" /> 
                                                <span class="required-class"> * </span>
                                            </label>  <br>
                                            <input type="text" class="form-control" name="gradeAmostral.tamy" id="gradeAmostral.tamy" placeholder="<fmt:message key="view.amostra.pixel.y.metros" /> " required="required" maxlength="10" minlength="1">
                                        </div> 
                                    </div>

                                    </tbody>										
                                </table> 
                                <br>	

                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-warning" data-dismiss="modal">
                                    <fmt:message key="view.cancelar" />
                                </button>
                                <input type="submit" class="btn btn-primary"<fmt:message key="modal.amostra.importar" />>

                            </div>
                        </div>			
                    </div>  
                </div>    
            </div>                                   
        </form>
        <script type="text/javascript">
            document.getElementById("menuinicial").style.display = 'block';
        </script>	

        <jsp:include page="../include/script.jsp"></jsp:include>
    </body>
</html>