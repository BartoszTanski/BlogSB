package com.bartosztanski.BlogApp.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class ImageResizeServiceTest {

	@Autowired
	ImageResizeService imageResizeService;
	
	@Test
	public void shouldResizeImageIfSizeGreater500KB() {
		
		Integer imageSizeBefore = null;
		Integer imageSizeAfter = null;
		try {
		File imageToResize = new File("./src/test/resources/imageToResize.jpg");
		Path path = Paths.get(imageToResize.getAbsolutePath());
		Binary binaryInputBinary = new Binary(BsonBinarySubType.BINARY, Files.readAllBytes(path));
		imageSizeBefore =  binaryInputBinary.length();
		Binary resizedImageBinary = imageResizeService.resizeImage(binaryInputBinary, 1280, 720);
		imageSizeAfter = resizedImageBinary.length();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertThat(imageSizeAfter<imageSizeBefore);
		System.out.println("Image size smaller: " + (float)imageSizeBefore/imageSizeAfter + " times");
	}
	
	
	
}
