package com.bartosztanski.BlogApp.service;

import java.io.IOException;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bartosztanski.BlogApp.entity.VideoEntity;
import com.bartosztanski.BlogApp.error.VideoNotFoundException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;

@Service
public class VideoService {

	    private GridFsTemplate gridFsTemplate;
	    private GridFsOperations operations;
	    
	    public VideoService(GridFsTemplate gridFsTemplate,GridFsOperations operations) {
			this.gridFsTemplate = gridFsTemplate;
			this.operations = operations;
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
}
