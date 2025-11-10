package com.example.geco.domains;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class BookingInclusion {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookingInclusionId;

    @ManyToOne
    @JoinColumn(name = "booking_id", referencedColumnName = "bookingId")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "inclusion_id", referencedColumnName = "inclusionId")
    private PackageInclusion inclusion;
    
    // How many people in the group will use this add-on.
    private Integer quantity;      
    
    // The PackageInclusion's pricePerPerson at the time of booking.
    private Integer priceAtBooking;
}
