package com.asu.aton.ui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import com.mortennobel.imagescaling.ResampleOp;
import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.jhlabs.image.UnsharpFilter;;
/**
 * 图片缩放工具类
 * @author sunnymoon
 */
public class MyImage {
	/**
	 * 接收输入流输生成图片
	 * @param input
	 * @param writePath
	 * @param width
	 * @param height
	 * @param format
	 * @return
	 */
	public boolean resizeImage(InputStream input, String writePath,
			Integer width, Integer height, String format) {
		try {
			BufferedImage inputBufImage = ImageIO.read(input);
			ResampleOp resampleOp = new ResampleOp(width, height);// 转换
			BufferedImage rescaledTomato = resampleOp.filter(inputBufImage,
					null);
			ImageIO.write(rescaledTomato, format, new File(writePath));
			//log.info("转后图片高度和宽度：" + rescaledTomato.getHeight() + ":"+ rescaledTomato.getWidth());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 接收File输出图片
	 * @param file
	 * @param writePath
	 * @param width
	 * @param height
	 * @param format
	 * @return
	 */
	public static Image resizeImage(String path, Integer width, Integer height, Image img) {
		try {
			File file = new File(path);
			BufferedImage inputBufImage = ImageIO.read(file);
			inputBufImage.getType();
			ResampleOp resampleOp = new ResampleOp(width, height);// 转换
			BufferedImage rescaledTomato = resampleOp.filter(inputBufImage,
					null);
			return rescaledTomato;
			//ImageIO.write(rescaledTomato, format, new File(writePath));
			//log.info("转后图片高度和宽度：" + rescaledTomato.getHeight() + ":"+ rescaledTomato.getWidth());
		} catch (IOException e) {
			e.printStackTrace();
			return img;
		}

	}
	

	/**
	 * 接收字节数组生成图片
	 * @param RGBS
	 * @param writePath
	 * @param width
	 * @param height
	 * @param format
	 * @return
	 */
	public boolean resizeImage(byte[] RGBS, String writePath, Integer width,
			Integer height, String format) {
		InputStream input = new ByteArrayInputStream(RGBS);
		return this.resizeImage(input, writePath, width, height, format);
	}

	public byte[] readBytesFromIS(InputStream is) throws IOException {
		int total = is.available();
		byte[] bs = new byte[total];
		is.read(bs);
		return bs;
	}
	
	public static BufferedImage createResizedCopy(Image originalImage, final int scaleSize)
	{
		BufferedImage crop = new BufferedImage(
		        originalImage.getWidth(null), originalImage.getHeight(null),
		        BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics2D g = crop.createGraphics();
		g.drawImage(originalImage, 0, 0, null);
		g.dispose();
	    ResampleOp resampleOp = new ResampleOp(scaleSize, scaleSize);
	    //resampleOp.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.Normal);
	    return resampleOp.filter(crop, null);
	}
	
	public static Image resizeImage(Image img, float ratio){
		float width = img.getWidth(null);
		float height = img.getHeight(null);
		width *= ratio;
		height *= ratio;
		img = img.getScaledInstance((int)width, (int)height, Image.SCALE_SMOOTH);
		return img;
	}
	
	public static Image resizeImage(Image img, int twidth){
		float width = img.getWidth(null);
		float height = img.getHeight(null);
		float ttwidth = twidth;
		float ratio = ttwidth / width;
		width *= ratio;
		height *= ratio;
		img = img.getScaledInstance((int)width, (int)height, Image.SCALE_SMOOTH);
		return img;
	}
	/*
	//测试：只测试了字节流的方式，其它的相对简单，没有一一测试
	public static void main(String[] args) throws IOException {
		
		int width = 150;
		int height = 150;
		File inputFile = new File("F:\\from.jpg");
		File outFile = new File("F:\\to.jpg");
		String outPath = outFile.getAbsolutePath();
		MyImage myImage = new MyImage();
		InputStream input = new FileInputStream(inputFile);
		byte[] byteArrayImage=myImage.readBytesFromIS(input);
		input.read(byteArrayImage);
		myImage.resizeImage(byteArrayImage, outPath, width, height, "jpg");
	}
	*/
}