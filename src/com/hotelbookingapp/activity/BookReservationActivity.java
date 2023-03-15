package com.hotelbookingapp.activity;

import com.hotelbookingapp.dao.ReservationDao;
import com.hotelbookingapp.dao.models.Reservation;
import com.hotelbookingapp.metrics.MetricsConstants;
import com.hotelbookingapp.metrics.MetricsPublisher;
import com.amazonaws.services.cloudwatch.model.StandardUnit;

import javax.inject.Inject;

/**
 * Handles requests to book a reservation.
 */
public class BookReservationActivity {

    private ReservationDao reservationDao;
    private MetricsPublisher metricsPublisher;

    /**
     * Constructs a BookReservationActivity
     * @param reservationDao Dao used to create reservations.
     */
    @Inject
    public BookReservationActivity(ReservationDao reservationDao, MetricsPublisher metricsPublisher) {
        this.reservationDao = reservationDao;
        this.metricsPublisher = metricsPublisher;
    }

    /**
     * Creates a reservation with the provided details.
     * @param reservation Reservation to create.
     * @return
     */
    public Reservation handleRequest(Reservation reservation) {

        Reservation response = reservationDao.bookReservation(reservation);

        // Increment the BookedReservationCount metric
        //                              metric-name                         , value  , unit-of-measure-for-value
        metricsPublisher.addMetric(MetricsConstants.BOOKED_RESERVATION_COUNT, 1, StandardUnit.Count);

        // Store a ReservationRevenue metric using the totalCost of Reservation
        metricsPublisher.addMetric(MetricsConstants.BOOKING_REVENUE, response.getTotalCost().doubleValue(), StandardUnit.None);

        return response;
    }
}
