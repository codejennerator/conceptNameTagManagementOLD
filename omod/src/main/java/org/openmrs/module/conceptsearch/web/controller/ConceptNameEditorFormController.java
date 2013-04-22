/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.conceptsearch.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.openmrs.Concept;
import org.openmrs.ConceptName;
import org.openmrs.ConceptNameTag;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.conceptsearch.ConceptSearch;
import org.openmrs.module.conceptsearch.ConceptSearchResult;
import org.openmrs.module.conceptsearch.ConceptSearchService;
import org.openmrs.web.WebConstants;
import org.openmrs.web.controller.ConceptFormController.ConceptFormBackingObject;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Controller to handle edit of concept names (only tags for now).
 */
@Controller
public class ConceptNameEditorFormController extends AbstractSearchFormController {

	@ModelAttribute("conceptQuery")
	public String getConceptQuery(@RequestParam(value="conceptQuery", required=false) String conceptQuery) {
		return (conceptQuery == null ? "" : conceptQuery);
    }
	
	@InitBinder("conceptQuery")
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.setAllowedFields(new String[] { "conceptQuery" });
		dataBinder.setRequiredFields(new String[] { "conceptQuery" });
	}
	
	@RequestMapping(value = "/module/conceptsearch/conceptNameEditor", method = RequestMethod.GET)
	public void showBasicSearch(ModelMap model, WebRequest request, HttpSession session) {
		//display basicSearch.jsp	
		session.removeAttribute("searchResult");
		session.removeAttribute("sortResults");
		session.removeAttribute("conceptSearch");
	}
	
	@RequestMapping(value = "/module/conceptsearch/conceptNameEditor", method = RequestMethod.POST)
	protected void performSaveorDelete(ModelMap model, WebRequest request, HttpSession session) {
		
		ConceptService cs = Context.getConceptService();
		ConceptSearchService searchService = (ConceptSearchService) Context.getService(ConceptSearchService.class);
		
		String id = request.getParameter("conceptId");
		int cid = Integer.parseInt(id);
		Concept concept = searchService.getConcept(cid);

			
			if (request.getParameter("saveTagWithConceptName") != null) {
				
				for(ConceptName cn : concept.getNames()){
					if(request.getParameter(cn.getName())!=null){
						String tagName = request.getParameter("conceptQuery");
						ConceptNameTag jennTag = cs.getConceptNameTagByName(tagName);
						cn.addTag(jennTag);
						cs.saveConcept(concept);
					}
				}
				
			}
			if (request.getParameter("deleteTagWithConceptName") != null) {
				for(ConceptName cn : concept.getNames()){
					int i = 0; boolean del = false;
					List<String> tags = new Vector<String>();
					for(ConceptNameTag cnt : cn.getTags()){
						if(request.getParameter("del"+cn.getName()+i)!=null){
							del = true;
							
							String tagName = request.getParameter("del"+cn.getName()+i);
							System.out.println("tagname----------------------------------------------->"+tagName);
							//ConceptNameTag jennTag = cs.getConceptNameTagByName(tagName);
							tags.add(tagName);
							
						}
						i++;
						
					}
					if(del){
						for(String cntName : tags){
							ConceptNameTag jennTag = cs.getConceptNameTagByName(cntName);
							cn.removeTag(jennTag);
							System.out.println("del----------------------------------------------- true");
							
						}
						cs.saveConcept(concept);
					}
				}
				
			}
			
			
			model.addAttribute("concept", concept);
			session.setAttribute("concept", concept);
	}
	
	@RequestMapping(value = "/module/conceptsearch/conceptNameEditor", method = RequestMethod.GET, params = "count")
	public void setConceptsPerPage(ModelMap model, WebRequest request, HttpSession session) {
		super.setConceptsPerPage(model, request, session);
	}
	
	@RequestMapping(value = "/module/conceptsearch/conceptNameEditor", method = RequestMethod.GET, params = "page")
	public void switchToPage(@RequestParam("page") String page, ModelMap model, WebRequest request, HttpSession session) {
		super.switchToPage(page, model, request, session);
	}
	
	@RequestMapping(value = "/module/conceptsearch/conceptNameEditor", method = RequestMethod.GET, params = "sort")
    public void sortResultsView(ModelMap model, WebRequest request, HttpSession session) {
		super.sortResultsView(model, request, session);
	}
	@RequestMapping(value = "/module/conceptsearch/conceptNameEditor", method = RequestMethod.GET, params = "conceptId")
	public void displayConceptEditPage(ModelMap model, WebRequest request, HttpSession session) {
		ConceptSearchService searchService = (ConceptSearchService) Context.getService(ConceptSearchService.class);
		System.out.println("***********************************jenn inside concept name editor*************************");
		String id = request.getParameter("conceptId");
		int cid = Integer.parseInt(id);
		
		Concept concept = searchService.getConcept(cid);
		List<ConceptSearchResult> resList = new ArrayList<ConceptSearchResult>();	

		if (concept != null) {
			ConceptSearchResult res = new ConceptSearchResult(concept);
			res.setNumberOfObs(searchService.getNumberOfObsForConcept(concept.getConceptId()));
			resList.add(res);
				
		}
		// add results to ListHolder
		PagedListHolder resListHolder = new PagedListHolder(resList);
		resListHolder.setPageSize(DEFAULT_RESULT_PAGE_SIZE);
		
		model.addAttribute("searchResult", resListHolder);
		session.setAttribute("sortResults", resListHolder);
		model.addAttribute("concept", concept);
	}
	
	@RequestMapping(value = "/module/conceptsearch/autocompletenametag", method = RequestMethod.GET)
	public void doAutocomplete(ModelMap model, WebRequest request, HttpSession session) {
		//ConceptSearchService searchService = (ConceptSearchService) Context.getService(ConceptSearchService.class);
		//String searchFor = request.getParameter("q");
		//List<String> autoResults = searchService.getAutocompleteConcepts(searchFor);
		//model.addAttribute("autoComplete", autoResults);
		
		// -- Autocompletehelper is used to avoid some problems -- 
		log.debug("Accessing autocomplete");
	}

}
