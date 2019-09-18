package com.zhangzlyuyx.fastssm.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * Controller 操作类
 *
 */
public class ControllerUtils {

	/**
	 * 获取客户端ip地址
	 * @param request
	 * @return
	 */
	public static String getRemoteAddr(HttpServletRequest request){
		//代理进来，则透过防火墙获取真实IP地址
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("X-Real-IP");
		}
		//如果没有代理，则获取真实ip
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getRemoteAddr();
		}
		return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
	}
	
	/**
	 * 获取 shiro 登录失败信息
	 * @param request
	 * @return
	 */
	public static Object getShiroLoginFailure(HttpServletRequest request){
		Object loginError =  request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		return loginError;
	}
	
	/**
	 * 设置 shiro 登录失败信息
	 * @param request
	 * @param e
	 */
	public static void setShiroLoginFailure(HttpServletRequest request, AuthenticationException e){
		request.setAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, e);
	}
	
	/**
	 * 获取请求上传的文件集合
	 * @param request 请求
	 * @return
	 */
	public static List<MultipartFile> getMultipartFiles(HttpServletRequest request){
		List<MultipartFile> multipartFiles = new ArrayList<MultipartFile>();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		if(!multipartResolver.isMultipart(request)){
			return multipartFiles;
		}
		// 转换成多部分request
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        Iterator<String> iterator = multiRequest.getFileNames();
        while (iterator.hasNext()) {
        	String name = iterator.next();
        	List<MultipartFile> files = multiRequest.getFiles(name);
        	for(MultipartFile file : files){
        		String fileName = file.getOriginalFilename();
        		if (StringUtils.isEmpty(fileName)) {
        			continue;
        		}
        		multipartFiles.add(file);
        	}
        }
        return multipartFiles;
	}
	
	/**
	 * 文件下载请求
	 * @param file 文件对象
	 * @param fileName 输出的文件名称(可空)
	 * @return
	 * @throws IOException
	 */
	public static ResponseEntity<byte[]> downloadFile(File file, String fileName) throws IOException{
		if (!file.isFile() || !file.exists()) {
            throw new IOException("文件不存在:" + file.getAbsolutePath());
        }
		if(StringUtils.isEmpty(fileName)){
			fileName = FileUtils.getFileName(file.getAbsolutePath());
		}
		byte[] buffer = FileUtils.readFileToByteArray(file);
		return downloadFile(buffer, fileName);
	}
	
	/**
	 * 文件下载请求
	 * @param buffer
	 * @param fileName 输出的文件名称
	 * @return
	 * @throws IOException
	 */
	public static ResponseEntity<byte[]> downloadFile(byte[] buffer, String fileName) throws IOException{
		if(StringUtils.isEmpty(fileName)){
			throw new IOException("下载的文件名称不能为空!");
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", URLEncoder.encode(fileName, "utf-8"));
		headers.add("Content-Length", String.valueOf(buffer.length));
		ResponseEntity<byte[]> entity = new ResponseEntity<>(buffer, headers, HttpStatus.OK);
		return entity;
	}
	
	/**
	 * 文件下载请求 
	 * @param response 响应对象
	 * @param file 文件
	 * @param fileName 输出文件名称
	 * @throws IOException
	 * @return 返回下载字节数
	 */
	public static long downloadFile(HttpServletResponse response, File file, String fileName) throws IOException{
		long downloadBytes = 0;
		if (!file.isFile() || !file.exists()) {
            throw new IOException("文件不存在:" + file.getAbsolutePath());
        }
		if(StringUtils.isEmpty(fileName)){
			fileName = FileUtils.getFileName(file.getAbsolutePath());
		}
		String contentType = new MimetypesFileTypeMap().getContentType(fileName);
		if(contentType == null || contentType.length() == 0){
			contentType = "application/octet-stream;charset=UTF-8";
		}
		response.reset();
		response.setContentType(contentType);
		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
		response.setHeader("Content-Length", String.valueOf(file.length()));
		
		InputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(file));
			ServletOutputStream outputStream = response.getOutputStream();
			byte[] buffer = new byte[1024 * 10];
			int readBytes = 0;
			while((readBytes = inputStream.read(buffer)) > 0){
				outputStream.write(buffer, 0, readBytes);
				downloadBytes+=readBytes;
			}
			outputStream.flush();
			outputStream.close();
			return downloadBytes;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}finally{
			inputStream.close();
		}
	}
	
	/**
	 * 文件下载请求 
	 * @param response 响应对象
	 * @param inputStream 输入流
	 * @param fileName 输出文件名称
	 * @throws IOException
	 * @return 返回下载字节数
	 */
	public static long downloadFile(HttpServletResponse response, InputStream inputStream, String fileName) throws IOException{
		long downloadBytes = 0;
		if(StringUtils.isEmpty(fileName)){
			throw new IOException("下载的文件名称不能为空!");
		}
		String contentType = new MimetypesFileTypeMap().getContentType(fileName);
		if(contentType == null || contentType.length() == 0){
			contentType = "application/octet-stream;charset=UTF-8";
		}
		response.reset();
		response.setContentType(contentType);
		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
		try {
			response.setHeader("Content-Length", String.valueOf(inputStream.available()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ServletOutputStream outputStream = response.getOutputStream();
			byte[] buffer = new byte[1024 * 10];
			int readBytes = 0;
			while((readBytes = inputStream.read(buffer)) > 0){
				outputStream.write(buffer, 0, readBytes);
				downloadBytes+=readBytes;
			}
			outputStream.flush();
			outputStream.close();
			return downloadBytes;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}finally{
			inputStream.close();
		}
	}
}
