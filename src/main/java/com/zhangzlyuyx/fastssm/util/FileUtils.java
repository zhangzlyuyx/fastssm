package com.zhangzlyuyx.fastssm.util;

import java.io.File;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;

import cn.hutool.core.io.FileUtil;

/**
 * 文件访问操作类
 *
 */
public class FileUtils {

	/**
	 * 获取文件对象(自动识别相对/绝对路径)
	 * @param path 文件路径
	 * @return 返回文件对象
	 */
	public static File file(String path){
		return FileUtil.file(path);
	}
	
	/**
	 * 判断文件是否存在
	 * @param path 文件路径
	 * @return 返回布尔值
	 */
	public static boolean exists(String path){
		return FileUtil.exist(path);
	}
	
	/**
	 * 获取 Web 根目录
	 * @return 返回路径字符串
	 */
	public static String getWebRoot(){
		return FileUtil.getWebRoot().getAbsolutePath();
	}
	
	/**
	 * 获取文件名
	 * @param filePath 文件路径
	 * @return 返回文件名 
	 */
	public static String getFileName(String filePath){
		return FileUtil.getName(filePath);
	}
	
	/**
	 * 获取扩展名
	 * @param fileName 文件名
	 * @return 返回扩展名
	 */
	public static String getExtName(String fileName){
		return FileUtil.extName(fileName);
	}
	
	/**
	 * 获取系统换行分割符
	 * @return 返回字符
	 */
	public static String getLineSeparator(){
		return FileUtil.getLineSeparator();
	}
	
	/**
	 * 读取文件二进制数据
	 * @param file 文件对象
	 * @return 返回字节数组
	 */
	public static byte[] readFileToByteArray(File file){
		return FileUtil.readBytes(file);
	}
	
	/**
	 * 写出文件
	 * @param src 输入文件流
	 * @param dest 输出文件对象
	 * @return 返回文件对象
	 */
	public static File writeFromStream(InputStream src, File dest){
		return FileUtil.writeFromStream(src, dest);
	}
	
	/**
	 * 拷贝文件
	 * @param src 源文件
	 * @param dest 目标文件
	 * @param options 选择
	 * @return 返回文件对象
	 */
	public static File copyFile(File src, File dest, StandardCopyOption options){
		return FileUtil.copyFile(src, dest, options);
	}
	
	/**
	 * 当前是否为 windows 系统
	 * @return 返回布尔值
	 */
	public static boolean isWindows(){
		return FileUtil.isWindows();
	}
}
