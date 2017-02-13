package com.zinglabs.util.flowUtil;
import java.util.Map;
public interface FlowDisponse {
	public <T> Map disponseFirst(Map node,Map data,T params);
	public <T> Map disponseEnd(Map node,Map data,T params);
	public <T> Map disponseNext(Map node,Map data,T params);
	public <T> Map disponsePrevious(Map node,Map data,T params);
	public <T> Map disponseToNode(Map node,Map data,T params);
	public <T> Map disponseToFirst(Map node,Map data,T params);
	public<T> Map disponseToEnd(Map node,Map data,T params);
	public <T> Map disponseStop(Map node,Map data,T params);
}
