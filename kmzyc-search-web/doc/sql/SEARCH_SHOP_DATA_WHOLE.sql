-- Create table
create table SEARCH_SHOP_DATA_WHOLE
(
  shop_id            NUMBER(22),
  supplier_id        NUMBER(22),
  shop_type          NUMBER(2),
  shop_name          VARCHAR2(128),
  shop_title         VARCHAR2(512),
  logo_path          VARCHAR2(512),
  introduce          VARCHAR2(2000),
  default_domain_url VARCHAR2(512),
  shop_level         NUMBER(5),
  shop_site          VARCHAR2(8),
  shop_seo_key       VARCHAR2(1024),
  audit_time         DATE,
  manage_brand       VARCHAR2(500),
  service_qq         VARCHAR2(100),
  service_type       NUMBER(2),
  corporate_name     VARCHAR2(250),
  corporate_location VARCHAR2(255),
  desc_score         NUMBER,
  speed_score        NUMBER,
  dist_score         NUMBER,
  sale_score         NUMBER,
  province           VARCHAR2(255),
  city               VARCHAR2(255),
  area               VARCHAR2(255)
);
commit;
-- Add comments to the columns 
comment on column SEARCH_SHOP_DATA_WHOLE.shop_id
  is '店铺ID';
comment on column SEARCH_SHOP_DATA_WHOLE.supplier_id
  is '供应商ID';
comment on column SEARCH_SHOP_DATA_WHOLE.shop_type
  is '店铺（销售）类型1,旗舰店  2,专营店 3,专卖店';
comment on column SEARCH_SHOP_DATA_WHOLE.shop_name
  is '店名';
comment on column SEARCH_SHOP_DATA_WHOLE.shop_title
  is '店铺标题';
comment on column SEARCH_SHOP_DATA_WHOLE.logo_path
  is 'LOGO地址';
comment on column SEARCH_SHOP_DATA_WHOLE.introduce
  is '商家（店铺）简介';
comment on column SEARCH_SHOP_DATA_WHOLE.default_domain_url
  is '店铺默认域名';
comment on column SEARCH_SHOP_DATA_WHOLE.shop_level
  is '店铺级别';
comment on column SEARCH_SHOP_DATA_WHOLE.shop_site
  is '店铺渠道';
comment on column SEARCH_SHOP_DATA_WHOLE.shop_seo_key
  is 'SEO';
comment on column SEARCH_SHOP_DATA_WHOLE.audit_time
  is '审核通过时间（开通时间）';
comment on column SEARCH_SHOP_DATA_WHOLE.manage_brand
  is '经营品牌';
comment on column SEARCH_SHOP_DATA_WHOLE.service_qq
  is '客服QQ';
comment on column SEARCH_SHOP_DATA_WHOLE.service_type
  is '客服方式1:QQ,2:旺旺';
comment on column SEARCH_SHOP_DATA_WHOLE.corporate_name
  is '公司名称';
comment on column SEARCH_SHOP_DATA_WHOLE.corporate_location
  is '公司注册地址';
comment on column SEARCH_SHOP_DATA_WHOLE.desc_score
  is '维度总得分';
comment on column SEARCH_SHOP_DATA_WHOLE.speed_score
  is '维度总得分';
comment on column SEARCH_SHOP_DATA_WHOLE.dist_score
  is '维度总得分';
comment on column SEARCH_SHOP_DATA_WHOLE.sale_score
  is '维度总得分';
comment on column SEARCH_SHOP_DATA_WHOLE.province
  is '省';
comment on column SEARCH_SHOP_DATA_WHOLE.city
  is '市';
comment on column SEARCH_SHOP_DATA_WHOLE.area
  is '区';
