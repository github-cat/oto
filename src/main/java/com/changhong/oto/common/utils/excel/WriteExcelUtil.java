package com.changhong.oto.common.utils.excel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
/**
 * 
 * Excel 导入工具类
 *
 */
public class WriteExcelUtil {
	/**
	 * 
	 * @param heads 头信息 标题栏
	 * @param list 数据
	 * @return
	 * @throws Exception
	 */
	public static InputStream exp(String[] heads, List<String[]> list) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		// 创建一个xls文件
		WritableWorkbook workbook = Workbook.createWorkbook(outputStream);
		// 创建xls文件里面的表
		WritableSheet sheet = workbook.createSheet("sheet1", 0);
		// param1:col 列
		// param2:row 行
		for (int i = 0; i < heads.length; i++) {
			Label label = new Label(i, 0, heads[i]);
			sheet.addCell(label);
//			sheet.setColumnView(i, heads[i].length());
		}
		// 2.处理数据
		for (int i = 0; i < list.size(); i++) {
			String[] strings = list.get(i);// 一行的数据
			for (int j = 0; j < strings.length; j++) {
				Label label = new Label(j, i + 1, strings[j]);// 排除表头已经用了一行
				sheet.addCell(label);
				if(strings[j] != null){
					sheet.setColumnView(j, strings[j].length()+10);
				}
			}
		}
		// 关闭流
		workbook.write();
		workbook.close();
		return new ByteArrayInputStream(outputStream.toByteArray());
	}
}
