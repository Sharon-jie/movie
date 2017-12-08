package com.oristartech.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.oristartech.pojo.ForSale_ticket;
import com.oristartech.pojo.SalesStatusImage;
import com.oristartech.pojo.Ticket;
import com.oristartech.pojo.TicketNum;


@Mapper
public interface TicketMapper {
		//1.根据场次编码查询剩余待售影票数量
		public List<TicketNum> findForSaleTicketLeft(List<String> session_code);
		
		//2.根据场次编码查询待售影票信息集合
		public List<ForSale_ticket> findForSaleTicket(String session_code);
		
		//3.查询票集合的状态(返回值是指不是s3的影票数量)
		public int quertTicketStatus(List<Map<String,Object>> list);
		
		//4.根据待售影票编码集合，锁定指定影票
		public int forSaleTicketLock(List<Map<String,Object>> list);
		
		//5.根据待售影票编码集合，释放指定影票
		public int forSaleTicketRelease(List<Map<String,Object>> list);
		
		//6.根据待售影票编码集合，售出该影票
		public int forSaleTicketSold(List<String> forSale_ticket_code);
		
		//7.根据座位图例编码,查询图片
		public List<SalesStatusImage> findSeatStatusImage();
		
		//8.根据座位类型,日期查符合要求的场次编码集合
		public List<String> findFilmCodeBySeatType(String seat_type,Date date);
		
		//9.将销售出去的影票信息存储到票库中
		public int insertTicket(List<Ticket> ticket);
		
		//10.根据待售影票编码删除票库影票数据
		public int deletTicketByTicketCode(String forSale_ticket_code);
		
		/**11.根据待售影票编码集合查询待售影票信息
		 * @param forSale_ticket_code
		 * @return 待售影票
		 */
		public List<ForSale_ticket> queryForSaleTicket(List<String> forSale_ticket_code);
		
}
