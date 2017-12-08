package com.oristartech.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.oristartech.mapper.TicketMapper;
import com.oristartech.pojo.ForSale_ticket;
import com.oristartech.pojo.SalesStatusImage;
import com.oristartech.pojo.Ticket;
import com.oristartech.pojo.TicketNum;
import com.oristartech.service.TicketMessageProvider;
@Service(loadbalance="roundrobin")
public class FindForSaleTicketImpl implements TicketMessageProvider {
	
	@Autowired
	private TicketMapper ticketMapper;
	
	//1.根据场次编码查询剩余待售影票数量
	@Override
	public List<TicketNum> findForSaleTicketLeft(List<String> session_code) {
		if(session_code.isEmpty()){
			return null;
		}
		List<TicketNum> ticketNum = ticketMapper.findForSaleTicketLeft(session_code);
		System.out.println(ticketNum);
		return ticketNum;
	}

	//2.根据场次编码查询待售影票信息集合
	@Override
	public List<ForSale_ticket> findForSaleTicket(String session_code) {
		if(session_code.isEmpty()){
			return null;
		}
		List<ForSale_ticket> forSaleTicket = ticketMapper.findForSaleTicket(session_code);
		return forSaleTicket;
	}

	//3.查询票集合的状态(返回值是指不是s3的影票数量)
	@Override
	public int quertTicketStatus(List<Map<String,Object>> forSaleTicketList) {
		// TODO Auto-generated method stub
		if(forSaleTicketList.isEmpty()){
			return 0;
		}
		int salesStatus = ticketMapper.quertTicketStatus(forSaleTicketList);
		return salesStatus;
	}
	
	//4.根据待售影票编码，锁定指定影票
		@Override
		public String forSaleTicketLock(List<Map<String,Object>> forSaleTicketList) {
			int count =  ticketMapper.quertTicketStatus(forSaleTicketList);
			if(count == 0){
				int ticketLock = ticketMapper.forSaleTicketLock(forSaleTicketList);
				if(ticketLock==0){
					return "fail";
				}
				return "success";
			}else{
				return "fail";
			}
		}
	
	//5.根据待售影票编码，释放指定影票
	@Override
	public String forSaleTicketRelease(List<Map<String,Object>> forSaleTicketList) {
		int ticketRelease = ticketMapper.forSaleTicketRelease(forSaleTicketList);
		if(ticketRelease==0){
			return "fail";
		}
		return "success";
	}
	
	//6.根据待售影票编码，售出该影票
	@Override
	public String forSaleTicketSold(List<String> forSale_ticket_code) {
		// TODO Auto-generated method stub
		if(!(forSale_ticket_code.isEmpty())){
			int ticketSold = ticketMapper.forSaleTicketSold(forSale_ticket_code);
			if(ticketSold!=0){
				List<ForSale_ticket> forSaleTicket = queryForSaleTicket(forSale_ticket_code);
				List<Ticket> ticketList=new ArrayList<>();
				if(!(forSaleTicket.isEmpty())){
					for (ForSale_ticket forSale_ticket : forSaleTicket) {
						Ticket ticket=new Ticket();
						ticket.setTicket_code(forSale_ticket.getForSale_ticket_code()+forSale_ticket.getScreen_code());
						ticket.setForSale_ticket_code(forSale_ticket.getForSale_ticket_code());
						ticket.setTicket_type(forSale_ticket.getSeat_type());
						ticket.setTicket_price(forSale_ticket.getPrice());
						ticketList.add(ticket);
					}
					String insertTicket = insertTicket(ticketList);
					if("success".equals(insertTicket)){
						return "success";
					}else{
						return "fail";
					}
				}
			}
		}
		return "fail";
	}
	
	//7.根据座位图例编码，查询图片
	@Override
	public List<SalesStatusImage> findSeatStatusImage() {
		List<SalesStatusImage> seatStatusImage = ticketMapper.findSeatStatusImage();
		return seatStatusImage; 
	}

	//8.根据座位类型,日期查符合要求的场次编码集合
	@Override
	public List<String> findFilmCodeBySeatType(String seat_type,Date date) {
		// TODO Auto-generated method stub
		if(!(seat_type.isEmpty())&&!(date.equals(null))){
			List<String> filmCode = ticketMapper.findFilmCodeBySeatType(seat_type, date);
			return filmCode;	
		}else{
			return null;
		}
	}
	
	//9.将销售出去的影票信息存储到票库中
	@Override
	public String insertTicket(List<Ticket> ticket) {
		// TODO Auto-generated method stub
		if(!(ticket.equals(null))){
			int insertTicket = ticketMapper.insertTicket(ticket);
			if(insertTicket==0){
				return "fail";
			}else{
				return "success";
			}
		}
		return "fail";
		
	}
	
	//10.根据待售影票编码删除票库影票数据
	@Override
	public String deletTicketByTicketCode(String forSale_ticket_code) {
		// TODO Auto-generated method stub
		if(!(forSale_ticket_code.isEmpty())){
			int ticketCode = ticketMapper.deletTicketByTicketCode(forSale_ticket_code);
			if(ticketCode==0){
				return "fail";
			}else{
				return "success";
			}
		}
		return "fail";
		
	}
	
	/**11.根据待售影票编码集合查询待售影票信息
	 * @param forSale_ticket_code
	 * @return 待售影票
	 */
	@Override
	public List<ForSale_ticket> queryForSaleTicket(List<String> forSale_ticket_code) {
		// TODO Auto-generated method stub
		if(forSale_ticket_code.isEmpty()){
			return null;
		}
		List<ForSale_ticket> forSaleTicket = ticketMapper.queryForSaleTicket(forSale_ticket_code);
		return forSaleTicket;
	}
	
}
