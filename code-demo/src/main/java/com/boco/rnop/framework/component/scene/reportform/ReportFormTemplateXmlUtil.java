package com.boco.rnop.framework.component.scene.reportform;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ReportFormTemplateXmlUtil {

	private ReportFormTemplateXmlUtil() {
	}

	/**
	 * 获取 src/main/resources/ReportFormTemplate 目录下报表模板信息
	 * 
	 * @param reportFormTemplateId
	 * @return
	 */
	public static ReportFormTemplate getReportFormTemplate(String reportFormTemplateId) {
		ReportFormTemplate reportFormTemplate = new ReportFormTemplate();

		if (reportFormTemplateId == null || "".equals(reportFormTemplateId)) {
			return reportFormTemplate;
		}

		JarFile jarFile = null;
		try {

			String path = System.getProperty("user.dir") + File.separator + System.getProperty("java.class.path");
			jarFile = new JarFile(new File(path));
			Enumeration<JarEntry> entries = jarFile.entries();

			while (entries.hasMoreElements()) {
				JarEntry jarEntry = entries.nextElement();
				String innerPath = jarEntry.getName();
				if (innerPath.contains("ReportFormTemplate") && innerPath.endsWith(".xml")) {

					SAXReader saxReader = new SAXReader();

					Document doc = saxReader
							.read(ReportFormTemplateXmlUtil.class.getClassLoader().getResourceAsStream(innerPath));
					Element root = doc.getRootElement();

					for (Iterator<Element> iterator = root.elementIterator("reportFormTemplate"); iterator.hasNext();) {
						Element element = iterator.next();
						if (reportFormTemplateId.equals(element.attributeValue("id"))) {
							reportFormTemplate.setId(element.attributeValue("id"));
							reportFormTemplate.setName(element.attributeValue("name"));
							reportFormTemplate.setColumns(getReportFormTemplateColumn(element));
							reportFormTemplate.setLinkedHashMap(getPropInfo(element));
						}
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return reportFormTemplate;
		} finally {
			if (jarFile != null) {
				try {
					jarFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

		return reportFormTemplate;
	}

	/**
	 * 递归解析column
	 * 
	 * @param root
	 * @return
	 */
	public static List<ReportFormTemplateColumn> getReportFormTemplateColumn(Element root) {
		List<ReportFormTemplateColumn> list = new ArrayList<>();

		for (Iterator<Element> iterator = root.elementIterator("column"); iterator.hasNext();) {
			Element element = iterator.next();
			ReportFormTemplateColumn reportFormTemplateColumn = new ReportFormTemplateColumn();
			reportFormTemplateColumn.setColumns(getReportFormTemplateColumn(element));
			reportFormTemplateColumn.setName(element.attributeValue("name"));
			reportFormTemplateColumn.setPropertyName(element.attributeValue("propertyName"));
			reportFormTemplateColumn.setWidth(element.attributeValue("width"));
			reportFormTemplateColumn.setIsFix(element.attributeValue("isFix"));
			list.add(reportFormTemplateColumn);
		}
		return list;
	}

	/**
	 * 获取字段映射关系
	 * 
	 * @param root
	 * @return
	 */
	private static LinkedHashMap<String, String> getPropInfo(Element root) {
		LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();

		for (Iterator<Element> iterator = root.elementIterator("column"); iterator.hasNext();) {
			Element element = iterator.next();
			String propertyName = element.attributeValue("propertyName");
			if (propertyName != null && !"".equals(propertyName)) {
				linkedHashMap.put(propertyName, element.attributeValue("name"));
			} else {
				linkedHashMap.putAll(getPropInfo(element));
			}
		}

		return linkedHashMap;
	}

}
