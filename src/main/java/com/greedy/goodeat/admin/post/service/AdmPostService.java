package com.greedy.goodeat.admin.post.service;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.greedy.goodeat.admin.post.repository.AdmPostRepository;
import com.greedy.goodeat.common.dto.PostDTO;
import com.greedy.goodeat.common.entity.Post;
import com.greedy.goodeat.common.entity.Product;


@Transactional
@Service
public class AdmPostService {
	
	public static final int TEXT_PAGE_SIZE = 10;
	public static final int THUMBNAIL_PAGE_SIZE = 9;
	public static final int TEXT_BOARD_TYPE = 1;
	public static final int THUMBNAIL_BOARD_TYPE = 2;
	public static final String SORT_BY = "postCode";
	public static final String ACTIVE_STATUS = "Y";
	
	
	private final ModelMapper modelMapper;
	private final AdmPostRepository admPostRepository;
	
	@Autowired
	public AdmPostService(ModelMapper modelMapper,
						  AdmPostRepository admPostRepository) {
		this.modelMapper = modelMapper;
		this.admPostRepository = admPostRepository;
		
	}

	public Page<PostDTO> findPostList(Pageable pageable) {
		
		pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1,
								  pageable.getPageSize(), 
								  Sort.by("postCode").descending());
		
		Page<Post> postList = admPostRepository.findAll(pageable);
		
		return postList.map(post -> modelMapper.map(post, PostDTO.class));
	}

	public void registPost(PostDTO newPost) {
		
		admPostRepository.save(modelMapper.map(newPost, Post.class));
		
	}

}