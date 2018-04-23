package com.kmzyc.search.app.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kmzyc.search.app.config.Configuration;
import com.kmzyc.search.app.model.KeywordVo;
import com.kmzyc.search.app.model.PageVo;
import com.kmzyc.search.app.service.KeywordService;
import com.kmzyc.util.HttpClientUtils;

import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 词库管理类
 * 
 * @author
 */
@Controller
@RequestMapping("/keyword")
@Scope("prototype")
public class KeywordAction extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(KeywordAction.class);

    /**
     * 分词导入模板
     */
    private static final String keywordImportFileName = "keywordTemplate.xls";

    /**
     * 刷新词库的访问路径
     */
    private static final String refreshKeywordPath = "/IKAnalysis/DicReload/keyword";

    @Resource
    private KeywordService keywordService;

    /**
     * 进入词库主页
     * 
     */
    @RequestMapping("/gotoKeyword")
    @ResponseBody
    public ModelAndView gotoKeyword() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/pages/view/keyword/keywordPage.ftl");
        return view;
    }

    /**
     * 获取分词列表
     * 
     */
    @RequestMapping("/keywordlist")
    @ResponseBody
    public ModelAndView getKeywordList() {
        ModelAndView view = new ModelAndView();

        // 搜索关键字
        String keyword = getRequest().getParameter("keyword");
        // 当前页
        String pageNo = getRequest().getParameter("pageNo");
        // 每面显示大小
        String pageSize = getRequest().getParameter("pageSize");
        // 分页对象
        PageVo page = new PageVo();
        // 获取总数
        int count = keywordService.getKeywordCount(keyword);
        if (count <= 0) {
            view.addObject("page", page);
            view.setViewName("/pages/view/keyword/keywordListPage.ftl");
            return view;
        }

        // 分页参数处理
        packPage(page, pageNo, pageSize, count);

        List<KeywordVo> keywordList = keywordService.getKeywordList(keyword, page);
        if (keywordList != null && !keywordList.isEmpty()) {

            view.addObject("keywordList", keywordList);
        }

        view.addObject("page", page);
        view.setViewName("/pages/view/keyword/keywordListPage.ftl");
        return view;
    }

    /**
     * 分页参数组装
     */
    private void packPage(PageVo page, String pageNo, String pageSize, int count) {
        // 分页信息
        if (StringUtils.isNotBlank(pageSize) && Integer.parseInt(pageSize) > 0) {
            page.setPageSize(Integer.parseInt(pageSize));
        }
        if (StringUtils.isNotBlank(pageNo) && Integer.parseInt(pageNo) > 0) {
            page.setPageNo(Integer.parseInt(pageNo));
        }
        // 总页数
        int pageCount;
        if (count % page.getPageSize() == 0) {
            pageCount = count / page.getPageSize();
        } else {
            pageCount = count / page.getPageSize() + 1;
        }
        if (pageCount < page.getPageNo()) {
            page.setPageNo(pageCount);
        }
        page.setPageCount(pageCount);
        page.setRecordCount(count);
        page.setStartIndex((page.getPageNo() - 1) * page.getPageSize());
        page.setEndIndex(page.getPageNo() * page.getPageSize());
    }

    /**
     * 进入添加分词页面
     * 
     */
    @RequestMapping("/gotoAddKeywordPage")
    @ResponseBody
    public ModelAndView gotoAddKeywordPage() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/pages/view/keyword/keywordAddPage.ftl");
        return view;
    }

    /**
     * 进入修改分词页面
     * 
     */
    @RequestMapping("/gotoUpateKeywordPage")
    @ResponseBody
    public ModelAndView gotoUpateKeywordPage() {
        ModelAndView view = new ModelAndView();
        // 分词ID
        String id = getRequest().getParameter("id");
        KeywordVo keywordVo = keywordService.getKeywordById(Long.parseLong(id));
        if (keywordVo != null) {
            view.addObject("keywordVo", keywordVo);
        }
        view.setViewName("/pages/view/keyword/keywordUpdatePage.ftl");
        return view;
    }

    /**
     * 保存分词(添加/修改)
     * 
     */
    @RequestMapping("/doSaveKeyword")
    @ResponseBody
    public String doSaveKeyword() {
        // 分词ID
        String id = getRequest().getParameter("id");
        // 分词
        String keyword = getRequest().getParameter("keyword");
        // 说明
        String description = getRequest().getParameter("description");
        // 用户名称
        Object userName = getRequest().getSession().getAttribute("loginUserName");
        // 返回结果
        Map<String, Object> resultMap = Maps.newHashMap();
        if (userName == null) {
            resultMap.put("is_success", false);
            resultMap.put("msg", "保存失败，用户未登录！");
            logger.error("分词保存失败，用户未登录！");
            return JSONObject.toJSONString(resultMap);
        }
        if (StringUtils.isBlank(keyword)) {
            resultMap.put("is_success", false);
            resultMap.put("msg", "保存失败，分词不能为空！");
            logger.error("分词保存失败，分词不能为空！");
            return JSONObject.toJSONString(resultMap);
        }

        // 判断添加的分词是否已存在
        boolean flag = keywordService.isExistKeyword(keyword.trim(), id);
        if (flag) {
            resultMap.put("is_success", false);
            resultMap.put("msg", "保存失败，分词已存在！");
            logger.error("分词保存失败，分词已存在！");
            return JSONObject.toJSONString(resultMap);
        }

        // 分词对象
        KeywordVo keywordVo = new KeywordVo();
        keywordVo.setKeyword(keyword.trim());
        keywordVo.setDescription(description);
        Date now = new Date();
        if (StringUtils.isBlank(id)) {
            keywordVo.setCreater(userName.toString());
            keywordVo.setCreateTime(now);
            keywordVo.setUpdater(userName.toString());
            keywordVo.setUpdateTime(now);
            // 添加
            keywordService.addKeyword(keywordVo);
            resultMap.put("is_success", true);
            resultMap.put("msg", "添加成功！");
        } else {
            keywordVo.setId(Long.parseLong(id));
            keywordVo.setUpdater(userName.toString());
            keywordVo.setUpdateTime(now);
            // 修改
            keywordService.updateKeyword(keywordVo);
            resultMap.put("is_success", true);
            resultMap.put("msg", "修改成功！");
        }

        return JSONObject.toJSONString(resultMap);
    }

    /**
     * 删除分词
     */
    @RequestMapping("/deleteKeyword")
    @ResponseBody
    public String deleteKeyword() {
        // 分词ID
        String id = getRequest().getParameter("id");
        // 返回结果
        Map<String, Object> resultMap = Maps.newHashMap();
        if (StringUtils.isBlank(id)) {
            resultMap.put("msg", "删除失败，数据不存在！");
            logger.error("分词删除失败，数据不存在！");
            return JSONObject.toJSONString(resultMap);
        }

        // 删除
        keywordService.deleteKeywordById(Long.parseLong(id));
        resultMap.put("msg", "删除成功！");

        return JSONObject.toJSONString(resultMap);
    }

    @RequestMapping("/batchDeleteKeyword")
    @ResponseBody
    public String batchDeleteKeyword() {
        // 分词ID
        String idStr = getRequest().getParameter("idStr");
        // 返回结果
        Map<String, Object> resultMap = Maps.newHashMap();
        if (StringUtils.isBlank(idStr)) {
            resultMap.put("msg", "删除失败，数据不存在！");
            logger.error("分词删除失败，数据不存在！");
            return JSONObject.toJSONString(resultMap);
        }

        // ID集合
        String[] idArr = idStr.split(",");
        List<Long> ids = Lists.newArrayList();
        for (String id : idArr) {
            if (StringUtils.isNotBlank(id)) {
                ids.add(Long.parseLong(id));
            }
        }

        // 删除
        keywordService.batchDeleteKeyword(ids);
        resultMap.put("msg", "删除成功！");

        return JSONObject.toJSONString(resultMap);
    }

    /**
     * 刷新关键词库
     * 
     * @author KM
     * @return
     */
    @RequestMapping("/refreshKeywordDict")
    @ResponseBody
    public String refreshKeywordDict() {
        // 返回结果
        Map<String, Object> resultMap = Maps.newHashMap();
        String esNodeAddress = Configuration.getString("es.node.httpAddress");
        if (StringUtils.isBlank(esNodeAddress)) {
            resultMap.put("is_success", false);
            resultMap.put("msg", "刷新失败，ES集群访问信息未指定");
        } else {
            String response = null;
            String[] addresses = esNodeAddress.split("[,;]");
            for (String address : addresses) {
                try {
                    response = HttpClientUtils.get(address + refreshKeywordPath);
                } catch (Exception e) {
                    logger.error("refreshKeywordDict exception !", e);
                    resultMap.put("is_success", false);
                    resultMap.put("msg", "刷新失败，访问es节点 " + address + " 异常！");
                    return JSONObject.toJSONString(resultMap);
                }
                JSONObject json = JSONObject.parseObject(response);
                String errorDesc = json.getString("errorDesc");
                if (StringUtils.isNotBlank(errorDesc)) {
                    resultMap.put("is_success", false);
                    resultMap.put("msg", errorDesc);
                    return JSONObject.toJSONString(resultMap);
                }
            }
            resultMap.put("is_success", true);
            resultMap.put("msg", "词库刷新成功！");
        }
        return JSONObject.toJSONString(resultMap);
    }


    /**
     * 进入导入页面
     * 
     * @return
     */
    @RequestMapping("/gotoImportKeywordPage")
    @ResponseBody
    public ModelAndView gotoImportKeywordPage() {

        ModelAndView view = new ModelAndView();
        view.setViewName("/pages/view/keyword/keywordImportPage.ftl");
        return view;

    }

    /**
     * 导入分词数据(添加/修改)
     * 
     */
    @RequestMapping("/importKeyword")
    @ResponseBody
    public String importKeyword(@RequestParam(value = "keywordImportFile") MultipartFile importFile,
            HttpServletRequest request, HttpServletResponse response) {

        logger.info(" == start import keyword excel data==");
        // 返回结果
        Map<String, Object> resultMap = Maps.newHashMap();
        // 用户名称
        Object userName = getRequest().getSession().getAttribute("loginUserName");
        Date now = new Date();
        Workbook rwb = null;
        InputStream in = null;
        // 获取Excel文件对象
        try {
            in = importFile.getInputStream();
            rwb = Workbook.getWorkbook(in);
            // 获取文件的指定工作表 默认的第一个
            Sheet sheet = rwb.getSheet(0);
            // 行数(表头的目录不需要，从1开始)
            for (int i = 1; i < sheet.getRows(); i++) {
                String keyword = sheet.getCell(0, i).getContents();
                // 对为空和已存在的数据跳过
                if (StringUtils.isBlank(keyword)
                        || (keywordService.isExistKeyword(keyword.trim(), null))) {
                    continue;
                }
                KeywordVo keywordVo = new KeywordVo();
                keywordVo.setKeyword(keyword.trim());
                keywordVo.setDescription(sheet.getCell(1, i).getContents());
                keywordVo.setCreater(userName.toString());
                keywordVo.setCreateTime(now);
                keywordVo.setUpdater(userName.toString());
                keywordVo.setUpdateTime(now);
                // 添加
                keywordService.addKeyword(keywordVo);
            }
        } catch (Exception e) {
            logger.error(" import keyword data exception !", e);
            resultMap.put("is_success", false);
            resultMap.put("msg", "导入失败！");
            return JSONObject.toJSONString(resultMap);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("InputStream close exception!", e);
                }
            }
        }
        resultMap.put("is_success", true);
        resultMap.put("msg", "导入成功！");
        return JSONObject.toJSONString(resultMap);
    }

    /**
     * 获取导入模板文件
     * 
     * @author KM
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/downloadKeywordTemp")
    public String downloadKeywordTemp(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + keywordImportFileName);
        WritableWorkbook rwb = null;
        try {
            rwb = Workbook.createWorkbook(response.getOutputStream());
            WritableSheet sheet = rwb.createSheet("keyword", 0);

            // 设置行高度
            sheet.getSettings().setDefaultRowHeight(320);
            sheet.setRowView(0, 320);
            // 固定表头第一行
            sheet.getSettings().setVerticalFreeze(1);
            // 设置列宽度
            sheet.setColumnView(0, 16);
            sheet.setColumnView(1, 30);

            // 设置单元格样式，背景色，居中，边框
            WritableCellFormat format = new WritableCellFormat();
            format.setBackground(Colour.LIGHT_GREEN);
            format.setAlignment(Alignment.CENTRE);
            format.setBorder(Border.ALL, BorderLineStyle.THIN);
            // 表头内容
            sheet.addCell(new Label(0, 0, "关键词（必填）", format));
            sheet.addCell(new Label(1, 0, "备注", format));
            rwb.write();
        } catch (Exception e) {
            logger.error("Get keyword import template file exception ！ ", e);
        } finally {
            if (rwb != null) {
                try {
                    rwb.close();
                } catch (Exception e) {
                    logger.error("close WritableWorkbook outStream exception !", e);
                }
            }
        }
        return null;
    }

}
