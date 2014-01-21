-- Create table
--该表用于存放用户留言
create table VPN_USER_ADVICE
(
  ID          NUMBER not null,
  LOGINNAME   VARCHAR2(64) not null,
  USERID      VARCHAR2(64),
  NAME        VARCHAR2(64),
  ADVICE      VARCHAR2(255) not null,
  CREATE_TIME DATE,
  PARENT_ID   NUMBER not null
)
tablespace BOMCBP_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 40K
    next 40K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
-- Add comments to the columns 
comment on column VPN_USER_ADVICE.ID
  is 'ID';
comment on column VPN_USER_ADVICE.LOGINNAME
  is '登录帐号';
comment on column VPN_USER_ADVICE.USERID
  is '用户ID关联 gaus_userinfo,暂时允许为空';
comment on column VPN_USER_ADVICE.NAME
  is '用户名';
comment on column VPN_USER_ADVICE.ADVICE
  is '留言信息';
comment on column VPN_USER_ADVICE.CREATE_TIME
  is '留言日期';
comment on column VPN_USER_ADVICE.PARENT_ID
  is '0';
-- Create/Recreate primary, unique and foreign key constraints 
alter table VPN_USER_ADVICE
  add constraint ID_PK primary key (ID);

create sequence VPN_USER_ADVICE_SEQ
	minvalue 1
	maxvalue 999999999999999999999999999
	start with 1
	increment by 1
	cache 20;