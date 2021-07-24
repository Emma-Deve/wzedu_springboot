package com.wuze.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-20 23:10:55
 */
public class ExcelListener extends AnalysisEventListener<DemoData> {

    //一行一行读取excel内容
    @Override
    public void invoke(DemoData demoData, AnalysisContext analysisContext) {
        System.out.println("----"+demoData);
    }

    //读取表头内容
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头："+headMap);
    }

    //读取完成之后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("读取完成");
    }
}
