package com.boco.rnop.framework.component.scene.entity;

import java.util.List;

/**
 * 
 * geojson 多面
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年9月25日 下午3:51:58
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
public class SceneCrawlingDataMultipleGeoObject extends SceneCrawlingDataGeoObject {

	/**
	 * 轮廓点集
	 */
	private List<List<List<Double[]>>> coordinates;

	public List<List<List<Double[]>>> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<List<List<Double[]>>> coordinates) {
		this.coordinates = coordinates;
	}

	@Override
	public List<List<List<Double[]>>> getPolygons() {
		// TODO Auto-generated method stub
		return coordinates;
	}

}
