package com.kmzyc.search.app.util;

import java.net.InetAddress;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.SearchHits;

import com.alibaba.fastjson.JSONObject;
import com.kmzyc.search.app.config.Configuration;


/**
 * 索引客户端操作工具类
 * 
 * @author zhoulinhong
 * @since 20160511
 */
public class IndexClientUtil {

    private static final Logger logger = Logger.getLogger(IndexClientUtil.class);

    private static IndexClientUtil indexClientUtil = null;

    private TransportClient transportClient = null;

    private IndexClientUtil() {

        // 索引客户端实例处理
        if (null == transportClient) {

            Settings settings =
                    Settings.settingsBuilder()
                            // 指定集群名称
                            .put("cluster.name",
                                    Configuration.getContextProperty("es.cluster.name"))
                            // 探测集群中机器状态
                            .put("client.transport.sniff", true).build();
            // 集群地址
            String clusterAddress = Configuration.getContextProperty("es.node.address");
            // 集群节点地址
            String[] addresses = clusterAddress.split("[;,]");
            // 索引客户端构建
            transportClient = TransportClient.builder().settings(settings).build();
            // 索引客户端地址配置
            for (String node : addresses) {
                String[] nodes = node.split(":");
                try {
                    transportClient.addTransportAddress(new InetSocketTransportAddress(
                            InetAddress.getByName(nodes[0]), Integer.valueOf(nodes[1])));
                } catch (Exception e) {
                    logger.error("客户端实例发生异常，异常节点：" + node, e);
                }
            }
        }
    }

    /**
     * 获取单例实例
     * 
     * @author zhoulinhong
     * @since 20160511
     * @return
     */
    public static IndexClientUtil getInstance() {
        if (null == indexClientUtil) {

            indexClientUtil = new IndexClientUtil();
        }

        return indexClientUtil;
    }

    public BulkRequestBuilder getBulkBuilder() {

        return transportClient.prepareBulk();
    }

    public GetRequestBuilder getGetBuilder(String indexName, String indexType, String docId) {

        return transportClient.prepareGet(indexName, indexType, docId);
    }

    public IndexRequestBuilder getIndexBuilder(String indexName, String indexType, String docId) {

        return transportClient.prepareIndex(indexName, indexType, docId);
    }

    public DeleteRequestBuilder getDeleteBuilder(String indexName, String indexType, String docId) {

        return transportClient.prepareDelete(indexName, indexType, docId);
    }

    /**
     * 新增索引
     * 
     * @param indexName
     * @param indexType
     * @param index
     * @author zhoulinhong
     * @since 20160511
     */
    public void addIndex(String indexName, String indexType, String indexId, JSONObject doc) {

        if (StringUtils.isBlank(indexName) || StringUtils.isBlank(indexType)
                || StringUtils.isBlank(indexId) || null == doc) {

            return;
        }

        IndexRequestBuilder requestBuilder =
                transportClient.prepareIndex(indexName, indexType, indexId).setRefresh(true);
        // 新增索引
        if (null != doc) {
            requestBuilder.setSource(doc.toString()).get();
        }
    }

    /**
     * 批量新增索引
     * 
     * @param indexName
     * @param indexType
     * @param docs map类型，key为索引ID，value为索引
     * @author zhoulinhong
     * @since 20160511
     */
    public void batchAddIndex(String indexName, String indexType, Map<String, JSONObject> docs) {

        if (StringUtils.isBlank(indexName) || StringUtils.isBlank(indexType) || null == docs
                || docs.isEmpty()) {

            return;
        }

        BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
        // 遍历索引,组装bulkRequestBuilder
        for (Map.Entry<String, JSONObject> entry : docs.entrySet()) {
            // 索引ID
            String indexId = entry.getKey();
            // 索引
            JSONObject index = entry.getValue();
            // builder组装
            if (null != index) {
                IndexRequestBuilder indexRequestBuilder = transportClient
                        .prepareIndex(indexName, indexType, indexId).setSource(index.toString());
                bulkRequestBuilder.add(indexRequestBuilder);
            }
        }

        // 批量新增索引
        bulkRequestBuilder.get();
    }

    /**
     * 删除索引
     * 
     * @param indexName
     * @param indexType
     * @param index
     * @author zhoulinhong
     * @since 20160511
     */
    public void deleteIndex(String indexName, String indexType, String indexId) {

        if (StringUtils.isBlank(indexName) || StringUtils.isBlank(indexType)
                || StringUtils.isBlank(indexId)) {

            return;
        }

        DeleteRequestBuilder deleteRequestBuilder =
                transportClient.prepareDelete(indexName, indexType, indexId).setRefresh(true);
        // 删除索引
        deleteRequestBuilder.get();
    }

    /**
     * 批量删除索引
     * 
     * @param indexName
     * @param indexType
     * @param docs
     * @author zhoulinhong
     * @since 20160511
     */
    public void batchDeleteIndex(String indexName, String indexType, List<String> docIds) {

        if (StringUtils.isBlank(indexName) || StringUtils.isBlank(indexType) || null == docIds
                || docIds.isEmpty()) {

            return;
        }

        BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
        // 遍历索引,组装bulkRequestBuilder
        for (String docId : docIds) {
            DeleteRequestBuilder deleteRequestBuilder =
                    transportClient.prepareDelete(indexName, indexType, docId);
            bulkRequestBuilder.add(deleteRequestBuilder);
        }

        // 批量删除索引
        bulkRequestBuilder.get();
    }

    /**
     * 修改索引
     * 
     * @param indexName
     * @param indexType
     * @param doc
     * @author zhoulinhong
     * @since 20160511
     */
    public void updateIndex(String indexName, String indexType, String docId, JSONObject doc) {

        if (StringUtils.isBlank(indexName) || StringUtils.isBlank(indexType)
                || StringUtils.isBlank(docId) || null == doc) {

            return;
        }

        UpdateRequestBuilder updateRequestBuilder =
                transportClient.prepareUpdate(indexName, indexType, docId).setRefresh(true);
        // 修改索引
        updateRequestBuilder.setDoc(doc).get();
    }

    /**
     * 批量修改索引
     * 
     * @param indexName
     * @param indexType
     * @param docs map类型，key为索引ID，value为索引修改信息
     * @author zhoulinhong
     * @since 20160511
     */
    public void batchUpdateIndex(String indexName, String indexType, Map<String, JSONObject> docs) {

        if (StringUtils.isBlank(indexName) || StringUtils.isBlank(indexType) || null == docs
                || docs.isEmpty()) {

            return;
        }

        BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
        // 遍历索引,组装bulkRequestBuilder
        for (Map.Entry<String, JSONObject> entry : docs.entrySet()) {
            // 索引ID
            String docId = entry.getKey();
            // 索引
            JSONObject doc = entry.getValue();
            // builder组装
            UpdateRequestBuilder updateRequestBuilder =
                    transportClient.prepareUpdate(indexName, indexType, docId).setDoc(doc);
            bulkRequestBuilder.add(updateRequestBuilder);
        }

        // 批量修改索引
        bulkRequestBuilder.get();
    }

    /**
     * 查询索引（根据ID）
     * 
     * @param indexName
     * @param indexType
     * @param doc
     * @author zhoulinhong
     * @since 20160511
     */
    public Map<String, Object> queryIndexById(String indexName, String indexType, String docId) {

        if (StringUtils.isBlank(indexName) || StringUtils.isBlank(indexType)
                || StringUtils.isBlank(docId)) {

            return null;
        }

        GetRequestBuilder getRequestBuilder =
                transportClient.prepareGet(indexName, indexType, docId).setRefresh(true);

        // 查询索引
        GetResponse getResponse = getRequestBuilder.get();

        return getResponse.getSource();
    }

    /**
     * 查询索引（根据查询语句）
     * 
     * @param indexName
     * @param indexType
     * @param queryString 查询语句
     * @author zhoulinhong
     * @return
     * @since 20160511
     */
    public SearchHits queryIndexByConditions(String indexName, String indexType,
            String queryString) {

        if (StringUtils.isBlank(indexName) || StringUtils.isBlank(indexType)
                || StringUtils.isBlank(queryString)) {

            return null;
        }

        SearchRequestBuilder searchRequestBuilder =
                transportClient.prepareSearch(indexName).setTypes(indexType).setSource(queryString);

        SearchResponse searchResponse = searchRequestBuilder.get();

        return searchResponse.getHits();
    }

    /**
     * 根据查询语句获取SearchResponse
     * 
     * @param indexName
     * @param indexType
     * @param queryString 查询语句
     * @author zhoulinhong
     * @return
     * @since 20160511
     */
    public SearchResponse getSearchResponse(String indexName, String indexType,
            String queryString) {

        if (StringUtils.isBlank(indexName) || StringUtils.isBlank(indexType)
                || StringUtils.isBlank(queryString)) {

            return null;
        }

        SearchRequestBuilder searchRequestBuilder =
                transportClient.prepareSearch(indexName).setTypes(indexType).setSource(queryString);

        SearchResponse searchResponse = searchRequestBuilder.get();

        return searchResponse;
    }
}
