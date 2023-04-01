package com.boot.common.ExcelImport;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.util.ListUtils;
import com.boot.entity.EmotionWords;
import com.boot.service.EmotionWordsService;
import com.boot.utils.ThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/3/31 13:26
 * @FileName: EmotionWordsImport
 * @Description: 激励语导入
 */
@Slf4j
@Component
public class EmotionWordsImport extends AnalysisEventListener<EmotionWords> {


    /**
     * 每隔100条存储数据库，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    @Resource
    private EmotionWordsService emotionWordsService;
    /**
     * 缓存的数据
     */
    private List<EmotionWords> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    /**
     * 录入错误信息
     */
    private List<String> errorList = new ArrayList<>();

    /**
     * 表头信息
     */
    private Map<Integer, String> headMap;

    /**
     * @param headMap:
     * @param context:
     * @Return: void
     * @Author: DengYinzhe
     * @Description: 读取表头中的内容
     * @Date: 2023/3/16 15:03
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        this.headMap = headMap;
    }

    /**
     * @param data:
     * @param context:
     * @Return: void
     * @Author: DengYinzhe
     * @Description: 这个每一条数据解析都会来调用
     * @Date: 2023/3/16 15:02
     */
    @Override
    public void invoke(EmotionWords data, AnalysisContext context) {
        LocalDateTime localDateTime = LocalDateTime.now();
        data.setEmoCreateTime(localDateTime);
        cachedDataList.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            emotionWordsService.saveBatch(cachedDataList);
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * @param context:
     * @Return: void
     * @Author: DengYinzhe
     * @Description: 所有数据解析完成了 都会来调用
     * @Date: 2023/3/16 15:03
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
//        //插入数据库
//        System.out.println(cachedDataList);
//        如果excel中的数据或最后一次的数据小于缓存size，则在这里插入
        emotionWordsService.saveBatch(cachedDataList);

        ThreadLocalUtils.set("errorMsg", errorList);
    }

    /**
     * @param exception:
     * @param context:
     * @Return: void
     * @Author: DengYinzhe
     * @Description: 监听器实现这个方法就可以在读取数据的时候获取到异常信息
     * @Date: 2023/3/16 15:03
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {
        // 如果是某一个单元格的转换异常 能获取到具体行号
        // 如果要获取头的信息 配合invokeHeadMap使用
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
            errorList.add(String.format(
                    "<%s>列的第%d行解析异常",
                    headMap.get(excelDataConvertException.getColumnIndex()),
                    excelDataConvertException.getRowIndex()
            ));

        }
    }

}
