package com.testing.activitiTest.listenerTest;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;

public class MyTestListener implements ExecutionListener,TaskListener {

	private static final long serialVersionUID = -505882453116383127L;

	public void notify(DelegateExecution execution) throws Exception {
		System.out.println("-------------------------- kkkk --- kkkk" + execution.getEventName() + " : " + execution.getProcessInstanceId());
		
	}

	@Override
	public void notify(DelegateTask delegateTask) {
		System.out.println("-------------------------- kkkk --- kkk " + delegateTask.getEventName() + " : " + delegateTask.getId() + " : " + delegateTask.getProcessInstanceId());
		
	}

}
