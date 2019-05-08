package com.boco.rnop.framework.component.scene.entity;

import java.util.ArrayList;
import java.util.List;

import com.boco.com.framework.gis.engine.models.EngineLayerAttrInfo;

/**
 * 
 * 解析图层文件结构信息
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年11月27日 下午7:19:23
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
public class AnalysisGeometryResultInfo {
	/**
	 * 图层文件结构属性信息
	 */
	private List<EngineLayerAttrInfo> layerAttrInfos;

	/**
	 * 映射关系属性字段
	 */
	private List<FieldDescriptionInfo> propertyName = new ArrayList<>();

	public AnalysisGeometryResultInfo() {

		propertyName.add(new FieldDescriptionInfo("intId", "网元ID(Long)"));
		propertyName.add(new FieldDescriptionInfo("costomId", "特殊编号(String)"));
		propertyName.add(new FieldDescriptionInfo("name", "图元名称(String)"));
		propertyName.add(new FieldDescriptionInfo("type", "图元类型(Integer)"));
		propertyName.add(new FieldDescriptionInfo("geometryName", "图元类型名称(String)"));
		propertyName.add(new FieldDescriptionInfo("lon", "经度(Double)"));
		propertyName.add(new FieldDescriptionInfo("lat", "纬度(Double)"));
		propertyName.add(new FieldDescriptionInfo("subtype", "网元亚类型(Double)"));
		propertyName.add(new FieldDescriptionInfo("provinceName", "所属省(String)"));
		propertyName.add(new FieldDescriptionInfo("provinceId", "所属省ID(Integer)"));
		propertyName.add(new FieldDescriptionInfo("regionName", "所属地市名称(String)"));
		propertyName.add(new FieldDescriptionInfo("regionId", "所属地市ID(Integer)"));
		propertyName.add(new FieldDescriptionInfo("geometryLength", "图元周长(Double)"));
		propertyName.add(new FieldDescriptionInfo("geometryArea", "图元面积(Double)"));
		propertyName.add(new FieldDescriptionInfo("isImport", "导入或爬取(Integer)"));
		propertyName.add(new FieldDescriptionInfo("bdUid", "百度地图UID(String)"));
		propertyName.add(new FieldDescriptionInfo("optr", "操作人(String)"));

	}

	public List<EngineLayerAttrInfo> getLayerAttrInfos() {
		return layerAttrInfos;
	}

	public void setLayerAttrInfos(List<EngineLayerAttrInfo> layerAttrInfos) {
		this.layerAttrInfos = layerAttrInfos;
	}

	public List<FieldDescriptionInfo> getPropertyName() {
		return propertyName;
	}

}
