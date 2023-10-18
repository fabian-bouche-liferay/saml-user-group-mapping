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

package com.liferay.samples.fbo.oidc.sid.service.persistence.impl;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.configuration.Configuration;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.SessionFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUID;
import com.liferay.samples.fbo.oidc.sid.exception.NoSuchOidcSidException;
import com.liferay.samples.fbo.oidc.sid.model.OidcSid;
import com.liferay.samples.fbo.oidc.sid.model.OidcSidTable;
import com.liferay.samples.fbo.oidc.sid.model.impl.OidcSidImpl;
import com.liferay.samples.fbo.oidc.sid.model.impl.OidcSidModelImpl;
import com.liferay.samples.fbo.oidc.sid.service.persistence.OidcSidPersistence;
import com.liferay.samples.fbo.oidc.sid.service.persistence.OidcSidUtil;
import com.liferay.samples.fbo.oidc.sid.service.persistence.impl.constants.OIDCSIDPersistenceConstants;

import java.io.Serializable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * The persistence implementation for the oidc sid service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(service = OidcSidPersistence.class)
public class OidcSidPersistenceImpl
	extends BasePersistenceImpl<OidcSid> implements OidcSidPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>OidcSidUtil</code> to access the oidc sid persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		OidcSidImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathWithPaginationFindByUuid;
	private FinderPath _finderPathWithoutPaginationFindByUuid;
	private FinderPath _finderPathCountByUuid;

	/**
	 * Returns all the oidc sids where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching oidc sids
	 */
	@Override
	public List<OidcSid> findByUuid(String uuid) {
		return findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the oidc sids where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OidcSidModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of oidc sids
	 * @param end the upper bound of the range of oidc sids (not inclusive)
	 * @return the range of matching oidc sids
	 */
	@Override
	public List<OidcSid> findByUuid(String uuid, int start, int end) {
		return findByUuid(uuid, start, end, null);
	}

	/**
	 * Returns an ordered range of all the oidc sids where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OidcSidModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of oidc sids
	 * @param end the upper bound of the range of oidc sids (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching oidc sids
	 */
	@Override
	public List<OidcSid> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<OidcSid> orderByComparator) {

		return findByUuid(uuid, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the oidc sids where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OidcSidModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of oidc sids
	 * @param end the upper bound of the range of oidc sids (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching oidc sids
	 */
	@Override
	public List<OidcSid> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<OidcSid> orderByComparator, boolean useFinderCache) {

		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByUuid;
				finderArgs = new Object[] {uuid};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByUuid;
			finderArgs = new Object[] {uuid, start, end, orderByComparator};
		}

		List<OidcSid> list = null;

		if (useFinderCache) {
			list = (List<OidcSid>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (OidcSid oidcSid : list) {
					if (!uuid.equals(oidcSid.getUuid())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_OIDCSID_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_UUID_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(OidcSidModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				list = (List<OidcSid>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first oidc sid in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching oidc sid
	 * @throws NoSuchOidcSidException if a matching oidc sid could not be found
	 */
	@Override
	public OidcSid findByUuid_First(
			String uuid, OrderByComparator<OidcSid> orderByComparator)
		throws NoSuchOidcSidException {

		OidcSid oidcSid = fetchByUuid_First(uuid, orderByComparator);

		if (oidcSid != null) {
			return oidcSid;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append("}");

		throw new NoSuchOidcSidException(sb.toString());
	}

	/**
	 * Returns the first oidc sid in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching oidc sid, or <code>null</code> if a matching oidc sid could not be found
	 */
	@Override
	public OidcSid fetchByUuid_First(
		String uuid, OrderByComparator<OidcSid> orderByComparator) {

		List<OidcSid> list = findByUuid(uuid, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last oidc sid in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching oidc sid
	 * @throws NoSuchOidcSidException if a matching oidc sid could not be found
	 */
	@Override
	public OidcSid findByUuid_Last(
			String uuid, OrderByComparator<OidcSid> orderByComparator)
		throws NoSuchOidcSidException {

		OidcSid oidcSid = fetchByUuid_Last(uuid, orderByComparator);

		if (oidcSid != null) {
			return oidcSid;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append("}");

		throw new NoSuchOidcSidException(sb.toString());
	}

	/**
	 * Returns the last oidc sid in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching oidc sid, or <code>null</code> if a matching oidc sid could not be found
	 */
	@Override
	public OidcSid fetchByUuid_Last(
		String uuid, OrderByComparator<OidcSid> orderByComparator) {

		int count = countByUuid(uuid);

		if (count == 0) {
			return null;
		}

		List<OidcSid> list = findByUuid(
			uuid, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the oidc sids before and after the current oidc sid in the ordered set where uuid = &#63;.
	 *
	 * @param OidcSidId the primary key of the current oidc sid
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next oidc sid
	 * @throws NoSuchOidcSidException if a oidc sid with the primary key could not be found
	 */
	@Override
	public OidcSid[] findByUuid_PrevAndNext(
			long OidcSidId, String uuid,
			OrderByComparator<OidcSid> orderByComparator)
		throws NoSuchOidcSidException {

		uuid = Objects.toString(uuid, "");

		OidcSid oidcSid = findByPrimaryKey(OidcSidId);

		Session session = null;

		try {
			session = openSession();

			OidcSid[] array = new OidcSidImpl[3];

			array[0] = getByUuid_PrevAndNext(
				session, oidcSid, uuid, orderByComparator, true);

			array[1] = oidcSid;

			array[2] = getByUuid_PrevAndNext(
				session, oidcSid, uuid, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected OidcSid getByUuid_PrevAndNext(
		Session session, OidcSid oidcSid, String uuid,
		OrderByComparator<OidcSid> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_OIDCSID_WHERE);

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_UUID_3);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_UUID_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(OidcSidModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindUuid) {
			queryPos.add(uuid);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(oidcSid)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<OidcSid> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the oidc sids where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	@Override
	public void removeByUuid(String uuid) {
		for (OidcSid oidcSid :
				findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(oidcSid);
		}
	}

	/**
	 * Returns the number of oidc sids where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching oidc sids
	 */
	@Override
	public int countByUuid(String uuid) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUuid;

		Object[] finderArgs = new Object[] {uuid};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_OIDCSID_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_UUID_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_UUID_UUID_2 = "oidcSid.uuid = ?";

	private static final String _FINDER_COLUMN_UUID_UUID_3 =
		"(oidcSid.uuid IS NULL OR oidcSid.uuid = '')";

	private FinderPath _finderPathWithPaginationFindByUuid_C;
	private FinderPath _finderPathWithoutPaginationFindByUuid_C;
	private FinderPath _finderPathCountByUuid_C;

	/**
	 * Returns all the oidc sids where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching oidc sids
	 */
	@Override
	public List<OidcSid> findByUuid_C(String uuid, long companyId) {
		return findByUuid_C(
			uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the oidc sids where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OidcSidModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of oidc sids
	 * @param end the upper bound of the range of oidc sids (not inclusive)
	 * @return the range of matching oidc sids
	 */
	@Override
	public List<OidcSid> findByUuid_C(
		String uuid, long companyId, int start, int end) {

		return findByUuid_C(uuid, companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the oidc sids where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OidcSidModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of oidc sids
	 * @param end the upper bound of the range of oidc sids (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching oidc sids
	 */
	@Override
	public List<OidcSid> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<OidcSid> orderByComparator) {

		return findByUuid_C(
			uuid, companyId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the oidc sids where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OidcSidModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of oidc sids
	 * @param end the upper bound of the range of oidc sids (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching oidc sids
	 */
	@Override
	public List<OidcSid> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<OidcSid> orderByComparator, boolean useFinderCache) {

		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByUuid_C;
				finderArgs = new Object[] {uuid, companyId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByUuid_C;
			finderArgs = new Object[] {
				uuid, companyId, start, end, orderByComparator
			};
		}

		List<OidcSid> list = null;

		if (useFinderCache) {
			list = (List<OidcSid>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (OidcSid oidcSid : list) {
					if (!uuid.equals(oidcSid.getUuid()) ||
						(companyId != oidcSid.getCompanyId())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					4 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(4);
			}

			sb.append(_SQL_SELECT_OIDCSID_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_C_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_C_UUID_2);
			}

			sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(OidcSidModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				queryPos.add(companyId);

				list = (List<OidcSid>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first oidc sid in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching oidc sid
	 * @throws NoSuchOidcSidException if a matching oidc sid could not be found
	 */
	@Override
	public OidcSid findByUuid_C_First(
			String uuid, long companyId,
			OrderByComparator<OidcSid> orderByComparator)
		throws NoSuchOidcSidException {

		OidcSid oidcSid = fetchByUuid_C_First(
			uuid, companyId, orderByComparator);

		if (oidcSid != null) {
			return oidcSid;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchOidcSidException(sb.toString());
	}

	/**
	 * Returns the first oidc sid in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching oidc sid, or <code>null</code> if a matching oidc sid could not be found
	 */
	@Override
	public OidcSid fetchByUuid_C_First(
		String uuid, long companyId,
		OrderByComparator<OidcSid> orderByComparator) {

		List<OidcSid> list = findByUuid_C(
			uuid, companyId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last oidc sid in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching oidc sid
	 * @throws NoSuchOidcSidException if a matching oidc sid could not be found
	 */
	@Override
	public OidcSid findByUuid_C_Last(
			String uuid, long companyId,
			OrderByComparator<OidcSid> orderByComparator)
		throws NoSuchOidcSidException {

		OidcSid oidcSid = fetchByUuid_C_Last(
			uuid, companyId, orderByComparator);

		if (oidcSid != null) {
			return oidcSid;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchOidcSidException(sb.toString());
	}

	/**
	 * Returns the last oidc sid in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching oidc sid, or <code>null</code> if a matching oidc sid could not be found
	 */
	@Override
	public OidcSid fetchByUuid_C_Last(
		String uuid, long companyId,
		OrderByComparator<OidcSid> orderByComparator) {

		int count = countByUuid_C(uuid, companyId);

		if (count == 0) {
			return null;
		}

		List<OidcSid> list = findByUuid_C(
			uuid, companyId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the oidc sids before and after the current oidc sid in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param OidcSidId the primary key of the current oidc sid
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next oidc sid
	 * @throws NoSuchOidcSidException if a oidc sid with the primary key could not be found
	 */
	@Override
	public OidcSid[] findByUuid_C_PrevAndNext(
			long OidcSidId, String uuid, long companyId,
			OrderByComparator<OidcSid> orderByComparator)
		throws NoSuchOidcSidException {

		uuid = Objects.toString(uuid, "");

		OidcSid oidcSid = findByPrimaryKey(OidcSidId);

		Session session = null;

		try {
			session = openSession();

			OidcSid[] array = new OidcSidImpl[3];

			array[0] = getByUuid_C_PrevAndNext(
				session, oidcSid, uuid, companyId, orderByComparator, true);

			array[1] = oidcSid;

			array[2] = getByUuid_C_PrevAndNext(
				session, oidcSid, uuid, companyId, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected OidcSid getByUuid_C_PrevAndNext(
		Session session, OidcSid oidcSid, String uuid, long companyId,
		OrderByComparator<OidcSid> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		sb.append(_SQL_SELECT_OIDCSID_WHERE);

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_C_UUID_3);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_C_UUID_2);
		}

		sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(OidcSidModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindUuid) {
			queryPos.add(uuid);
		}

		queryPos.add(companyId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(oidcSid)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<OidcSid> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the oidc sids where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	@Override
	public void removeByUuid_C(String uuid, long companyId) {
		for (OidcSid oidcSid :
				findByUuid_C(
					uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(oidcSid);
		}
	}

	/**
	 * Returns the number of oidc sids where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching oidc sids
	 */
	@Override
	public int countByUuid_C(String uuid, long companyId) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUuid_C;

		Object[] finderArgs = new Object[] {uuid, companyId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_OIDCSID_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_C_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_C_UUID_2);
			}

			sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				queryPos.add(companyId);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_UUID_C_UUID_2 =
		"oidcSid.uuid = ? AND ";

	private static final String _FINDER_COLUMN_UUID_C_UUID_3 =
		"(oidcSid.uuid IS NULL OR oidcSid.uuid = '') AND ";

	private static final String _FINDER_COLUMN_UUID_C_COMPANYID_2 =
		"oidcSid.companyId = ?";

	private FinderPath _finderPathFetchBySid;
	private FinderPath _finderPathCountBySid;

	/**
	 * Returns the oidc sid where sid = &#63; or throws a <code>NoSuchOidcSidException</code> if it could not be found.
	 *
	 * @param sid the sid
	 * @return the matching oidc sid
	 * @throws NoSuchOidcSidException if a matching oidc sid could not be found
	 */
	@Override
	public OidcSid findBySid(String sid) throws NoSuchOidcSidException {
		OidcSid oidcSid = fetchBySid(sid);

		if (oidcSid == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("sid=");
			sb.append(sid);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchOidcSidException(sb.toString());
		}

		return oidcSid;
	}

	/**
	 * Returns the oidc sid where sid = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param sid the sid
	 * @return the matching oidc sid, or <code>null</code> if a matching oidc sid could not be found
	 */
	@Override
	public OidcSid fetchBySid(String sid) {
		return fetchBySid(sid, true);
	}

	/**
	 * Returns the oidc sid where sid = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param sid the sid
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching oidc sid, or <code>null</code> if a matching oidc sid could not be found
	 */
	@Override
	public OidcSid fetchBySid(String sid, boolean useFinderCache) {
		sid = Objects.toString(sid, "");

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {sid};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(
				_finderPathFetchBySid, finderArgs, this);
		}

		if (result instanceof OidcSid) {
			OidcSid oidcSid = (OidcSid)result;

			if (!Objects.equals(sid, oidcSid.getSid())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_SELECT_OIDCSID_WHERE);

			boolean bindSid = false;

			if (sid.isEmpty()) {
				sb.append(_FINDER_COLUMN_SID_SID_3);
			}
			else {
				bindSid = true;

				sb.append(_FINDER_COLUMN_SID_SID_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindSid) {
					queryPos.add(sid);
				}

				List<OidcSid> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchBySid, finderArgs, list);
					}
				}
				else {
					if (list.size() > 1) {
						Collections.sort(list, Collections.reverseOrder());

						if (_log.isWarnEnabled()) {
							if (!useFinderCache) {
								finderArgs = new Object[] {sid};
							}

							_log.warn(
								"OidcSidPersistenceImpl.fetchBySid(String, boolean) with parameters (" +
									StringUtil.merge(finderArgs) +
										") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
						}
					}

					OidcSid oidcSid = list.get(0);

					result = oidcSid;

					cacheResult(oidcSid);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (OidcSid)result;
		}
	}

	/**
	 * Removes the oidc sid where sid = &#63; from the database.
	 *
	 * @param sid the sid
	 * @return the oidc sid that was removed
	 */
	@Override
	public OidcSid removeBySid(String sid) throws NoSuchOidcSidException {
		OidcSid oidcSid = findBySid(sid);

		return remove(oidcSid);
	}

	/**
	 * Returns the number of oidc sids where sid = &#63;.
	 *
	 * @param sid the sid
	 * @return the number of matching oidc sids
	 */
	@Override
	public int countBySid(String sid) {
		sid = Objects.toString(sid, "");

		FinderPath finderPath = _finderPathCountBySid;

		Object[] finderArgs = new Object[] {sid};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_OIDCSID_WHERE);

			boolean bindSid = false;

			if (sid.isEmpty()) {
				sb.append(_FINDER_COLUMN_SID_SID_3);
			}
			else {
				bindSid = true;

				sb.append(_FINDER_COLUMN_SID_SID_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindSid) {
					queryPos.add(sid);
				}

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_SID_SID_2 = "oidcSid.sid = ?";

	private static final String _FINDER_COLUMN_SID_SID_3 =
		"(oidcSid.sid IS NULL OR oidcSid.sid = '')";

	private FinderPath _finderPathFetchByOpenIdConnectSessionId;
	private FinderPath _finderPathCountByOpenIdConnectSessionId;

	/**
	 * Returns the oidc sid where openIdConnectSessionId = &#63; or throws a <code>NoSuchOidcSidException</code> if it could not be found.
	 *
	 * @param openIdConnectSessionId the open ID connect session ID
	 * @return the matching oidc sid
	 * @throws NoSuchOidcSidException if a matching oidc sid could not be found
	 */
	@Override
	public OidcSid findByOpenIdConnectSessionId(long openIdConnectSessionId)
		throws NoSuchOidcSidException {

		OidcSid oidcSid = fetchByOpenIdConnectSessionId(openIdConnectSessionId);

		if (oidcSid == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("openIdConnectSessionId=");
			sb.append(openIdConnectSessionId);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchOidcSidException(sb.toString());
		}

		return oidcSid;
	}

	/**
	 * Returns the oidc sid where openIdConnectSessionId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param openIdConnectSessionId the open ID connect session ID
	 * @return the matching oidc sid, or <code>null</code> if a matching oidc sid could not be found
	 */
	@Override
	public OidcSid fetchByOpenIdConnectSessionId(long openIdConnectSessionId) {
		return fetchByOpenIdConnectSessionId(openIdConnectSessionId, true);
	}

	/**
	 * Returns the oidc sid where openIdConnectSessionId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param openIdConnectSessionId the open ID connect session ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching oidc sid, or <code>null</code> if a matching oidc sid could not be found
	 */
	@Override
	public OidcSid fetchByOpenIdConnectSessionId(
		long openIdConnectSessionId, boolean useFinderCache) {

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {openIdConnectSessionId};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(
				_finderPathFetchByOpenIdConnectSessionId, finderArgs, this);
		}

		if (result instanceof OidcSid) {
			OidcSid oidcSid = (OidcSid)result;

			if (openIdConnectSessionId != oidcSid.getOpenIdConnectSessionId()) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_SELECT_OIDCSID_WHERE);

			sb.append(
				_FINDER_COLUMN_OPENIDCONNECTSESSIONID_OPENIDCONNECTSESSIONID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(openIdConnectSessionId);

				List<OidcSid> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchByOpenIdConnectSessionId,
							finderArgs, list);
					}
				}
				else {
					if (list.size() > 1) {
						Collections.sort(list, Collections.reverseOrder());

						if (_log.isWarnEnabled()) {
							if (!useFinderCache) {
								finderArgs = new Object[] {
									openIdConnectSessionId
								};
							}

							_log.warn(
								"OidcSidPersistenceImpl.fetchByOpenIdConnectSessionId(long, boolean) with parameters (" +
									StringUtil.merge(finderArgs) +
										") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
						}
					}

					OidcSid oidcSid = list.get(0);

					result = oidcSid;

					cacheResult(oidcSid);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (OidcSid)result;
		}
	}

	/**
	 * Removes the oidc sid where openIdConnectSessionId = &#63; from the database.
	 *
	 * @param openIdConnectSessionId the open ID connect session ID
	 * @return the oidc sid that was removed
	 */
	@Override
	public OidcSid removeByOpenIdConnectSessionId(long openIdConnectSessionId)
		throws NoSuchOidcSidException {

		OidcSid oidcSid = findByOpenIdConnectSessionId(openIdConnectSessionId);

		return remove(oidcSid);
	}

	/**
	 * Returns the number of oidc sids where openIdConnectSessionId = &#63;.
	 *
	 * @param openIdConnectSessionId the open ID connect session ID
	 * @return the number of matching oidc sids
	 */
	@Override
	public int countByOpenIdConnectSessionId(long openIdConnectSessionId) {
		FinderPath finderPath = _finderPathCountByOpenIdConnectSessionId;

		Object[] finderArgs = new Object[] {openIdConnectSessionId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_OIDCSID_WHERE);

			sb.append(
				_FINDER_COLUMN_OPENIDCONNECTSESSIONID_OPENIDCONNECTSESSIONID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(openIdConnectSessionId);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String
		_FINDER_COLUMN_OPENIDCONNECTSESSIONID_OPENIDCONNECTSESSIONID_2 =
			"oidcSid.openIdConnectSessionId = ?";

	public OidcSidPersistenceImpl() {
		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("uuid", "uuid_");

		setDBColumnNames(dbColumnNames);

		setModelClass(OidcSid.class);

		setModelImplClass(OidcSidImpl.class);
		setModelPKClass(long.class);

		setTable(OidcSidTable.INSTANCE);
	}

	/**
	 * Caches the oidc sid in the entity cache if it is enabled.
	 *
	 * @param oidcSid the oidc sid
	 */
	@Override
	public void cacheResult(OidcSid oidcSid) {
		entityCache.putResult(
			OidcSidImpl.class, oidcSid.getPrimaryKey(), oidcSid);

		finderCache.putResult(
			_finderPathFetchBySid, new Object[] {oidcSid.getSid()}, oidcSid);

		finderCache.putResult(
			_finderPathFetchByOpenIdConnectSessionId,
			new Object[] {oidcSid.getOpenIdConnectSessionId()}, oidcSid);
	}

	private int _valueObjectFinderCacheListThreshold;

	/**
	 * Caches the oidc sids in the entity cache if it is enabled.
	 *
	 * @param oidcSids the oidc sids
	 */
	@Override
	public void cacheResult(List<OidcSid> oidcSids) {
		if ((_valueObjectFinderCacheListThreshold == 0) ||
			((_valueObjectFinderCacheListThreshold > 0) &&
			 (oidcSids.size() > _valueObjectFinderCacheListThreshold))) {

			return;
		}

		for (OidcSid oidcSid : oidcSids) {
			if (entityCache.getResult(
					OidcSidImpl.class, oidcSid.getPrimaryKey()) == null) {

				cacheResult(oidcSid);
			}
		}
	}

	/**
	 * Clears the cache for all oidc sids.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(OidcSidImpl.class);

		finderCache.clearCache(OidcSidImpl.class);
	}

	/**
	 * Clears the cache for the oidc sid.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(OidcSid oidcSid) {
		entityCache.removeResult(OidcSidImpl.class, oidcSid);
	}

	@Override
	public void clearCache(List<OidcSid> oidcSids) {
		for (OidcSid oidcSid : oidcSids) {
			entityCache.removeResult(OidcSidImpl.class, oidcSid);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(OidcSidImpl.class);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(OidcSidImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(OidcSidModelImpl oidcSidModelImpl) {
		Object[] args = new Object[] {oidcSidModelImpl.getSid()};

		finderCache.putResult(_finderPathCountBySid, args, Long.valueOf(1));
		finderCache.putResult(_finderPathFetchBySid, args, oidcSidModelImpl);

		args = new Object[] {oidcSidModelImpl.getOpenIdConnectSessionId()};

		finderCache.putResult(
			_finderPathCountByOpenIdConnectSessionId, args, Long.valueOf(1));
		finderCache.putResult(
			_finderPathFetchByOpenIdConnectSessionId, args, oidcSidModelImpl);
	}

	/**
	 * Creates a new oidc sid with the primary key. Does not add the oidc sid to the database.
	 *
	 * @param OidcSidId the primary key for the new oidc sid
	 * @return the new oidc sid
	 */
	@Override
	public OidcSid create(long OidcSidId) {
		OidcSid oidcSid = new OidcSidImpl();

		oidcSid.setNew(true);
		oidcSid.setPrimaryKey(OidcSidId);

		String uuid = _portalUUID.generate();

		oidcSid.setUuid(uuid);

		oidcSid.setCompanyId(CompanyThreadLocal.getCompanyId());

		return oidcSid;
	}

	/**
	 * Removes the oidc sid with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param OidcSidId the primary key of the oidc sid
	 * @return the oidc sid that was removed
	 * @throws NoSuchOidcSidException if a oidc sid with the primary key could not be found
	 */
	@Override
	public OidcSid remove(long OidcSidId) throws NoSuchOidcSidException {
		return remove((Serializable)OidcSidId);
	}

	/**
	 * Removes the oidc sid with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the oidc sid
	 * @return the oidc sid that was removed
	 * @throws NoSuchOidcSidException if a oidc sid with the primary key could not be found
	 */
	@Override
	public OidcSid remove(Serializable primaryKey)
		throws NoSuchOidcSidException {

		Session session = null;

		try {
			session = openSession();

			OidcSid oidcSid = (OidcSid)session.get(
				OidcSidImpl.class, primaryKey);

			if (oidcSid == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchOidcSidException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(oidcSid);
		}
		catch (NoSuchOidcSidException noSuchEntityException) {
			throw noSuchEntityException;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected OidcSid removeImpl(OidcSid oidcSid) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(oidcSid)) {
				oidcSid = (OidcSid)session.get(
					OidcSidImpl.class, oidcSid.getPrimaryKeyObj());
			}

			if (oidcSid != null) {
				session.delete(oidcSid);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (oidcSid != null) {
			clearCache(oidcSid);
		}

		return oidcSid;
	}

	@Override
	public OidcSid updateImpl(OidcSid oidcSid) {
		boolean isNew = oidcSid.isNew();

		if (!(oidcSid instanceof OidcSidModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(oidcSid.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(oidcSid);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in oidcSid proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom OidcSid implementation " +
					oidcSid.getClass());
		}

		OidcSidModelImpl oidcSidModelImpl = (OidcSidModelImpl)oidcSid;

		if (Validator.isNull(oidcSid.getUuid())) {
			String uuid = _portalUUID.generate();

			oidcSid.setUuid(uuid);
		}

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date date = new Date();

		if (isNew && (oidcSid.getCreateDate() == null)) {
			if (serviceContext == null) {
				oidcSid.setCreateDate(date);
			}
			else {
				oidcSid.setCreateDate(serviceContext.getCreateDate(date));
			}
		}

		if (!oidcSidModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				oidcSid.setModifiedDate(date);
			}
			else {
				oidcSid.setModifiedDate(serviceContext.getModifiedDate(date));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(oidcSid);
			}
			else {
				oidcSid = (OidcSid)session.merge(oidcSid);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		entityCache.putResult(OidcSidImpl.class, oidcSidModelImpl, false, true);

		cacheUniqueFindersCache(oidcSidModelImpl);

		if (isNew) {
			oidcSid.setNew(false);
		}

		oidcSid.resetOriginalValues();

		return oidcSid;
	}

	/**
	 * Returns the oidc sid with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the oidc sid
	 * @return the oidc sid
	 * @throws NoSuchOidcSidException if a oidc sid with the primary key could not be found
	 */
	@Override
	public OidcSid findByPrimaryKey(Serializable primaryKey)
		throws NoSuchOidcSidException {

		OidcSid oidcSid = fetchByPrimaryKey(primaryKey);

		if (oidcSid == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchOidcSidException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return oidcSid;
	}

	/**
	 * Returns the oidc sid with the primary key or throws a <code>NoSuchOidcSidException</code> if it could not be found.
	 *
	 * @param OidcSidId the primary key of the oidc sid
	 * @return the oidc sid
	 * @throws NoSuchOidcSidException if a oidc sid with the primary key could not be found
	 */
	@Override
	public OidcSid findByPrimaryKey(long OidcSidId)
		throws NoSuchOidcSidException {

		return findByPrimaryKey((Serializable)OidcSidId);
	}

	/**
	 * Returns the oidc sid with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param OidcSidId the primary key of the oidc sid
	 * @return the oidc sid, or <code>null</code> if a oidc sid with the primary key could not be found
	 */
	@Override
	public OidcSid fetchByPrimaryKey(long OidcSidId) {
		return fetchByPrimaryKey((Serializable)OidcSidId);
	}

	/**
	 * Returns all the oidc sids.
	 *
	 * @return the oidc sids
	 */
	@Override
	public List<OidcSid> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the oidc sids.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OidcSidModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of oidc sids
	 * @param end the upper bound of the range of oidc sids (not inclusive)
	 * @return the range of oidc sids
	 */
	@Override
	public List<OidcSid> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the oidc sids.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OidcSidModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of oidc sids
	 * @param end the upper bound of the range of oidc sids (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of oidc sids
	 */
	@Override
	public List<OidcSid> findAll(
		int start, int end, OrderByComparator<OidcSid> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the oidc sids.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OidcSidModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of oidc sids
	 * @param end the upper bound of the range of oidc sids (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of oidc sids
	 */
	@Override
	public List<OidcSid> findAll(
		int start, int end, OrderByComparator<OidcSid> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindAll;
				finderArgs = FINDER_ARGS_EMPTY;
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindAll;
			finderArgs = new Object[] {start, end, orderByComparator};
		}

		List<OidcSid> list = null;

		if (useFinderCache) {
			list = (List<OidcSid>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_OIDCSID);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_OIDCSID;

				sql = sql.concat(OidcSidModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<OidcSid>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the oidc sids from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (OidcSid oidcSid : findAll()) {
			remove(oidcSid);
		}
	}

	/**
	 * Returns the number of oidc sids.
	 *
	 * @return the number of oidc sids
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(_SQL_COUNT_OIDCSID);

				count = (Long)query.uniqueResult();

				finderCache.putResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	public Set<String> getBadColumnNames() {
		return _badColumnNames;
	}

	@Override
	protected EntityCache getEntityCache() {
		return entityCache;
	}

	@Override
	protected String getPKDBName() {
		return "OidcSidId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_OIDCSID;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return OidcSidModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the oidc sid persistence.
	 */
	@Activate
	public void activate() {
		_valueObjectFinderCacheListThreshold = GetterUtil.getInteger(
			PropsUtil.get(PropsKeys.VALUE_OBJECT_FINDER_CACHE_LIST_THRESHOLD));

		_finderPathWithPaginationFindAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathWithoutPaginationFindAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathCountAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0], new String[0], false);

		_finderPathWithPaginationFindByUuid = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid",
			new String[] {
				String.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"uuid_"}, true);

		_finderPathWithoutPaginationFindByUuid = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid",
			new String[] {String.class.getName()}, new String[] {"uuid_"},
			true);

		_finderPathCountByUuid = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid",
			new String[] {String.class.getName()}, new String[] {"uuid_"},
			false);

		_finderPathWithPaginationFindByUuid_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid_C",
			new String[] {
				String.class.getName(), Long.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			},
			new String[] {"uuid_", "companyId"}, true);

		_finderPathWithoutPaginationFindByUuid_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid_C",
			new String[] {String.class.getName(), Long.class.getName()},
			new String[] {"uuid_", "companyId"}, true);

		_finderPathCountByUuid_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid_C",
			new String[] {String.class.getName(), Long.class.getName()},
			new String[] {"uuid_", "companyId"}, false);

		_finderPathFetchBySid = new FinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchBySid",
			new String[] {String.class.getName()}, new String[] {"sid"}, true);

		_finderPathCountBySid = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countBySid",
			new String[] {String.class.getName()}, new String[] {"sid"}, false);

		_finderPathFetchByOpenIdConnectSessionId = new FinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByOpenIdConnectSessionId",
			new String[] {Long.class.getName()},
			new String[] {"openIdConnectSessionId"}, true);

		_finderPathCountByOpenIdConnectSessionId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByOpenIdConnectSessionId",
			new String[] {Long.class.getName()},
			new String[] {"openIdConnectSessionId"}, false);

		_setOidcSidUtilPersistence(this);
	}

	@Deactivate
	public void deactivate() {
		_setOidcSidUtilPersistence(null);

		entityCache.removeCache(OidcSidImpl.class.getName());
	}

	private void _setOidcSidUtilPersistence(
		OidcSidPersistence oidcSidPersistence) {

		try {
			Field field = OidcSidUtil.class.getDeclaredField("_persistence");

			field.setAccessible(true);

			field.set(null, oidcSidPersistence);
		}
		catch (ReflectiveOperationException reflectiveOperationException) {
			throw new RuntimeException(reflectiveOperationException);
		}
	}

	@Override
	@Reference(
		target = OIDCSIDPersistenceConstants.SERVICE_CONFIGURATION_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
	}

	@Override
	@Reference(
		target = OIDCSIDPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = OIDCSIDPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Reference
	protected EntityCache entityCache;

	@Reference
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_OIDCSID =
		"SELECT oidcSid FROM OidcSid oidcSid";

	private static final String _SQL_SELECT_OIDCSID_WHERE =
		"SELECT oidcSid FROM OidcSid oidcSid WHERE ";

	private static final String _SQL_COUNT_OIDCSID =
		"SELECT COUNT(oidcSid) FROM OidcSid oidcSid";

	private static final String _SQL_COUNT_OIDCSID_WHERE =
		"SELECT COUNT(oidcSid) FROM OidcSid oidcSid WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS = "oidcSid.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No OidcSid exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No OidcSid exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		OidcSidPersistenceImpl.class);

	private static final Set<String> _badColumnNames = SetUtil.fromArray(
		new String[] {"uuid"});

	@Override
	protected FinderCache getFinderCache() {
		return finderCache;
	}

	@Reference
	private PortalUUID _portalUUID;

}