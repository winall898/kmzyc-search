-- Create table
create table KMDATA.SEARCH_KEYWORD_DICT_T
(
  id          NUMBER not null,
  keyword     VARCHAR2(200),
  creater     VARCHAR2(50),
  create_time DATE,
  updater     VARCHAR2(50),
  update_time DATE,
  description VARCHAR2(2000),
  constraint PK_SEARCH_KEYWORD_DICT primary key (id)
);
-- Add comments to the table 
comment on table KMDATA.SEARCH_KEYWORD_DICT_T
  is '关键词词库表';
-- Add comments to the columns 
comment on column KMDATA.SEARCH_KEYWORD_DICT_T.id
  is '主键id';
comment on column KMDATA.SEARCH_KEYWORD_DICT_T.keyword
  is '关键词';
comment on column KMDATA.SEARCH_KEYWORD_DICT_T.creater
  is '创建人';
comment on column KMDATA.SEARCH_KEYWORD_DICT_T.create_time
  is '创建时间';
comment on column KMDATA.SEARCH_KEYWORD_DICT_T.updater
  is '最后修改人';
comment on column KMDATA.SEARCH_KEYWORD_DICT_T.update_time
  is '最后修改时间';
comment on column KMDATA.SEARCH_KEYWORD_DICT_T.description
  is '描述';
 
-- Create sequence 
create sequence KMDATA.SEQ_SEARCH_KEYWORD_DICT_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 100
increment by 1
cache 40;

grant select on KMDATA.SEQ_SEARCH_KEYWORD_DICT_ID to r_kmdata_qry;
create public synonym SEQ_SEARCH_KEYWORD_DICT_ID for kmdata.SEQ_SEARCH_KEYWORD_DICT_ID;

grant select on KMDATA.SEARCH_KEYWORD_DICT_T to r_kmdata_qry;
grant insert,update,delete on KMDATA.SEARCH_KEYWORD_DICT_T to r_kmdata_dml;
create public synonym SEARCH_KEYWORD_DICT_T for kmdata.SEARCH_KEYWORD_DICT_T;