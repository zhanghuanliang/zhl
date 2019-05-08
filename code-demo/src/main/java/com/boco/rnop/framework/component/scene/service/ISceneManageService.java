package com.boco.rnop.framework.component.scene.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.boco.rnop.framework.common.ResponseMessage2;
import com.boco.rnop.framework.common.excel.ExcelException;
import com.boco.rnop.framework.component.scene.entity.BuildGridDataConfigWebPar;
import com.boco.rnop.framework.component.scene.entity.ExecuteCellRelateDataByGeojsonWebParam;
import com.boco.rnop.framework.component.scene.entity.SceneBuildGridWebInfo;
import com.boco.rnop.framework.component.scene.entity.SceneCellInsertInfo;
import com.boco.rnop.framework.component.scene.entity.SceneGridConfig;
import com.boco.rnop.framework.component.scene.entity.SceneTreeChildrenData;
import com.boco.rnop.framework.component.scene.entity.Tv3SceneExcelWebInfo;
import com.boco.rnop.framework.component.scene.entity.UploadCellWebResult;
import com.boco.rnop.framework.component.scene.entity.po.SceneBaseInfoPO;
import com.boco.rnop.framework.component.scene.entity.po.SceneDetailedInfoPO;
import com.boco.rnop.framework.component.scene.entity.po.SceneGridCountInfoPO;
import com.boco.rnop.framework.component.scene.entity.po.SceneRelateCellDetaildInfoPO;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3GisGeometry;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3SceneCell;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3SceneDtgrid;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3SceneGrid;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3SceneInfo;
import com.github.pagehelper.PageInfo;

/**
 * 
 * 场景管理
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月14日 上午9:43:55
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
public interface ISceneManageService {

	/**
	 * 根据场景id查询关联小区数据
	 * 
	 * @param intId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public ResponseMessage2<PageInfo<SceneRelateCellDetaildInfoPO>> selectCellRelateBySceneId(Long intId,
			Integer pageNum, Integer pageSize);

	/**
	 * 删除小区关联信息
	 * 
	 * @param cellId
	 * @param sceneId
	 * @return
	 */
	public Integer deleteCellRelateByIds(Long cellId, Long sceneId);

	/**
	 * 修改场景数据
	 * 
	 * @param request
	 * 
	 * @param tv3SceneInfo
	 * @return
	 */
	public ResponseMessage2<Integer> alterSceneData(HttpServletRequest request, Tv3SceneInfo tv3SceneInfo);

	/**
	 * 新增场景数据
	 * 
	 * @param request
	 * 
	 * @param tv3SceneInfo
	 * @return
	 */
	public ResponseMessage2<Tv3SceneInfo> insertSceneData(HttpServletRequest request, Tv3SceneInfo tv3SceneInfo);

	/**
	 * 新增关联小区信息
	 * 
	 * @param tv3GisCellrelateGeos
	 * @return
	 */
	public Integer insertCellRelateData(List<Tv3SceneCell> tv3GisCellrelateGeos);

	/**
	 * 查询地理区域场景数据
	 * 
	 * @param subtype
	 * @param regionId
	 * @param name
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PageInfo<Tv3GisGeometry> selectGeometryData(Integer subtype, Long regionId, String name, Integer pageNum,
			Integer pageSize);

	/**
	 * 计算场景关联小区
	 * 
	 * @param sceneId
	 * @return
	 */
	public List<UploadCellWebResult> executeCellRelateData(Long sceneId);

	/**
	 * 生成栅格数据
	 * 
	 * @param sceneId
	 * @param step
	 * @param extendWidth
	 * @param gridLength
	 * @param isDeleteAll
	 * @param gisEngineCentralLongtitude
	 * @return
	 */
	public SceneBuildGridWebInfo buildGridData(Long sceneId, Integer step, Integer extendWidth, Integer gridLength,
			Integer isDeleteAll, Double gisEngineCentralLongtitude);

	/**
	 * 根据场景Geojson计算场景关联小区数据(前端分页)
	 * 
	 * @param webParam
	 * @return
	 */
	public List<UploadCellWebResult> executeCellRelateData(ExecuteCellRelateDataByGeojsonWebParam webParam);

	/**
	 * 上传关联小区数据
	 * 
	 * @param sceneId
	 * @param file
	 * @return
	 */
	public List<UploadCellWebResult> uploadCellRelateDataFile(Long sceneId, MultipartFile file);

	/**
	 * 根据父场景编号查询场景基本信息
	 * 
	 * @param parentId
	 * @return
	 */
	public ResponseMessage2<List<SceneBaseInfoPO>> selectSceneBaseInfoByParentId(String parentId);

	/**
	 * 统计场景名称相同数量
	 * 
	 * @param sceneName
	 * @param parentId
	 * @param technology
	 * @return
	 */
	public Integer countSameSceneName(String sceneName, Long parentId, String technology);

	/**
	 * 删除场景信息
	 * 
	 * @param intId
	 * @return
	 */
	public Integer deleteSceneData(Long intId);

	/**
	 * 删除场景栅格信息
	 * 
	 * @param sceneId
	 * @param step
	 * @param extendWidth
	 * @param gridBorderLength
	 * @return
	 */
	public Integer deleteGridDataBySceneId(Long sceneId, Integer step, Integer extendWidth, Integer gridBorderLength);

	/**
	 * 查询统计场景栅格信息
	 * 
	 * @param sceneId
	 * @return
	 */
	public List<SceneGridCountInfoPO> countGridInfoBySceneId(Long sceneId);

	/**
	 * 上传场景图片
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public String uploadSceneImage(MultipartFile file) throws IOException;

	/**
	 * 上传场景excel文件
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws ExcelException
	 */
	public ResponseMessage2<List<Tv3SceneExcelWebInfo>> uploadSceneInfoExcelFile(MultipartFile file);

	/**
	 * 批量新增场景
	 * 
	 * @param request
	 * 
	 * @param sceneCellInsertInfos
	 * @return
	 */
	public ResponseMessage2<List<SceneCellInsertInfo>> insertSceneDatas(HttpServletRequest request,
			List<SceneCellInsertInfo> sceneCellInsertInfos);

	/**
	 * 批量新增场景小区数据
	 * 
	 * @param request
	 * 
	 * @param tv3SceneExcelWebInfos
	 * @return
	 */
	public ResponseMessage2<List<Tv3SceneExcelWebInfo>> insertSceneAndCellDatas(HttpServletRequest request,
			List<Tv3SceneExcelWebInfo> tv3SceneExcelWebInfos);

	/**
	 * 导出底层采集的ini文件
	 * 
	 * @param response
	 */
	public void exportIniFile(HttpServletResponse response);

	/**
	 * 获取底层采集的ini文件内容
	 * 
	 * @return
	 */
	public String getIniFileContent();

	/**
	 * 查询场景树数据
	 * 
	 * @param sceneName
	 * @param sceneId
	 * 
	 * @param request
	 * @return
	 */
	public ResponseMessage2<List<SceneTreeChildrenData>> getSceneTreeData(Long sceneId, String sceneName,
			HttpServletRequest request);

	/**
	 * 根据场景id查询场景详细信息
	 * 
	 * @param intId
	 * @return
	 */
	public SceneDetailedInfoPO selectSceneDetailedInfoById(Long intId);

	/**
	 * 根据场景id查询关联小区缺省数据
	 * 
	 * @param intId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public ResponseMessage2<PageInfo<Tv3SceneCell>> selectDefaultCellRelateBySceneId(Long intId, Integer pageNum,
			Integer pageSize);

	/**
	 * 获取场景栅格配置信息
	 * 
	 * @return
	 */
	public ResponseMessage2<SceneGridConfig> getSceneGridConfigInfo();

	/**
	 * 生成所有栅格数据
	 * 
	 * @param buildGridDataConfigWebPar
	 * @return
	 */
	public ResponseMessage2<String> buildAllGridData(BuildGridDataConfigWebPar buildGridDataConfigWebPar);

	/**
	 * 生成新栅格数据
	 * 
	 * @param buildGridDataConfigWebPar
	 * @return
	 */
	public ResponseMessage2<String> buildNewGridData(BuildGridDataConfigWebPar buildGridDataConfigWebPar);

	/**
	 * 分批插入栅格数据
	 * 
	 * @param gisGridPOs
	 * @param count
	 * @return
	 */
	public Integer insertBatchGridData(List<Tv3SceneGrid> gisGridPOs, Integer count);

	/**
	 * 分批插入路段数据
	 * 
	 * @param tv3SceneDtgrids
	 * @param count
	 * @return
	 */
	public Integer insertBatchDTGridData(List<Tv3SceneDtgrid> tv3SceneDtgrids, Integer count);

	/**
	 * 导出老版底层采集的ini文件
	 * 
	 * @param response
	 */
	public void exportOldIniFile(HttpServletResponse response);

}
