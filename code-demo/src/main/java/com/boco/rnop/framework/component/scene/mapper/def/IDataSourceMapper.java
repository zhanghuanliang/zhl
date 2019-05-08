package com.boco.rnop.framework.component.scene.mapper.def;

import java.util.List;

import com.boco.rnop.framework.component.scene.entity.po.CitySceneDataCountPO;
import com.boco.rnop.framework.component.scene.entity.po.SceneTypeDataCountPO;

/**
 * 
 * 数据资源统计
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月10日 下午4:15:40
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
public interface IDataSourceMapper {

	/**
	 * 统计各类型地理场景数据量
	 * 
	 * @return
	 */
	public List<SceneTypeDataCountPO> countSceneDataByType();

	/**
	 * 统计各地市地理场景数据
	 * 
	 * @return
	 */
	public List<CitySceneDataCountPO> countSceneDataByRegion();

}
