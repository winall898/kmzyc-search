create or replace procedure KMDATA.PRO_SEARCH_SHOP_DATA_WHOLE as
  cursor cur is
    select s.SHOP_ID,
           s.SUPPLIER_ID,
           s.SHOP_TYPE,
           s.SHOP_NAME,
           s.SHOP_TITLE,
           s.LOGO_PATH,
           s.INTRODUCE,
           s.SHOP_SITE,
           s.SHOP_SEO_KEY,
           s.AUDIT_TIME,
           s.MANAGE_BRAND,
           s.SHOP_LEVEL,
           s.DEFAULT_DOMAIN_URL,
           s.SERVICE_QQ,
           s.SERVICE_TYPE,
           t.CORPORATE_NAME,
           t.CORPORATE_LOCATION,
           t.PROVINCE,
           t.CITY,
           t.AREA,
           so.ASSESS_TYPE_ONE   as DESC_SCORE,
           so.ASSESS_TYPE_TWO   as SPEED_SCORE,
           so.ASSESS_TYPE_THREE as DIST_SCORE,
           so.ASSESS_TYPE_FOUR  as SALE_SCORE
      from SHOP_MAIN s
      left join SUPPLIERS_INFO spi
        on s.SUPPLIER_ID = spi.SUPPLIER_ID
      left join COMMERCIAL_TENANT_BASIC_INFO t
        on spi.MERCHANT_ID = t.N_COMMERCIAL_TENANT_ID
      left join (select *
                   from (select PM.SHOP_CODE,
                                d.ASSESS_TYPE,
                                d.ASSESS_SCORE as SCORE
                           from ORDER_ASSESS_DETAIL d,
                                ORDER_ITEM          ot,
                                PRODUCTMAIN       pm,
                                PRODUCT_SKU       ps
                          where d.order_code = ot.Order_Code
                            and PS.PRODUCT_ID = pm.PRODUCT_ID(+)
                            AND OT.COMMODITY_SKU = ps.PRODUCT_SKU_CODE) pivot(AVG(score) for ASSESS_TYPE in('Assess_Type_one' as
                                                                                                            ASSESS_TYPE_ONE,
                                                                                                            'Assess_Type_two' as
                                                                                                            ASSESS_TYPE_TWO,
                                                                                                            'Assess_Type_three' as
                                                                                                            ASSESS_TYPE_THREE,
                                                                                                            'Assess_Type_four' as
                                                                                                            ASSESS_TYPE_FOUR))) so
        on to_char(s.SUPPLIER_ID) = so.SHOP_CODE
     where s.STATUS = 1;

  type dept_record is table of cur%rowtype;
  cur_result dept_record;

begin
  execute immediate 'truncate table SEARCH_SHOP_DATA_WHOLE';

  open cur;
  loop
    exit when cur%NOTFOUND;
    fetch cur bulk collect
      into cur_result limit 500;

    for i in 1 .. cur_result.count loop
      insert into SEARCH_SHOP_DATA_WHOLE
        (SHOP_ID,
         SUPPLIER_ID,
         SHOP_TYPE,
         SHOP_NAME,
         SHOP_TITLE,
         LOGO_PATH,
         INTRODUCE,
         SHOP_SITE,
         SHOP_SEO_KEY,
         AUDIT_TIME,
         MANAGE_BRAND,
         SHOP_LEVEL,
         DEFAULT_DOMAIN_URL,
         SERVICE_QQ,
         SERVICE_TYPE,
         CORPORATE_NAME,
         CORPORATE_LOCATION,
         PROVINCE,
         CITY,
         AREA,
         DESC_SCORE,
         SPEED_SCORE,
         DIST_SCORE,
         SALE_SCORE)
      values
        (cur_result(i).SHOP_ID,
         cur_result(i).SUPPLIER_ID,
         cur_result(i).SHOP_TYPE,
         cur_result(i).SHOP_NAME,
         cur_result(i).SHOP_TITLE,
         cur_result(i).LOGO_PATH,
         cur_result(i).INTRODUCE,
         cur_result(i).SHOP_SITE,
         cur_result(i).SHOP_SEO_KEY,
         cur_result(i).AUDIT_TIME,
         cur_result(i).MANAGE_BRAND,
         cur_result(i).SHOP_LEVEL,
         cur_result(i).DEFAULT_DOMAIN_URL,
         cur_result(i).SERVICE_QQ,
         cur_result(i).SERVICE_TYPE,
         cur_result(i).CORPORATE_NAME,
         cur_result(i).CORPORATE_LOCATION,
         cur_result(i).PROVINCE,
         cur_result(i).CITY,
         cur_result(i).AREA,
         cur_result(i).DESC_SCORE,
         cur_result(i).SPEED_SCORE,
         cur_result(i).DIST_SCORE,
         cur_result(i).SALE_SCORE);
    end loop;
    commit;
  end loop;
  close cur;
end;
