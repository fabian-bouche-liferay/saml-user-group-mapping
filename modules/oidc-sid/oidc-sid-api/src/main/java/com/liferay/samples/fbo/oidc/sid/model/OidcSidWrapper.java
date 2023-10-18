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

package com.liferay.samples.fbo.oidc.sid.model;

import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.wrapper.BaseModelWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link OidcSid}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see OidcSid
 * @generated
 */
public class OidcSidWrapper
	extends BaseModelWrapper<OidcSid>
	implements ModelWrapper<OidcSid>, OidcSid {

	public OidcSidWrapper(OidcSid oidcSid) {
		super(oidcSid);
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("uuid", getUuid());
		attributes.put("OidcSidId", getOidcSidId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("openIdConnectSessionId", getOpenIdConnectSessionId());
		attributes.put("sid", getSid());
		attributes.put("jwksUri", getJwksUri());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		String uuid = (String)attributes.get("uuid");

		if (uuid != null) {
			setUuid(uuid);
		}

		Long OidcSidId = (Long)attributes.get("OidcSidId");

		if (OidcSidId != null) {
			setOidcSidId(OidcSidId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		Long openIdConnectSessionId = (Long)attributes.get(
			"openIdConnectSessionId");

		if (openIdConnectSessionId != null) {
			setOpenIdConnectSessionId(openIdConnectSessionId);
		}

		String sid = (String)attributes.get("sid");

		if (sid != null) {
			setSid(sid);
		}

		String jwksUri = (String)attributes.get("jwksUri");

		if (jwksUri != null) {
			setJwksUri(jwksUri);
		}
	}

	@Override
	public OidcSid cloneWithOriginalValues() {
		return wrap(model.cloneWithOriginalValues());
	}

	/**
	 * Returns the company ID of this oidc sid.
	 *
	 * @return the company ID of this oidc sid
	 */
	@Override
	public long getCompanyId() {
		return model.getCompanyId();
	}

	/**
	 * Returns the create date of this oidc sid.
	 *
	 * @return the create date of this oidc sid
	 */
	@Override
	public Date getCreateDate() {
		return model.getCreateDate();
	}

	/**
	 * Returns the jwks uri of this oidc sid.
	 *
	 * @return the jwks uri of this oidc sid
	 */
	@Override
	public String getJwksUri() {
		return model.getJwksUri();
	}

	/**
	 * Returns the modified date of this oidc sid.
	 *
	 * @return the modified date of this oidc sid
	 */
	@Override
	public Date getModifiedDate() {
		return model.getModifiedDate();
	}

	/**
	 * Returns the oidc sid ID of this oidc sid.
	 *
	 * @return the oidc sid ID of this oidc sid
	 */
	@Override
	public long getOidcSidId() {
		return model.getOidcSidId();
	}

	/**
	 * Returns the open ID connect session ID of this oidc sid.
	 *
	 * @return the open ID connect session ID of this oidc sid
	 */
	@Override
	public long getOpenIdConnectSessionId() {
		return model.getOpenIdConnectSessionId();
	}

	/**
	 * Returns the primary key of this oidc sid.
	 *
	 * @return the primary key of this oidc sid
	 */
	@Override
	public long getPrimaryKey() {
		return model.getPrimaryKey();
	}

	/**
	 * Returns the sid of this oidc sid.
	 *
	 * @return the sid of this oidc sid
	 */
	@Override
	public String getSid() {
		return model.getSid();
	}

	/**
	 * Returns the user ID of this oidc sid.
	 *
	 * @return the user ID of this oidc sid
	 */
	@Override
	public long getUserId() {
		return model.getUserId();
	}

	/**
	 * Returns the user name of this oidc sid.
	 *
	 * @return the user name of this oidc sid
	 */
	@Override
	public String getUserName() {
		return model.getUserName();
	}

	/**
	 * Returns the user uuid of this oidc sid.
	 *
	 * @return the user uuid of this oidc sid
	 */
	@Override
	public String getUserUuid() {
		return model.getUserUuid();
	}

	/**
	 * Returns the uuid of this oidc sid.
	 *
	 * @return the uuid of this oidc sid
	 */
	@Override
	public String getUuid() {
		return model.getUuid();
	}

	@Override
	public void persist() {
		model.persist();
	}

	/**
	 * Sets the company ID of this oidc sid.
	 *
	 * @param companyId the company ID of this oidc sid
	 */
	@Override
	public void setCompanyId(long companyId) {
		model.setCompanyId(companyId);
	}

	/**
	 * Sets the create date of this oidc sid.
	 *
	 * @param createDate the create date of this oidc sid
	 */
	@Override
	public void setCreateDate(Date createDate) {
		model.setCreateDate(createDate);
	}

	/**
	 * Sets the jwks uri of this oidc sid.
	 *
	 * @param jwksUri the jwks uri of this oidc sid
	 */
	@Override
	public void setJwksUri(String jwksUri) {
		model.setJwksUri(jwksUri);
	}

	/**
	 * Sets the modified date of this oidc sid.
	 *
	 * @param modifiedDate the modified date of this oidc sid
	 */
	@Override
	public void setModifiedDate(Date modifiedDate) {
		model.setModifiedDate(modifiedDate);
	}

	/**
	 * Sets the oidc sid ID of this oidc sid.
	 *
	 * @param OidcSidId the oidc sid ID of this oidc sid
	 */
	@Override
	public void setOidcSidId(long OidcSidId) {
		model.setOidcSidId(OidcSidId);
	}

	/**
	 * Sets the open ID connect session ID of this oidc sid.
	 *
	 * @param openIdConnectSessionId the open ID connect session ID of this oidc sid
	 */
	@Override
	public void setOpenIdConnectSessionId(long openIdConnectSessionId) {
		model.setOpenIdConnectSessionId(openIdConnectSessionId);
	}

	/**
	 * Sets the primary key of this oidc sid.
	 *
	 * @param primaryKey the primary key of this oidc sid
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		model.setPrimaryKey(primaryKey);
	}

	/**
	 * Sets the sid of this oidc sid.
	 *
	 * @param sid the sid of this oidc sid
	 */
	@Override
	public void setSid(String sid) {
		model.setSid(sid);
	}

	/**
	 * Sets the user ID of this oidc sid.
	 *
	 * @param userId the user ID of this oidc sid
	 */
	@Override
	public void setUserId(long userId) {
		model.setUserId(userId);
	}

	/**
	 * Sets the user name of this oidc sid.
	 *
	 * @param userName the user name of this oidc sid
	 */
	@Override
	public void setUserName(String userName) {
		model.setUserName(userName);
	}

	/**
	 * Sets the user uuid of this oidc sid.
	 *
	 * @param userUuid the user uuid of this oidc sid
	 */
	@Override
	public void setUserUuid(String userUuid) {
		model.setUserUuid(userUuid);
	}

	/**
	 * Sets the uuid of this oidc sid.
	 *
	 * @param uuid the uuid of this oidc sid
	 */
	@Override
	public void setUuid(String uuid) {
		model.setUuid(uuid);
	}

	@Override
	public String toXmlString() {
		return model.toXmlString();
	}

	@Override
	public StagedModelType getStagedModelType() {
		return model.getStagedModelType();
	}

	@Override
	protected OidcSidWrapper wrap(OidcSid oidcSid) {
		return new OidcSidWrapper(oidcSid);
	}

}