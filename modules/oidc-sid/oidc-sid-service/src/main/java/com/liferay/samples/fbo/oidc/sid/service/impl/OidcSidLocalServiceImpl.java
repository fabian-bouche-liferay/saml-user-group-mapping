/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.samples.fbo.oidc.sid.service.impl;

import com.liferay.portal.aop.AopService;
import com.liferay.samples.fbo.oidc.sid.exception.NoSuchOidcSidException;
import com.liferay.samples.fbo.oidc.sid.model.OidcSid;
import com.liferay.samples.fbo.oidc.sid.service.base.OidcSidLocalServiceBaseImpl;

import org.osgi.service.component.annotations.Component;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	property = "model.class.name=com.liferay.samples.fbo.oidc.sid.model.OidcSid",
	service = AopService.class
)
public class OidcSidLocalServiceImpl extends OidcSidLocalServiceBaseImpl {
	
	public OidcSid getBySid(String sid) throws NoSuchOidcSidException {
		return oidcSidPersistence.findBySid(sid);
	}
	
	public OidcSid getByOpenIdConnectSessionId(long openIdConnectSessionId) throws NoSuchOidcSidException {
		return oidcSidPersistence.findByOpenIdConnectSessionId(openIdConnectSessionId);
	}
	
}