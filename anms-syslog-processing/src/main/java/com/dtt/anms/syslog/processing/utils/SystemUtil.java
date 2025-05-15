/**  
 * Project Name:Hive-data-batch  
 * File Name:SystemUtil.java  
 * Package Name:com.hive.data.utils  
 * Date:2021年12月1日上午10:52:16  
 * Copyright (c) 2021, chenzhou1025@126.com All Rights Reserved.  
 *  
*/  
  
package com.dtt.anms.syslog.processing.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;

/**  
 * ClassName:SystemUtil <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2021年12月1日 上午10:52:16 <br/>  
 * @author   yaoshuai  
 * @version    
 * @since    JDK 1.6  
 * @see         
 */
@Slf4j
public class SystemUtil {
	



	public static void sleep(long millon) {
		try {
			Thread.sleep(millon);
		} catch (Exception e) {
			log.error("休眠出错:",e);  
			e.printStackTrace();  
			
		}
	}
	
	public static String getAppPath(){
		URL baseClassPath = Thread.currentThread().getContextClassLoader().getResource("");
		String filePath = new File(baseClassPath.getFile()).getAbsolutePath();
		return filePath;
	}

	public static String getServerIp(){
		try {
			InetAddress address = InetAddress.getLocalHost();
			return address.getHostAddress();
		}catch (Exception e){

		}
		return "127.0.0.1";
	}
}
  
