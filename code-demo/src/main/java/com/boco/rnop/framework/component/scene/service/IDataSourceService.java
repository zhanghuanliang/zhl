package com.boco.rnop.framework.component.scene.service;

import java.util.List;

import com.boco.rnop.framework.component.scene.entity.CascaderInfo;
import com.boco.rnop.framework.component.scene.entity.po.BaiduAkInfoPO;
import com.boco.rnop.framework.component.scene.entity.po.CitySceneDataCountPO;
import com.boco.rnop.framework.component.scene.entity.po.SceneTypeDataCountPO;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3GisGeometry;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3MapAkType;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3PoiTypeRelation;

/**
 * 
 * 场景数据资源统计
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月10日 下午4:20:11
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
public interface IDataSourceService {

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

	/**
	 * 查询全部ak信息
	 * 
	 * @return
	 */
	public List<BaiduAkInfoPO> selectAllAkInfo();

	/**
	 * 查询百度poi类型信息
	 * 
	 * @return
	 */
	public List<CascaderInfo> selectAllBaiduPoiTypeInfo(Long parentId);

	/**
	 * 新增网优场景信息
	 * 
	 * @param list
	 * @return
	 */
	public Integer insertSceneTypeDictionaryInfo(List<Tv3PoiTypeRelation> list);

	/**
	 * 查询网优场景字典信息
	 * 
	 * @return
	 */
	public List<Tv3PoiTypeRelation> selectSceneTypeDictionaryInfo();

	/**
	 * 查询行政区轮廓
	 * 
	 * @param districtId
	 * @return
	 */
	public Tv3GisGeometry selectRegionOutline(String regionName);

	/**
	 * 新增行政区轮廓
	 * 
	 * @param tv3GisDistrictGeometryPO
	 * @return
	 */
	public Integer insertRegionOutline(Tv3GisGeometry tv3GisGeometry);

	/**
	 * 查询ak类型信息
	 * 
	 * @return
	 */
	public List<Tv3MapAkType> selectAkTypeInfo();

	/**
	 * 删除网优场景字典信息
	 * 
	 * @param intId
	 * @return
	 */
	public Integer deleteSceneTypeDictionaryInfo(Long intId);

	/**
	 * 修改网优场景字典信息
	 * 
	 * @param tv3PoiTypeRelation
	 * @return
	 */
	public Integer updateSceneTypeDictionaryInfo(Tv3PoiTypeRelation tv3PoiTypeRelation);

}
