package com.kmzyc.search.app.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kmzyc.search.app.dao.SynonymDao;
import com.kmzyc.search.app.model.PageVo;
import com.kmzyc.search.app.model.SynonymVo;
import com.kmzyc.search.app.service.SynonymService;

@Service("synonymService")
public class SynonymServiceImpl implements SynonymService {

  @Autowired
  private SynonymDao synonymDao;

  @Override
  public int getSynonymCount(SynonymVo synonymVo) {
    if (synonymVo == null) {

      return 0;
    }

    // 原始词
    if (StringUtils.isNotBlank(synonymVo.getKeyword())) {
      synonymVo.setKeyword("%" + synonymVo.getKeyword().trim() + "%");
    }
    // 双向同义词
    if (StringUtils.isNotBlank(synonymVo.getKeyword())) {
      synonymVo.setKeyword("%" + synonymVo.getKeyword().trim() + "%");
    }
    // 单向同义词
    if (StringUtils.isNotBlank(synonymVo.getKeyword())) {
      synonymVo.setKeyword("%" + synonymVo.getKeyword().trim() + "%");
    }

    return synonymDao.getSynonymCount(synonymVo);
  }


  @Override
  public List<SynonymVo> getSynonymList(SynonymVo synonymVo, PageVo page) {
    if (page == null || synonymVo == null) {

      return Collections.emptyList();
    }

    // 原始词
    if (StringUtils.isNotBlank(synonymVo.getKeyword())) {
      synonymVo.setKeyword("%" + synonymVo.getKeyword().trim() + "%");
    }
    // 双向同义词
    if (StringUtils.isNotBlank(synonymVo.getKeyword())) {
      synonymVo.setKeyword("%" + synonymVo.getKeyword().trim() + "%");
    }
    // 单向同义词
    if (StringUtils.isNotBlank(synonymVo.getKeyword())) {
      synonymVo.setKeyword("%" + synonymVo.getKeyword().trim() + "%");
    }

    return synonymDao.getSynonymList(synonymVo, page);
  }


  @Override
  public SynonymVo getSynonymById(Long id) {
    if (id == null) {

      return null;
    }

    return synonymDao.getSynonymById(id);
  }


  @Override
  public void addSynonym(SynonymVo synonymVo) {
    if (synonymVo == null) {

      return;
    }

    // 执行添加
    synonymDao.addSynonym(synonymVo);
  }


  @Override
  public void updateSynonym(SynonymVo synonymVo) {
    if (synonymVo == null) {

      return;
    }

    // 执行修改
    synonymDao.updateSynonym(synonymVo);
  }


  @Override
  public void deleteSynonymById(Long id) {
    if (id == null) {

      return;
    }

    // 执行删除
    synonymDao.deleteSynonymById(id);
  }


  @Override
  public void batchDeleteSynonym(List<Long> ids) {
    if (ids == null || ids.isEmpty()) {

      return;
    }

    // 执行删除
    synonymDao.batchDeleteSynonym(ids);
  }


  @Override
  public boolean isExistSynonym(String keyword, String id) {
    if (StringUtils.isBlank(keyword)) {

      return false;
    }

    int count = synonymDao.getCountByKeyword(keyword, id);

    if (count > 0) {

      return true;
    }

    return false;
  }
}
