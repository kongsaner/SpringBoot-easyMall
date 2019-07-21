package com.jt.order.job;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.jt.order.mapper.OrderMapper;


public class OrderOTJob extends QuartzJobBean{
	/*
	 * 实现父类的未实现方法,定时执行逻辑
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		//context当前一个环境变量,定时任务的所有环境信息
		//获取spring的上下文对象
		ApplicationContext spring=(ApplicationContext) 
				context.getJobDetail().getJobDataMap().get("applicationContext");
		//利用spring上下文对象,获取mapper的实现对象
		System.out.println("执行定时删除");
		spring.getBean(OrderMapper.class).
		deleteOTOrder(new Date(new Date().getTime()-1000*60*60*24));
		System.out.println("执行定时完毕");
	}

}
