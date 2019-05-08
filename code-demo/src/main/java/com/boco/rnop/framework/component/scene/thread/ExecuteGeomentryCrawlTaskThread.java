package com.boco.rnop.framework.component.scene.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.gdal.ogr.Geometry;

import com.boco.com.framework.gis.engine.EngineTransCoord;
import com.boco.com.framework.gis.engine.geometrys.EnginePoint;
import com.boco.com.framework.gis.engine.geometrys.MilitaryGrid;
import com.boco.rnop.framework.common.util.IdUtil;
import com.boco.rnop.framework.common.util.JsonUtil;
import com.boco.rnop.framework.common.util.SpringContextUtil;
import com.boco.rnop.framework.component.scene.entity.SceneCrawlingDataSingleGeoObject;
import com.boco.rnop.framework.component.scene.entity.baidu.api.BaiduGeoconvApiCoordinate;
import com.boco.rnop.framework.component.scene.entity.baidu.api.BaiduGeoconvApiResponseMessage;
import com.boco.rnop.framework.component.scene.entity.baidu.api.BaiduPlaceSearchApiResponseMessage;
import com.boco.rnop.framework.component.scene.entity.baidu.api.BaiduPlaceSearchApiResult;
import com.boco.rnop.framework.component.scene.entity.baidu.api.BaiduSceneGeoApiResponseMessage;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3GisGeometryTemporary;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3PoiCrawlTask;
import com.boco.rnop.framework.component.scene.exception.BaiduApiHttpFalseException;
import com.boco.rnop.framework.component.scene.exception.NoAvailableBaiduAkException;
import com.boco.rnop.framework.component.scene.manager.BaiduApiManager;
import com.boco.rnop.framework.component.scene.manager.BaiduSourseManager;
import com.boco.rnop.framework.component.scene.mapper.def.ICrawlTaskManagerMapper;
import com.boco.rnop.framework.component.scene.mapper.def.ITv3PoiCrawlTaskMapper;
import com.boco.rnop.framework.component.scene.service.ICrawlTaskManagerService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 执行地理图元爬取任务线程
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2019年1月7日 下午3:44:59
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@Slf4j
public class ExecuteGeomentryCrawlTaskThread implements Runnable {
	private BaiduApiManager baiduApiManager = SpringContextUtil.getBean(BaiduApiManager.class);
	private ITv3PoiCrawlTaskMapper tv3GisCrawlTaskMapper = SpringContextUtil.getBean(ITv3PoiCrawlTaskMapper.class);
	private ICrawlTaskManagerMapper crawlTaskManagerMapper = SpringContextUtil.getBean(ICrawlTaskManagerMapper.class);
	private ICrawlTaskManagerService crawlTaskManagerService = SpringContextUtil
			.getBean(ICrawlTaskManagerService.class);

	/**
	 * 军事栅格
	 */
	private List<MilitaryGrid> militaryGrids;
	/**
	 * 军事栅格起始索引
	 */
	private Integer militaryGridStartIndex;
	/**
	 * 批次编号
	 */
	private Integer batchNum;
	/**
	 * 爬取任务
	 */
	private Tv3PoiCrawlTask tv3GisCrawlTask;
	/**
	 * 地市序号
	 */
	private Integer regionOrderNum;
	/**
	 * 类型序号
	 */
	private Integer subtypeOrderNum;
	/**
	 * 百度API 检索关键字
	 */
	private String query;
	/**
	 * 场景类型编号
	 */
	private Long geometryType;
	/**
	 * 线程控制
	 */
	private CountDownLatch countDownLatch;

	public ExecuteGeomentryCrawlTaskThread(List<MilitaryGrid> militaryGrids, Integer batchNum, String query,
			Long geometryType, CountDownLatch countDownLatch, Tv3PoiCrawlTask tv3GisCrawlTask, Integer regionOrderNum,
			Integer subtypeOrderNum, Integer militaryGridStartIndex) {
		super();
		this.militaryGrids = militaryGrids;
		this.batchNum = batchNum;
		this.query = query;
		this.geometryType = geometryType;
		this.countDownLatch = countDownLatch;
		this.tv3GisCrawlTask = tv3GisCrawlTask;
		this.regionOrderNum = regionOrderNum;
		this.subtypeOrderNum = subtypeOrderNum;
		this.militaryGridStartIndex = militaryGridStartIndex;
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < militaryGrids.size(); i++) {
				// 任务id
				Long taskId = tv3GisCrawlTask.getIntId();

				MilitaryGrid militaryGrid = militaryGrids.get(i);
				EnginePoint lbPoint = EngineTransCoord.WGS84ToBD(militaryGrid.getPoint_lb());
				EnginePoint ruPoint = EngineTransCoord.WGS84ToBD(militaryGrid.getPoint_ru());
				String bounds = lbPoint.getY() + "," + lbPoint.getX() + "," + ruPoint.getY() + "," + ruPoint.getX();
				List<BaiduPlaceSearchApiResponseMessage> baiduPlaceSearchApiResponseMessages = new ArrayList<>();
				try {
					baiduPlaceSearchApiResponseMessages = baiduApiManager
							.getBaiduRectanglePlaceSearchApiResponseMessage(query, bounds);
				} catch (BaiduApiHttpFalseException e1) {
					e1.printStackTrace();
					continue;
				} catch (NoAvailableBaiduAkException e1) {
					e1.printStackTrace();
					// 无法获取可用百度AK，任务中断（记录大概中断信息）
					Tv3PoiCrawlTask tv3GisCrawlTask = tv3GisCrawlTaskMapper.selectByPrimaryKey(taskId);
					if (tv3GisCrawlTask.getTaskState() != -1) {
						Tv3PoiCrawlTask record = new Tv3PoiCrawlTask();
						record.setIntId(taskId);
						record.setTaskState(-1);
						record.setBreakRegionOrder(regionOrderNum);
						record.setBreakSubtypeOrder(subtypeOrderNum);
						record.setBreakGridOrder(militaryGridStartIndex + i);
						record.setBreakBatchNum(batchNum);
						tv3GisCrawlTaskMapper.updateByPrimaryKeySelective(record);
						break;
					}
					continue;
				}
				for (BaiduPlaceSearchApiResponseMessage searchApiResponseMessage : baiduPlaceSearchApiResponseMessages) {

					List<BaiduPlaceSearchApiResult> baiduPlaceSearchApiResults = searchApiResponseMessage.getResults();

					// 场景信息集合
					List<Tv3GisGeometryTemporary> tv3GisGeometryTemporaries = new ArrayList<>();
					for (BaiduPlaceSearchApiResult baiduPlaceSearchApiResult : baiduPlaceSearchApiResults) {
						if (baiduPlaceSearchApiResult.getUid() != null) {
							// 调用场景服务
							BaiduSceneGeoApiResponseMessage baiduSceneGeoApiResponseMessage;
							try {
								baiduSceneGeoApiResponseMessage = baiduApiManager
										.getBaiduSceneGeoApiResponseMessage(baiduPlaceSearchApiResult.getUid());
							} catch (BaiduApiHttpFalseException e) {
								e.printStackTrace();
								continue;
							}

							// 场景geojson对象
							SceneCrawlingDataSingleGeoObject sceneCrawlingDataSingleGeoObject = new SceneCrawlingDataSingleGeoObject();
							sceneCrawlingDataSingleGeoObject.setType("Polygon");
							List<List<Double[]>> coordinates = new ArrayList<>();
							List<Double[]> doubles = new ArrayList<>();
							if (baiduSceneGeoApiResponseMessage != null) {
								// 调用百度坐标转换服务
								List<String> list = baiduSceneGeoApiResponseMessage.getGoeCoordinates();

								if (list.size() > 0) {
									for (String string : list) {
										BaiduGeoconvApiResponseMessage baiduGeoconvApiResponseMessage = null;
										try {
											baiduGeoconvApiResponseMessage = baiduApiManager
													.getBaiduGeoconvApiResponseMessage(string, "6", "5");
										} catch (BaiduApiHttpFalseException e) {
											e.printStackTrace();
											continue;
										} catch (NoAvailableBaiduAkException e) {
											e.printStackTrace();
											Tv3PoiCrawlTask tv3GisCrawlTask = tv3GisCrawlTaskMapper
													.selectByPrimaryKey(taskId);
											if (tv3GisCrawlTask.getTaskState() != -1) {
												Tv3PoiCrawlTask record = new Tv3PoiCrawlTask();
												record.setIntId(taskId);
												record.setTaskState(-1);
												record.setBreakRegionOrder(regionOrderNum);
												record.setBreakSubtypeOrder(subtypeOrderNum);
												record.setBreakGridOrder(militaryGridStartIndex + i);
												record.setBreakBatchNum(batchNum);
												tv3GisCrawlTaskMapper.updateByPrimaryKeySelective(record);
												break;
											}
											continue;
										}
										List<BaiduGeoconvApiCoordinate> baiduGeoconvApiCoordinates = baiduGeoconvApiResponseMessage
												.getResult();
										// 坐标转换
										for (BaiduGeoconvApiCoordinate baiduGeoconvApiCoordinate : baiduGeoconvApiCoordinates) {
											EnginePoint enginePoint = EngineTransCoord
													.BDToWGS84(new EnginePoint(baiduGeoconvApiCoordinate.getX(),
															baiduGeoconvApiCoordinate.getY()));
											doubles.add(new Double[] { enginePoint.getX(), enginePoint.getY() });
										}
									}
								}
							}

							/**
							 * 场景临时表数据
							 */
							Tv3GisGeometryTemporary tv3GisGeometryTemporary = new Tv3GisGeometryTemporary();
							tv3GisGeometryTemporary.setBatchNum(batchNum);
							tv3GisGeometryTemporary.setUuid(baiduPlaceSearchApiResult.getUid());
							EnginePoint poi = EngineTransCoord
									.BDToWGS84(new EnginePoint(baiduPlaceSearchApiResult.getLocation().getLng(),
											baiduPlaceSearchApiResult.getLocation().getLat()));
							tv3GisGeometryTemporary.setLatitude(poi.getY());
							tv3GisGeometryTemporary.setLongitude(poi.getX());
							tv3GisGeometryTemporary.setCityName(baiduPlaceSearchApiResult.getArea());

							if (!doubles.isEmpty()) {
								coordinates.add(doubles);
								sceneCrawlingDataSingleGeoObject.setCoordinates(coordinates);
								tv3GisGeometryTemporary
										.setGeometryJson(JsonUtil.toJson(sceneCrawlingDataSingleGeoObject));
								// 计算
								try {
									Geometry geometry = Geometry
											.CreateFromJson(JsonUtil.toJson(sceneCrawlingDataSingleGeoObject));

									tv3GisGeometryTemporary.setCentriodLat(geometry.Centroid().GetY());
									tv3GisGeometryTemporary.setCentriodLon(geometry.Centroid().GetX());
									tv3GisGeometryTemporary.setGeometryLength(geometry.Length());
									tv3GisGeometryTemporary.setGeometryArea(geometry.Area());
									tv3GisGeometryTemporary.setGeojsonType(sceneCrawlingDataSingleGeoObject.getType());

								} catch (Exception e) {
									e.printStackTrace();
								}

							}

							tv3GisGeometryTemporary.setInsertTime(new Date());
							tv3GisGeometryTemporary.setIntId(Long.valueOf(IdUtil.getIntCRC32()));
							tv3GisGeometryTemporary.setAcquireWayId(2);
							tv3GisGeometryTemporary.setAcquireWayName("爬取");
							tv3GisGeometryTemporary.setGeometryType(1);
							tv3GisGeometryTemporary.setGeometryTypeName("场景");
							tv3GisGeometryTemporary.setName(baiduPlaceSearchApiResult.getName());
							tv3GisGeometryTemporary.setRegionName(baiduPlaceSearchApiResult.getCity());
							tv3GisGeometryTemporary.setSceneTypeId(geometryType);
							tv3GisGeometryTemporary.setTaskId(taskId);
							tv3GisGeometryTemporary.setCreateUserid(tv3GisCrawlTask.getCreateUserid());
							tv3GisGeometryTemporary.setCreateUsername(tv3GisCrawlTask.getCreateUsername());
							tv3GisGeometryTemporary.setAddress(baiduPlaceSearchApiResult.getAddress());
							tv3GisGeometryTemporary.setGeojsonType(sceneCrawlingDataSingleGeoObject.getType());
							tv3GisGeometryTemporary.setPoiTypeName(baiduPlaceSearchApiResult.getDetail_info().getTag());

							tv3GisGeometryTemporaries.add(tv3GisGeometryTemporary);
						} else {
							continue;
						}
					}
					// 批量插入场景临时数据
					crawlTaskManagerService.insertBatchGeometryTemporary(tv3GisGeometryTemporaries, 20);
					crawlTaskManagerMapper.updateCrawlTaskEndGridNum(militaryGrids.size(), taskId);

				}

			}
		} finally {
			BaiduSourseManager.updateAkInfo();
			countDownLatch.countDown();
		}
	}

}
