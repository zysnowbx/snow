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
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

public class GoToDaoWayCopy {
    public static void main(String[] args) {
        // appkey：863ae345e15d418ba07952cf578623c2
        // oncestr ：6f8efb31ea1b4c8685e789bbb2406c13
        String oncestr = "b31ea1b4c8685e789bbb2406c13";
        SortedMap<String, Object> map = new TreeMap<>();
        map.put("oncestr", oncestr);
        JSONArray dataArray = new JSONArray();
        map.put("data", dataArray);

        SortedMap<String, Object> map1 = new TreeMap<>();
        map1.put("id", "" + UUID.randomUUID().toString().replaceAll("-", "") + "");
        map1.put("real_name", "张三");
        map1.put("start_time", "08:00");
        map1.put("end_time", "19:00");
        map1.put("available_days", "0,1,2,3,4,5,6");
        map1.put("head_photo", "head_photo");
        map1.put("city", "青岛");
        map1.put("phone", "12345678909");

        dataArray.add(JSON.toJSON(map1));

        /*map1.put("\"id\"","\""+UUID.randomUUID().toString().replaceAll("-", "")+"\"");
        map1.put("\"real_name\"","\"张三\"");
        map1.put("\"start_time\"","\"08:00\"");
        map1.put("\"end_time\"","\"19:00\"");
        map1.put("\"available_days\"","\"0,1,2,3,4,5,6\"");
        map1.put("\"head_photo\"","\"head_photo\"");
        map1.put("\"phone\"","\"12345678909\"");*/
        /*map.put("data",map1);*/
        /*String json1 = getMapToString(map1);
        String json2 = getMapToString(map);*/
        /*System.out.println("json1:"+json1);
        System.out.println("json2:"+json2);*/
        /* map.put("data",json1);*/
        String signStr = DaowayUtils.sign(map, DaowayConfig.appkey, DaowayConfig.appsecret);
        String json = JSON.toJSONString(map);//map转String
        JSONObject obj = JSON.parseObject(json);//String转json
        /*   obj.replace("\"\\\"","/");*/

        String rep = sendPost(obj);
        System.out.println("resp:" + rep);

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
            URL url = new URL(" http://test.daoway.cn:8080/daoway/rest/technician/sync"); //url地址

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
}


