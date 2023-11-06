package com.liferay.samples.fbo.oidc.redirect.configuration;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

import aQute.bnd.annotation.metatype.Meta;

@ExtendedObjectClassDefinition(
		category = "sso",
		scope = ExtendedObjectClassDefinition.Scope.COMPANY, strictScope = true
	)
@Meta.OCD(
	id = "com.liferay.samples.fbo.oidc.redirect.configuration.OIDCLoginRedirectConfiguration",
	localization = "content/Language",
	name = "oidc-login-redirect-configuration"
)
public interface OIDCLoginRedirectConfiguration {

	@Meta.AD(deflt = "false", required = false)
	public boolean enabled();
	
	@Meta.AD(deflt = "", required = false)
	public String openIdConnectProviderName();

	@Meta.AD(deflt = "", required = false)
	public String urlPattern();

}
