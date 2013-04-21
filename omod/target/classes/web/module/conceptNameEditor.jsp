<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="View Concept Classes" otherwise="/login.htm" redirect="/module/conceptsearch/conceptNameEditor.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>

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
<span class="openmrsSearchDiv">
<table class="openmrsSearchTable">
	<tr>
		
		<th><openmrs:message code="general.locale"/><a href="?sort=conceptLocale&order=desc"><img style="width: 14px; height: 14px;" border="0" src="<%=request.getContextPath()%>/images/movedown.gif"></a><a href="?sort=conceptClass&order=asc"><img style="width: 14px; height: 14px;" border="0" src="<%=request.getContextPath()%>/images/moveup.gif"></a></th>
		<th><spring:message code="conceptsearch.nameLabel" /><a href="?sort=conceptName&order=desc"><img style="width: 14px; height: 14px;" border="0" src="<%=request.getContextPath()%>/images/movedown.gif"></a><a href="?sort=conceptName&order=asc"><img style="width: 14px; height: 14px;" border="0" src="<%=request.getContextPath()%>/images/moveup.gif"></a></th>
		<th><spring:message code="conceptsearch.tags" /><a href="?sort=conceptTag&order=desc"><img style="width: 14px; height: 14px;" border="0" src="<%=request.getContextPath()%>/images/movedown.gif"></a><a href="?sort=conceptDatatype&order=asc"><img style="width: 14px; height: 14px;" border="0" src="<%=request.getContextPath()%>/images/moveup.gif"></a></th>
		<!-- <th>Other Names</th> -->
	</tr>

	<c:forEach var="concept" items="${searchResult.pageList}" varStatus="rowStatus">
		<tr class='${rowStatus.index % 2 == 0 ? "evenRow" : "oddRow"}'>
			<td><c:forEach var="locale" items="${concept.locales}" varStatus="rowStatus">
			</br>${locale}
			</c:forEach>
			</td>
			<td>
			<c:forEach var="otherName" items="${concept.otherNames}" varStatus="rowStatus">
			</br>${otherName}
			</c:forEach>
			</td>
			<td><c:forEach var="tag" items="${concept.conceptNameTags}" varStatus="rowStatus">
				</br>${tag}
			</c:forEach>
			</td>
			<!--  <td>${concept.otherNames}</td> -->
		</tr>
	</c:forEach>
</table>
</span>
</div>
<%@ include file="/WEB-INF/template/footer.jsp"%>
