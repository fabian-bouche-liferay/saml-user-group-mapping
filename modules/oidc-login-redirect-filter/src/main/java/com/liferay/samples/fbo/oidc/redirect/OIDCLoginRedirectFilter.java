package com.liferay.samples.fbo.oidc.redirect;

import com.liferay.portal.configuration.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.servlet.BaseFilter;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.security.sso.openid.connect.OpenIdConnectAuthenticationHandler;
import com.liferay.samples.fbo.oidc.redirect.configuration.OIDCLoginRedirectConfiguration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		configurationPid = "com.liferay.samples.fbo.oidc.redirect.configuration.OIDCLoginRedirectConfiguration",
		property = {
			"after-filter=Monitoring Filter", "servlet-context-name=",
			"servlet-filter-name=SSO OpenId Connect Login Redirect Filter",
			"url-pattern=/*"
		},
		service = Filter.class
	)
public class OIDCLoginRedirectFilter extends BaseFilter {

	@Override
	protected void processFilter(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			FilterChain filterChain) throws Exception {

		String uri = httpServletRequest.getRequestURI();
		
		if(!uri.startsWith("/c/portal")) {

			HttpSession httpSession = httpServletRequest.getSession(false);
			
			long companyId = CompanyThreadLocal.getCompanyId();
			
			Long userId = (Long)httpSession.getAttribute(WebKeys.USER_ID);

			User guestUser = _userLocalService.getGuestUser(companyId);

			OIDCLoginRedirectConfiguration oidcLoginRedirectConfiguration =
					_configurationProvider.getCompanyConfiguration(
							OIDCLoginRedirectConfiguration.class, companyId);
			
			if(oidcLoginRedirectConfiguration.enabled() && (userId == null || guestUser.getUserId() == userId)) {

				Pattern pattern = Pattern.compile(oidcLoginRedirectConfiguration.urlPattern());
				
				Matcher matcher = pattern.matcher(uri);
				
				if(matcher.find()) {
					
					_openIdConnectAuthenticationHandler.requestAuthentication(
							oidcLoginRedirectConfiguration.openIdConnectProviderName(),
							httpServletRequest,
							httpServletResponse);

					return;
				}
				
			}

		}
		
		super.processFilter(httpServletRequest, httpServletResponse, filterChain);

	}

	@Reference
	private UserLocalService _userLocalService;

	@Reference
	private OpenIdConnectAuthenticationHandler
		_openIdConnectAuthenticationHandler;
	
	@Reference
	private ConfigurationProvider _configurationProvider;
	
	@Override
	protected Log getLog() {
		return _log;
	}
	
	private static final Log _log = LogFactoryUtil.getLog(
			OIDCLoginRedirectFilter.class);
	
	@Reference
	private Portal _portal;
}
