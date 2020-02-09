package com.estrok;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;


public class HttpsUtil {
	
	public static String getHttpsConnection(String method, String url) {
		HttpsURLConnection conn = null;
		String result = "";

		try {
			conn = (HttpsURLConnection) new URL(url).openConnection();

			conn.setRequestMethod(method.toUpperCase());
			conn.setRequestProperty("User-Agent", "java-client");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			
			conn.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});

			// SSL setting
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, new TrustManager[] { new javax.net.ssl.X509TrustManager() {
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}
			} }, null);
			
			conn.setSSLSocketFactory(context.getSocketFactory());

//			String param = "param=1234&param2=abcd";

//			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
//			wr.writeBytes(param);
//			wr.flush();
//			wr.close();

//			Print response from host
			InputStream in = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				result += line;
			}
			
			
			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		
		return result;
	}

	public static String getHttpsConnectionJsonObj(String method, String url) {
		HttpsURLConnection conn = null;
		String result = "";

		try {
			conn = (HttpsURLConnection) new URL(url).openConnection();

			conn.setRequestMethod(method.toUpperCase());
			conn.setRequestProperty("User-Agent", "java-client");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			
			conn.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});

			// SSL setting
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, new TrustManager[] { new javax.net.ssl.X509TrustManager() {
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}
			} }, null);
			
			conn.setSSLSocketFactory(context.getSocketFactory());

//			String param = "param=1234&param2=abcd";

//			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
//			wr.writeBytes(param);
//			wr.flush();
//			wr.close();

//			Print response from host
			InputStream in = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				result += line;
			}
			
			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println( httpPostConncet("post", "https://www.biki.com/fe-ex-api/common/public_info_v4", null, null) );
//		Map<String, String> headers = new HashMap<String, String>();
//		headers.put("Content-Type", "application/x-www-form-urlencoded");
//		headers.put("authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL3Npc3RlbWtvaW4uY29tL2FwaS91c2VyL2NvbmZpcm0vMmZjb2RlIiwiaWF0IjoxNTU1NTU0OTM3LCJleHAiOjE1NTU2NDEzMzcsIm5iZiI6MTU1NTU1NDkzNywianRpIjoiMXIycTVQb21GbURTNzB2SyIsInN1YiI6NTIzMjcsInBydiI6Ijg3ZTBhZjFlZjlmZDE1ODEyZmRlYzk3MTUzYTE0ZTBiMDQ3NTQ2YWEifQ.zchK0i54WlcP9imfBCzGh_5R4NJjs8QhxmxY6H7g4ug");
//		
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("socket_id", "126158.22889567");
//		params.put("channel_name", "private-App.User.52327");
//		
//		doHttpConncet("post", "https://sistemkoin.com/broadcasting/auth", headers, params);
	}
	
	private static final String USER_AGENT = "Mozilla/5.0";
	
	public static String doHttpConncet(String method, String url, Map<String, String> headers, Map<String, String> params) {
		HttpsURLConnection conn = null;
		String result = "";
		
		try {
			conn = (HttpsURLConnection) new URL(url).openConnection();

			conn.setRequestMethod(method.toUpperCase());
			conn.setRequestProperty("User-Agent", USER_AGENT);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			
			if( null != headers ) {
				Set<String> keys = headers.keySet();
				for (String key : keys) {
					conn.setRequestProperty(key, headers.get(key));
				}
			}
			
			
			conn.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});

			// SSL setting
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, new TrustManager[] { new javax.net.ssl.X509TrustManager() {
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}
			} }, null);
			
			conn.setSSLSocketFactory(context.getSocketFactory());

			String param = "";
			
			if( null != params ) {
				Set<String> keys = params.keySet();
				for (String key : keys) {
					param += key + "=" + params.get(key) + "&";
				}
				
				DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
				wr.writeBytes(param);
				wr.flush();
				wr.close();
			}
			
			InputStream in = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				result += line;
			}
			
			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		
		return result;
	}
	
	public static String httpPostConncet(String method, String url, Map<String, String> headers, Map<String, String> params) {
		HttpsURLConnection conn = null;
		String result = "";
		
		try {
			conn = (HttpsURLConnection) new URL(url).openConnection();

			conn.setRequestMethod(method.toUpperCase());
			conn.setRequestProperty("User-Agent", USER_AGENT);
			conn.setDoOutput(true);
			
			if( null != headers ) {
				Set<String> keys = headers.keySet();
				for (String key : keys) {
					conn.setRequestProperty(key, headers.get(key));
				}
			}
			
			
			conn.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});

			// SSL setting
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, new TrustManager[] { new javax.net.ssl.X509TrustManager() {
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}
			} }, null);
			
			conn.setSSLSocketFactory(context.getSocketFactory());

			String param = "";
			
			if( null != params ) {
				Set<String> keys = params.keySet();
				for (String key : keys) {
					param += key + "=" + params.get(key) + "&";
				}
				
				DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
				wr.writeBytes(param);
				wr.flush();
				wr.close();
			}
			
			InputStream in = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				result += line;
			}
			
			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		
		return result;
	}
	
}
