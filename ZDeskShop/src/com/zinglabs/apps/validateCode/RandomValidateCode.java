package com.zinglabs.apps.validateCode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.zinglabs.util.DESUtil;

/**
 * 生成验证码
 * 
 */
public class RandomValidateCode {

	public static final String RANDOMCODEKEY = "codeKey";// 放到session中的key
	private Random random = new Random();
	private String randString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";// 随机产生的字符串
	private int width = 80;// 图片宽
	private int height = 26;// 图片高
	private int lineSize = 40;// 干扰线数量
	private int stringNum = 4;// 随机产生字符数量

	private static RandomValidateCode instance = null;

	private RandomValidateCode() {
	}

	public static RandomValidateCode getInstance() {
		if (instance == null)
			instance = new RandomValidateCode();
		return instance;
	}

	/*
	 * 获得字体
	 */
	private Font getFont() {
		return new Font("Fixedsys", Font.CENTER_BASELINE, 18);
	}

	/*
	 * 获得颜色
	 */
	private Color getRandColor(int fc, int bc) {
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc - 16);
		int g = fc + random.nextInt(bc - fc - 14);
		int b = fc + random.nextInt(bc - fc - 18);
		return new Color(r, g, b);
	}

	/**
	 * 生成随机图片
	 */
	public void getRandcode(HttpServletRequest request,
			HttpServletResponse response) {

		// BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_BGR);
		Graphics g = image.getGraphics();// 产生Image对象的Graphics对象,改对象可以在图像上进行各种绘制操作
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 18));
		g.setColor(getRandColor(110, 133));
		// 绘制干扰线
		for (int i = 0; i <= lineSize; i++) {
			drowLine(g);
		}
		// 绘制随机字符
		String randomString = "";
		for (int i = 1; i <= stringNum; i++) {
			randomString = drowString(g, randomString, i);
		}
		// 存储生成字符串
		try {
			saveCode(randomString, request, response);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		g.dispose();
		try {
			// JPEGImageEncoder encoder =
			// JPEGCodec.createJPEGEncoder(response.getOutputStream());
			// encoder.encode(image);
			OutputStream os = response.getOutputStream();
			ImageIO.write(image, "JPEG", os);
			os.flush();
			os.close();
			response.flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 绘制字符串
	 */
	private String drowString(Graphics g, String randomString, int i) {
		g.setFont(getFont());
		g.setColor(new Color(random.nextInt(101), random.nextInt(111), random
				.nextInt(121)));
		String rand = String.valueOf(getRandomString(random.nextInt(randString
				.length())));
		randomString += rand;
		g.translate(random.nextInt(3), random.nextInt(3));
		g.drawString(rand, 13 * i, 16);
		return randomString;
	}

	/*
	 * 绘制干扰线
	 */
	private void drowLine(Graphics g) {
		int x = random.nextInt(width);
		int y = random.nextInt(height);
		int xl = random.nextInt(13);
		int yl = random.nextInt(15);
		g.drawLine(x, y, x + xl, y + yl);
	}

	/*
	 * 获取随机的字符
	 */
	public String getRandomString(int num) {
		return String.valueOf(randString.charAt(num));
	}

	/**
	 * 存储生成字符串
	 * 
	 * @param code
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void saveCode(String code, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String cookieToken = null;
		cookieToken = code + "_ZSP_" + System.currentTimeMillis()
				+ "_ZSP_login";
		cookieToken = DESUtil.encodeUTF2(cookieToken);
		// System.out.println(cookieToken);
		Cookie resCookie = new Cookie("cookietest", cookieToken);
		response.addCookie(resCookie);

	}

}