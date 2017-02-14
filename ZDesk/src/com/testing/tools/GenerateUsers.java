package com.testing.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class GenerateUsers {
	public static void main(String[] args) {
		FileOutputStream fos = null;
		BufferedWriter bw = null;
		FileOutputStream fos1 = null;
		BufferedWriter bw1 = null;
		try {
			fos = new FileOutputStream(new File("d:/sql添加语句.sql"));
			bw = new BufferedWriter(new OutputStreamWriter(fos));
			fos1 = new FileOutputStream(new File("d:/sql删除语句.sql"));
			bw1 = new BufferedWriter(new OutputStreamWriter(fos1));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			// 添加系统管理员
			for (int i = 0; i < 200; i++) {
				bw.write(sqlValues("testadmin" + i, "testadmin" + i, "系统管理员","org_00120"));
				bw.newLine();
			}

			/*// 添加文本坐席
			for (int i = 0; i < 500; i++) {
				bw.write(sqlValues("testwbzx" + i, "testwbzx" + i,"文本座席", "org_00202"));
				bw.newLine();
			}*/

			bw.flush();
			
			// 删除系统管理员
			for (int i = 0; i < 200; i++) {
				bw1.write("delete from `suSecurityUser` where `loginName`=" + "testadmin" + i + ";");
				bw1.newLine();
			}
			/*// 删除文本坐席
			for (int i = 0; i < 500; i++) {
				bw1.write("delete from `suSecurityUser` where `loginName`=" + "testwbzx" + i + ";");
				bw1.newLine();
			}*/
			bw1.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
				if (fos != null) {
					fos.close();
				}
				if (bw1 != null) {
					bw.close();
				}
				if (fos1 != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static String sqlValues(String name, String phone_number,
			String roleName, String organizationCode) {
		String sql = "insert into `suSecurityUser` (`loginName`, " + "`name`, "
				+ "`pwd`," + " `createDate`, " + "`lastLoginDate`, "
				+ "`startus`, " + "`viewIndex`, " + "`roleCode`, "
				+ "`roleName`, " + "`phone_number`," + " `agent_level`, "
				+ "`agent_information`, " + "`alias1`, " + "`alias2`, "
				+ "`alias3`, " + "`alias4`, " + "`alias5`, " + "`taskCount`, "
				+ "`finishTaskCount`, " + "`notFinishTaskCount`, "
				+ "`agentZLInfo`, " + "`organizationCode`, " + "`companyId`, "
				+ "`companyName`, " + "`lastModifyPassword`,"
				+ " `departmentId`, " + "`departmentName`) " + "values('"
				+ name + "'" + ",'" + name + "',"
				+ "'222','0000-00-00 00:00:00','0000-00-00 00:00:00',"
				+ "'0','999999','','" + roleName + "'," + "'" + phone_number
				+ "','',''," + "'" + name + "','','','','','0','0','0','','"
				+ organizationCode
				+ "','01','客服中心','0000-00-00 00:00:00',NULL,NULL);";
		return sql;
	}
}
