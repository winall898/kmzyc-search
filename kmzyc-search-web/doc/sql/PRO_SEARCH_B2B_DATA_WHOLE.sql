create or replace procedure PRO_SEARCH_B2B_DATA_WHOLE as
  CURSOR cur is
    select t1.product_sku_id,
           t1.product_id,
           t1.product_sku_code,
           t1.price,
           t1.bottom_amount,
           t1.mark_price,
           t1.pv_value,
           t1.procuct_name,
           t1.product_title,
           t1.product_subtitle,
           t1.keyword,
           t1.producthot,
           t1.brand_id,
           t1.brand_name,
           t1.create_time,
           t1.up_time,
           t1.approval_type,
           t1.approval_no,
           t1.category_id,
           t1.shop_code,
           t1.product_no,
           t1.drug_cate_id,
           t1.drug_cate_code,
           t1.channel,
           t1.product_type,
           t1.unit,
           t2.sales,
           t3.image,
           t4.supType,
           t4.market_id,
           t4.market_name,
           t5.shopId,
           t5.shopName,
           NVL2(t12.attrname, t12.attrname || ',', '') || t13.attrname as attrname,
           NVL2(t12.attrval, t12.attrval || ',', '') || t13.attrval as attrval,
           NVL2(t12.attrval_cp, t12.attrval_cp || ',', '') ||
           t13.attrval_cp as attrval_cp,
           t9.san_ss,
           t9.sav_cns,
           t9.sav_ss,
           t10.opname,
           NVL(t6.stock, 0) as stock,
           NVL(t7.grade, 0) as grade,
           NVL(t7.pnum, 0) as pnum,
           NVL(t8.browse, 0) as browse,
           t11.shopgory
      from (select ps.PRODUCT_SKU_ID,
                   ps.PRODUCT_SKU_CODE,
                   ps.PRODUCT_ID,
                   nvl(t.PRICE, 0) PRICE,
                   nvl(t.BOTTOM_AMOUNT, 0) BOTTOM_AMOUNT,
                   ps.MARK_PRICE,
                   ps.PV_VALUE,
                   p.PROCUCT_NAME,
                   p.PRODUCT_TITLE,
                   p.PRODUCT_SUBTITLE,
                   p.KEYWORD,
                   p.PRODUCTHOT,
                   p.BRAND_ID,
                   b.BRAND_NAME,
                   p.CREATE_TIME,
                   p.UP_TIME,
                   p.APPROVAL_TYPE,
                   p.APPROVAL_NO,
                   p.CATEGORY_ID,
                   p.SHOP_CODE,
                   p.PRODUCT_NO,
                   p.DRUG_CATE_ID,
                   p.DRUG_CATE_CODE,
                   p.CHANNEL,
                   p.PRODUCT_TYPE,
                   p.UNIT
              from PRODUCT_SKU ps,
                   PRODUCTMAIN p,
                   PROD_BRAND b,
                   (select ppl.PRODUCT_SKU_ID,
                           min(ppl.PRICE) PRICE,
                           min(ppl.BOTTOM_AMOUNT) BOTTOM_AMOUNT
                      from PRODUCT_PRICE_LADDER ppl
                     group by ppl.PRODUCT_SKU_ID) t
             where ps.PRODUCT_ID = p.PRODUCT_ID
               and p.BRAND_ID = b.BRAND_ID(+)
               and ps.product_sku_id = t.product_sku_id(+)
               and p.STATUS = 3
               and ps.status = 1) t1
      left join (select max(SALE_QUANTITY) as sales, PRODUCT_SKU_ID
                   from PRODUCT_SKU_QUANTITY
                  group by PRODUCT_SKU_ID) t2
        on t1.PRODUCT_SKU_ID = t2.PRODUCT_SKU_ID
      left join (select max(NVL2(IMG_PATH1, 'IMG_PATH1=' || IMG_PATH1, '') || ',' ||
                            NVL2(IMG_PATH2, 'IMG_PATH2=' || IMG_PATH2, '') || ',' ||
                            NVL2(IMG_PATH3, 'IMG_PATH3=' || IMG_PATH3, '') || ',' ||
                            NVL2(IMG_PATH4, 'IMG_PATH4=' || IMG_PATH4, '') || ',' ||
                            NVL2(IMG_PATH5, 'IMG_PATH5=' || IMG_PATH5, '') || ',' ||
                            NVL2(IMG_PATH6, 'IMG_PATH6=' || IMG_PATH6, '') || ',' ||
                            NVL2(IMG_PATH7, 'IMG_PATH7=' || IMG_PATH7, '') || ',' ||
                            NVL2(IMG_PATH8, 'IMG_PATH8=' || IMG_PATH8, '') || ',' ||
                            NVL2(IMG_PATH9, 'IMG_PATH9=' || IMG_PATH9, '') || ',' ||
                            NVL2(IMG_PATH10, 'IMG_PATH10=' || IMG_PATH10, '')) as image,
                        SKU_ID
                   from PRODUCT_IMAGE
                  where IS_DEFAULT = 0
                  group by SKU_ID) t3
        on t1.PRODUCT_SKU_ID = t3.SKU_ID
      left join (select si.supplier_type as supType,
                        si.supplier_id,
                        si.market_id,
                        mm.market_name
                   from suppliers_info si, medicine_market mm
                  where si.market_id = mm.market_id(+)) t4
        on t1.SHOP_CODE = t4.SUPPLIER_ID
      left join (select SHOP_ID     as shopId,
                        SHOP_NAME   as shopName,
                        SUPPLIER_ID
                   from shop_main) t5
        on t1.SHOP_CODE = t5.SUPPLIER_ID
      left join (select sum(STOCK_QUALITY) as stock, SKU_ATTRIBUTE_ID
                   from PRODUCT_STOCK
                  group by SKU_ATTRIBUTE_ID) t6
        on t1.PRODUCT_SKU_ID = t6.SKU_ATTRIBUTE_ID
      left join (select avg(POINT) as grade,
                        count(APPRAISE_ID) as pnum,
                        PRODUCT_SKU_ID
                   from PROD_APPRAISE
                  group by PRODUCT_SKU_ID) t7
        on t1.PRODUCT_SKU_ID = t7.PRODUCT_SKU_ID
      left join (select count(CONTENT_CODE) as browse, CONTENT_CODE
                   from BNES_BROWSING_HIS
                  where BROWSING_TYPE = 1
                  group by CONTENT_CODE) t8
        on t1.PRODUCT_SKU_CODE = t8.CONTENT_CODE
      left join (select LISTAGG(psa.CATEGORY_ATTR_NAME, ',') WITHIN GROUP(ORDER BY psa.product_sku_attr_id) as san_ss,
                        LISTAGG(cav.CATEGORY_ATTR_VALUE) WITHIN GROUP(ORDER BY cav.CATEGORY_ATTR_ID) as sav_cns,
                        LISTAGG(cav.CATEGORY_ATTR_VALUE) WITHIN GROUP(ORDER BY cav.CATEGORY_ATTR_ID) as sav_ss,
                        PRODUCT_SKU_ID
                   from PRODUCT_SKU_ATTR    psa,
                        CATEGORY_ATTR       ca,
                        CATEGORY_ATTR_VALUE cav
                  where psa.CATEGORY_ATTR_VALUE_ID =
                        cav.CATEGORY_ATTR_VALUE_ID
                    and ca.CATEGORY_ATTR_ID = psa.CATEGORY_ATTR_ID
                  group by PRODUCT_SKU_ID) t9
        on t1.PRODUCT_SKU_ID = t9.PRODUCT_SKU_ID
      left join (select LISTAGG(PRODUCT_ATTR_NAME, ',') WITHIN GROUP(ORDER BY PRODUCT_ATTR_ID) as opname,
                        PRODUCT_ID
                   from PRODUCT_ATTR
                  where PRODUCT_ATTR_TYPE = 3
                  group by PRODUCT_ID) t10
        on t1.PRODUCT_ID = t10.PRODUCT_ID
      left join (select LISTAGG(shop_category_id, ',') WITHIN GROUP(ORDER BY ID) as shopgory,
                        PRODUCT_ID
                   from SHOP_PRODUCT_CATEGORY
                  group by PRODUCT_ID) t11
        on t1.PRODUCT_ID = t11.PRODUCT_ID
      left join (select psa.PRODUCT_SKU_ID,
                        LISTAGG(psa.CATEGORY_ATTR_NAME, ',') WITHIN GROUP(ORDER BY psa.PRODUCT_SKU_ATTR_ID) as attrname,
                        LISTAGG(cav.CATEGORY_ATTR_VALUE, ',') WITHIN GROUP(ORDER BY cav.CATEGORY_ATTR_ID) as attrval,
                        LISTAGG((psa.CATEGORY_ATTR_NAME || '=' ||
                                cav.CATEGORY_ATTR_VALUE),
                                ',') WITHIN GROUP(ORDER BY psa.PRODUCT_SKU_ATTR_ID) as attrval_cp
                   from PRODUCT_SKU_ATTR    psa,
                        CATEGORY_ATTR       ca,
                        CATEGORY_ATTR_VALUE cav
                  where psa.CATEGORY_ATTR_VALUE_ID =
                        cav.CATEGORY_ATTR_VALUE_ID
                    and ca.CATEGORY_ATTR_ID = psa.CATEGORY_ATTR_ID
                    and ca.IS_NAV = 1
                  group by psa.PRODUCT_SKU_ID) t12
        on t1.PRODUCT_SKU_ID = t12.PRODUCT_SKU_ID
      left join (select tmp.PRODUCT_ID,
                        LISTAGG(tmp.PRODUCT_ATTR_NAME, ',') WITHIN GROUP(ORDER BY tmp.PRODUCT_ATTR_ID) as attrname,
                        LISTAGG(cav.CATEGORY_ATTR_VALUE, ',') WITHIN GROUP(ORDER BY cav.CATEGORY_ATTR_ID) as attrval,
                        LISTAGG((tmp.PRODUCT_ATTR_NAME || '=' ||
                                cav.CATEGORY_ATTR_VALUE),
                                ',') WITHIN GROUP(ORDER BY tmp.PRODUCT_ATTR_ID) as attrval_cp
                   from (select pa.*, pb.COLUMN_VALUE as VALUE_ID
                           from (select *
                                   from PRODUCT_ATTR
                                  where IS_SKU = 0
                                    and IS_NAV = 1
                                    and PRODUCT_ATTR_TYPE = 1
                                    and INPUT_TYPE != 0
                                    and PRODUCT_ATTR_VALUE is not null) pa,
                                TABLE(splitByComma(pa.PRODUCT_ATTR_VALUE)) pb) tmp
                   left join CATEGORY_ATTR_VALUE cav
                     on tmp.VALUE_ID = cav.CATEGORY_ATTR_VALUE_ID
                  group by tmp.PRODUCT_ID) t13
        on t1.PRODUCT_ID = t13.PRODUCT_ID;

  pcId   VARCHAR2(500);
  pcCode VARCHAR2(1000);
  pcName VARCHAR2(1000);
  type dept_record is table of cur%rowtype;
  cur_result dept_record;
BEGIN

  execute immediate 'truncate table KMDATA.SEARCH_B2B_DATA_WHOLE';

  open cur;
  loop
    exit when cur%NOTFOUND;
    fetch cur bulk collect
      into cur_result limit 500;
  
    for i in 1 .. cur_result.count loop
      select LISTAGG(CATEGORY_ID, ',') WITHIN GROUP(ORDER BY CATEGORY_ID desc),
             LISTAGG(CATEGORY_CODE, ',') WITHIN GROUP(ORDER BY CATEGORY_ID desc),
             LISTAGG(CATEGORY_NAME, ',') WITHIN GROUP(ORDER BY CATEGORY_ID desc)
        into pcId, pcCode, pcName
        from CATEGORYS ca
       start with ca.CATEGORY_ID = cur_result(i).CATEGORY_ID
      connect by prior ca.PARENT_ID = ca.CATEGORY_ID;
    
      INSERT INTO SEARCH_B2B_DATA_WHOLE
        (PRODUCT_SKU_ID,
         PRODUCT_ID,
         PRODUCT_SKU_CODE,
         PRICE,
         BOTTOM_AMOUNT,
         MARK_PRICE,
         PV_VALUE,
         PROCUCT_NAME,
         PRODUCT_TITLE,
         PRODUCT_SUBTITLE,
         KEYWORD,
         PRODUCTHOT,
         BRAND_ID,
         BRAND_NAME,
         CREATE_TIME,
         UP_TIME,
         APPROVAL_TYPE,
         APPROVAL_NO,
         SHOP_CODE,
         PRODUCT_NO,
         DRUG_CATE_ID,
         DRUG_CATE_CODE,
         CHANNEL,
         PRODUCT_TYPE,
         UNIT,
         SALE_QUANTITY,
         IMG_PATH,
         CATEGORY_ATTR_NAME_IS_NAV,
         CATEGORY_ATTR_VALUE_IS_NAV,
         CATEGORY_ATTR_NAME_IS_NAV_CP,
         CATEGORY_ATTR_NAME,
         CATEGORY_ATTR_VALUE,
         CATEGORY_ATTR_VALUE_SS,
         CATEGORY_ID,
         CATEGORY_CODE,
         CATEGORY_NAME,
         SUPPLIER_TYPE,
         SHOP_ID,
         SHOP_NAME,
         PRODUCT_ATTR_NAME_OP,
         PRODUCT_ATTR_NAME_OPVAL,
         QUALITY,
         GRADE,
         PNUM,
         BROWSE,
         SHOP_CATEGORY_ID,
         MARKET_ID,
         MARKET_NAME)
      VALUES
        (cur_result(i).product_sku_id,
         cur_result(i).product_id,
         cur_result(i).product_sku_code,
         cur_result(i).price,
         cur_result(i).bottom_amount,
         cur_result(i).mark_price,
         cur_result(i).pv_value,
         cur_result(i).procuct_name,
         cur_result(i).product_title,
         cur_result(i).product_subtitle,
         cur_result(i).keyword,
         cur_result(i).producthot,
         cur_result(i).brand_id,
         cur_result(i).brand_name,
         cur_result(i).create_time,
         cur_result(i).up_time,
         cur_result(i).approval_type,
         cur_result(i).approval_no,
         cur_result(i).shop_code,
         cur_result(i).product_no,
         cur_result(i).drug_cate_id,
         cur_result(i).drug_cate_code,
         cur_result(i).channel,
         cur_result(i).product_type,
         cur_result(i).unit,
         cur_result(i).sales,
         cur_result(i).image,
         cur_result(i).attrname,
         cur_result(i).attrval,
         cur_result(i).attrval_cp,
         cur_result(i).san_ss,
         cur_result(i).sav_cns,
         cur_result(i).sav_ss,
         pcId,
         pcCode,
         pcName,
         cur_result(i).supType,
         cur_result(i).shopId,
         cur_result(i).shopName,
         cur_result(i).opname,
         cur_result(i).opname,
         cur_result(i).stock,
         cur_result(i).grade,
         cur_result(i).pnum,
         cur_result(i).browse,
         cur_result(i).shopgory,
         cur_result(i).market_id,
         cur_result(i).market_name);
    end loop;
    commit;
  end loop;
  close cur;
END;