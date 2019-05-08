package com.boco.rnop.framework.component.scene.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.boco.rnop.framework.common.ResponseMessage2;
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
import com.boco.rnop.framework.component.scene.entity.tk.Tv3SceneInfo;
import com.boco.rnop.framework.component.scene.service.ISceneManageService;
import com.boco.rnop.framework.log.entity.annotation.SystemHandleLogInfo;
import com.boco.rnop.framework.log.entity.annotation.SystemHandleLogInfo.DatabaseAccessType;
import com.boco.rnop.framework.log.entity.annotation.SystemHandleLogInfo.LogLevel;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 场景管理
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月14日 上午9:25:32
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@RestController
@Api(tags = "场景管理")
@RequestMapping("/SceneManage")
@Slf4j
public class SceneManageController {

	@Autowired
	private ISceneManageService sceneManageService;

	@ApiOperation(value = "根据场景id查询场景详细信息", notes = "根据场景id查询场景详细信息")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "intId", value = "场景id", required = true, dataType = "Long", paramType = "query") })
	@GetMapping(value = "/selectSceneDetailedInfoById")
	public ResponseMessage2<SceneDetailedInfoPO> selectSceneDetailedInfoById(Long intId) {
		return ResponseMessage2.Success2(sceneManageService.selectSceneDetailedInfoById(intId));
	}

	@ApiOperation(value = "删除场景数据", notes = "删除场景数据(包括该场景下的所有子场景、所有相关场景的关联小区、所有相关场景的栅格数据)")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "intId", value = "场景Id", required = true, dataType = "Long", paramType = "query") })
	@GetMapping(value = "/deleteSceneData")
	public ResponseMessage2<Integer> deleteSceneData(Long intId) {
		return ResponseMessage2.Success2(sceneManageService.deleteSceneData(intId));
	}

	@ApiOperation(value = "查询地理区域场景数据", notes = "查询地理区域场景数据")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "subtype", value = "地理区域类型", required = false, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "regionId", value = "地市ID", required = false, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "name", value = "地理区域名称（模糊搜索）", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "pageNum", value = "分页页数", required = true, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "pageSize", value = "分页每页数量", required = true, dataType = "Long", paramType = "query") })
	@GetMapping(value = "/selectGeometryData")
	public ResponseMessage2<PageInfo<Tv3GisGeometry>> selectGeometryData(Integer subtype, Long regionId, String name,
			Integer pageNum, Integer pageSize) {
		return ResponseMessage2
				.Success2(sceneManageService.selectGeometryData(subtype, regionId, name, pageNum, pageSize));
	}

	@ApiOperation(value = "修改场景数据", notes = "修改场景数据")
	@PostMapping(value = "/alterSceneData")
	public ResponseMessage2<Integer> alterSceneData(HttpServletRequest request,
			@RequestBody Tv3SceneInfo tv3SceneInfo) {

		return sceneManageService.alterSceneData(request, tv3SceneInfo);
	}

	@ApiOperation(value = "新增场景数据", notes = "新增场景数据")
	@PostMapping(value = "/insertSceneData")
	public ResponseMessage2<Tv3SceneInfo> insertSceneData(HttpServletRequest request,
			@RequestBody Tv3SceneInfo tv3SceneInfo) {

		return sceneManageService.insertSceneData(request, tv3SceneInfo);
	}

	@ApiOperation(value = "批量新增场景小区数据(界面点选新增)", notes = "批量新增场景数据(界面点选新增)")
	@PostMapping(value = "/insertSceneDatas")
	public ResponseMessage2<List<SceneCellInsertInfo>> insertSceneDatas(HttpServletRequest request,
			@RequestBody List<SceneCellInsertInfo> sceneCellInsertInfos) {

		return sceneManageService.insertSceneDatas(request, sceneCellInsertInfos);
	}

	@ApiOperation(value = "批量新增场景小区数据（前置场景excel上传接口）", notes = "批量新增场景小区数据（前置场景excel上传接口）")
	@PostMapping(value = "/insertSceneAndCellDatas")
	public ResponseMessage2<List<Tv3SceneExcelWebInfo>> insertSceneAndCellDatas(HttpServletRequest request,
			@RequestBody List<Tv3SceneExcelWebInfo> tv3SceneExcelWebInfos) {

		return sceneManageService.insertSceneAndCellDatas(request, tv3SceneExcelWebInfos);
	}

	@ApiOperation(value = "根据场景id查询关联小区详情数据", notes = "根据场景id查询关联小区详情数据")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "intId", value = "场景id", required = true, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "pageNum", value = "分页页数", required = true, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "pageSize", value = "分页每页数量", required = true, dataType = "Long", paramType = "query") })
	@GetMapping(value = "/selectCellRelateBySceneId")
	public ResponseMessage2<PageInfo<SceneRelateCellDetaildInfoPO>> selectCellRelateBySceneId(Long intId,
			Integer pageNum, Integer pageSize) {

		return sceneManageService.selectCellRelateBySceneId(intId, pageNum, pageSize);

	}

	@ApiOperation(value = "根据场景id查询关联小区缺省数据", notes = "根据场景id查询关联小区缺省数据")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "intId", value = "场景id", required = false, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "pageNum", value = "分页页数", required = true, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "pageSize", value = "分页每页数量", required = true, dataType = "Long", paramType = "query") })
	@GetMapping(value = "/selectDefaultCellRelateBySceneId")
	public ResponseMessage2<PageInfo<Tv3SceneCell>> selectDefaultCellRelateBySceneId(Long intId, Integer pageNum,
			Integer pageSize) {

		return sceneManageService.selectDefaultCellRelateBySceneId(intId, pageNum, pageSize);

	}

	@ApiOperation(value = "删除关联小区数据", notes = "删除关联小区数据")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "cellId", value = "小区id", required = true, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "sceneId", value = "场景id", required = true, dataType = "Long", paramType = "query") })
	@GetMapping(value = "/deleteCellRelateByIds")
	public ResponseMessage2<Integer> deleteCellRelateByIds(Long cellId, Long sceneId) {

		return ResponseMessage2.Success2(sceneManageService.deleteCellRelateByIds(cellId, sceneId));

	}

	@ApiOperation(value = "新增关联小区数据", notes = "新增关联小区数据")
	@PostMapping(value = "/insertCellRelateData")
	public ResponseMessage2<Integer> insertCellRelateData(@RequestBody List<Tv3SceneCell> tv3GisCellrelateGeos) {

		return ResponseMessage2.Success2(sceneManageService.insertCellRelateData(tv3GisCellrelateGeos));
	}

	@ApiOperation(value = "根据场景id计算场景关联小区数据(前端分页)", notes = "根据场景id计算场景关联小区数据(前端分页)")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "sceneId", value = "场景id", required = true, dataType = "Long", paramType = "query") })
	@GetMapping(value = "/executeCellRelateDataBySceneId")
	public ResponseMessage2<List<UploadCellWebResult>> executeCellRelateDataBySceneId(Long sceneId) {

		return ResponseMessage2.Success2(sceneManageService.executeCellRelateData(sceneId));
	}

	@ApiOperation(value = "根据场景Geojson计算场景关联小区数据(前端分页)", notes = "根据场景Geojson计算场景关联小区数据(前端分页)")
	@PostMapping(value = "/executeCellRelateDataByGeojson")
	public ResponseMessage2<List<UploadCellWebResult>> executeCellRelateDataByGeojson(
			@RequestBody ExecuteCellRelateDataByGeojsonWebParam webParam) {

		return ResponseMessage2.Success2(sceneManageService.executeCellRelateData(webParam));
	}

	@ApiOperation(value = "上传关联小区数据文件(制式、cgi)", notes = "上传关联小区数据文件")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "sceneId", value = "场景id", required = false, dataType = "Long", paramType = "query") })
	@PostMapping(value = "/uploadCellRelateDataFile")
	public ResponseMessage2<List<UploadCellWebResult>> uploadCellRelateDataFile(Long sceneId, MultipartFile file) {

		return ResponseMessage2.Success2(sceneManageService.uploadCellRelateDataFile(sceneId, file));
	}

	@ApiOperation(value = "生成场景栅格数据", notes = "生成场景栅格数据")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "sceneId", value = "场景id", required = true, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "step", value = "线场景步长", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "extendWidth", value = "线场景扩充宽度", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "gridLength", value = "栅格边长", required = true, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "gisEngineCentralLongtitude", value = "gis工具包 所在省份 中心经度", required = false, dataType = "Double", paramType = "query"),
			@ApiImplicitParam(name = "isDeleteAll", value = "是否删除该场景全部栅格数据", required = true, dataType = "Long", paramType = "query") })
	@GetMapping(value = "/buildGridData")
	public ResponseMessage2<SceneBuildGridWebInfo> buildGridData(Long sceneId, Integer step, Integer extendWidth,
			Integer gridLength, Integer isDeleteAll, Double gisEngineCentralLongtitude) {

		return ResponseMessage2.Success2(sceneManageService.buildGridData(sceneId, step, extendWidth, gridLength,
				isDeleteAll, gisEngineCentralLongtitude));
	}

	@ApiOperation(value = "根据父场景编号查询场景基本信息,返回element UI 选择器数据格式", notes = "根据父场景编号查询场景基本信息,返回element UI 选择器数据格式")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "parentId", value = "父场景编号", required = true, dataType = "String", paramType = "query") })
	@GetMapping(value = "/selectSceneBaseInfoByParentId")
	public ResponseMessage2<List<SceneBaseInfoPO>> selectSceneBaseInfoByParentId(String parentId) {

		return sceneManageService.selectSceneBaseInfoByParentId(parentId);

	}

	@ApiOperation(value = "统计该场景id下场景名称相同的数量", notes = "统计该场景id下场景名称相同的数量")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "sceneName", value = "场景名称", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "parentId", value = "父场景id", required = true, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "technology", value = "制式（GSM LTE NB-IOT）", required = true, dataType = "Long", paramType = "query") })
	@GetMapping(value = "/countSameSceneName")
	public ResponseMessage2<Integer> countSameSceneName(String sceneName, Long parentId, String technology) {
		return ResponseMessage2.Success2(sceneManageService.countSameSceneName(sceneName, parentId, technology));

	}

	@ApiOperation(value = "查询统计场景栅格信息", notes = "查询统计场景栅格信息")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "sceneId", value = "场景Id", required = true, dataType = "String", paramType = "query") })
	@GetMapping(value = "/countGridInfoBySceneId")
	public ResponseMessage2<List<SceneGridCountInfoPO>> countGridInfoBySceneId(Long sceneId) {
		return ResponseMessage2.Success2(sceneManageService.countGridInfoBySceneId(sceneId));
	}

	@ApiOperation(value = "删除场景栅格信息", notes = "删除场景栅格信息")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "sceneId", value = "场景Id", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "step", value = "路段步长", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "extendWidth", value = "路段扩充宽度", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "gridBorderLength", value = "栅格边长", required = true, dataType = "String", paramType = "query") })
	@GetMapping(value = "/deleteGridDataBySceneId")
	public ResponseMessage2<Integer> deleteGridDataBySceneId(Long sceneId, Integer step, Integer extendWidth,
			Integer gridBorderLength) {
		return ResponseMessage2
				.Success2(sceneManageService.deleteGridDataBySceneId(sceneId, step, extendWidth, gridBorderLength));
	}

	@ApiOperation(value = "上传场景图片", notes = "上传场景图片")
	@PostMapping(value = "/uploadSceneImage")
	public ResponseMessage2<String> uploadSceneImage(MultipartFile file) {
		try {
			return ResponseMessage2.Success2(sceneManageService.uploadSceneImage(file));
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseMessage2.Failed("场景图片文件上传ftp失败！");
		}
	}

	@ApiOperation(value = "上传场景excel文件", notes = "上传场景excel文件")
	@PostMapping(value = "/uploadSceneInfoExcelFile")
	public ResponseMessage2<List<Tv3SceneExcelWebInfo>> uploadSceneInfoExcelFile(MultipartFile file) {
		return sceneManageService.uploadSceneInfoExcelFile(file);
	}

	@ApiOperation(value = "导出底层采集的ini文件", notes = "导出底层采集的ini文件")
	@GetMapping(value = "/exportIniFile")
	public void exportIniFile(HttpServletResponse response) {
		sceneManageService.exportIniFile(response);
	}

	@ApiOperation(value = "导出老版底层采集的ini文件", notes = "导出老版底层采集的ini文件")
	@GetMapping(value = "/exportOldIniFile")
	public void exportOldIniFile(HttpServletResponse response) {
		sceneManageService.exportOldIniFile(response);
	}

	@ApiOperation(value = "查询场景树数据", notes = "查询场景树数据")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "sceneId", value = "场景Id", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "sceneName", value = "场景名称", required = false, dataType = "String", paramType = "query") })
	@SystemHandleLogInfo(databaseAccessType = DatabaseAccessType.READ, logFunction = "查询场景树数据", logLevel = LogLevel.DEBUG, logMessage = "查询场景树数据", logModule = "场景管理")
	@GetMapping(value = "/getSceneTreeData")
	public ResponseMessage2<List<SceneTreeChildrenData>> getSceneTreeData(Long sceneId, String sceneName,
			HttpServletRequest request) {
		return sceneManageService.getSceneTreeData(sceneId, sceneName, request);
	}

	@ApiOperation(value = "获取场景栅格配置信息", notes = "获取场景栅格配置信息")
	@GetMapping(value = "/getSceneGridConfigInfo")
	public ResponseMessage2<SceneGridConfig> getSceneGridConfigInfo() {
		return sceneManageService.getSceneGridConfigInfo();
	}

	@ApiOperation(value = "生成所有栅格数据", notes = "生成所有栅格数据")
	@PostMapping(value = "/buildAllGridData")
	public ResponseMessage2<String> buildAllGridData(@RequestBody BuildGridDataConfigWebPar buildGridDataConfigWebPar) {

		return sceneManageService.buildAllGridData(buildGridDataConfigWebPar);
	}

	@ApiOperation(value = "生成新栅格数据", notes = "生成新栅格数据")
	@PostMapping(value = "/buildNewGridData")
	public ResponseMessage2<String> buildNewGridData(@RequestBody BuildGridDataConfigWebPar buildGridDataConfigWebPar) {

		return sceneManageService.buildNewGridData(buildGridDataConfigWebPar);
	}

}
