package com.liuzy.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * JSON专用HTTP工具
 * <pre>键值POST，发送的是k1=v2&k2=v2类型的BODY。
 * HTTP http = new HTTP();
 * http.put2body("k1", "v1")
 *     .put2body("k2", "v2")
 *     .doPOST("http://127.0.0.1/test");
 * 键值转JSON后POST，发送的是{"k1":"v1","k2":"v2"}
 * http.put2header("Content-Type", "application/json")
 *     .put2body("k1", "v1")
 *     .put2body("k2", "v2")
 *     .bodyMap2Json()
 *     .doPOST("http://127.0.0.1/test");
 * 字符串POST（可直接传JSON，然后添加application/json头）
 * http.put2header("Content-Type", "application/json")
 *     .put2body("{\"k1\":\"v1\",\"k2\":\"v2\"}")
 *     .doPOST("http://127.0.0.1/test");</pre>
 * @author liuzy
 * @since 2015年10月17日
 */
public class HTTP {
	private Map<String, String> urlMap = new HashMap<String, String>();
	private Map<String, String> headerMap = new HashMap<String, String>();
	private Map<String, String> bodyMap = new HashMap<String, String>();
	private boolean bodyMap2Json;
	private String bodyString;
	private byte[] bodyBytes;
	protected int timeout = 5000;
	private String charset = "UTF-8";
	public HTTP put2url(String key, String value) {
		this.urlMap.put(key, value);
		return this;
	}
	public HTTP put2header(String key, String value) {
		this.headerMap.put(key, value);
		return this;
	}
	public HTTP put2body(String key, String value) {
		this.bodyMap.put(key, value);
		return this;
	}
	public HTTP bodyMap2Json() {
		this.bodyMap2Json = true;
		return this;
	}
	public HTTP put2body(String body) {
		this.bodyMap.clear();
		this.bodyString = body;
		this.bodyBytes = null;
		return this;
	}
	public HTTP put2body(byte[] body) {
		this.bodyMap.clear();
		this.bodyString = null;
		this.bodyBytes = body;
		return this;
	}
	public void setReadGBK() {
		this.charset = "GBK";
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public String doGET(String url) {
		return request(url, "GET");
	}
	public String doGET(String url, Map<String, String> urlMap) {
		if (urlMap != null)
			this.urlMap = urlMap;
		return request(url, "GET");
	}
	public String doPOST(String url) {
		return request(url, "POST");
	}
	public String doPOST(String url, Map<String, String> urlMap, Map<String, String> headerMap, Map<String, String> bodyMap, boolean bodyMap2Json) {
		this.bodyMap2Json = bodyMap2Json;
		return doPOST(url, urlMap, headerMap, bodyMap);
	}
	public String doPOST(String url, Map<String, String> urlMap, Map<String, String> headerMap, Map<String, String> bodyMap) {
		if (urlMap != null)
			this.urlMap = urlMap;
		if (headerMap != null)
			this.headerMap = headerMap;
		if (bodyMap != null) {
			this.bodyMap = bodyMap;
			this.bodyString = null;
			this.bodyBytes = null;
		}
		return request(url, "POST");
	}
	public String doPOST(String url, Map<String, String> urlMap, Map<String, String> headerMap, String bodyString) {
		if (urlMap != null)
			this.urlMap = urlMap;
		if (headerMap != null)
			this.headerMap = headerMap;
		if (bodyString != null) {
			this.bodyMap.clear();
			this.bodyString = bodyString;
			this.bodyBytes = null;
		}
		return request(url, "POST");
	}
	public String doPOST(String url, Map<String, String> urlMap, Map<String, String> headers, byte[] bodyBytes) {
		if (urlMap != null)
			this.urlMap = urlMap;
		if (headers != null)
			this.headerMap = headers;
		if (bodyBytes != null) {
			this.bodyMap.clear();
			this.bodyString = null;
			this.bodyBytes = bodyBytes;
		}
		return request(url, "POST");
	}
	protected String request(String url, String method) {
		url = setUrlParams(url);
		log(method, url);
		String result = null;
		HttpURLConnection conn = null;
		int code = 0;
		try {
			URL uri = new URL(url);
			conn = (HttpURLConnection) uri.openConnection();
			conn.setRequestMethod(method);
			conn.setConnectTimeout(timeout);
			conn.setReadTimeout(timeout);
			conn.setUseCaches(false);
			setHeader(conn);
			writeBody(conn);
			conn.connect();
			code = conn.getResponseCode();
			InputStream inputStream = conn.getInputStream();
			result = readStream(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
			result = "{\"status\":" + code + ",\"message\":\"" + e.getMessage() + "\"}";
		} finally {
			conn.disconnect();
			clear();
		}
		log("RESUT", result);
		return result;
	}
	protected String setUrlParams(String url) {
		if (urlMap.isEmpty())
			return url;
		if (!url.contains("?"))
			url += "?";
		StringBuilder sb = new StringBuilder();
		for (String key : urlMap.keySet())
			sb.append(key).append("=").append(urlMap.get(key)).append("&");
		return url + sb.substring(0, sb.length() - 1);
	}
	protected void setHeader(HttpURLConnection conn) {
		if (headerMap.isEmpty())
			return;
		String head = "";
		for (String key : headerMap.keySet()) {
			String value = headerMap.get(key);
			head += key + "=" + value + ", ";
			conn.addRequestProperty(key, value);
		}
		log("HEAD", head.substring(0, head.length() - 2));
	}
	protected void writeBody(HttpURLConnection conn) throws IOException {
		if (bodyBytes == null && bodyString == null && bodyMap.isEmpty()) {
			return;
		}
		conn.setDoOutput(true);
		OutputStream outputStream = conn.getOutputStream();
		if (bodyBytes != null) {
			log("BODY", "body is bytes, you can use base64 to print.");
			outputStream.write(bodyBytes);
		} else {
			if (bodyString == null && !bodyMap.isEmpty()) {
				if (bodyMap2Json)
					bodyString = map2Json(bodyMap);
				else
					bodyString = map2KV(bodyMap);
			}
			if (bodyString != null) {
				log("BODY", bodyString);
				outputStream.write(bodyString.getBytes());
			}
		}
		outputStream.flush();
		outputStream.close();
	}
	private String map2Json(Map<String, String> map) {
		if (map.isEmpty())
			return "{}";
		StringBuilder json = new StringBuilder();
		for (String key : map.keySet())
			json.append("\"").append(key).append("\":\"").append(map.get(key)).append("\",");
		return "{" + json.substring(0, json.length() - 1) + "}";
	}
	private String map2KV(Map<String, String> map) {
		if (map.isEmpty())
			return "";
		StringBuilder sb = new StringBuilder();
		for (String key : map.keySet())
			sb.append(key).append("=").append(map.get(key)).append("&");
		return sb.substring(0, sb.length() - 1);
	}
	protected String readStream(InputStream inputStream) throws IOException {
		Reader reader = new InputStreamReader(inputStream, charset);
		StringBuffer buffer = new StringBuffer();
        char[] tmp = new char[1024];
        int len;
        while((len = reader.read(tmp)) != -1)
            buffer.append(tmp, 0, len);
        inputStream.close();
        reader.close();
        return buffer.toString();
	}
	public HTTP clear() {
		urlMap.clear();
		headerMap.clear();
		bodyMap.clear();
		bodyString = null;
		bodyBytes = null;
		return this;
	}
	public static void log(String prefix, String msg) {
		System.out.println(String.format("[%5s] %s", prefix, msg));
	}
}
