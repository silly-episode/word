package com.boot.utils;

import com.boot.entity.BookOfWords;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/25 16:11
 * @FileName: PdfUtils
 * @Description:
 */

@Component
@SuppressWarnings("all")
public class PdfUtils {

    // 字体
    private static Font topicfont;
    private static Font titlefont;
    private static Font headfont;
    private static Font keyfont;
    private static Font textfont;
    private static Font footerfoot;
    // 最大宽度
    private static int maxWidth = 530;

//单元格类型
    private  enum CellType {
        FinalyCell,
        NoTitle,WordOrMeaningTitle,BlankTitle,
        NoCell, WordOrMeaningCell,BlankCell
    }


    // 字体静态代码块
    static {
        try {
//            可兼顾中文的windows系统字体
            String frontPath = new ClassPathResource("static/fonts/simsun.ttc").getPath();
            BaseFont chinese = BaseFont.createFont(frontPath+",1", BaseFont.IDENTITY_H , BaseFont.EMBEDDED);
//            idea的字体
            BaseFont idea = BaseFont.createFont("static/fonts/JetBrainsMono-Light.ttf", BaseFont.IDENTITY_H , BaseFont.EMBEDDED);
//            花里胡哨字体
            BaseFont xieTi = BaseFont.createFont("static/fonts/Candarali.ttf", BaseFont.IDENTITY_H , BaseFont.EMBEDDED);
//            字体样式
            topicfont = new Font(xieTi, 30, Font.BOLD);
            headfont = new Font(chinese, 15, Font.BOLD);
            keyfont = new Font(idea, 8, Font.NORMAL);
            titlefont = new Font(idea, 10, Font.BOLD);
            textfont = new Font(chinese, 7, Font.NORMAL);
            footerfoot = new Font(chinese, 10, Font.BOLD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //右测虚线
    static class CustomCellRight implements PdfPCellEvent {
        @Override
        public  void cellLayout(PdfPCell cell, Rectangle position,
                                PdfContentByte[] canvases) {
            PdfContentByte cb = canvases[PdfPTable.LINECANVAS];
            cb.saveState();
            cb.setLineWidth(0.5f);
            cb.setLineDash(new float[] { 1.5f, 1.5f }, 0);
            cb.setRGBColorStroke(156, 166, 184);
            cb.moveTo(position.getRight(), position.getTop());
            cb.lineTo(position.getRight(),position.getBottom());
            cb.stroke();
            cb.restoreState();
        }
    }
    //底部虚线
    static class CustomCellBottom implements PdfPCellEvent {
        @Override
        public void cellLayout(PdfPCell cell, Rectangle position,
                               PdfContentByte[] canvases) {
            // TODO Auto-generated method stub
            PdfContentByte cb = canvases[PdfPTable.LINECANVAS];
            cb.saveState();
            cb.setLineWidth(0.5f);
            cb.setLineDash(new float[] { 1.5f, 1.5f }, 0);
            cb.setRGBColorStroke(156, 166, 184);
            cb.moveTo(position.getLeft(), position.getBottom());
            cb.lineTo(position.getRight(), position.getBottom());
            cb.stroke();
            cb.restoreState();


        }
    }
    //底部实线
    static class CustomCellSolidBottom implements PdfPCellEvent {
        @Override
        public  void cellLayout(PdfPCell cell, Rectangle position,
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
    //左侧实线
    static class CustomCellSolidLeft implements PdfPCellEvent {
        @Override
        public  void cellLayout(PdfPCell cell, Rectangle position,
                                PdfContentByte[] canvases) {
            PdfContentByte cb = canvases[PdfPTable.LINECANVAS];
            cb.saveState();
            cb.setLineWidth(0.5f);
            cb.moveTo(position.getLeft(), position.getTop());
            cb.lineTo(position.getLeft(),position.getBottom());
            cb.stroke();
            cb.restoreState();
        }
    }
    //右侧实线
    static class CustomCellSolidRight implements PdfPCellEvent {
        @Override
        public  void cellLayout(PdfPCell cell, Rectangle position,
                                PdfContentByte[] canvases) {
            PdfContentByte cb = canvases[PdfPTable.LINECANVAS];
            cb.saveState();
            cb.setLineWidth(0.5f);
            cb.moveTo(position.getRight(), position.getTop());
            cb.lineTo(position.getRight(),position.getBottom());
            cb.stroke();
            cb.restoreState();
        }
    }

    /*--------------------------创建表格的方法start------------------- ---------*/
    /**
     * @param widths: 各个单元格的所占长度
     * @Return: PdfPTable
     * @Author: DengYinzhe
     * @Description: 创建指定列宽、列数的表格
     * @Date: 2023/2/27 17:10
     */
    public PdfPTable createTable(float[] widths) {
        for (float v : widths) {
            v = v * maxWidth;
        }
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
    /*--------------------------创建表格的方法end------------------- ---------*/



    /*------------------------创建表格单元格的方法start----------------------------*/

    /**
     * @param value:
     * @param font:
     * @param type:
     * @Return: PdfPCell
     * @Author: DengYinzhe
     * @Description: 绘制不同类型的单元格
     * @Date: 2023/2/27 17:47
     */
    public PdfPCell createCell(String value, Font font, CellType type, Boolean Finalytype) {

        PdfPCell cell = new PdfPCell();
        cell.setFixedHeight(25);
        cell.setPaddingLeft(7);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        if (type.equals(CellType.NoTitle)) {
            cell.setFixedHeight(30);
//            顶部、左侧、底部实线；右侧虚线
            cell.setBorder(Rectangle.TOP);
            cell.setCellEvent(new CustomCellRight());
            cell.setCellEvent(new CustomCellSolidBottom());
            cell.setCellEvent(new CustomCellSolidLeft());
        } else if (type.equals(CellType.WordOrMeaningTitle)) {
            cell.setFixedHeight(30);
//            顶部，底部实线；右侧虚线
            cell.setBorder(Rectangle.TOP);
            cell.setCellEvent(new CustomCellRight());
            cell.setCellEvent(new CustomCellSolidBottom());
        } else if (type.equals(CellType.BlankTitle)) {
            cell.setFixedHeight(30);
//            顶部，底部，右侧实现；
            cell.setBorder(Rectangle.TOP);
            cell.setCellEvent(new CustomCellSolidRight());
            cell.setCellEvent(new CustomCellSolidBottom());
            cell.setPaddingLeft(2);
        } else if (type.equals(CellType.NoCell)) {
//             左边实线；右侧、底部虚线
            cell.setBorder(Rectangle.LEFT);
            cell.setCellEvent(new CustomCellRight());
            cell.setCellEvent(new CustomCellBottom());
        } else if (type.equals(CellType.WordOrMeaningCell)) {
//            右侧、底部虚线
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setCellEvent(new CustomCellRight());
            cell.setCellEvent(new CustomCellBottom());
        } else if (type.equals(CellType.BlankCell)) {
//            右侧实线，底部虚线
            cell.setBorder(Rectangle.RIGHT);
            cell.setCellEvent(new CustomCellBottom());
            cell.setPaddingLeft(2);
        }
        if (Finalytype) {
            cell.setCellEvent(new CustomCellSolidBottom());
        }
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
            cell.setPaddingTop(16.5f);
            cell.setPaddingBottom(16.5f);
            cell.setFixedHeight(50);
        } else if (boderFlag) {
            cell.setBorder(0);
            cell.setPaddingTop(0.0f);
            cell.setPaddingBottom(11.5f);
            cell.setFixedHeight(5);
        }
        return cell;
    }

    /*------------------------创建表格单元格的方法end----------------------------*/

    /*------------------------定义表头的方法begin----------------------------*/

    public  PdfPTable createHead(){
        Boolean Finalytype = false;
        // 8个单元格所占比例
        PdfPTable table = createTable(new float[] { 0.069f, 0.152f, 0.236f, 0.04f, 0.069f, 0.152f, 0.236f, 0.04f });
//        表格上方信息
        table.addCell(createCell("Title: "+"收藏的单词", headfont, Element.ALIGN_LEFT, 6, false));
        table.addCell(createCell("Date:    /    /  ", headfont, Element.ALIGN_LEFT, 4, false));

//        表头
        table.addCell(createCell("No.", titlefont,CellType.NoTitle,Finalytype));
        table.addCell(createCell("Word", titlefont,CellType.WordOrMeaningTitle,Finalytype));
        table.addCell(createCell("Meaning", titlefont,CellType.WordOrMeaningTitle,Finalytype));
        table.addCell(createCell(" √ ", titlefont,CellType.BlankTitle,Finalytype));

        table.addCell(createCell("No.", titlefont,CellType.NoTitle,Finalytype));
        table.addCell(createCell("Word", titlefont,CellType.WordOrMeaningTitle,Finalytype));
        table.addCell(createCell("Meaning", titlefont,CellType.WordOrMeaningTitle,Finalytype));
        table.addCell(createCell(" √ ", titlefont,CellType.BlankTitle,Finalytype));
        return table;
    }


    /*------------------------定义表头的方法end----------------------------*/

    /*------------------------插入数据的方法begin----------------------------*/

    public void generatePDF(Document document,List<BookOfWords> bookOfWordsList) throws Exception {
//        页眉
        Chunk headerChunk = new Chunk("Word  List");
        // 设置字体，字体定宽
        headerChunk.setFont(topicfont);
        // 设置背景颜色
        headerChunk.setBackground(new BaseColor(255, 255, 255));
//        headerChunk.setLineHeight(5);
        Paragraph headerChunkParagraph = new Paragraph(headerChunk);
        headerChunkParagraph.setAlignment(Element.ALIGN_CENTER);

//        int totalWord = bookOfWordsList.size();
        int totalWord = 100;
        int totalPage = (int) Math.ceil(totalWord / 50.0);
        System.out.println(bookOfWordsList.size());
        int currentPage = 0;
        Boolean Finalytype = false;
        PdfPTable  table= createHead();
        int i = 0;
        for (int j = 0; j < totalWord-1; j+=2) {
            i+=2;
//            当页结束时，设置最下方的单元格的底部为实线
            if ((i)%50==0 || (j!=0&&j==totalWord-1)){
                Finalytype = true;
            } else {
                Finalytype = false;
            }
            table.addCell(createCell(String.valueOf(j+1), keyfont,CellType.NoCell,Finalytype));
            table.addCell(createCell(bookOfWordsList.get(j).getWord(), keyfont,CellType.WordOrMeaningCell,Finalytype));
            table.addCell(createCell(bookOfWordsList.get(j).getMeaning(), textfont,CellType.WordOrMeaningCell,Finalytype));
            table.addCell(createCell("", keyfont,CellType.BlankCell,Finalytype));

            table.addCell(createCell(String.valueOf(j+2), keyfont,CellType.NoCell,Finalytype));
            table.addCell(createCell(bookOfWordsList.get(j+1).getWord(), keyfont,CellType.WordOrMeaningCell,Finalytype));
            table.addCell(createCell(bookOfWordsList.get(j+1).getMeaning(), textfont,CellType.WordOrMeaningCell,Finalytype));
            table.addCell(createCell("", keyfont, CellType.BlankCell,Finalytype));
//            每页50个单词或总页不足50个单词结束当前页
                if ((i)%50==0 || (j!=0&&j==totalWord-1)){
                    i = 0;

                    Chunk chunkFooter = new Chunk(String.format("第 %d 页/共 %d 页",++currentPage,totalPage));
                    chunkFooter.setFont(footerfoot);
                    chunkFooter.setBackground(new BaseColor(255, 255, 255));
                    chunkFooter.setLineHeight(10);
                    Paragraph footerParagraph = new Paragraph(chunkFooter);

                    footerParagraph.setAlignment(Element.ALIGN_CENTER);

                    document.add(headerChunkParagraph);
                    document.add(table);
                    document.add(footerParagraph);
                    document.newPage();
                    table = createHead();
                }


        }




    }
    /*------------------------插入数据的方法end----------------------------*/



    public  void pdfExport(List<BookOfWords> bookOfWordsList) throws Exception {
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
            generatePDF(document,bookOfWordsList);

            // 5.关闭文档
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
