package com.kmzyc.search.app.dbcrawler;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;
import org.apache.zookeeper.data.Stat;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;

import com.kmzyc.search.app.dbcrawler.transformer.ITransformer;
import com.kmzyc.search.app.util.IndexClientUtil;
import com.kmzyc.search.config.Configuration;

public class DBCrawler {

    private static final Logger logger = Logger.getLogger(DBCrawler.class);

    private static final String crawlerFilePath =
            DBCrawler.class.getClassLoader().getResource("").getPath();

    private static int pageSize = 500;

    private static final String crawlerZookeeperPath = "/kmconfig/esconfig/web/crawler/";

    /**
     * 存放模板中配置的索引字段与数据表字段的映射关系
     */
    private Map<String, Map<String, String>> indexColumnMap =
            new HashMap<String, Map<String, String>>();

    /**
     * 存放全量索引的id记录
     */
    private Set<String> wholeIndexIDSet = new HashSet<String>();

    /**
     * 存放全量索引过后需要进行索引删除的id记录
     */
    private Set<String> deleteIndexIDSet = new HashSet<String>();

    private String crawlerName = "";


    private String indexDBName = "";
    private String indexType = "";
    private String indexId = null;

    private String driverClassName = "";
    private String dbURL = "";
    private String username = "";
    private String password = "";

    private String querySql = "";

    private boolean isWhole = false;

    // 存放索引数据转换处理类
    private List<ITransformer> transformerClasses = new ArrayList<ITransformer>();

    public DBCrawler(String crawlerName) throws Exception {
        this.crawlerName = crawlerName;
        try {
            loadCrawlerConfig(crawlerName);
        } catch (Exception e) {
            logger.error("加载 " + crawlerName + ".xml 配置信息异常 ！", e);
            throw new RuntimeException(e);
        }
        try {
            loadIndexIds();
        } catch (Exception e) {
            logger.error("加载 索引id记录信息异常 ！", e);
        }
    }

    public void startCrawl() {
        DBConnection dbConn = new DBConnection(driverClassName, dbURL, username, password);
        try {
            BulkRequestBuilder bulkBuilder = null;
            int count = dbConn.queryCount(querySql);
            System.out.println("Crawl total : " + count);
            int pageCount = count % pageSize > 0 ? count / pageSize + 1 : count / pageSize;
            for (int page = 1; page <= pageCount; page++) {
                bulkBuilder = IndexClientUtil.getInstance().getBulkBuilder();
                ResultSet result = dbConn.queryPage(querySql, page, pageSize);
                while (result.next()) {
                    String indexID = result.getObject(indexId).toString();
                    // 将数据库数据结果转换成map
                    Map<String, Object> indexMap = buildIndexMap(result);
                    // 对索引数据进行进一步处理，例如促销价格添加
                    indexMap = transProcess(indexMap);
                    bulkBuilder.add(IndexClientUtil.getInstance()
                            .getIndexBuilder(indexDBName, indexType, indexID).setSource(indexMap));
                    addIndexID(indexID);
                }
                if (bulkBuilder.numberOfActions() > 0) {
                    BulkResponse bulkResponse = bulkBuilder.execute().actionGet();
                    if (bulkResponse.hasFailures()) {
                        logger.error(bulkResponse.buildFailureMessage());
                    }
                }
            }
            // 处理索引id集合
            processIndexIDs();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("抓取异常！", e);
        } finally {
            dbConn.close();
        }
    }

    /**
     * 将数据库结果转换成map
     * 
     * @param result
     * @return
     * @throws SQLException
     */
    private Map<String, Object> buildIndexMap(ResultSet result) throws SQLException {
        ResultSetMetaData data = result.getMetaData();
        Map<String, Object> indexMap = new HashMap<String, Object>();
        for (int i = 1; i <= data.getColumnCount(); i++) {
            String columnName = data.getColumnName(i);
            Map<String, String> columnMap = indexColumnMap.get(columnName);
            if (null == columnMap || columnMap.isEmpty()) {
                continue;
            }
            String indexField = columnMap.get("indexField");
            String type = columnMap.get("type");
            if (StringUtils.isBlank(indexField) || result.getObject(i) == null) {
                continue;
            }
            String splitBy = columnMap.get("splitBy");
            Object indexValue = null;
            if (StringUtils.isNotBlank(splitBy)) {
                indexValue =
                        (result.getString(i) == null ? null : result.getString(i).split(splitBy));
            } else if (StringUtils.isBlank(type)) {
                indexValue = result.getObject(i);
            } else if ("string".equals(type.toLowerCase().trim())) {
                indexValue = result.getString(i);
            } else if ("long".equals(type.toLowerCase().trim())) {
                indexValue = result.getLong(i);
            } else if ("double".equals(type.toLowerCase().trim())) {
                indexValue = result.getDouble(i);
            } else if ("int".equals(type.toLowerCase().trim())) {
                indexValue = result.getInt(i);
            } else if ("date".equals(type.toLowerCase().trim()) && result.getTimestamp(i) != null) {
                // 日期格式化
                SimpleDateFormat fommat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                indexValue = fommat.format(result.getTimestamp(i));
            } else {
                indexValue = result.getObject(i);
            }
            if (indexValue == null) {
                continue;
            }
            indexMap.put(indexField, indexValue);
        }
        return indexMap;

    }



    /**
     * 对索引数据进行进一步处理
     * 
     * @param indexMap
     * @return
     */
    private Map<String, Object> transProcess(Map<String, Object> indexMap) {
        if (transformerClasses == null || transformerClasses.isEmpty()) {
            return indexMap;
        }
        for (ITransformer transformer : transformerClasses) {
            indexMap = transformer.process(indexMap, crawlerName);
        }
        return indexMap;
    }

    /**
     * 处理索引id记录，全量的模式下要删除未更新过的索引id
     * 
     * @throws Exception
     */
    private void processIndexIDs() throws Exception {
        // 若是全量抓取模式，则按待删除索引id库中的索引id批量删除索引数据
        if (isWhole) {
            Iterator<String> ite = deleteIndexIDSet.iterator();
            BulkRequestBuilder bulkBuilder = IndexClientUtil.getInstance().getBulkBuilder();
            int bulkSize = 1;
            while (ite.hasNext()) {
                String indexID = ite.next();
                bulkBuilder.add(IndexClientUtil.getInstance().getDeleteBuilder(indexDBName,
                        indexType, indexID));
                bulkSize++;
                if (bulkSize >= pageSize) {
                    BulkResponse bulkResponse = bulkBuilder.execute().actionGet();
                    if (bulkResponse.hasFailures()) {
                        logger.error(bulkResponse.buildFailureMessage());
                    }
                    bulkBuilder = IndexClientUtil.getInstance().getBulkBuilder();
                    bulkSize = 1;
                }
            }
            if (bulkBuilder.numberOfActions() > 0) {
                BulkResponse bulkResponse = bulkBuilder.execute().actionGet();
                if (bulkResponse.hasFailures()) {
                    logger.error(bulkResponse.buildFailureMessage());
                }
            }
        }

        // 持久化全量索引id记录
        ObjectOutputStream objectOut = null;
        try {
            objectOut = new ObjectOutputStream(
                    new FileOutputStream(new File(crawlerFilePath + indexDBName + ".ids")));
            objectOut.writeObject(wholeIndexIDSet);
            objectOut.flush();
        } finally {
            if (objectOut != null) {
                objectOut.close();
            }
        }
    }

    /**
     * 处理索引id
     * 
     * @param indexID
     */
    private void addIndexID(String indexID) {
        wholeIndexIDSet.add(indexID);
        if (isWhole) {
            deleteIndexIDSet.remove(indexID);
        }
    }

    /**
     * 将全量索引数据的id记录加载到set集合中
     * 
     * @param isWhole
     * @param indexDB
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private void loadIndexIds() throws Exception {
        File idsFile = new File(crawlerFilePath + indexDBName + ".ids");
        if (!idsFile.exists()) {
            idsFile.createNewFile();
            return;
        }
        ObjectInputStream objectInput = null;
        try {
            objectInput = new ObjectInputStream(new FileInputStream(idsFile));
            // 若是全量抓取模式，则将id记录加载到待删除的索引id集合中。在抓取过程中会剔除掉有更新的id
            if (isWhole) {
                deleteIndexIDSet = (Set<String>) objectInput.readObject();
                // 增量抓取模式，将id记录加载到全量索引id集合中，在抓取过程中添加新抓取的数据id
            } else {
                wholeIndexIDSet = (Set<String>) objectInput.readObject();
            }
        } finally {
            if (objectInput != null) {
                objectInput.close();
            }
        }
    }

    /**
     * 加载爬虫配置信息
     * 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private void loadCrawlerConfig(String crawlerName) throws Exception {
        // 获取爬虫配置文件数据
        byte[] data = getCrawlerConfigFromZookeeper(crawlerName);
        ByteArrayInputStream in = new ByteArrayInputStream(data, 0, data.length);

        // 创建saxReader对象
        SAXReader reader = new SAXReader();
        // 通过read方法读取一个文件 转换成Document对象
        Document document = reader.read(in);
        // 获取根节点元素对象
        Element node = document.getRootElement();

        Element mode = node.element("whole");
        isWhole = Boolean.valueOf(mode.getTextTrim());

        Element database = node.element("database");
        driverClassName = database.element("driverClassName").getTextTrim();
        dbURL = database.element("dbURL").getTextTrim();
        username = database.element("username").getTextTrim();
        password = database.element("password").getTextTrim();
        querySql = database.element("querySql").getTextTrim();

        try {
            pageSize = Integer.valueOf(database.element("pageSize").getTextTrim());
        } catch (Exception e) {
            // 出现异常自动按默认值处理
        }
        String transformeres = null;
        if (null != node.element("transformeres")) {

            transformeres = node.element("transformeres").getTextTrim();
        }
        parseTransFormeres(transformeres);

        Element esIndex = node.element("esIndex");

        indexDBName = esIndex.element("indexDB").getTextTrim();
        indexType = esIndex.element("indexType").getTextTrim();
        indexId = esIndex.element("indexId").getTextTrim();

        Element fields = node.element("fields");
        List<Element> els = fields.elements();
        for (Element el : els) {
            String column = el.attributeValue("column").trim();
            Map<String, String> columnMap = new HashMap<String, String>();
            String indexName = el.attributeValue("indexField").trim();
            String type = el.attributeValue("type").trim().toLowerCase();
            columnMap.put("indexField", indexName);
            columnMap.put("type", type);
            String splitBy = el.attributeValue("splitBy");
            if (StringUtils.isNotBlank(splitBy)) {
                columnMap.put("splitBy", splitBy);
            }
            indexColumnMap.put(column, columnMap);
        }
    }

    private byte[] getCrawlerConfigFromZookeeper(String crawlerName) throws Exception {
        String zkConnectionString = Configuration.getString("zk.host");
        int baseSleepTimeMs = Configuration.getInt("zk.sleepTime", 1000);
        int maxRetries = Configuration.getInt("zk.retryTimes", 3);
        int connectionTimeoutMs = Configuration.getInt("zk.connect.timeout", 10000);
        int sessionTimeoutMs = Configuration.getInt("zk.session.timeout", 10000);
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
        CuratorFramework zkClient = null;
        try {
            zkClient = CuratorFrameworkFactory.builder().connectString(zkConnectionString)
                    .retryPolicy(retryPolicy).connectionTimeoutMs(connectionTimeoutMs)
                    .sessionTimeoutMs(sessionTimeoutMs).build();
            zkClient.start();
            Stat stat = zkClient.checkExists().forPath(crawlerZookeeperPath + crawlerName + ".xml");
            if (null == stat) {
                throw new RuntimeException("can not find crawler config in zookeeper !!");
            }
            return zkClient.getData().forPath(crawlerZookeeperPath + crawlerName + ".xml");
        } finally {
            if (zkClient != null) {
                zkClient.close();
            }
        }
    }

    private void parseTransFormeres(String transformeres) {
        if (StringUtils.isBlank(transformeres)) {
            return;
        }
        String[] trans = transformeres.split(",");
        for (String tranClassName : trans) {
            try {
                ITransformer transformer =
                        (ITransformer) Class.forName(tranClassName).newInstance();
                transformerClasses.add(transformer);
            } catch (Exception e) {
                logger.error("create class " + tranClassName + " exception !", e);
                continue;
            }
        }
    }



}
