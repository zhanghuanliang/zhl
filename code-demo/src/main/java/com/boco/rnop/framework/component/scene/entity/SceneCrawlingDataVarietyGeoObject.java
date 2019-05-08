package com.boco.rnop.framework.component.scene.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * geojson 变种面（无孔洞）
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年9月25日 下午3:52:39
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
public class SceneCrawlingDataVarietyGeoObject extends SceneCrawlingDataGeoObject {
	/**
	 * 轮廓点集
	 */
	private List<Double[]> coordinates;

	public List<Double[]> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<Double[]> coordinates) {
		this.coordinates = coordinates;
	}

	@Override
	public List<List<List<Double[]>>> getPolygons() {
		// TODO Auto-generated method stub
		List<List<List<Double[]>>> lists = new ArrayList<>();
		List<List<Double[]>> list = new ArrayList<>();
		list.add(coordinates);
		lists.add(list);
		return lists;
	}

}
