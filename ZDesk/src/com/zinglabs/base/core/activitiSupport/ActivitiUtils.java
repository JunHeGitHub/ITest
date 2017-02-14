package com.zinglabs.base.core.activitiSupport;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricFormProperty;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zinglabs.util.DateUtil;
import com.zinglabs.util.StringUtils;

/**
 * 发部文件时乱码，在-Dsun.jnu.encoding=UTF-8 -Dfile.encoding=UTF-8
 * @author QCF
 *
 */
@SuppressWarnings("unchecked")
public class ActivitiUtils extends ActivitiService{

	public static Logger logger = LoggerFactory.getLogger("Activiti");
	private final static String ACTIVITI_START_FLAG="startEvent";
	private final static String ACTIVITI_END_FLAG="endEvent";
	/**
     * 根据任务ID获取对应的流程实例 
     * @param taskId 任务ID 
     * @return 
     * @throws Exception 
     */
    public ProcessInstance findProcessInstanceByTaskId(String taskId)  
            throws Exception { 
    	logger.debug("do_findProcessInstanceByTaskId_taskId:"+taskId);
        // 找到流程实例
        ProcessInstance processInstance = getRuntimeService()  
                .createProcessInstanceQuery().processInstanceId(findTaskById(taskId).getProcessInstanceId())  
                .singleResult();  
        if (processInstance == null) {   
            throw new Exception("流程实例未找到!");  
        }  
        logger.debug("finish_findProcessInstanceByTaskId_processInstance:"+processInstance.getId());
        return processInstance;  
    }
  
    /** 
     * 根据任务ID获得任务实例 Task 使用
     * @param taskId 任务ID 
     * @return 
     * @throws Exception 
     */  
    public TaskEntity findTaskById(String taskId) throws Exception {  
        TaskEntity task = (TaskEntity) getTaskService().createTaskQuery().taskId(taskId).singleResult();  
        if (task == null) {  
            throw new Exception("任务实例未找到!");  
        }
        return task;  
    }
	/**
	 * 查找流程节点 task.getTaskDefinitionKey() = activityId
	 * @param processDefinitionId
	 * @param activityId
	 * @return
	 */
    public ActivityImpl findActivityImpl(String processDefinitionId,String activityId){
    	ProcessDefinitionEntity def = (ProcessDefinitionEntity)((RepositoryServiceImpl)getRepositoryService()).getDeployedProcessDefinition(processDefinitionId);
    	return def.findActivity(activityId);
    }
    
	/**
	 * 获取流程全部节点
	 * @param processDefinitionId 流程的定义Id
	 * @param noStart 不包含开始事件节点
	 * @param noEnd 不包含结束事件节点
	 * @return
	 */
	public List<ActivityImpl> getProcessAllNodes(String processDefinitionId,boolean noStart,boolean noEnd) throws Exception{
		List<ActivityImpl> list;
		logger.debug("do_getProcessAllNodes_processDefinitionId:"+processDefinitionId);
		ProcessDefinitionEntity def = (ProcessDefinitionEntity)((RepositoryServiceImpl)getRepositoryService()).getDeployedProcessDefinition(processDefinitionId);
		list=def.getActivities();
		if(noStart || noEnd){
			for(int i=list.size()-1;i>=0;i--){
				ActivityImpl ai=list.get(i);
				String type=(String)ai.getProperty("type");
				if(type.equals(ACTIVITI_START_FLAG) && noStart){
					list.remove(i);
				}else if(type.equals(ACTIVITI_END_FLAG) && noEnd){
					list.remove(i);
				}
			}
		}
		logger.debug("finish_getProcessAllNodes_processDefinitionId:"+list);
		return list;
	}
	
	/**
	 * 获取开始节点
	 * @param processDefinitionKey
	 * @return
	 */
	public ActivityImpl getProcessStartNode(String processDefinitionKey)throws Exception{
		logger.debug("do_getProcessStartNode_processDefinitionKey:"+processDefinitionKey);
		String processDefinitionId=getRepositoryService().createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).latestVersion().singleResult().getId();
		List<ActivityImpl> list;
		ProcessDefinitionEntity def = (ProcessDefinitionEntity)((RepositoryServiceImpl)getRepositoryService()).getDeployedProcessDefinition(processDefinitionId);
		list=def.getActivities();
		for(int i=list.size()-1;i>=0;i--){
			ActivityImpl ai=list.get(i);
			String type=(String)ai.getProperty("type");
			if(type.equals(ACTIVITI_START_FLAG)){
				return ai;
			}
		}
		logger.debug("finish_getProcessStartNode_processDefinitionKey:"+processDefinitionKey);
		return null;
	}
	
	/**
	 * 获取结束节点
	 * @param processDefinitionKey
	 * @return
	 */
	public ActivityImpl getProcessEndNode(String processDefinitionKey)throws Exception{
		logger.debug("do_getProcessEndNode_processDefinitionKey:"+processDefinitionKey);
		String processDefinitionId=getRepositoryService().createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).latestVersion().singleResult().getId();
		List<ActivityImpl> list;
		ProcessDefinitionEntity def = (ProcessDefinitionEntity)((RepositoryServiceImpl)getRepositoryService()).getDeployedProcessDefinition(processDefinitionId);
		list=def.getActivities();
		for(int i=list.size()-1;i>=0;i--){
			ActivityImpl ai=list.get(i);
			String type=(String)ai.getProperty("type");
			if(type.equals(ACTIVITI_END_FLAG)){
				return ai;
			}
		}
		logger.debug("finish_getProcessEndNode_processDefinitionKey:"+processDefinitionKey);
		return null;
	}
	
	/**
	 * 获取流程当前节点的下一级节点
	 * @param task 当前执行的任务
	 * @return
	 * @throws Exception
	 */
	public List<PvmTransition> getProcessNextNode(Task task)throws Exception{
		ActivityImpl ai=getProcessNowNode(task);
		if(ai!=null){
			return ai.getOutgoingTransitions();
		}
		return new ArrayList<PvmTransition>();
	}
	
	/**
	 * 获取流程当前节点的上一级节点集合
	 * @return
	 * @throws Exception
	 */
	public List<ActivityImpl> getProcessNextNodeList(Task task)throws Exception{
		logger.debug("do_getProcessNextNodeList_task:"+task.getId()+"taskName"+task.getName());
		List<ActivityImpl> list=getProcessAllNodes(task.getProcessDefinitionId(), true, true);
		List<PvmTransition> listp=getProcessNextNode(task);
		boolean badd=true;
		for(PvmTransition pt:listp){
			String id=(String)pt.getSource().getId();
			String type="";
			if(type.equals(ACTIVITI_START_FLAG)){
				
			}else if(type.equals(ACTIVITI_END_FLAG)){
				list=new ArrayList();
			}else{
				for(int i=list.size()-1;i>=0;i--){
					ActivityImpl aii=list.get(i);
					//System.out.println(id + "  " + aii.getId());
					if(id.equals(aii.getId())){
						badd=false;
					}
					if(!badd){
						list.remove(i);
					}
				}
			}
		}
		logger.debug("finish_getProcessNextNodeList_task:"+task.getId()+"taskName"+task.getName());
		return list;
	}
	
	/**
	 * 获取流程当前节点的上一级节点
	 * @return
	 * @throws Exception
	 */
	public List<PvmTransition> getProcessPreviousNode(Task task)throws Exception{
		ActivityImpl ai=getProcessNowNode(task);
		if(ai!=null){
			return ai.getIncomingTransitions();
		}
		return new ArrayList<PvmTransition>();
	}
	
	/**
	 * 获取流程当前节点的上一级节点集合
	 * @return
	 * @throws Exception
	 */
	public List<ActivityImpl> getProcessPreviousNodeList(Task task)throws Exception{
		List<ActivityImpl> list=getProcessAllNodes(task.getProcessDefinitionId(), true, true);
		List<PvmTransition> listp=getProcessPreviousNode(task);
		boolean badd=false;
		if(list!=null && listp!=null){
			for(PvmTransition pt:listp){
				String id=pt.getSource().getId();
				String type=(String)pt.getSource().getProperty("type");
				if(type.equals(ACTIVITI_START_FLAG)){
					list=new ArrayList();
				}else if(type.equals(ACTIVITI_END_FLAG)){
					
				}else{
					for(int i=list.size()-1;i>=0;i--){
						ActivityImpl aii=list.get(i);
						//System.out.println(id + "  " + aii.getId());
						if(id.equals(aii.getId())){
							badd=true;
						}
						if(!badd){
							list.remove(i);
						}
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * 获取流程当前节点
	 * @param task 当前执行的任务
	 * @return
	 * @throws Exception
	 */
	public ActivityImpl getProcessNowNode(Task task)throws Exception{
		//根据任务获取当前流程执行ID，执行实例以及当前流程节点的ID
		String execId=task.getExecutionId();
		ExecutionEntity execution = (ExecutionEntity) getRuntimeService().createExecutionQuery().executionId(execId).singleResult();
		String activitiId = execution.getActivityId();
		//获取流程定义
		String processDefinitionId=task.getProcessDefinitionId();
		List<ActivityImpl> activitiList=getProcessAllNodes(processDefinitionId,false,false);
		for(ActivityImpl activityImpl:activitiList){
			String id = activityImpl.getId();
			if(activitiId.equals(id)){
				return activityImpl;
			}
		}
		return null;
	}
	
	/**
	 * 获取全部已部署流程
	 * @throws Exception
	 */
	public List<ProcessDefinition> getAllProcessDefinitions(String key,boolean onlyLastVersion) throws Exception{
		logger.debug("do_getAllProcessDefinitions_key:"+key);
		List<ProcessDefinition>  processDefinitions=null;
		if(key==null || key.length()<=0){
			if(onlyLastVersion){
				processDefinitions=getRepositoryService().createProcessDefinitionQuery().latestVersion().list();
			}else{
				processDefinitions=getRepositoryService().createProcessDefinitionQuery().list();
			}
		}else{
			if(onlyLastVersion){
				processDefinitions=getRepositoryService().createProcessDefinitionQuery().latestVersion().processDefinitionKey(key).list();
			}else{
				processDefinitions=getRepositoryService().createProcessDefinitionQuery().processDefinitionKey(key).list();
			}
		}
		logger.debug("finish_getAllProcessDefinitions_key:"+key);
		return processDefinitions;
	}
	
	/**
	 * 获取全部已部署流程并转换成list<Map>对象
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,String>> getAllProcessDefinitions2Map(String key,boolean onlyLastVersion) throws Exception{
		List<ProcessDefinition> deployments=getAllProcessDefinitions(key,onlyLastVersion);
		List<Map<String,String>> jlist=new ArrayList<Map<String,String>>();
		for(ProcessDefinition de:deployments){
			System.out.println(de.getId() + " " + de.getCategory() +" " + de.getKey() + " " + de.getName() + " " + de.getDeploymentId());
	        jlist.add(convertProcessDefinition2Map(de));
		}
		return jlist;
	}
	
	/**
	 * 转换ProcessDefinition中的属性至map
	 * @param pd ProcessDefinition实例
	 * @return
	 */
	public  Map<String,String> convertProcessDefinition2Map(ProcessDefinition pd){
		Map<String,String> map=new HashMap<String,String>();
		map.put("id", pd.getId());
		map.put("category", pd.getCategory());
		map.put("name", pd.getName());
		map.put("key", pd.getKey());
		map.put("description", pd.getDescription());
		map.put("version", String.valueOf(pd.getVersion()));
		map.put("resourceName", pd.getResourceName());
		map.put("deploymentId", pd.getDeploymentId());
		map.put("diagramResourceName", pd.getDiagramResourceName());
		map.put("hasStartFormKey", String.valueOf(pd.hasStartFormKey()));
		map.put("isSuspended", String.valueOf(pd.isSuspended()));
		return map;
	}
	
	/**
	 * 创建流程模型
	 * @param name
	 * @param key
	 * @param description
	 * @return
	 * @throws Exception
	 */
	public String createProcessDefinitionModle(String name,String key,String description) throws Exception{
		logger.debug("do_createProcessDefinitionModle_name:"+name+"+_key:"+key+"+_description"+description);
		  ObjectMapper objectMapper = new ObjectMapper();
	      ObjectNode editorNode = objectMapper.createObjectNode();
	      editorNode.put("id", "canvas");
	      editorNode.put("resourceId", "canvas");
	      ObjectNode stencilSetNode = objectMapper.createObjectNode();  
	      stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
	      editorNode.put("stencilset", stencilSetNode);
	      Model modelData = getRepositoryService().newModel();
	      ObjectNode modelObjectNode = objectMapper.createObjectNode();
	      modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
	      modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
	      description = org.apache.commons.lang.StringUtils.defaultString(description);
	      modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
	      modelData.setMetaInfo(modelObjectNode.toString());
	      modelData.setName(name);
	      modelData.setKey(org.apache.commons.lang.StringUtils.defaultString(key));
	      getRepositoryService().saveModel(modelData);
	      getRepositoryService().addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
	      logger.debug("finish_createProcessDefinitionModle_name:"+name+"+_key:"+key+"+_description"+description);
	      return modelData.getId();
	}
	
	/**
	 * 删除流程模型
	 * @param modelId
	 */
	public void delProcessDefinitionModle(String modelId){
		logger.debug("do_delProcessDefinitionModle_modelId:"+modelId);
		getRepositoryService().deleteModel(modelId);
		logger.debug("finish_delProcessDefinitionModle_modelId:"+modelId);
	}
	
	/**
	 * 获取流程模型
	 * @return
	 */ 
	public List<Model> getProcessDefinitionModles(String key) {
		List<Model> list =null;
		if(key!=null &&key!=""&& key.length()>=0){
			list=getRepositoryService().createModelQuery().modelKey(key).list();
		}else{
			list=getRepositoryService().createModelQuery().list();
		}
		return list;
	}
	
	/**
	 * 获取Map实例的流程模型
	 * @return
	 */ 
	public List<Map<String,String>> getProcessDefinitionModles2Map(String key) {
		List<Model> list=getProcessDefinitionModles(key);
		List<Map<String,String>> listm=new ArrayList();
		if(list!=null && list.size()>0){
			for(Model md:list){
				listm.add(convertModelDefinition2Map(md));
			}
		}
		return listm;
	}
	
	/**
	 * 转换Model中的属性至map
	 * @param md Model实例
	 * @return
	 */
	public Map<String,String> convertModelDefinition2Map(Model md){
		Map<String,String> map=new HashMap<String,String>();
		map.put("id", md.getId());
		map.put("key", md.getKey());
		map.put("name", md.getName());
		map.put("deploymentId", md.getDeploymentId());
		map.put("category", md.getCategory());
		Date d=md.getCreateTime();
		if(d!=null){
			map.put("createTime",DateUtil.convertDateToString(md.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
		}else{
			map.put("createTime","");
		}
		d=md.getLastUpdateTime();
		if(d!=null){
			map.put("lastUpdateTime",DateUtil.convertDateToString(md.getLastUpdateTime(),"yyyy-MM-dd HH:mm:ss"));
		}else{
			map.put("lastUpdateTime","");
		}
		map.put("version", md.getVersion().toString());
		map.put("tenantId", md.getTenantId());
		map.put("metaInfo", md.getMetaInfo());
		return map;
	}
	
	/**
	 * 部署流程模型
	 * @param modelId 流程模型ID
	 * @return
	 * @throws Exception
	 */
	public String deployProcessDefinitionModle(String modelId) throws Exception{
		logger.debug("do_deployProcessDefinitionModle_modelId:"+modelId);
		Model modelData = getRepositoryService().getModel(modelId);
		JsonNode modelNode =  new ObjectMapper().readTree(getRepositoryService().getModelEditorSource(modelData.getId()));
		byte[] bpmnBytes = null;
		BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
		bpmnBytes = new BpmnXMLConverter().convertToXML(model);
		String processName = modelData.getName() + ".bpmn20.xml";
		System.out.println(new String(bpmnBytes,"UTF-8"));
		Deployment deployment = getRepositoryService().createDeployment().name(modelData.getName()).addString(processName, new String(bpmnBytes,"UTF-8")).deploy();
		logger.debug("finish_deployProcessDefinitionModle_modelId:"+modelId);
		return deployment.getId();
	}
	
	/**
	 * 部署流程模型
	 * @param modelId 流程模型ID
	 * @return
	 * @throws Exception
	 */
	public void deployProcessDefinitionModle(String modelId,String xmlStr) throws Exception{
		BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
        XMLInputFactory xif = XMLInputFactory.newInstance();
        InputStreamReader in = new InputStreamReader(new ByteArrayInputStream(xmlStr.trim().getBytes()), "UTF-8");
        XMLStreamReader xtr = xif.createXMLStreamReader(in);
        BpmnModel bpmnModel = xmlConverter.convertToBpmnModel(xtr);
        
        BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
        ObjectNode editorNode = jsonConverter.convertToJson(bpmnModel);
        getRepositoryService().addModelEditorSource(modelId, editorNode.toString().getBytes("utf-8"));
 //      getRepositoryService().ADD
	}
	/**
	 * 导出流程模型
	 * @param modelId 流程模型ID
	 * @param os 流程接口
	 * @throws Exception
	 */
	public void exportProcessDefinitionModle(String modelId,OutputStream os) throws Exception{
		Model modelData = getRepositoryService().getModel(modelId);  
		BpmnJsonConverter jsonConverter = new BpmnJsonConverter();  
		JsonNode editorNode = new ObjectMapper().readTree(getRepositoryService().getModelEditorSource(modelData.getId()));  
		BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);  
		BpmnXMLConverter xmlConverter = new BpmnXMLConverter();  
		byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);
		ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);  
		IOUtils.copy(in, os);
		in.close();
		os.close();
	}
	
	/**
	 * 导出流程模型
	 * @param modelId 流程模型ID
	 * @param os 流程接口
	 * @throws Exception
	 */
	public void exportProcessDefinitionModle(String modelId,HttpServletResponse response) throws Exception{
		Model modelData = getRepositoryService().getModel(modelId);  
		BpmnJsonConverter jsonConverter = new BpmnJsonConverter();  
		JsonNode editorNode = new ObjectMapper().readTree(getRepositoryService().getModelEditorSource(modelData.getId()));  
		BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
		BpmnXMLConverter xmlConverter = new BpmnXMLConverter();  
		byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);
		ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
		String filename = bpmnModel.getMainProcess().getName() + ".bpmn20.xml";
		response.setHeader("Content-Disposition", "attachment; filename=" + filename);
		OutputStream os=response.getOutputStream();
		IOUtils.copy(in, os);
		//IOUtils.closeQuietly(os);
	    response.flushBuffer();
	    in.close();
	}
	
	/**
	 * 启动一个流程
	 * @param processKey
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String doStartProcess(String processKey)throws Exception{
//		logger.debug("do_doStartProcess_processKey:"+processKey);
		ProcessInstance pi=processEngine.getRuntimeService().startProcessInstanceByKey(processKey);
//		logger.debug("finish_doStartProcess_processKey:"+processKey);
		return pi.getId();
	}
//	public String submitStartProcess(String processKey,Map<String,String> map)throws Exception{、
//		ProcessInstance pi=processEngine.getFormService().submitStartFormData("12313123123", map) ;、
//		return pi.getId();
//	}
	
	/**
	 * task签收
	 * @param t
	 * @param userId
	 */
	public void doClaim(String taskId,String userId) throws Exception{
//		logger.debug("do_doClaim_taskId:"+taskId+"+userId"+userId);
		processEngine.getTaskService().claim(taskId, userId);
//		logger.debug("finish_doClaim_taskId:"+taskId+"+userId"+userId);
	}
	/**
	 * task  反签收
	 * @param t
	 * @param userId
	 */
	public void douUnClaim(String taskId) throws Exception{
//		logger.debug("do_douUnClaim_taskId:"+taskId);
		processEngine.getTaskService().unclaim(taskId);
//		logger.debug("finish_douUnClaim_taskId:"+taskId);
	}
	
	/** 
     * 转办流程 
     *  
     * @param taskId 当前任务节点ID 
     * @param userCode  被转办人Code 
     */  
    public  void transferAssignee(String taskId, String userId)  throws Exception{  
 //   	logger.debug("do_transferAssignee_taskId:"+taskId+"+userId:"+userId);
    	processEngine.getTaskService().setAssignee(taskId, userId);  
 //   	logger.debug("finish_transferAssignee_taskId:"+taskId+"+userId:"+userId);
    }
	
	/**
	 * 获取属于该用户的某类流程
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Map getWaitTaskForUserAndKey(String processKey,String user) throws Exception{
		List<Task> tlist ;
//		logger.debug("do_getWaitTaskForUser_user:"+user);
		tlist=processEngine.getTaskService().createTaskQuery().taskAssignee(user).processDefinitionKey(processKey).orderByTaskCreateTime().desc().listPage(0,10);
		Long total = processEngine.getTaskService().createTaskQuery().taskAssignee(user).processDefinitionKey(processKey).count();
		List<Map> convertTask2Map  = convertTask2Map(tlist);
		Map map = new HashMap();
		map.put("taskData", convertTask2Map);
		map.put("total", total) ;
//		logger.debug("finish_getWaitTaskForUser__user:"+user+"_total:"+total);
		return map;
	}
	/**
	 * 获取某一类型的用户的待办数据，分页查询
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Map getWaitTaskForUserAndKey(String user,String processKey,int beginNum,int maxNum) throws Exception{
		List<Task> tlist ;
//		logger.debug("do_getWaitTaskForUser_beginNum:"+beginNum+"_maxNum:"+maxNum+"_user:"+user);
		tlist=processEngine.getTaskService().createTaskQuery().taskAssignee(user).processDefinitionKey(processKey).orderByTaskCreateTime().desc().listPage(beginNum, maxNum);
		Long total = processEngine.getTaskService().createTaskQuery().taskAssignee(user).processDefinitionKey(processKey).count();
		List<Map> convertTask2Map  = convertTask2Map(tlist);
		Map map = new HashMap();
		map.put("taskData", convertTask2Map);
		map.put("total", total) ;
//		logger.debug("finish_getWaitTaskForUser_beginNum:"+beginNum+"_maxNum:"+maxNum+"_user:"+user+"_total:"+total);
		return map;
	}
	
	/**
	 * 获取未签收任务
	 * @param 
	 * 		taskDefinitionKey  taskName
	 * 		processKey   流程标识
	 * @return
	 * */
	public Map getTaskUnassigned(String taskDefinitionKey,String processKey) throws Exception{
		List<Task> tlist ;
//		logger.debug("do_getTaskUnassigned__taskDefinitionKey:"+taskDefinitionKey+"_processKey:"+processKey);.or().taskName("huiFuZuoXi")
		String[] str = taskDefinitionKey.split(";") ;
		List<String> taskNameList= new ArrayList() ;
		for(int i=0;i<str.length;i++){
			taskNameList.add(str[i]) ;
		}
		tlist=processEngine.getTaskService().createTaskQuery().processDefinitionKey(processKey).taskNameIn(taskNameList).taskUnassigned().orderByTaskCreateTime().asc().listPage(0, 10);
		long total = processEngine.getTaskService().createTaskQuery().processDefinitionKey(processKey).taskName(taskDefinitionKey).taskUnassigned().count() ;
		List<Map> convertTask2Map  = convertTask2Map(tlist);
		Map map = new HashMap();
		map.put("taskData", convertTask2Map);
		map.put("total", total) ;
//		logger.debug("finish_getTaskUnassigned__taskDefinitionKey:"+taskDefinitionKey+"_processKey:"+processKey);
		return map;
	}
	
	public Map getTaskUnassigned(String taskDefinitionKey,String processKey,int beginNum,int maxNum) throws Exception{
		List<Task> tlist ;
		String[] str = taskDefinitionKey.split(";") ;
		List<String> taskNameList= new ArrayList() ;
		for(int i=0;i<str.length;i++){
			taskNameList.add(str[i]) ;
		}
		tlist=processEngine.getTaskService().createTaskQuery().processDefinitionKey(processKey).taskNameIn(taskNameList).taskUnassigned().orderByTaskCreateTime().asc().listPage(beginNum, maxNum);
		long total = processEngine.getTaskService().createTaskQuery().processDefinitionKey(processKey).taskNameIn(taskNameList).taskUnassigned().count() ;
		List<Map> convertTask2Map  = convertTask2Map(tlist);
		Map map = new HashMap();
		map.put("taskData", convertTask2Map);
		map.put("total", total) ;
//		logger.debug("finish_getTaskUnassigned__taskDefinitionKey:"+taskDefinitionKey+"_processKey:"+processKey);
		return map;
	}
	
	/**
	 * 通过流程实例ID(processInstanceId)获取当前Task的id
	 * @param processInstanceId
	 * @return
	 * @throws Exception
	 */
	public String getTaskIdByProcessInstanceId(String processInstanceId) throws Exception{
		Task tt=processEngine.getTaskService().createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
		if(tt!=null){
			return tt.getId();
		}
		return "";
	}
	public Task getTaskByProcessInstanceId(String processInstanceId) throws Exception{
		Task tt=processEngine.getTaskService().createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
		if(tt!=null){
			return tt ;
		}
		return null;
	}
	/**
	 * Task提交
	 * @param taskid
	 * @param map 主要是存 flow_type
	 * @throws Exception
	 */
	public void doCompletTask(String taskId,Map map)throws Exception{
//		logger.debug("do_doCompletTask_taskId:"+taskId);
		processEngine.getTaskService().complete(taskId,map);
//		logger.debug("finish_doCompletTask_taskId:"+taskId);
	}
	
	public void doClearTask(String arg0,String arg1)throws Exception{
		logger.debug("do_doClearTask_:"+arg0+","+arg1) ;
		processEngine.getRuntimeService().deleteProcessInstance(arg0, null) ;
//		getRuntimeService().d
		logger.debug("finish_doCompletTask_:"+arg0+","+arg1) ;
	}
	
	/**
	 * 自由转向提交
	 * @param t
	 * @param tkey 转向标识
	 * @param map
	 * @throws Exception
	 */
	public void doCompletTask(String taskId,String toTaskId,Map<String,String> map)throws Exception{
		logger.debug("do_doCompletTask_taskId:"+taskId+"_toTaskId:"+toTaskId);
		Map vmap=new HashMap();
		Task task=findTaskById(taskId);
		ActivityImpl toAi = findActivityImpl(task.getProcessDefinitionId(),toTaskId);
	    if(toAi!=null && task!=null){
	    	TaskFormData tdf=getFormService().getTaskFormData(task.getId());
			vmap=genFormPropertyMap(tdf.getFormProperties(), map);
			// 当前节点  
	        ActivityImpl nowAi = findActivityImpl(task.getProcessDefinitionId(), task.getTaskDefinitionKey());
	        // 清空当前流向  
	        List<PvmTransition> oriPvmTransitionList = clearTransition(nowAi);
	        // 创建新流向  
	        TransitionImpl newTransition = nowAi.createOutgoingTransition();
	       
	        // 设置新流向的目标节点  
	        newTransition.setDestination(toAi);
	        // 执行转向任务  
	        getTaskService().complete(task.getId(), vmap);
	        // 删除目标节点新流入  
	        toAi.getIncomingTransitions().remove(newTransition);
	        // 还原以前流向
	        restoreTransition(nowAi, oriPvmTransitionList);
	    }
	    logger.debug("finish_doCompletTask_taskId:"+taskId+"_toTaskId:"+toTaskId);
	}
	
	
	/** 
     * 清空指定活动节点流向 
     *  
     * @param activityImpl 
     *            活动节点 
     * @return 节点流向集合 
     */  
    private List<PvmTransition> clearTransition(ActivityImpl activityImpl) {  
        // 存储当前节点所有流向临时变量  
    	logger.debug("do_clearTransition_activityImpl.getProcessDefinition:"+activityImpl.getProcessDefinition());
        List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();  
        // 获取当前节点所有流向，存储到临时变量，然后清空  
        List<PvmTransition> pvmTransitionList = activityImpl  
                .getOutgoingTransitions();  
        for (PvmTransition pvmTransition : pvmTransitionList) {  
            oriPvmTransitionList.add(pvmTransition);  
        }  
        pvmTransitionList.clear();  
        logger.debug("finish_clearTransition_activityImpl.getProcessDefinition:"+activityImpl.getProcessDefinition());
        return oriPvmTransitionList;  
    }
	
    /** 
     * 还原指定活动节点流向 
     *  
     * @param activityImpl 
     *            活动节点 
     * @param oriPvmTransitionList 
     *            原有节点流向集合 
     */  
    private void restoreTransition(ActivityImpl activityImpl,  
            List<PvmTransition> oriPvmTransitionList) {  
        // 清空现有流向  
        List<PvmTransition> pvmTransitionList = activityImpl  
                .getOutgoingTransitions();  
        pvmTransitionList.clear();  
        // 还原以前流向  
        for (PvmTransition pvmTransition : oriPvmTransitionList) {  
            pvmTransitionList.add(pvmTransition);  
        }  
    }
    
	/**
	 * 生成FormPropertyMap传参数对象
	 * @param fl
	 * @param pmap
	 * @return
	 */
	public Map genFormPropertyMap(List<FormProperty> fl,Map<String,String> pmap){
		Map mapv=new HashMap();
		if(fl!=null){
			for(FormProperty fp:fl){
				String key=fp.getName();
				mapv.put(key, pmap.get(key)==null?"":pmap.get(key));
			}
		}
		return mapv;
	}
	
	/**
	 * 获取用户所有处理过的任务
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<Map> getHistoryTaskForUser(String user) throws Exception{
		logger.debug("do_getHistoryTaskForUser_user:"+user);
		List <HistoricTaskInstance> hl =getHistoryService().createHistoricTaskInstanceQuery().taskAssignee(user).list();
		logger.debug("finish_getHistoryTaskForUser_user:"+user);
		return convertHistoricTaskInstance2Map(hl);
	}
	
	/**
	 * 获取用户所有处理过的任务，分页
	 * @param user
	 * @param bgeinNum
	 * @param maxNum
	 * @return
	 * @throws Exception
	 */
	public List<Map> getHistoryTaskForUser(String user,int bgeinNum,int maxNum) throws Exception{
		List <HistoricTaskInstance> hl =getHistoryService().createHistoricTaskInstanceQuery().taskAssignee(user).listPage(bgeinNum, maxNum);
		return convertHistoricTaskInstance2Map(hl);
	}
	/**
	 * 获取某一类型的用户的历史数据
	 * @param user
	 * @param processKey
	 * @return
	 * @throws Exception
	 */
	public List<Map> getHistoryTaskForUserAndKey(String user,String processKey) throws Exception{
		List <HistoricTaskInstance> hl =getHistoryService().createHistoricTaskInstanceQuery().taskAssignee(user).processDefinitionKey(processKey).list();
		return convertHistoricTaskInstance2Map(hl);
	}
	/**
	 * 获取某一类型的用户的历史数据,分页
	 * @param user
	 * @param processKey
	 * @param beginNum
	 * @param maxNum
	 * @return
	 * @throws Exception
	 */
	public List<Map> getHistoryTaskForUserAndKey(String user,String processKey,int beginNum,int maxNum) throws Exception{
		List <HistoricTaskInstance> hl =getHistoryService().createHistoricTaskInstanceQuery().taskAssignee(user).processDefinitionKey(processKey).listPage(beginNum, maxNum);
		return convertHistoricTaskInstance2Map(hl);
	}
	
	/**
	 * 获取实例所有历史
	 * @param processInstanceId
	 * @return
	 * @throws Exception
	 */
	public List<Map> getHistoryTaskForProcessInstanceId(Map map) throws Exception{
		Object processInstanceId = map.get("processInstanceId") ;
		List <HistoricTaskInstance> hl = new ArrayList() ;
		if(processInstanceId.toString()!=null){
			if("true".equals(map.get("startTimeAsc").toString())){
				hl=getHistoryService().createHistoricTaskInstanceQuery().processInstanceId(processInstanceId.toString()).orderByHistoricTaskInstanceStartTime().asc().list();
			}else{
				hl=getHistoryService().createHistoricTaskInstanceQuery().processInstanceId(processInstanceId.toString()).list();
			}
		}
		return convertHistoricTaskInstance2Map(hl);
	}
	
	/**
	 * 转换历史对象HistoricTaskInstance 至 Map 集合类型
	 * @param tlist
	 * @return
	 * @throws Exception
	 */
	public List<Map> convertHistoricTaskInstance2Map(List<HistoricTaskInstance> tlist) throws Exception{
		List<Map> rlist=new ArrayList<Map>();
		if(tlist!=null){
			for(HistoricTaskInstance t:tlist){
				Map m=convertHistoricTaskInstance2Map(t);
				if(m!=null){
					rlist.add(m);
				}
			}
		}
		return rlist;
	}
	
	/**
	 * 转换历史对象HistoricTaskInstance 至 Map
	 * @param t
	 * @return
	 * @throws Exception
	 */
	public Map convertHistoricTaskInstance2Map(HistoricTaskInstance t) throws Exception{
		Map rmap=new HashMap();
		if(t!=null){
			rmap.put("id",t.getId());
			rmap.put("processDefinitionId", StringUtils.toString(t.getProcessDefinitionId()));
			rmap.put("processInstanceId", StringUtils.toString(t.getProcessInstanceId()));
			rmap.put("executionId", StringUtils.toString(t.getExecutionId()));
			rmap.put("name", StringUtils.toString(t.getName()));
			rmap.put("description", StringUtils.toString(t.getDescription()));
			rmap.put("deleteReason", StringUtils.toString(t.getDeleteReason()));
			rmap.put("owner", StringUtils.toString(t.getOwner()));
			rmap.put("assignee", StringUtils.toString(t.getAssignee()));
			rmap.put("startTime", StringUtils.toString(t.getStartTime()));
			rmap.put("endTime", StringUtils.toString(t.getEndTime()));
			rmap.put("durationInMillis", StringUtils.toString(t.getDurationInMillis()));
			rmap.put("workTimeInMillis", StringUtils.toString(t.getWorkTimeInMillis()));
			rmap.put("claimTime", StringUtils.toString(t.getClaimTime()));
			rmap.put("taskDefinitionKey", StringUtils.toString(t.getTaskDefinitionKey()));
			rmap.put("formKey", StringUtils.toString(t.getFormKey()));
			rmap.put("priority", StringUtils.toString(t.getPriority()));
			rmap.put("dueDate", StringUtils.toString(t.getDueDate()));
			rmap.put("category", StringUtils.toString(t.getCategory()));
			rmap.put("parentTaskId", StringUtils.toString(t.getParentTaskId()));
			rmap.put("tenantId", StringUtils.toString(t.getTenantId()));
			List<HistoricDetail> listhd=getHistoryService().createHistoricDetailQuery().taskId(t.getId()).formProperties().list();
			if(listhd!=null && listhd.size()>0){
				rmap.put("formData", convertHistoricDetail2Map(listhd));
			}else{
				rmap.put("formData",new HashMap());
			}
			return rmap;
		}
		return null;
	}
	
	/**
	 * 转换历史表单对象HistoricDetail 至 map
	 * @param listd
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> convertHistoricDetail2Map(List<HistoricDetail> listd) throws Exception{
		Map<String,String> rmap=new HashMap<String,String>();
		if(listd!=null && listd.size()>0){
			for(HistoricDetail hd:listd){
				HistoricFormProperty hfp=(HistoricFormProperty)hd;
				String key=hfp.getPropertyId()==null?"":hfp.getPropertyId();
				if(key.length()>0){
					rmap.put(key, StringUtils.toString(hfp.getPropertyValue()));
				}
			}
		}
		return rmap;
	}
	
	/**
	 * 转换List<Task> 至 List<Map>
	 * @param tlist
	 * @return
	 * @throws Exception
	 */
	public  List<Map> convertTask2Map(List<Task> tlist) throws Exception{
		List<Map> rlist=new ArrayList<Map>();
		if(tlist!=null){
			for(Task t:tlist){
				Map m=convertTask2Map(t);
				if(m!=null){
					rlist.add(m);
				}
			}
		}
		return rlist;
	}
	/**
	 * 转换 Task 至 Map
	 * 
	 * @param task
	 * @return
	 * @throws Exception
	 */
	public Map convertTask2Map(Task t) throws Exception{
		Map rmap=new HashMap();
		if(t!=null){
			rmap.put("a_id",t.getId());
			rmap.put("a_assignee", StringUtils.toString(t.getAssignee()));
			rmap.put("a_category", StringUtils.toString(t.getCategory()));
			rmap.put("a_createTime", StringUtils.toString(t.getCreateTime()));
			rmap.put("a_description", StringUtils.toString(t.getDescription()));
			rmap.put("a_dueDate", StringUtils.toString(t.getDueDate()));
			rmap.put("a_executionId", StringUtils.toString(t.getExecutionId()));
			rmap.put("a_name", StringUtils.toString(t.getName()));
			rmap.put("a_owner", StringUtils.toString(t.getOwner()));
			rmap.put("a_parentTaskId", StringUtils.toString(t.getParentTaskId()));
			rmap.put("a_processDefinitionId", StringUtils.toString(t.getProcessDefinitionId()));
			rmap.put("a_processInstanceId", StringUtils.toString(t.getProcessInstanceId()));
			rmap.put("a_taskDefinitionKey", StringUtils.toString(t.getTaskDefinitionKey()));
			rmap.put("a_tenantId", StringUtils.toString(t.getTenantId()));
			rmap.put("a_priority", StringUtils.toString(t.getPriority()));
			return rmap;
		}
		return null;
	}
	/**
	 * 转换 FormProperty 至 Map
	 * @param fl
	 * @return
	 */
	public Map<String,String> convertFormData2Map(List<FormProperty> fl) throws Exception{
		Map<String,String> rmap=new HashMap<String,String>();
		if(fl!=null){
			for(FormProperty f:fl){
				String name=f.getName();
				if(name!=null && name.length()>0){
					rmap.put(name, f.getValue());
				}
			}
		}
		return rmap;
	}
	public static void main(String[] args) throws Exception{
		Map map = new HashMap() ;
//		Map maps = new HashMap() ;
//		ActivitiUtils ActivitiUtils = new ActivitiUtils();
//		ActivitiUtils.initActivitiConf() ;
//		map.put("id", "111") ;
//		
//		ActivitiUtils.doStartProcess("dd") ;
//获取多个节点的task
//		try{
//		List<String> l= new ArrayList() ;
//		l.add("huiFuZuoXi") ;
//		l.add("fuHe") ;
//		List<Task> tlist=processEngine.getTaskService().createTaskQuery().processDefinitionKey("BOC_gongdan").taskNameIn(l).taskUnassigned().orderByTaskCreateTime().desc().list();
//		tlist=tlist;
//		tlist=tlist;
//		System.out.println(tlist);
//		}catch(Exception e){e.printStackTrace();
//		logger.error("",e);}
		
		//	ActivitiUtils.getFormService().submitStartFormData(pdf.getId(), map);
//		map.put("id","333");
//		Map variables = new HashMap() ;
//		variables.put("workOrderId", "aaaa");
//        variables.put("callInNum", "aaa");
//        ProcessDefinition processDefinition = ActivitiUtils.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey("dd").singleResult();
//        ActivitiUtils.getFormService().submitStartFormData(processDefinition.getId().toString(), variables);
////		getHistoryService().
//		ActivitiUtils.doClearTask("c418633c-e8b3-11e4-9eb5-7427eadfd231","") ;
		
//		map.put("variableName", "id");//getRuntimeService().//
//		ActivitiUtils.getRuntimeService().createExecutionQuery().
//		ActivitiUtils.getRuntimeService().removeVariable("0ef362cf-41f2-11e4-b047-28e34750951e", "usercode");
//		System.out.println(ActivitiUtils.getTaskService().createTaskQuery().processDefinitionId("testing2").processVariableValueEquals(map).list());
//		getTaskService().createTaskQuery().processDefinitionId("workerOrder").processVariableValueEquals(map).list() ;
//		ActivitiUtils.getRuntimeService().setVariables("1af0f5be-b4eb-11e3-81cd-0090f5eb08f5",map) ;
//		ActivitiUtils.getTaskService().unclaim("");
//		List list1  = convertTask2Map(list) ;
//		List list = ActivitiUtils.getTaskService().createTaskQuery().taskId("4f1d64a7-63ee-11e4-858c-28e34750951e").processDefinitionKey("workOrder").list();
	//	System.out.println(list1);
//		Task mapl = (Task)list.get(0) ;
//		Map mapt = (Map)mapl.get("Task") ;
		
//		TaskFormData f1 = getFormService().getTaskFormData(mapl.getId()) ;
//		Map<String,String> rmap=new HashMap<String,String>();
//		List<FormProperty> fl=f1.getFormProperties() ;
//		if(f1!=null){
//			for(FormProperty f:fl){
//				String name=f.getName();
//				if(name!=null && name.length()>0){
//					rmap.put(name, f.getValue());
//				}
//			}
//		}
//		System.out.println(rmap);
//		ActivitiUtils.getRuntimeService().setVariables("0ef362cf-41f2-11e4-b047-28e34750951e", "") ;
//		ActivitiUtils.getRuntimeService().setVariable("078b6720-60de-11e4-8e21-28e34750951e", "true", map) ;
		
//		String pkey="myTestProcess_1";
//		String user="admin_7";
		
//		List<Map> listu=getWaitTaskForUser(user,0,2);
//		
//		for(Map m:listu){
//			System.out.println("user : " + m.get("assignee") + "_" + m.get("processInstanceId") + "_" + m.get("owner"));
//			Map fm=(Map)m.get("formData");
//			System.out.println(fm.get("id") + " " + fm.get("name"));
//		}
		
//		List <Map> hl =getHistoryTaskForUserAndKey(user, pkey);
//		String jstr=JSONSerializer.toJSON(hl).toString();
//		System.out.println(jstr);
		
//		List <Map> hl =getHistoryTaskForProcessInstanceId("2dec0a84-b666-11e3-b122-0090f5eb08f5");
//		String jstr=JSONSerializer.toJSON(hl).toString();
//		System.out.println(jstr);
		
//		listu=getWaitTaskForUserAndKey(user,pkey);
//		
//		for(Map m:listu){
//			System.out.println("key : " + m.get("assignee") + "_" + m.get("taskDefinitionKey") + "_" + m.get("owner"));
//		}
		//test_modeler();
		
		//test_viewer();
		
		//test_flow();
		
		//test_doFlow();
	//	System.exit(0);
	}
}
