package com.kmzyc.search.app.es;

import java.net.InetAddress;

import org.apache.log4j.Logger;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import com.kmzyc.search.app.config.Configuration;

public class EsClient {

  private static final Logger logger = Logger.getLogger(EsClient.class);

  private static TransportClient transportClient;

  private EsClient() {}

  private static boolean init() {
    if (transportClient != null) {
      return true;
    }
    try {
      Settings settings = Settings.settingsBuilder()
          // 指定集群名称
          .put("cluster.name", Configuration.getContextProperty("es.cluster.name"))
          // 探测集群中机器状态
          .put("client.transport.sniff", true).build();
      String esAddress = Configuration.getContextProperty("es.node.address");
      String[] addresses = esAddress.split("[;,]");
      transportClient = TransportClient.builder().settings(settings).build();
      for (String node : addresses) {
        String[] nodes = node.split(":");
        transportClient.addTransportAddress(new InetSocketTransportAddress(
            InetAddress.getByName(nodes[0]), Integer.valueOf(nodes[1])));
      }
    } catch (Exception e) {
      logger.error("esclient initialize exception !", e);
    }
    return true;
  }

  public static Client getClient() {
    if (init()) {
      return transportClient;
    }
    return null;
  }


  public static BulkRequestBuilder getBulkBuilder() {
    if (init()) {
      return transportClient.prepareBulk();
    }
    return null;

  }

  public static GetRequestBuilder getGetBuilder(String indexDB, String indexType, String id) {
    if (init()) {
      return transportClient.prepareGet(indexDB, indexType, id);
    }
    return null;
  }

  public static IndexRequestBuilder getIndexBuilder(String indexDB, String indexType, String id) {
    if (init()) {
      return transportClient.prepareIndex(indexDB, indexType, id);
    }
    return null;
  }

  public static DeleteRequestBuilder getDeleteBuilder(String indexDB, String indexType, String id) {
    if (init()) {
      return transportClient.prepareDelete(indexDB, indexType, id);
    }
    return null;
  }



  public static void close() {
    if (transportClient != null) {
      transportClient.close();
      transportClient = null;
    }
  }


}
