package com.greedy.goodeat.user.productdetail.controller;



import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greedy.goodeat.common.dto.InquiryDTO;
import com.greedy.goodeat.common.dto.MemberDTO;
import com.greedy.goodeat.common.paging.Pagenation;
import com.greedy.goodeat.common.paging.PagingButtonInfo;
import com.greedy.goodeat.user.productdetail.hgdto.hgProductDTO;
import com.greedy.goodeat.user.productdetail.hgdto.hgReviewDTO;
import com.greedy.goodeat.user.productdetail.service.InquiryService;
import com.greedy.goodeat.user.productdetail.service.ProductdetailService;
import com.greedy.goodeat.user.productdetail.service.ReviewService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/user/productdetail")
public class ProductdetailController {
	

	private final ReviewService reviewService;
	private final InquiryService inquiryService; 
	private final ProductdetailService productdetailService;
	private final MessageSourceAccessor messageSourceAccessor;

	public ProductdetailController(ReviewService reviewService, InquiryService inquiryService , ProductdetailService productdetailService,
			MessageSourceAccessor messageSourceAccessor) {
		this.reviewService = reviewService;
		this.productdetailService = productdetailService;
		this.inquiryService = inquiryService;
		this.messageSourceAccessor = messageSourceAccessor;
	}

	@GetMapping("/list")
	public String productdetailList(Integer productCode,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(required = false) String searchValue, Model model) {
		// 리뷰리스트
		hgProductDTO hgproduct = productdetailService.selecthgProduct(productCode);
		
		log.info("hgproduct : {}", hgproduct);
		model.addAttribute("hgproduct", hgproduct);

		Page<hgReviewDTO> hgreviewList = reviewService.selectReviewList(page, searchValue);
		Page<InquiryDTO> inquiryList = inquiryService.selectInquiryList(page, searchValue);
		
		PagingButtonInfo pagingin = Pagenation.getPagingButtonInfo(inquiryList);
		PagingButtonInfo paging = Pagenation.getPagingButtonInfo(hgreviewList);
		
		//리뷰
		log.info("hgreviewList : {}", hgreviewList);
		log.info("paing : {}", paging);

		model.addAttribute("hgreviewList", hgreviewList);
		model.addAttribute("paging", paging);
		
		//문의
		log.info("inquiryList : {}", inquiryList);
		log.info("paingin : {}", pagingin);
		
		model.addAttribute("inquiryList", inquiryList);
		model.addAttribute("pagingin", pagingin);

		return "user/productdetail/product-list";
	}

	
	  //리뷰 등록
	  
	  @GetMapping("/regist") 
	  public String goRegist() {
		  
	  return "user/productdetail/review/registReview"; 
	  }
	  
	  @PostMapping("/regist") public String registReview(hgReviewDTO
	  review, @AuthenticationPrincipal MemberDTO member, RedirectAttributes rttr) {
	  
	  member = new MemberDTO(); 
	  member.setMemberNo(2);
	  
	  
	  review.setMember(member); log.info("review : {}", review);
	  reviewService.registReview(review);
	  
	  
	  rttr.addFlashAttribute("message",
	 messageSourceAccessor.getMessage("review.regist"));
	  
	  
	  return "redirect:/user/productdetail/list?productCode=1"; 
	  }
	  
	  //리뷰 상세확인
	  
	  @GetMapping("/detail")
	  public String selectReviewDetail(Model model, Integer
	  reviewCode) {
	  
	  hgReviewDTO review = reviewService.selectReviewDetail(reviewCode);
	  
	  log.info("review : {}", review); model.addAttribute("review", review);
	  
	 return "user/productdetail/review/detailReview";
	  
	  }
	  
	  //수정
	  
	  @GetMapping("/review/modify") 
	  public String midifyReview(Model model, Integer
			  reviewCode) {
		  hgReviewDTO review = reviewService.selectReviewDetail(reviewCode);
		  
		  log.info("review : {}", review); model.addAttribute("review", review);
	  return "user/productdetail/review/modifyReview"; 
	  }
	  
	  @PostMapping("/review/modify") 
	  public String modifyReview(Model model, hgReviewDTO review, RedirectAttributes rttr) {
	  
		  reviewService.modifyReview(review);
	  
		  rttr.addFlashAttribute("modifySuccessMessage",
		  messageSourceAccessor.getMessage("review.modify"));
	  
		  log.info("[ReviewController] review: {}", review);
		  model.addAttribute("review", review);
	  
	 return "redirect:/user/productdetail/list?productCode=1"; }
	  
	  //삭제
	  
	  @GetMapping("/review/delete") 
	  public String deleteReview(@RequestParam
	  Integer reviewCode) {
	  
	 reviewService.deleteReview(reviewCode);
	 
	 return "redirect:/"; 
	 }
	 
	  //문의 상세확인
	  
	  @GetMapping("/detail/inquiry") public String selectInquiryDetail(Model model, Integer
	  inquiryCode) {
	  
		  InquiryDTO inquiry = inquiryService.selectInquiryDetail(inquiryCode);
	  
	  log.info("inquiry : {}", inquiry); 
	  model.addAttribute("inquiry", inquiry);
	  
	 return "user/inquiry/detailInquiry";
	  
	  }
	  
	  //문의 등록
	  
	  @GetMapping("/inquiry/regist") 
	  public String goInquiryRegist() {
	  
	  return "user/inquiry/registInquiry"; 
	  }
	  
	  @PostMapping("/inquiry/regist") 
	  public String registInquiry(InquiryDTO inquiry, @AuthenticationPrincipal MemberDTO member, RedirectAttributes rttr) {
	  
	  member = new MemberDTO(); 
	  member.setMemberNo(2);
	  
	  
	  inquiry.setMember(member); 
	  log.info("inquiry : {}", inquiry);
	  inquiryService.registInquiry(inquiry);
	  
	  
	  rttr.addFlashAttribute("message",
			  			messageSourceAccessor.getMessage("inquiry.regist"));
	  
	  
	  return "redirect:/user/productdetail/list?productCode=1"; }
	  
	  //수정
	  
	  @GetMapping("/inquiry/modify") 
	  public String goInquiryMidify(Model model, Integer
			  inquiryCode) {
		  
		  InquiryDTO inquiry = inquiryService.selectInquiryDetail(inquiryCode);
		  
		  log.info("inquiry : {}", inquiry); 
		  model.addAttribute("inquiry", inquiry);
	  
	  return "user/inquiry/modifyInquiry"; 
	  }
	  
	  @PostMapping("/inquiry/modify") 
	  public String modifyInquiry(Model model, InquiryDTO inquiry, RedirectAttributes rttr) {
	  
		  inquiryService.modifyInquiry(inquiry);
	  
		  rttr.addFlashAttribute("modifySuccessMessage",
		  messageSourceAccessor.getMessage("inquiry.modify"));
	  
		  log.info("inquiry: {}", inquiry);
		  model.addAttribute("inquiry", inquiry);
	  
	 return "redirect:/user/productdetail/list?productCode=1"; 
	 }

	  //삭제
	  
	  @GetMapping("/inquiry/delete") 
	  public String deleteInquiry(@RequestParam
	  Integer inquiryCode) {
	  
		  inquiryService.deleteInquiry(inquiryCode);
	 
		  return "redirect:/user/productdetail/list?productCode=1"; 
	 }
	  
}