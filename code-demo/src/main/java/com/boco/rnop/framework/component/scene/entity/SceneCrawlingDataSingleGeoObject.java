package com.boco.rnop.framework.component.scene.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * geojson 单面
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年9月17日 上午11:21:10
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
public class SceneCrawlingDataSingleGeoObject extends SceneCrawlingDataGeoObject {
	/**
	 * 轮廓点集
	 */
	private List<List<Double[]>> coordinates;

	public List<List<Double[]>> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<List<Double[]>> coordinates) {
		this.coordinates = coordinates;
	}

	@Override
	public List<List<List<Double[]>>> getPolygons() {
		// TODO Auto-generated method stub
		List<List<List<Double[]>>> list = new ArrayList<>();
		list.add(coordinates);
		return list;
	}

}
