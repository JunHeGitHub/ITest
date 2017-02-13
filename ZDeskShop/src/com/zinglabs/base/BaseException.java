package com.zinglabs.base;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class BaseException extends Exception {

	protected Throwable cRootCause = null; // chained exception

	protected String csResourceKey = null; // resource file key for display

	private List cExceptions = new ArrayList();

	// Constructor
	public BaseException() {
	}

	public BaseException(String asErrMsg) {
		super(asErrMsg);
		csResourceKey = asErrMsg;
	}

	// inside message, not user message
	public BaseException(String asErrMsg, Throwable ex) {
		this.cRootCause = ex;
		this.csResourceKey = asErrMsg;
	}

	public BaseException(Throwable ex) {
		this(ex.getMessage());
		if (!(ex instanceof BaseException))
			this.cRootCause = ex;
		else // means ex is InventoryException
		{
			this.cRootCause = ((BaseException) ex).getRootCause();
			this.csResourceKey = ((BaseException) ex).getResourceKey();
		}
	}

	public BaseException(List asErrMsg) {
		if (asErrMsg != null && asErrMsg.size() > 0) {
			for (int i = 0; i < asErrMsg.size(); i++)
				this.addException(new BaseException(asErrMsg.get(i).toString()));
		}
	}

	public String getResourceKey() {
		return csResourceKey;
	}

	public Throwable getRootCause() {
		return this.cRootCause;
	}

	public void setRootCause(Throwable aException) {
		this.cRootCause = aException;
	}

	public List getExcepionsList() {
		return this.cExceptions;
	}

	public void addException(BaseException ex) {
		this.cExceptions.add(ex);
	}

	// overload throwable function
	public String getMessage() {
		if (cRootCause == null)
			return super.getMessage();
		else
			return this.cRootCause.getMessage();
	}

	public void printStackTrace() {
		if (cRootCause == null)
			super.printStackTrace();
		else
			this.cRootCause.printStackTrace();
	}

	public void printStackTrace(PrintStream outStream) {
		if (cRootCause == null)
			super.printStackTrace(outStream);
		else
			this.cRootCause.printStackTrace(outStream);
	}

	public void printStackTrace(PrintWriter writer) {
		if (cRootCause == null)
			super.printStackTrace(writer);
		else
			this.cRootCause.printStackTrace(writer);
	}
}
