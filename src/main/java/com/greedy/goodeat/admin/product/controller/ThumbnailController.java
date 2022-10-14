package com.greedy.goodeat.admin.product.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greedy.goodeat.admin.product.service.AdmProductService;
import com.greedy.goodeat.common.dto.AddfileDTO;
import com.greedy.goodeat.common.dto.ProductDTO;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

@Slf4j
@Controller
@RequestMapping("/thumbnail")
public class ThumbnailController {
	
	@Value("${image.image-dir}")
	private String IMAGE_DIR;
	
	private final AdmProductService admProductService;
	private final MessageSourceAccessor messageSourceAccessor;
	
	public ThumbnailController(AdmProductService admProductService, MessageSourceAccessor messageSourceAccessor) {
		this.admProductService = admProductService;
		this.messageSourceAccessor = messageSourceAccessor;
	}
	
	@GetMapping("/regist")
	public String goRegister() {
		
		return "admin/product/adm-productregist";
	}
	
	@PostMapping("/regist")
	public String registThumbnail(ProductDTO product, List<MultipartFile> attachImage,RedirectAttributes rttr) {
		
		log.info("[ThumbnailController] ==========================================");
		
		log.info("[ThumbnailController] board request : {}", product);
		log.info("[ThumbnailController] attachImage request : {}", attachImage);
		
		String rootLocation = IMAGE_DIR;
		
		String fileUploadDirectory = rootLocation + "/upload/original";
		String thumbnailDirectory = rootLocation + "/upload/thumbnail";
		
		File directory = new File(fileUploadDirectory);
		File directory2 = new File(thumbnailDirectory);
		
		log.info("[ThumbnailController] directory : {}", directory);
		log.info("[ThumbnailController] directory2 : {}", directory2);
		
		if (!directory.exists() || !directory2.exists()) {
			log.info("[ThumbnailController] 폴더 생성 : {}", directory.mkdirs());
			log.info("[ThumbnailController] 폴더 생성 : {}", directory2.mkdirs());
		}
		
		/* 업로드 파일에 대한 정보를 담을 리스트 */
		List<AddfileDTO> addfileList = new ArrayList<>();
		
		try {
			
			/* 업로드 된 List<MultipartFile> 반복문 (4개) */
			for(int i = 0; i < attachImage.size(); i++) {
				/* 첨부파일이 실제로 있는 경우만 로직 수행 */
				if(attachImage.get(i).getSize() > 0) {
					
					String originalFileName = attachImage.get(i).getOriginalFilename();
					
					log.info("[ThumbnailController] originalFileName : " + originalFileName);
					
					String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
					String savedFileName = UUID.randomUUID().toString().replace("-", "") + ext;
					
					log.info("[ThumbnailController] savedFileName : " + savedFileName);

					attachImage.get(i).transferTo(new File(fileUploadDirectory + "/" + savedFileName));
	
					/* DB에 저장할 파일의 정보 */
					AddfileDTO fileInfo = new AddfileDTO();
					fileInfo.setOriginalFileName(originalFileName);
					fileInfo.setSavedFileName(savedFileName);
					fileInfo.setSavedRoute("/upload/original/");
					
					if(i == 0) {
						fileInfo.setFileType("TITLE");
						/* 대표 사진에 대한 썸네일을 가공해서 저장한다. */
						Thumbnails.of(fileUploadDirectory + "/" + savedFileName).size(300, 300)
							.toFile(thumbnailDirectory + "/thumbnail_" + savedFileName);
						
						fileInfo.setThumbnailRoute("/upload/thumbnail/thumbnail_" + savedFileName);
					} else {
						fileInfo.setFileType("BODY");
					}
					addfileList.add(fileInfo);
				}
			}
			
			log.info("[ThumbnailController] attachmentList : {}", addfileList);
			
			product.setAddfieList(addfileList);
			admProductService.registThumbnail(product);
			
			log.info("[ThumbnailController] thumbnail : {}", product);
		
			
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			/* 실패 시 이미 저장 된 파일을 삭제해야 한다. */
			for(AddfileDTO attachment : addfileList) {
				
				File deleteFile = new File(attachment.getSavedRoute() + "/" + attachment.getSavedFileName());
				deleteFile.delete();
				
				File deleteThumbnail = new File(thumbnailDirectory + "/thumbnail_" + attachment.getSavedFileName());
				deleteThumbnail.delete();
			}
		}

		log.info("[ThumbnailController] ==========================================");
		
		return "redirect:/admin/productRegist";
	}
	
	
}