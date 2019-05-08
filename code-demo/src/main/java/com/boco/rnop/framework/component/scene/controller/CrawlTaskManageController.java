package com.boco.rnop.framework.component.scene.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.boco.rnop.framework.common.ResponseMessage2;
import com.boco.rnop.framework.component.scene.entity.AnalysisGeometryResultInfo;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3GisGeometry;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3PoiCrawlTask;
import com.boco.rnop.framework.component.scene.service.ICrawlTaskManagerService;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * 场景管理-爬取任务管理
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2019年4月4日 上午9:58:51
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@RestController
@RequestMapping("/SceneCrawlTaskManager")
@Api(tags = "场景管理-爬取任务管理")
public class CrawlTaskManageController {

	@Autowired
	private ICrawlTaskManagerService crawlTaskManagerService;

	@ApiOperation(value = "查询爬取任务", notes = "查询爬取任务")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "taskType", value = "任务类型（1地理图元爬取,2图层文件导入）", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "taskState", value = "任务状态（-2实时执行,-1中断，0未执行，1执行中，2已执行）", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "taskName", value = "任务名称(模糊查询)", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "pageNum", value = "分页页数", required = true, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "pageSize", value = "分页每页数量", required = true, dataType = "Long", paramType = "query") })
	@GetMapping(value = "/selectCrawlTaskInfo")
	public ResponseMessage2<PageInfo<Tv3PoiCrawlTask>> selectCrawlTaskInfo(Integer taskType, Integer taskState,
			String taskName, Integer pageNum, Integer pageSize) {
		return ResponseMessage2.Success2(
				crawlTaskManagerService.selectCrawlTaskInfo(taskType, taskState, taskName, pageNum, pageSize));
	}

	@ApiOperation(value = "删除爬取任务", notes = "删除爬取任务")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "intId", value = "编号", required = true, dataType = "Long", paramType = "query") })
	@GetMapping(value = "/deleteCrawlTaskInfo")
	public ResponseMessage2<Integer> deleteCrawlTaskInfo(Long intId) {
		return ResponseMessage2.Success2(crawlTaskManagerService.deleteCrawlTaskInfo(intId));
	}

	@ApiOperation(value = "修改爬取任务状态", notes = "修改爬取任务状态")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "intId", value = "编号", required = true, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "taskState", value = "任务状态（-1中断，0未执行，1执行中，2已执行）", required = true, dataType = "Long", paramType = "query") })
	@GetMapping(value = "/updateCrawlTaskStateInfo")
	public ResponseMessage2<Integer> updateCrawlTaskStateInfo(Long intId, Integer taskState) {
		return crawlTaskManagerService.updateCrawlTaskStateInfo(intId, taskState);
	}

	@ApiOperation(value = "新增爬取任务(等待调度执行)", notes = "新增爬取任务")
	@PostMapping(value = "/insertCrawlTaskInfos")
	public ResponseMessage2<Integer> insertCrawlTaskInfos(HttpServletRequest request,
			@RequestBody List<Tv3PoiCrawlTask> tv3GisCrawlTasks) {
		return crawlTaskManagerService.insertCrawlTaskInfos(request, tv3GisCrawlTasks);
	}

	@ApiOperation(value = "新增爬取任务(立即执行)", notes = "新增爬取任务(立即执行)")
	@PostMapping(value = "/insertCrawlTaskInfosAndExecute")
	public ResponseMessage2<String> insertCrawlTaskInfosAndExecute(HttpServletRequest request,
			@RequestBody List<Tv3PoiCrawlTask> tv3GisCrawlTasks) {
		return crawlTaskManagerService.insertCrawlTaskInfosAndExecute(request, tv3GisCrawlTasks);
	}

	@ApiOperation(value = "立即执行已存在任务", notes = "立即执行已存在任务")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "intId", value = "编号", required = true, dataType = "Long", paramType = "query") })
	@GetMapping(value = "/executeCrawlTask")
	public ResponseMessage2<String> executeCrawlTask(HttpServletRequest request, Long intId) {
		return crawlTaskManagerService.executeCrawlTask(request, intId);
	}

	@ApiOperation(value = "根据任务编号查询地理区域信息", notes = "根据任务编号查询地理区域信息")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "taskId", value = "任务编号", required = true, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "pageNum", value = "分页页数", required = true, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true, dataType = "Long", paramType = "query") })
	@GetMapping(value = "/selectGeometryInfosByTaskId")
	public ResponseMessage2<PageInfo<Tv3GisGeometry>> selectGeometryInfosByTaskId(Long taskId, Integer pageNum,
			Integer pageSize) {
		return ResponseMessage2
				.Success2(crawlTaskManagerService.selectGeometryInfosByTaskId(taskId, pageNum, pageSize));
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

	/**
	 * 上传入库场景数据
	 * 
	 * @param file
	 * @return
	 */
	@ApiOperation(value = "上传入库图层数据", notes = "上传入库图层数据")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "fieldDescriptionInfos", value = "图层数据字段映射关系json串", dataType = "String", required = true, paramType = "query"),
			@ApiImplicitParam(name = "regionId", value = "地市编号", required = true, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "regionName", value = "地市名称", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "subtype", value = "图元亚类型", required = true, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "geometryType", value = "区域分类", required = true, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "geometryTypeName", value = "区域分类名称", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "dataHandleType", value = "任务数据处理类型名称（1新增未存在的信息，2更新爬取的全部信息）", required = true, dataType = "Long", paramType = "query") })
	@PostMapping(value = "/uploadLayerData")
	public ResponseMessage2<Tv3PoiCrawlTask> uploadLayerData(HttpServletRequest request, String fieldDescriptionInfos,
			Long regionId, String regionName, Integer subtype, Integer geometryType, String geometryTypeName,
			Integer dataHandleType, MultipartFile[] files) {
		return crawlTaskManagerService.uploadLayerData(request, fieldDescriptionInfos, regionId, regionName, subtype,
				geometryType, geometryTypeName, dataHandleType, files);
	}

}
