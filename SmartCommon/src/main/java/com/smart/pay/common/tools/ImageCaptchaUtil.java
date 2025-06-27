package com.smart.pay.common.tools;




import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;


/**
 * @author john 2017年11月3日 下午4:24:12
 */
public class ImageCaptchaUtil {

    private Font                font         = creatFont(20);                                                                                                 // 字体

    private Integer             length       = 5;                                                                                                             // 验证码随机字符长度

    private Integer             width        = 100;                                                                                                           // 验证码显示跨度

    private Integer             height       = 34;                                                                                                            // 验证码显示高度

    private Integer             size         = 20;

    private char[]              chars        = alphas(5);                                                                                                     // 随机字符串

    private static final Random RANDOM       = new Random();

    // 定义验证码字符.去除了O和I等容易混淆的字母
    private static final char   ALPHA[]      = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'G', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
            'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '2', '3', '4', '5',
            '6', '7', '8', '9' };

    private static final String sessionFiled = "imageCaptchaFiled";


    public Font getFont() {
        return font;
    }


    public void setFont(Font font) {
        this.font = font;
    }


    public Integer getLength() {
        return length;
    }


    public void setLength(Integer length) {
        this.length = length;
        this.chars = alphas(length);
    }


    public Integer getWidth() {
        return width;
    }


    public void setWidth(Integer width) {
        this.width = width;
    }


    public Integer getHeight() {
        return height;
    }


    public void setHeight(Integer height) {
        this.height = height;
    }


    public Integer getSize() {
        return size;
    }


    public void setSize(Integer size) {
        this.size = size;
        this.font = creatFont(size);
    }


    public char[] getChars() {
        return chars;
    }


    public void setChars(char[] chars) {
        this.chars = chars;
    }


    /**
     * 显示随机验证码图片
     * 
     * @param chars
     *            文本
     * @param out
     *            输出流
     */
    public void show(OutputStream out, HttpSession session) {
        try {
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = (Graphics2D) bi.getGraphics();
            AlphaComposite ac3;
            Color color;
            int len = chars.length;
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);
            // 随机画干扰的蛋蛋
            for (int i = 0; i < 15; i++) {
                color = color(150, 250);
                g.setColor(color);
                // 画蛋蛋，有蛋的生活才精彩
                g.drawOval(num(width), num(height), 5 + num(10), 5 + num(10));
                color = null;
            }
            g.setFont(font);
            int h = height - ((height - font.getSize()) >> 1), w = width / len, size = w - font.getSize() + 1;
            // 画字符串
            for (int i = 0; i < len; i++) {
                ac3 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f);// 指定透明度
                g.setComposite(ac3);
                color = new Color(20 + num(110), 20 + num(110), 20 + num(110));// 对每个字符都用随机颜色
                g.setColor(color);
                g.drawString(chars[i] + "", (width - (len - i) * w) + size, h - 4);
                color = null;
                ac3 = null;
            }
            String stringSession = new String(chars);
            session.setAttribute(sessionFiled, stringSession);
            ImageIO.write(bi, "png", out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }


    /**
     * 校验验证码
     * 
     * @param session
     * @param str
     *            验证的字符串
     * @return
     */
    public static boolean valid(HttpSession session, String vcode) {
        if (session.getAttribute(sessionFiled) != null && vcode != null) {
            String s = (String) session.getAttribute(sessionFiled);
            return s.toLowerCase().equals(vcode.toLowerCase());
        } else {
            return false;
        }
    }


    /**
     * 删除验证码
     * 
     * @param session
     * @return
     */
    public static void delete(HttpSession session) {
        session.setAttribute(sessionFiled, null);
    }


    public char[] alphas(Integer len) {
        char[] cs = new char[len];
        for (int i = 0; i < len; i++) {
            cs[i] = alpha();
        }
        return cs;
    }


    public Font creatFont(Integer size) {
        Font font = new Font("Verdana", Font.ITALIC | Font.BOLD, size);
        return font;
    }


    /**
     * 给定范围获得随机颜色
     * 
     * @return Color 随机颜色
     */
    public Color color(int fc, int bc) {
        if (fc > 255) fc = 255;
        if (bc > 255) bc = 255;
        int r = fc + num(bc - fc);
        int g = fc + num(bc - fc);
        int b = fc + num(bc - fc);
        return new Color(r, g, b);
    }


    /**
     * 产生两个数之间的随机数
     * 
     * @param min
     *            小数
     * @param max
     *            比min大的数
     * @return int 随机数字
     */
    public static int num(int min, int max) {
        return min + RANDOM.nextInt(max - min);
    }


    /**
     * 产生0--num的随机数,不包括num
     * 
     * @param num
     *            数字
     * @return int 随机数字
     */
    public static int num(int num) {
        return RANDOM.nextInt(num);
    }


    public static char alpha() {
        return ALPHA[num(0, ALPHA.length)];
    }

}
