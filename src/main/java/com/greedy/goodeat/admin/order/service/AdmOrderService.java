package com.greedy.goodeat.admin.order.service;


import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.greedy.goodeat.admin.order.dto.JyDeliveryDTO;
import com.greedy.goodeat.admin.order.dto.JyOrderDTO;
import com.greedy.goodeat.admin.order.entity.JyDelivery;
import com.greedy.goodeat.admin.order.entity.JyOrder;
import com.greedy.goodeat.admin.order.repository.AdmDeliveryRepository;
import com.greedy.goodeat.admin.order.repository.AdmOrderRepository;
import com.greedy.goodeat.admin.product.entity.KjyProductCategory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdmOrderService {
	
	public static final int TEXT_PAGE_SIZE = 10;
	public static final String SORT_BY = "orderNo";
	
	private final AdmOrderRepository admOrderRepository;
	private final AdmDeliveryRepository admDeliveryRepository;
	private final ModelMapper modelMapper;
	
	public AdmOrderService (AdmOrderRepository admOrderRepository, AdmDeliveryRepository admDeliveryRepository, ModelMapper modelMapper) {
		
		this.admOrderRepository = admOrderRepository;
		this.admDeliveryRepository = admDeliveryRepository;
		this.modelMapper = modelMapper;
	}
	public Page<JyOrderDTO> findOrderList(int page, String searchValue) {
		
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		Pageable pageable = PageRequest.of(page - 1, TEXT_PAGE_SIZE, Sort.by(SORT_BY).descending());
		Page<JyOrder> orderList = null;
		
		if(searchValue !=null && !searchValue.isEmpty()) {
			orderList = admOrderRepository.finBySearchValue(searchValue, pageable);
		} else {
			orderList = admOrderRepository.findAll(pageable);
		}
		
		
		log.info("orderList : {}", orderList.getContent());
		
		return orderList.map(order -> modelMapper.map(order, JyOrderDTO.class));
		
	}

	@Transactional
	public JyOrderDTO selectOrderList(Integer orderNo) {
		
		JyOrder order = admOrderRepository.findById(orderNo).get();
		
		return modelMapper.map(order, JyOrderDTO.class);
	}
	
	@Transactional
	public List<JyOrderDTO> getOrderInfo() {
		
		List<JyOrder> orderList = admOrderRepository.findAll();
		
		return orderList.stream().map(order -> modelMapper.map(order, JyOrderDTO.class)).collect(Collectors.toList());
		
	}
	
	@Transactional
	public void statusModify(JyOrderDTO order) {
		
		JyOrder statusModify = admOrderRepository.findById(order.getOrderNo()).get();
		
		statusModify.setOrderStatus(order.getOrderStatus());
		
		
	}

	@Transactional
	public void modifyDelivery(JyDeliveryDTO delivery) {
	
		JyDelivery modifydelivery = admDeliveryRepository.findById(delivery.getDeliveryCode()).get();
		
		modifydelivery.setDeliveryCompany(delivery.getDeliveryCompany());
		modifydelivery.setInvoiceNo(delivery.getInvoiceNo());


	
	}



}
