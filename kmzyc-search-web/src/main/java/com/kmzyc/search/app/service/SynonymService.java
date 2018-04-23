package com.kmzyc.search.app.service;

import java.util.List;

import com.kmzyc.search.app.model.PageVo;
import com.kmzyc.search.app.model.SynonymVo;

/**
 * 词库管理接口
 * 
 * @author zhoulinhong
 * @since 20160831
 */
public interface SynonymService {
    /**
     * 获取同义词总数
     * 
     * @param userName
     * @return
     */
    public int getSynonymCount(SynonymVo synonymVo);

    /**
     * 获取同义词列表
     * 
     * @param synonym,page
     */
    public List<SynonymVo> getSynonymList(SynonymVo synonymVo, PageVo page);

    /**
     * 根据ID获取同义词
     * 
     * @param parseLong
     * @return
     */
    public SynonymVo getSynonymById(Long id);

    /**
     * 添加
     * 
     * @param synonymVo
     */
    public void addSynonym(SynonymVo synonymVo);

    /**
     * 修改
     * 
     * @param synonymVo
     */
    public void updateSynonym(SynonymVo synonymVo);

    /**
     * 删除同义词
     * 
     * @param parseLong
     */
    public void deleteSynonymById(Long id);

    /**
     * 批量删除
     * 
     * @param ids
     */
    public void batchDeleteSynonym(List<Long> ids);

    /**
     * 判断同义词是否已存在
     * 
     * @param synonym
     * @param id
     * @return
     */
    public boolean isExistSynonym(String synonym, String id);
}
