package com.bartosztanski.BlogApp.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "fs.files")
public class VideoFilesEntity {

	@Id
	private String id;
	private String fileName;
	private long length;
	private int chunkSize;
	private Date uploadDate;
	private Object metadata;
}
