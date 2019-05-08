package com.boco.rnop.framework.component.scene.manager;

import java.util.List;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.boco.rnop.framework.component.scene.entity.po.BaiduAkInfoPO;
import com.boco.rnop.framework.component.scene.service.IBaiduSourseService;

/**
 * 
 * 百度资源初始化
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年9月12日 下午2:40:05
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@Component
public class BaiduSourseApplicationRunner implements ApplicationRunner {

	@Autowired
	private IBaiduSourseService baiduSourseService;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		List<BaiduAkInfoPO> baiduAkInfoPOs = baiduSourseService.selectAllBaiduAk();
		Vector<BaiduAkInfoPO> vector = new Vector<>();

		vector.addAll(baiduAkInfoPOs);
		BaiduSourseManager.setBaiduAkInfoPOs(vector);
		// 检查ak信息是否最新
		BaiduSourseManager.checkAkLatest();
	}

}
