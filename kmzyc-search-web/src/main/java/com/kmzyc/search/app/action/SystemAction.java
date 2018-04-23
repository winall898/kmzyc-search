package com.kmzyc.search.app.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kmzyc.promotion.app.util.MD5;
import com.kmzyc.search.app.service.SystemService;

/**
 * 系统管理类
 * 
 * @author
 */
@Controller
@RequestMapping("/sys")
@Scope("prototype")
public class SystemAction extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(SystemAction.class);

    @Resource
    private SystemService systemService;

    /**
     * 获取用户权限内头部Menu信息
     * 
     * @param @return String @exception
     */
    @RequestMapping("/getSysTop")
    @ResponseBody
    public ModelAndView getSysTop() {
        ModelAndView view = new ModelAndView("/pages/html/common/top.html");
        Object userName = getRequest().getSession().getAttribute("loginUserName");
        if (userName == null) {

            return view;
        }
        // 获取用户权限
        List<Map<String, Object>> upMenuList = systemService.getUpMenuList(userName.toString());
        // 菜单为空
        if (upMenuList == null || upMenuList.isEmpty()) {

            return view;
        }
        // 菜单列表
        view.addObject("upMenuList", upMenuList);
        // 返回菜单页面
        return view;
    }

    /**
     * 欢迎页面
     * 
     */
    @RequestMapping("/mainPage")
    @ResponseBody
    public ModelAndView mainPage() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/pages/html/common/sysHomeMain.html");
        return view;
    }

    /**
     * 操作员登录
     * 
     * @param @return String @exception
     */
    @RequestMapping("/loginSysUser")
    @ResponseBody
    public ModelAndView login() {
        Subject currUser = SecurityUtils.getSubject();

        // 将登陆的用户名存入session中
        String userName = currUser.getPrincipal().toString();
        getRequest().getSession().setAttribute("loginUserName", userName);

        ModelAndView view = new ModelAndView();
        if (null == userName) {
            view.setViewName("/WEB-INF/index.html");
            return view;
        }

        // 获取用户信息
        Map<String, Object> userInfo = systemService.getUserInfo(userName.toString());
        if (userInfo != null && !userInfo.isEmpty()) {
            // 用户ID存入session
            getRequest().getSession().setAttribute("loginUserId", userInfo.get("userId"));

            // 用户真实姓名存入session
            getRequest().getSession().setAttribute("loginUserReal", userInfo.get("userReal"));
        }

        // 将topMenuId存入session中
        String topMenuId = getRequest().getParameter("topMenuId");
        if (StringUtils.isNotBlank(topMenuId)) {
            getRequest().getSession().setAttribute("topMenuId", topMenuId);
        }

        view.setViewName("/WEB-INF/home.html");
        return view;
    }

    /**
     * 跳转用户修改密码页面
     * 
     */
    @RequestMapping("/toUpdateUserPwdPage")
    @ResponseBody
    public ModelAndView toUpdateUserPwdPage() throws Exception {

        // 返回页面
        ModelAndView view = new ModelAndView();
        Object userId = getRequest().getSession().getAttribute("loginUserId");
        if (userId == null) {

            logger.info("用户未登录！");
            view.setViewName("/WEB-INF/index.html");
            return view;
        }

        // 获取用户信息
        Map<String, Object> userInfo =
                systemService.getUserInfoByUserId(Integer.parseInt(userId.toString()));

        if (userInfo == null || userInfo.isEmpty()) {
            logger.info("用户未登录！");
            view.setViewName("/WEB-INF/index.html");
            return view;
        }

        // 用户信息
        view.addObject("userInfo", userInfo);
        view.setViewName("/pages/view/sys/sysUserPwdModify.ftl");

        return view;
    }

    /**
     * 保存用户密码
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping("/doSaveUserPwd")
    @ResponseBody
    public ModelAndView doSaveUserPwd() throws Exception {
        // 用户ID
        String userId = getRequest().getParameter("userId");
        // 原密码
        String userPwd = getRequest().getParameter("userPwd");
        // 新密码
        String newPwd1 = getRequest().getParameter("newPwd1");
        // 返回页面
        ModelAndView view = new ModelAndView();
        view.setViewName("/pages/view/sys/sysUserPwdModify.ftl");

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(userPwd)
                || StringUtils.isBlank(newPwd1)) {

            view.addObject("msg", "修改密码失败，参数错误！");
            return view;
        }

        // 获取用户信息
        Map<String, Object> userInfo =
                systemService.getUserInfoByUserId(Integer.parseInt(userId.toString()));

        if (userInfo == null || userInfo.isEmpty()) {

            view.addObject("msg", "修改密码失败，用户不存在！");
            return view;
        }


        // 校验密码
        if (!MD5.md5crypt(userPwd).equals(userInfo.get("userPwd").toString())) {

            view.addObject("userInfo", userInfo);
            view.addObject("msg", "修改密码失败，原密码错误！");
            return view;
        }

        // 执行密码修改
        systemService.updateUserPwd(Integer.parseInt(userId), MD5.md5crypt(newPwd1)); // 修改密码
        userInfo.put("userPwd", MD5.md5crypt(newPwd1));

        view.addObject("userInfo", userInfo);
        view.addObject("msg", "修改密码成功！");

        return view;
    }

    /**
     * 获取左侧导航菜单
     * 
     */
    @RequestMapping("/getLeftMenu")
    @ResponseBody
    public ModelAndView getLeftMenu() {

        // 返回页面
        ModelAndView view = new ModelAndView();

        // 登录用户ID
        Object userId = getRequest().getSession().getAttribute("loginUserId");
        Object userName = getRequest().getSession().getAttribute("loginUserName");
        if (userId == null || userName == null) {

            logger.info("用户未登录！");
            view.setViewName("/WEB-INF/index.html");
            return view;
        }

        // topMenuId
        String topMenuId = getRequest().getParameter("topMenuId");
        if (StringUtils.isBlank(topMenuId)) {
            Object menuId = getRequest().getSession().getAttribute("topMenuId");
            if (menuId == null) {
                // 获取用户权限
                List<Map<String, Object>> upMenuList =
                        systemService.getUpMenuList(userName.toString());
                for (Map<String, Object> menu : upMenuList) {
                    if (menu.get("menuName") != null
                            && "搜索系统".equals(menu.get("menuName").toString())) {
                        // 获取菜单ID
                        menuId = menu.get("menuId");
                        // 一级菜单ID存入session
                        getRequest().getSession().setAttribute("topMenuId", menu.get("menuId"));
                        break;
                    }
                }
            }
            topMenuId = menuId != null ? menuId.toString() : "-1";
        }

        // 左边菜单一级目录
        List<Map<String, Object>> menuList = systemService.getSubMenuListByUserId(
                Integer.parseInt(userId.toString()), Integer.valueOf(topMenuId));

        //
        if (menuList != null && !menuList.isEmpty()) {
            for (Map<String, Object> menu : menuList) {
                List<Map<String, Object>> subMenuList =
                        systemService.getSubMenuListByUserId(Integer.parseInt(userId.toString()),
                                Integer.valueOf(menu.get("menuId").toString()));
                // 子菜单
                menu.put("subMenuList", subMenuList);
            }
        }
        view.addObject("menuList", menuList);
        view.setViewName("/pages/html/common/left_tree_menu.html");

        return view;
    }
}
