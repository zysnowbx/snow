package cn.daoway.to;

import cn.daoway.sp.DaowayConfig;
import cn.daoway.sp.DaowayUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.*;

public class GoToDaoWay {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        List<SortedMap<String,Object>> sortedMapList= new ArrayList<>();
       /* String oncestr = "b31ea1b4c8685e789bbb2406c13";*/
        sortedMapList= GetMap.getData();
        int j=0;
        System.out.println("sortedMapList.size()"+sortedMapList.size());
        for(int i=0;i<sortedMapList.size();i++){
            SortedMap<String, Object> map1 = new TreeMap<>();
            SortedMap<String, Object> map = new TreeMap<>();
            String oncestr="" + UUID.randomUUID().toString().replaceAll("-", "") + "";
            map.put("oncestr", oncestr);
            JSONArray dataArray = new JSONArray();
            map.put("data", dataArray);
            map1= sortedMapList.get(i);
            /*map1.put("id", id);*/
            dataArray.add(JSON.toJSON(map1));
            String signStr = DaowayUtils.sign(map, DaowayConfig.appkey, DaowayConfig.appsecret);
            String json = JSON.toJSONString(map);//map转String
            JSONObject obj = JSON.parseObject(json);//String转json
            String rep = sendPost(obj);
            System.out.println("oncestr:"+oncestr+"  resp:"+rep);
            j=j++;

        }
    System.out.println("一个发送成功记录数："+j);

    }

    public static String send(String url, Map<String, String> map, Charset encoding) throws Exception {
        String body = "";

        // 创建httpclient对象
        CloseableHttpClient client = HttpClients.createDefault();
        // 创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);
        encoding = Charset.forName("UTF-8");
        // 装填参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        // 设置参数到请求对象中

        httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));

        // 设置header信息
        // 指定报文头【Content-type】、【User-Agent】
        httpPost.setHeader("Content-type", "application/json");
        // 执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = client.execute(httpPost);
        // 获取结果实体
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            // 按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, encoding);
        }
        EntityUtils.consume(entity);
        // 释放链接
        response.close();
        return body;
    }

    public static String sendPost(JSONObject obj) {
        String resp = null;
        /*JSONObject obj = new JSONObject();
        obj.put("name", "张三");
        obj.put("age", "18");*/
        String query = obj.toString();
        System.out.println("发送到URL的报文为：");
        System.out.println(query);
        try {
            URL url = new URL(" http://test.daoway.cn:8080/daoway/rest/technician/sync"); //测试环境url地址
           /* URL url = new URL("http://api.daoway.cn/daoway/rest/technician/sync"); //生产环境url地址*/

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();

            try (OutputStream os = connection.getOutputStream()) {
                os.write(query.getBytes("UTF-8"));
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String lines;
                StringBuffer sbf = new StringBuffer();
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), "utf-8");
                    sbf.append(lines);
                }
                System.out.println("返回来的报文：" + sbf.toString());
                resp = sbf.toString();
                JSONObject json= JSON.parseObject(resp);
                if(""!=json.get("status"+"")&& null!=json.get("status"+"")&& "ok".equals(json.get("status"+""))){
                    System.out.println("返回成功：" + sbf.toString());
                }else{
                    System.out.println("返回失败：" + sbf.toString());
                }



            }
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JSONObject json = (JSONObject) JSON.parse(resp);
            resp = json.toString();

        }
        return resp;
    }

    public static String getMapToString(Map<String, Object> map) {
        Set<String> keySet = map.keySet();
        //将set集合转换为数组
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
//给数组排序(升序)
        Arrays.sort(keyArray);
//因为String拼接效率会很低的，所以转用StringBuilder
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keyArray.length; i++) {
// 参数值为空，则不参与签名 这个方法trim()是去空格
            if ((String.valueOf(map.get(keyArray[i]))).trim().length() > 0) {
                sb.append(keyArray[i]).append(":").append(String.valueOf(map.get(keyArray[i])).trim());
            }
            if (i != keyArray.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
    public static JSONObject toJsonArrayList (SortedMap<String, Object> map){
        JSONObject obj = new JSONObject();

        return obj;
    }
}


