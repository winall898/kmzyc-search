package com.kmzyc.search.app.dbcrawler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class DBConnection {

  private static Logger logger = Logger.getLogger(DBConnection.class);

  private Connection conn = null;
  private Statement stmt = null;
  private ResultSet rs = null;

  public DBConnection(String driverClassName, String dbURL, String userName, String password) {
    try {
      Class.forName(driverClassName);
      conn = DriverManager.getConnection(dbURL, userName, password);
      stmt = conn.createStatement();
    } catch (Exception e) {
      logger.error("数据库连接创建失败 ！", e);
    }
  }

  public int queryCount(String querySql) throws SQLException {
    String countSql = "select count(1) from (" + querySql + ")";
    rs = stmt.executeQuery(countSql);
    if (rs.next()) {
      return rs.getInt(1);
    }
    return 0;
  }

  public ResultSet queryPage(String querySql, int pages, int pageSize) throws SQLException {
    StringBuffer sb = new StringBuffer();
    sb.append("SELECT * FROM (SELECT t1.*, ROWNUM as rn FROM (");
    sb.append(querySql);
    sb.append(") t1 WHERE ROWNUM <= ");
    sb.append(pages * pageSize);
    sb.append(") t2 where t2.rn > ");
    sb.append((pages - 1) * pageSize);
    logger.debug("executeSql: " + sb.toString());
    rs = stmt.executeQuery(sb.toString());
    return rs;
  }

  public void close() {
    try {
      if (rs != null) {
        rs.close();
      }
      if (stmt != null) {
        stmt.close();
      }
      if (conn != null) {
        conn.close();
      }
    } catch (SQLException e) {
      logger.error("dbconnection close stream excepion", e);
    }

  }

}
