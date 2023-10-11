package com.liferay.samples.fbo.saml.mapping;

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.LifecycleAction;
import com.liferay.portal.kernel.events.LifecycleEvent;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.service.UserGroupLocalService;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.kernel.xml.XPath;
import com.liferay.saml.persistence.model.SamlSpSession;
import com.liferay.saml.runtime.servlet.profile.WebSsoProfile;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		immediate = true, property = "key=login.events.post",
		service = LifecycleAction.class
	)
public class SAMLUserGroupMappingPostLoginAction implements LifecycleAction {

	private static final String USER_GROUP_ATTRIBUTE_NAME = "member";

	@Override
	public void processLifecycleEvent(LifecycleEvent lifecycleEvent) throws ActionException {

		SamlSpSession samlSpSession = _webSsoProfile.getSamlSpSession(lifecycleEvent.getRequest());
		
		if(samlSpSession != null) {

			HttpServletRequest httpServletRequest = lifecycleEvent.getRequest();
			long userId = _portal.getUserId(httpServletRequest);
			long companyId = _portal.getCompanyId(httpServletRequest);

			_userGroupLocalService.getUserUserGroups(userId).forEach(userGroup -> {
				_userGroupLocalService.deleteUserUserGroup(userId, userGroup.getUserGroupId());
			});
			
			try {
				Document assertion = SAXReaderUtil.read(samlSpSession.getAssertionXml());
				
				XPath xPath = SAXReaderUtil.createXPath("//*[local-name()='Attribute' and @Name='"
						+ USER_GROUP_ATTRIBUTE_NAME
						+ "' and namespace-uri()='urn:oasis:names:tc:SAML:2.0:assertion']/*[local-name()='AttributeValue' and namespace-uri()='urn:oasis:names:tc:SAML:2.0:assertion']");
	
				List<Node> userGroupAttributeValueList = xPath.selectNodes(assertion);
				
				userGroupAttributeValueList.forEach(userGroupAttributeValue -> {
					String userGroupName = userGroupAttributeValue.getText().replace("/", "");
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
				
			} catch (DocumentException e) {
				if(_log.isErrorEnabled()) {
					_log.error("Failed to read assertion XML", e);
				}
			}						

		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
			SAMLUserGroupMappingPostLoginAction.class);
	
	@Reference
	private WebSsoProfile _webSsoProfile;
	
	@Reference
	private Portal _portal;

	@Reference
	private UserGroupLocalService _userGroupLocalService;

}
