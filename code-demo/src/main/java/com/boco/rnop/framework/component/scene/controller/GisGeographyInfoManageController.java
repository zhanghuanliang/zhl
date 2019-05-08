package com.boco.rnop.framework.component.scene.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.boco.rnop.framework.common.ResponseMessage2;
import com.boco.rnop.framework.component.scene.entity.AnalysisGeometryResultInfo;
import com.boco.rnop.framework.component.scene.entity.SelectGisGeographyInfoWebPar;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3GisGeometry;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3GisNumDictionary;
import com.boco.rnop.framework.component.scene.service.ICrawlTaskManagerService;
import com.boco.rnop.framework.component.scene.service.IGisGeographyInfoManageService;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * 场景管理-Gis地理信息管理
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年9月12日 下午2:38:46
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@RestController
@RequestMapping("/SceneGisGeographyInfoManage")
@Api(tags = "场景管理-Gis地理信息管理")
public class GisGeographyInfoManageController {

	@Autowired
	private IGisGeographyInfoManageService gisGeographyInfoManageService;

	@Autowired
	private ICrawlTaskManagerService crawlTaskManagerService;

	@ApiOperation(value = "查询Gis地理信息", notes = "查询Gis地理信息")
	@PostMapping(value = "/selectGisGeographyInfo")
	public ResponseMessage2<PageInfo<Tv3GisGeometry>> selectGisGeographyInfo(
			@RequestBody SelectGisGeographyInfoWebPar webPar) {
		return gisGeographyInfoManageService.selectGisGeographyInfo(webPar);
	}

	@ApiOperation(value = "更新地理图元信息", notes = "更新地理图元信息")
	@PostMapping(value = "updateGeometryInfos")
	public ResponseMessage2<Integer> updateGeometryInfos(@RequestBody List<Tv3GisGeometry> tv3GisGeometries) {
		return gisGeographyInfoManageService.updateGeometryInfos(tv3GisGeometries);
	}

	@ApiOperation(value = "删除地理图元信息", notes = "删除地理图元信息")
	@PostMapping(value = "deleteGeometryInfos")
	public ResponseMessage2<Integer> deleteGeometryInfos(@RequestBody List<Long> intIds) {
		return gisGeographyInfoManageService.deleteGeometryInfos(intIds);
	}

	/**
	 * 解析图层文件场景数据
	 * 
	 * @param files
	 * @return
	 */
	@ApiOperation(value = "解析图层文件数据", notes = "解析图层文件数据")
	@PostMapping(value = "/analysisLayerData")
	public ResponseMessage2<AnalysisGeometryResultInfo> analysisLayerData(MultipartFile[] files) {
		return crawlTaskManagerService.analysisLayerData(files);
	}

	@ApiOperation(value = "查询Geometry类型", notes = "查询Geometry类型")
	@GetMapping(value = "/selectGeometryType")
	public ResponseMessage2<List<Tv3GisNumDictionary>> selectGeometryType() {
		return gisGeographyInfoManageService.selectGeometryType();
	}

	@ApiOperation(value = "根据图层文件导出区域数据信息csv", notes = "根据图层文件导出区域数据信息csv")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "fieldDescriptionInfos", value = "图层数据字段映射关系json串", dataType = "String", required = false, paramType = "query"),
			@ApiImplicitParam(name = "regionId", value = "地市编号", required = false, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "regionName", value = "地市名称", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "subtype", value = "图元亚类型", required = false, dataType = "Integer", paramType = "query") })
	@PostMapping(value = "/exportGeometryDataByLayerFile")
	public void exportGeometryDataByLayerFile(String fieldDescriptionInfos, Long regionId, String regionName,
			Integer subtype, MultipartFile[] files, HttpServletResponse response) {
		gisGeographyInfoManageService.exportGeometryDataByLayerFile(fieldDescriptionInfos, regionId, regionName,
				subtype, files, response);
	}

	@ApiOperation(value = "根据图层文件导出栅格数据信息csv", notes = "根据图层文件导出区域数据信息csv")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "fieldDescriptionInfos", value = "图层数据字段映射关系json串", dataType = "String", required = false, paramType = "query"),
			@ApiImplicitParam(name = "regionId", value = "地市编号", required = false, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "regionName", value = "地市名称", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "subtype", value = "网优场景类型", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "step", value = "线场景步长", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "extendWidth", value = "线场景扩充宽度", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "gridLength", value = "栅格边长", required = true, dataType = "Long", paramType = "query") })
	@PostMapping(value = "/exportGridDataByLayerFile")
	public void exportGridDataByLayerFile(String fieldDescriptionInfos, Long regionId, String regionName,
			Integer subtype, Integer step, Integer extendWidth, Integer gridLength, MultipartFile[] files,
			HttpServletResponse response) {
		gisGeographyInfoManageService.exportGridDataByLayerFile(fieldDescriptionInfos, regionId, regionName, subtype,
				step, extendWidth, gridLength, files, response);
	}

}
