package com.allinpay.autotest.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesUtil {
	private static final Logger logger = Logger.getLogger(PropertiesUtil.class);
	private static Properties defaultProps;
	private static Properties customProps;
	static {
		loadProps();
	}

	synchronized static private void loadProps() {
		//logger.info("开始加载config properties文件内容.......");
		defaultProps = new Properties();
		InputStream in = null;
		try {
			// <!--第一种，通过类加载器进行获取properties文件流-->
			// in =
			// PropertyUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
			// <!--第二种，通过类进行获取properties文件流-->
			in = PropertiesUtil.class.getResourceAsStream("/yunsdk.properties");
			defaultProps.load(in);
		} catch (FileNotFoundException e) {
			logger.error("config.properties文件未找到");
		} catch (IOException e) {
			logger.error("出现IOException");
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				logger.error("config.properties文件流关闭出现异常");
			}
		}
		//logger.info("加载properties文件内容完成...........");
		//logger.info("properties文件内容：" + defaultProps);
	}

	public static Properties getDefaultConfigProperties() {
		if (null == defaultProps) {
			loadProps();
		}
		return defaultProps;
	}

	public static Properties getSpecificProperties(InputStream in) {
		
		try {
			// <!--第一种，通过类加载器进行获取properties文件流-->
			// in =PropertyUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
			// <!--第二种，通过类进行获取properties文件流-->
			//in = PropertyUtils.class.getResourceAsStream("/config.properties");
			
			if (null == customProps) {
				customProps = new Properties();
				customProps.load(in);
			}
		} catch (FileNotFoundException e) {
			logger.error("指定的properties文件未找到");
		} catch (IOException e) {
			logger.error("出现IOException");
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				logger.error("指定的properties文件流关闭出现异常");
			}
		}
		return customProps;
	}
	
	public static Properties getSpecificProperties(InputStream in, boolean reload) {
		
		try {
			// <!--第一种，通过类加载器进行获取properties文件流-->
			// in =PropertyUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
			// <!--第二种，通过类进行获取properties文件流-->
			//in = PropertyUtils.class.getResourceAsStream("/config.properties");
			
			if (null == customProps) {
				customProps = new Properties();
				customProps.load(in);
			}
			if(reload) {
				customProps = new Properties();
				customProps.load(in);
			}
		} catch (FileNotFoundException e) {
			logger.error("指定的properties文件未找到");
		} catch (IOException e) {
			logger.error("出现IOException");
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				logger.error("指定的properties文件流关闭出现异常");
			}
		}
		return customProps;
	}

	public static String getProperty(String key) {
		if (null == defaultProps) {
			loadProps();
		}
		return defaultProps.getProperty(key);
	}

	public static String getProperty(String key, String defaultValue) {
		if (null == defaultProps) {
			loadProps();
		}
		return defaultProps.getProperty(key, defaultValue);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(PropertiesUtil.getProperty("redis_ip"));
	}

}
