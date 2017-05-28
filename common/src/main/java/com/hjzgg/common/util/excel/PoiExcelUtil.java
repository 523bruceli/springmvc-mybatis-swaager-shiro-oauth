package com.hjzgg.common.util.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

public class PoiExcelUtil {
    private static String excel2007Path = "D:\\temp\\style_2007.xlsx";
    private static String excel2003Path = "D:\\temp\\style_2003.xls";

    public static void main(String[] args) throws Exception {
        creatExcel(excel2007Path, "2007");// 创建2007版Excel文件
        creatExcel(excel2003Path,"2003");// 创建2003版Excel文件

//        List<List<Object>> excel2007List = readExcel(excel2007Path);// 读取2007版Excel文件
        List<List<Object>> excel2003List = readExcel(excel2003Path);// 读取2003版Excel文件
        System.out.println(excel2003List.toString());
    }

    /**
     * 创建Excel文件
     * 
     * @return
     * @throws IOException
     */
    public static void creatExcel(String excelPath, String version) throws IOException {
        // XSSFWork used for .xslx (>=2007), HSSWorkbook for 03 .xsl
        Workbook workbook = null;
        if (version.equals("2007")) {
            workbook = new XSSFWorkbook();// 创建 一个excel文档对象
        } else if (version.equals("2003")) {
            workbook = new HSSFWorkbook();// 创建 一个excel文档对象
        }
        Sheet sheet = workbook.createSheet("2007sheet");// 创建一个工作薄对象
        sheet.setColumnWidth(1, 10000);// 设置第二列的宽度为

        Row row0 = sheet.createRow(1);// 创建一个行对象,从0行开始
        row0.setHeightInPoints(23);// 设置行高23像素
        for (int i = 0; i < 11; i++) {
            Cell cell_1 = row0.createCell(i, Cell.CELL_TYPE_STRING);// 创建单元格，从0列开始
            cell_1.setCellValue("column" + i);// 写入单元格的值
            CellStyle style = getStyle(workbook);
            cell_1.setCellStyle(style);// 应用样式对象
            sheet.autoSizeColumn(i);// 自动调整列宽
        }

        FileOutputStream outputStream = new FileOutputStream(excelPath);
        workbook.write(outputStream);// 将文档对象写入文件输出流

        outputStream.close();// 关闭文件输出流
        System.out.println("创建成功 office excel");
    }

    /**
     * 设置样式
     * 
     * @param workbook
     * @return
     */
    private static CellStyle getStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();// 创建样式对象
        // 设置对齐方式
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);// 水平居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中

        // 设置边框
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);// 顶部边框粗线
        style.setTopBorderColor(HSSFColor.RED.index);// 设置为红色
        style.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);// 底部边框双线
        style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);// 左边边框
        style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);// 右边边框

        style.setWrapText(true);// 设置单元格内容是否自动换行
        // 格式化日期
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));

        // 设置单元格字体
        Font font = workbook.createFont(); // 创建字体对象
        font.setFontHeightInPoints((short) 14);// 设置字体大小
        font.setColor(HSSFColor.RED.index);// 设置字体颜色
        font.setFontName("宋体");// 设置为宋体字
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 设置粗体
        style.setFont(font);// 将字体加入到样式对象

        return style;
    }

    /**
     * 读取excel
     * 
     * @param fileName
     * @return 行<列>
     * @throws IOException
     */

    private static List<List<Object>> readExcel(String fileName) throws IOException {
        File file = new File(fileName);
        Workbook wb = null;
        if (fileName.endsWith(".xlsx")) {// 2007
            wb = new XSSFWorkbook(new FileInputStream(file));// 创建 一个excel文档对象
        } else if (fileName.endsWith(".xls")) {// 2003
            wb = new HSSFWorkbook(new FileInputStream(file));// 创建 一个excel文档对象
        }

        Sheet sheet = wb.getSheetAt(0);// 读取第一个sheet页表格内容
        Object value = null;
        Row row = null;
        Cell cell = null;
        System.out.println("读取office 2007 excel内容如下：");
//        System.out.println(sheet.getPhysicalNumberOfRows());// 获取的是物理行数，也就是不包括那些空行（隔行）的情况。
//        System.out.println(sheet.getLastRowNum());// 获取的是最后一行的编号（编号从0开始）
        // 行
        List<List<Object>> rowlist = new LinkedList<List<Object>>();
        for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            if (row == null) {
                continue;
            }

            // 列
            List<Object> cellList = new LinkedList<Object>();
            for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                cell = row.getCell(j);
                if (cell == null) {
                    continue;
                }

                DecimalFormat df = new DecimalFormat("0");// 格式化 number String
                DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
                switch (cell.getCellType()) {
                    case XSSFCell.CELL_TYPE_STRING:// 字符串——String type
                        value = cell.getStringCellValue();
                        break;
                    case XSSFCell.CELL_TYPE_NUMERIC:// 数字——Number type
                        if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                            value = df.format(cell.getNumericCellValue());
                        } else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                            value = nf.format(cell.getNumericCellValue());
                        } else {
                            value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                        }
                        break;
                    case XSSFCell.CELL_TYPE_BOOLEAN:// boolean——Boolean type
                        value = cell.getBooleanCellValue();
                        break;
                    case XSSFCell.CELL_TYPE_BLANK:// 空白——Blank type
                        value = "";
                        break;
                    default:// default type
                        value = cell.toString();
                }
                if (value == null || "".equals(value)) {
                    continue;
                }
                cellList.add(value);
            }
            rowlist.add(cellList);
        }
        return rowlist;
    }
}