<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:if test="${not empty success}">
	<div class="alert alert-success alert-dismissable alert-close">
		<button type="button" class="close closeAlerta" data-dismiss="alert" >&times;</button>
		<fmt:message key="${success}"/>
	</div>	
</c:if>

<c:if test="${not empty danger}">
	<div class="alert alert-danger alert-dismissable alert-close">
		<button type="button" class="close closeAlerta" data-dismiss="alert" >&times;</button>
	<fmt:message key="${danger}"/>
	</div>	
</c:if>

<c:if test="${not empty info}">
	<div class="alert alert-info alert-dismissable alert-close">
		<button type="button" class="close closeAlerta" data-dismiss="alert" >&times;</button>
		<fmt:message key="${info}"/> 
	</div>	
</c:if>

<c:if test="${not empty warning}">
	<div class="alert alert-warning alert-dismissable alert-close">
		<button type="button" class="close" data-dismiss="alert" >&times;</button>
		<fmt:message key="${warning}"/> 
	</div>	
</c:if>

<script type="text/javascript">

	var taimerAlert;

	taimerAlert = setTimeout(function(){
		$(".alert-close").hide(1500);
		clearTimeout(taimerAlert);
	}, 6000);

</script>