package cn.daoway.sp;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

public class DaowayUtils {
    public static String map2String(SortedMap<String,Object> map) {
		StringBuilder builder = new StringBuilder();
		if(map.size()==0){
			return "";
		}
		try{
			for(Entry<String,Object> kv : map.entrySet()){
				builder.append(kv.getKey()
						+"="+URLEncoder.encode(kv.getValue().toString(),"UTF-8")+"&");
			}	
		}catch(UnsupportedEncodingException e){
			
		}
		
		if(builder.length()>0){
			return builder.substring(0, builder.length()-1);	
		}else{
			return "";
		}
	}
    
	private static String md5(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
			byte[] byteArray = messageDigest.digest();
			StringBuffer md5StrBuff = new StringBuffer();
			for (int i = 0; i < byteArray.length; i++) {
				if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
					md5StrBuff.append("0").append(
							Integer.toHexString(0xFF & byteArray[i]));
				else
					md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
			}
			return md5StrBuff.toString();
		} catch (Exception e) {
			throw new RuntimeException("md5 failed!");
		}
	}
    
    private static void dumpParams(Map<String, String[]> params){
    	for(Entry<String,String[]> kv : params.entrySet()){
    		System.out.println(kv.getKey()+"="+kv.getValue()[0]);
    	}
    	
    }
    /*public static boolean verifySign(
    		HttpServletRequest request, 
    		String appkey, String appsecret){
    	SortedMap<String,String> map = new TreeMap<String,String>();
    	@SuppressWarnings("unchecked")
		Map<String, String[]> params = request.getParameterMap();
		
		// for debug only
		dumpParams(params);
    	
		String expSign = null;
    	String[] appkeyParam = params.get("appkey");
    	if(appkeyParam==null){
    		return false;
    	}
    	if(!appkey.equals(appkeyParam[0])){
    		return false;
    	}
    	for(Entry<String,String[]> pv : params.entrySet()){
    		String param = pv.getKey();
    		String[] value = pv.getValue();
    		if(!param.equals("sign")){
    			map.put(param, value[0]);
    		}else{
    			expSign = value[0];
    		}
    	}
    	String string1 = map2String(map);
    	String stringSignTemp = string1 + "&secret="+appsecret;
    	String actualSign = md5(stringSignTemp).toUpperCase();
    	
    	return expSign.equals(actualSign);
    }*/
    
    private static String getUniqueId(){
    	return UUID.randomUUID().toString().replaceAll("-", "");
    }
    
    private static String createSign(SortedMap<String,Object> map, String appsecret){
		String string1 = map2String(map);
		System.out.println("string1:"+string1);
		String stringSignTemp = string1+"&secret="+appsecret;
		System.out.println("stringSignTemp:"+stringSignTemp);
		return md5(stringSignTemp).toUpperCase();
	}
    
    public static String sign(SortedMap<String,Object> map, String appkey, String appsecret){
		/*String oncestr = getUniqueId()*/;
		map.put("appkey", appkey);
		/*map.put("oncestr", oncestr);*/
		String sign = createSign(map,appsecret);
		System.out.println("sign:"+sign);
		map.put("sign", sign);
		
		return map2String(map);
	}


	public static String map2String1(SortedMap<String,Object> map) {
		StringBuilder builder = new StringBuilder();
		if(map.size()==0){
			return "";
		}
		try{
			for(Entry<String,Object> kv : map.entrySet()){
				builder.append(kv.getKey()
						+"="+URLEncoder.encode((String) kv.getValue(),"UTF-8")+"&");
			}
		}catch(UnsupportedEncodingException e){

		}

		if(builder.length()>0){
			return builder.substring(0, builder.length()-1);
		}else{
			return "";
		}
	}
}
