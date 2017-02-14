package com.zinglabs.base.core.activitiSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.history.HistoryLevel;
import org.activiti.engine.impl.persistence.StrongUuidGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.zinglabs.base.core.frame.SysPropertiesUtil;

public abstract  class ActivitiService {
	
	private static SpringProcessEngineConfiguration springProcessEngineConfiguration;
	
	protected static ProcessEngine processEngine;
	
	private static RepositoryService repositoryService;
	
	private static RuntimeService runtimeService;
	
	private static TaskService taskService;
	
	private static HistoryService historyService;
	
	private static ManagementService managementService;
	
	private static IdentityService identityService;
	
	private static FormService formService;
	
	private static BasicDataSource datasource;
	
	public static Logger logger = LoggerFactory.getLogger("Activiti");
	
	/**
	 * 获取流程引擎配置
	 * @return
	 */
	
//	static{
//		initActivitiConf() ;
//		
//	}
	
	// 初始化activiti配置
	@SuppressWarnings("unchecked")
	public static BasicDataSource initDatasource() throws Exception{
        //驱动
			String driver = "com.mysql.jdbc.Driver";
			//ip
			String ip = "128.128.128.128";
			//端口
			String port = "3306";
			//用户名
			String username = "root";
			//密码
			String password = "12";
			//连接的数据库名
			String dbName = "activiti";
			//数据库类型
			String dbType = "mysql";
			//编码
			String charSet = "utf-8";

			// 连接池最大空闲连接数
			int maxIdleConnection = 20;
			// 超时时间
			int maxCheckoutTime = 3000;
			// 最大连接数
			int activeConnections = 200;
			
         //取activiti配置
			List list = SysPropertiesUtil.getConfWithBItemAndProductionId("database", "activiti");
			if(list.size()<=0){
				logger.error("缺少activiti数据源配置 ！ 请检查DataitemAlllocation表 ，缺少activiti配置数据。");
				throw new NullPointerException("缺少activiti数据源配置 ！ 请检查DataitemAlllocation表  或执行/updateSqls/base/activiti配置 脚本");
				
			}
			for (int i = 0; i < list.size(); i++) {
				Map<String,Object> conf = new HashMap<String,Object>() ;
				conf = (Map<String,Object>) list.get(i);
				String peizhiItem = String.valueOf(conf.get("peizhiItem"));
				String peizhiItemValue = String.valueOf(conf.get("peizhiItemValue"));
				if (peizhiItem == null) {
					logger.error("activiti 数据源配置错误! peizhiItem 不能为null ");
					throw new NullPointerException("activiti 数据源配置错误!");
				}
				if (peizhiItem.equals("dbip")) {
					ip = peizhiItemValue;
				} else if (peizhiItem.equals("name")) {
					dbName = peizhiItemValue;
				} else if (peizhiItem.equals("password")) {
					password = peizhiItemValue;
				} else if (peizhiItem.equals("type")) {
					if (peizhiItemValue.equals("mysql")) {
						driver = "com.mysql.jdbc.Driver";
						dbType = peizhiItemValue;
					}
				} else if (peizhiItem.equals("port")) {
					port = peizhiItemValue;
				} else if (peizhiItem.equals("charset")) {
					if (!"".equals(peizhiItemValue) && peizhiItemValue != null) {
						charSet = peizhiItemValue;
					}
				}

			}
			String url = "jdbc:"+dbType+"://" + ip + ":" + port + "/" + dbName + "?useUnicode=true&characterEncoding=" + charSet+"&transformedBitIsBoolean=true&failOverReadOnly=false&zeroDateTimeBehavior=round&useOldAliasMetadataBehavior=true&autoReconnect=true";
			logger.debug("activiti connection url :"+ url);
			logger.debug("activiti connection username :"+ username+"--password"+password);
			// TODO 这里使用DBCP数据源  
			
			BasicDataSource datasource = new BasicDataSource();
			//驱动
			datasource.setDriverClassName(driver);
	   		datasource.setUsername(username);
	   		datasource.setUrl(url);
	   		datasource.setPassword(password);
	   		//是否自动提交
	   		datasource.setDefaultAutoCommit(true);
	   		//初始化连接数
	   		datasource.setInitialSize(20);
	   		//最大活动链接
	   		datasource.setMaxActive(activeConnections);
	   		//最大空闲连接
	   		datasource.setMaxIdle(maxIdleConnection);
	   		//最小空闲连接
	   		datasource.setMinIdle(20);
	   		//最长等待时间  毫秒
	   		datasource.setMaxWait(maxCheckoutTime);
	   		//在空闲连接回收器线程运行期间休眠的时间值,以毫秒为单位. 如果设置为非正数,则不运行空闲连接回收器线程
	   		datasource.setTimeBetweenEvictionRunsMillis( 60 * 60 * 1000);
	   		//minEvictableIdleTimeMillis  1000 * 60 * 30  连接在池中保持空闲而不被空闲连接回收器线程
	   		datasource.setMinEvictableIdleTimeMillis( 5 * 60 * 1000);
	   		datasource.setLogAbandoned(true);
	   		// 标记是否删除泄露的连接,如果他们超过了removeAbandonedTimout的限制.如果设置为true, 连接被认为是被泄露并且可以被删除,如果空闲时间超过removeAbandonedTimeout. 
	   		datasource.setRemoveAbandonedTimeout(180);
	   		datasource.setValidationQuery("select 1");
	   		//从连接池取出连接是否检验  true 是每次检验一次
	   		datasource.setTestOnBorrow(true);
	   		// 指明连接是否被空闲连接回收器(如果有)进行检验.如果检测失败,则连接将被从池中去除.
	   		datasource.setTestWhileIdle(true);
	   		//指明是否在归还到池中前进行检验
	   		datasource.setTestOnReturn(false);
	   		//在每次空闲连接回收器线程(如果有)运行时检查的连接数量
	   		datasource.setNumTestsPerEvictionRun(5);
		
	   		return datasource ;
	}
	//配置SpringProcessEngineConfiguration
	public static ProcessEngineConfigurationImpl processEngineConfiguration() {
		springProcessEngineConfiguration = new SpringProcessEngineConfiguration();
	   	springProcessEngineConfiguration.setDataSource(getDatasource());
	   	springProcessEngineConfiguration.setActivityFontName("宋体") ;
	   	springProcessEngineConfiguration.setTransactionManager(annotationDrivenTransactionManager()) ;
	   	springProcessEngineConfiguration.setLabelFontName("宋体") ;
	   	springProcessEngineConfiguration.setHistoryLevel(HistoryLevel.NONE) ;
	   	StrongUuidGenerator id = new StrongUuidGenerator() ;
	   	springProcessEngineConfiguration.setIdGenerator(id) ;
	    return springProcessEngineConfiguration ;
	}
	public synchronized void initActivitiConf(){
		try{
			logger.debug("activiti 加载开始！ ");
			this.datasource=initDatasource();
			processEngine = processEngine() ;
			repositoryService = processEngine.getRepositoryService() ;
			runtimeService = processEngine.getRuntimeService() ;
			taskService = processEngine.getTaskService() ;
			historyService = processEngine.getHistoryService() ;
			managementService = processEngine.getManagementService() ;
			identityService = processEngine.getIdentityService() ;
			formService = processEngine.getFormService() ;
			logger.debug("activiti 加载结束！");
			
		}catch(Exception e){ 
			logger.error("...... initActivitiConf-error: ",e);
//			e.printStackTrace() ;
		}
	}
	
	/**
	 * 获取流程引擎配置
	 * 
	 * @return
	 * 
	 */
	public static SpringProcessEngineConfiguration getSpringProcessEngineConfiguration() {
		if (springProcessEngineConfiguration != null)
			return springProcessEngineConfiguration;
		return springProcessEngineConfiguration;
	}
	
	public ProcessEngineFactoryBean processEngineFactoryBean()
	{
		ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
		factoryBean.setProcessEngineConfiguration(processEngineConfiguration());
//		logger.debug(factoryBean.toString()) ;
		return factoryBean;
	}

	public synchronized ProcessEngine processEngine()
	{
		try{
			processEngine = processEngineFactoryBean().getObject();
		}catch(Exception e){
			logger.error("processEngine -error") ;
			logger.error(e.getMessage()) ;
			e.printStackTrace() ;
		}
		return processEngine ;
	}
	//必须配置事务管理
	public static PlatformTransactionManager annotationDrivenTransactionManager()
	{
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
		transactionManager.setDataSource(getDatasource());
		return transactionManager;
	}
	
	
	/**
	 * 获取流程管理服务
	 * @return
	 */
	public RepositoryService getRepositoryService(){
		if(repositoryService!=null) return repositoryService;
		repositoryService=processEngine().getRepositoryService();
		logger.debug("repositoryService is not load,and reload.") ;
		return repositoryService;
	}
	/**
	 * 获取流程实例服务
	 * @return
	 */
	public RuntimeService getRuntimeService(){
		if(runtimeService!=null) return runtimeService;
		runtimeService=processEngine().getRuntimeService();
		logger.debug("runtimeService is not load,and reload.") ;
		return runtimeService;
	}
	/**
	 * 获取流程任务服务
	 * @return
	 */
	public TaskService getTaskService(){
		if(taskService!=null) return taskService;
		taskService=processEngine().getTaskService();
		logger.debug("taskService is not load,and reload.") ;
		return taskService;
	}
	/**
	 * 获取流程实例运行历史服务
	 * @return
	 */
	public HistoryService getHistoryService(){
		if(historyService!=null) return historyService;
		historyService=processEngine().getHistoryService();
		logger.debug("historyService is not load,and reload.") ;
		return historyService;
	}
	
	/**
	 * 获取流程实例管理服务，查询和管理异步操作的功能
	 * @return
	 */
	public ManagementService getManagementService(){
		if(managementService!=null) return managementService;
		managementService=processEngine().getManagementService();
		logger.debug("managementService is not load,and reload.") ;
		return managementService;
	}
	/**
	 * 流程群组和用户管理服务，这个基本没用。
	 * 用户与群组都是业务系统的，不会用activiti中的。
	 * @return
	 */
	public IdentityService getIdentityService(){
		if(identityService!=null) return identityService;
		identityService=processEngine().getIdentityService();
		logger.debug("identityService is not load,and reload.") ;
		return identityService;
	}
	/**
	 * 流程表单服务
	 * 启动表单和任务表单
	 * 基本没用，表单都是业务系统定制的。
	 * @return
	 */
	public FormService getFormService(){
		if(formService!=null) return formService;
		formService=processEngine().getFormService();
		logger.debug("formService is not load,and reload.") ;
		return formService;
	}
	public static BasicDataSource getDatasource() {
		return datasource;
	}
	public static void setDatasource(BasicDataSource datasource) {
		ActivitiService.datasource = datasource;
	}
	
	
}
