package com.kmzyc.search.app.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemDao {

  /**
   * 获取用户主菜单
   * 
   * @param userName
   * @return
   */
  public List<Map<String, Object>> getUpMenuList(@Param("userName") String userName);

  /**
   * 获取用户信息
   * 
   * @param userName
   * @return
   */
  public Map<String, Object> getUserInfo(@Param("userName") String userName);

  /**
   * 根据用户ID获取用户信息
   * 
   * @param userName
   * @return
   */
  public Map<String, Object> getUserInfoByUserId(@Param("userId") Integer userId);

  /**
   * 用户密码修改
   * 
   * @param userId
   * @param newPwd
   */
  public void updateUserPwd(@Param("userId") Integer userId, @Param("newPwd") String newPwd);

  /**
   * 根据用户ID获取左边菜单栏
   * 
   * @param userId
   * @param topMenuId
   * @return
   */
  public List<Map<String, Object>> getSubMenuListByUserId(@Param("userId") Integer userId,
      @Param("topMenuId") Integer topMenuId);
}
