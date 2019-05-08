package com.boco.rnop.framework.component.scene.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 
 * 场景图片配置信息
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月26日 下午3:05:58
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@Component
@Data
public class SceneManageAutoConfiguration {
	/**
	 * 场景管理 场景图片存放FTP IP
	 */
	@Value(value = "${sceneManage.sceneImages.ftp.ip:10.10.2.232}")
	private String ftpIp;
	/**
	 * 场景管理 场景图片存放FTP 端口
	 */
	@Value(value = "${sceneManage.sceneImages.ftp.port:21}")
	private Integer ftpPort;
	/**
	 * 场景管理 场景图片存放FTP 用户名
	 */
	@Value(value = "${sceneManage.sceneImages.ftp.userName:root}")
	private String ftpUserName;
	/**
	 * 场景管理 场景图片存放FTP 密码
	 */
	@Value(value = "${sceneManage.sceneImages.ftp.pwd:Boco123}")
	private String ftpPwd;
	/**
	 * 场景管理 场景图片存放FTP 图片存放目录
	 */
	@Value(value = "${sceneManage.sceneImages.ftp.imagePath:/images/sceneImages/}")
	private String ftpImagePath;
	/**
	 * 场景管理 场景图片 入库 图片地址路径
	 */
	@Value(value = "${sceneManage.sceneImages.imageUrlPath:/}")
	private String imageUrlPath;
	/**
	 * 场景管理 栅格边长
	 */
	@Value(value = "#{'${sceneManage.grid.borderLength:20,50,100}'.split(',')}")
	private List<Integer> gridBorderLength;
	/**
	 * 场景管理 线场景路段步长
	 */
	@Value(value = "#{'${sceneManage.grid.step:20,50,100}'.split(',')}")
	private List<Integer> gridStep;
	/**
	 * 场景管理 线场景路段扩充宽度
	 */
	@Value(value = "#{'${sceneManage.grid.extendWidth:20,50,100}'.split(',')}")
	private List<Integer> gridExtendWidth;

}
