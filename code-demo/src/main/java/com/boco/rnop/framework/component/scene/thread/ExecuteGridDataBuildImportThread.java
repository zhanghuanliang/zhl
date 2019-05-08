package com.boco.rnop.framework.component.scene.thread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.boco.com.framework.gis.engine.geometrys.EnginePoint;
import com.boco.com.framework.gis.engine.geometrys.EnginePolygon;
import com.boco.com.framework.gis.engine.geometrys.MilitaryGrid;
import com.boco.com.framework.gis.engine.repositorys.LineExtendRepertory;
import com.boco.com.framework.gis.engine.repositorys.MilitaryGridRepository;
import com.boco.rnop.framework.common.util.IdUtil;
import com.boco.rnop.framework.common.util.SpringContextUtil;
import com.boco.rnop.framework.component.scene.entity.BuildGridDataConfigWebPar;
import com.boco.rnop.framework.component.scene.entity.SceneCrawlingDataGeoObject;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3SceneDtgrid;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3SceneGrid;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3SceneInfo;
import com.boco.rnop.framework.component.scene.service.ISceneManageService;
import com.boco.rnop.framework.component.scene.util.GeojsonUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 执行栅格数据生成导入线程
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2019年1月22日 下午3:45:35
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@Slf4j
public class ExecuteGridDataBuildImportThread implements Runnable {

	private ISceneManageService sceneManageService = SpringContextUtil.getBean(ISceneManageService.class);

	/**
	 * 生成栅格数据配置
	 */
	private BuildGridDataConfigWebPar buildGridDataConfigWebPar;
	/**
	 * 场景信息
	 */
	private Tv3SceneInfo tv3SceneInfo;

	public ExecuteGridDataBuildImportThread(BuildGridDataConfigWebPar buildGridDataConfigWebPar,
			Tv3SceneInfo tv3SceneInfo) {
		super();
		this.buildGridDataConfigWebPar = buildGridDataConfigWebPar;
		this.tv3SceneInfo = tv3SceneInfo;
	}

	@Override
	public void run() {
		log.debug(Thread.currentThread().getName() + ":开始生成栅格！");
		if (tv3SceneInfo.getGeojson() != null && !"".equals(tv3SceneInfo.getGeojson())) {

			SceneCrawlingDataGeoObject sceneCrawlingDataGeoObject = GeojsonUtil
					.getSceneCrawlingDataGeoObject(tv3SceneInfo.getGeojson());
			if (sceneCrawlingDataGeoObject == null) {
				log.warn(tv3SceneInfo.getIntId() + "该场景geojson解析失败！");
				return;
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

			String geoType = sceneCrawlingDataGeoObject.getType().toUpperCase();

			switch (geoType) {
			case "POLYGON":
				// 面
				// 军事栅格数据集合
				List<MilitaryGrid> grids = new ArrayList<>();
				log.debug("生成面军事栅格数据！");
				for (List<EnginePoint> enginePoints : polygons) {
					grids.addAll(MilitaryGridRepository.GetPolygonGrid(enginePoints,
							buildGridDataConfigWebPar.getPolygonGridBorderLength()));
				}
				log.debug("生成" + grids.size() + "个军事栅格！");
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
					tv3GisGrid.setDtgridId(-1L);
					tv3GisGrid.setGridBorderLength(buildGridDataConfigWebPar.getPolygonGridBorderLength());
					tv3GisGrids.add(tv3GisGrid);
				}
				break;
			case "LINESTRING":
				// 线
				log.debug("生成线军事栅格数据！");
				for (List<EnginePoint> enginePoints : polygons) {
					LineExtendRepertory lineExtendRepertory = new LineExtendRepertory(enginePoints,
							buildGridDataConfigWebPar.getLineStringGridStep(),
							buildGridDataConfigWebPar.getLineStringGridExtendWidth(), true);
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
							tv3SceneDtgrid.setStep(buildGridDataConfigWebPar.getLineStringGridStep());
							// 路段扩充宽度
							tv3SceneDtgrid.setExtendWidth(buildGridDataConfigWebPar.getLineStringGridExtendWidth());
							// 路段序号
							tv3SceneDtgrid.setOrderNum(i + 1);
							tv3SceneDtgrid.setSceneId(tv3SceneInfo.getIntId());
							tv3SceneDtgrid.setSceneName(tv3SceneInfo.getName());
							tv3SceneDtgrid.setInsertTime(new Date());
							tv3SceneDtgrids.add(tv3SceneDtgrid);

							List<MilitaryGrid> dtGrids = MilitaryGridRepository.GetPolygonGrid(Arrays.asList(points),
									buildGridDataConfigWebPar.getLineStringGridBorderLength());
							for (MilitaryGrid militaryGrid : dtGrids) {
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
								tv3GisGrid.setSceneId(tv3SceneInfo.getIntId());
								tv3GisGrid.setSceneName(tv3SceneInfo.getName());
								tv3GisGrid
										.setGridBorderLength(buildGridDataConfigWebPar.getLineStringGridBorderLength());
								tv3GisGrid.setRegionId(tv3SceneInfo.getRegionId());
								tv3GisGrid.setRegionName(tv3SceneInfo.getRegionName());
								tv3GisGrid.setDtgridId(dtgridId);
								tv3GisGrids.add(tv3GisGrid);

							}

						}

					}
				}

				break;

			default:
				break;
			}

			if (!tv3GisGrids.isEmpty()) {
				sceneManageService.insertBatchGridData(tv3GisGrids, 1000);
				log.debug("入库" + tv3GisGrids.size() + "条栅格数据！");
			}

			if (!tv3SceneDtgrids.isEmpty()) {
				sceneManageService.insertBatchDTGridData(tv3SceneDtgrids, 1000);
				log.debug("入库" + tv3SceneDtgrids.size() + "条路段栅格数据！");
			}

		}

	}

}
