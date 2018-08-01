package com.zzsoft.weixin.wxcms.service;

import com.zzsoft.weixin.dao.domain.MediaFiles;

import java.util.List;

/**
 * 语音和视频逻辑层
 * @author nigulading
 *
 */
public interface MediaFileService {

	/**
	 *  分页
	 * @param entity
	 * @return
	 */
	public List<MediaFiles> getMediaListByPage(MediaFiles entity);
	/**
	 * 添加
	 * @param entity
	 */
	public void add(MediaFiles entity);
	
	/**
	 * 删除
	 * @param mediaId
	 */
	public void deleteByMediaId(String mediaId);
	/**
	 * 获取单条数据
	 * @param mediaId
	 * @return
	 */
	public MediaFiles getFileByMediaId(String mediaId);
}
