package com.boco.rnop.framework.component.scene.entity.tk;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import tk.mybatis.mapper.annotation.ColumnType;

@Table(name = "tv3_poi_crawl_task")
@ApiModel(description = "任务表")
@Component
public class Tv3PoiCrawlTask implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 任务状态（-1中断，0未执行，1执行中，2已执行） */
	@ApiModelProperty(value = "任务状态（-1中断，0未执行，1执行中，2已执行）")
	private Integer taskState;
	@ApiModelProperty(value = "任务状态名称")
	@Transient
	private String taskStateName;
	/** 任务类型（1poi爬取,2地理图元爬取） */
	@ApiModelProperty(value = "任务类型（1地理图元爬取,2图层文件导入）")
	private Integer taskType;
	@ApiModelProperty(value = "任务类型名称")
	@Transient
	private String taskTypeName;
	/** 任务数据处理类型（1新增未存在的信息，2更新爬取的全部信息） */
	@ApiModelProperty(value = "任务数据处理类型（1新增未存在的信息，2更新爬取的全部信息）")
	private Integer taskDataHandleType;
	@ApiModelProperty(value = "任务数据处理类型名称（1新增未存在的信息，2更新爬取的全部信息）")
	@Transient
	private String taskDataHandleTypeName;
	/** 任务栅格总数量 */
	@ApiModelProperty(value = "任务栅格总数量")
	private Integer taskGridNum;
	/** 任务当前已爬取栅格数量 */
	@ApiModelProperty(value = "任务当前已爬取栅格数量")
	private Integer taskEndGridNum;
	/** 任务编号 */
	@Id
	@ApiModelProperty(value = "任务编号")
	private Long intId;
	/** 任务名称 */
	@ApiModelProperty(value = "任务名称")
	private String taskName;
	/** 创建人编号 */
	@ApiModelProperty(value = "创建人编号")
	private String createUserid;
	/** 创建人名称 */
	@ApiModelProperty(value = "创建人名称")
	private String createUsername;
	/** 爬取名称（多名称用逗号隔开） */
	@ColumnType(jdbcType = JdbcType.CLOB)
	@ApiModelProperty(value = "爬取名称（多名称用逗号隔开）")
	private String crawlQueryNames;

	/** 中断地市序号 */
	@ApiModelProperty(value = "中断地市序号")
	private Integer breakRegionOrder;
	/** 中断地市栅格序号 */
	@ApiModelProperty(value = "中断地市栅格序号")
	private Integer breakGridOrder;
	/** 中断类型序号 */
	@ApiModelProperty(value = "中断类型序号")
	private Integer breakSubtypeOrder;

	/** 中断批次号 */
	@ApiModelProperty(value = "中断批次号")
	private Integer breakBatchNum;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	private Date updateDate;
	/**
	 * 任务地市关联
	 */
	@Transient
	@ApiModelProperty(value = "任务地市关联")
	private List<Tv3PoiCrawlTaskRegion> tv3GisCrawlTaskRegions;
	/**
	 * 任务网优类型关联表
	 */
	@Transient
	@ApiModelProperty(value = "任务网优类型关联")
	private List<Tv3PoiCrawlTaskSty> tv3GisCrawlTaskSties;

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getTaskState() {
		return taskState;
	}

	public void setTaskState(Integer taskState) {
		switch (taskState) {
		case -2:
			this.taskStateName = "立即执行";
			break;
		case -1:
			this.taskStateName = "中断";
			break;
		case 0:
			this.taskStateName = "未执行";
			break;
		case 1:
			this.taskStateName = "执行中";
			break;
		case 2:
			this.taskStateName = "已执行";
			break;
		default:

			break;
		}
		this.taskState = taskState;
	}

	public Integer getTaskType() {
		return taskType;
	}

	public void setTaskType(Integer taskType) {

		switch (taskType) {
		case 1:
			this.taskTypeName = "poi爬取任务";
			break;
		case 2:
			this.taskTypeName = "地理区域爬取";
			break;
		case 3:
			this.taskTypeName = "图层文件上传";
			break;
		default:
			break;
		}

		this.taskType = taskType;
	}

	public Integer getTaskGridNum() {
		return taskGridNum;
	}

	public void setTaskGridNum(Integer taskGridNum) {
		this.taskGridNum = taskGridNum;
	}

	public Integer getTaskEndGridNum() {
		return taskEndGridNum;
	}

	public void setTaskEndGridNum(Integer taskEndGridNum) {
		this.taskEndGridNum = taskEndGridNum;
	}

	public Long getIntId() {
		return intId;
	}

	public void setIntId(Long intId) {

		this.intId = intId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Integer getBreakRegionOrder() {
		return breakRegionOrder;
	}

	public void setBreakRegionOrder(Integer breakRegionOrder) {
		this.breakRegionOrder = breakRegionOrder;
	}

	public Integer getBreakGridOrder() {
		return breakGridOrder;
	}

	public void setBreakGridOrder(Integer breakGridOrder) {
		this.breakGridOrder = breakGridOrder;
	}

	public Integer getBreakSubtypeOrder() {
		return breakSubtypeOrder;
	}

	public void setBreakSubtypeOrder(Integer breakSubtypeOrder) {
		this.breakSubtypeOrder = breakSubtypeOrder;
	}

	public List<Tv3PoiCrawlTaskRegion> getTv3GisCrawlTaskRegions() {
		return tv3GisCrawlTaskRegions;
	}

	public void setTv3GisCrawlTaskRegions(List<Tv3PoiCrawlTaskRegion> tv3GisCrawlTaskRegions) {
		this.tv3GisCrawlTaskRegions = tv3GisCrawlTaskRegions;
	}

	public List<Tv3PoiCrawlTaskSty> getTv3GisCrawlTaskSties() {
		return tv3GisCrawlTaskSties;
	}

	public void setTv3GisCrawlTaskSties(List<Tv3PoiCrawlTaskSty> tv3GisCrawlTaskSties) {
		this.tv3GisCrawlTaskSties = tv3GisCrawlTaskSties;
	}

	public String getTaskStateName() {
		return taskStateName;
	}

	public void setTaskStateName(String taskStateName) {
		this.taskStateName = taskStateName;
	}

	public String getTaskTypeName() {
		return taskTypeName;
	}

	public void setTaskTypeName(String taskTypeName) {
		this.taskTypeName = taskTypeName;
	}

	public Integer getTaskDataHandleType() {
		return taskDataHandleType;
	}

	public void setTaskDataHandleType(Integer taskDataHandleType) {
		if (taskDataHandleType == null) {
			this.taskDataHandleTypeName = "更新爬取的全部数据信息";
		}
		if (taskDataHandleType == 1) {
			this.taskDataHandleTypeName = "新增爬取到的新数据信息";
		} else {
			this.taskDataHandleTypeName = "更新爬取的全部数据信息";
		}
		this.taskDataHandleType = taskDataHandleType;
	}

	public String getTaskDataHandleTypeName() {
		return taskDataHandleTypeName;
	}

	public void setTaskDataHandleTypeName(String taskDataHandleTypeName) {
		this.taskDataHandleTypeName = taskDataHandleTypeName;
	}

	public Integer getBreakBatchNum() {
		return breakBatchNum;
	}

	public void setBreakBatchNum(Integer breakBatchNum) {
		this.breakBatchNum = breakBatchNum;
	}

	public String getCreateUserid() {
		return createUserid;
	}

	public void setCreateUserid(String createUserid) {
		this.createUserid = createUserid;
	}

	public String getCreateUsername() {
		return createUsername;
	}

	public void setCreateUsername(String createUsername) {
		this.createUsername = createUsername;
	}

	public String getCrawlQueryNames() {
		return crawlQueryNames;
	}

	public void setCrawlQueryNames(String crawlQueryNames) {
		this.crawlQueryNames = crawlQueryNames;
	}

}
