package com.boco.rnop.framework.component.scene.entity.baidu.api;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 场景轮廓点集服务返回结果
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年9月19日 下午3:29:54
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
public class BaiduSceneGeoApiResponseMessage {

	private BaiduSceneGeoApiContent content;

	public BaiduSceneGeoApiContent getContent() {
		return content;
	}

	public void setContent(BaiduSceneGeoApiContent content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "BaiduSceneGeoApiResponseMessage [content=" + content + "]";
	}

	/**
	 * 获取百度米制坐标点集
	 * 
	 * @return 如：12946839.3108, 4838322.93685; 12946906.8634, 4838319.28782;
	 *         12946972.4113, 4838315.20834; 12946999.3711, 4838313.12022;
	 *         12947009.8834, 4838331.74027
	 */
	public List<String> getGoeCoordinates() {
		List<String> list = new ArrayList<>();

		String geo = content.getGeo();

		if (geo == null) {
			return list;
		}

		try {
			if (geo.contains("|1-")) {
				// 支持正则表达式（特殊字符需使用转义字符）
				String[] strs = geo.split("/|1-");
				geo = strs[1];
				geo = geo.replaceAll(";", "");
				String[] coordinates = geo.split(",");
				// 分组，20条一组，避免后续调用百度api数据超长
				int count = 20 * 2;
				int size = coordinates.length / count + 1;

				for (int i = 0; i < size; i++) {
					StringBuffer buffer = new StringBuffer();
					for (int j = i * count; j < (i + 1) * count; j += 2) {
						if ((j + 1) < coordinates.length) {
							buffer.append(coordinates[j] + "," + coordinates[j + 1] + ";");
						}
					}
					String result = buffer.toString();
					result = result.substring(0, result.length() - 1);
					list.add(result);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

}
