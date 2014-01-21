-- Create table
--�ñ����ڴ�������¼���û�,���������û�
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
  is '���ʺ�ID';
comment on column VPN_4A_USER.LOGINNAME
  is '4A���ʺ�����';
comment on column VPN_4A_USER.PASSWORD
  is '���ʺ�����';
comment on column VPN_4A_USER.STATE
  is '���ʺ�״̬';
comment on column VPN_4A_USER.EFFICTTIME
  is '��Ч��ʼʱ��';
comment on column VPN_4A_USER.EXPIRETIME
  is 'ʧЧʱ��';
comment on column VPN_4A_USER.APPROVE
  is '�Ƿ�����VPN��¼';
-- Create/Recreate primary, unique and foreign key constraints 
alter table VPN_4A_USER
  add constraint VPN_4A_PK primary key (ACCOUNTID, LOGINNAME);
