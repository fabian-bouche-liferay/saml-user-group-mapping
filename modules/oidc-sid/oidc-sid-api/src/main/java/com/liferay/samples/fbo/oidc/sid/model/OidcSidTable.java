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

import com.liferay.petra.sql.dsl.Column;
import com.liferay.petra.sql.dsl.base.BaseTable;

import java.sql.Types;

import java.util.Date;

/**
 * The table class for the &quot;OIDCSID_OidcSid&quot; database table.
 *
 * @author Brian Wing Shun Chan
 * @see OidcSid
 * @generated
 */
public class OidcSidTable extends BaseTable<OidcSidTable> {

	public static final OidcSidTable INSTANCE = new OidcSidTable();

	public final Column<OidcSidTable, String> uuid = createColumn(
		"uuid_", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<OidcSidTable, Long> OidcSidId = createColumn(
		"OidcSidId", Long.class, Types.BIGINT, Column.FLAG_PRIMARY);
	public final Column<OidcSidTable, Long> companyId = createColumn(
		"companyId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<OidcSidTable, Long> userId = createColumn(
		"userId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<OidcSidTable, String> userName = createColumn(
		"userName", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<OidcSidTable, Date> createDate = createColumn(
		"createDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<OidcSidTable, Date> modifiedDate = createColumn(
		"modifiedDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<OidcSidTable, Long> openIdConnectSessionId =
		createColumn(
			"openIdConnectSessionId", Long.class, Types.BIGINT,
			Column.FLAG_DEFAULT);
	public final Column<OidcSidTable, String> sid = createColumn(
		"sid", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<OidcSidTable, String> jwksUri = createColumn(
		"jwksUri", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);

	private OidcSidTable() {
		super("OIDCSID_OidcSid", OidcSidTable::new);
	}

}