package com.hc.xiaobairent.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Xml;

public class CityUtil {

	private CityUtil(Context context) {
		super();
	}

	public static List<CityModel> getList(Context context) {
		InputStream inputStream;
		try {
			inputStream = context.getResources().getAssets().open("allregion.xml");
			XmlPullParser parser = Xml.newPullParser();
			try {
				parser.setInput(inputStream, "UTF-8");// 设置数据源编码
				int eventType = parser.getEventType();// 获取事件类型
				CityModel provience = null;
				CityModel city = null;
				CityModel region = null;
				List<CityModel> proviences = null;
				List<CityModel> cities = null;
				List<CityModel> regions = null;
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:// 文档开始事件,可以进行数据初始化处理
						proviences = new ArrayList<CityModel>();// 实例化集合类
						break;
					case XmlPullParser.START_TAG:// 开始读取某个标签
						// 通过getName判断读到哪个标签，然后通过nextText()获取文本节点值，或通过getAttributeValue(i)获取属性节点值
						String name = parser.getName();
						if (name.equalsIgnoreCase("item")) {
							if (provience == null) {
								provience = new CityModel();
								cities = new ArrayList<CityModel>();// 实例化集合类
							} else if (city == null) {
								city = new CityModel();
								regions = new ArrayList<CityModel>();// 实例化集合类
							} else if (region == null) {
								region = new CityModel();
							}
						} else if (region != null) {
							if (name.equalsIgnoreCase("id")) {
								region.setId(Integer.valueOf(parser.nextText()));
							} else if (name.equalsIgnoreCase("region_name")) {
								region.setRegion_name(parser.nextText());// 如果后面是Text元素,即返回它的值
							} else if (name.equalsIgnoreCase("region_type")) {
								region.setRegion_type(new Short(parser.nextText()));
							}
						} else if (city != null) {
							if (name.equalsIgnoreCase("id")) {
								city.setId(Integer.valueOf(parser.nextText()));
							} else if (name.equalsIgnoreCase("region_name")) {
								city.setRegion_name(parser.nextText());// 如果后面是Text元素,即返回它的值
							} else if (name.equalsIgnoreCase("region_type")) {
								city.setRegion_type(new Short(parser.nextText()));
							}
						} else if (provience != null) {
							if (name.equalsIgnoreCase("id")) {
								provience.setId(Integer.valueOf(parser.nextText()));
							} else if (name.equalsIgnoreCase("region_name")) {
								provience.setRegion_name(parser.nextText());// 如果后面是Text元素,即返回它的值
							} else if (name.equalsIgnoreCase("region_type")) {
								provience.setRegion_type(new Short(parser.nextText()));
							}
						}
						break;
					case XmlPullParser.END_TAG:// 结束元素事件
						// 读完一个Person，可以将其添加到集合类中
						if (parser.getName().equalsIgnoreCase("item")) {
							if (region != null) {
								regions.add(region);
								region = null;
							} else if (city != null) {
								city.setSon(regions);
								cities.add(city);
								city = null;
							} else if (provience != null) {
								provience.setSon(cities);
								proviences.add(provience);
								provience = null;
							}
						}
						break;
					}
					eventType = parser.next();
				}
				inputStream.close();
				return proviences;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return null;
	}

}
