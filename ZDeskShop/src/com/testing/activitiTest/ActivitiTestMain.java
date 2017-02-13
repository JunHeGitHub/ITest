package com.testing.activitiTest;

import java.util.List;  
  
import org.activiti.engine.HistoryService;  
import org.activiti.engine.ProcessEngine;  
import org.activiti.engine.ProcessEngineConfiguration;  
import org.activiti.engine.RepositoryService;  
import org.activiti.engine.RuntimeService;  
import org.activiti.engine.TaskService;  
import org.activiti.engine.history.HistoricProcessInstance;  
import org.activiti.engine.task.Task;  
  
public class ActivitiTestMain {  
  
	/**
	 * 简单顺序流程，流程配置中指定了处理的组
	 * @param args
	 */
    //本流程模拟的是某公司2012年实习生招聘流程  
    //张三来参加该招聘流程，公司相关负责人负责处理流程  
    public static void main(String[] args) {  
        //加载配置文件  
//        ProcessEngine processEngine = ProcessEngineConfiguration  
//                .createProcessEngineConfigurationFromResource("appConf/activiti.cfg.xml")  
//                .buildProcessEngine();  
//        RepositoryService repositoryService = processEngine.getRepositoryService();  
//        RuntimeService runtimeService = processEngine.getRuntimeService();  
//        repositoryService.createDeployment()  
//           .addClasspathResource("activitiDefineds/activitiTest1.bpmn")  
//           .deploy();
//        String processId = runtimeService.startProcessInstanceByKey("Interview").getId();  
//        
//        TaskService taskService = processEngine.getTaskService();  
//        HistoryService historyService = processEngine.getHistoryService();  
//        //得到笔试的流程  
//        System.out.println("***************笔试流程开始***************");  
//        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("人力资源部").list();
//        for (Task task : tasks) {  
//            System.out.println("人力资源部的任务：name:"+task.getName()+",id:"+task.getId());  
//            taskService.claim(task.getId(), "张三");
//        }  
//        System.out.println("张三的任务数量："+taskService.createTaskQuery().taskAssignee("张三").count());  
//        tasks = taskService.createTaskQuery().taskAssignee("张三").list();  
//        for (Task task : tasks) {  
//            System.out.println("张三的任务：name:"+task.getName()+",id:"+task.getId()); 
//            taskService.complete(task.getId());
//        }  
//        System.out.println("张三的任务数量："+taskService.createTaskQuery().taskAssignee("张三").count());  
//        System.out.println("***************笔试流程结束***************");  
//          
//        System.out.println("***************一面流程开始***************");  
//        tasks = taskService.createTaskQuery().taskCandidateGroup("技术部").list();  
//        for (Task task : tasks) {  
//            System.out.println("技术部的任务：name:"+task.getName()+",id:"+task.getId());  
//            taskService.claim(task.getId(), "李四");  
//        }  
//        System.out.println("李四的任务数量："+taskService.createTaskQuery().taskAssignee("李四").count());  
//        for (Task task : tasks) {  
//            System.out.println("李四的任务：name:"+task.getName()+",id:"+task.getId());  
//            taskService.complete(task.getId());  
//        }  
//        System.out.println("李四的任务数量："+taskService.createTaskQuery().taskAssignee("李四").count());  
//        System.out.println("***************一面流程结束***************");  
          
//        System.out.println("***************二面流程开始***************");  
//        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("技术部").list();  
//        for (Task task : tasks) {  
//            System.out.println("技术部的任务：name:"+task.getName()+",id:"+task.getId());  
//            taskService.claim(task.getId(), "李四");  
//        }  
//        System.out.println("李四的任务数量："+taskService.createTaskQuery().taskAssignee("李四").count());  
//        for (Task task : tasks) {  
//            System.out.println("李四的任务：name:"+task.getName()+",id:"+task.getId());  
//            taskService.complete(task.getId());  
//        }  
//        System.out.println("李四的任务数量："+taskService.createTaskQuery().taskAssignee("李四").count());  
//        System.out.println("***************二面流程结束***************");  
//          
//        System.out.println("***************HR面流程开始***************");  
//        tasks = taskService.createTaskQuery().taskCandidateGroup("人力资源部").list();  
//        for (Task task : tasks) {  
//            System.out.println("技术部的任务：name:"+task.getName()+",id:"+task.getId());  
//            taskService.claim(task.getId(), "李四");  
//        }  
//        System.out.println("李四的任务数量："+taskService.createTaskQuery().taskAssignee("李四").count());  
//        for (Task task : tasks) {  
//            System.out.println("李四的任务：name:"+task.getName()+",id:"+task.getId());  
//            taskService.complete(task.getId());  
//        }  
//        System.out.println("李四的任务数量："+taskService.createTaskQuery().taskAssignee("李四").count());  
//        System.out.println("***************HR面流程结束***************");  
//          
//        System.out.println("***************录用流程开始***************");  
//        tasks = taskService.createTaskQuery().taskCandidateGroup("人力资源部").list();  
//        for (Task task : tasks) {  
//            System.out.println("技术部的任务：name:"+task.getName()+",id:"+task.getId());  
//            taskService.claim(task.getId(), "李四");  
//        }  
//        System.out.println("李四的任务数量："+taskService.createTaskQuery().taskAssignee("李四").count());  
//        for (Task task : tasks) {  
//            System.out.println("李四的任务：name:"+task.getName()+",id:"+task.getId());  
//            taskService.complete(task.getId());  
//        }  
//        System.out.println("李四的任务数量："+taskService.createTaskQuery().taskAssignee("李四").count());  
//        System.out.println("***************录用流程结束***************");  
//          
//          
//        HistoricProcessInstance historicProcessInstance = historyService
//                .createHistoricProcessInstanceQuery()  
//                .processInstanceId(processId).singleResult();  
//        System.out.println("流程结束时间："+historicProcessInstance.getEndTime());  
        System.exit(0);  
    }  
}  