package com.greedy.goodeat.user.product.orderby.service;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;

//import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.greedy.goodeat.common.dto.PostDTO;
import com.greedy.goodeat.user.product.orderby.entity.Post;
import com.greedy.goodeat.user.product.orderby.entity.PostType;
import com.greedy.goodeat.user.product.orderby.repository.PostRepository;

@Service
@Transactional
public class PostService {
	
	public static final int PAGE_SIZE = 10;
	public static final int NOTICE_TYPE = 1;
	public static final int EVENT_TYPE = 2;
	public static final String SORT_BY = "postCode";  //코드기준

	public static final String ACTIVE_STATUS = "정상";
	
	private final PostRepository postRepository;
	private final ModelMapper modelMapper;
	
	public PostService(PostRepository postRepository, ModelMapper modelMapper) {
		this.postRepository = postRepository;
		this.modelMapper = modelMapper;
	}
	
	public Page<PostDTO> selectPostList(int page, String searchValue) {
		Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE, Sort.by(SORT_BY).descending());
		Page<Post> postList = null;
		
		PostType postType = new PostType();
		postType.setPostTypeCode(NOTICE_TYPE);
	
		
		
		if(searchValue != null && !searchValue.isEmpty()) {
			
		} else {
			
			postList = postRepository.findByPostTypeAndPostStatus(postType, ACTIVE_STATUS, pageable);
		
			
		}            
		System.out.println(postList.getContent());
		
		
		return postList.map(post -> modelMapper.map(post, PostDTO.class));
	}

	public Page<PostDTO> selectEventList(int page, String searchValue) {
		Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE, Sort.by(SORT_BY).descending());
		
		Page<Post> eventList = null;
	
		PostType postType2 = new PostType();
		postType2.setPostTypeCode(EVENT_TYPE);
		
		
		if(searchValue != null && !searchValue.isEmpty()) {
			
		} else {
			eventList = postRepository.findByPostTypeAndPostStatus(postType2, ACTIVE_STATUS,  pageable);
			
		}            
		System.out.println(eventList.getContent());
		
		return eventList.map(post -> modelMapper.map(post, PostDTO.class));
	}
	
		public PostDTO selectpostDetail(int postCode) {
			
			PostType postType = new PostType();
			postType.setPostTypeCode(NOTICE_TYPE);
			
			Post post = postRepository.findByPostCodeAndPostTypeAndPostStatus(postCode, postType, ACTIVE_STATUS);
			
			
			
			return modelMapper.map(post, PostDTO.class);
	
	}

		public PostDTO selecteventDetail(int postCode) {
			
			PostType postType = new PostType();
			postType.setPostTypeCode(EVENT_TYPE);
			
			Post post = postRepository.findByPostCodeAndPostTypeAndPostStatus(postCode, postType, ACTIVE_STATUS);
			
			
			return modelMapper.map(post, PostDTO.class);
		}

	
	

}
