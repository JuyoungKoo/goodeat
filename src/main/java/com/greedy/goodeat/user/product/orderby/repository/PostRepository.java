package com.greedy.goodeat.user.product.orderby.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.greedy.goodeat.user.product.orderby.entity.Post;
import com.greedy.goodeat.user.product.orderby.entity.PostType;



@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

	Page<Post> findByPostTypeAndPostStatus(PostType noticeType, String activeStatus, Pageable pageable);

	Post findByPostCodeAndPostTypeAndPostStatus(int postCode, PostType postType, String noticeStatus) ;

	

	
}
