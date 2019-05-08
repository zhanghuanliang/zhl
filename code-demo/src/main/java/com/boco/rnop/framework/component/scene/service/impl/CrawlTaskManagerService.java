package com.boco.rnop.framework.component.scene.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import javax.servlet.http.HttpServletRequest;

import org.gdal.ogr.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.boco.com.framework.gis.engine.geometrys.EnginePoint;
import com.boco.com.framework.gis.engine.geometrys.MilitaryGrid;
import com.boco.com.framework.gis.engine.models.EngineFeature;
import com.boco.com.framework.gis.engine.models.EngineLayerAttrInfo;
import com.boco.com.framework.gis.engine.repositorys.MilitaryGridRepository;
import com.boco.com.framework.gis.engine.utils.GdalTools;
import com.boco.rnop.framework.common.ResponseMessage2;
import com.boco.rnop.framework.common.util.IdUtil;
import com.boco.rnop.framework.common.util.SpringContextUtil;
import com.boco.rnop.framework.component.scene.entity.AnalysisGeometryResultInfo;
import com.boco.rnop.framework.component.scene.entity.FieldDescriptionInfo;
import com.boco.rnop.framework.component.scene.entity.SceneCrawlingDataGeoObject;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3GisGeometry;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3GisGeometryTemporary;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3PoiCrawlTask;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3PoiCrawlTaskRegion;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3PoiCrawlTaskSty;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3PoiTypeRelation;
import com.boco.rnop.framework.component.scene.mapper.def.ICrawlTaskManagerMapper;
import com.boco.rnop.framework.component.scene.mapper.def.ITv3GisGeometryMapper;
import com.boco.rnop.framework.component.scene.mapper.def.ITv3PoiCrawlTaskMapper;
import com.boco.rnop.framework.component.scene.mapper.def.ITv3PoiCrawlTaskRegionMapper;
import com.boco.rnop.framework.component.scene.mapper.def.ITv3PoiCrawlTaskStyMapper;
import com.boco.rnop.framework.component.scene.mapper.def.ITv3PoiTypeRelationMapper;
import com.boco.rnop.framework.component.scene.service.ICrawlTaskManagerService;
import com.boco.rnop.framework.component.scene.thread.ExecuteGeomentryCrawlTaskThread;
import com.boco.rnop.framework.component.scene.thread.ExecuteGeomentryNameCrawlTaskThread;
import com.boco.rnop.framework.component.scene.util.GeojsonUtil;
import com.boco.rnop.framework.parent.microservice.FrameworkMicroserviceApplication;
import com.boco.rnop.framework.parent.microservice.client.SSOIdentity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Slf4j
@Service
public class CrawlTaskManagerService implements ICrawlTaskManagerService {

	@Autowired
	private ITv3PoiCrawlTaskRegionMapper tv3GisCrawlTaskRegionMapper;
	@Autowired
	private ITv3PoiCrawlTaskStyMapper tv3GisCrawlTaskStyMapper;
	@Autowired
	private ITv3PoiCrawlTaskMapper tv3GisCrawlTaskMapper;
	@Autowired
	private ITv3PoiTypeRelationMapper tv3GisDictionaryMapper;
	@Autowired
	private ITv3GisGeometryMapper tv3GisGeometryMapper;
	@Autowired
	private ICrawlTaskManagerMapper crawlTaskManagerMapper;
	@Autowired
	private TaskExecutor taskExecutor;

	@Override
	public PageInfo<Tv3PoiCrawlTask> selectCrawlTaskInfo(Integer taskType, Integer taskState, String taskName,
			Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		Example example = new Example(Tv3PoiCrawlTask.class);
		example.orderBy("updateDate").desc();
		Criteria criteria = example.createCriteria();
		if (taskType != null) {
			criteria.andEqualTo("taskType", taskType);
		}
		if (taskState != null) {
			criteria.andEqualTo("taskState", taskState);
		}
		if (taskName != null) {
			criteria.andLike("taskName", "%" + taskName + "%");
		}
		List<Tv3PoiCrawlTask> tv3GisCrawlTasks = tv3GisCrawlTaskMapper.selectByExample(example);

		for (Tv3PoiCrawlTask tv3GisCrawlTask : tv3GisCrawlTasks) {
			Example tv3GisCrawlTaskRegionExample = new Example(Tv3PoiCrawlTaskRegion.class);
			tv3GisCrawlTaskRegionExample.orderBy("orderNum");
			tv3GisCrawlTaskRegionExample.createCriteria().andEqualTo("taskId", tv3GisCrawlTask.getIntId());
			tv3GisCrawlTask.setTv3GisCrawlTaskRegions(
					tv3GisCrawlTaskRegionMapper.selectByExample(tv3GisCrawlTaskRegionExample));

			Example tv3GisCrawlTaskStyExample = new Example(Tv3PoiCrawlTaskSty.class);
			tv3GisCrawlTaskStyExample.orderBy("orderNum");
			tv3GisCrawlTaskStyExample.createCriteria().andEqualTo("taskId", tv3GisCrawlTask.getIntId());
			tv3GisCrawlTask
					.setTv3GisCrawlTaskSties(tv3GisCrawlTaskStyMapper.selectByExample(tv3GisCrawlTaskStyExample));

		}
		PageInfo<Tv3PoiCrawlTask> pageInfo = new PageInfo<>(tv3GisCrawlTasks);
		return pageInfo;
	}

	@Transactional
	@Override
	public Integer deleteCrawlTaskInfo(Long intId) {
		Integer result = 0;

		result += tv3GisCrawlTaskMapper.deleteByPrimaryKey(intId);

		Example tv3GisCrawlTaskRegionExample = new Example(Tv3PoiCrawlTaskRegion.class);
		tv3GisCrawlTaskRegionExample.createCriteria().andEqualTo("taskId", intId);
		result += tv3GisCrawlTaskRegionMapper.deleteByExample(tv3GisCrawlTaskRegionExample);

		Example tv3GisCrawlTaskStyExample = new Example(Tv3PoiCrawlTaskSty.class);
		tv3GisCrawlTaskStyExample.createCriteria().andEqualTo("taskId", intId);
		result += tv3GisCrawlTaskStyMapper.deleteByExample(tv3GisCrawlTaskStyExample);

		return result;
	}

	@Override
	public ResponseMessage2<Integer> updateCrawlTaskStateInfo(Long intId, Integer taskState) {

		Tv3PoiCrawlTask tv3PoiCrawlTask = tv3GisCrawlTaskMapper.selectByPrimaryKey(intId);
		if (tv3PoiCrawlTask == null) {
			return ResponseMessage2.Failed("未查询到该任务！");
		} else if (tv3PoiCrawlTask.getTaskType() != 1) {
			// 该任务不是地理图元爬取任务，不允许修改任务状态
			return ResponseMessage2.Failed("该任务不是地理图元爬取任务，不允许修改任务状态！");
		}

		Tv3PoiCrawlTask record = new Tv3PoiCrawlTask();
		record.setIntId(intId);
		record.setTaskState(taskState);
		record.setUpdateDate(new Date());
		return ResponseMessage2.Success2(tv3GisCrawlTaskMapper.updateByPrimaryKeySelective(record));
	}

	@Transactional
	@Override
	public ResponseMessage2<Integer> insertCrawlTaskInfos(HttpServletRequest request,
			List<Tv3PoiCrawlTask> tv3GisCrawlTasks) {

		SSOIdentity ssoIdentity = FrameworkMicroserviceApplication.getApplication().getSSO().getIdentity(request);

		if (ssoIdentity == null) {
			log.warn("未查询到当前用户信息！");
			return ResponseMessage2.Failed("未查询到当前登录用户信息！");
		}

		// 校验是否存在行政区轮廓数据
		List<String> errorRegionNames = new ArrayList<>();

		for (Tv3PoiCrawlTask tv3PoiCrawlTask : tv3GisCrawlTasks) {
			List<Tv3PoiCrawlTaskRegion> tv3PoiCrawlTaskRegions = tv3PoiCrawlTask.getTv3GisCrawlTaskRegions();
			if ((tv3PoiCrawlTask.getCrawlQueryNames() == null || "".equals(tv3PoiCrawlTask.getCrawlQueryNames()))
					&& tv3PoiCrawlTaskRegions != null) {
				for (Tv3PoiCrawlTaskRegion tv3PoiCrawlTaskRegion : tv3PoiCrawlTaskRegions) {
					Tv3GisGeometry tv3GisGeometry = new Tv3GisGeometry();
					tv3GisGeometry.setName(tv3PoiCrawlTaskRegion.getRegionName());
					tv3GisGeometry.setGeometryType(3);
					if (tv3GisGeometryMapper.selectCount(tv3GisGeometry) <= 0) {
						errorRegionNames.add(tv3PoiCrawlTaskRegion.getRegionName());
					}

				}
			}

		}
		if (!errorRegionNames.isEmpty()) {
			StringBuffer errorMessage = new StringBuffer();
			for (String string : errorRegionNames) {
				errorMessage.append(string).append("、");
			}
			errorMessage.append("未查询到行政区外廓数据，请先爬取该行政区外廓数据！");
			return ResponseMessage2.Failed(errorMessage.toString());
		}

		Integer result = 0;
		for (Tv3PoiCrawlTask tv3GisCrawlTask : tv3GisCrawlTasks) {
			// 任务栅格数量
			Integer taskGridNum = 0;
			// 任务编号
			Long taskId = IdUtil.getLongUUID();

			// 关联地市
			if (tv3GisCrawlTask.getTv3GisCrawlTaskRegions() != null) {
				List<Tv3PoiCrawlTaskRegion> tv3GisCrawlTaskRegions = new ArrayList();
				for (int i = 0; i < tv3GisCrawlTask.getTv3GisCrawlTaskRegions().size(); i++) {
					Tv3PoiCrawlTaskRegion tv3GisCrawlTaskRegion = tv3GisCrawlTask.getTv3GisCrawlTaskRegions().get(i);
					tv3GisCrawlTaskRegion.setOrderNum(i);
					tv3GisCrawlTaskRegion.setTaskId(taskId);
					tv3GisCrawlTaskRegion.setIntId(IdUtil.getLongUUID());
					tv3GisCrawlTaskRegions.add(tv3GisCrawlTaskRegion);

					// 如果名称爬取，无需计算地市栅格数量
					if (tv3GisCrawlTask.getCrawlQueryNames() == null
							|| "".equals(tv3GisCrawlTask.getCrawlQueryNames())) {
						// 计算爬取栅格数量
						Tv3GisGeometry regionTv3GisGeometry = new Tv3GisGeometry();
						regionTv3GisGeometry.setName(tv3GisCrawlTaskRegion.getRegionName());
						regionTv3GisGeometry.setGeometryType(3);// 行政区边界
						List<Tv3GisGeometry> regionTv3GisGeometries = tv3GisGeometryMapper.select(regionTv3GisGeometry);
						if (regionTv3GisGeometries != null && !regionTv3GisGeometries.isEmpty()) {
							regionTv3GisGeometry = regionTv3GisGeometries.get(0);
						}

						SceneCrawlingDataGeoObject sceneCrawlingDataGeoObject = GeojsonUtil
								.getSceneCrawlingDataGeoObject(regionTv3GisGeometry.getGeometryJson());
						List<List<List<Double[]>>> lists = sceneCrawlingDataGeoObject.getPolygons();
						for (List<List<Double[]>> list : lists) {
							if (list.size() <= 0) {
								continue;
							}
							List<EnginePoint> enginePoints = new ArrayList<>();
							// TODO 带孔洞
							for (Double[] doubles : list.get(0)) {
								EnginePoint enginePoint = new EnginePoint(doubles[0], doubles[1]);
								enginePoints.add(enginePoint);
							}
							List<MilitaryGrid> grids = MilitaryGridRepository.GetPolygonGrid(enginePoints, 800);
							taskGridNum += grids.size();
						}

					}
				}
				for (Tv3PoiCrawlTaskRegion tv3GisCrawlTaskRegion : tv3GisCrawlTaskRegions) {
					result += tv3GisCrawlTaskRegionMapper.insertSelective(tv3GisCrawlTaskRegion);
				}
			}

			// 关联类型
			if (tv3GisCrawlTask.getTv3GisCrawlTaskSties() != null) {
				List<Tv3PoiCrawlTaskSty> tv3GisCrawlTaskSties = new ArrayList<>();
				for (int i = 0; i < tv3GisCrawlTask.getTv3GisCrawlTaskSties().size(); i++) {
					Tv3PoiCrawlTaskSty tv3GisCrawlTaskSty = tv3GisCrawlTask.getTv3GisCrawlTaskSties().get(i);
					Tv3PoiTypeRelation tv3GisDictionary = new Tv3PoiTypeRelation();
					tv3GisDictionary.setSceneTypeId(tv3GisCrawlTaskSty.getSceneTypeId());
					List<Tv3PoiTypeRelation> tv3GisDictionaries = tv3GisDictionaryMapper.select(tv3GisDictionary);

					// 根据网优类型分组
					Map<Long, List<Tv3PoiTypeRelation>> map = new HashMap<>();

					for (Tv3PoiTypeRelation tv3GisDictionary2 : tv3GisDictionaries) {
						if (map.containsKey(tv3GisDictionary2.getSceneTypeId())) {
							map.get(tv3GisDictionary2.getSceneTypeId()).add(tv3GisDictionary2);
						} else {
							List<Tv3PoiTypeRelation> dictionaries = new ArrayList<>();
							dictionaries.add(tv3GisDictionary2);
							map.put(tv3GisDictionary2.getSceneTypeId(), dictionaries);
						}

					}
					// 序号
					int index = 0;
					for (Entry<Long, List<Tv3PoiTypeRelation>> entry : map.entrySet()) {

						List<Tv3PoiTypeRelation> gisDictionaries = entry.getValue();

						for (int j = 0; j < gisDictionaries.size(); j++) {
							Tv3PoiTypeRelation gisDictionary = tv3GisDictionaries.get(j);
							Tv3PoiCrawlTaskSty gisCrawlTaskSty = new Tv3PoiCrawlTaskSty();
							gisCrawlTaskSty.setIntId(IdUtil.getLongUUID());
							gisCrawlTaskSty.setOrderNum(index + (j / 10));
							gisCrawlTaskSty.setPoiTypeId(gisDictionary.getPoiTypeId());
							gisCrawlTaskSty.setPoiTypeName(gisDictionary.getPoiTypeName());
							gisCrawlTaskSty.setSceneTypeId(gisDictionary.getSceneTypeId());
							gisCrawlTaskSty.setSceneTypeName(gisDictionary.getSceneTypeName());
							gisCrawlTaskSty.setTaskId(taskId);
							tv3GisCrawlTaskSties.add(gisCrawlTaskSty);
						}
						index = index + (gisDictionaries.size() / 10) + 1;
					}

				}

				for (Tv3PoiCrawlTaskSty tv3GisCrawlTaskSty : tv3GisCrawlTaskSties) {
					result += tv3GisCrawlTaskStyMapper.insertSelective(tv3GisCrawlTaskSty);
				}

			}

			if (tv3GisCrawlTask.getCrawlQueryNames() != null && !"".equals(tv3GisCrawlTask.getCrawlQueryNames())) {
				if (tv3GisCrawlTask.getCrawlQueryNames().contains(",")) {
					taskGridNum = tv3GisCrawlTask.getCrawlQueryNames().split(",").length;
				} else {
					taskGridNum = 1;
				}
			}

			// 任务
			tv3GisCrawlTask.setTaskGridNum(taskGridNum);
			tv3GisCrawlTask.setTaskState(0);
			// 地理图元爬取任务类型
			tv3GisCrawlTask.setTaskType(1);
			tv3GisCrawlTask.setTaskEndGridNum(0);
			tv3GisCrawlTask.setIntId(taskId);
			tv3GisCrawlTask.setUpdateDate(new Date());
			tv3GisCrawlTask.setCreateUserid(ssoIdentity.getUserId());
			tv3GisCrawlTask.setCreateUsername(ssoIdentity.getUserName());
			tv3GisCrawlTask.setTaskEndGridNum(0);
			result += tv3GisCrawlTaskMapper.insertSelective(tv3GisCrawlTask);

		}

		return ResponseMessage2.Success2(result);

	}

	@Override
	public ResponseMessage2<String> insertCrawlTaskInfosAndExecute(HttpServletRequest request,
			List<Tv3PoiCrawlTask> tv3GisCrawlTasks) {

		SSOIdentity ssoIdentity = FrameworkMicroserviceApplication.getApplication().getSSO().getIdentity(request);

		if (ssoIdentity == null) {
			log.warn("未查询到当前用户信息！");
			return ResponseMessage2.Failed("未查询到当前用户信息！");
		}

		// 校验是否存在行政区轮廓数据
		List<String> errorRegionNames = new ArrayList<>();

		for (Tv3PoiCrawlTask tv3PoiCrawlTask : tv3GisCrawlTasks) {
			List<Tv3PoiCrawlTaskRegion> tv3PoiCrawlTaskRegions = tv3PoiCrawlTask.getTv3GisCrawlTaskRegions();
			if ((tv3PoiCrawlTask.getCrawlQueryNames() == null || "".equals(tv3PoiCrawlTask.getCrawlQueryNames()))
					&& tv3PoiCrawlTaskRegions != null) {
				for (Tv3PoiCrawlTaskRegion tv3PoiCrawlTaskRegion : tv3PoiCrawlTaskRegions) {
					Tv3GisGeometry tv3GisGeometry = new Tv3GisGeometry();
					tv3GisGeometry.setName(tv3PoiCrawlTaskRegion.getRegionName());
					tv3GisGeometry.setGeometryType(3);
					if (tv3GisGeometryMapper.selectCount(tv3GisGeometry) <= 0) {
						errorRegionNames.add(tv3PoiCrawlTaskRegion.getRegionName());
					}

				}
			}

		}
		if (!errorRegionNames.isEmpty()) {
			StringBuffer errorMessage = new StringBuffer();
			for (String string : errorRegionNames) {
				errorMessage.append(string).append("、");
			}
			errorMessage.append("未查询到行政区外廓数据，请先爬取该行政区外廓数据！");
			return ResponseMessage2.Failed(errorMessage.toString());
		}

		Integer result = 0;
		for (Tv3PoiCrawlTask tv3GisCrawlTask : tv3GisCrawlTasks) {
			// 任务栅格数量
			Integer taskGridNum = 0;
			// 任务编号
			Long taskId = IdUtil.getLongUUID();

			// 关联地市
			if (tv3GisCrawlTask.getTv3GisCrawlTaskRegions() != null) {
				List<Tv3PoiCrawlTaskRegion> tv3GisCrawlTaskRegions = new ArrayList();
				for (int i = 0; i < tv3GisCrawlTask.getTv3GisCrawlTaskRegions().size(); i++) {
					Tv3PoiCrawlTaskRegion tv3GisCrawlTaskRegion = tv3GisCrawlTask.getTv3GisCrawlTaskRegions().get(i);
					tv3GisCrawlTaskRegion.setOrderNum(i);
					tv3GisCrawlTaskRegion.setTaskId(taskId);
					tv3GisCrawlTaskRegion.setIntId(IdUtil.getLongUUID());
					tv3GisCrawlTaskRegions.add(tv3GisCrawlTaskRegion);

					// 如果名称爬取，无需计算地市栅格数量
					if (tv3GisCrawlTask.getCrawlQueryNames() == null
							|| "".equals(tv3GisCrawlTask.getCrawlQueryNames())) {
						// 计算爬取栅格数量
						Tv3GisGeometry regionTv3GisGeometry = new Tv3GisGeometry();
						regionTv3GisGeometry.setName(tv3GisCrawlTaskRegion.getRegionName());
						regionTv3GisGeometry.setGeometryType(3);// 行政区边界
						List<Tv3GisGeometry> regionTv3GisGeometries = tv3GisGeometryMapper.select(regionTv3GisGeometry);
						if (regionTv3GisGeometries != null && !regionTv3GisGeometries.isEmpty()) {
							regionTv3GisGeometry = regionTv3GisGeometries.get(0);
						}

						SceneCrawlingDataGeoObject sceneCrawlingDataGeoObject = GeojsonUtil
								.getSceneCrawlingDataGeoObject(regionTv3GisGeometry.getGeometryJson());
						List<List<List<Double[]>>> lists = sceneCrawlingDataGeoObject.getPolygons();
						for (List<List<Double[]>> list : lists) {
							if (list.size() <= 0) {
								continue;
							}
							List<EnginePoint> enginePoints = new ArrayList<>();
							// TODO 带孔洞
							for (Double[] doubles : list.get(0)) {
								EnginePoint enginePoint = new EnginePoint(doubles[0], doubles[1]);
								enginePoints.add(enginePoint);
							}
							List<MilitaryGrid> grids = MilitaryGridRepository.GetPolygonGrid(enginePoints, 800);
							taskGridNum += grids.size();
						}

					}
				}
				for (Tv3PoiCrawlTaskRegion tv3GisCrawlTaskRegion : tv3GisCrawlTaskRegions) {
					result += tv3GisCrawlTaskRegionMapper.insertSelective(tv3GisCrawlTaskRegion);
				}
				tv3GisCrawlTask.setTv3GisCrawlTaskRegions(tv3GisCrawlTaskRegions);
			}

			// 关联类型
			if (tv3GisCrawlTask.getTv3GisCrawlTaskSties() != null) {
				List<Tv3PoiCrawlTaskSty> tv3GisCrawlTaskSties = new ArrayList<>();
				for (int i = 0; i < tv3GisCrawlTask.getTv3GisCrawlTaskSties().size(); i++) {
					Tv3PoiCrawlTaskSty tv3GisCrawlTaskSty = tv3GisCrawlTask.getTv3GisCrawlTaskSties().get(i);
					Tv3PoiTypeRelation tv3GisDictionary = new Tv3PoiTypeRelation();
					tv3GisDictionary.setSceneTypeId(tv3GisCrawlTaskSty.getSceneTypeId());
					List<Tv3PoiTypeRelation> tv3GisDictionaries = tv3GisDictionaryMapper.select(tv3GisDictionary);

					// 根据网优类型分组
					Map<Long, List<Tv3PoiTypeRelation>> map = new HashMap<>();

					for (Tv3PoiTypeRelation tv3GisDictionary2 : tv3GisDictionaries) {
						if (map.containsKey(tv3GisDictionary2.getSceneTypeId())) {
							map.get(tv3GisDictionary2.getSceneTypeId()).add(tv3GisDictionary2);
						} else {
							List<Tv3PoiTypeRelation> dictionaries = new ArrayList<>();
							dictionaries.add(tv3GisDictionary2);
							map.put(tv3GisDictionary2.getSceneTypeId(), dictionaries);
						}

					}
					// 序号
					int index = 0;
					for (Entry<Long, List<Tv3PoiTypeRelation>> entry : map.entrySet()) {

						List<Tv3PoiTypeRelation> gisDictionaries = entry.getValue();

						for (int j = 0; j < gisDictionaries.size(); j++) {
							Tv3PoiTypeRelation gisDictionary = tv3GisDictionaries.get(j);
							Tv3PoiCrawlTaskSty gisCrawlTaskSty = new Tv3PoiCrawlTaskSty();
							gisCrawlTaskSty.setIntId(IdUtil.getLongUUID());
							gisCrawlTaskSty.setOrderNum(index + (j / 10));
							gisCrawlTaskSty.setPoiTypeId(gisDictionary.getPoiTypeId());
							gisCrawlTaskSty.setPoiTypeName(gisDictionary.getPoiTypeName());
							gisCrawlTaskSty.setSceneTypeId(gisDictionary.getSceneTypeId());
							gisCrawlTaskSty.setSceneTypeName(gisDictionary.getSceneTypeName());
							gisCrawlTaskSty.setTaskId(taskId);
							tv3GisCrawlTaskSties.add(gisCrawlTaskSty);
						}
						index = index + (gisDictionaries.size() / 10) + 1;
					}

				}

				for (Tv3PoiCrawlTaskSty tv3GisCrawlTaskSty : tv3GisCrawlTaskSties) {
					result += tv3GisCrawlTaskStyMapper.insertSelective(tv3GisCrawlTaskSty);
				}

				tv3GisCrawlTask.setTv3GisCrawlTaskSties(tv3GisCrawlTaskSties);
			}

			if (tv3GisCrawlTask.getCrawlQueryNames() != null && !"".equals(tv3GisCrawlTask.getCrawlQueryNames())) {
				if (tv3GisCrawlTask.getCrawlQueryNames().contains(",")) {
					taskGridNum = tv3GisCrawlTask.getCrawlQueryNames().split(",").length;
				} else {
					taskGridNum = 1;
				}
			}

			// 修改未执行任务状态(立即执行)
			tv3GisCrawlTask.setIntId(taskId);
			// 立即执行
			tv3GisCrawlTask.setTaskState(-2);
			tv3GisCrawlTask.setUpdateDate(new Date());
			tv3GisCrawlTask.setCreateUsername(ssoIdentity.getUserId());
			tv3GisCrawlTask.setCreateUsername(ssoIdentity.getUserName());
			tv3GisCrawlTask.setTaskGridNum(taskGridNum);
			// 地理图元爬取任务类型
			tv3GisCrawlTask.setTaskType(1);
			tv3GisCrawlTask.setTaskEndGridNum(0);
			tv3GisCrawlTaskMapper.insertSelective(tv3GisCrawlTask);

			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					CrawlTaskManagerService crawlTaskManagerService = SpringContextUtil
							.getBean(CrawlTaskManagerService.class);
					crawlTaskManagerService.executeNewCrawlTask(tv3GisCrawlTask);
				}
			});

		}

		return ResponseMessage2.Success2("该任务正在执行！");
	}

	@Override
	public PageInfo<Tv3GisGeometry> selectGeometryInfosByTaskId(Long taskId, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Tv3GisGeometry record = new Tv3GisGeometry();
		record.setTaskId(taskId);
		List<Tv3GisGeometry> tv3GisGeometries = tv3GisGeometryMapper.select(record);
		return new PageInfo<>(tv3GisGeometries);
	}

	@Override
	public ResponseMessage2<AnalysisGeometryResultInfo> analysisLayerData(MultipartFile[] files) {

		if (files == null || files.length == 0) {
			return ResponseMessage2.Failed("未上传文件！");
		}

		List<String> fileNames = new ArrayList<>();

		// 服务器端文件集合
		List<File> allFiles = new ArrayList<>();

		String prefixFileName = getLayerFileUploadPath() + System.currentTimeMillis() + "_";
		for (int i = 0; i < files.length; i++) {
			String orgiginalFileName = files[i].getOriginalFilename();
			String newFileName = prefixFileName + orgiginalFileName;
			File newFile = new File(newFileName);
			try {
				files[i].transferTo(newFile);
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseMessage2.Failed("文件上传失败");
			}
			allFiles.add(newFile);
			if (newFileName.toUpperCase().contains(".TAB") || newFileName.toUpperCase().contains(".SHP")) {
				fileNames.add(newFileName);
			}
		}

		List<EngineLayerAttrInfo> list = new ArrayList<>();

		if (fileNames.size() > 0) {
			String fileName = fileNames.get(0);

			list = GdalTools.getAttrStructureInfo(fileName);

		}

		AnalysisGeometryResultInfo analysisGeometryResultInfo = new AnalysisGeometryResultInfo();
		analysisGeometryResultInfo.setLayerAttrInfos(list);

		// 删除上传文件
		for (File file : allFiles) {
			file.delete();
		}

		return ResponseMessage2.Success2(analysisGeometryResultInfo);
	}

	@Transactional
	@Override
	public ResponseMessage2<Tv3PoiCrawlTask> uploadLayerData(HttpServletRequest request, String fieldDescriptionInfos,
			Long regionId, String regionName, Integer subtype, Integer geometryType, String geometryTypeName,
			Integer dataHandleType, MultipartFile[] files) {

		SSOIdentity ssoIdentity = FrameworkMicroserviceApplication.getApplication().getSSO().getIdentity(request);

		if (ssoIdentity == null) {
			return ResponseMessage2.Failed("无法获取当前登录用户！");
		}

		if (files == null || files.length == 0) {
			return ResponseMessage2.Failed("未上传文件！");
		}

		// 文件名称集合
		Set<String> fileNames = new HashSet<>();
		// 任务名称，取上传文件名称
		String taskName = "";

		// 服务器端文件集合
		List<File> allFiles = new ArrayList<>();

		String prefixFileName = getLayerFileUploadPath() + System.currentTimeMillis() + "_";
		for (int i = 0; i < files.length; i++) {
			String orgiginalFileName = files[i].getOriginalFilename();
			String newFileName = prefixFileName + orgiginalFileName;
			File newFile = new File(newFileName);
			try {
				files[i].transferTo(newFile);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();

				return ResponseMessage2.Failed("文件上传失败！");
			}
			allFiles.add(newFile);
			if (newFileName.toUpperCase().contains(".TAB") || newFileName.toUpperCase().contains(".SHP")) {
				fileNames.add(newFileName);
				taskName += files[i].getOriginalFilename() + "-";
			}
		}

		// 解析文件
		HashMap<String, String> tabEntity = new HashMap<>();

		if (fieldDescriptionInfos != null && !"".equals(fieldDescriptionInfos)) {

		}

		List<FieldDescriptionInfo> fieldDess = parseFieldDescriptionInfos(fieldDescriptionInfos);

		for (FieldDescriptionInfo fieldDescriptionInfo : fieldDess) {
			tabEntity.put(fieldDescriptionInfo.getFieldName(), fieldDescriptionInfo.getFieldDes());
		}

		ArrayList<EngineFeature> list = new ArrayList<>();

		for (String string : fileNames) {
			list.addAll(GdalTools.ReadVectorFile(string, tabEntity));
		}

		// 任务Id
		Long taskId = IdUtil.getLongUUID();

		// 组装入库信息
		List<Tv3GisGeometryTemporary> tv3GisGeometryTemporaries = new ArrayList<>();
		// 批次号
		int batchNum = IdUtil.getIntCRC32();

		for (EngineFeature engineFeature : list) {
			Geometry geometry = null;
			try {
				geometry = Geometry.CreateFromJson(engineFeature.getGeometryJson());

			} catch (Exception e) {
				e.printStackTrace();
			}

			Tv3GisGeometryTemporary tv3GisGeometryTemporary = new Tv3GisGeometryTemporary();
			tv3GisGeometryTemporary.setBatchNum(batchNum);
			tv3GisGeometryTemporary.setUuid(engineFeature.getBdUid());
			tv3GisGeometryTemporary.setLatitude(engineFeature.getLat());
			tv3GisGeometryTemporary.setLongitude(engineFeature.getLon());
			tv3GisGeometryTemporary.setCustomId(engineFeature.getCostomId());
			if (engineFeature.getGeometryArea() == null && geometry != null) {
				tv3GisGeometryTemporary.setGeometryArea(geometry.Area());
			} else {
				tv3GisGeometryTemporary.setGeometryArea(engineFeature.getGeometryArea());
			}

			tv3GisGeometryTemporary.setGeometryJson(engineFeature.getGeometryJson());

			// 解析geojson类型
			SceneCrawlingDataGeoObject sceneCrawlingDataGeoObject = GeojsonUtil
					.getSceneCrawlingDataGeoObject(engineFeature.getGeometryJson());

			if (sceneCrawlingDataGeoObject != null) {
				tv3GisGeometryTemporary.setGeojsonType(sceneCrawlingDataGeoObject.getType());
			}

			if (engineFeature.getGeometryArea() == null && geometry != null) {
				tv3GisGeometryTemporary.setGeometryLength(geometry.Length());
			} else {
				tv3GisGeometryTemporary.setGeometryLength(engineFeature.getGeometryLength());
			}

			if (geometry != null) {
				tv3GisGeometryTemporary.setCentriodLon(geometry.Centroid().GetX());
				tv3GisGeometryTemporary.setCentriodLat(geometry.Centroid().GetY());
			}

			tv3GisGeometryTemporary.setName(engineFeature.getGeometryName());
			tv3GisGeometryTemporary.setInsertTime(new Date());
			tv3GisGeometryTemporary.setIntId(Long.valueOf(IdUtil.getIntCRC32()));
			tv3GisGeometryTemporary.setAcquireWayId(1);
			tv3GisGeometryTemporary.setAcquireWayName("导入");
			tv3GisGeometryTemporary.setGeometryType(geometryType);
			tv3GisGeometryTemporary.setGeometryTypeName(geometryTypeName);
			tv3GisGeometryTemporary.setName(engineFeature.getName());
			// 操作人
			tv3GisGeometryTemporary.setCreateUserid(ssoIdentity.getUserId());
			tv3GisGeometryTemporary.setCreateUsername(ssoIdentity.getUserName());

			tv3GisGeometryTemporary.setTaskId(taskId);
			if (regionId == null) {
				tv3GisGeometryTemporary.setRegionId(engineFeature.getRegionId());
			} else {
				tv3GisGeometryTemporary.setRegionId(regionId);
			}

			if (regionName != null && !"".equals(regionName)) {
				tv3GisGeometryTemporary.setRegionName(regionName);
			} else {
				tv3GisGeometryTemporary.setRegionName(engineFeature.getRegionName());
			}

			if (subtype == null) {
				tv3GisGeometryTemporary.setSceneTypeId(
						engineFeature.getSubtype() == null ? null : new Double(engineFeature.getSubtype()).longValue());
			} else {
				tv3GisGeometryTemporary.setSceneTypeId(subtype.longValue());
			}

			tv3GisGeometryTemporaries.add(tv3GisGeometryTemporary);
		}
		// 批量插入临时表
		insertBatchGeometryTemporary(tv3GisGeometryTemporaries, 20);

		// 删除服务器端保存的上传文件
		for (File file : allFiles) {
			file.delete();
		}

		// 将临时表数据合并到正式表
		if (dataHandleType == 1) {
			crawlTaskManagerMapper.mergeNewGeometryDataByGeometryTemporary(batchNum);
		} else {
			crawlTaskManagerMapper.mergeAllGeometryDataByGeometryTemporary(batchNum);
		}
		// 将临时表数据删除
		crawlTaskManagerMapper.deleteGisGeometryTemporaryByBatchNum(batchNum);

		// 新增任务信息
		Tv3PoiCrawlTask tv3GisCrawlTask = new Tv3PoiCrawlTask();
		tv3GisCrawlTask.setCreateUserid(ssoIdentity.getUserId());
		tv3GisCrawlTask.setCreateUsername(ssoIdentity.getUserName());
		tv3GisCrawlTask.setIntId(taskId);
		tv3GisCrawlTask.setTaskDataHandleType(dataHandleType);
		tv3GisCrawlTask.setTaskGridNum(list.size());
		tv3GisCrawlTask.setTaskName(taskName);
		tv3GisCrawlTask.setTaskState(2);
		// 图层文件上传任务类型
		tv3GisCrawlTask.setTaskType(2);
		tv3GisCrawlTask.setUpdateDate(new Date());
		tv3GisCrawlTask.setTaskEndGridNum(0);
		tv3GisCrawlTaskMapper.insertSelective(tv3GisCrawlTask);

		// 新增任务地市信息
		Tv3PoiCrawlTaskRegion tv3PoiCrawlTaskRegion = new Tv3PoiCrawlTaskRegion();
		tv3PoiCrawlTaskRegion.setIntId(IdUtil.getLongUUID());
		tv3PoiCrawlTaskRegion.setOrderNum(0);
		tv3PoiCrawlTaskRegion.setRegionId(regionId);
		tv3PoiCrawlTaskRegion.setRegionName(regionName);
		tv3PoiCrawlTaskRegion.setTaskId(taskId);
		tv3GisCrawlTaskRegionMapper.insert(tv3PoiCrawlTaskRegion);
		// 新增任务网优类型信息
		Tv3PoiCrawlTaskSty tv3PoiCrawlTaskSty = new Tv3PoiCrawlTaskSty();
		tv3PoiCrawlTaskSty.setIntId(IdUtil.getLongUUID());
		tv3PoiCrawlTaskSty.setOrderNum(0);
		tv3PoiCrawlTaskSty.setSceneTypeId(Long.valueOf(subtype));

		Tv3PoiTypeRelation tv3PoiTypeRelation = new Tv3PoiTypeRelation();
		tv3PoiTypeRelation.setSceneTypeId(Long.valueOf(subtype));
		List<Tv3PoiTypeRelation> tv3PoiTypeRelations = tv3GisDictionaryMapper.select(tv3PoiTypeRelation);
		if (tv3PoiTypeRelations != null && !tv3PoiTypeRelations.isEmpty()) {
			tv3PoiCrawlTaskSty.setSceneTypeName(tv3PoiTypeRelations.get(0).getSceneTypeName());
		}

		tv3PoiCrawlTaskSty.setTaskId(taskId);
		tv3GisCrawlTaskStyMapper.insert(tv3PoiCrawlTaskSty);

		return ResponseMessage2.Success2(tv3GisCrawlTask);
	}

	/**
	 * 解析图层数据字段映射关系json串
	 * 
	 * @param fieldDescriptionInfos
	 * @return
	 */
	public static List<FieldDescriptionInfo> parseFieldDescriptionInfos(String fieldDescriptionInfos) {
		List<FieldDescriptionInfo> list = new ArrayList<>();

		if (fieldDescriptionInfos == null || "".equals(fieldDescriptionInfos)) {
			return list;
		}

		JsonParser jsonParser = new JsonParser();
		JsonArray jsonArray = jsonParser.parse(fieldDescriptionInfos).getAsJsonArray();
		for (JsonElement jsonElement : jsonArray) {
			FieldDescriptionInfo fieldDescriptionInfo = new FieldDescriptionInfo();
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			fieldDescriptionInfo.setFieldDes(jsonObject.get("fieldDes").getAsString());
			fieldDescriptionInfo.setFieldName(jsonObject.get("fieldName").getAsString());
			list.add(fieldDescriptionInfo);
		}

		return list;

	}

	/**
	 * 获取场景图层文件上传路径
	 * 
	 * @return
	 */
	private String getLayerFileUploadPath() {
		File file = new File("");
		String path = file.getAbsolutePath() + File.separator + "GeometryDataUpload" + File.separator;
		File excelFile = new File(path);
		if (!excelFile.exists()) {
			Boolean result = excelFile.mkdir();
			if (!result) {
				System.out.println(path + "目录无法创建！");
			}
		}
		return path;
	}

	/**
	 * 批量插入地理图元临时数据
	 * 
	 * @param tv3GisGeometryTemporaryQOs
	 * @param BatchCount
	 * @return
	 */
	@Override
	public Integer insertBatchGeometryTemporary(List<Tv3GisGeometryTemporary> gisGeometryTemporaries,
			Integer batchCount) {
		int result = 0;
		if (gisGeometryTemporaries == null || gisGeometryTemporaries.isEmpty()) {
			return result;
		}
		if (gisGeometryTemporaries.size() > batchCount) {
			int size = gisGeometryTemporaries.size() / batchCount + 1;
			for (int i = 0; i < size; i++) {
				result += crawlTaskManagerMapper
						.insertBatchGeometryTemporary(gisGeometryTemporaries.subList(i * batchCount,
								(i + 1) * batchCount > gisGeometryTemporaries.size() ? gisGeometryTemporaries.size()
										: (i + 1) * batchCount));
			}
		} else {
			result += crawlTaskManagerMapper.insertBatchGeometryTemporary(gisGeometryTemporaries);
		}
		return result;
	}

	@Override
	public ResponseMessage2<String> executeCrawlTask(HttpServletRequest request, Long intId) {
		Tv3PoiCrawlTask tv3GisCrawlTask = tv3GisCrawlTaskMapper.selectByPrimaryKey(intId);

		if (tv3GisCrawlTask.getTaskState() != 0) {
			return ResponseMessage2.Success("该任务未处于未执行状态！");
		} else {
			Tv3PoiCrawlTask updateTv3PoiCrawlTask = new Tv3PoiCrawlTask();
			updateTv3PoiCrawlTask.setIntId(tv3GisCrawlTask.getIntId());
			updateTv3PoiCrawlTask.setTaskState(-2);
			tv3GisCrawlTaskMapper.updateByPrimaryKeySelective(updateTv3PoiCrawlTask);
		}

		Tv3PoiCrawlTaskRegion tv3PoiCrawlTaskRegion = new Tv3PoiCrawlTaskRegion();
		tv3PoiCrawlTaskRegion.setTaskId(intId);
		tv3GisCrawlTask.setTv3GisCrawlTaskRegions(tv3GisCrawlTaskRegionMapper.select(tv3PoiCrawlTaskRegion));

		Tv3PoiCrawlTaskSty tv3PoiCrawlTaskSty = new Tv3PoiCrawlTaskSty();
		tv3PoiCrawlTaskSty.setTaskId(intId);
		tv3GisCrawlTask.setTv3GisCrawlTaskSties(tv3GisCrawlTaskStyMapper.select(tv3PoiCrawlTaskSty));

		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				CrawlTaskManagerService crawlTaskManagerService = SpringContextUtil
						.getBean(CrawlTaskManagerService.class);
				crawlTaskManagerService.executeNewCrawlTask(tv3GisCrawlTask);
			}
		});

		return ResponseMessage2.Success("该任务正在执行！");
	}

	/**
	 * 执行未执行的爬取任务
	 */
	public void executeNewCrawlTask(Tv3PoiCrawlTask tv3GisCrawlTask) {
		// 批次号
		int batchNum = IdUtil.getIntCRC32();

		if (tv3GisCrawlTask.getCrawlQueryNames() != null && !tv3GisCrawlTask.getCrawlQueryNames().isEmpty()) {

			CountDownLatch countDownLatch = new CountDownLatch(1);
			// 名称爬取
			ExecuteGeomentryNameCrawlTaskThread crawlTaskThread = new ExecuteGeomentryNameCrawlTaskThread(batchNum,
					countDownLatch, tv3GisCrawlTask);
			taskExecutor.execute(crawlTaskThread);
			try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// 行政区域爬取

			List<Tv3PoiCrawlTaskRegion> tv3GisCrawlTaskRegions = tv3GisCrawlTask.getTv3GisCrawlTaskRegions();
			List<Tv3PoiCrawlTaskSty> tv3GisCrawlTaskSties = tv3GisCrawlTask.getTv3GisCrawlTaskSties();
			// 处理场景类型信息
			List<List<Tv3PoiCrawlTaskSty>> tv3GisCrawlTaskStylists = new ArrayList<>();

			for (int i = 0; i < 1000; i++) {
				List<Tv3PoiCrawlTaskSty> gisCrawlTaskSties = new ArrayList<>();
				for (Tv3PoiCrawlTaskSty tv3GisCrawlTaskSty : tv3GisCrawlTaskSties) {
					if (tv3GisCrawlTaskSty.getOrderNum() == i) {
						gisCrawlTaskSties.add(tv3GisCrawlTaskSty);
					}
				}
				if (gisCrawlTaskSties.size() == 0) {
					break;
				}
				tv3GisCrawlTaskStylists.add(gisCrawlTaskSties);
			}

			for (Tv3PoiCrawlTaskRegion tv3GisCrawlTaskRegion : tv3GisCrawlTaskRegions) {

				// 根据地市名称查询行政区划轮廓
				Tv3GisGeometry regionTv3GisGeometry = new Tv3GisGeometry();
				regionTv3GisGeometry.setName(tv3GisCrawlTaskRegion.getRegionName());
				regionTv3GisGeometry.setGeometryType(3);// 行政区边界
				List<Tv3GisGeometry> regionTv3GisGeometries = tv3GisGeometryMapper.select(regionTv3GisGeometry);
				if (regionTv3GisGeometries != null && !regionTv3GisGeometries.isEmpty()) {
					regionTv3GisGeometry = regionTv3GisGeometries.get(0);
				}

				// 栅格
				List<MilitaryGrid> militaryGrids = getRegionMilitaryGrids(regionTv3GisGeometry);

				for (int i = 0; i < tv3GisCrawlTaskStylists.size(); i++) {

					List<Tv3PoiCrawlTaskSty> crawlTaskSties = tv3GisCrawlTaskStylists.get(i);
					String query = crawlTaskSties.get(0).getPoiTypeName();
					for (int i1 = 1; i1 < crawlTaskSties.size(); i1++) {
						query = query + "$" + crawlTaskSties.get(0).getPoiTypeName();
					}

					List<Tv3PoiTypeRelation> tv3GisDictionaries = new ArrayList<>();
					for (Tv3PoiCrawlTaskSty tv3GisCrawlTaskSty : crawlTaskSties) {
						Tv3PoiTypeRelation tv3GisDictionary = new Tv3PoiTypeRelation();
						tv3GisDictionary.setSceneTypeId(tv3GisCrawlTaskSty.getSceneTypeId());
						tv3GisDictionary.setSceneTypeName(tv3GisCrawlTaskSty.getSceneTypeName());
						tv3GisDictionary.setPoiTypeId(tv3GisCrawlTaskSty.getPoiTypeId());
						tv3GisDictionary.setPoiTypeName(tv3GisCrawlTaskSty.getPoiTypeName());
						tv3GisDictionaries.add(tv3GisDictionary);
					}

					/**
					 * 每个线程解析2000个栅格
					 */
					int threadSize = 1;// militaryGrids.size() / 2000 + 1;
					int count = militaryGrids.size() / threadSize;
					CountDownLatch countDownLatch = new CountDownLatch(threadSize);

					for (int i1 = 0; i1 < threadSize; i1++) {
						// 地理图元爬取任务
						ExecuteGeomentryCrawlTaskThread executeGeomentryCrawlTaskThread = new ExecuteGeomentryCrawlTaskThread(
								militaryGrids.subList(i1 * count,
										(i1 + 1) * count > militaryGrids.size() ? militaryGrids.size()
												: (i1 + 1) * count),
								batchNum, query, crawlTaskSties.get(0).getSceneTypeId(), countDownLatch,
								tv3GisCrawlTask, tv3GisCrawlTaskRegion.getOrderNum(),
								crawlTaskSties.get(0).getOrderNum(), i1 * count);
						taskExecutor.execute(executeGeomentryCrawlTaskThread);

					}

					// ExecuteGeomentryCrawlTaskThread
					// executeGeomentryCrawlTaskThread = new
					// ExecuteGeomentryCrawlTaskThread(
					// militaryGrids, batchNum, query,
					// crawlTaskSties.get(0).getSceneTypeId(), countDownLatch,
					// tv3GisCrawlTask, tv3GisCrawlTaskRegion.getOrderNum(),
					// crawlTaskSties.get(0).getOrderNum(),
					// militaryGrids.size());
					// taskExecutor.execute(executeGeomentryCrawlTaskThread);
					try {
						countDownLatch.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}

			}
		}

		if (tv3GisCrawlTask.getTaskDataHandleType() == 1) {
			// 新增爬取到的新数据信息
			crawlTaskManagerMapper.mergeNewGeometryDataByGeometryTemporary(batchNum);
		} else {
			// 更新爬取的全部数据信息
			crawlTaskManagerMapper.mergeAllGeometryDataByGeometryTemporary(batchNum);
		}
		// 删除临时表数据
		crawlTaskManagerMapper.deleteGisGeometryTemporaryByBatchNum(batchNum);

		// 更新任务状态
		Tv3PoiCrawlTask record = new Tv3PoiCrawlTask();
		record.setIntId(tv3GisCrawlTask.getIntId());
		record.setTaskState(2);
		record.setUpdateDate(new Date());
		tv3GisCrawlTaskMapper.updateByPrimaryKeySelective(record);

	}

	/**
	 * 获取地市栅格
	 * 
	 * @param tv3GisGeometry
	 * @return
	 */
	public List<MilitaryGrid> getRegionMilitaryGrids(Tv3GisGeometry tv3GisGeometry) {
		List<MilitaryGrid> militaryGrids = new ArrayList<>();
		if (tv3GisGeometry.getGeometryJson() == null || "".equals(tv3GisGeometry.getGeometryJson())) {
			return militaryGrids;
		}

		SceneCrawlingDataGeoObject sceneCrawlingDataGeoObject = GeojsonUtil
				.getSceneCrawlingDataGeoObject(tv3GisGeometry.getGeometryJson());

		List<List<List<Double[]>>> lists = sceneCrawlingDataGeoObject.getPolygons();
		for (List<List<Double[]>> list : lists) {
			if (list.size() <= 0) {
				continue;
			}
			List<EnginePoint> enginePoints = new ArrayList<>();
			// TODO 带孔洞
			for (Double[] doubles : list.get(0)) {
				EnginePoint enginePoint = new EnginePoint(doubles[0], doubles[1]);
				enginePoints.add(enginePoint);
			}
			List<MilitaryGrid> grids = MilitaryGridRepository.GetPolygonGrid(enginePoints, 800);
			militaryGrids.addAll(grids);
		}
		return militaryGrids;
	}
}
