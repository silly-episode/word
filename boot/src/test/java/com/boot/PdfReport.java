package com.boot;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2523/2/25 16:31
 * @FileName: PdfTest
 * @Description:
 */
public class PdfReport {

    // 定义全局的字体静态变量
    private static Font titlefont;
    private static Font headfont;
    private static Font keyfont;
    private static Font textfont;
    // 最大宽度
    private static final int maxWidth = 525;

    // 静态代码块
    static {
        try {
            String frontPath = new ClassPathResource("static/fonts/simsun.ttc").getPath();
            // 不同字体（这里定义为同一种字体：包含不同字号、不同style）
            BaseFont chinese = BaseFont.createFont(frontPath + ",1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            BaseFont idea = BaseFont.createFont("static/fonts/JetBrainsMono-Light.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            titlefont = new Font(bfChinese, 10, Font.BOLD);
            headfont = new Font(chinese, 15, Font.BOLD);
            keyfont = new Font(idea, 8, Font.NORMAL);
            titlefont = new Font(idea, 10, Font.BOLD);
            textfont = new Font(chinese, 7, Font.NORMAL);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // main测试
    public static void main(String[] args) throws Exception {
        try {
            // 1.新建document对象
            Document document = new Document(PageSize.A4);// 建立一个Document对象

            // 2.建立一个书写器(Writer)与document对象关联
            File file = new File("D:\\PDFDemo.pdf");
            file.createNewFile();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));


            // 3.打开文档
            document.open();
            document.addTitle("Title@PDF-Java");// 标题
            document.addAuthor("Author@umiz");// 作者
            document.addSubject("Subject@iText pdf sample");// 主题
            document.addKeywords("Keywords@iTextpdf");// 关键字
            document.addCreator("Creator@umiz`s");// 创建者

            // 4.向文档中添加内容
            new PdfReport().generatePDF(document);

            // 5.关闭文档
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 生成PDF文件
    public void generatePDF(Document document) throws Exception {

        // 表格
        PdfPTable table = createTable(new float[]{50, 110, 170, 30, 50, 110, 170, 30});
//        表格上方信息
        table.addCell(createCell("Title: " + "收藏的单词", headfont, Element.ALIGN_LEFT, 6, false));
        table.addCell(createCell("Date:    /    /  ", headfont, Element.ALIGN_LEFT, 4, false));

//        表头
        table.addCell(createNoTitleCell("No.", titlefont));
        table.addCell(createWordTitleCell("Word", titlefont));
        table.addCell(createWordTitleCell("Meaning", titlefont));
        table.addCell(createBlankTitleCell(" √ ", titlefont));

        table.addCell(createNoTitleCell("No.", titlefont));
        table.addCell(createWordTitleCell("Word", titlefont));
        table.addCell(createWordTitleCell("Meaning", titlefont));
        table.addCell(createBlankTitleCell(" √ ", titlefont));


//表格主题
        for (int i = 0; i < 50; i = i + 2) {
            table.addCell(createNoCell(String.valueOf(1000 + 1), keyfont));
            table.addCell(createWordCell("headmistress", keyfont));
            table.addCell(createWordCell("n. 女校长", textfont));
            table.addCell(createBlackCell("", keyfont));

            table.addCell(createNoCell(String.valueOf(i + 2), keyfont));
            table.addCell(createWordCell("platform", keyfont));
            table.addCell(createWordCell("n. 站台；平台，阵地；讲台；舞台；论坛；高台", textfont));
            table.addCell(createBlackCell("", keyfont));
        }
        document.add(table);

    }

    /**
     * @param value:
     * @param font:
     * @Return: PdfPCell
     * @Author: DengYinzhe
     * @Description: NoTitle的单元格
     * @Date: 2523/2/27 9:50
     */
    public PdfPCell createNoTitleCell(String value, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);

        cell.setBorder(Rectangle.TOP);
//        右边虚线
        cell.setCellEvent(new CustomCellRight());
        cell.setCellEvent(new CustomCellSolidBottom());
        cell.setCellEvent(new CustomCellSolidLeft());
        cell.setFixedHeight(25);
        cell.setPaddingLeft(7);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }

    /**
     * @param value:
     * @param font:
     * @Return: PdfPCell
     * @Author: DengYinzhe
     * @Description: NoTitle的单元格
     * @Date: 2523/2/27 9:50
     */
    public PdfPCell createWordTitleCell(String value, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);

        cell.setBorder(Rectangle.TOP);

//        右边虚线
        cell.setCellEvent(new CustomCellRight());
//        底部实线
        cell.setCellEvent(new CustomCellSolidBottom());
        cell.setFixedHeight(25);
        cell.setPaddingLeft(7);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }

    /**
     * @param value:
     * @param font:
     * @Return: PdfPCell
     * @Author: DengYinzhe
     * @Description: NoTitle的单元格
     * @Date: 2523/2/27 9:50
     */
    public PdfPCell createBlankTitleCell(String value, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);

        cell.setBorder(Rectangle.TOP);
        cell.setCellEvent(new CustomCellSolidRight());
        cell.setCellEvent(new CustomCellSolidBottom());
        cell.setFixedHeight(25);
        cell.setPaddingLeft(2);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }

    /**
     * @param value:
     * @param font:
     * @Return: PdfPCell
     * @Author: DengYinzhe
     * @Description: No的单元格
     * @Date: 2523/2/27 9:50
     */
    public PdfPCell createNoCell(String value, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
//        左边实线
        cell.setBorder(Rectangle.LEFT);
//        右边虚线
        cell.setCellEvent(new CustomCellRight());
//        底部虚线
        cell.setCellEvent(new CustomCellBottom());
        cell.setFixedHeight(25);
        cell.setPaddingLeft(7);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }

    /**
     * @param value:
     * @param font:
     * @Return: PdfPCell
     * @Author: DengYinzhe
     * @Description: No的单元格
     * @Date: 2523/2/27 9:50
     */
    public PdfPCell createWordCell(String value, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
//       无线
        cell.setBorder(Rectangle.NO_BORDER);
//        右边虚线
        cell.setCellEvent(new CustomCellRight());
//        底部虚线
        cell.setCellEvent(new CustomCellBottom());
        cell.setFixedHeight(25);
        cell.setPaddingLeft(7);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }


/**------------------------创建表格单元格的方法start----------------------------*/

    /**
     * @param value:
     * @param font:
     * @Return: PdfPCell
     * @Author: DengYinzhe
     * @Description: No的单元格
     * @Date: 2523/2/27 9:50
     */
    public PdfPCell createBlackCell(String value, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
//        边款无线
        cell.setBorder(Rectangle.RIGHT);
//        底部虚线
        cell.setCellEvent(new CustomCellBottom());
        cell.setFixedHeight(25);
        cell.setPaddingLeft(2);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }

    /**
     * @param value:
     * @param font:
     * @param align:
     * @param colspan:
     * @param boderFlag:
     * @Return: PdfPCell
     * @Author: DengYinzhe
     * @Description: 创建单元格（指定字体、水平居..、单元格跨x列合并、设置单元格内边距）,表格上方的信息
     * @Date: 2523/2/27 9:45
     */
    public PdfPCell createCell(String value, Font font, int align, int colspan, boolean boderFlag) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value, font));
        cell.setPadding(3.0f);
        if (!boderFlag) {
            cell.setBorder(0);
            cell.setPaddingTop(11.5f);
            cell.setPaddingBottom(11.5f);
            cell.setFixedHeight(40);
        } else if (boderFlag) {
            cell.setBorder(0);
            cell.setPaddingTop(0.0f);
            cell.setPaddingBottom(11.5f);
        }
        return cell;
    }

    /**
     * 创建指定列宽、列数的表格
     *
     * @param widths
     * @return
     */
    public PdfPTable createTable(float[] widths) {
        PdfPTable table = new PdfPTable(widths);
        try {
            table.setTotalWidth(maxWidth);
            table.setLockedWidth(true);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setBorder(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }

    //右测虚线
    static class CustomCellRight implements PdfPCellEvent {
        public void cellLayout(PdfPCell cell, Rectangle position,
                               PdfContentByte[] canvases) {
            PdfContentByte cb = canvases[PdfPTable.LINECANVAS];
            cb.saveState();
            cb.setLineWidth(0.5f);
            cb.setLineDash(new float[]{1.5f, 1.5f}, 0);
            cb.setRGBColorStroke(156, 166, 184);
            cb.moveTo(position.getRight(), position.getTop());
            cb.lineTo(position.getRight(), position.getBottom());
            cb.stroke();
            cb.restoreState();
        }
    }

    //虚线格式 底部
    static class CustomCellBottom implements PdfPCellEvent {
        public void cellLayout(PdfPCell cell, Rectangle position,
                               PdfContentByte[] canvases) {
            // TODO Auto-generated method stub
            PdfContentByte cb = canvases[PdfPTable.LINECANVAS];
            cb.saveState();
            cb.setLineWidth(0.5f);
            cb.setLineDash(new float[]{1.5f, 1.5f}, 0);
            cb.setRGBColorStroke(156, 166, 184);
            cb.moveTo(position.getLeft(), position.getBottom());
            cb.lineTo(position.getRight(), position.getBottom());
            cb.stroke();
            cb.restoreState();


        }
    }

    //    底部实线
    static class CustomCellSolidBottom implements PdfPCellEvent {
        public void cellLayout(PdfPCell cell, Rectangle position,
                               PdfContentByte[] canvases) {
            PdfContentByte cb = canvases[PdfPTable.LINECANVAS];
            cb.saveState();
            cb.setLineWidth(0.5f);
            cb.moveTo(position.getLeft(), position.getBottom());
            cb.lineTo(position.getRight(), position.getBottom());
            cb.stroke();
            cb.restoreState();
        }
    }

    //    左侧实线
    static class CustomCellSolidLeft implements PdfPCellEvent {
        public void cellLayout(PdfPCell cell, Rectangle position,
                               PdfContentByte[] canvases) {
            PdfContentByte cb = canvases[PdfPTable.LINECANVAS];
            cb.saveState();
            cb.setLineWidth(0.5f);
            cb.moveTo(position.getLeft(), position.getTop());
            cb.lineTo(position.getLeft(), position.getBottom());
            cb.stroke();
            cb.restoreState();
        }
    }

/**------------------------创建表格单元格的方法end----------------------------*/


/**--------------------------创建表格的方法start------------------- ---------*/

    //    右侧实线
    static class CustomCellSolidRight implements PdfPCellEvent {
        public void cellLayout(PdfPCell cell, Rectangle position,
                               PdfContentByte[] canvases) {
            PdfContentByte cb = canvases[PdfPTable.LINECANVAS];
            cb.saveState();
            cb.setLineWidth(0.5f);
            cb.moveTo(position.getRight(), position.getTop());
            cb.lineTo(position.getRight(), position.getBottom());
            cb.stroke();
            cb.restoreState();
        }
    }
/**--------------------------创建表格的方法end------------------- ---------*/


}
