package com.bartosztanski.BlogApp.model;

import java.util.List;

import com.bartosztanski.BlogApp.entity.PostEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostsPage {
	
	private List<PostEntity> posts;
	private int size;
}
