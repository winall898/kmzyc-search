package com.kmzyc.search.facade.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kmzyc.search.facade.vo.Category;

public class CategoryUtil {

  private static final Logger LOG = LoggerFactory.getLogger(CategoryUtil.class);

  private final MemCache memCache =
      (MemCache) ApplicationContextUtil.getApplicationContext().getBean("memCache");

  private String getRootId(String id, Map<String, Map<String, String>> cateMap) {
    Map<String, String> currCate = cateMap.get(id);
    if (null == currCate || currCate.isEmpty()) { // 新增类目缓存内没有
      return null;
    }
    String parentId = currCate.get("PARENT_ID");
    if ("0".equals(parentId))
      return currCate.get("CATEGORY_ID");
    return getRootId(parentId, cateMap);
  }

  /**
   * 获取所有2，3运营类目
   * 
   * @param channel
   * @return
   */
  public List<Category> getFixCategoryList(String channel, int oid) {
    List<Category> result = new ArrayList<Category>();
    if (StringUtils.isBlank(channel)) {
      return result;
    }

    String currId = oid + "";
    Map<String, List<Map<String, String>>> cateGroup = memCache.getCategoryGroupByLevel(channel);
    if (null == cateGroup || cateGroup.isEmpty()) {
      LOG.warn("无法获取运营类目分组。");
      return result;
    }
    Map<String, Map<String, String>> cateMap = memCache.getSiteCategory(channel);
    if (null == cateMap || cateMap.isEmpty()) {
      LOG.warn("无法获取站点内运营类目。");
      return result;
    }
    String firstLevelCateId = getRootId(currId, cateMap); // 获取当前搜索的运营类目根目录ID
    if (null == firstLevelCateId) {
      LOG.warn("无法获取当前搜索的运营类目根目录ID。");
      return result;
    }
    List<Map<String, String>> secondLevelCates = cateGroup.get("2");
    if (null == secondLevelCates || secondLevelCates.isEmpty()) {
      LOG.warn("无法获取站点内2级运营类目。");
      return result;
    }
    List<Map<String, String>> thridLevelCates = cateGroup.get("3");
    if (null == thridLevelCates || thridLevelCates.isEmpty()) {
      LOG.warn("无法获取站点内3级运营类目。");
      return result;
    }

    int i = 0;
    for (Map<String, String> secondCate : secondLevelCates) {

      boolean selected = false;
      String parentId = secondCate.get("PARENT_ID");
      if (firstLevelCateId.equals(parentId)) { // 获取该类目所属2级运营类目
        i++;
        int pid = MapUtils.getIntValue(secondCate, "CATEGORY_ID");
        String pname = MapUtils.getString(secondCate, "CATEGORY_NAME");
        if (StringUtils.isBlank(pname)) {
          continue;
        }
        Category parent = new Category();
        parent.setId(pid);
        parent.setName(pname);
        parent.setCode(pid + "");
        if (firstLevelCateId.equals(currId) && i == 1) { // 如果搜索是一级类目，第一个二级类目前端展示时展开
          selected = true;
        }
        List<Category> children = new ArrayList<Category>();

        for (Map<String, String> thirdCate : thridLevelCates) {// 获取2级运营类目下的3级运营类目
          int childParentId = MapUtils.getIntValue(thirdCate, "PARENT_ID");
          if (pid == childParentId) {
            int childId = MapUtils.getIntValue(thirdCate, "CATEGORY_ID");
            String cname = MapUtils.getString(thirdCate, "CATEGORY_NAME");
            if (StringUtils.isBlank(cname)) {
              continue;
            }
            Category child = new Category();
            child.setId(childId);
            child.setName(cname);
            child.setCode(childId + "");
            children.add(child);
            if (childId == oid) { // 三级所在的父级二级类目展开
              selected = true;
            }
          }
        }
        parent.setChildren(children);

        if (oid == pid)
          selected = true;
        parent.setSelected(selected);
        result.add(parent);
        selected = false; // 重置
      }
    }
    return result;
  }
}
