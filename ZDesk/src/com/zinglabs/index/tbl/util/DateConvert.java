

package com.zinglabs.index.tbl.util;


import java.text.SimpleDateFormat;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

public class DateConvert implements Converter {
	static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	public DateConvert() {

	}

	public Object convert(Class type, Object value) {

		if (value == null)
			return null;
		if (((String) value).trim().length() == 0)
			return null;

		if (value instanceof String) {
			try {
				return df.parse((String) value);
			} catch (Exception ex) {
				throw new ConversionException("输入的日期必须是\"yyyy-MM-dd\"格式"
						+ value.getClass());
			}

		} else {
			throw new ConversionException("输入的不是字符类" + value.getClass());
		}
	}
}
