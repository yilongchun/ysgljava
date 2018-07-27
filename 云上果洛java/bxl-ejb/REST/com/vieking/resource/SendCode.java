package com.vieking.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;








import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.util.EntityUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;



public class SendCode {
	//发送验证码的请求路径URL
    private static final String
            SERVER_URL="https://api.netease.im/sms/sendcode.action";
    //网易云信分配的账号，请替换你在管理后台应用下申请的Appkey
    private static final String 
            APP_KEY="fe36d9995010aac43191642c42371eea";
    //网易云信分配的密钥，请替换你在管理后台应用下申请的appSecret
    private static final String APP_SECRET="7760f05ea694";
    //短信模板ID
    private static final String TEMPLATEID="3111210";
    //手机号
//    private static final String MOBILE="15907127685";
    //验证码长度，范围4～10，默认为4
    private static final String CODELEN="4";
 

    public static void main(String[] args) throws Exception {

//        String res = sendMsg(MOBILE);
//        JSONObject obj = JSONObject.fromObject(res);
//        int code = obj.getInt("code");
//        String msg = obj.getString("obj");
//        System.out.println("code:"+code+",msg:"+msg);
    	
//    	List<String> mobileList = new ArrayList<String>();
//    	mobileList.add("18671701215");
//    	mobileList.add("15907127685");
//    	sendNotice(mobileList, "果洛州宣传部", "2017-12-04 09:55:12");
    	
    	
    	List<String> mobileList = new ArrayList<String>();
    	for (int i = 0; i < 2; i++) {
			mobileList.add(i + "");
		}
    	
    	int page;
    	if (mobileList.size()%100 == 0) {
			page = mobileList.size()/100;
		}else{
			page = mobileList.size()/100 + 1;
		}
    	
    	for (int i = 0; i < page; i++) {
    		int toIndex = (i+1)*100;
    		if (toIndex > mobileList.size()) {
    			toIndex = mobileList.size();
			}
    		List<String> subList = mobileList.subList((i)*100, toIndex);
		}
    	
    }
    
    public static String sendMsg(String mobile) throws Exception{
    	DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_URL);
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        /*
         * 参考计算CheckSum的java代码，在上述文档的参数列表中，有CheckSum的计算文档示例
         */
        int random = (int)((Math.random()*9+1)*100000);
        System.out.println(random);
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET, String.valueOf(random), curTime);

        // 设置请求的header
        httpPost.addHeader("AppKey", APP_KEY);
        httpPost.addHeader("Nonce", String.valueOf(random));
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 设置请求的的参数，requestBody参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        /*
         * 1.如果是模板短信，请注意参数mobile是有s的，详细参数配置请参考“发送模板短信文档”
         * 2.参数格式是jsonArray的格式，例如 "['13888888888','13666666666']"
         * 3.params是根据你模板里面有几个参数，那里面的参数也是jsonArray格式
         */
        nvps.add(new BasicNameValuePair("templateid", TEMPLATEID));
        nvps.add(new BasicNameValuePair("mobile", mobile));
        nvps.add(new BasicNameValuePair("codeLen", CODELEN));

        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);
        /*
         * 1.打印执行结果，打印结果一般会200、315、403、404、413、414、500
         * 2.具体的code有问题的可以参考官网的Code状态表
         */
        
        String result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println("mobile:" + mobile + " send code result:"+result);
        Log log = Logging.getLog(SendCode.class);
        log.debug("mobile:" + mobile + " send code result:"+result);
        return result;
    }
    
    //发送通知
    public static String sendNotice(List<String> mobileList,String user,String datetime) throws Exception{
    	DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("https://api.netease.im/sms/sendtemplate.action");
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        /*
         * 参考计算CheckSum的java代码，在上述文档的参数列表中，有CheckSum的计算文档示例
         */
        int random = (int)((Math.random()*9+1)*100000);
//        System.out.println(random);
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET, String.valueOf(random), curTime);

        // 设置请求的header
        httpPost.addHeader("AppKey", APP_KEY);
        httpPost.addHeader("Nonce", String.valueOf(random));
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 设置请求的的参数，requestBody参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        /*
         * 1.如果是模板短信，请注意参数mobile是有s的，详细参数配置请参考“发送模板短信文档”
         * 2.参数格式是jsonArray的格式，例如 "['13888888888','13666666666']"
         * 3.params是根据你模板里面有几个参数，那里面的参数也是jsonArray格式
         */
        nvps.add(new BasicNameValuePair("templateid", "3136333"));
        
        
        JSONArray mobiles = new JSONArray();
        for (int i = 0; i < mobileList.size(); i++) {
        	mobiles.add(mobileList.get(i));
		}
        nvps.add(new BasicNameValuePair("mobiles", mobiles.toString()));
        
        
        JSONArray params = new JSONArray();
        params.add(user);
        params.add(datetime);
        nvps.add(new BasicNameValuePair("params",params.toString()));

        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);
        /*
         * 1.打印执行结果，打印结果一般会200、315、403、404、413、414、500
         * 2.具体的code有问题的可以参考官网的Code状态表
         */
        
        String result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println("sendNotice mobiles:" + mobiles.toString() + " result:" + result);
        return result;
    }
}
