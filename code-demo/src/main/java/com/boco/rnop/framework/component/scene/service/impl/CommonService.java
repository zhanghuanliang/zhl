package com.boco.rnop.framework.component.scene.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.rnop.framework.component.scene.entity.po.RegionCityPO;
import com.boco.rnop.framework.component.scene.entity.po.SceneTypePO;
import com.boco.rnop.framework.component.scene.mapper.def.ICommonMapper;
import com.boco.rnop.framework.component.scene.service.ICommonService;

/**
 * 
 * 通用
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月10日 下午3:43:00
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@Service
public class CommonService implements ICommonService {

	@Autowired
	private ICommonMapper commonMapper;

	@Override
	public List<RegionCityPO> selectRegion() {
		return commonMapper.selectRegion();
	}

	@Override
	public List<SceneTypePO> selectSceneType() {
		return commonMapper.selectSceneType();
	}

	@Override
	public List<RegionCityPO> selectRegionAndAll() {
		return commonMapper.selectRegionAndAll();
	}

}
