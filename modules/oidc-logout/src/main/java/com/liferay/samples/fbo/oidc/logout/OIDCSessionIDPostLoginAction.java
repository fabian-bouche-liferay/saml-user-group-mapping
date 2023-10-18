package com.liferay.samples.fbo.oidc.logout;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.LifecycleAction;
import com.liferay.portal.kernel.events.LifecycleEvent;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.security.sso.openid.connect.constants.OpenIdConnectWebKeys;
import com.liferay.portal.security.sso.openid.connect.persistence.model.OpenIdConnectSession;
import com.liferay.portal.security.sso.openid.connect.persistence.service.OpenIdConnectSessionLocalService;
import com.liferay.samples.fbo.oidc.sid.model.OidcSid;
import com.liferay.samples.fbo.oidc.sid.service.OidcSidLocalService;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		immediate = true, property = "key=login.events.post",
		service = LifecycleAction.class
	)
public class OIDCSessionIDPostLoginAction implements LifecycleAction {

	@Override
	public void processLifecycleEvent(LifecycleEvent lifecycleEvent) throws ActionException {
		
		HttpServletRequest httpServletRequest = lifecycleEvent.getRequest();
		HttpSession httpSession = httpServletRequest.getSession();
		Long openIdConnectSessionId = (Long)httpSession.getAttribute(
			OpenIdConnectWebKeys.OPEN_ID_CONNECT_SESSION_ID);
		
		long userId = _portal.getUserId(httpServletRequest);
		long companyId = _portal.getCompanyId(httpServletRequest);

		if(openIdConnectSessionId != null) {
			OpenIdConnectSession openIdConnectSession;
			
			try {
				openIdConnectSession = _openIdConnectSessionLocalService.getOpenIdConnectSession(openIdConnectSessionId);
				String idToken = openIdConnectSession.getIdToken();
				
				System.out.println("ID TOKEN: " + idToken);
				
				JWT jwt = JWTParser.parse(idToken);
				JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
				String sid = (String) claimsSet.getClaim("sid");
				
				System.out.println("SID: " + sid);
				
				OidcSid oidcSid = _oidcSidLocalService.createOidcSid(_counterLocalService.increment(OidcSid.class.getName()));
				oidcSid.setCompanyId(companyId);
				oidcSid.setUserId(userId);
				oidcSid.setOpenIdConnectSessionId(openIdConnectSessionId);
				oidcSid.setSid(sid);
				oidcSid.setJwksUri("");
				_oidcSidLocalService.addOidcSid(oidcSid);

			} catch (PortalException e) {
				_log.error("Failed to get openid connect session", e);
	        } catch (ParseException e) {
				_log.error("Failed to parse id token", e);
			}		

		}
	}
	
	private static final Log _log = LogFactoryUtil.getLog(
			OIDCSessionIDPostLoginAction.class);
	
	@Reference
	private Portal _portal;
	
	@Reference
	private OpenIdConnectSessionLocalService _openIdConnectSessionLocalService;
	
	@Reference
	private OidcSidLocalService _oidcSidLocalService;
	
	@Reference
	private CounterLocalService _counterLocalService;
}
