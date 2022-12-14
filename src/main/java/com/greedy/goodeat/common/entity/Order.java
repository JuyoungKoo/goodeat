package com.greedy.goodeat.common.entity;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TBL_ORDER")
@SequenceGenerator(name = "ORDER_SEQ_GENERATOR", sequenceName = "SEQ_ORDER_NO", initialValue = 1, allocationSize = 1)
@DynamicInsert
public class Order {
	

	@Id
	@Column(name="ORDER_NO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDER_SEQ_GENERATOR")
	private Integer orderNo;
	
	@Column(name="ORDER_DATE")
	private java.sql.Date orderDate;
	
	@Column(name="ORDER_STATUS")
	private String orderStatus;
	
	@Column(name="DELIVERY_FEE")
	private Integer deliveryFee;
	
	@Column(name="AMOUNT_PAY")
	private Integer amountPay;
	
	@ManyToOne
	@JoinColumn(name = "MEMBER_NO")
	private Member member;
	
	@ManyToOne
	@JoinColumn(name = "PRODUCT_CODE")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name = "DELIVERY_CODE")
	private Delivery delivery;
	
	@OneToMany(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "ORDER_NO")
	private List<OrderProduct> orderProduct;
	

}
