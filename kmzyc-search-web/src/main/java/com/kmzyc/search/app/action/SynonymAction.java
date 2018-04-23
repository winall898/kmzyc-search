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
import com.kmzyc.search.app.model.PageVo;
import com.kmzyc.search.app.model.SynonymVo;
import com.kmzyc.search.app.service.SynonymService;
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
 * 同义词管理类
 * 
 * @author
 */
@Controller
@RequestMapping("/synonym")
@Scope("prototype")
public class SynonymAction extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(SynonymAction.class);

    /**
     * 分词导入模板
     */
    private static final String synonymImportFileName = "synonymTemplate.xls";

    /**
     * 刷新词库路径
     */
    private static final String refreshSynonymPath = "/synonym/reload";

    @Resource
    private SynonymService synonymService;

    /**
     * 进入同义词主页
     * 
     */
    @RequestMapping("/gotoSynonym")
    @ResponseBody
    public ModelAndView gotoSynonym() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/pages/view/synonym/synonymPage.ftl");
        return view;
    }

    /**
     * 获取同义词列表
     * 
     */
    @RequestMapping("/synonymlist")
    @ResponseBody
    public ModelAndView getSynonymList() {
        ModelAndView view = new ModelAndView();

        // 原始词
        String keyword = getRequest().getParameter("keyword");
        // 双向同义词
        String synonymWord = getRequest().getParameter("synonymWord");
        // 单向同义词
        String unidirWord = getRequest().getParameter("unidirWord");
        // 当前页
        String pageNo = getRequest().getParameter("pageNo");
        // 每面显示大小
        String pageSize = getRequest().getParameter("pageSize");
        // 分页对象
        PageVo page = new PageVo();

        // 搜索条件
        SynonymVo synonymVo = new SynonymVo();
        synonymVo.setKeyword(keyword);
        synonymVo.setSynonymWord(synonymWord);
        synonymVo.setUnidirWord(unidirWord);

        // 获取总数
        int count = synonymService.getSynonymCount(synonymVo);
        if (count <= 0) {
            view.addObject("page", page);
            view.setViewName("/pages/view/synonym/synonymListPage.ftl");
            return view;
        }

        // 分页参数处理
        packPage(page, pageNo, pageSize, count);

        List<SynonymVo> synonymList = synonymService.getSynonymList(synonymVo, page);
        if (synonymList != null && !synonymList.isEmpty()) {

            view.addObject("synonymList", synonymList);
        }

        view.addObject("page", page);
        view.setViewName("/pages/view/synonym/synonymListPage.ftl");
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
     * 进入添加同义词页面
     * 
     */
    @RequestMapping("/gotoAddSynonymPage")
    @ResponseBody
    public ModelAndView gotoAddSynonymPage() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/pages/view/synonym/synonymAddPage.ftl");
        return view;
    }

    /**
     * 进入修改同义词页面
     * 
     */
    @RequestMapping("/gotoUpateSynonymPage")
    @ResponseBody
    public ModelAndView gotoUpateSynonymPage() {
        ModelAndView view = new ModelAndView();
        // 同义词ID
        String id = getRequest().getParameter("id");
        SynonymVo synonymVo = synonymService.getSynonymById(Long.parseLong(id));
        if (synonymVo != null) {
            view.addObject("synonymVo", synonymVo);
        }
        view.setViewName("/pages/view/synonym/synonymUpdatePage.ftl");
        return view;
    }

    /**
     * 保存同义词(添加/修改)
     * 
     */
    @RequestMapping("/doSaveSynonym")
    @ResponseBody
    public String doSaveSynonym() {
        // 同义词ID
        String id = getRequest().getParameter("id");
        // 原始词
        String keyword = getRequest().getParameter("keyword");
        // 双向同义词
        String synonymWord = getRequest().getParameter("synonymWord");
        // 单向同义词
        String unidirWord = getRequest().getParameter("unidirWord");
        // 说明
        String description = getRequest().getParameter("description");
        // 用户名称
        Object userName = getRequest().getSession().getAttribute("loginUserName");
        // 返回结果
        Map<String, Object> resultMap = Maps.newHashMap();
        if (userName == null) {
            resultMap.put("is_success", false);
            resultMap.put("msg", "保存失败，用户未登录！");
            logger.error("同义词保存失败，用户未登录！");
            return JSONObject.toJSONString(resultMap);
        }
        if (StringUtils.isBlank(keyword)) {
            resultMap.put("is_success", false);
            resultMap.put("msg", "保存失败，原始词不能为空！");
            logger.error("同义词保存失败，原始词不能为空！");
            return JSONObject.toJSONString(resultMap);
        }
        if (StringUtils.isBlank(synonymWord) && StringUtils.isBlank(unidirWord)) {
            resultMap.put("is_success", false);
            resultMap.put("msg", "保存失败，双向同义词和单向同义词不能同时为空！");
            logger.error("同义词保存失败，双向同义词和单向同义词不能同时为空！");
            return JSONObject.toJSONString(resultMap);
        }

        // 判断添加的原始词是否已存在
        boolean flag = synonymService.isExistSynonym(keyword.trim(), id);
        if (flag) {
            resultMap.put("is_success", false);
            resultMap.put("msg", "保存失败，原始词已存在！");
            logger.error("同义词保存失败，原始词已存在！");
            return JSONObject.toJSONString(resultMap);
        }

        // 同义词对象
        SynonymVo synonymVo = new SynonymVo();
        synonymVo.setKeyword(keyword.trim());
        synonymVo.setSynonymWord(synonymWord);
        synonymVo.setUnidirWord(unidirWord);
        synonymVo.setDescription(description);
        Date now = new Date();
        if (StringUtils.isBlank(id)) {
            synonymVo.setCreater(userName.toString());
            synonymVo.setCreateTime(now);
            synonymVo.setUpdater(userName.toString());
            synonymVo.setUpdateTime(now);
            // 添加
            synonymService.addSynonym(synonymVo);
            resultMap.put("is_success", true);
            resultMap.put("msg", "添加成功！");
        } else {
            synonymVo.setId(Long.parseLong(id));
            synonymVo.setUpdater(userName.toString());
            synonymVo.setUpdateTime(now);
            // 修改
            synonymService.updateSynonym(synonymVo);
            resultMap.put("is_success", true);
            resultMap.put("msg", "修改成功！");
        }

        return JSONObject.toJSONString(resultMap);
    }

    /**
     * 删除同义词
     */
    @RequestMapping("/deleteSynonym")
    @ResponseBody
    public String deleteSynonym() {
        // 同义词ID
        String id = getRequest().getParameter("id");
        // 返回结果
        Map<String, Object> resultMap = Maps.newHashMap();
        if (StringUtils.isBlank(id)) {
            resultMap.put("msg", "删除失败，数据不存在！");
            logger.error("同义词删除失败，数据不存在！");
            return JSONObject.toJSONString(resultMap);
        }

        // 删除
        synonymService.deleteSynonymById(Long.parseLong(id));
        resultMap.put("msg", "删除成功！");

        return JSONObject.toJSONString(resultMap);
    }

    @RequestMapping("/batchDeleteSynonym")
    @ResponseBody
    public String batchDeleteSynonym() {
        // 同义词ID
        String idStr = getRequest().getParameter("idStr");
        // 返回结果
        Map<String, Object> resultMap = Maps.newHashMap();
        if (StringUtils.isBlank(idStr)) {
            resultMap.put("msg", "删除失败，数据不存在！");
            logger.error("同义词删除失败，数据不存在！");
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
        synonymService.batchDeleteSynonym(ids);
        resultMap.put("msg", "删除成功！");

        return JSONObject.toJSONString(resultMap);
    }

    /**
     * 刷新同义词库
     * 
     * @author KM
     * @return
     */
    @RequestMapping("/refreshSynonymDict")
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
                    response = HttpClientUtils.get(address + refreshSynonymPath);
                } catch (Exception e) {
                    logger.error("refreshSynonymDict exception !", e);
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
    @RequestMapping("/gotoImportSynonymPage")
    @ResponseBody
    public ModelAndView gotoImportSynonymPage() {

        ModelAndView view = new ModelAndView();
        view.setViewName("/pages/view/synonym/synonymImportPage.ftl");
        return view;

    }

    /**
     * 导入分词数据(添加/修改)
     * 
     */
    @RequestMapping("/importSynonym")
    @ResponseBody
    public String importSynonym(@RequestParam(value = "synonymImportFile") MultipartFile importFile,
            HttpServletRequest request, HttpServletResponse response) {

        logger.info(" == start import synonym excel data==");
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
                String synonym = sheet.getCell(0, i).getContents();
                // 对为空和已存在的数据跳过
                if (StringUtils.isBlank(synonym)
                        || (synonymService.isExistSynonym(synonym.trim(), null))) {
                    continue;
                }
                String synonymWord = sheet.getCell(1, i).getContents();
                String unidirWord = sheet.getCell(2, i).getContents();
                if (StringUtils.isBlank(synonymWord) && StringUtils.isBlank(unidirWord)) {
                    continue;
                }

                SynonymVo synonymVo = new SynonymVo();
                synonymVo.setKeyword(synonym.trim());
                synonymVo.setSynonymWord(synonymWord.trim());
                synonymVo.setUnidirWord(unidirWord.trim());
                synonymVo.setDescription(sheet.getCell(3, i).getContents());
                synonymVo.setCreater(userName.toString());
                synonymVo.setCreateTime(now);
                synonymVo.setUpdater(userName.toString());
                synonymVo.setUpdateTime(now);
                // 添加
                synonymService.addSynonym(synonymVo);
            }
        } catch (Exception e) {
            logger.error(" import synonym data exception !", e);
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
    @RequestMapping("/downloadSynonymTemp")
    public String downloadSynonymTemp(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + synonymImportFileName);
        WritableWorkbook rwb = null;
        try {
            rwb = Workbook.createWorkbook(response.getOutputStream());
            WritableSheet sheet = rwb.createSheet("synonym", 0);

            // 设置行高度
            sheet.getSettings().setDefaultRowHeight(320);
            sheet.setRowView(0, 320);
            // 固定表头第一行
            sheet.getSettings().setVerticalFreeze(1);
            // 设置列宽度
            sheet.setColumnView(0, 16);
            sheet.setColumnView(1, 16);
            sheet.setColumnView(2, 16);
            sheet.setColumnView(3, 30);

            // 设置单元格样式，背景色，居中，边框
            WritableCellFormat format = new WritableCellFormat();
            format.setBackground(Colour.LIGHT_GREEN);
            format.setAlignment(Alignment.CENTRE);
            format.setBorder(Border.ALL, BorderLineStyle.THIN);
            // 表头内容
            sheet.addCell(new Label(0, 0, "关键词（必填）", format));
            sheet.addCell(new Label(1, 0, "双向同义词", format));
            sheet.addCell(new Label(2, 0, "单项同义词", format));
            sheet.addCell(new Label(3, 0, "备注", format));
            rwb.write();
        } catch (Exception e) {
            logger.error("Get synonym import template file exception ！ ", e);
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
