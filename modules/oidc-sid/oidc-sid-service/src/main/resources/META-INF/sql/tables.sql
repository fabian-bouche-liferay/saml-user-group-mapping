create table OIDCSID_OidcSid (
	uuid_ VARCHAR(75) null,
	OidcSidId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	openIdConnectSessionId LONG,
	sid VARCHAR(75) null,
	jwksUri VARCHAR(75) null
);