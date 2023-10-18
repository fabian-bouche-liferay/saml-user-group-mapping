create index IX_F6CE5431 on OIDCSID_OidcSid (openIdConnectSessionId);
create index IX_10C97B0D on OIDCSID_OidcSid (sid[$COLUMN_LENGTH:75$]);
create index IX_4E1626C5 on OIDCSID_OidcSid (uuid_[$COLUMN_LENGTH:75$], companyId);