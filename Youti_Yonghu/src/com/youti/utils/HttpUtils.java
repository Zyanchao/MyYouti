package com.youti.utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

public class HttpUtils {
    private static     AsyncHttpClient client =new AsyncHttpClient();    //ʵ�����
    private static     AsyncHttpClient clientSync =new SyncHttpClient();    //ʵ�����
    static
    {
        client.setTimeout(11000);   //�������ӳ�ʱ��������ã�Ĭ��Ϊ10s
    }
//    public static void get(String urlString,AsyncHttpResponseHandler res)    //��һ������url��ȡһ��string����
//    {
//        client.get(urlString, res);
//    }
//    public static void get(String urlString,RequestParams params,AsyncHttpResponseHandler res)   //url��������
//    {
//        client.get(urlString, params,res);
//    }
//    public static void get(String urlString,JsonHttpResponseHandler res)   //��������ȡjson�����������
//    {
//        client.get(urlString, res);
//    }
//    public static void get(String urlString,RequestParams params,JsonHttpResponseHandler res)   //������ȡjson�����������
//    {
//        client.get(urlString, params,res);
//    }
//    public static void get(String uString, BinaryHttpResponseHandler bHandler)   //�������ʹ�ã��᷵��byte���
//    {
//        client.get(uString, bHandler);
//    }
    public static AsyncHttpClient getClient()
    {
        return client;
    }
    
    public static void post(String urlString,RequestParams params,AsyncHttpResponseHandler res)
    {
    	 client.post(urlString, params,res);
    	 client.setMaxRetriesAndTimeout(0, 120000);
    }
    
    public static void postSync(String urlString,RequestParams params,AsyncHttpResponseHandler res)
    {
    	 clientSync.post(urlString, params, res);
    }
    
}