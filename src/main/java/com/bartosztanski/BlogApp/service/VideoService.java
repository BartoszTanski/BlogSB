package com.bartosztanski.BlogApp.service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bartosztanski.BlogApp.entity.VideoEntity;
import com.bartosztanski.BlogApp.error.VideoNotFoundException;
import com.bartosztanski.BlogApp.repository.VideoFilesRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;

@Service
public class VideoService {

	    private final GridFsTemplate gridFsTemplate;
	    private final GridFsOperations operations;
	    private final VideoFilesRepository videoFilesRepository;
	    
	    public VideoService(GridFsTemplate gridFsTemplate,GridFsOperations operations,
	    		VideoFilesRepository videoFilesRepository) {
			this.gridFsTemplate = gridFsTemplate;
			this.operations = operations;
			this.videoFilesRepository = videoFilesRepository;
		}

	    public VideoEntity getVideo(String id) throws IllegalStateException, IOException, VideoNotFoundException{
	        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
	        if(file==null) throw new VideoNotFoundException("Video with this id doesn't exist");
	        VideoEntity video = new VideoEntity();
	        video.setId(id);
	        video.setStream(operations.getResource(file).getInputStream());
	        return video;
	    }

	    public String addVideo(MultipartFile file) throws IOException {
	        DBObject metaData = new BasicDBObject();
	        metaData.put("type", "video");
	        ObjectId id = gridFsTemplate.store(file.getInputStream(), file.getName(), file.getContentType(), metaData);
	        return id.toString();
	    }

		public void deleteVideo(String id) {
			gridFsTemplate.delete(new Query(Criteria.where("_id").is(id)));
		}
		
		public List<String> getAllVideos() {
			List<String> videos= new LinkedList<String>();
			videos = videoFilesRepository.findAll().stream()
					.map(videoData -> videoData.getId()).collect(Collectors.toList());
			return videos;
		}
}
