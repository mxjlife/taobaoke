package com.mxjlife.taobaoke.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 类说明: jdbc工具类, 从jdbc连接池中获取连接, 查询数据库
 * 
 * @author mxj
 * @email xj.meng@sinowaycredit.com
 * @version 创建时间：2017年9月11日 下午3:17:27
 */
public class JDBCUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(JDBCUtils.class);
    
    /**
     * 传入SQL语句, 将查询到的结果封装到List<Map<String, String>> 中
     * @param sql
     */
    public static List<Map<String, String>> excuteQuery(String sql) {
        List<Map<String, String>> list = null;
        Connection conn = null;
        try {
            // 使用连接池获取连接
            conn = JDBCDataSource.getInstance().getConnection();
            list = excuteQuery(conn, sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放资源连接归还连接池
            JDBCDataSource.closeConnection(conn);
        }
        return list;
    }

    public static List<Map<String, String>> excuteQuery(Connection conn, String sql) {
        List<Map<String, String>> list = null;
        Map<String, String> map = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        JDBCDataSource instance = null;
        try {
            // 建立PreparedStatement对象

            ps = conn.prepareStatement(sql);
            // 执行查询
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            //总列数
            int columnCount = rsmd.getColumnCount();
            // 解析结果集ResultSet对象
            list = new ArrayList<Map<String, String>>();
            while(rs.next()){
                map = new HashMap<String, String>();
                for (int i = 0; i < columnCount; i++) {
                    //获取列名
                    String name = rsmd.getColumnName(i+1);
                    //获取列值
                    String value = rs.getString(i+1);
                    map.put(name, value);
                }
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放资源连接归还连接池
            if(ps!=null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }


    public static void excuteInsert(String sql) {
        Connection conn = null;
        PreparedStatement ps = null;
        JDBCDataSource instance = null;
        try {
            // 使用连接池获取连接
            instance = JDBCDataSource.getInstance();
            conn = instance.getConnection();
            // 建立PreparedStatement对象
            ps = conn.prepareStatement(sql);
            ps.execute();
            // 执行查询
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放资源连接归还连接池
            JDBCDataSource.closeConnection(conn);
            if(ps!=null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
//
//
//    public static void main(String[] args) {
////        List<Map<String, String>> list = JDBCUtils.excuteQuery("Select api_id,api_name,param,param_name  from api_params");
//        String sql = "INSERT INTO `manager`.`sys_admin` (`username`, `email`, `password`, `name`) VALUES ('admin_pre_index', 'E-index_pre@163.com', '49dc52e6bf2abe5ef6e2bb5b0f1ee2d765b922ae6cc8b95d39dc06c21c848f8c', '用户-pre_index')";
//        ExecutorService executorService = Executors.newFixedThreadPool(50);
//        for (int i = 0; i < 50; i++) {
//            final String fi = String.valueOf(i);
//            executorService.submit(() -> {
//                Connection conn = JDBCDataSource.getInstance().getConnection();
//                Statement statement = null;
//                try {
//                    statement = conn.createStatement();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    conn.setAutoCommit(false);
//                    long l = System.currentTimeMillis();
//                    for (int j = 0; j < 100000; j++) {
//                        statement.execute(sql.replaceAll("pre", fi).replaceAll("index", String.valueOf(j)));
//                    }
//                    System.out.println(System.currentTimeMillis() - l);
//                    conn.commit();
//                } catch (Exception e) {
//                    try {
//                        conn.rollback();
//                    } catch (SQLException e1) {
//                        e1.printStackTrace();
//                    }
//                    e.printStackTrace();
//                } finally {
//
//                    // 释放资源连接归还连接池
//                    JDBCDataSource.closeConnection(conn);
//                }
//            });
//        }
//        executorService.shutdown();
//
////        JSONArray jarr = new JSONArray();
////        Map<String,JSONObject> jsonMap = new HashMap();
////        for (int i = 0; i < list.size(); i++) {
//////            [[{},{},{},{},{}],[],[],[]]
////            JSONObject json = new JSONObject();
////            json.put("api_id", list.get(i).get("api_id"));
////            json.put("api_name", list.get(i).get("api_name"));
////            json.put("params", new JSONArray());
////            jsonMap.put(list.get(i).get("api_id"),json);
////        }
////        for (int i = 0; i < list.size(); i++) {
////            Map<String, String> map = list.get(i);
////            JSONObject apiJson = jsonMap.get(map.get("api_id"));
////            JSONObject json = new JSONObject();
////            json.put("param_id", map.get("param"));
////            json.put("param_name", map.get("param_name"));
////            apiJson.getJSONArray("params").add(json);
////        }
////        for (String key: jsonMap.keySet()) {
////            jarr.add(jsonMap.get(key));
////        }
//
//
//
////        System.out.println(jarr);
//    }
}

/**
 * 
 * JDBC连接池
 *
 */
class JDBCDataSource {
    // 私有化构造函数
    private JDBCDataSource() {
    }

      private static final String dirverClassName = "com.mysql.cj.jdbc.Driver";
      private static final String url = "jdbc:mysql://192.168.200.101:3306/manager?useUnicode=true&characterEncoding=utf8&useSSL=false";
      private static final String user = "root";
      private static final String password = "123456";
//    private static final String dirverClassName = ConfigUtils.getConfig("jdbc.driverClass");
//    private static final String url = ConfigUtils.getConfig("jdbc.jdbcUrl");
//    private static final String user = ConfigUtils.getConfig("jdbc.user");
//    private static final String password = ConfigUtils.getConfig("jdbc.password");
    // 连接池
    private static LinkedList<Connection> pool = new LinkedList<Connection>();
    private static JDBCDataSource instance = new JDBCDataSource();
    private static final int maxConn = 200;

    // 加载驱动
    static {
        try {
            Class.forName(dirverClassName);
        } catch (ClassNotFoundException e) {
            System.out.println("找不到驱动类！" + e.getMessage());
        }
    }

    /**
     * 获取数据源单例
     * 
     * @return 数据源单例
     */
    public static synchronized JDBCDataSource getInstance() {
        if (instance == null) {
            synchronized (JDBCDataSource.class){
                if(instance == null){
                    instance = new JDBCDataSource();
                }
            }
        }
        return instance;
    }

    /**
     * 获取一个数据库连接
     * 
     * @return 一个数据库连接
     * @throws SQLException
     */
    public synchronized Connection getConnection() {
        if (pool.size() > 0) {
            return pool.removeFirst();
        } else {
            return makeConnection();
        }
    }

    /**
     * 将连接归还连接池
     * 
     * @param conn
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            if(pool.size() < maxConn){
                pool.addLast(conn);
            } else {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("关闭jdbc连接时出错" + e.getMessage());
                }
            }
        }
    }

    /**
     * 创建连接 创建连接, 使用连接后归还连接池的时候再设置连接池
     * 
     * @return
     * @throws SQLException
     */
    private Connection makeConnection() {
        Connection connection = null;
        try {
            if (pool.size() >= maxConn){
                System.out.println("连接池连接数已经到达最大值！");
                throw new RuntimeException("连接池连接数已经到达最大值");
            } else {
                connection = DriverManager.getConnection(url, user, password);
            }
        } catch (SQLException e) {
            System.out.println("创建jdbc连接出错");
            e.printStackTrace();
        }
        return connection;
    }

    
}
