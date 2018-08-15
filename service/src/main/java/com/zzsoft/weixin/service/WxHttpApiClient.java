/*
 * FileName：WxApiClient.java 
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
package com.zzsoft.weixin.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zzsoft.weixin.core.common.Identities;
import com.zzsoft.weixin.core.exception.WxError;
import com.zzsoft.weixin.core.exception.WxErrorException;
import com.zzsoft.weixin.core.util.DateUtil;
import com.zzsoft.weixin.dao.domain.AccountFans;
import com.zzsoft.weixin.dao.domain.MsgNews;
import com.zzsoft.weixin.dao.util.WxUtil;
import com.zzsoft.weixin.wxapi.process.*;
import com.zzsoft.weixin.wxapi.vo.Material;
import com.zzsoft.weixin.wxapi.vo.MaterialArticle;
import com.zzsoft.weixin.wxapi.vo.MaterialItem;
import com.zzsoft.weixin.wxapi.vo.TemplateMessage;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 微信 客户端，统一处理微信相关接口
 */

public interface WxHttpApiClient {
    
    // 获取accessToken
    public String getAccessToken(MpAccount mpAccount)  throws WxErrorException ;
    
    // 获取jsTicket
    public String getJSTicket(MpAccount mpAccount)
        throws WxErrorException ;
    
    // 获取OAuthAccessToken
    public OAuthAccessToken getOAuthAccessToken(MpAccount mpAccount, String code)
        throws WxErrorException ;
    
    // 获取openId
    public String getOAuthOpenId(MpAccount mpAccount, String code)
        throws WxErrorException ;
	//创建用户标签
	public JSONObject createUserTag(String userTags,MpAccount mpAccount ) throws WxErrorException ;
	
	//获取标签下粉丝列表
	public JSONObject getUserListByTag(String tagId,MpAccount mpAccount) throws WxErrorException ;
	
	//删除用户标签
	public JSONObject deleteUserTag(String tagId,MpAccount mpAccount) throws WxErrorException ;
	
    // 发布菜单
    public JSONObject publishMenus(String menus, MpAccount mpAccount)
        throws WxErrorException ;
    
    // 创建个性化菜单
    public JSONObject publishAddconditionalMenus(String menus, MpAccount mpAccount)
        throws WxErrorException ;
    
    // 删除菜单
    public JSONObject deleteMenu(MpAccount mpAccount)
        throws WxErrorException ;
    
    // 根据openId获取粉丝信息
    public AccountFans syncAccountFans(String openId, MpAccount mpAccount)
        throws WxErrorException ;
    
    /**
     * 获取素材
     * @param mediaType 素材类型
     * @param offset 开始位置
     * @param count 获取数量
     * @return
     */
    public Material syncBatchMaterial(MediaType mediaType, Integer offset, Integer count, MpAccount mpAccount)
        throws WxErrorException ;
    
    // 上传图文消息
    public JSONObject uploadNews(List<MsgNews> msgNewsList, MpAccount mpAccount)
        throws Exception ;
    
    /**
     * 根据openid群发接口
     * 
     * @param mediaId：素材的id；通过素材管理,或者上传素材获取
     * @param msgType
     * @param mpAccount
     * @return
     */
    public JSONObject massSendByOpenIds(List<String> openids, String mediaId, MsgType msgType, MpAccount mpAccount)
        throws WxErrorException ;
    
    /**
     * 根据openid群发文本消息
     * 
     * @param openids
     * @param content
     * @param mpAccount
     * @return
     */
    public JSONObject massSendTextByOpenIds(List<String> openids, String content, MpAccount mpAccount)
        throws WxErrorException ;
    
    /**
     * 发送客服消息
     * 
     * @param openid
     * @param content 消息内容
     * @return
     */
    public JSONObject sendCustomTextMessage(String openid, String content, MpAccount mpAccount)
        throws WxErrorException ;

	/**
	 * 发送客服消息
	 *
	 * @param openid
	 * @param mpAccount 消息内容
	 * @return
	 */
	public JSONObject sendCustomNews(String openid, MsgNews msgNews, MpAccount mpAccount)
			throws WxErrorException ;

    /**
     * 发送模板消息
     * 
     * @param tplMsg
     * @param mpAccount 消息内容
     * @return
     */
    public JSONObject sendTemplateMessage(TemplateMessage tplMsg, MpAccount mpAccount)
        throws WxErrorException ;
    
    /**
     * 创建临时二维码
     * 
     * @param expireSecodes 该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒。
     * @param scene 临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000)
     * @return
     */
    public byte[] createQRCode(Integer expireSecodes, Integer scene, MpAccount mpAccount)
        throws WxErrorException ;
    
    // 创建永久字符串二维码
    public byte[] createQRCodeLimit(String qrcodeStr, MpAccount mpAccount)
        throws WxErrorException ;
    
    // 上传永久图片
    public JSONObject uploadMaterialImg(String filePath, MpAccount mpAccount)
        throws Exception ;
    
    // 新增微信永久素材
    public JSONObject addMaterial(String filePath, String materialType, MpAccount mpAccount)
        throws Exception ;
    
    /**
     * 根据media_id获取永久图文素材
     *
     * @param media_id
     * @param mpAccount
     * @return
     */
    public JSONObject getMaterial(String media_id, MpAccount mpAccount)
        throws WxErrorException ;
    
    /**
     * 根据media_id删除永久图文素材
     * 
     * @param media_id
     * @param mpAccount
     * @return
     */
    public JSONObject deleteMaterial(String media_id, MpAccount mpAccount)
        throws WxErrorException ;


    // 新增永久图文素材
    public JSONObject addNewsMaterial(List<MsgNews> msgNewsList, String mediaId, MpAccount mpAccount)
        throws Exception ;
    
    // 修改永久图文素材
    public JSONObject updateNewsMaterial(List<MsgNews> msgNewsList, int index, String mediaId, MpAccount mpAccount)
        throws Exception ;
    
    public JSONObject updateNewsMaterial2(JSONObject jsonObj, int index, String mediaId, MpAccount mpAccount)
        throws Exception ;
    
    // 新增多图文永久素材
    public JSONObject addMoreNewsMaterial(List<MsgNews> msgNewsList, MpAccount mpAccount)
        throws Exception ;
    
    // 新增多图文永久素材
    public JSONObject addMoreNewsMaterial2(JSONArray arryarticles, MpAccount mpAccount)
        throws Exception ;

    /**
     * 构造微信JSSDK支付参数，返回到页面
     */
    public Map<String, String> getWSJSPayPara(MpAccount mpAccount, String openid, String timestamp, String nonceStr) ;

	/*public void main(String[] args) {
        String appid = "wx91961db8b6273777";
        String appsecret = "7d0377b8b30d4b3df4ba46bb7febc793";
        String mch_id = "1317476101";
        String partnerkey = "abcdefghijklmnopqrstuvwxyz123456";// 在微信商户平台pay.weixin.com里自己生成的那个key
        MpAccount mpAccount = new MpAccount();
        mpAccount.setAppid(appid);
        mpAccount.setAppsecret(appsecret);
        
        String openid = "otLBWs_uiGnrWBGgHEemPZTQLatE";
        
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonceStr = Identities.getRandomString(8);
        getWSJSPayPara(mpAccount, openid, timestamp, nonceStr);
    }*/
}
