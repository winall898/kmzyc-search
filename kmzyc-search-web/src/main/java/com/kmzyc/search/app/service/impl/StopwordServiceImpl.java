package com.kmzyc.search.app.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kmzyc.search.app.dao.StopwordDao;
import com.kmzyc.search.app.model.PageVo;
import com.kmzyc.search.app.model.StopwordVo;
import com.kmzyc.search.app.service.StopwordService;

@Service("stopwordService")
public class StopwordServiceImpl implements StopwordService {

  @Autowired
  private StopwordDao stopwordDao;

  @Override
  public int getStopwordCount(String stopword) {

    if (StringUtils.isNotBlank(stopword)) {
      stopword = "%" + stopword.trim() + "%";
    }

    return stopwordDao.getStopwordCount(stopword);
  }


  @Override
  public List<StopwordVo> getStopwordList(String stopword, PageVo page) {
    if (page == null) {

      return Collections.emptyList();
    }

    if (StringUtils.isNotBlank(stopword)) {
      stopword = "%" + stopword.trim() + "%";
    }

    return stopwordDao.getStopwordList(stopword, page);
  }


  @Override
  public StopwordVo getStopwordById(Long id) {
    if (id == null) {

      return null;
    }

    return stopwordDao.getStopwordById(id);
  }


  @Override
  public void addStopword(StopwordVo stopwordVo) {
    if (stopwordVo == null) {

      return;
    }

    // 执行添加
    stopwordDao.addStopword(stopwordVo);
  }


  @Override
  public void updateStopword(StopwordVo stopwordVo) {
    if (stopwordVo == null) {

      return;
    }

    // 执行修改
    stopwordDao.updateStopword(stopwordVo);
  }


  @Override
  public void deleteStopwordById(Long id) {
    if (id == null) {

      return;
    }

    // 执行删除
    stopwordDao.deleteStopwordById(id);
  }


  @Override
  public void batchDeleteStopword(List<Long> ids) {
    if (ids == null || ids.isEmpty()) {

      return;
    }

    // 执行删除
    stopwordDao.batchDeleteStopword(ids);
  }


  @Override
  public boolean isExistStopword(String stopword, String id) {
    if (StringUtils.isBlank(stopword)) {

      return false;
    }

    int count = stopwordDao.getCountByStopword(stopword, id);

    if (count > 0) {

      return true;
    }

    return false;
  }
}
