package top.fluffcotton.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @Description 管理任务调度
 * @Author zyc
 * @Date 2020/12/2 11:10
 */
public class TimeJob {

    private static TimeJob timeJob;
    private final ScheduledExecutorService service;

    private TimeJob(){
         service = Executors.newScheduledThreadPool(10);
    }
    private TimeJob(int size){
        service = Executors.newScheduledThreadPool(size);
    }

    /**
     * 获取timejob
     * @return 定时器
     */
    public static TimeJob getTimeJob(){
        if(timeJob == null){
            synchronized (TimeJob.class){
                timeJob = new TimeJob();
            }
        }
        return timeJob;
    }

    /**
     * 实现runnable接口
     * @param runnable 执行任务
     * @param startTime 开始时间
     * @param replyTime 回复时间
     */
    public ScheduledFuture<?> start(Runnable runnable,long startTime,long replyTime){
        ScheduledFuture<?> schedule;
        if(replyTime <= 0){
            schedule = service.schedule(()->{
                try{
                    runnable.run();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }, Math.max(startTime - System.currentTimeMillis(), 0), TimeUnit.MILLISECONDS);
        }else{
            schedule = service.scheduleAtFixedRate(()->{
                try{
                    runnable.run();
                }catch (Exception e){
                    e.printStackTrace();
                }
            },Math.max(startTime - System.currentTimeMillis(),0),replyTime,TimeUnit.MILLISECONDS);
        }
        return schedule;
    }
}
