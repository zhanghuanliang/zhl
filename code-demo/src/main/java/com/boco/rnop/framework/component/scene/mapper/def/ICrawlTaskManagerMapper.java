package com.boco.rnop.framework.component.scene.mapper.def;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.boco.rnop.framework.component.scene.entity.tk.Tv3GisGeometryTemporary;

public interface ICrawlTaskManagerMapper {

	/**
	 * 更新爬取任务结束栅格数量
	 * 
	 * @param taskEndGridNum
	 * @param intId
	 */
	public Integer updateCrawlTaskEndGridNum(@Param("taskEndGridNum") Integer taskEndGridNum,
			@Param("intId") Long intId);

	/**
	 * 批量插入地理图元临时表数据
	 * 
	 * @param tv3GisGeometryTemporaryQOs
	 */
	public Integer insertBatchGeometryTemporary(List<Tv3GisGeometryTemporary> tv3GisGeometryTemporaries);

	/**
	 * 根据地理图元数据临时表,全部地理图元数据合并入库
	 * 
	 * @param batchNum
	 * @return
	 */
	public Integer mergeAllGeometryDataByGeometryTemporary(@Param("batchNum") Integer batchNum);

	/**
	 * 根据地理图元数据临时表,新生成地理图元数据合并入库
	 * 
	 * @param batchNum
	 * @return
	 */
	public Integer mergeNewGeometryDataByGeometryTemporary(@Param("batchNum") Integer batchNum);

	/**
	 * 根据批次号删除临时地理图元数据
	 * 
	 * @param batchNum
	 * @return
	 */
	public Integer deleteGisGeometryTemporaryByBatchNum(@Param("batchNum") Integer batchNum);
}
