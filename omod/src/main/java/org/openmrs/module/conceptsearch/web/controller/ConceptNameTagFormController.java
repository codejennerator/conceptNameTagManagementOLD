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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.ConceptNameTag;
import org.openmrs.api.ConceptService;
import org.openmrs.module.conceptsearch.ConceptSearchService;
import org.openmrs.api.context.Context;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.web.WebConstants;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class ConceptNameTagFormController extends SimpleFormController {
	
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * Allows for Integers to be used as values in input tags. Normally, only strings and lists are
	 * expected
	 * 
	 * @see org.springframework.web.servlet.mvc.BaseCommandController#initBinder(javax.servlet.http.HttpServletRequest,
	 *      org.springframework.web.bind.ServletRequestDataBinder)
	 */
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request, binder);
		//NumberFormat nf = NumberFormat.getInstance(new Locale("en_US"));
		binder.registerCustomEditor(java.lang.Integer.class, new CustomNumberEditor(java.lang.Integer.class, true));
	}
	
	/**
	 * The onSubmit function receives the form/command object that was modified by the input form
	 * and saves it to the db
	 * 
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object obj,
	        BindException errors) throws Exception {
		
		HttpSession httpSession = request.getSession();
		String view = getFormView();
		
		if (Context.isAuthenticated()) {
			ConceptNameTag nameTag = (ConceptNameTag) obj;
			ConceptService conceptService = Context.getConceptService();
			ConceptSearchService css = (ConceptSearchService) Context.getService(ConceptSearchService.class);
			if (request.getParameter("retireNameTag") != null) {
				String retireReason = request.getParameter("retireReason");
				if (nameTag.getId() != null && (retireReason == null || retireReason.length() == 0)) {
					errors.reject("retireReason", "ConceptNameTag.retire.reason.empty");
					return showForm(request, response, errors);
				}
				nameTag.setVoided(true);
				nameTag.setVoidReason(retireReason);
				nameTag.setVoidedBy(Context.getAuthenticatedUser());
				nameTag.setDateVoided(new Date());
				css.retireNameTag(nameTag, retireReason);
				httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "ConceptNameTag.retiredSuccessfully");
			}

			// if this obs is already voided and needs to be unvoided
			else if (request.getParameter("unretireNameTag") != null) {
				nameTag.setVoided(false);
				nameTag.setVoidedBy(Context.getAuthenticatedUser());
				nameTag.setDateVoided(new Date());
				css.unretireNameTag(nameTag);
				httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "ConceptNameTag.unretiredSuccessfully");
			} else {
				Context.getConceptService().saveConceptNameTag(nameTag);
				httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "ConceptNameTag.saved");
			}
			
			view = getSuccessView();
			
		}
		
		return new ModelAndView(new RedirectView(view));
	}
	
	/**
	 * This is called prior to displaying a form for the first time. It tells Spring the
	 * form/command object to load into the request
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		
		ConceptNameTag conceptNameTag = null;
		
		if (Context.isAuthenticated()) {
			ConceptService cs = Context.getConceptService();
			String conceptNameTagId = request.getParameter("conceptNameTagId");
			if (conceptNameTagId != null)
				conceptNameTag = cs.getConceptNameTag(Integer.valueOf(conceptNameTagId));
		}
		
		if (conceptNameTag == null)
			conceptNameTag = new ConceptNameTag();
		
		return conceptNameTag;
	}
}
	