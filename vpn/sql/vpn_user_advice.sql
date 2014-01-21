-- Create table
--�ñ����ڴ���û�����
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
  is '��¼�ʺ�';
comment on column VPN_USER_ADVICE.USERID
  is '�û�ID���� gaus_userinfo,��ʱ����Ϊ��';
comment on column VPN_USER_ADVICE.NAME
  is '�û���';
comment on column VPN_USER_ADVICE.ADVICE
  is '������Ϣ';
comment on column VPN_USER_ADVICE.CREATE_TIME
  is '��������';
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