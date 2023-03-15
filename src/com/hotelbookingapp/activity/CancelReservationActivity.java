package com.hotelbookingapp.activity;

import com.hotelbookingapp.dao.ReservationDao;
import com.hotelbookingapp.dao.models.Reservation;
import com.hotelbookingapp.metrics.MetricsConstants;
import com.hotelbookingapp.metrics.MetricsPublisher;
import com.amazonaws.services.cloudwatch.model.StandardUnit;

import javax.inject.Inject;

/**
 * Handles requests to cancel a reservation.
 */
public class CancelReservationActivity {

    private ReservationDao reservationDao;
    private MetricsPublisher metricsPublisher;

    /**
     * Constructs a CancelReservationActivity
     * @param reservationDao Dao used to update reservations.
     */
    @Inject
    public CancelReservationActivity(ReservationDao reservationDao, MetricsPublisher metricsPublisher) {
        this.reservationDao = reservationDao;
        this.metricsPublisher = metricsPublisher;
    }

    /**
     * Cancels the given reservation.
     * @param reservationId of the reservation to cancel.
     * @return canceled reservation
     */
    public Reservation handleRequest(final String reservationId) {
        // Cancel a reservation - response object contains information about the reservation
        Reservation response = reservationDao.cancelReservation(reservationId);

        // Increment the CanceledReservationCount metric
        //                              metric-name             , value  , unit-of-measure-for-value
        metricsPublisher.addMetric(MetricsConstants.CANCEL_COUNT, 1, StandardUnit.Count);

        // Add a ReservationRevenue using the totalCost in the response from the cancellation
        metricsPublisher.addMetric(MetricsConstants.BOOKING_REVENUE, response.getTotalCost().doubleValue(), StandardUnit.None);

        return response;
    }
}
