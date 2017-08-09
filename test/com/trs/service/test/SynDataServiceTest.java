package com.trs.service.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.trs.model.AppAppendix;
import com.trs.model.AppSyncField;
import com.trs.service.PublicAppBaseService;
import com.trs.service.SynDataService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/config/spring-mvc.xml","/config/spring-hibernate.xml"})

public class SynDataServiceTest {
	@Autowired
	private SynDataService synDataService;
	@Test
	public void synToWcm(){
		Long appId = 1118881l;
		synDataService.synAppDataToWcm(appId);
	}
	@Test
	public void synMetaDataToWcm(){
		Long appId = 1118881l;
		Long metadataId = 2l;
		synDataService.synMetaDataToWcm(appId, metadataId);
	}
	@Test
	public void synToWcmDoc(){
		List<Object> appendixs = new ArrayList<Object>();
		
		AppAppendix dix1 = new AppAppendix();
		dix1.setFileExt("doc");
		dix1.setFileName("附件1");
		dix1.setSrcfile("E:\\temp\\test.doc");
		appendixs.add(dix1);
		AppAppendix dix2 = new AppAppendix();
		dix2.setFileExt("jpg");
		dix2.setFileName("附件2");
		dix2.setSrcfile("E:\\temp\\恩惠.jpg");
		appendixs.add(dix2);
		List<Object> fieldRel = new ArrayList<Object>();
		AppSyncField field = new AppSyncField();
		field.setAppFieldName("title");
		field.setToFieldName("DocTitle");
		fieldRel.add(field);
		AppSyncField field1 = new AppSyncField();
		field1.setAppFieldName("content");
		field1.setToFieldName("DocHtmlCon");
		fieldRel.add(field1);
		Map<String,Object> appData = new HashMap<String,Object>();
		appData.put("title", "测试标题");
		appData.put("content", "测试文档同步。");
		synDataService.synToWcmDoc(10, fieldRel, appData, appendixs);
		
	}
	@Test
	public void synToWcmMeta(){
		List<Object> appendixs = new ArrayList<Object>();
		AppAppendix dix1 = new AppAppendix();
		dix1.setFileExt("doc");
		dix1.setFileName("附件1");
		dix1.setSrcfile("E:\\temp\\test.doc");
		appendixs.add(dix1);
		AppAppendix dix2 = new AppAppendix();
		dix2.setFileExt("jpg");
		dix2.setFileName("附件2");
		dix2.setSrcfile("E:\\temp\\恩惠.jpg");
		appendixs.add(dix2);
		List<Object> fieldRel = new ArrayList<Object>();
		AppSyncField field = new AppSyncField();
		field.setAppFieldName("title");
		field.setToFieldName("title");
		fieldRel.add(field);
		AppSyncField field1 = new AppSyncField();
		field1.setAppFieldName("content");
		field1.setToFieldName("name");
		fieldRel.add(field1);
		Map<String,Object> appData = new HashMap<String,Object>();
		appData.put("title", "测试标题");
		appData.put("content", "测试文档同步。");
		synDataService.synToWcmMeta(144, fieldRel, appData, appendixs);
		
	}
	@Test
	public void isHasView(){
		int isHas = synDataService.isHasView(144);
		System.out.println(isHas);
	}
}
