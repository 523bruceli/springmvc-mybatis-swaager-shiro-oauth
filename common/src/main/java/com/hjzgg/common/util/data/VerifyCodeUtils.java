package com.hjzgg.common.util.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 产生一个指定位数的随机码(由数字或大小写字母构成)
 *
 */
public class VerifyCodeUtils {
	private static final Logger logger = LoggerFactory.getLogger(VerifyCodeUtils.class);

	// 验证码图片的宽度。
	private static final int WIDTH = 100;
	// 验证码图片的高度。
	private static final int HEIGHT = 40;
	// 字体高度
	private static final int FONT_HEIGHT = 32;

	public static final String[] CODE_SEQUENCE = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
			"N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	/**
	 * 生成验证码
	 * 
	 * @param length
	 *            验证码的位数
	 * @return
	 * @throws IOException
	 */
	public static String createVerifyCode(int length, HttpServletResponse response) throws IOException {
		// 定义图像buffer
		BufferedImage buffImg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D graph = buffImg.createGraphics();

		// 创建一个随机数生成器类
		Random random = new Random();
		// 将图像填充为白色
		graph.setColor(Color.WHITE);
		graph.fillRect(0, 0, WIDTH, HEIGHT);

		// 创建字体，字体的大小应该根据图片的高度来定。
		Font font = new Font("Fixedsys", Font.PLAIN, FONT_HEIGHT);
		// 设置字体
		graph.setFont(font);

		// 随机产生17条干扰线，使图象中的认证码不易被其它程序探测到。
		graph.setColor(Color.BLACK);
		for (int i = 0; i < 17; i++) {
			int x = random.nextInt(WIDTH);
			int y = random.nextInt(HEIGHT);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			graph.drawLine(x, y, x + xl, y + yl);
		}
		logger.info("绘制验证码干扰线");

		// randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
		StringBuilder randomCode = new StringBuilder();
		for (int i = 0; i < length; i++) {
			String strRand = CODE_SEQUENCE[random.nextInt(36)];
			randomCode.append(strRand);
			// 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
			int r = random.nextInt(255);
			int g = random.nextInt(255);
			int b = random.nextInt(255);

			// 用随机产生的颜色将验证码绘制到图像中。
			graph.setColor(new Color(r, g, b));
			graph.drawString(strRand, i * 25, 32);
		}
		logger.info("绘制验证码内容");

		// 禁止图像缓存。
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");

		ServletOutputStream sos = response.getOutputStream();
		ImageIO.write(buffImg, "jpeg", sos);
		sos.flush();
		sos.close();
		logger.info("将验证码图像通过流写出");
		return randomCode.toString();
	}

	/**
	 * 随机生成6位随机数
	 * 
	 * @return
	 */
	public static String createRandom() {
		List<String> list = Arrays.asList(CODE_SEQUENCE);
		Collections.shuffle(list);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i));
		}
		String afterShuffle = sb.toString();
		return afterShuffle.substring(2, 8);
	}

	/**
	 * 生成6位随机数,包含字母和数字
	 * 
	 * @param n
	 *            循环次数
	 * @return
	 */
	public static String createRandom(int n) {
		String randomStr = "";
		for (int i = 0; i < n; i++) {
			randomStr = createRandom();
			boolean flag = randomStr.matches("([0-9](?=[0-9]*?[a-zA-Z])\\w{5})|([a-zA-Z](?=[a-zA-Z]*?[0-9])\\w{5})");
			if (flag) {
				break;
			}
		}
		return randomStr;
	}
}