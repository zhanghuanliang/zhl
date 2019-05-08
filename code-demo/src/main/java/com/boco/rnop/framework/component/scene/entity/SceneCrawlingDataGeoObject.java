package com.boco.rnop.framework.component.scene.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.boco.com.framework.gis.engine.geometrys.EnginePoint;

/**
 * 
 * geojson
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年9月28日 上午11:02:31
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
public abstract class SceneCrawlingDataGeoObject {

	/**
	 * 
	 */
	private String type;

	/**
	 * 获取面坐标点
	 * 
	 * @return
	 */
	public abstract List<List<List<Double[]>>> getPolygons();

	/**
	 * 获取EnginePoint点面集合
	 * 
	 * @return
	 */
	public List<List<EnginePoint>> getEnginePoints() {
		List<List<EnginePoint>> result = new ArrayList<>();

		List<List<List<Double[]>>> lists = getPolygons();
		if (lists == null) {
			return result;
		}
		for (List<List<Double[]>> list : lists) {
			if (list != null && !list.isEmpty()) {
				List<EnginePoint> enginePoints = new ArrayList<>();
				List<Double[]> doubles = list.get(0);
				for (Double[] doubles2 : doubles) {
					if (doubles2 != null && doubles2.length == 2) {
						EnginePoint enginePoint = new EnginePoint(doubles2[0], doubles2[1]);
						enginePoints.add(enginePoint);
					}
				}
				result.add(enginePoints);
			}
		}

		return result;

	}

	public Double getMaxLongitude() {
		List<List<List<Double[]>>> lists = getPolygons();
		List<Double> list = new ArrayList<>();
		for (List<List<Double[]>> list2 : lists) {
			for (List<Double[]> list3 : list2) {
				for (Double[] doubles : list3) {
					if (doubles != null && doubles.length == 2) {
						list.add(doubles[0]);
					}
				}
			}
		}

		return Collections.max(list);
	}

	public Double getMaxLatitude() {
		List<List<List<Double[]>>> lists = getPolygons();
		List<Double> list = new ArrayList<>();
		for (List<List<Double[]>> list2 : lists) {
			for (List<Double[]> list3 : list2) {
				for (Double[] doubles : list3) {
					if (doubles != null && doubles.length == 2) {
						list.add(doubles[1]);
					}
				}
			}
		}

		return Collections.max(list);
	}

	public Double getMinLongitude() {
		List<List<List<Double[]>>> lists = getPolygons();
		List<Double> list = new ArrayList<>();
		for (List<List<Double[]>> list2 : lists) {
			for (List<Double[]> list3 : list2) {
				for (Double[] doubles : list3) {
					if (doubles != null && doubles.length == 2) {
						list.add(doubles[0]);
					}
				}
			}
		}

		return Collections.min(list);
	}

	public Double getMinLatitude() {
		List<List<List<Double[]>>> lists = getPolygons();
		List<Double> list = new ArrayList<>();
		for (List<List<Double[]>> list2 : lists) {
			for (List<Double[]> list3 : list2) {
				for (Double[] doubles : list3) {
					if (doubles != null && doubles.length == 2) {
						list.add(doubles[1]);
					}
				}
			}
		}

		return Collections.min(list);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
