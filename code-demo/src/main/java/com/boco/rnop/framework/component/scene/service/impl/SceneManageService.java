package com.boco.rnop.framework.component.scene.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gdal.ogr.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.boco.com.framework.gis.engine.EngineTransCoord;
import com.boco.com.framework.gis.engine.geometrys.EnginePoint;
import com.boco.com.framework.gis.engine.geometrys.EnginePolygon;
import com.boco.com.framework.gis.engine.geometrys.MilitaryGrid;
import com.boco.com.framework.gis.engine.repositorys.LineExtendRepertory;
import com.boco.com.framework.gis.engine.repositorys.MilitaryGridRepository;
import com.boco.rnop.framework.common.ResponseMessage2;
import com.boco.rnop.framework.common.excel.ExcelException;
import com.boco.rnop.framework.common.excel.ExcelImporter;
import com.boco.rnop.framework.common.excel.ExcelParams;
import com.boco.rnop.framework.common.ftp.FtpClient;
import com.boco.rnop.framework.common.util.IdUtil;
import com.boco.rnop.framework.common.util.StringUtil;
import com.boco.rnop.framework.component.scene.entity.BuildGridDataConfigWebPar;
import com.boco.rnop.framework.component.scene.entity.CascaderInfo;
import com.boco.rnop.framework.component.scene.entity.ExecuteCellRelateDataByGeojsonWebParam;
import com.boco.rnop.framework.component.scene.entity.SceneBuildGridWebInfo;
import com.boco.rnop.framework.component.scene.entity.SceneCellInsertInfo;
import com.boco.rnop.framework.component.scene.entity.SceneCrawlingDataGeoObject;
import com.boco.rnop.framework.component.scene.entity.SceneGridConfig;
import com.boco.rnop.framework.component.scene.entity.SceneInfoUploadExcel;
import com.boco.rnop.framework.component.scene.entity.SceneTreeChildrenData;
import com.boco.rnop.framework.component.scene.entity.Tv3SceneExcelWebInfo;
import com.boco.rnop.framework.component.scene.entity.UploadCellRelateDataFile;
import com.boco.rnop.framework.component.scene.entity.UploadCellWebResult;
import com.boco.rnop.framework.component.scene.entity.po.DefaultSceneTreeInfoPO;
import com.boco.rnop.framework.component.scene.entity.po.IniFileInfoPO;
import com.boco.rnop.framework.component.scene.entity.po.OldIniFileInfoPO;
import com.boco.rnop.framework.component.scene.entity.po.SceneBaseInfoPO;
import com.boco.rnop.framework.component.scene.entity.po.SceneDetailedInfoPO;
import com.boco.rnop.framework.component.scene.entity.po.SceneGridCountInfoPO;
import com.boco.rnop.framework.component.scene.entity.po.SceneRelateCellDetaildInfoPO;
import com.boco.rnop.framework.component.scene.entity.po.SceneTreeIdsNamesPO;
import com.boco.rnop.framework.component.scene.entity.tk.BaseCellRelateInfo;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3GisGeometry;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3SceneCell;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3SceneDtgrid;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3SceneGrid;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3SceneInfo;
import com.boco.rnop.framework.component.scene.manager.SceneManageAutoConfiguration;
import com.boco.rnop.framework.component.scene.mapper.def.ICommonMapper;
import com.boco.rnop.framework.component.scene.mapper.def.ISceneManageMapper;
import com.boco.rnop.framework.component.scene.mapper.def.ITv3GisGeometryMapper;
import com.boco.rnop.framework.component.scene.mapper.def.ITv3SceneCellMapper;
import com.boco.rnop.framework.component.scene.mapper.def.ITv3SceneDtgridMapper;
import com.boco.rnop.framework.component.scene.mapper.def.ITv3SceneGridMapper;
import com.boco.rnop.framework.component.scene.mapper.def.ITv3SceneInfoMapper;
import com.boco.rnop.framework.component.scene.service.ISceneManageService;
import com.boco.rnop.framework.component.scene.thread.ExecuteGridDataBuildImportThread;
import com.boco.rnop.framework.component.scene.util.GeojsonUtil;
import com.boco.rnop.framework.gis.engine.config.EngineConfiguration;
import com.boco.rnop.framework.parent.microservice.FrameworkMicroserviceApplication;
import com.boco.rnop.framework.parent.microservice.client.SSOIdentity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
@Slf4j
public class SceneManageService implements ISceneManageService {

	@Autowired
	private ITv3SceneInfoMapper tv3SceneInfoMapper;
	@Autowired
	private ITv3SceneCellMapper tv3SceneCellMapper;
	@Autowired
	private ITv3GisGeometryMapper tv3GisGeometryMapper;
	@Autowired
	private ITv3SceneGridMapper tv3SceneGridMapper;
	@Autowired
	private ITv3SceneDtgridMapper tv3SceneDtgridMapper;
	@Autowired
	private ISceneManageMapper senceManageMapper;
	@Autowired
	private ICommonMapper commonMapper;
	@Autowired
	private SceneManageAutoConfiguration sceneImageAutoConfiguration;
	@Autowired
	private TaskExecutor taskExecutor;

	@Override
	public ResponseMessage2<PageInfo<SceneRelateCellDetaildInfoPO>> selectCellRelateBySceneId(Long intId,
			Integer pageNum, Integer pageSize) {
		Tv3SceneInfo tv3SceneInfo = tv3SceneInfoMapper.selectByPrimaryKey(intId);
		if (tv3SceneInfo == null) {
			return ResponseMessage2.Failed("未查询到该场景！");
		}

		PageHelper.startPage(pageNum, pageSize);
		List<SceneRelateCellDetaildInfoPO> sceneRelateCellDetaildInfoPOs = senceManageMapper
				.selectRelateCellDetaildInfoBySceneId(intId, tv3SceneInfo.getTechnology().toUpperCase());

		PageInfo<SceneRelateCellDetaildInfoPO> pageInfo = new PageInfo<>(sceneRelateCellDetaildInfoPOs);

		return ResponseMessage2.Success2(pageInfo);
	}

	@Override
	public Integer deleteCellRelateByIds(Long cellId, Long sceneId) {
		Tv3SceneCell tv3GisCellrelateGeo = new Tv3SceneCell();
		tv3GisCellrelateGeo.setCellId(cellId);
		tv3GisCellrelateGeo.setSceneId(sceneId);
		return tv3SceneCellMapper.delete(tv3GisCellrelateGeo);
	}

	@Override
	public ResponseMessage2<Integer> alterSceneData(HttpServletRequest request, Tv3SceneInfo tv3SceneInfo) {

		SSOIdentity ssoIdentity = FrameworkMicroserviceApplication.getApplication().getSSO().getIdentity(request);
		if (ssoIdentity == null) {
			return ResponseMessage2.Failed("未查询到当前登录用户！");
		}

		Tv3SceneInfo oldTv3SceneInfo = tv3SceneInfoMapper.selectByPrimaryKey(tv3SceneInfo.getIntId());

		// 用户可以编辑自己创建的场景
		if (!"63c26d7f-6e39-40b0-9526-94c58f2d77ca".equals(ssoIdentity.getUserId())
				&& !ssoIdentity.getUserId().equals(oldTv3SceneInfo.getCreateUserid())) {

			if (ssoIdentity.getIsAdministrator()) {
				if (ssoIdentity.getRegionId() != null && ssoIdentity.getRegionId() != -1) {
					// 地市管理员只能管理本地市的场景
					if (Long.valueOf(ssoIdentity.getRegionId()) != oldTv3SceneInfo.getRegionId()) {
						return ResponseMessage2.Failed("地市管理员只能管理本地市的场景！");
					}

				}
				// 管理员可以编辑所有场景
			} else {
				// 普通用户只能编辑自己创建的场景
				if (!ssoIdentity.getUserId().equals(oldTv3SceneInfo.getCreateUserid())) {
					return ResponseMessage2.Failed("普通用户只能编辑自己创建的场景！");
				}
			}
		}

		// 修改场景制式,删除该场景下关联的其他制式的小区数据
		if (tv3SceneInfo.getTechnology() != null) {
			String sceneTechnology = tv3SceneInfo.getTechnology().toUpperCase();
			Example example = new Example(Tv3SceneCell.class);
			Criteria criteria = example.createCriteria();
			criteria.andEqualTo("sceneId", tv3SceneInfo.getIntId());
			criteria.andNotEqualTo("technology", sceneTechnology);
			Integer count = tv3SceneCellMapper.deleteByExample(example);
			log.debug("删除" + count + "条关联小区数据！");
		}

		// 修改场景

		if (tv3SceneInfo.getGeometryId() != null) {
			Tv3GisGeometry tv3GisGeometry = tv3GisGeometryMapper.selectByPrimaryKey(tv3SceneInfo.getGeometryId());
			if (tv3GisGeometry != null) {
				tv3SceneInfo.setSceneLength(tv3GisGeometry.getGeometryLength());
				tv3SceneInfo.setSceneArea(tv3GisGeometry.getGeometryArea());
				tv3SceneInfo.setCentriodLon(tv3GisGeometry.getCentriodLon());
				tv3SceneInfo.setGeojsonType(tv3GisGeometry.getGeojsonType());
				tv3SceneInfo.setGeojson(tv3GisGeometry.getGeometryJson());
				tv3SceneInfo.setCentriodLat(tv3GisGeometry.getCentriodLat());
				tv3SceneInfo.setAcquireWay(tv3GisGeometry.getAcquireWayName());
			}
		} else if (tv3SceneInfo.getGeojson() != null && !"".equals(tv3SceneInfo.getGeojson())) {
			try {
				SceneCrawlingDataGeoObject sceneCrawlingDataGeoObject = GeojsonUtil
						.getSceneCrawlingDataGeoObject(tv3SceneInfo.getGeojson());
				Geometry geometry = Geometry.CreateFromJson(tv3SceneInfo.getGeojson());
				tv3SceneInfo.setSceneLength(geometry.Length());
				tv3SceneInfo.setSceneArea(geometry.Area());
				tv3SceneInfo.setCentriodLon(geometry.Centroid().GetX());
				tv3SceneInfo.setGeojsonType(sceneCrawlingDataGeoObject.getType());
				tv3SceneInfo.setGeojson(tv3SceneInfo.getGeojson());
				tv3SceneInfo.setCentriodLat(geometry.Centroid().GetY());
				tv3SceneInfo.setAcquireWay("绘制");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Integer result = tv3SceneInfoMapper.updateByPrimaryKeySelective(tv3SceneInfo);

		return ResponseMessage2.Success2(result);
	}

	@Override
	public ResponseMessage2<Tv3SceneInfo> insertSceneData(HttpServletRequest request, Tv3SceneInfo tv3SceneInfo) {

		SSOIdentity ssoIdentity = FrameworkMicroserviceApplication.getApplication().getSSO().getIdentity(request);
		if (ssoIdentity == null) {
			return ResponseMessage2.Failed("未查询到当前登录用户！");
		}

		tv3SceneInfo.setCreateTime(new Date());
		tv3SceneInfo.setCreateUserid(ssoIdentity.getUserId());
		tv3SceneInfo.setCreateUsername(ssoIdentity.getUserName());
		if (tv3SceneInfo.getIntId() == null) {
			tv3SceneInfo.setIntId(IdUtil.getLongUUID());
		}

		Integer count = tv3SceneInfoMapper.insertSelective(tv3SceneInfo);
		if (count > 0) {

			return ResponseMessage2.Success2(tv3SceneInfo);
		} else {
			return ResponseMessage2.Failed("数据库插入数据失败！");
		}

	}

	@Override
	public Integer insertCellRelateData(List<Tv3SceneCell> tv3GisCellrelateGeos) {
		Integer count = 0;
		// 小区cgi集合，用于去重
		List<String> cgis = new ArrayList<>();

		for (Tv3SceneCell tv3GisCellrelateGeo : tv3GisCellrelateGeos) {
			if (cgis.contains(tv3GisCellrelateGeo.getCgi())) {
				continue;
			}
			count += tv3SceneCellMapper.insertSelective(tv3GisCellrelateGeo);
		}

		return count;
	}

	@Override
	public PageInfo<Tv3GisGeometry> selectGeometryData(Integer subtype, Long regionId, String name, Integer pageNum,
			Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		Example example = new Example(Tv3GisGeometry.class);
		Criteria criteria = example.createCriteria();

		if (subtype != null) {
			criteria.andEqualTo("sceneTypeId", subtype);
		}
		if (regionId != null) {
			criteria.andEqualTo("regionId", regionId);
		}
		if (name != null) {
			criteria.andLike("name", "%" + name + "%");
		}
		criteria.andIsNotNull("geometryJson");
		List<Tv3GisGeometry> tv3GisGeometries = tv3GisGeometryMapper.selectByExample(example);

		return new PageInfo<>(tv3GisGeometries);
	}

	@Override
	public List<UploadCellWebResult> executeCellRelateData(ExecuteCellRelateDataByGeojsonWebParam webParam) {

		String sceneGeojson = webParam.getSceneGeojson();
		String sceneTechnology = webParam.getSceneTechnology();

		return executeCellRelateData(null, sceneTechnology, sceneGeojson);

	}

	@Override
	public List<UploadCellWebResult> executeCellRelateData(Long sceneId) {
		List<UploadCellWebResult> result = new ArrayList<>();

		Tv3SceneInfo record = new Tv3SceneInfo();
		record.setIntId(sceneId);
		Tv3SceneInfo tv3SceneInfo = tv3SceneInfoMapper.selectOne(record);
		if (tv3SceneInfo == null) {
			return result;
		}

		return executeCellRelateData(sceneId, tv3SceneInfo.getTechnology(), tv3SceneInfo.getGeojson());
	}

	private List<UploadCellWebResult> executeCellRelateData(Long sceneId, String sceneTechnology, String sceneGeojson) {

		List<UploadCellWebResult> result = new ArrayList<>();

		if (sceneGeojson == null || "".equals(sceneGeojson)) {
			log.debug(sceneGeojson + "场景轮廓为空！");
			return result;
		}
		SceneCrawlingDataGeoObject sceneCrawlingDataGeoObject = GeojsonUtil.getSceneCrawlingDataGeoObject(sceneGeojson);
		Integer sceneCellCount = 0;
		if (sceneId != null) {
			// 检查该场景小区数量
			Tv3SceneCell tv3SceneCell = new Tv3SceneCell();
			tv3SceneCell.setSceneId(sceneId);
			sceneCellCount = tv3SceneCellMapper.selectCount(tv3SceneCell);
		}

		List<BaseCellRelateInfo> gsmBaseCellRelateInfos = senceManageMapper.selectCellByLonLat(
				sceneTechnology.toUpperCase(), sceneCrawlingDataGeoObject.getMinLongitude(),
				sceneCrawlingDataGeoObject.getMaxLongitude(), sceneCrawlingDataGeoObject.getMinLatitude(),
				sceneCrawlingDataGeoObject.getMaxLatitude());
		for (BaseCellRelateInfo baseCellRelateInfo : gsmBaseCellRelateInfos) {
			if (getTv3GisCellrelateGeo(sceneGeojson, baseCellRelateInfo.getLongitude(),
					baseCellRelateInfo.getLatitude())) {
				UploadCellWebResult tv3GisCellrelateGeo = new UploadCellWebResult();
				tv3GisCellrelateGeo.setCellId(baseCellRelateInfo.getIntId());
				tv3GisCellrelateGeo.setCellZhName(baseCellRelateInfo.getZhName());
				tv3GisCellrelateGeo.setCgi(baseCellRelateInfo.getCgi());
				tv3GisCellrelateGeo.setCityId(baseCellRelateInfo.getCityId());
				tv3GisCellrelateGeo.setCityName(baseCellRelateInfo.getCityName());
				tv3GisCellrelateGeo.setRegionId(baseCellRelateInfo.getRegionId());
				tv3GisCellrelateGeo.setRegionName(baseCellRelateInfo.getRegionName());
				tv3GisCellrelateGeo.setTechnology("GSM");
				tv3GisCellrelateGeo.setSceneId(sceneId);
				tv3GisCellrelateGeo.setTechnology(sceneTechnology.toUpperCase());

				if (sceneId != null && sceneCellCount != 0) {
					Tv3SceneCell sceneCell = new Tv3SceneCell();
					sceneCell.setSceneId(sceneId);
					sceneCell.setCgi(baseCellRelateInfo.getCgi());
					Integer count = tv3SceneCellMapper.selectCount(sceneCell);
					if (count > 0) {
						tv3GisCellrelateGeo.setIsExisted(true);
					} else {
						tv3GisCellrelateGeo.setIsExisted(false);
					}

				} else {
					tv3GisCellrelateGeo.setIsExisted(false);
				}

				result.add(tv3GisCellrelateGeo);
			}
		}

		return result;

	}

	/**
	 * 计算该经纬度是否在geojson中
	 * 
	 * @param sceneGeojson
	 * @param longitude
	 * @param latitude
	 * @return
	 */
	private Boolean getTv3GisCellrelateGeo(String sceneGeojson, Double longitude, Double latitude) {
		EnginePoint point = new EnginePoint(longitude, latitude);
		if (sceneGeojson == null || "".equals(sceneGeojson)) {
			log.debug("场景轮廓为空！无法关联小区");
			return false;
		}
		SceneCrawlingDataGeoObject sceneCrawlingDataGeoObject = GeojsonUtil.getSceneCrawlingDataGeoObject(sceneGeojson);
		if (sceneCrawlingDataGeoObject == null) {
			log.debug("场景轮廓无法解析：" + sceneGeojson);
			return false;
		}
		String geojsonType = sceneCrawlingDataGeoObject.getType();
		switch (geojsonType) {
		case "LineString":// TODO 线场景

			break;
		case "Polygon":
			List<List<EnginePoint>> lists = sceneCrawlingDataGeoObject.getEnginePoints();
			for (List<EnginePoint> list : lists) {
				if (EngineTransCoord.PolygonIsContainsPoint(point, list.toArray(new EnginePoint[list.size()]))) {
					return true;
				}
			}
			break;
		default:
			break;
		}

		return false;

	}

	@Override
	public SceneBuildGridWebInfo buildGridData(Long sceneId, Integer step, Integer extendWidth, Integer gridLength,
			Integer isDeleteAll, Double gisEngineCentralLongtitude) {

		SceneBuildGridWebInfo result = new SceneBuildGridWebInfo();

		Tv3SceneInfo tv3SceneInfo = tv3SceneInfoMapper.selectByPrimaryKey(sceneId);

		if (isDeleteAll == 1) {
			Tv3SceneGrid record = new Tv3SceneGrid();
			record.setSceneId(sceneId);
			result.setDelectGridCount(tv3SceneGridMapper.delete(record));
		} else {
			Tv3SceneGrid record = new Tv3SceneGrid();
			record.setSceneId(sceneId);
			record.setGridBorderLength(gridLength);
			result.setDelectGridCount(tv3SceneGridMapper.delete(record));
		}

		String sceneGeojson = tv3SceneInfo.getGeojson();

		// TODO geojson专业解析工具
		SceneCrawlingDataGeoObject sceneCrawlingDataGeoObject = GeojsonUtil.getSceneCrawlingDataGeoObject(sceneGeojson);

		if (sceneCrawlingDataGeoObject == null) {
			log.debug(sceneGeojson + "无法解析！");
			result.setBuildGridCount(0);
			return result;
		}
		// 解析面点
		List<List<List<Double[]>>> lists = sceneCrawlingDataGeoObject.getPolygons();
		List<List<EnginePoint>> polygons = new ArrayList<>();
		for (List<List<Double[]>> list : lists) {
			List<EnginePoint> points = new ArrayList<>();
			if (list.size() <= 0) {
				continue;
			}
			// TODO 带孔洞的面处理
			for (Double[] ds : list.get(0)) {
				if (ds.length > 0) {
					EnginePoint enginePoint = new EnginePoint(ds[0], ds[1]);
					points.add(enginePoint);
				}
			}
			polygons.add(points);
		}
		// 栅格数据集合
		List<Tv3SceneGrid> tv3GisGrids = new ArrayList<>();
		// 路段数据集合
		List<Tv3SceneDtgrid> tv3SceneDtgrids = new ArrayList<>();

		// 设置
		if (gisEngineCentralLongtitude != null) {
			EngineConfiguration.setCentralLongtitude(gisEngineCentralLongtitude);
		}

		// 面场景
		if ("Polygon".equals(sceneCrawlingDataGeoObject.getType())) {
			// 军事栅格数据集合
			List<MilitaryGrid> grids = new ArrayList<>();
			for (List<EnginePoint> enginePoints : polygons) {
				grids.addAll(MilitaryGridRepository.GetPolygonGrid(enginePoints, gridLength));
			}
			for (MilitaryGrid militaryGrid : grids) {
				Tv3SceneGrid tv3GisGrid = new Tv3SceneGrid();
				tv3GisGrid.setIntId(IdUtil.getLongUUID());
				tv3GisGrid.setCityId(tv3SceneInfo.getCityId());
				tv3GisGrid.setCityName(tv3SceneInfo.getCityName());
				tv3GisGrid.setGridId(militaryGrid.getId());
				tv3GisGrid.setGridLeftbottomLongitude(militaryGrid.getPoint_lb().getX());
				tv3GisGrid.setGridLeftbottomLatitude(militaryGrid.getPoint_lb().getY());
				tv3GisGrid.setGridLefttopLongitude(militaryGrid.getPoint_lu().getX());
				tv3GisGrid.setGridLefttopLatitude(militaryGrid.getPoint_lu().getY());
				tv3GisGrid.setGridRightbottomLongitude(militaryGrid.getPoint_rb().getX());
				tv3GisGrid.setGridRightbottomLatitude(militaryGrid.getPoint_rb().getY());
				tv3GisGrid.setGridRighttopLongitude(militaryGrid.getPoint_ru().getX());
				tv3GisGrid.setGridRighttopLatitude(militaryGrid.getPoint_ru().getY());
				tv3GisGrid.setInsertTime(new Date());
				tv3GisGrid.setRegionId(tv3SceneInfo.getRegionId());
				tv3GisGrid.setRegionName(tv3SceneInfo.getRegionName());
				tv3GisGrid.setSceneId(tv3SceneInfo.getIntId());
				tv3GisGrid.setSceneName(tv3SceneInfo.getName());
				tv3GisGrid.setGridBorderLength(gridLength);
				tv3GisGrids.add(tv3GisGrid);
			}
			// 线场景
		} else if ("LineString".equals(sceneCrawlingDataGeoObject.getType())) {

			for (List<EnginePoint> enginePoints : polygons) {
				LineExtendRepertory lineExtendRepertory = new LineExtendRepertory(enginePoints, step, extendWidth,
						true);
				List<EnginePolygon> enginePolygons = lineExtendRepertory.GenerateLineRects();
				for (int i = 0; i < enginePolygons.size(); i++) {
					EnginePolygon enginePolygon = enginePolygons.get(i);
					/** 路段名称(如合芜高速_1) */
					String dtgridName = tv3SceneInfo.getName() + "_" + (i + 1);
					/** 路段唯一标识ID */
					Long dtgridId = IdUtil.getLongUUID();
					EnginePoint[] points = enginePolygon.getPoints();
					if (points.length >= 4) {

						/** 路段左上经度 */
						Double dtgridLefttopLongitude = points[0].getX();
						/** 路段左上纬度 */
						Double dtgridLefttopLatitude = points[0].getY();
						/** 路段右上经度 */
						Double dtgridRighttopLongitude = points[1].getX();
						/** 路段右上纬度 */
						Double dtgridRighttopLatitude = points[1].getY();
						/** 路段右下经度 */
						Double dtgridRightbottomLongitude = points[2].getX();
						/** 路段右下纬度 */
						Double dtgridRightbottomLatitude = points[2].getY();
						/** 路段左下经度 */
						Double dtgridLeftbottomLongitude = points[3].getX();
						/** 路段左下纬度 */
						Double dtgridLeftbottomLatitude = points[3].getY();

						// 路段信息
						Tv3SceneDtgrid tv3SceneDtgrid = new Tv3SceneDtgrid();
						/** 路段左下纬度 */
						tv3SceneDtgrid.setDtgridLeftbottomLatitude(dtgridLeftbottomLatitude);
						/** 路段左上经度 */
						tv3SceneDtgrid.setDtgridLefttopLongitude(dtgridLefttopLongitude);
						/** 路段左上纬度 */
						tv3SceneDtgrid.setDtgridLefttopLatitude(dtgridLefttopLatitude);
						/** 路段右上经度 */
						tv3SceneDtgrid.setDtgridRighttopLongitude(dtgridRighttopLongitude);
						/** 路段左下经度 */
						tv3SceneDtgrid.setDtgridLeftbottomLongitude(dtgridLeftbottomLongitude);
						/** 路段唯一标识ID */
						tv3SceneDtgrid.setIntId(dtgridId);
						/** 路段右上纬度 */
						tv3SceneDtgrid.setDtgridRighttopLatitude(dtgridRighttopLatitude);
						/** 路段右下经度 */
						tv3SceneDtgrid.setDtgridRightbottomLongitude(dtgridRightbottomLongitude);
						/** 路段右下纬度 */
						tv3SceneDtgrid.setDtgridRightbottomLatitude(dtgridRightbottomLatitude);
						/** 路段名称(如合芜高速_1) */
						tv3SceneDtgrid.setDtgridName(dtgridName);
						// 路段步长
						tv3SceneDtgrid.setStep(step);
						// 路段扩充宽度
						tv3SceneDtgrid.setExtendWidth(extendWidth);
						// 路段序号
						tv3SceneDtgrid.setOrderNum(i + 1);
						tv3SceneDtgrids.add(tv3SceneDtgrid);

						List<MilitaryGrid> grids = MilitaryGridRepository.GetPolygonGrid(Arrays.asList(points),
								gridLength);
						for (MilitaryGrid militaryGrid : grids) {
							Tv3SceneGrid tv3GisGrid = new Tv3SceneGrid();
							tv3GisGrid.setIntId(IdUtil.getLongUUID());
							tv3GisGrid.setGridId(militaryGrid.getId());
							tv3GisGrid.setInsertTime(new Date());
							tv3GisGrid.setGridLeftbottomLongitude(militaryGrid.getPoint_lb().getX());
							tv3GisGrid.setGridLeftbottomLatitude(militaryGrid.getPoint_lb().getY());
							tv3GisGrid.setGridLefttopLongitude(militaryGrid.getPoint_lu().getX());
							tv3GisGrid.setGridLefttopLatitude(militaryGrid.getPoint_lu().getY());
							tv3GisGrid.setGridRightbottomLongitude(militaryGrid.getPoint_rb().getX());
							tv3GisGrid.setGridRightbottomLatitude(militaryGrid.getPoint_rb().getY());
							tv3GisGrid.setGridRighttopLongitude(militaryGrid.getPoint_ru().getX());
							tv3GisGrid.setGridRighttopLatitude(militaryGrid.getPoint_ru().getY());
							tv3GisGrid.setSceneId(sceneId);
							tv3GisGrid.setSceneName(tv3SceneInfo.getName());
							tv3GisGrid.setGridBorderLength(gridLength);
							tv3GisGrid.setRegionId(tv3SceneInfo.getRegionId());
							tv3GisGrid.setRegionName(tv3SceneInfo.getRegionName());
							tv3GisGrid.setDtgridId(dtgridId);
							tv3GisGrids.add(tv3GisGrid);

						}

					}

				}
			}

		}

		Integer gridCount = insertBatchGridData(tv3GisGrids, 1000);
		log.debug(" 入库栅格数据表:" + gridCount + "条栅格数据！");
		Integer dtgridCount = insertBatchDTGridData(tv3SceneDtgrids, 1000);
		log.debug(" 入库路段数据表:" + dtgridCount + "条路段数据！");
		result.setBuildGridCount(gridCount + dtgridCount);
		return result;
	}

	/**
	 * 分批插入栅格数据
	 * 
	 * @param gisGridPOs
	 * @param count
	 * @return
	 */
	@Override
	public Integer insertBatchGridData(List<Tv3SceneGrid> gisGridPOs, Integer count) {
		int result = 0;
		if (gisGridPOs == null || gisGridPOs.isEmpty()) {
			return result;
		}
		if (gisGridPOs.size() > count) {
			int size = gisGridPOs.size() / count + 1;
			for (int i = 0; i < size; i++) {
				result += senceManageMapper.insertBatchGridData(gisGridPOs.subList(i * count,
						(i + 1) * count > gisGridPOs.size() ? gisGridPOs.size() : (i + 1) * count));
			}
		} else {
			result += senceManageMapper.insertBatchGridData(gisGridPOs);
		}
		return result;
	}

	/**
	 * 分批插入路段数据
	 * 
	 * @param tv3SceneDtgrids
	 * @param count
	 * @return
	 */
	@Override
	public Integer insertBatchDTGridData(List<Tv3SceneDtgrid> tv3SceneDtgrids, Integer count) {
		int result = 0;
		if (tv3SceneDtgrids == null || tv3SceneDtgrids.isEmpty()) {
			return result;
		}
		if (tv3SceneDtgrids.size() > count) {
			int size = tv3SceneDtgrids.size() / count + 1;
			for (int i = 0; i < size; i++) {
				result += senceManageMapper.insertBatchDTGridData(tv3SceneDtgrids.subList(i * count,
						(i + 1) * count > tv3SceneDtgrids.size() ? tv3SceneDtgrids.size() : (i + 1) * count));
			}
		} else {
			result += senceManageMapper.insertBatchDTGridData(tv3SceneDtgrids);
		}
		return result;
	}

	@Override
	public List<UploadCellWebResult> uploadCellRelateDataFile(Long sceneId, MultipartFile file) {
		List<UploadCellWebResult> tv3GisCellrelateGeos = new ArrayList<>();
		try {
			ExcelParams<UploadCellRelateDataFile> excelParams = new ExcelParams<>(file.getOriginalFilename(),
					file.getInputStream(), getCellRelateDataFileFieldMap());
			excelParams.setEntityClass(UploadCellRelateDataFile.class);
			List<UploadCellRelateDataFile> list = ExcelImporter.excelToList(excelParams);

			for (UploadCellRelateDataFile uploadCellRelateDataFile : list) {
				String technology = uploadCellRelateDataFile.getTechnology();
				String cgi = uploadCellRelateDataFile.getCgi();

				UploadCellWebResult tv3GisCellrelateGeo = new UploadCellWebResult();
				List<String> cgis = new ArrayList<>();
				cgis.add(cgi);
				List<BaseCellRelateInfo> baseCellRelateInfos = senceManageMapper
						.selectCellByCgis(technology.toUpperCase(), cgis);
				if (baseCellRelateInfos != null && !baseCellRelateInfos.isEmpty()) {
					BaseCellRelateInfo baseCellRelateInfo = baseCellRelateInfos.get(0);
					tv3GisCellrelateGeo.setCellId(baseCellRelateInfo.getIntId());
					tv3GisCellrelateGeo.setCellZhName(baseCellRelateInfo.getZhName());
					tv3GisCellrelateGeo.setCgi(baseCellRelateInfo.getCgi());
					tv3GisCellrelateGeo.setCityId(baseCellRelateInfo.getCityId());
					tv3GisCellrelateGeo.setCityName(baseCellRelateInfo.getCityName());
					tv3GisCellrelateGeo.setRegionId(baseCellRelateInfo.getRegionId());
					tv3GisCellrelateGeo.setRegionName(baseCellRelateInfo.getRegionName());
					// tv3GisCellrelateGeo.setSceneId(sceneId);
					tv3GisCellrelateGeo.setTechnology(technology);
				}

				if (sceneId != null) {
					// 检查小区信息是否重复
					Tv3SceneCell tv3SceneCell = new Tv3SceneCell();
					tv3SceneCell.setSceneId(sceneId);
					tv3SceneCell.setCgi(cgi);
					Integer count = tv3SceneCellMapper.selectCount(tv3SceneCell);
					if (count > 0) {
						tv3GisCellrelateGeo.setIsExisted(true);
					} else {
						tv3GisCellrelateGeo.setIsExisted(false);
					}
				} else {
					// 未传场景ID，默认false
					tv3GisCellrelateGeo.setIsExisted(false);
				}

				tv3GisCellrelateGeos.add(tv3GisCellrelateGeo);

			}

		} catch (IOException | ExcelException e) {
			e.printStackTrace();
		}

		return tv3GisCellrelateGeos;
	}

	/**
	 * 获取上传关联小区文件映射关系
	 * 
	 * @return
	 */
	private LinkedHashMap<String, String> getCellRelateDataFileFieldMap() {

		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		map.put("制式", "technology");
		map.put("cgi", "cgi");
		return map;
	}

	@Override
	public ResponseMessage2<List<SceneBaseInfoPO>> selectSceneBaseInfoByParentId(String parentId) {
		List<CascaderInfo> result = new ArrayList<>();

		List<SceneBaseInfoPO> baseInfoPOs = senceManageMapper.selectSceneBaseInfoByParentId(parentId);

		return ResponseMessage2.Success2(baseInfoPOs);
	}

	@Override
	public Integer countSameSceneName(String sceneName, Long parentId, String technology) {
		// 同一父场景下，相同制式的场景名称不能相同
		Tv3SceneInfo record = new Tv3SceneInfo();
		record.setName(sceneName);
		record.setParentId(parentId);
		record.setTechnology(technology != null ? technology.toUpperCase() : null);
		return tv3SceneInfoMapper.selectCount(record);
	}

	@Override
	public Integer deleteSceneData(Long intId) {
		Integer result = 0;
		Set<Long> longs = getChildSceneIds(intId);
		// 批量删除场景信息
		Example example = new Example(Tv3SceneInfo.class);
		Criteria criteria = example.createCriteria();
		criteria.andIn("intId", longs);
		result += tv3SceneInfoMapper.deleteByExample(example);
		// 批量删除场景关联小区
		Example exampleTv3GisCellrelateGeo = new Example(Tv3SceneCell.class);
		Criteria criteriaTv3GisCellrelateGeo = exampleTv3GisCellrelateGeo.createCriteria();
		criteriaTv3GisCellrelateGeo.andIn("sceneId", longs);
		result += tv3SceneCellMapper.deleteByExample(exampleTv3GisCellrelateGeo);
		// 批量删除场景栅格数据
		Example exampleTv3GisGrid = new Example(Tv3SceneGrid.class);
		Criteria criteriaTv3GisGrid = exampleTv3GisGrid.createCriteria();
		criteriaTv3GisGrid.andIn("sceneId", longs);
		result += tv3SceneGridMapper.deleteByExample(exampleTv3GisCellrelateGeo);
		// 批量删除场景路段数据
		Example tv3SceneDtgridExample = new Example(Tv3SceneDtgrid.class);
		Criteria tv3SceneDtgridCriteria = tv3SceneDtgridExample.createCriteria();
		tv3SceneDtgridCriteria.andIn("sceneId", longs);
		result += tv3SceneDtgridMapper.deleteByExample(tv3SceneDtgridExample);

		return result;
	}

	/**
	 * 递归查询该场景id下所有的子场景id值
	 * 
	 * @param intId
	 * @return
	 */
	private Set<Long> getChildSceneIds(Long intId) {
		Set<Long> longs = new HashSet<>();
		longs.add(intId);
		Tv3SceneInfo record = new Tv3SceneInfo();
		record.setParentId(intId);
		List<Tv3SceneInfo> sceneInfos = tv3SceneInfoMapper.select(record);
		if (sceneInfos != null) {
			for (Tv3SceneInfo tv3SceneInfo : sceneInfos) {
				longs.addAll(getChildSceneIds(tv3SceneInfo.getIntId()));
			}
		}
		return longs;
	}

	@Override
	public List<SceneGridCountInfoPO> countGridInfoBySceneId(Long sceneId) {

		return senceManageMapper.countGridInfoBySceneId(sceneId);
	}

	@Override
	public Integer deleteGridDataBySceneId(Long sceneId, Integer step, Integer extendWidth, Integer gridBorderLength) {
		Tv3SceneGrid tv3SceneGrid = new Tv3SceneGrid();
		tv3SceneGrid.setSceneId(sceneId);
		tv3SceneGrid.setGridBorderLength(gridBorderLength);
		Integer gridCount = tv3SceneGridMapper.delete(tv3SceneGrid);

		Tv3SceneDtgrid tv3SceneDtgrid = new Tv3SceneDtgrid();
		tv3SceneDtgrid.setSceneId(sceneId);
		tv3SceneDtgrid.setStep(step);
		tv3SceneDtgrid.setExtendWidth(extendWidth);
		Integer dtgridCount = tv3SceneDtgridMapper.delete(tv3SceneDtgrid);

		return gridCount + dtgridCount;
	}

	@Override
	public String uploadSceneImage(MultipartFile file) throws IOException {

		String newFileName = IdUtil.getUUID() + "_" + new Date().getTime();

		FtpClient client = new FtpClient(sceneImageAutoConfiguration.getFtpIp(),
				sceneImageAutoConfiguration.getFtpPort(), sceneImageAutoConfiguration.getFtpUserName(),
				sceneImageAutoConfiguration.getFtpPwd());

		client.open();
		if (client.upload(file.getInputStream(), newFileName, sceneImageAutoConfiguration.getFtpImagePath())) {
			return newFileName;
		} else {
			log.warn("场景图片上传失败！");
		}
		client.close();

		return null;
	}

	@Override
	public ResponseMessage2<List<Tv3SceneExcelWebInfo>> uploadSceneInfoExcelFile(MultipartFile file) {
		List<Tv3SceneExcelWebInfo> result = new ArrayList<>();

		LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
		fieldMap.put("网络制式(*)", "sceneTechnology");
		fieldMap.put("地市(*)", "regionName");
		fieldMap.put("区县(*)", "cityName");
		fieldMap.put("根级场景(*)", "rootSceneName");
		fieldMap.put("一级场景", "oneSceneName");
		fieldMap.put("二级场景", "twoSceneName");
		fieldMap.put("三级场景", "threeSceneName");
		fieldMap.put("四级场景", "fourSceneName");
		fieldMap.put("小区中文名(*)", "cellName");
		fieldMap.put("CGI(*)", "cgi");
		fieldMap.put("标签", "tag");
		fieldMap.put("是否共享场景", "isSharing");
		fieldMap.put("是否需要汇总场景粒度", "isNeedSceneSize");
		fieldMap.put("是否需要大数据汇总", "isNeedBigdataSum");
		fieldMap.put("是否需要统计性能", "isNeedPerformanceSum");
		fieldMap.put("是否需要生成场景栅格", "isNeedGirdSize");
		fieldMap.put("是否需要汇总小区粒度", "isNeedCellSize");
		List<SceneInfoUploadExcel> tv3SceneInfos = new ArrayList<>();
		try {
			ExcelParams<SceneInfoUploadExcel> excelParams = new ExcelParams<SceneInfoUploadExcel>(
					file.getOriginalFilename(), file.getInputStream(), fieldMap);
			excelParams.setEntityClass(SceneInfoUploadExcel.class);
			tv3SceneInfos = ExcelImporter.excelToList(excelParams);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseMessage2.Failed("Excel文件解析出错，请检查文件表头格式！");
		}

		Map<String, List<SceneInfoUploadExcel>> map = new LinkedHashMap<>();
		// 场景小区信息分组
		for (SceneInfoUploadExcel sceneInfoUploadExcel : tv3SceneInfos) {

			String key = sceneInfoUploadExcel.getRootSceneName() + sceneInfoUploadExcel.getOneSceneName()
					+ sceneInfoUploadExcel.getTwoSceneName() + sceneInfoUploadExcel.getThreeSceneName()
					+ sceneInfoUploadExcel.getThreeSceneName() + sceneInfoUploadExcel.getSceneTechnology();

			if (map.containsKey(key)) {
				map.get(key).add(sceneInfoUploadExcel);
			} else {
				List<SceneInfoUploadExcel> sceneInfoUploadExcels = new ArrayList<>();
				sceneInfoUploadExcels.add(sceneInfoUploadExcel);
				map.put(key, sceneInfoUploadExcels);
			}
		}

		// 处理分组后的数据
		for (Entry<String, List<SceneInfoUploadExcel>> entry : map.entrySet()) {
			Tv3SceneExcelWebInfo tv3SceneExcelWebInfo = new Tv3SceneExcelWebInfo();
			tv3SceneExcelWebInfo.setIsFull(true);
			tv3SceneExcelWebInfo.setIsExist(false);

			SceneInfoUploadExcel excel = new SceneInfoUploadExcel();
			if (entry.getValue() != null && !entry.getValue().isEmpty()) {
				excel = entry.getValue().get(0);
			} else {
				continue;
			}

			// 关联小区信息
			List<String> cgis = new ArrayList<>();
			for (SceneInfoUploadExcel sceneInfoUploadExcel : entry.getValue()) {
				cgis.add(sceneInfoUploadExcel.getCgi());
			}

			List<Tv3SceneCell> tv3GisCellrelateGeos = new ArrayList<>();
			if (excel.getSceneTechnology() == null || "".equals(excel.getSceneTechnology())) {
				continue;
			}

			List<BaseCellRelateInfo> baseCellRelateInfos = senceManageMapper
					.selectCellByCgis(excel.getSceneTechnology().toUpperCase(), cgis);
			for (BaseCellRelateInfo baseCellRelateInfo : baseCellRelateInfos) {
				Tv3SceneCell tv3GisCellrelateGeo = new Tv3SceneCell();
				tv3GisCellrelateGeo.setCellId(baseCellRelateInfo.getIntId());
				tv3GisCellrelateGeo.setCellZhName(baseCellRelateInfo.getZhName());
				tv3GisCellrelateGeo.setCgi(baseCellRelateInfo.getCgi());
				tv3GisCellrelateGeo.setCityId(baseCellRelateInfo.getCityId());
				tv3GisCellrelateGeo.setCityName(baseCellRelateInfo.getCityName());
				tv3GisCellrelateGeo.setRegionId(baseCellRelateInfo.getRegionId());
				tv3GisCellrelateGeo.setRegionName(baseCellRelateInfo.getRegionName());
				tv3GisCellrelateGeo.setTechnology(excel.getSceneTechnology().toUpperCase());
				// tv3GisCellrelateGeo.setSceneId(sceneId);
				tv3GisCellrelateGeos.add(tv3GisCellrelateGeo);
			}
			tv3SceneExcelWebInfo.setTv3GisCellrelateGeos(tv3GisCellrelateGeos);

			// 场景级别
			// 获取场景名称、根场景名称、父场景名称
			String sceneName = "";
			String rootSceneName = "";
			String parentSceneName = "";
			Integer curLevel = 0;
			if (excel.getRootSceneName() != null) {
				rootSceneName = excel.getRootSceneName();
				if (excel.getOneSceneName() != null && !excel.getOneSceneName().isEmpty()) {
					if (excel.getTwoSceneName() != null && !excel.getTwoSceneName().isEmpty()) {
						if (excel.getThreeSceneName() != null && !excel.getThreeSceneName().isEmpty()) {
							if (excel.getFourSceneName() != null && !excel.getFourSceneName().isEmpty()) {
								sceneName = excel.getFourSceneName();
								parentSceneName = excel.getThreeSceneName();
								curLevel = 4;
							} else {
								sceneName = excel.getThreeSceneName();
								parentSceneName = excel.getTwoSceneName();
								curLevel = 3;
							}
						} else {
							sceneName = excel.getTwoSceneName();
							parentSceneName = excel.getOneSceneName();
							curLevel = 2;
						}
					} else {
						sceneName = excel.getOneSceneName();
						parentSceneName = excel.getRootSceneName();
						curLevel = 1;
					}
				} else {
					sceneName = excel.getRootSceneName();
					parentSceneName = excel.getRootSceneName();
					curLevel = 0;
				}
			}

			if (curLevel == null || curLevel == 0) {
				// 新增场景为根场景
				tv3SceneExcelWebInfo.setRootId(-1L);
				tv3SceneExcelWebInfo.setParentId(-1L);
			} else {

				// 查询根场景信息
				PageHelper.startPage(1, 1);
				Tv3SceneInfo systemTv3SceneInfo = new Tv3SceneInfo();
				systemTv3SceneInfo.setName(rootSceneName);
				systemTv3SceneInfo.setCurLevel(0);
				List<Tv3SceneInfo> systemTv3SceneInfos = tv3SceneInfoMapper.select(systemTv3SceneInfo);
				if (systemTv3SceneInfos != null && !systemTv3SceneInfos.isEmpty()) {
					tv3SceneExcelWebInfo.setRootId(systemTv3SceneInfos.get(0).getIntId());
					tv3SceneExcelWebInfo.setSystemSceneName(systemTv3SceneInfos.get(0).getName());

				} else {
					tv3SceneExcelWebInfo.setIsFull(false);
				}

				// 查询父场景信息
				if (tv3SceneExcelWebInfo.getRootId() != null) {
					PageHelper.startPage(1, 1);
					Tv3SceneInfo parentTv3SceneInfo = new Tv3SceneInfo();
					parentTv3SceneInfo.setRootId(curLevel - 1 == 0 ? -1 : tv3SceneExcelWebInfo.getRootId());
					parentTv3SceneInfo.setCurLevel(curLevel - 1);
					parentTv3SceneInfo.setName(parentSceneName);
					List<Tv3SceneInfo> parentTv3SceneInfos = tv3SceneInfoMapper.select(parentTv3SceneInfo);
					if (parentTv3SceneInfos != null && !parentTv3SceneInfos.isEmpty()) {
						tv3SceneExcelWebInfo.setParentId(parentTv3SceneInfos.get(0).getIntId());
						tv3SceneExcelWebInfo.setParentSceneName(parentTv3SceneInfos.get(0).getName());
					} else {
						tv3SceneExcelWebInfo.setIsFull(false);
					}

				} else {

					tv3SceneExcelWebInfo.setIsFull(false);
				}

			}

			// 组装基本数据

			Long regionId = commonMapper.selectRegionIdByName("%" + excel.getRegionName() + "%");

			Long cityId = commonMapper.selectCityIdByName("%" + excel.getCityName() + "%");

			tv3SceneExcelWebInfo.setCityId(cityId);
			tv3SceneExcelWebInfo.setCityName(excel.getCityName());
			tv3SceneExcelWebInfo.setCreateTime(new Date());
			tv3SceneExcelWebInfo.setCurLevel(curLevel);
			tv3SceneExcelWebInfo.setIntId(IdUtil.getLongUUID());
			if (tv3SceneExcelWebInfo.getParentId() != null) {
				SceneTreeIdsNamesPO sceneTreeIdsNamesPO = senceManageMapper
						.selectSceneTreeIdsNames(tv3SceneExcelWebInfo.getParentId());
				if (sceneTreeIdsNamesPO != null) {
					tv3SceneExcelWebInfo.setTreeIds(sceneTreeIdsNamesPO.getTreeIds());
					tv3SceneExcelWebInfo.setTreeNames(sceneTreeIdsNamesPO.getTreeNames());
				}
			}

			tv3SceneExcelWebInfo.setIsNeedGirdSize("是".equals(excel.getIsNeedGirdSize()) ? 1 : 0);
			tv3SceneExcelWebInfo.setIsNeedBigdataSum("是".equals(excel.getIsNeedBigdataSum()) ? 1 : 0);
			tv3SceneExcelWebInfo.setIsNeedPerformanceSum("是".equals(excel.getIsNeedPerformanceSum()) ? 1 : 0);
			tv3SceneExcelWebInfo.setIsNeedCellSize("是".equals(excel.getIsNeedCellSize()) ? 1 : 0);
			tv3SceneExcelWebInfo.setIsNeedSceneSize("是".equals(excel.getIsNeedSceneSize()) ? 1 : 0);
			tv3SceneExcelWebInfo.setTag(excel.getTag());
			// 导入场景默认，不锁定
			tv3SceneExcelWebInfo.setIsLocking(0);
			tv3SceneExcelWebInfo.setIsSharing("是".equals(excel.getIsSharing()) ? 1 : 0);
			tv3SceneExcelWebInfo.setRegionId(regionId);
			tv3SceneExcelWebInfo.setRegionName(excel.getRegionName());
			tv3SceneExcelWebInfo.setName(sceneName);
			tv3SceneExcelWebInfo.setTechnology(excel.getSceneTechnology());

			// 检查场景是否已存在
			Tv3SceneInfo tv3SceneInfo = new Tv3SceneInfo();
			tv3SceneInfo.setName(sceneName);
			tv3SceneInfo.setParentId(tv3SceneExcelWebInfo.getParentId());
			tv3SceneInfo.setRootId(tv3SceneExcelWebInfo.getRootId());
			tv3SceneInfo.setCurLevel(curLevel);
			Integer count = tv3SceneInfoMapper.selectCount(tv3SceneInfo);

			if (count > 0) {
				tv3SceneExcelWebInfo.setIsExist(true);
			}
			result.add(tv3SceneExcelWebInfo);
		}

		return ResponseMessage2.Success2(result);
	}

	@Override
	public ResponseMessage2<List<SceneCellInsertInfo>> insertSceneDatas(HttpServletRequest request,
			List<SceneCellInsertInfo> sceneCellInsertInfos) {
		SSOIdentity ssoIdentity = FrameworkMicroserviceApplication.getApplication().getSSO().getIdentity(request);
		if (ssoIdentity == null) {
			return ResponseMessage2.Failed("未查询到当前登录用户！");
		}
		for (SceneCellInsertInfo sceneCellInsertInfo : sceneCellInsertInfos) {
			Long intId = IdUtil.getLongUUID();
			Tv3SceneInfo tv3SceneInfo = sceneCellInsertInfo.getTv3SceneInfo();
			if (tv3SceneInfo.getGeometryId() != null) {
				Tv3GisGeometry tv3GisGeometry = tv3GisGeometryMapper.selectByPrimaryKey(tv3SceneInfo.getGeometryId());
				if (tv3GisGeometry != null) {
					tv3SceneInfo.setSceneLength(tv3GisGeometry.getGeometryLength());
					tv3SceneInfo.setSceneArea(tv3GisGeometry.getGeometryArea());
					tv3SceneInfo.setCentriodLon(tv3GisGeometry.getCentriodLon());
					tv3SceneInfo.setGeojsonType(tv3GisGeometry.getGeojsonType());
					tv3SceneInfo.setGeojson(tv3GisGeometry.getGeometryJson());
					tv3SceneInfo.setCentriodLat(tv3GisGeometry.getCentriodLat());
					tv3SceneInfo.setAcquireWay(tv3GisGeometry.getAcquireWayName());
				}
			} else if (tv3SceneInfo.getGeojson() != null && !"".equals(tv3SceneInfo.getGeojson())) {
				try {
					SceneCrawlingDataGeoObject sceneCrawlingDataGeoObject = GeojsonUtil
							.getSceneCrawlingDataGeoObject(tv3SceneInfo.getGeojson());

					Geometry geometry = Geometry.CreateFromJson(tv3SceneInfo.getGeojson());
					tv3SceneInfo.setSceneLength(geometry.Length());
					tv3SceneInfo.setSceneArea(geometry.Area());
					tv3SceneInfo.setCentriodLon(geometry.Centroid().GetX());
					tv3SceneInfo.setGeojsonType(sceneCrawlingDataGeoObject.getType());
					tv3SceneInfo.setGeojson(tv3SceneInfo.getGeojson());
					tv3SceneInfo.setCentriodLat(geometry.Centroid().GetY());
					tv3SceneInfo.setAcquireWay("绘制");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			tv3SceneInfo.setCreateTime(new Date());
			tv3SceneInfo.setIntId(intId);
			SceneTreeIdsNamesPO sceneTreeIdsNamesPO = senceManageMapper
					.selectSceneTreeIdsNames(tv3SceneInfo.getParentId());
			tv3SceneInfo.setCreateUserid(ssoIdentity.getUserId());
			tv3SceneInfo.setCreateUsername(ssoIdentity.getUserName());
			List<Tv3SceneCell> tv3GisCellrelateGeos = sceneCellInsertInfo.getTv3GisCellrelateGeos();
			if (tv3GisCellrelateGeos != null) {
				for (Tv3SceneCell tv3GisCellrelateGeo : tv3GisCellrelateGeos) {
					tv3GisCellrelateGeo.setSceneId(intId);
					tv3SceneCellMapper.insertSelective(tv3GisCellrelateGeo);
				}

			}
			tv3SceneInfoMapper.insertSelective(tv3SceneInfo);

		}

		return ResponseMessage2.Success2(sceneCellInsertInfos);
	}

	@Transactional
	@Override
	public ResponseMessage2<List<Tv3SceneExcelWebInfo>> insertSceneAndCellDatas(HttpServletRequest request,
			List<Tv3SceneExcelWebInfo> tv3SceneExcelWebInfos) {
		SSOIdentity ssoIdentity = FrameworkMicroserviceApplication.getApplication().getSSO().getIdentity(request);
		if (ssoIdentity == null) {
			return ResponseMessage2.Failed("未查询到当前登录用户！");
		}
		for (Tv3SceneExcelWebInfo tv3SceneExcelWebInfo : tv3SceneExcelWebInfos) {
			Long intId = IdUtil.getLongUUID();
			Tv3SceneInfo tv3SceneInfo = new Tv3SceneInfo();
			tv3SceneInfo.setAcquireWay(tv3SceneExcelWebInfo.getAcquireWay());
			tv3SceneInfo.setCityId(tv3SceneExcelWebInfo.getCityId());
			tv3SceneInfo.setCityName(tv3SceneExcelWebInfo.getCityName());
			tv3SceneInfo.setCreateTime(new Date());
			tv3SceneInfo.setCreateUserid(ssoIdentity.getUserId());
			tv3SceneInfo.setCreateUsername(ssoIdentity.getUserName());
			tv3SceneInfo.setCurLevel(tv3SceneExcelWebInfo.getCurLevel());
			tv3SceneInfo.setDescription(tv3SceneExcelWebInfo.getDescription());
			tv3SceneInfo.setGeometryId(tv3SceneExcelWebInfo.getGeometryId());
			tv3SceneInfo.setIntId(intId);
			tv3SceneInfo.setImageName(tv3SceneExcelWebInfo.getImageName());
			tv3SceneInfo.setIsLocking(tv3SceneExcelWebInfo.getIsLocking());
			tv3SceneInfo.setIsSharing(tv3SceneExcelWebInfo.getIsSharing());
			tv3SceneInfo.setIsNeedGirdSize(tv3SceneExcelWebInfo.getIsNeedGirdSize());
			tv3SceneInfo.setIsNeedBigdataSum(tv3SceneExcelWebInfo.getIsNeedBigdataSum());
			tv3SceneInfo.setIsNeedPerformanceSum(tv3SceneExcelWebInfo.getIsNeedPerformanceSum());
			tv3SceneInfo.setIsNeedSceneSize(tv3SceneExcelWebInfo.getIsNeedSceneSize());
			tv3SceneInfo.setIsNeedCellSize(tv3SceneExcelWebInfo.getIsNeedCellSize());
			tv3SceneInfo.setOrderNo(tv3SceneExcelWebInfo.getOrderNo());
			tv3SceneInfo.setParentId(tv3SceneExcelWebInfo.getParentId());
			tv3SceneInfo.setRegionId(tv3SceneExcelWebInfo.getRegionId());
			tv3SceneInfo.setRegionName(tv3SceneExcelWebInfo.getRegionName());
			tv3SceneInfo.setSceneArea(tv3SceneExcelWebInfo.getSceneArea());
			tv3SceneInfo.setCentriodLat(tv3SceneExcelWebInfo.getSceneCentriodLat());
			tv3SceneInfo.setCentriodLon(tv3SceneExcelWebInfo.getSceneCentriodLon());
			tv3SceneInfo.setGeojson(tv3SceneExcelWebInfo.getGeojson());
			tv3SceneInfo.setSceneLength(tv3SceneExcelWebInfo.getSceneLength());
			tv3SceneInfo.setName(tv3SceneExcelWebInfo.getName());
			tv3SceneInfo.setTechnology(tv3SceneExcelWebInfo.getTechnology());
			tv3SceneInfo.setRootId(tv3SceneExcelWebInfo.getRootId());
			tv3SceneInfo.setTag(tv3SceneExcelWebInfo.getTag());
			tv3SceneInfoMapper.insertSelective(tv3SceneInfo);

			List<Tv3SceneCell> tv3GisCellrelateGeos = tv3SceneExcelWebInfo.getTv3GisCellrelateGeos();
			for (Tv3SceneCell tv3GisCellrelateGeo : tv3GisCellrelateGeos) {
				tv3GisCellrelateGeo.setSceneId(intId);
				tv3SceneCellMapper.insertSelective(tv3GisCellrelateGeo);
			}

			//
			tv3SceneExcelWebInfo.setIntId(intId);
			tv3SceneExcelWebInfo.setTv3GisCellrelateGeos(tv3GisCellrelateGeos);

		}

		return ResponseMessage2.Success2(tv3SceneExcelWebInfos);
	}

	@Override
	public void exportIniFile(HttpServletResponse response) {
		try {
			String fileName = "source.ini";
			String contentFile = getIniFileContent();
			response.setContentType("application/octet-stream;charset=utf-8"); // 改成输出excel文件
			response.setHeader("Content-disposition",
					"attachment; filename=" + new String(fileName.getBytes(), "ISO-8859-1"));
			OutputStream out = response.getOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(out);
			writer.write(contentFile, 0, contentFile.length());
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getIniFileContent() {
		StringBuffer stringBuffer = new StringBuffer();

		List<IniFileInfoPO> iniFileInfoPOs = senceManageMapper.selectIniFileInfo();

		for (IniFileInfoPO iniFileInfoPO : iniFileInfoPOs) {
			stringBuffer.append(iniFileInfoPO.getIntId()).append(",").append(iniFileInfoPO.getName()).append(",")
					.append(iniFileInfoPO.getCurLevel()).append(",").append(iniFileInfoPO.getTechnology()).append(",")
					.append(iniFileInfoPO.getRegionId()).append(",").append(iniFileInfoPO.getIsSharing()).append("|")
					.append(iniFileInfoPO.getIsNeedSceneSize()).append(",").append(iniFileInfoPO.getIsNeedBigdataSum())
					.append(",").append(iniFileInfoPO.getIsNeedPerformanceSum()).append(",")
					.append(iniFileInfoPO.getIsNeedGirdSize()).append(",").append(iniFileInfoPO.getIsNeedCellSize())
					.append("|").append(iniFileInfoPO.getTreeIds()).append("|").append(iniFileInfoPO.getTreeNames())
					.append("|").append(iniFileInfoPO.getCellIds()).append("\r\n");
		}

		return stringBuffer.toString();
	}

	@Override
	public void exportOldIniFile(HttpServletResponse response) {
		try {
			String fileName = "source.ini";

			List<OldIniFileInfoPO> oldIniFileInfoPOs = senceManageMapper.selectOldIniFileInfo();
			StringBuffer contentFile = new StringBuffer("");
			for (int i = 0; i < oldIniFileInfoPOs.size(); i++) {
				OldIniFileInfoPO oldIniFileInfoPO = oldIniFileInfoPOs.get(i);
				if (!StringUtil.isEmpty(oldIniFileInfoPO.getSceneInfo()) && oldIniFileInfoPO.getCellId() != null) {
					if (i != oldIniFileInfoPOs.size() - 1) {
						contentFile.append(oldIniFileInfoPO.getCellId().toString()).append("|")
								.append(oldIniFileInfoPO.getSceneInfo()).append("\r\n");
					} else {
						contentFile.append(oldIniFileInfoPO.getCellId().toString()).append("|")
								.append(oldIniFileInfoPO.getSceneInfo());
					}

				}
			}

			response.setContentType("application/octet-stream;charset=utf-8"); // 改成输出excel文件
			response.setHeader("Content-disposition",
					"attachment; filename=" + new String(fileName.getBytes(), "ISO-8859-1"));
			OutputStream out = response.getOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(out);
			writer.write(contentFile.toString(), 0, contentFile.toString().length());
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ResponseMessage2<List<SceneTreeChildrenData>> getSceneTreeData(Long sceneId, String sceneName,
			HttpServletRequest request) {

		SSOIdentity ssoIdentity = FrameworkMicroserviceApplication.getApplication().getSSO().getIdentity(request);

		if (ssoIdentity == null) {
			return ResponseMessage2.Failed("未查询到当前登录用户！");
		}

		// 场景树
		List<SceneTreeChildrenData> result = new ArrayList<>();

		// 共享场景树
		List<SceneTreeChildrenData> shareSceneTreeChildrenDatas = new ArrayList<>();

		// 个人私有树
		List<SceneTreeChildrenData> privateSceneTreeChildrenDatas = new ArrayList<>();

		// 超级管理员id 63c26d7f-6e39-40b0-9526-94c58f2d77ca
		List<DefaultSceneTreeInfoPO> defaultSceneTreeInfoPOs = new ArrayList<>();
		if (!ssoIdentity.getIsAdministrator()) {
			// 非管理员用户只能查看所有共享场景和自己的私有场景
			defaultSceneTreeInfoPOs = senceManageMapper.selectDefaultSceneTreeInfo(
					sceneId != null ? sceneId + "%" : null, sceneName, ssoIdentity.getUserId());

		} else {
			// 管理员能查看所有共享场景和私有场景
			defaultSceneTreeInfoPOs = senceManageMapper
					.selectAllDefaultSceneTreeInfo(sceneId != null ? sceneId + "%" : null, sceneName);
		}

		for (DefaultSceneTreeInfoPO defaultSceneTreeInfoPO : defaultSceneTreeInfoPOs) {

			// 根场景
			if (defaultSceneTreeInfoPO.getParentId() == null || defaultSceneTreeInfoPO.getParentId() == -1
					|| defaultSceneTreeInfoPO.getIntId() == sceneId
					|| defaultSceneTreeInfoPO.getName().equals(sceneName)) {

				List<DefaultSceneTreeInfoPO> newDefaultSceneTreeInfoPOs = new ArrayList<>(defaultSceneTreeInfoPOs);

				newDefaultSceneTreeInfoPOs.remove(defaultSceneTreeInfoPO);
				List<SceneTreeChildrenData> shareTreeChildrenDatas = getShareTreeList(defaultSceneTreeInfoPO,
						newDefaultSceneTreeInfoPOs);
				List<SceneTreeChildrenData> privateTreeChildrenDatas = getPrivateTreeList(defaultSceneTreeInfoPO,
						newDefaultSceneTreeInfoPOs);

				SceneTreeChildrenData shareChildrenData = new SceneTreeChildrenData();
				shareChildrenData.setIsSharing(defaultSceneTreeInfoPO.getIsSharing());
				shareChildrenData.setSceneId(defaultSceneTreeInfoPO.getIntId());
				shareChildrenData.setSceneName(defaultSceneTreeInfoPO.getName());
				shareChildrenData.setCellRelateCount(defaultSceneTreeInfoPO.getCellRelateCount());
				shareChildrenData.setIsExistGeojson(defaultSceneTreeInfoPO.getIsExistGeojson());

				if (shareTreeChildrenDatas == null || shareTreeChildrenDatas.isEmpty()) {
					shareChildrenData.setLabel(
							defaultSceneTreeInfoPO.getName() + "(" + defaultSceneTreeInfoPO.getCellRelateCount() + ")");
					if (defaultSceneTreeInfoPO.getIsSharing() == 1) {
						shareChildrenData.setChildren(shareTreeChildrenDatas);
						shareSceneTreeChildrenDatas.add(shareChildrenData);
					}
				} else {
					shareChildrenData.setLabel(defaultSceneTreeInfoPO.getName());
					shareChildrenData.setChildren(shareTreeChildrenDatas);
					shareSceneTreeChildrenDatas.add(shareChildrenData);
				}

				SceneTreeChildrenData privateChildrenData = new SceneTreeChildrenData();
				privateChildrenData.setIsSharing(defaultSceneTreeInfoPO.getIsSharing());
				privateChildrenData.setSceneId(defaultSceneTreeInfoPO.getIntId());
				privateChildrenData.setSceneName(defaultSceneTreeInfoPO.getName());
				privateChildrenData.setCellRelateCount(defaultSceneTreeInfoPO.getCellRelateCount());
				shareChildrenData.setIsExistGeojson(defaultSceneTreeInfoPO.getIsExistGeojson());

				if (privateTreeChildrenDatas == null || privateTreeChildrenDatas.isEmpty()) {
					privateChildrenData.setLabel(
							defaultSceneTreeInfoPO.getName() + "(" + defaultSceneTreeInfoPO.getCellRelateCount() + ")");
					if (defaultSceneTreeInfoPO.getIsSharing() != 1) {
						privateChildrenData.setChildren(privateTreeChildrenDatas);
						privateSceneTreeChildrenDatas.add(privateChildrenData);
					}

				} else {
					privateChildrenData.setLabel(defaultSceneTreeInfoPO.getName());
					privateChildrenData.setChildren(privateTreeChildrenDatas);
					privateSceneTreeChildrenDatas.add(privateChildrenData);
				}

			}

		}
		result.add(new SceneTreeChildrenData("共享场景", shareSceneTreeChildrenDatas, 1L, "共享场景", 1, 0, 0));
		result.add(new SceneTreeChildrenData("私有场景", privateSceneTreeChildrenDatas, 0L, "私有场景", 0, 0, 0));
		return ResponseMessage2.Success2(result);
	}

	/**
	 * 获取共享场景树
	 * 
	 * @param defaultSceneTreeInfoPO
	 * @param defaultSceneTreeInfoPOs
	 * @return
	 */
	private List<SceneTreeChildrenData> getShareTreeList(DefaultSceneTreeInfoPO defaultSceneTreeInfoPO,
			List<DefaultSceneTreeInfoPO> defaultSceneTreeInfoPOs) {

		List<SceneTreeChildrenData> childreList = new ArrayList<>();// 子节点list

		for (DefaultSceneTreeInfoPO sceneTreeInfoPO : defaultSceneTreeInfoPOs) {

			if (defaultSceneTreeInfoPO.getIntId().equals(sceneTreeInfoPO.getParentId())) {// 判断是否当前节点的子节点

				SceneTreeChildrenData sceneTreeChildrenData = new SceneTreeChildrenData();

				List<DefaultSceneTreeInfoPO> newDefaultSceneTreeInfoPOs = new ArrayList<>(defaultSceneTreeInfoPOs);

				newDefaultSceneTreeInfoPOs.remove(sceneTreeInfoPO);
				List<SceneTreeChildrenData> treeChildrenDatas = getShareTreeList(sceneTreeInfoPO,
						newDefaultSceneTreeInfoPOs);
				if (treeChildrenDatas == null || treeChildrenDatas.isEmpty()) {
					sceneTreeChildrenData
							.setLabel(sceneTreeInfoPO.getName() + "(" + sceneTreeInfoPO.getCellRelateCount() + ")");
					if (sceneTreeInfoPO.getIsSharing() != 1) {
						continue;
					}
				} else {
					sceneTreeChildrenData.setLabel(sceneTreeInfoPO.getName());
				}

				sceneTreeChildrenData.setChildren(treeChildrenDatas);
				sceneTreeChildrenData.setSceneId(sceneTreeInfoPO.getIntId());
				sceneTreeChildrenData.setSceneName(sceneTreeInfoPO.getName());
				sceneTreeChildrenData.setCellRelateCount(sceneTreeInfoPO.getCellRelateCount());
				sceneTreeChildrenData.setIsExistGeojson(defaultSceneTreeInfoPO.getIsExistGeojson());
				sceneTreeChildrenData.setIsSharing(sceneTreeInfoPO.getIsSharing());
				childreList.add(sceneTreeChildrenData);

			}
		}

		return childreList;
	}

	/**
	 * 获取私有场景树
	 * 
	 * @param defaultSceneTreeInfoPO
	 * @param defaultSceneTreeInfoPOs
	 * @return
	 */
	private List<SceneTreeChildrenData> getPrivateTreeList(DefaultSceneTreeInfoPO defaultSceneTreeInfoPO,
			List<DefaultSceneTreeInfoPO> defaultSceneTreeInfoPOs) {

		List<SceneTreeChildrenData> childreList = new ArrayList<>();// 子节点list

		for (DefaultSceneTreeInfoPO sceneTreeInfoPO : defaultSceneTreeInfoPOs) {

			if (defaultSceneTreeInfoPO.getIntId().equals(sceneTreeInfoPO.getParentId())) {// 判断是否当前节点的子节点

				SceneTreeChildrenData sceneTreeChildrenData = new SceneTreeChildrenData();

				List<DefaultSceneTreeInfoPO> newDefaultSceneTreeInfoPOs = new ArrayList<>(defaultSceneTreeInfoPOs);

				newDefaultSceneTreeInfoPOs.remove(sceneTreeInfoPO);

				List<SceneTreeChildrenData> treeChildrenDatas = getPrivateTreeList(sceneTreeInfoPO,
						newDefaultSceneTreeInfoPOs);
				if (treeChildrenDatas == null || treeChildrenDatas.isEmpty()) {
					sceneTreeChildrenData
							.setLabel(sceneTreeInfoPO.getName() + "(" + sceneTreeInfoPO.getCellRelateCount() + ")");
					if (sceneTreeInfoPO.getIsSharing() == 1) {
						continue;
					}
				} else {
					sceneTreeChildrenData.setLabel(sceneTreeInfoPO.getName());
				}

				sceneTreeChildrenData.setChildren(treeChildrenDatas);
				sceneTreeChildrenData.setSceneId(sceneTreeInfoPO.getIntId());
				sceneTreeChildrenData.setSceneName(sceneTreeInfoPO.getName());
				sceneTreeChildrenData.setCellRelateCount(sceneTreeInfoPO.getCellRelateCount());
				sceneTreeChildrenData.setIsExistGeojson(sceneTreeInfoPO.getIsExistGeojson());
				sceneTreeChildrenData.setIsSharing(sceneTreeInfoPO.getIsSharing());
				childreList.add(sceneTreeChildrenData);

			}
		}

		return childreList;
	}

	@Override
	public SceneDetailedInfoPO selectSceneDetailedInfoById(Long intId) {
		return senceManageMapper.selectSceneDetailedInfoById(intId);
	}

	@Override
	public ResponseMessage2<PageInfo<Tv3SceneCell>> selectDefaultCellRelateBySceneId(Long intId, Integer pageNum,
			Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Tv3SceneCell tv3SceneCell = new Tv3SceneCell();
		tv3SceneCell.setSceneId(intId);
		PageInfo<Tv3SceneCell> pageInfo = new PageInfo<>(tv3SceneCellMapper.select(tv3SceneCell));

		return ResponseMessage2.Success2(pageInfo);
	}

	@Override
	public ResponseMessage2<SceneGridConfig> getSceneGridConfigInfo() {
		SceneGridConfig result = new SceneGridConfig();
		result.setGridBorderLength(sceneImageAutoConfiguration.getGridBorderLength());
		result.setGridStep(sceneImageAutoConfiguration.getGridStep());
		result.setGridExtendWidth(sceneImageAutoConfiguration.getGridExtendWidth());
		return ResponseMessage2.Success2(result);
	}

	@Override
	public ResponseMessage2<String> buildAllGridData(BuildGridDataConfigWebPar buildGridDataConfigWebPar) {
		Example example = new Example(Tv3SceneInfo.class);
		Criteria criteria = example.createCriteria();
		criteria.andIsNotNull("geojson");
		List<Tv3SceneInfo> tv3SceneInfos = tv3SceneInfoMapper.selectByExample(example);
		log.debug("将要生成" + tv3SceneInfos.size() + "个场景栅格！");
		// 删除全部栅格、路段数据
		tv3SceneGridMapper.delete(new Tv3SceneGrid());
		tv3SceneDtgridMapper.delete(new Tv3SceneDtgrid());

		for (Tv3SceneInfo tv3SceneInfo : tv3SceneInfos) {
			ExecuteGridDataBuildImportThread executeGridDataBuildImportThread = new ExecuteGridDataBuildImportThread(
					buildGridDataConfigWebPar, tv3SceneInfo);
			taskExecutor.execute(executeGridDataBuildImportThread);
		}

		return ResponseMessage2.Success("栅格后台生成中！");
	}

	@Override
	public ResponseMessage2<String> buildNewGridData(BuildGridDataConfigWebPar buildGridDataConfigWebPar) {

		Example example = new Example(Tv3SceneInfo.class);
		Criteria criteria = example.createCriteria();
		criteria.andIsNotNull("geojson");
		List<Tv3SceneInfo> tv3SceneInfos = tv3SceneInfoMapper.selectByExample(example);
		log.debug("将要检查" + tv3SceneInfos.size() + "个场景栅格！");
		for (Tv3SceneInfo tv3SceneInfo : tv3SceneInfos) {
			Tv3SceneGrid tv3SceneGrid = new Tv3SceneGrid();
			if (tv3SceneInfo.getGeojsonType() != null
					&& "POLYGON".equals(tv3SceneInfo.getGeojsonType().toUpperCase())) {
				tv3SceneGrid.setGridBorderLength(buildGridDataConfigWebPar.getPolygonGridBorderLength());
			} else {
				tv3SceneGrid.setGridBorderLength(buildGridDataConfigWebPar.getLineStringGridBorderLength());
			}

			tv3SceneGrid.setSceneId(tv3SceneInfo.getIntId());
			Integer count = tv3SceneGridMapper.selectCount(tv3SceneGrid);

			if (count == 0) {
				ExecuteGridDataBuildImportThread executeGridDataBuildImportThread = new ExecuteGridDataBuildImportThread(
						buildGridDataConfigWebPar, tv3SceneInfo);
				taskExecutor.execute(executeGridDataBuildImportThread);
			}

		}
		return ResponseMessage2.Success("栅格后台生成中！");
	}

}
