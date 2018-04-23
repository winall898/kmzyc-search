package com.kmzyc.search.app.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kmzyc.search.app.dao.KeywordDao;
import com.kmzyc.search.app.model.KeywordVo;
import com.kmzyc.search.app.model.PageVo;
import com.kmzyc.search.app.service.KeywordService;

@Service("keywordService")
public class KeywordServiceImpl implements KeywordService {

  @Autowired
  private KeywordDao keywordDao;

  @Override
  public int getKeywordCount(String keyword) {

    if (StringUtils.isNotBlank(keyword)) {
      keyword = "%" + keyword.trim() + "%";
    }

    return keywordDao.getKeywordCount(keyword);
  }


  @Override
  public List<KeywordVo> getKeywordList(String keyword, PageVo page) {
    if (page == null) {

      return Collections.emptyList();
    }

    if (StringUtils.isNotBlank(keyword)) {
      keyword = "%" + keyword.trim() + "%";
    }

    return keywordDao.getKeywordList(keyword, page);
  }


  @Override
  public KeywordVo getKeywordById(Long id) {
    if (id == null) {

      return null;
    }

    return keywordDao.getKeywordById(id);
  }


  @Override
  public void addKeyword(KeywordVo keywordVo) {
    if (keywordVo == null) {

      return;
    }

    // 执行添加
    keywordDao.addKeyword(keywordVo);
  }


  @Override
  public void updateKeyword(KeywordVo keywordVo) {
    if (keywordVo == null) {

      return;
    }

    // 执行修改
    keywordDao.updateKeyword(keywordVo);
  }


  @Override
  public void deleteKeywordById(Long id) {
    if (id == null) {

      return;
    }

    // 执行删除
    keywordDao.deleteKeywordById(id);
  }


  @Override
  public void batchDeleteKeyword(List<Long> ids) {
    if (ids == null || ids.isEmpty()) {

      return;
    }

    // 执行删除
    keywordDao.batchDeleteKeyword(ids);
  }


  @Override
  public boolean isExistKeyword(String keyword, String id) {
    if (StringUtils.isBlank(keyword)) {

      return false;
    }

    int count = keywordDao.getCountByKeyword(keyword, id);

    if (count > 0) {

      return true;
    }

    return false;
  }
}
