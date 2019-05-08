package com.boco.rnop.framework.component.scene.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.boco.com.framework.gis.engine.geometrys.MilitaryGrid;
import com.boco.rnop.framework.common.ResponseMessage2;
import com.boco.rnop.framework.component.scene.entity.AnalysisGeometryResultInfo;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3GisGeometry;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3GisGeometryTemporary;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3PoiCrawlTask;
import com.github.pagehelper.PageInfo;

/**
 * 
 * 爬取任务管理
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2019年4月4日 上午10:54:30
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
public interface ICrawlTaskManagerService {
	/**
	 * 查询爬取任务
	 * 
	 * @param taskType
	 * @param taskState
	 * @param taskName
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PageInfo<Tv3PoiCrawlTask> selectCrawlTaskInfo(Integer taskType, Integer taskState, String taskName,
			Integer pageNum, Integer pageSize);

	/**
	 * 删除爬取任务
	 * 
	 * @param intId
	 * @return
	 */
	public Integer deleteCrawlTaskInfo(Long intId);

	/**
	 * 修改爬取任务状态
	 * 
	 * @param intId
	 * @param taskState
	 * @return
	 */
	public ResponseMessage2<Integer> updateCrawlTaskStateInfo(Long intId, Integer taskState);

	/**
	 * 新增爬取任务
	 * 
	 * @param request
	 * 
	 * @param tv3GisCrawlTasks
	 * @return
	 */
	public ResponseMessage2<Integer> insertCrawlTaskInfos(HttpServletRequest request,
			List<Tv3PoiCrawlTask> tv3GisCrawlTasks);

	/**
	 * 新增爬取任务(立即执行)
	 * 
	 * @param request
	 * 
	 * @param tv3GisCrawlTasks
	 * @return
	 */
	public ResponseMessage2<String> insertCrawlTaskInfosAndExecute(HttpServletRequest request,
			List<Tv3PoiCrawlTask> tv3GisCrawlTasks);

	/**
	 * 根据任务编号查询地理区域信息
	 * 
	 * @param taskId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PageInfo<Tv3GisGeometry> selectGeometryInfosByTaskId(Long taskId, Integer pageNum, Integer pageSize);

	/**
	 * 解析图层文件数据
	 * 
	 * @param files
	 * @return
	 */
	public ResponseMessage2<AnalysisGeometryResultInfo> analysisLayerData(MultipartFile[] files);

	/**
	 * 上传入库图层数据
	 * 
	 * @param request
	 * 
	 * @param fieldDescriptionInfos
	 * @param regionId
	 * @param regionName
	 * @param subtype
	 * @param dataHandleType
	 * @param dataHandleType2
	 * @param geometryTypeName
	 * @param files
	 * @return
	 */
	public ResponseMessage2<Tv3PoiCrawlTask> uploadLayerData(HttpServletRequest request, String fieldDescriptionInfos,
			Long regionId, String regionName, Integer subtype, Integer geometryType, String geometryTypeName,
			Integer dataHandleType, MultipartFile[] files);

	/**
	 * 批量插入地理图元临时数据
	 * 
	 * @param tv3GisGeometryTemporaryQOs
	 * @param batchCount
	 * @return
	 */
	public Integer insertBatchGeometryTemporary(List<Tv3GisGeometryTemporary> tv3GisGeometryTemporaries,
			Integer batchCount);

	/**
	 * 立即执行已存在任务
	 * 
	 * @param request
	 * @param intId
	 * @return
	 */
	public ResponseMessage2<String> executeCrawlTask(HttpServletRequest request, Long intId);

	/**
	 * 执行未执行的爬取任务
	 * 
	 * @param request
	 * @param intId
	 * @return
	 */
	public void executeNewCrawlTask(Tv3PoiCrawlTask tv3GisCrawlTask);

	/**
	 * 立即执行已存在任务
	 * 
	 * @param request
	 * @param intId
	 * @return
	 */
	public List<MilitaryGrid> getRegionMilitaryGrids(Tv3GisGeometry tv3GisGeometry);

}
