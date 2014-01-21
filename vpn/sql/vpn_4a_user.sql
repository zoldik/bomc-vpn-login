-- Create table
--该表用于存放允许登录的用户,即白名单用户
create table VPN_4A_USER
(
  ACCOUNTID  NUMBER not null,
  LOGINNAME  VARCHAR2(64) not null,
  PASSWORD   VARCHAR2(255),
  STATE      NUMBER(1),
  EFFICTTIME DATE,
  EXPIRETIME DATE,
  APPROVE    NUMBER(1) default 0
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
comment on column VPN_4A_USER.ACCOUNTID
  is '主帐号ID';
comment on column VPN_4A_USER.LOGINNAME
  is '4A主帐号名称';
comment on column VPN_4A_USER.PASSWORD
  is '主帐号密码';
comment on column VPN_4A_USER.STATE
  is '主帐号状态';
comment on column VPN_4A_USER.EFFICTTIME
  is '有效开始时间';
comment on column VPN_4A_USER.EXPIRETIME
  is '失效时间';
comment on column VPN_4A_USER.APPROVE
  is '是否允许VPN登录';
-- Create/Recreate primary, unique and foreign key constraints 
alter table VPN_4A_USER
  add constraint VPN_4A_PK primary key (ACCOUNTID, LOGINNAME);
