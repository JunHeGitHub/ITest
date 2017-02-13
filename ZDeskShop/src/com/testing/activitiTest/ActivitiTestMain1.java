package com.testing.activitiTest;

import java.util.HashMap;
import java.util.List;  
import java.util.Map;
  
import org.activiti.engine.HistoryService;  
import org.activiti.engine.ProcessEngine;  
import org.activiti.engine.ProcessEngineConfiguration;  
import org.activiti.engine.RepositoryService;  
import org.activiti.engine.RuntimeService;  
import org.activiti.engine.TaskService;  
import org.activiti.engine.history.HistoricProcessInstance;  
import org.activiti.engine.task.Task;  
  
public class ActivitiTestMain1 {  
  
	/**
	 * 指定用户或用户组，由变量决定用户或组
	 * @param args
	 */
    public static void main(String[] args) {  
        //加载配置文件  
        ProcessEngine processEngine = ProcessEngineConfiguration  
                .createProcessEngineConfigurationFromResource("appConf/activiti.cfg.xml")  
                .buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        RuntimeService runtimeService = processEngine.getRuntimeService();  
        repositoryService.createDeployment()
           .addClasspathResource("activitiDefineds/activitiTest1.bpmn")  
            .deploy();
        
//        //指定用户
//        String user="admin";
//        //指定用户组
//        String group="ADMIN_GROUP";
//        Map map=new HashMap();
//        map.put("T_USER", user);
//        String processId = runtimeService.startProcessInstanceByKey("myProcess",map).getId();  
//        
//        TaskService taskService = processEngine.getTaskService();  
//        HistoryService historyService = processEngine.getHistoryService();  
//        //得到笔试的流程
//        List<Task> tasks = taskService.createTaskQuery().taskAssignee(user).list();
//        System.out.println(user + "的任务数量："+taskService.createTaskQuery().taskAssignee(user).count());  
//        for (Task task : tasks) {
//        	map=new HashMap();
//            map.put("T_GROUP", "ADMIN_GROUP");
//            taskService.complete(task.getId(), map);
//        }  
////        System.out.println(group + "任务数量："+taskService.createTaskQuery().taskCandidateGroup(group).count());  
////        tasks = taskService.createTaskQuery().taskCandidateGroup(group).list();  
////        for (Task task : tasks) {
////        	//获取task中的变量
////        	map=taskService.getVariables(task.getId());
////        	System.out.println("T_GROUP：" + map.get("T_GROUP"));
////        	taskService.claim(task.getId(), user);
////        	taskService.complete(task.getId());
////        }
////        
////        HistoricProcessInstance historicProcessInstance = historyService
////                .createHistoricProcessInstanceQuery()
////                .processInstanceId(processId).singleResult();
////        System.out.println("流程结束时间："+historicProcessInstance.getEndTime());
        System.exit(0);  
    }  
}  