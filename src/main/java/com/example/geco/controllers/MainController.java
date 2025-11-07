package com.example.geco.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.geco.domains.Account;
import com.example.geco.domains.Booking;
import com.example.geco.services.AccountService;
import com.example.geco.services.BookingService;

@RestController
public class MainController {
	/*
	 * entities to add
	 * - Attractions: id, name, description
	 * 
	 * functions to build:
	 * homepage
	 * - get attractions
	 * - get monthly visitors
	 * - get avg rating
	 * 
	 * 
	 * sign in & sign up
	 * - login with email and password
	 * - logout
	 * - sign in with name, email, password
	 * 
	 * 
	 * about us
	 * - POST feedback
	 * - GET feedback by category(facilities, staff&service, attractions, cleanliness, overall experience)
	 * - EDIT feedback
	 * - DELETE feedback
	 * - CRUD FAQ questions
	 * 
	 * 
	 * book visit
	 * - list out packages
	 * - list out add ons
	 * - get adds on that's not included on the chosen package
	 * - list out discount and their details
	 * - calendar details
	 * 	 - month and year paramater
	 *   - mark each day of that date as available, low, moderate, high, and fully booked
	 * - booking status of selected day = status, expected visitors, n bookings
	 * - booking status of selected month = total bookings, available days, busy days
	 * 
	 * 
	 * feedback
	 * - post feedback
	 * 
	 * 
	 * admin-dashboard
	 * - current month's booking
	 * - current month's revenue
	 * - pending bookings
	 * - current month's feedback
	 * 
	 * admin-dashboard bookings 
	 * - list of bookings sorted descending by date, can be filtered by status and date
	 * - searchable by name of the client
	 * - update a booking's status, can add note for self.
	 * 
	 * 
	 * admin-dashboard financial 
	 * - total revenue = number in peso
	 * - confirmed bookings = accepted bookings
	 * - average booking = per month
	 * - total bookings
	 * - monthly revenue bar graph filtered by year
	 * 
	 * 
	 * admin-dashboard trends
	 * - availed packages = pie graph of packages, filtered by year, month
	 * - revenue stats = if monthly view, filtered by year; if weekly view, filtered by year and month
	 * 
	 * 
	 * admin-dashboard feedback
	 * - list feedbacks, can be filtered by categories, and date
	 * 
	 * 
	 * admin-dashboard packages
	 * - CRUD available packages
	 * 
	 * 
	 * admin-dashboard discount
	 * - CRUD discount
	 * 
	 * 
	 * admin-dashboard attractions
	 * - CRUD discount
	 * 
	 * 
	 * my bookings
	 * - get bookings of the user
	 * - paying the downpayment for confirmed bookings
	 * 	- either gcash send proof of payment or pay through gcash api
	*/
	
	@Autowired
	private BookingService bookingService;
	
	@Autowired
	private AccountService accountService;
	
	@GetMapping
	public String home() {
        return "Agri-Eco Tourism Park";
    }
	
	@PostMapping("/login")
	public ResponseEntity<Account> addAccount(@RequestBody Account account) {
		Account savedAccount  = accountService.addAccount(account);
		return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
	}
}
