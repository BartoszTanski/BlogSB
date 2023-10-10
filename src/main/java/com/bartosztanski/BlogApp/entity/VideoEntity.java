package com.bartosztanski.BlogApp.entity;

import java.io.InputStream;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoEntity {
	@Id
	private String id;
	private InputStream stream;


	public static class VideoEntityBuilder {
		
		public VideoEntityBuilder id() {
			this.id = new ObjectId().toString();
			return this;
		}
	}
}