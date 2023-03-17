package com.boot;

import com.boot.entity.BookOfWords;
import com.boot.service.BookOfWordsService;
import com.boot.utils.PdfUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/27 17:57
 * @FileName: PdfTest
 * @Description:
 */
@SpringBootTest
public class PdfTest {


    @Resource
    private PdfUtils pdfUtils;

    @Resource
    private BookOfWordsService bookOfWordsService;

    @Test
    public void test() throws Exception {

        Long bookId = 1L;

        Map<String, Object> map = new HashMap<>();
        map.put("book_id", bookId);

        List<BookOfWords> bookOfWords = bookOfWordsService.listByMap(map);
        pdfUtils.pdfExport(bookOfWords);
    }
}
