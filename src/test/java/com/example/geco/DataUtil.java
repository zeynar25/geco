package com.example.geco;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.geco.domains.Account;
import com.example.geco.domains.Booking;

public class DataUtil {
	public static Account createUserA() {
		Account account = new Account();
		
		account.setUserId("mapagmahal@gmail.com");
		account.setPassword("123");
		
		return account;
	}
	
	public static Booking createBookingA(Account account) {
	    Booking booking = new Booking();
	    booking.setBookingId(1);
	    booking.setAccount(account);
	    booking.setVisitDate(LocalDate.now());
	    booking.setVisitTime(LocalTime.of(10, 30));
	    booking.setGroupSize(4);
	    booking.setStatus("Pending");
	    booking.setTotalPrice(1000);

	    return booking;
	}
	
	public static Booking createBookingB(Account account) {
	    Booking booking = new Booking();
	    booking.setBookingId(2);
	    booking.setAccount(account);
	    booking.setVisitDate(LocalDate.now());
	    booking.setVisitTime(LocalTime.of(10, 30));
	    booking.setGroupSize(4);
	    booking.setStatus("Pending");
	    booking.setTotalPrice(1000);

	    return booking;
	}
}
