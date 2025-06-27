package com.smart.pay.channel.util.qrcode;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.aliyun.oss.OSSClient;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;  
  
public class QrCodeUtil {  
  
//    public static void main(String[] args) throws Exception {  
//    	qrCodeEncode();  
//        qrCodeDecode();  
//    }  
  
	
	public static String qrOSSCodeEncode(String fileName, String content) {
		
		int width = 300; // 图像宽度  
        int height = 300; // 图像高度  
        String format = "png";// 图像类型  
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();  
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");  
        BitMatrix bitMatrix = null;
		try {
			bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 生成矩阵  
       /* Path path = FileSystems.getDefault().getPath(filePath, fileName); 
        File file = new File(filePath); 
        if(!file.exists()) {
        	file.mkdirs();
        }
        file = new File(path.toString());
        file.createNewFile();*/
		BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);// 输出图像  
        ByteArrayOutputStream os = new ByteArrayOutputStream();  
        try {
			ImageIO.write(image, format, os);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        InputStream is = new ByteArrayInputStream(os.toByteArray());  
	
	    // endpoint以杭州为例，其它region请按实际情况填写
	    String endpoint = "oss-cn-shanghai.aliyuncs.com";
	    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号
	    String accessKeyId = "LTAITeh9fCycyont";
	    String accessKeySecret = "kz5yn5VE1xyUZEJzWX3gWEWy6oeTmS";
	    // 创建OSSClient实例
	    OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
	    // 上传文件
	    ossClient.putObject("smartpayqrcode", fileName, is);
	    // 关闭client
	    ossClient.shutdown();
	    
	    Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
        String url = ossClient.generatePresignedUrl("smartpayqrcode", fileName, expiration).toString();
        return url;
	}
	
	
    /** 
     * 生成二维码 
     *  
     * @throws WriterException 
     * @throws IOException 
     */  
    public static void qrCodeEncode(String filePath,String fileName, String content) throws WriterException, IOException {  
        int width = 300; // 图像宽度  
        int height = 300; // 图像高度  
        String format = "png";// 图像类型  
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();  
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");  
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵  
        Path path = FileSystems.getDefault().getPath(filePath, fileName); 
        File file = new File(filePath); 
        if(!file.exists()) {
        	file.mkdirs();
        }
        file = new File(path.toString());
        file.createNewFile();
        MatrixToImageWriter.writeToFile(bitMatrix, format,file);// 输出图像  
//        System.out.println("输出成功.");  
    }  
  
    /** 
     * 解析二维码 
     */  
    public static void qrCodeDecode() {  
        String filePath = "D://zxing.png";  
        BufferedImage image;  
        try {  
            image = ImageIO.read(new File(filePath));  
            LuminanceSource source = new BufferedImageLuminanceSource(image);  
            Binarizer binarizer = new HybridBinarizer(source);  
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);  
            Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();  
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");  
            Result result = new MultiFormatReader().decode(binaryBitmap, hints);// 对图像进行解码  
            System.out.println("图片中内容：  ");  
            System.out.println("author： " + result.getText());  
            System.out.println("图片中格式：  ");  
            System.out.println("encode： " + result.getBarcodeFormat());  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (NotFoundException e) {  
            e.printStackTrace();  
        }  
    }  
    
    
    /*public static void main(String[] args) {
    	
    	
    	System.out.println(qrOSSCodeEncode("33311212", "http://www.baidu.com"));
    	
    }*/
    
    
  
}  