package com.bartosztanski.BlogApp.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.bson.types.Binary;
import org.springframework.stereotype.Component;

import net.coobird.thumbnailator.Thumbnails;
@Component
public class ImageResizeService {
	public Binary resizeImage(Binary inputBinary,
			int targetWidth, int targetHeight) throws IOException {
		
		//If image size less than 0,5 MB return without compressing
		if (inputBinary.length()<524288) return inputBinary;
		
		InputStream is = new ByteArrayInputStream(inputBinary.getData());
		BufferedImage bi = ImageIO.read(is);
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    
	    Thumbnails.of(bi)
	        .size(targetWidth, targetHeight)
	        .outputFormat("JPEG")
	        .outputQuality(0.95)
	        .toOutputStream(outputStream);
	    
	    Binary outputBinary = new Binary(outputStream.toByteArray());
	    return outputBinary;
	}
}
