package com.greedy.goodeat.admin.post.service;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.greedy.goodeat.admin.post.repository.AdmEventRepository;
import com.greedy.goodeat.common.dto.PostDTO;
import com.greedy.goodeat.common.entity.Post;
import com.greedy.goodeat.common.entity.PostType;

import antlr.debug.Event;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class AdmEventService {
	
	public static final int TEXT_PAGE_SIZE = 5;
	public static final int THUMBNAIL_PAGE_SIZE = 6;
	public static final String SORT_BY = "postCode";
//	public static final int POST_TYPE_CODE = 1;
//	public static final int EVENT_TYPE_CODE = 2;
	
	
	
	
	private final AdmEventRepository admEventRepository;
	private final ModelMapper modelMapper;
	
	@Autowired
	public AdmEventService(AdmEventRepository admEventRepository,
						   ModelMapper modelMapper) {
		this.admEventRepository = admEventRepository;
		this.modelMapper = modelMapper;
		
	}
	
	public Page<PostDTO> findEventList(int page, String searchValue) {
		
		Pageable pageable = PageRequest.of(page -1, TEXT_PAGE_SIZE, Sort.by(SORT_BY).descending());
		Page<Post> eventList = null;
		
		if(searchValue !=null && !searchValue.isEmpty()) {
			eventList = admEventRepository.findBySearchValue(searchValue, pageable);
		} else {
			eventList = admEventRepository.findAll(pageable);
		}
		
		return eventList.map(post -> modelMapper.map(post, PostDTO.class));
	}

	public void registEvent(PostDTO newEvent) {
		
		admEventRepository.save(modelMapper.map(newEvent, Post.class));
		
		log.info("[EventController] newEvent : {}", newEvent);
	}

	public PostDTO selectEventList(Integer eventCode) {
		
		log.info("[EventController] select ==============================");
		
		Post selectedEvnet = admEventRepository.findById(eventCode).get();
		
		log.info("[EventController] selectedEvnet : {}", selectedEvnet);
		
		return modelMapper.map(selectedEvnet, PostDTO.class);
		
	}

	

}
