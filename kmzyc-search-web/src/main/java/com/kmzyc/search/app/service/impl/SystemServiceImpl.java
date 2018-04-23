package com.kmzyc.search.app.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kmzyc.search.app.dao.SystemDao;
import com.kmzyc.search.app.service.SystemService;

@Service("systemService")
public class SystemServiceImpl implements SystemService {

  @Autowired
  private SystemDao systemDao;

  @Override
  public List<Map<String, Object>> getUpMenuList(String userName) {
    if (StringUtils.isBlank(userName)) {

      return Collections.emptyList();
    }

    return systemDao.getUpMenuList(userName);
  }

  @Override
  public Map<String, Object> getUserInfo(String userName) {
    if (StringUtils.isBlank(userName)) {

      return Collections.emptyMap();
    }

    return systemDao.getUserInfo(userName);
  }

  @Override
  public Map<String, Object> getUserInfoByUserId(Integer userId) {
    if (null == userId) {

      return Collections.emptyMap();
    }

    return systemDao.getUserInfoByUserId(userId);
  }

  @Override
  public void updateUserPwd(Integer userId, String newPwd) {
    if (null == userId || StringUtils.isBlank(newPwd)) {

      return;
    }

    systemDao.updateUserPwd(userId, newPwd);
  }

  @Override
  public List<Map<String, Object>> getSubMenuListByUserId(Integer userId, Integer topMenuId) {
    if (null == userId || null == topMenuId) {

      return Collections.emptyList();
    }

    return systemDao.getSubMenuListByUserId(userId, topMenuId);
  }

}
