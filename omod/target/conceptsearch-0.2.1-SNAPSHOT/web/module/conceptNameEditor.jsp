<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="View Concept Classes" otherwise="/login.htm" redirect="/module/conceptsearch/conceptNameEditor.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>


<!-- Tell 1.7+ versions of core to not include JQuery themselves. Also, on 1.7+ we may get different jquery and jquery-ui versions than 1.3.2 and 1.7.2 -->
<c:set var="DO_NOT_INCLUDE_JQUERY" value="true"/>

<!-- Include css from conceptmanagement module -->
<openmrs:htmlInclude file="${pageContext.request.contextPath}/moduleResources/conceptsearch/scripts/jquery/autocomplete/css/jquery.autocomplete.css" />

<!-- Include javascript from conceptmanagement module -->
<openmrs:htmlInclude file='${pageContext.request.contextPath}/moduleResources/conceptsearch/scripts/jquery/autocomplete/jquery.autocomplete.min.js'/>
<openmrs:htmlInclude file='${pageContext.request.contextPath}/moduleResources/conceptsearch/scripts/jquery/autocomplete/jquery.js'/>
<openmrs:htmlInclude file="${pageContext.request.contextPath}/moduleResources/conceptsearch/scripts/jquery-ui/js/jquery-ui-1.7.2.custom.min.js"/>
<openmrs:htmlInclude file='${pageContext.request.contextPath}/moduleResources/conceptsearch/scripts/jquery/autocomplete/jquery.autocomplete.js'/>


<%--
	passed from controller:
	* searchResult: concept with list of names tags and locales
--%>

<h2>
	<spring:message code="conceptsearch.manageconceptnameheading" />
</h2>

<br />
<br />
<div class="boxHeader">
<b>${concept.name}</div>
<div class="box">
<form method="post">
<span class="openmrsSearchDiv">
<table class="openmrsSearchTable">
		<tr><td></td><td></td><td></td><td><input type="submit" name="deleteTagWithConceptName" value="<spring:message code="conceptsearch.deletetags" />"></td><td><b>Concept Name Tag To Add:</b></td>
			<td><input id="conceptQuery" type="text" name="conceptQuery" size="25" value="${conceptQuery}">
			<script>
			$("#conceptQuery").autocomplete("<%=request.getContextPath()%>/module/conceptsearch/autocompletenametag.form");
			</script>&nbsp;&nbsp;&nbsp;
			<input type="submit" name="saveTagWithConceptName" value="<spring:message code="conceptsearch.savetags" />">
			</td></tr>
	<tr>
		
		<th><openmrs:message code="general.locale"/><a href="?sort=locales&order=desc"><img style="width: 14px; height: 14px;" border="0" src="<%=request.getContextPath()%>/images/movedown.gif"></a><a href="?sort=locales&order=asc"><img style="width: 14px; height: 14px;" border="0" src="<%=request.getContextPath()%>/images/moveup.gif"></a></th>
		<th><spring:message code="conceptsearch.nameLabel" /><a href="?sort=otherNames&order=desc"><img style="width: 14px; height: 14px;" border="0" src="<%=request.getContextPath()%>/images/movedown.gif"></a><a href="?sort=otherNames&order=asc"><img style="width: 14px; height: 14px;" border="0" src="<%=request.getContextPath()%>/images/moveup.gif"></a></th>
		<th><spring:message code="conceptsearch.tags" /><a href="?sort=conceptNameTags&order=desc"><img style="width: 14px; height: 14px;" border="0" src="<%=request.getContextPath()%>/images/movedown.gif"></a><a href="?sort=conceptNameTags&order=asc"><img style="width: 14px; height: 14px;" border="0" src="<%=request.getContextPath()%>/images/moveup.gif"></a></th>
		<th>Delete</th>
		<th>Add</th>

	</tr>

	<c:forEach var="conceptName" items="${concept.names}" varStatus="rowStatus">
		<tr class='${rowStatus.index % 2 == 0 ? "evenRow" : "oddRow"}'>
			<td>${conceptName.locale.displayLanguage}</td>
			<td>
			${conceptName}
			</td>
			<td><c:forEach var="tag" items="${conceptName.tags}" varStatus="rowTagStatus">
			</br>${tag}
			</c:forEach></td>
			<td><c:forEach var="tag" items="${conceptName.tags}" varStatus="rowStatusDelete">
			</br><input id="del${conceptName}${rowStatusDelete.index}" type="checkbox" name="del${conceptName}${rowStatusDelete.index}" value="${tag}">
			</c:forEach>
			</td>
			<td>
			<input id="${conceptName}" type="checkbox" name="${conceptName}" value="${conceptName}">
			</td>
		</tr>
	</c:forEach>
</table></br></br>

</span>
</form>
</div>
<%@ include file="/WEB-INF/template/footer.jsp"%>
