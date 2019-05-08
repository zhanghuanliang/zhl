package com.boco.rnop.framework.component.scene.mapper.def;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.boco.rnop.framework.component.scene.entity.po.RegionCityPO;
import com.boco.rnop.framework.component.scene.entity.po.SceneTypePO;

/**
 * 
 * 通用
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月10日 下午3:31:57
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
public interface ICommonMapper {

	/**
	 * 查询地市（不带全省）
	 * 
	 * @param provinceId
	 * @return
	 */
	public List<RegionCityPO> selectRegion();

	/**
	 * 查询地市（带全省）
	 * 
	 * @return
	 */
	List<RegionCityPO> selectRegionAndAll();

	/**
	 * 查询类型字典表网优场景类型
	 * 
	 * @return
	 */
	public List<SceneTypePO> selectSceneType();

	/**
	 * 根据省名称模糊查询省编号
	 * 
	 * @param provinceName
	 * @return
	 */
	public Long selectProvinceIdByName(@Param("provinceName") String provinceName);

	/**
	 * 根据地市名称模糊查询省编号
	 * 
	 * @param regionName
	 * @return
	 */
	public Long selectRegionIdByName(@Param("regionName") String regionName);

	/**
	 * 根据区县名称模糊查询省编号
	 * 
	 * @param cityName
	 * @return
	 */
	public Long selectCityIdByName(@Param("cityName") String cityName);
}
