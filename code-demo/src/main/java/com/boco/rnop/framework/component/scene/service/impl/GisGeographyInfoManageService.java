package com.boco.rnop.framework.component.scene.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.gdal.ogr.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.boco.com.framework.gis.engine.geometrys.EnginePoint;
import com.boco.com.framework.gis.engine.geometrys.EnginePolygon;
import com.boco.com.framework.gis.engine.geometrys.MilitaryGrid;
import com.boco.com.framework.gis.engine.models.EngineFeature;
import com.boco.com.framework.gis.engine.repositorys.LineExtendRepertory;
import com.boco.com.framework.gis.engine.repositorys.MilitaryGridRepository;
import com.boco.com.framework.gis.engine.utils.GdalTools;
import com.boco.rnop.framework.common.ResponseMessage2;
import com.boco.rnop.framework.common.csv.CSVWriter;
import com.boco.rnop.framework.common.util.IdUtil;
import com.boco.rnop.framework.component.scene.entity.FieldDescriptionInfo;
import com.boco.rnop.framework.component.scene.entity.GisGridExportCsvEntity;
import com.boco.rnop.framework.component.scene.entity.SceneCrawlingDataGeoObject;
import com.boco.rnop.framework.component.scene.entity.SelectGisGeographyInfoWebPar;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3GisGeometry;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3GisNumDictionary;
import com.boco.rnop.framework.component.scene.mapper.def.ITv3GisGeometryMapper;
import com.boco.rnop.framework.component.scene.mapper.def.ITv3GisNumDictionaryMapper;
import com.boco.rnop.framework.component.scene.reportform.ReportFormTemplate;
import com.boco.rnop.framework.component.scene.reportform.ReportFormTemplateXmlUtil;
import com.boco.rnop.framework.component.scene.service.IGisGeographyInfoManageService;
import com.boco.rnop.framework.component.scene.util.GeojsonUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 
 * Gis地理信息管理
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2019年1月14日 下午4:28:58
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@Slf4j
@Service
public class GisGeographyInfoManageService implements IGisGeographyInfoManageService {

	@Autowired
	private ITv3GisGeometryMapper tv3GisGeometryMapper;
	@Autowired
	private ITv3GisNumDictionaryMapper tv3GisNumDictionaryMapper;

	@Override
	public ResponseMessage2<PageInfo<Tv3GisGeometry>> selectGisGeographyInfo(SelectGisGeographyInfoWebPar webPar) {
		// 分页
		PageHelper.startPage(webPar.getPageNum(), webPar.getPageSize());
		// 查询地理图元信息
		Example example = new Example(Tv3GisGeometry.class);
		Criteria criteria = example.createCriteria();
		if (webPar.getGeometryType() != null) {
			criteria.andEqualTo("geometryType", webPar.getGeometryType());
		}
		if (webPar.getInfoType() != null && webPar.getInfoType() == 1) {
			// 信息点(改为经度，纬度)
			criteria.andIsNotNull("longitude");
			criteria.andIsNotNull("latitude");
		} else if (webPar.getInfoType() != null && webPar.getInfoType() == 2) {
			// 区域
			criteria.andIsNotNull("geometryJson");
		}
		if (webPar.getRegionIds() != null && webPar.getRegionIds().length > 0) {
			criteria.andIn("regionId", Arrays.asList(webPar.getRegionIds()));
		}
		if (webPar.getSceneTypeIds() != null && webPar.getSceneTypeIds().length > 0) {
			criteria.andIn("sceneTypeId", Arrays.asList(webPar.getSceneTypeIds()));
		}
		if (webPar.getName() != null && !"".equals(webPar.getName())) {
			criteria.andLike("name", "%" + webPar.getName() + "%");
		}

		return ResponseMessage2.Success2(new PageInfo<>(tv3GisGeometryMapper.selectByExample(example)));
	}

	@Override
	public ResponseMessage2<Integer> updateGeometryInfos(List<Tv3GisGeometry> tv3GisGeometries) {
		Integer result = 0;
		for (Tv3GisGeometry tv3GisGeometry : tv3GisGeometries) {
			result += tv3GisGeometryMapper.updateByPrimaryKey(tv3GisGeometry);
		}

		return ResponseMessage2.Success2(result);
	}

	@Override
	public void exportGeometryDataByLayerFile(String fieldDescriptionInfos, Long regionId, String regionName,
			Integer subtype, MultipartFile[] files, HttpServletResponse response) {
		// 文件名称集合
		Set<String> fileNames = new HashSet<>();

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
				log.warn("文件上传失败！");
			}
			allFiles.add(newFile);
			if (newFileName.toUpperCase().contains(".TAB") || newFileName.toUpperCase().contains(".SHP")) {
				fileNames.add(newFileName);
			}
		}

		// 解析文件
		HashMap<String, String> tabEntity = new HashMap<>();

		List<FieldDescriptionInfo> fieldDess = parseFieldDescriptionInfos(fieldDescriptionInfos);

		for (FieldDescriptionInfo fieldDescriptionInfo : fieldDess) {
			tabEntity.put(fieldDescriptionInfo.getFieldName(), fieldDescriptionInfo.getFieldDes());
		}

		ArrayList<EngineFeature> list = new ArrayList<>();

		for (String string : fileNames) {
			list.addAll(GdalTools.ReadVectorFile(string, tabEntity));
		}

		// 删除服务器端保存的上传文件
		for (File file : allFiles) {
			if (!file.delete()) {
				log.warn(file.getAbsolutePath() + "文件删除失败！");
			}
		}

		log.debug("图层文件解析出" + list.size() + "条数据！");

		List<Tv3GisGeometry> tv3GisGeometries = new ArrayList<>();
		for (EngineFeature engineFeature : list) {
			try {
				Geometry geometry = Geometry.CreateFromJson(engineFeature.getGeometryJson());

				Tv3GisGeometry tv3GisGeometry = new Tv3GisGeometry();
				tv3GisGeometry.setUuid(engineFeature.getBdUid());
				tv3GisGeometry.setLatitude(engineFeature.getLat());
				tv3GisGeometry.setLongitude(engineFeature.getLon());
				tv3GisGeometry.setCentriodLon(geometry.Centroid().GetX());
				tv3GisGeometry.setCentriodLat(geometry.Centroid().GetY());

				tv3GisGeometry.setCustomId(engineFeature.getCostomId());
				if (engineFeature.getGeometryArea() == null) {
					tv3GisGeometry.setGeometryArea(geometry.Area());
				} else {
					tv3GisGeometry.setGeometryArea(engineFeature.getGeometryArea());
				}
				tv3GisGeometry.setGeometryJson(engineFeature.getGeometryJson());
				if (engineFeature.getGeometryLength() == null) {
					tv3GisGeometry.setGeometryLength(geometry.Length());
				} else {
					tv3GisGeometry.setGeometryLength(engineFeature.getGeometryLength());
				}
				tv3GisGeometry.setName(engineFeature.getName());
				tv3GisGeometry.setRegionId(regionId != null ? regionId : engineFeature.getRegionId());
				tv3GisGeometry.setRegionName(regionName != null ? regionName : engineFeature.getRegionName());
				tv3GisGeometry.setSceneTypeId(subtype == null ? null : Long.valueOf(subtype));
				tv3GisGeometries.add(tv3GisGeometry);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		log.debug("生成" + tv3GisGeometries.size() + "条区域数据！");
		try {
			ReportFormTemplate reportFormTemplate = ReportFormTemplateXmlUtil
					.getReportFormTemplate("GisGeometryByLayerFile");
			CSVWriter.write(reportFormTemplate.getName() + ".csv", tv3GisGeometries,
					reportFormTemplate.getLinkedHashMap(), response);

		} catch (Exception e) {
			e.printStackTrace();
			log.warn("区域数据导出csv失败！");
			return;
		}
		// 当前时间
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

		response.setContentType("application/x-msdownload");
		response.setHeader("Content-Type", "application/octet-stream");
		response.setHeader("Content-disposition",
				"attachment;filename=" + "geometryData-" + simpleDateFormat.format(new Date()) + ".csv");

	}

	@Override
	public void exportGridDataByLayerFile(String fieldDescriptionInfos, Long regionId, String regionName,
			Integer subtype, Integer step, Integer extendWidth, Integer gridLength, MultipartFile[] files,
			HttpServletResponse response) {
		// 文件名称集合
		Set<String> fileNames = new HashSet<>();

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
				log.warn("文件上传失败！");
				return;
			}
			allFiles.add(newFile);
			if (newFileName.toUpperCase().contains(".TAB") || newFileName.toUpperCase().contains(".SHP")) {
				fileNames.add(newFileName);
			}
		}

		// 解析文件
		HashMap<String, String> tabEntity = new HashMap<>();

		List<FieldDescriptionInfo> fieldDess = parseFieldDescriptionInfos(fieldDescriptionInfos);

		for (FieldDescriptionInfo fieldDescriptionInfo : fieldDess) {
			tabEntity.put(fieldDescriptionInfo.getFieldName(), fieldDescriptionInfo.getFieldDes());
		}

		ArrayList<EngineFeature> engineFeatures = new ArrayList<>();

		for (String string : fileNames) {
			engineFeatures.addAll(GdalTools.ReadVectorFile(string, tabEntity));
		}
		// 删除服务器端保存的上传文件
		for (File file : allFiles) {
			if (!file.delete()) {
				log.warn(file.getAbsolutePath() + "文件删除失败！");
			}
		}
		log.debug("图层文件解析出" + engineFeatures.size() + "条数据！");

		// 导出栅格数据集合
		List<GisGridExportCsvEntity> gisGridExportCsvEntities = new ArrayList<>();

		for (EngineFeature engineFeature : engineFeatures) {
			String geometryJson = engineFeature.getGeometryJson();
			// TODO geojson专业解析工具
			SceneCrawlingDataGeoObject sceneCrawlingDataGeoObject = GeojsonUtil
					.getSceneCrawlingDataGeoObject(geometryJson);

			if (sceneCrawlingDataGeoObject == null) {
				log.debug(geometryJson + "无法解析！");
				return;
			}
			// 解析面点
			List<List<List<Double[]>>> lists = sceneCrawlingDataGeoObject.getPolygons();
			List<List<EnginePoint>> polygons = new ArrayList<>();
			for (List<List<Double[]>> list : lists) {
				List<EnginePoint> points = new ArrayList<>();
				if (list == null || list.isEmpty()) {
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

			// 面场景
			if ("Polygon".equals(sceneCrawlingDataGeoObject.getType())) {
				Long sceneId = IdUtil.getLongUUID();
				// 军事栅格数据集合
				List<MilitaryGrid> grids = new ArrayList<>();
				for (List<EnginePoint> enginePoints : polygons) {
					grids.addAll(MilitaryGridRepository.GetPolygonGrid(enginePoints, gridLength));
				}
				for (MilitaryGrid militaryGrid : grids) {
					GisGridExportCsvEntity gisGridExportCsvEntity = new GisGridExportCsvEntity();
					gisGridExportCsvEntity.setIntId(IdUtil.getLongUUID());
					gisGridExportCsvEntity.setGridId(militaryGrid.getId());
					gisGridExportCsvEntity.setGridLeftbottomLongitude(militaryGrid.getPoint_lb().getX());
					gisGridExportCsvEntity.setGridLeftbottomLatitude(militaryGrid.getPoint_lb().getY());
					gisGridExportCsvEntity.setGridLefttopLongitude(militaryGrid.getPoint_lu().getX());
					gisGridExportCsvEntity.setGridLefttopLatitude(militaryGrid.getPoint_lu().getY());
					gisGridExportCsvEntity.setGridRightbottomLongitude(militaryGrid.getPoint_rb().getX());
					gisGridExportCsvEntity.setGridRightbottomLatitude(militaryGrid.getPoint_rb().getY());
					gisGridExportCsvEntity.setGridRighttopLongitude(militaryGrid.getPoint_ru().getX());
					gisGridExportCsvEntity.setGridRighttopLatitude(militaryGrid.getPoint_ru().getY());
					gisGridExportCsvEntity.setInsertTime(new Date());
					gisGridExportCsvEntity.setRegionId(regionId);
					gisGridExportCsvEntity.setRegionName(regionName);
					gisGridExportCsvEntity.setSceneId(sceneId);
					gisGridExportCsvEntity.setSceneName(engineFeature.getName());
					gisGridExportCsvEntity.setGridBorderLength(gridLength);
					gisGridExportCsvEntities.add(gisGridExportCsvEntity);
				}
				// 线场景
			} else if ("LineString".equals(sceneCrawlingDataGeoObject.getType())) {

				for (List<EnginePoint> enginePoints : polygons) {
					Long sceneId = IdUtil.getLongUUID();
					LineExtendRepertory lineExtendRepertory = new LineExtendRepertory(enginePoints, step, extendWidth,
							true);
					List<EnginePolygon> enginePolygons = lineExtendRepertory.GenerateLineRects();
					for (int i = 0; i < enginePolygons.size(); i++) {
						EnginePolygon enginePolygon = enginePolygons.get(i);
						/** 路段名称(如合芜高速_1) */
						String dtgridName = engineFeature.getName() + "_" + (i + 1);
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

							List<MilitaryGrid> grids = MilitaryGridRepository.GetPolygonGrid(Arrays.asList(points),
									gridLength);
							for (MilitaryGrid militaryGrid : grids) {
								GisGridExportCsvEntity gisGridExportCsvEntity = new GisGridExportCsvEntity();
								gisGridExportCsvEntity.setIntId(IdUtil.getLongUUID());
								gisGridExportCsvEntity.setGridId(militaryGrid.getId());
								gisGridExportCsvEntity.setInsertTime(new Date());
								gisGridExportCsvEntity.setGridLeftbottomLongitude(militaryGrid.getPoint_lb().getX());
								gisGridExportCsvEntity.setGridLeftbottomLatitude(militaryGrid.getPoint_lb().getY());
								gisGridExportCsvEntity.setGridLefttopLongitude(militaryGrid.getPoint_lu().getX());
								gisGridExportCsvEntity.setGridLefttopLatitude(militaryGrid.getPoint_lu().getY());
								gisGridExportCsvEntity.setGridRightbottomLongitude(militaryGrid.getPoint_rb().getX());
								gisGridExportCsvEntity.setGridRightbottomLatitude(militaryGrid.getPoint_rb().getY());
								gisGridExportCsvEntity.setGridRighttopLongitude(militaryGrid.getPoint_ru().getX());
								gisGridExportCsvEntity.setGridRighttopLatitude(militaryGrid.getPoint_ru().getY());
								gisGridExportCsvEntity.setSceneId(sceneId);
								gisGridExportCsvEntity.setSceneName(engineFeature.getName());
								gisGridExportCsvEntity.setGridBorderLength(gridLength);
								gisGridExportCsvEntity.setRegionId(regionId);
								gisGridExportCsvEntity.setRegionName(regionName);
								gisGridExportCsvEntity.setDtgridId(dtgridId);
								// 路段信息
								/** 路段左下纬度 */
								gisGridExportCsvEntity.setDtgridLeftbottomLatitude(dtgridLeftbottomLatitude);
								/** 路段左上经度 */
								gisGridExportCsvEntity.setDtgridLefttopLongitude(dtgridLefttopLongitude);
								/** 路段左上纬度 */
								gisGridExportCsvEntity.setDtgridLefttopLatitude(dtgridLefttopLatitude);
								/** 路段右上经度 */
								gisGridExportCsvEntity.setDtgridRighttopLongitude(dtgridRighttopLongitude);
								/** 路段左下经度 */
								gisGridExportCsvEntity.setDtgridLeftbottomLongitude(dtgridLeftbottomLongitude);
								/** 路段唯一标识ID */
								gisGridExportCsvEntity.setIntId(dtgridId);
								/** 路段右上纬度 */
								gisGridExportCsvEntity.setDtgridRighttopLatitude(dtgridRighttopLatitude);
								/** 路段右下经度 */
								gisGridExportCsvEntity.setDtgridRightbottomLongitude(dtgridRightbottomLongitude);
								/** 路段右下纬度 */
								gisGridExportCsvEntity.setDtgridRightbottomLatitude(dtgridRightbottomLatitude);
								/** 路段名称(如合芜高速_1) */
								gisGridExportCsvEntity.setDtgridName(dtgridName);
								// 路段步长
								gisGridExportCsvEntity.setStep(step);
								// 路段扩充宽度
								gisGridExportCsvEntity.setExtendWidth(extendWidth);
								// 路段序号
								gisGridExportCsvEntity.setOrderNum(i + 1);

								gisGridExportCsvEntities.add(gisGridExportCsvEntity);
							}

						}

					}
				}

			}

		}
		log.debug("生成" + gisGridExportCsvEntities.size() + "条栅格数据！");
		try {
			ReportFormTemplate reportFormTemplate = ReportFormTemplateXmlUtil
					.getReportFormTemplate("GisGridDetailsByLayerFile");
			CSVWriter.write(reportFormTemplate.getName() + ".csv", gisGridExportCsvEntities,
					reportFormTemplate.getLinkedHashMap(), response);
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("栅格数据导出csv失败！");
			return;
		}
		// 当前时间
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

		response.setContentType("application/x-msdownload");
		response.setHeader("Content-Type", "application/octet-stream");
		response.setHeader("Content-disposition",
				"attachment;filename=" + "gridData-" + simpleDateFormat.format(new Date()) + ".csv");

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
				log.warn(path + "目录无法创建！");
			}
		}
		return path;
	}

	@Override
	public ResponseMessage2<List<Tv3GisNumDictionary>> selectGeometryType() {
		Tv3GisNumDictionary record = new Tv3GisNumDictionary();
		record.setType("tv3_gis_geometry.geometry_type");
		return ResponseMessage2.Success2(tv3GisNumDictionaryMapper.select(record));
	}

	@Override
	public ResponseMessage2<Integer> deleteGeometryInfos(List<Long> intIds) {
		Integer result = 0;
		Example example = new Example(Tv3GisGeometry.class);
		example.createCriteria().andIn("intId", intIds);
		result = tv3GisGeometryMapper.deleteByExample(example);
		return ResponseMessage2.Success2(result);
	}

}
