package com.kmzyc.search.app.service;

import java.util.List;
import java.util.Map;

/**
 * 系统服务接口
 * 
 * @author zhoulinhong
 * @since 20160831
 */
public interface SystemService {

  /**
   * 获取用户菜单
   * 
   * @param userName
   */
  public List<Map<String, Object>> getUpMenuList(String userName);

  /**
   * 获取用户信息
   * 
   * @param userName
   */
  public Map<String, Object> getUserInfo(String userName);

  /**
   * 根据用户ID获取用户信息
   * 
   * @param userName
   * @return
   */
  public Map<String, Object> getUserInfoByUserId(Integer userId);

  /**
   * 修改用户密码
   * 
   * @param userId
   * @param md5crypt
   */
  public void updateUserPwd(Integer userId, String newPwd);

  /**
   * 根据用户ID获取左边菜单栏
   * 
   * @param userId
   * @param topMenuId
   * @return
   */
  public List<Map<String, Object>> getSubMenuListByUserId(Integer userId, Integer topMenuId);
}
