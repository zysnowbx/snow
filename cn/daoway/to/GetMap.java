package cn.daoway.to;

import java.sql.*;
import java.util.*;

public class GetMap {
    /*private static final String URL="jdbc:mysql://127.0.0.1:3306/";*/
    /*private static final String URL="jdbc:mysql://localhost:3306/mysql8?useUnicode=true&characterEncoding=UTF-8";*/
    private static final String URL="jdbc:mysql://localhost:3306/mysql?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=GMT";
    private static final String USER="root";
    private static final String PASSWORD="1qaz2WSX";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        ResultSet rs=null;
        Connection conn=null;
        Statement stmt=null;
        try {
            //1.加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
           /* Class.forName("com.mysql.dbc.Driver");*/
            //2.获得数据连接
             conn = DriverManager.getConnection(URL, USER, PASSWORD);
            //3.使用数据库的连接创建声明
             stmt = conn.createStatement();
            //4.使用声明执行SQL语句
           rs = stmt.executeQuery("select * from t_user_info");
            //5.读取数据库的信息
/**
 *boolean next() 方法 如果新的当前行有效，则返回 true；如果不存在下一行，则返回 false
 * 将光标从当前位置向前移一行。ResultSet 光标最初位于第一行之前；第一次调用 next 方法使第一行成为当前行；第二次调用使第二行成为当前行，依此类推。
 */
          /*  while(rs.next()){
                String id = rs.getString("id");
                String phone = rs.getString("phone");
                String realName = rs.getString("real_name");
                System.out.println(id+" 手机号："+phone+"名字："+realName);
            }*/
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs!=null){
                    rs.close();

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            rs=null;
            try {
                if(stmt!=null){
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            stmt=null;
            try {
                if(conn!=null){
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            conn.close();
        }


    }
    public static List<SortedMap<String,Object>> getData()throws ClassNotFoundException, SQLException {
        //定义参数
        ResultSet rs= null;
        Connection conn=null;
        Statement stmt=null;
        SortedMap<String, Object> map= new TreeMap<>();
        List<SortedMap<String,Object>> list1=new ArrayList<>();
        try {
            //加载数据库驱动
            Class.forName("com.mysql.jdbc.Driver");
            //获取数据库链接
            conn=DriverManager.getConnection(URL, USER, PASSWORD);
            //使用数据库链接建立声明
            stmt=conn.createStatement();
            rs= stmt.executeQuery("SELECT * FROM t_user_info");
            list1=returnMap(rs);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(rs!=null){
                rs.close();
            }
            if(stmt!=null){
                stmt.close();
            }
            if(conn!=null){
                conn.close();
            }
        }
        return list1;
    }
    public static List<SortedMap<String,Object>> returnMap(ResultSet rs) throws SQLException {
        List<SortedMap<String,Object>>  list = new ArrayList<>();
        while(rs.next()){
            SortedMap<String, Object> map1= new TreeMap<>();
            map1.put("id",rs.getString("id"));
            map1.put("real_name",rs.getString("real_name"));
            map1.put("phone",rs.getString("phone"));
            map1.put("city",rs.getString("city"));
            map1.put("start_time",rs.getString("start_time"));
            map1.put("end_time",rs.getString("end_time"));
            map1.put("head_photo",rs.getString("head_photo"));
            map1.put("available_days",rs.getString("available_days"));
            list.add(map1);
        }
        /*list.add(map1);*/
        return list;
    }

    public static  Connection  getDbConection()throws ClassNotFoundException, SQLException {
        //定义参数
        ResultSet rs= null;
        Connection conn=null;
        Statement stmt=null;
        SortedMap<String, Object> map= new TreeMap<>();
        List<SortedMap<String,Object>> list1=new ArrayList<>();
        try {
            //加载数据库驱动
            Class.forName("com.mysql.jdbc.Driver");
            //获取数据库链接
            conn=DriverManager.getConnection(URL, USER, PASSWORD);
            //使用数据库链接建立声明
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

        }
        return  conn;
    }

}
