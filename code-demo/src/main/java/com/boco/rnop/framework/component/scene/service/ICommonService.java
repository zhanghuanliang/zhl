package com.boco.rnop.framework.component.scene.service;

import java.util.List;

import com.boco.rnop.framework.component.scene.entity.po.RegionCityPO;
import com.boco.rnop.framework.component.scene.entity.po.SceneTypePO;

/**
 * 
 * 通用
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月10日 下午3:25:47
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
public interface ICommonService {

	/**
	 * 查询地市（不带全省）
	 * 
	 * @return
	 */
	public List<RegionCityPO> selectRegion();

	/**
	 * 查询类型字典表网优场景类型
	 * 
	 * @return
	 */
	public List<SceneTypePO> selectSceneType();

	/**
	 * 查询地市（带全省）
	 * 
	 * @return
	 */
	public List<RegionCityPO> selectRegionAndAll();

}
