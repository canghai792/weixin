package com.zzsoft.weixin.wxcms.service.impl;

import com.zzsoft.weixin.dao.domain.MediaFiles;
import com.zzsoft.weixin.dao.domain.MsgBase;
import com.zzsoft.weixin.dao.mapper.MediaFilesDao;
import com.zzsoft.weixin.dao.mapper.MsgBaseDao;
import com.zzsoft.weixin.wxcms.service.MediaFileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class MediaFileServiceImpl implements MediaFileService {

	@Resource
	private MediaFilesDao mediaFilesDao;
	@Resource
	private MsgBaseDao baseDao;
	
	@Override
	public void add(MediaFiles entity) {
		MsgBase base = new MsgBase();
		base.setCreateTime(new Date());
		base.setMsgtype(entity.getMediaType());
		baseDao.add(base);
		//关联回复表
		entity.setBaseId(base.getId());
		//需要对base表添加数据
		mediaFilesDao.add(entity);
	}

	@Override
	public void deleteByMediaId(String mediaId) {
		MediaFiles media = mediaFilesDao.getFileByMediaId(mediaId);
		MsgBase base = new MsgBase();
		base.setId(media.getBaseId());
		baseDao.delete(base);
		mediaFilesDao.deleteByMediaId(mediaId);
	}

	@Override
	public MediaFiles getFileByMediaId(String mediaId) {
		return mediaFilesDao.getFileByMediaId(mediaId);
	}

	@Override
	public List<MediaFiles> getMediaListByPage(MediaFiles entity) {
		return mediaFilesDao.getMediaListByPage(entity);
	}

}
