package com.example.geco.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.geco.domains.Booking;
import com.example.geco.repositories.BookingRepository;

@Service
public class BookingService {
	@Autowired
	private BookingRepository bookingRepository;
	
	public double getAverageVisitor(String type) {
		Iterable<Booking> iterable = bookingRepository.findAllByOrderByVisitDateAsc();	
		List<Booking> bookings = StreamSupport
		        .stream(iterable.spliterator(), false)
		        .collect(Collectors.toList());
		
		if(bookings.isEmpty()) return 0;
		
		int minYear = bookings.get(0).getVisitDate().getYear();
		int maxYear = bookings.get(bookings.size() - 1).getVisitDate().getYear();;
		int minMonth = bookings.get(0).getVisitDate().getMonthValue();
		int maxMonth = bookings.get(bookings.size() - 1).getVisitDate().getMonthValue();

		double totalAvgVisitors = 0;
		int totalMonth = 0;
		int index = 0;
		
		for (int year = minYear; year <= maxYear; year++) {
			int startMonth = (year == minYear) ? minMonth : 1;
		    int endMonth = (year == maxYear) ? maxMonth : 12;
			
			for (int month = startMonth; month <= endMonth; month++) {
				int visitors = 0;
				
				while (index < bookings.size()) {
					// If we go beyond current year and month, break.
					LocalDate currDate = bookings.get(index).getVisitDate();
					
					if (currDate.getYear() > year ||
							(currDate.getYear() == year && currDate.getMonthValue() > month)) {
						break;
					}
					
					visitors++;
					index++;
				}
				totalMonth++;
				totalAvgVisitors += (double) visitors / 30.0;
			}
		}

		double n = 0;
		if (type.equals("year")) {
			n = totalAvgVisitors / (maxYear - minYear + 1);
			
		} else if (type.equals("month")) {
			n = totalAvgVisitors / totalMonth;
		}
			
		return n;
	}
}
