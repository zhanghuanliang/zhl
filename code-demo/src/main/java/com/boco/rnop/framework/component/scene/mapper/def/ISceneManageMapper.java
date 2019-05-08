package com.boco.rnop.framework.component.scene.mapper.def;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.boco.rnop.framework.component.scene.entity.po.DefaultSceneTreeInfoPO;
import com.boco.rnop.framework.component.scene.entity.po.IniFileInfoPO;
import com.boco.rnop.framework.component.scene.entity.po.OldIniFileInfoPO;
import com.boco.rnop.framework.component.scene.entity.po.SceneBaseInfoPO;
import com.boco.rnop.framework.component.scene.entity.po.SceneDetailedInfoPO;
import com.boco.rnop.framework.component.scene.entity.po.SceneGridCountInfoPO;
import com.boco.rnop.framework.component.scene.entity.po.SceneRelateCellDetaildInfoPO;
import com.boco.rnop.framework.component.scene.entity.po.SceneTreeIdsNamesPO;
import com.boco.rnop.framework.component.scene.entity.tk.BaseCellRelateInfo;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3SceneDtgrid;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3SceneGrid;

/**
 * 
 * 场景管理
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月17日 上午10:09:15
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
public interface ISceneManageMapper {
	/**
	 * 根据父场景编号查询场景基本信息
	 * 
	 * @param parentId
	 * @return
	 */
	public List<SceneBaseInfoPO> selectSceneBaseInfoByParentId(@Param("parentId") String parentId);

	/**
	 * 根据场景级别、根场景id、场景名称查询场景详细信息
	 * 
	 * @param curLevel
	 * @param systemId
	 * @param sceneName
	 * @return
	 */
	public List<SceneDetailedInfoPO> selectSceneDetailedInfoByWhere(@Param("curLevel") Integer curLevel,
			@Param("systemId") Long systemId, @Param("sceneName") String sceneName);

	/**
	 * 查询统计场景栅格信息
	 * 
	 * @param sceneId
	 * @return
	 */
	public List<SceneGridCountInfoPO> countGridInfoBySceneId(@Param("sceneId") Long sceneId);

	/**
	 * 查询底层采集的ini文件信息
	 * 
	 * @return
	 */
	public List<IniFileInfoPO> selectIniFileInfo();

	/**
	 * 查询老版底层采集的ini文件信息
	 */
	List<OldIniFileInfoPO> selectOldIniFileInfo();

	/**
	 * 查询场景 场景树编号、名称集合
	 * 
	 * @param intId
	 * @return
	 */
	public SceneTreeIdsNamesPO selectSceneTreeIdsNames(@Param("parentId") Long parentId);

	/**
	 * 查询缺省场景树信息
	 * 
	 * @param createUserid
	 * @return
	 */
	public List<DefaultSceneTreeInfoPO> selectDefaultSceneTreeInfo(@Param("sceneId") String sceneId,
			@Param("sceneName") String sceneName, @Param("createUserid") String createUserid);

	/**
	 * 根据场景id查询场景详细信息
	 * 
	 * @param intId
	 * @return
	 */
	public SceneDetailedInfoPO selectSceneDetailedInfoById(@Param("intId") Long intId);

	/**
	 * 查询全部缺省场景树信息
	 * 
	 * @return
	 */
	public List<DefaultSceneTreeInfoPO> selectAllDefaultSceneTreeInfo(@Param("sceneId") String sceneId,
			@Param("sceneName") String sceneName);

	/**
	 * 批量插入栅格数据
	 * 
	 * @param tv3GisGrids
	 * @return
	 */
	public Integer insertBatchGridData(List<Tv3SceneGrid> tv3GisGrids);

	/**
	 * 批量插入路段数据
	 * 
	 * @param tv3SceneDtgrids
	 * @return
	 */
	public Integer insertBatchDTGridData(List<Tv3SceneDtgrid> tv3SceneDtgrids);

	/**
	 * 根据制式，场景id查询关联小区详情信息
	 * 
	 * @param sceneId
	 * @param sceneTechnology
	 * @return
	 */
	public List<SceneRelateCellDetaildInfoPO> selectRelateCellDetaildInfoBySceneId(@Param("sceneId") Long sceneId,
			@Param("sceneTechnology") String sceneTechnology);

	/**
	 * 查询在一定经纬度内的某个制式小区信息
	 * 
	 * @param minLongitude
	 * @param maxLongitude
	 * @param minLatitude
	 * @param maxLatitude
	 * @return
	 */
	List<BaseCellRelateInfo> selectCellByLonLat(@Param("technology") String technology,
			@Param("minLongitude") Double minLongitude, @Param("maxLongitude") Double maxLongitude,
			@Param("minLatitude") Double minLatitude, @Param("maxLatitude") Double maxLatitude);

	/**
	 * 根据CGI查询某个制式小区信息
	 * 
	 * @param cgi
	 * @return
	 */
	List<BaseCellRelateInfo> selectCellByCgis(@Param("technology") String technology, @Param("cgis") List<String> cgis);
}
