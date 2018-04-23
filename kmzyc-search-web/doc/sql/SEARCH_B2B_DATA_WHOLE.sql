-- Create table
create table SEARCH_B2B_DATA_WHOLE
(
  product_sku_id               NUMBER(22),
  product_id                   NUMBER(22),
  product_sku_code             VARCHAR2(64),
  mark_price                   NUMBER(11,2),
  pv_value                     NUMBER(11,2),
  price                        NUMBER(11,2),
  channel                      VARCHAR2(16),
  sale_quantity                NUMBER(22),
  img_path                     VARCHAR2(2000),
  category_attr_name_is_nav    VARCHAR2(1000),
  category_attr_value_is_nav   VARCHAR2(1000),
  category_attr_name           VARCHAR2(1000),
  category_attr_value          VARCHAR2(1000),
  procuct_name                 VARCHAR2(512),
  product_title                VARCHAR2(512),
  product_subtitle             VARCHAR2(512),
  keyword                      VARCHAR2(600),
  producthot                   NUMBER(22),
  brand_id                     NUMBER(22),
  brand_name                   VARCHAR2(64),
  create_time                  DATE,
  up_time                      DATE,
  approval_type                NUMBER(2),
  approval_no                  VARCHAR2(128),
  shop_code                    VARCHAR2(8),
  product_no                   VARCHAR2(32),
  drug_cate_id                 NUMBER(22),
  drug_cate_code               VARCHAR2(16),
  product_type                 NUMBER(2),
  category_id                  VARCHAR2(500),
  category_code                VARCHAR2(1000),
  category_name                VARCHAR2(1000),
  supplier_type                NUMBER(2),
  shop_name                    VARCHAR2(128),
  shop_id                      NUMBER(22),
  product_attr_name_cate       VARCHAR2(1000),
  category_attr_value_product  VARCHAR2(1000),
  product_attr_name_op         VARCHAR2(1000),
  quality                      NUMBER(22),
  grade                        NUMBER,
  pnum                         NUMBER(22),
  browse                       NUMBER(22),
  shop_category_id             VARCHAR2(500),
  category_attr_name_is_nav_cp VARCHAR2(2000),
  category_attr_value_ss       VARCHAR2(1000),
  product_attr_name_cate_cp    VARCHAR2(2000),
  product_attr_name_opval      VARCHAR2(1000)
);
commit;
-- Add comments to the table 
comment on table SEARCH_B2B_DATA_WHOLE
  is '搜索索引数据表';
-- Add comments to the columns 
comment on column SEARCH_B2B_DATA_WHOLE.product_sku_id
  is 'SKU唯一标示';
comment on column SEARCH_B2B_DATA_WHOLE.product_id
  is '产品唯一标示';
comment on column SEARCH_B2B_DATA_WHOLE.product_sku_code
  is 'SKU编号';
comment on column SEARCH_B2B_DATA_WHOLE.mark_price
  is '市场价';
comment on column SEARCH_B2B_DATA_WHOLE.pv_value
  is 'pv值';
comment on column SEARCH_B2B_DATA_WHOLE.price
  is '价格';
comment on column SEARCH_B2B_DATA_WHOLE.channel
  is '所属渠道（B2B）';
comment on column SEARCH_B2B_DATA_WHOLE.sale_quantity
  is '销售数量';
comment on column SEARCH_B2B_DATA_WHOLE.category_attr_name_is_nav
  is '类目属性名（导航）';
comment on column SEARCH_B2B_DATA_WHOLE.category_attr_value_is_nav
  is '类目属性值（导航）';
comment on column SEARCH_B2B_DATA_WHOLE.category_attr_name
  is '类目属性名';
comment on column SEARCH_B2B_DATA_WHOLE.category_attr_value
  is '类目属性值';
comment on column SEARCH_B2B_DATA_WHOLE.procuct_name
  is '产品名称';
comment on column SEARCH_B2B_DATA_WHOLE.product_title
  is '产品主标题';
comment on column SEARCH_B2B_DATA_WHOLE.product_subtitle
  is '产品副标题';
comment on column SEARCH_B2B_DATA_WHOLE.keyword
  is '关键词（seo）';
comment on column SEARCH_B2B_DATA_WHOLE.producthot
  is '产品热度';
comment on column SEARCH_B2B_DATA_WHOLE.brand_id
  is '品牌ID';
comment on column SEARCH_B2B_DATA_WHOLE.brand_name
  is '品牌名称';
comment on column SEARCH_B2B_DATA_WHOLE.create_time
  is '创建时间';
comment on column SEARCH_B2B_DATA_WHOLE.up_time
  is '上架时间';
comment on column SEARCH_B2B_DATA_WHOLE.approval_type
  is '批准文类型 1：国药准字 2：国药试字 3：国药健字 4：国食健字 5：卫食健字';
comment on column SEARCH_B2B_DATA_WHOLE.approval_no
  is '批准文号';
comment on column SEARCH_B2B_DATA_WHOLE.shop_code
  is '商铺唯一标示:康美电商默认：S001';
comment on column SEARCH_B2B_DATA_WHOLE.product_no
  is '产品编码';
comment on column SEARCH_B2B_DATA_WHOLE.drug_cate_id
  is '品类ID';
comment on column SEARCH_B2B_DATA_WHOLE.drug_cate_code
  is '品类编号';
comment on column SEARCH_B2B_DATA_WHOLE.product_type
  is '0：非药品 1：OTC药品 2：医疗器械';
comment on column SEARCH_B2B_DATA_WHOLE.category_id
  is '物理类目唯一性标示';
comment on column SEARCH_B2B_DATA_WHOLE.category_code
  is '编码规则：父编码+该父类目4位唯一编码';
comment on column SEARCH_B2B_DATA_WHOLE.category_name
  is '类目名称';
comment on column SEARCH_B2B_DATA_WHOLE.supplier_type
  is '经销商 厂商';
comment on column SEARCH_B2B_DATA_WHOLE.shop_name
  is '店名';
comment on column SEARCH_B2B_DATA_WHOLE.product_attr_name_cate
  is '产品属性名（类目）';
comment on column SEARCH_B2B_DATA_WHOLE.category_attr_value_product
  is '产品属性值 (类目)';
comment on column SEARCH_B2B_DATA_WHOLE.product_attr_name_op
  is '产品属性名（运营）';
comment on column SEARCH_B2B_DATA_WHOLE.quality
  is '库存';
comment on column SEARCH_B2B_DATA_WHOLE.grade
  is '评价分数平均值';
comment on column SEARCH_B2B_DATA_WHOLE.pnum
  is '评价条数';
comment on column SEARCH_B2B_DATA_WHOLE.browse
  is '浏览次数';
comment on column SEARCH_B2B_DATA_WHOLE.shop_category_id
  is '店铺类别id';

  
--20180104 增加药材市场id和药材市场名称字段
-- Add/modify columns 
alter table SEARCH_B2B_DATA_WHOLE add market_id NUMBER(22);
alter table SEARCH_B2B_DATA_WHOLE add market_name VARCHAR2(256);
-- Add comments to the columns 
comment on column SEARCH_B2B_DATA_WHOLE.market_id
  is '药材市场ID';
comment on column SEARCH_B2B_DATA_WHOLE.market_name
  is '药材市场名称';
