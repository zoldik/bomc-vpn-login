-- Create table
create table VPN_APPROVE_USER
(
  LOGINNAME     VARCHAR2(64) not null,
  USERID        VARCHAR2(64) not null,
  NAME          VARCHAR2(64) not null,
  PASSWORD      VARCHAR2(64) not null,
  REGISTEDATE   DATE not null,
  MOBILE        VARCHAR2(11) not null,
  EMAIL         VARCHAR2(255),
  STATE         NUMBER(2) not null,
  SMSTYPE       VARCHAR2(10) default 'SMS' not null,
  IS_ADMIN      NUMBER(1) default 0 not null,
  APPROVE_LOGIN NUMBER(1) default 0
)

comment on column VPN_APPROVE_USER.LOGINNAME
  is '登录帐号名';
comment on column VPN_APPROVE_USER.USERID
  is '用户ID用来关联用户表';
comment on column VPN_APPROVE_USER.NAME
  is '用户登录名';
comment on column VPN_APPROVE_USER.PASSWORD
  is '密码';
comment on column VPN_APPROVE_USER.REGISTEDATE
  is '注册时间';
comment on column VPN_APPROVE_USER.MOBILE
  is '手机号码';
comment on column VPN_APPROVE_USER.EMAIL
  is '邮箱地址';
comment on column VPN_APPROVE_USER.STATE
  is '用户状态,0激活1禁用';
comment on column VPN_APPROVE_USER.SMSTYPE
  is '短信通知方式,SMS,EMAIL,SMSEMAIL';
comment on column VPN_APPROVE_USER.IS_ADMIN
  is '是否为管理员0否，1是';
comment on column VPN_APPROVE_USER.APPROVE_LOGIN
  is '是允许VPN登录，0否，1是';
-- Create/Recreate primary, unique and foreign key constraints 
alter table VPN_APPROVE_USER
  add constraint PK primary key (LOGINNAME);
  
 -- Create table
create table VPN_USER_ADVICE
(
  ID        NUMBER not null,
  LOGINNAME VARCHAR2(64),
  USERID    VARCHAR2(64),
  NAME      VARCHAR2(64),
  ADVICE   VARCHAR2(255),
   CREATE_TIME DATE,
  PARENT_ID   NUMBER not null
)

comment on column VPN_USER_ADVICE.ID
  is 'ID';
comment on column VPN_USER_ADVICE.LOGINNAME
  is '登录帐号';
comment on column VPN_USER_ADVICE.USERID
  is '用户ID关联 gaus_userinfo';
comment on column VPN_USER_ADVICE.NAME
  is '用户名';
comment on column VPN_USER_ADVICE.ADVICE
  is '留言信息';
  comment on column VPN_USER_ADVICE.CREATE_TIME
  is '创建时间';
comment on column VPN_USER_ADVICE.PARENT_ID
  is '0';
-- Create/Recreate primary, unique and foreign key constraints 
alter table VPN_USER_MESSAGE
  add constraint ID_PK primary key (ID);
  
 create sequence VPN_USER_ADVICE_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;