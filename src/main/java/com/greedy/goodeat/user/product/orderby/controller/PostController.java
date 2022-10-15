package com.greedy.goodeat.user.product.orderby.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.greedy.goodeat.common.dto.PostDTO;
import com.greedy.goodeat.common.paging.Pagenation;
import com.greedy.goodeat.common.paging.PagingButtonInfo;
import com.greedy.goodeat.user.product.orderby.service.PostService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/post")
public class PostController {

	private final PostService postService;
	
	public PostController(PostService postService) {
		this.postService = postService;
	}
	
	@GetMapping("/list")     
	public String postList(@RequestParam(defaultValue="1") int page, 
			@RequestParam(required=false) String searchValue, Model model){

		log.info("[PostController] ========================================= ");
		log.info("[PostController] param page : {}", page);
		log.info("[PostController] param searchValue : {}", searchValue);
		
		Page<PostDTO> noticeList = postService.selectPostList(page, searchValue);
		Page<PostDTO> eventList = postService.selectEventList(page, searchValue);
		
		
		 PagingButtonInfo noticePaging = Pagenation.getPagingButtonInfo(noticeList);
		 PagingButtonInfo eventPaging = Pagenation.getPagingButtonInfo(eventList);
		 
		 log.info("[PostController] noticeList : {}", noticeList);
		 log.info("[PostController] noticePaging : {}", noticePaging);
		 
		 log.info("[PostController] eventList : {}", eventList);
		 log.info("[PostController] eventPaging : {}", eventPaging);
		 
		 model.addAttribute("noticeList", noticeList); 
		 model.addAttribute("noticePaging",noticePaging); 
		 
		 model.addAttribute("eventList", eventList);
		 model.addAttribute("eventPaging", eventPaging);
		 
		 if(searchValue != null && !searchValue.isEmpty()) {
			 model.addAttribute("searchValue", searchValue); 
		 }
		 
		 log.info("[POSTController] ============= postList" + noticeList.getContent());
		 log.info("[POSTController] ========================================= ");
		 log.info("[POSTController] ============= eventList" + eventList.getContent());
		 log.info("[POSTController] ========================================= ");
		 
		//return "redirect:/";
		return "user/product/board/board";
		
	};
	
	@GetMapping("/notice")     
	public String noticeList(@RequestParam(defaultValue="1") int page, 
			@RequestParam(required=false) String searchValue, Model model){

		log.info("[PostController] ========================================= ");
		log.info("[PostController] param page : {}", page);
		log.info("[PostController] param searchValue : {}", searchValue);
		
		Page<PostDTO> noticeList = postService.selectPostList(page, searchValue);
		
		
		 PagingButtonInfo noticePaging = Pagenation.getPagingButtonInfo(noticeList);
		
		 
		 log.info("[PostController] noticeList : {}", noticeList);
		 log.info("[PostController] noticePaging : {}", noticePaging);
		 
		 
		 model.addAttribute("noticeList", noticeList); 
		 model.addAttribute("noticePaging",noticePaging); 
		 

		 
		 if(searchValue != null && !searchValue.isEmpty()) {
			 model.addAttribute("searchValue", searchValue); 
		 }
		 
		 log.info("[POSTController] ============= postList" + noticeList.getContent());
		 log.info("[POSTController] ========================================= ");

		 
		//return "redirect:/";
		return "user/product/board/noticeboard";
		
	};
	
	@GetMapping("/event")     
	public String eventList(@RequestParam(defaultValue="1") int page, 
			@RequestParam(required=false) String searchValue, Model model){

		log.info("[PostController] ========================================= ");
		log.info("[PostController] param page : {}", page);
		log.info("[PostController] param searchValue : {}", searchValue);
		
		Page<PostDTO> eventList = postService.selectEventList(page, searchValue);
		
	
		 PagingButtonInfo eventPaging = Pagenation.getPagingButtonInfo(eventList);
		 
		 
		 log.info("[PostController] eventList : {}", eventList);
		 log.info("[PostController] eventPaging : {}", eventPaging);
		 

		 
		 model.addAttribute("eventList", eventList);
		 model.addAttribute("eventPaging", eventPaging);
		 
		 if(searchValue != null && !searchValue.isEmpty()) {
			 model.addAttribute("searchValue", searchValue); 
		 }
		 
	
		 log.info("[POSTController] ============= eventList" + eventList.getContent());
		 log.info("[POSTController] ========================================= ");
		 
		//return "redirect:/";
		return "user/product/board/eventboard";
	}
	
	
		@GetMapping("noticedetail")
		public String selectBoardDetail(Model model, @RequestParam int postCode) {
		
			log.info("PostCode : {}", postCode);
			
			PostDTO post = postService.selectpostDetail(postCode);
			
			log.info("Post : {}", post);
			
			model.addAttribute("post", post);
		
			
			return "user/product/board/boarddetail";
	};
	
	
	@GetMapping("eventdetail")
	public String selecteventDetail(Model model, @RequestParam int postCode) {
	
		log.info("PostCode : {}", postCode);
		
		PostDTO post = postService.selecteventDetail(postCode);
		
		log.info("Post : {}", post);
		
		model.addAttribute("post", post);
	
		
		return "user/product/board/eventdetail";
};
	
	
};
