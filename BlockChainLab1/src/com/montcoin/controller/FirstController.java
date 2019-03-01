/*Shivam Agarwal
 * shivamag@buffalo.edu
 * UBIT 50289132
 * */
package com.montcoin.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.montcoin.dao.MontCoinDAO;
import com.montcoin.pojo.Customer;
import com.montcoin.pojo.Items;

@Controller
public class FirstController {

	MontCoinDAO montcoin = new MontCoinDAO();
	
	@PostConstruct
	/*
	 * public void init() { //System.out.println("inside init");
	 * //HttpServletRequest req = new HttpSession s = List<Items> items =
	 * montcoin.getItems(); List<Items> itemsList = new ArrayList<Items>(); for(int
	 * i=0; i<items.size(); i++) { if(null != items.get(i) &&
	 * !items.get(i).getSold().equalsIgnoreCase("yes")) {
	 * itemsList.add(items.get(i)); } } req.getSession().setAttribute("products",
	 * itemsList); }
	 */
	
	
	@RequestMapping("/login")
	public String getCustomer(HttpServletRequest req,Model m)
	{
		Customer customer = new Customer();
		//read the provided form data
		if(null != req.getSession().getAttribute("customer")) {
			customer = (Customer) req.getSession().getAttribute("customer");
		}else {
			String accno=req.getParameter("accno");
			int accountNumber = Integer.parseInt(accno);
			String pass=req.getParameter("pass");
			//Customer customer = new Customer();
			try {
				customer = montcoin.getCustomer(accountNumber, pass);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(null != customer && customer.getAccountNumber() != 0 ) {
			req.getSession().setAttribute("customer", customer);
			//get items details
			List<Items> items = montcoin.getItems();
			List<Items> itemsList = new ArrayList<Items>();
			for(int i=0; i<items.size(); i++) {
				if(null != items.get(i) && !items.get(i).getSold().equalsIgnoreCase("yes")) {
					itemsList.add(items.get(i));
				}
			}
			req.getSession().setAttribute("products", itemsList);
			return "buypage";
		}else {
			return "errorpage";
		}
	}
	
	@RequestMapping("/createAcc")
	public String createCustomer(HttpServletRequest req,Model m)
	{
		//read the provided form data
		String name = req.getParameter("name");
		String pass=req.getParameter("password");
		int success = 0;
		try {
			 success = montcoin.createCustomer(name, pass);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		if(success >= 1) {
			String msg = "Hello "+name+" your account has been successfully created please proceed to login with account number: "+success;
			m.addAttribute("message",msg);
			return "viewpage";
		}else {
			return "errorpage";
		}
	}
	
	@RequestMapping("/buy")
	public String purchaseItem(HttpServletRequest req,Model m){
		
		Customer customer = (Customer) req.getSession().getAttribute("customer");
		if(Integer.parseInt(req.getParameter("tokens")) == 1) {
			//purchase of tokens
			String currency = req.getParameter("currency");
			String coins = req.getParameter("montcoin");
			
			double tokens = Double.parseDouble(coins);
			if(currency.equalsIgnoreCase("USD")) {
				tokens = tokens * 4;
			}else if(currency.equalsIgnoreCase("CAD")) {
				tokens = tokens * 3;
			}else if(currency.equalsIgnoreCase("AUD")) {
				tokens = tokens * 2;
			}else if(currency.equalsIgnoreCase("INR")) {
				tokens = tokens * 1;
			}else {
				tokens = tokens * 5;
			}
			
			int success = montcoin.buyTokens(customer.getAccountNumber(),customer.getToken()+tokens);
			
			if(success == 1) {
				String msg = "Dear "+customer.getName()+" montcoins has been added to your account successfully!";
				customer.setToken(customer.getToken()+tokens);
				req.getSession().setAttribute("customer", customer);
				m.addAttribute("message",msg);
				return "buypage";
			}else {
				String msg = "Sorry "+customer.getName()+" something went wrong. Please try again!";
				m.addAttribute("message",msg);
				return "buypage";
			}
			
		}else {
			//purchase of items
			String itemRequested = req.getParameter("itemSelected");
			int item = Integer.parseInt(itemRequested);
			List<Items> itemsList = (List<Items>) req.getSession().getAttribute("products");
			
			for(int i=0; i< itemsList.size(); i++) {
				if(null != itemsList.get(i) && itemsList.get(i).getItemId() == item) {
					//this is user wants to purchase
					if(customer.getToken() >= itemsList.get(i).getPrice()) {
						//user can buy
						int success = montcoin.purchaseItem(customer, itemsList.get(i));
						if(success == 1){
							List<Items> items = montcoin.getItems();
							List<Items> newItemsList = new ArrayList<Items>();
							for(int j=0; j<items.size(); j++) {
								if(null != items.get(j) && !items.get(j).getSold().equalsIgnoreCase("yes")) {
									newItemsList.add(items.get(j));
								}
							}
							req.getSession().setAttribute("products", newItemsList);
							String msg = "Dear "+customer.getName()+" you have successfully purchased the product";
							m.addAttribute("message",msg);
							return "buypage";
						}else {
							String msg = "Sorry "+customer.getName()+" something went wrong. Please try again!";
							m.addAttribute("message",msg);
							return "buypage";
						}
					}else {
						//error redirect to same page
						String msg = "Sorry "+customer.getName()+" you are short of montcoins to buy this product";
						m.addAttribute("message",msg);
						return "buypage";
					}
				}
			}
			
			String msg = "Sorry "+customer.getName()+" something went wrong. Please try again!";
			m.addAttribute("message",msg);
			return "buypage";
		}
		
		
	}
	
	@RequestMapping("/sell")
	public String sellItem(HttpServletRequest req,Model m){
		
		Customer customer = (Customer) req.getSession().getAttribute("customer");
		Items item = new Items();
		item.setDetails(req.getParameter("pdetails"));
		item.setPrice(Double.parseDouble(req.getParameter("price")));
		item.setSold("no");
		if(customer.getAccountNumber() == Integer.parseInt(req.getParameter("sellerAccNo"))){
			item.setSeller(Integer.parseInt(req.getParameter("sellerAccNo")));
		}else {
			String msg = "Sorry "+customer.getName()+" incorrect account number entered";
			m.addAttribute("message",msg);
			return "sellconfirmpage";
		}
		
		int status = montcoin.sellItem(item);
		
		if(status == 1) {
			String msg = "Dear "+customer.getName()+" you product details has been succesfully recorded";
			m.addAttribute("message",msg);
			return "sellconfirmpage";
		}else {
			String msg = "Sorry "+customer.getName()+" something went wrong. Please try again!";
			m.addAttribute("message",msg);
			return "sellconfirmpage";
		}
		
	}
	
	
	@RequestMapping("/listProducts")
	public String listProducts(HttpServletRequest req,Model m){
		Customer customer = (Customer) req.getSession().getAttribute("customer");
		List<Items> itemsPurchased = montcoin.listProducts(customer.getAccountNumber());
		req.getSession().setAttribute("itemsPurchased", itemsPurchased);
		return "productsBought";
	}
	
	@RequestMapping("/deleteAccount")
	public String deleteAccount(HttpServletRequest req,Model m){
		Customer customer = (Customer) req.getSession().getAttribute("customer");
		try {
			montcoin.deleteAccount(customer.getAccountNumber());
		} catch (SQLException e) {
			String msg = "Sorry "+customer.getName()+" something went wrong. Please try again!";
			m.addAttribute("message",msg);
			return "viewpage";
		}
		String msg = "Your account has been successfully deleted, we will miss you "+customer.getName();
		m.addAttribute("message",msg);
		return "viewpage";
	}
}
