package top.fluffcotton.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: ExcelUtils
 * @Description: excel工具
 * @date 2020.08.31 20:43
 */
@Component
public class ExcelUtils {

    /**
     * 昵称
     */
    public static final int NAME = 0;
    /**
     * qq
     */
    public static final int PRIMARY_KEY = 1;
    /**
     * 完成标至
     */
    public static final int FINISH_FLAG = 2;
    private static ExcelUtils excelUtils = null;
    /**
     * 运行根目录
     */
    private String groupTaskListURL;
    /**
     * 任务对应的名单
     */
    private Map<String, List<Map<Integer, String>>> groupTaskList = new HashMap<>(5);
    /**
     * 任务名单对应表头
     */
    private List<List<String>> headList;

    private ExcelUtils() {
        groupTaskListURL = Objects.requireNonNull(ExcelUtils.class.getClassLoader().getResource("")).getPath();
    }

    public synchronized static ExcelUtils getExcelUtils(){
        if(excelUtils == null){
            excelUtils = new ExcelUtils();
        }
        return excelUtils;
    }

    /**
     * 根据读取到的excel 获取写出的数据
     *
     * @param List 读取到的excel
     * @return 写出的list
     * @Title getExcelDataList
     * @Description 根据读取到的excel 获取写出的数据
     * @author 张逸辰
     * @Date 2020/8/12 22:10
     */
    public synchronized List<List<String>> getExcelDataList(List<Map<Integer, String>> List) {
        List<List<String>> list = new ArrayList<>();
        for (Map<Integer, String> l : List) {
            List<String> data = new ArrayList<>();
            l.forEach((i, s) -> data.add(s));
            list.add(data);
        }
        return list;
    }

    /**
     * 根据任务获取用户名单
     *
     * @param m_ID 任务
     * @param fileUrl 文件地址
     * @return 用户名单
     * @Title getUserList
     * @Description 根据任务获取用户名单
     * @author 张逸辰
     * @Date 2020/8/12 22:11
     */
    public List<Map<Integer, String>> getUserList(String m_ID, String fileUrl) {
        synchronized (ExcelUtils.class){
            //没有列表则读取
            if (groupTaskList.get(m_ID) == null) {
                File file = new File(fileUrl);
                if(!file.exists() || !file.isFile()){
                    return null;
                }
                //读取列表
                EasyExcel.read(fileUrl, new AnalysisEventListener<Map<Integer, String>>() {
                    final List<Map<Integer, String>> list = new ArrayList<>();

                    @Override
                    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
                        //直接读取表头
                        if (headList != null) {
                            return;
                        }
                        headList = new ArrayList<>();
                        headMap.forEach((index, title) -> {
                            List<String> head = new ArrayList<>();
                            head.add(title);
                            headList.add(head);
                        });
                    }

                    @Override
                    public void invoke(Map<Integer, String> data, AnalysisContext context) {
                        //保存任务列表
                        list.add(data);
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext context) {
                        //储存
                        groupTaskList.put(m_ID, list);
                    }
                }).sheet().doRead();
            }
        }

        return groupTaskList.get(m_ID);
    }

    public List<List<String>> getHeadList() {
        return headList;
    }

    public void setHeadList(List<List<String>> headList) {
        this.headList = headList;
    }

    public String getGroupTaskListURL() {
        return groupTaskListURL;
    }

    public void setGroupTaskListURL(String groupTaskListURL) {
        this.groupTaskListURL = groupTaskListURL;
    }

    public Map<String, List<Map<Integer, String>>> getGroupTaskList() {
        return groupTaskList;
    }

    public void setGroupTaskList(Map<String, List<Map<Integer, String>>> groupTaskList) {
        this.groupTaskList = groupTaskList;
    }
}
