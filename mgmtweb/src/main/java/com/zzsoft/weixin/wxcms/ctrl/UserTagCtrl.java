/**
 * Copyright &copy; 2017-2018 <a href="http://www.webcsn.com">webcsn</a> All rights reserved.
 *
 * @author hermit
 * @date 2018-04-17 10:54:58
 */
package com.zzsoft.weixin.wxcms.ctrl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zzsoft.weixin.core.common.BaseCtrl;
import com.zzsoft.weixin.core.common.Constants;
import com.zzsoft.weixin.core.exception.WxErrorException;
import com.zzsoft.weixin.core.util.AjaxResult;
import com.zzsoft.weixin.wxapi.process.MpAccount;
import com.zzsoft.weixin.wxapi.process.WxApiClient;
import com.zzsoft.weixin.wxcms.todo.WxMemoryCacheClient;
import com.zzsoft.weixin.dao.domain.AccountFans;
import com.zzsoft.weixin.dao.domain.UserTag;
import com.zzsoft.weixin.wxcms.service.AccountFansService;
import com.zzsoft.weixin.wxcms.service.UserTagService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author fuziKong
 * @version 2.0
 * @date 2018-05-30 10:54:58
 */
@Controller
@RequestMapping("/userTag")
public class UserTagCtrl extends BaseCtrl {

	@Autowired
	private UserTagService entityService;
	@Autowired
	private AccountFansService accountFansService;
	
	/**
	 * 根据用户标签获取粉丝列表
	 * @return
	 * @throws WxErrorException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getUserListByTag")
	@ResponseBody
	public AjaxResult getUserListByTag(Integer id) throws WxErrorException {
		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();//获取缓存中的唯一账号
		JSONObject  tagId = new JSONObject();
		tagId.put("tagid", id);
		tagId.put("next_openid", "");
		JSONObject jsonObjUserList = WxApiClient.getUserListByTag(tagId.toString(), mpAccount);
		if(jsonObjUserList !=null && jsonObjUserList.containsKey("data") ) {
			JSONObject  data =  (JSONObject) jsonObjUserList.get("data");
			JSONArray openidArray = data.getJSONArray("openid");
			String js=JSONObject.toJSONString(openidArray);
			List<String> list = JSONObject.parseArray(js, String.class);
			List<AccountFans> fansList = new ArrayList<AccountFans>();
			for (String  openId : list) {
				AccountFans fans = new AccountFans();
				fans.setOpenId(openId);
				fansList.add(fans);
			}
			 fansList = accountFansService.getFansByOpenIdListByPage(fansList);
			return AjaxResult.success(fansList);
		}
		AjaxResult result = new AjaxResult();
		result.setMsg("没有数据");
		return result;
	}
	
	
	/**
	 * 同步用户标签列表
	 * @return
	 */
	@RequestMapping(value = "/syncUserTagList")
	@ResponseBody
	public AjaxResult syncUserTagList(){
		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();//获取缓存中的唯一账号
		if(mpAccount != null){
			boolean flag = entityService.syncUserTagList(mpAccount);
			if(flag){
				return AjaxResult.success();
			}
		}
		return AjaxResult.failure();
	}
	
	/**
	 * 根据Id查询用户标签
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/getById")
	@ResponseBody
	public AjaxResult getById(Integer id){
		UserTag  userTag = entityService.getById(id);
		return AjaxResult.success(userTag);
	}

	/**
	 * 分页查询
	 * @param searchEntity
	 * @return
	 */
	@RequestMapping(value = "/listForPage")
	@ResponseBody
	public AjaxResult listForPage(UserTag searchEntity) {
		List<UserTag> list = entityService.listForPage(searchEntity);
		if (CollectionUtils.isEmpty(list)) {
			return AjaxResult.success();
		}
		return getResult(searchEntity,list);
	}

	/**
	 * 修改/添加
	 * @param entity
	 * @return
	 * @throws WxErrorException 
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public AjaxResult update(UserTag entity) throws WxErrorException{
		if (entity.getId() != null) {
			entityService.update(entity);
			//更新成功
			return AjaxResult.updateSuccess();
		} else {
			//添加分两步
			//1. 调用微信API添加 
			MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();//获取缓存中的唯一账号
			JSONObject  tagName = new JSONObject();
			tagName.put("name", entity.getName());
			JSONObject  tagjson = new JSONObject();
			tagjson.put("tag", tagName);
			String userTags = tagjson.toString();
			JSONObject userTag = WxApiClient.createUserTag(userTags, mpAccount);
			//2.添加到本地库
			if(userTag != null) {
				JSONObject returnUserTag = (JSONObject) userTag.get("tag");
				entity = JSONObject.parseObject(returnUserTag.toJSONString(), UserTag.class);
				entityService.add(entity);
				return AjaxResult.saveSuccess();
			}
			return AjaxResult.failure(Constants.MSG_ERROR);
		}
	}

	/**
	 * 删除
	 * @param entity
	 * @return
	 * @throws WxErrorException 
	 */
	@RequestMapping(value = "/deleteById")
	@ResponseBody
	public AjaxResult deleteById(UserTag entity) {
		//1.删除微信服务器的用户标签
		if(deleteUserTag(entity.getId())){
			//2.删除本地数据库的用户标签
			entityService.delete(entity);
			return AjaxResult.deleteSuccess();
		}
		return AjaxResult.failure("用户标签删除失败！");
	}
	
	/**
	 * 批量删除
	 * @param String[] ids
	 * @return
	 * @throws WxErrorException 
	 */
	@RequestMapping(value = "/deleteBatchIds")
	@ResponseBody
	public AjaxResult deleteBatchIds(String [] ids)  {
		if(null != ids && ids.length>0) {
			int nums = 0;
			for (String id : ids) {
				if(deleteUserTag(Integer.parseInt(id))) {
					nums++;
				}
			}
			if(nums == ids.length) {
				entityService.deleteBatchIds(ids);				
			}
			return AjaxResult.deleteSuccess();
		}else {
			return AjaxResult.failure("用户标签批量删除失败");
		}
	}
	/**
	 * 删除微信服务器的用户标签
	 * @param id
	 * @return boolean
	 */
	private boolean deleteUserTag(Integer id) {
		MpAccount mpAccount = WxMemoryCacheClient.getMpAccount();//获取缓存中的唯一账号
		JSONObject  tag = new JSONObject();
		JSONObject  tagId = new JSONObject();
		tagId.put("id", id);
		tag.put("tag", tagId);
		JSONObject result=null ;
		try {
			result = WxApiClient.deleteUserTag(tag.toJSONString(), mpAccount);
		} catch (WxErrorException e) {
			e.printStackTrace();
		}
		if(result!=null && result.containsKey("errmsg")) {
			if("ok".equals(result.get("errmsg").toString())){
				return true;
			}
		}
		return false;
	}
}