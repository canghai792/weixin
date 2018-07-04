/*
 * FileName：ApplicatonStaticUtil.java 
 * <p>
 * Copyright (c) 2017-2020, <a href="http://www.webcsn.com">hermit (794890569@qq.com)</a>.
 * <p>
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/gpl-3.0.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.zzsoft.weixin.core.util;

import java.util.HashMap;
import java.util.Map;

public class ApplicatonStaticUtil {
	
	public static String THEME = "_app_static_theme_";
	

	private static Map<String,Object> appMap = new HashMap<String,Object>();
	
	public static void addAppStaticData(String key ,Object obj){
		appMap.put(key, obj);
	}
	
	public static Object getAppStaticData(String key){
		return appMap.get(key);
	}
	
	public static boolean hasAppStaticData(String key){
		return appMap.containsKey(key);
	}
	
}
