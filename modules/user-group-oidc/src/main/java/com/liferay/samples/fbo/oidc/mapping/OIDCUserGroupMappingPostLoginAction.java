package com.liferay.samples.fbo.oidc.mapping;

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.LifecycleAction;
import com.liferay.portal.kernel.events.LifecycleEvent;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.service.UserGroupLocalService;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.security.sso.openid.connect.constants.OpenIdConnectWebKeys;
import com.liferay.portal.security.sso.openid.connect.persistence.model.OpenIdConnectSession;
import com.liferay.portal.security.sso.openid.connect.persistence.service.OpenIdConnectSessionLocalService;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import net.minidev.json.JSONArray;

@Component(
		immediate = true, property = "key=login.events.post",
		service = LifecycleAction.class
	)
public class OIDCUserGroupMappingPostLoginAction implements LifecycleAction {

	@Override
	public void processLifecycleEvent(LifecycleEvent lifecycleEvent) throws ActionException {
		
		HttpServletRequest httpServletRequest = lifecycleEvent.getRequest();
		HttpSession httpSession = httpServletRequest.getSession();
		Long openIdConnectSessionId = (Long)httpSession.getAttribute(
			OpenIdConnectWebKeys.OPEN_ID_CONNECT_SESSION_ID);
		
		long userId = _portal.getUserId(httpServletRequest);
		long companyId = _portal.getCompanyId(httpServletRequest);

		if(openIdConnectSessionId != null) {

			try {
				OpenIdConnectSession openIdConnectSession = _openIdConnectSessionLocalService.getOpenIdConnectSession(openIdConnectSessionId);
				String idToken = openIdConnectSession.getIdToken();
				
	            JWT jwt = JWTParser.parse(idToken);
	            JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
	            JSONArray groupClaimsArray = (JSONArray) claimsSet.getClaim("member");
	            groupClaimsArray.forEach(groupClaim -> {
					String userGroupName = groupClaim.toString().replace("/", "");
					UserGroup userGroup;
					try {
						userGroup = _userGroupLocalService.getUserGroup(companyId, userGroupName);
						_userGroupLocalService.addUserUserGroup(userId, userGroup.getUserGroupId());
					} catch (PortalException e) {
						if(_log.isErrorEnabled()) {
							_log.error("Failed to find user group " + userGroupName, e);
						}
					}
	            });

			} catch (PortalException e) {
				_log.error("Failed to get openid connect session", e);
	        } catch (ParseException e) {
				_log.error("Failed to parse id token", e);
			}				

		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
			OIDCUserGroupMappingPostLoginAction.class);
	
	@Reference
	private Portal _portal;
	
	@Reference
	private OpenIdConnectSessionLocalService _openIdConnectSessionLocalService;

	@Reference
	private UserGroupLocalService _userGroupLocalService;
}
