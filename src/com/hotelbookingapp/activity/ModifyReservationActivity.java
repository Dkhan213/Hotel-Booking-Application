package com.hotelbookingapp.activity;

import com.hotelbookingapp.dao.ReservationDao;
import com.hotelbookingapp.dao.models.UpdatedReservation;
import com.hotelbookingapp.metrics.MetricsConstants;
import com.hotelbookingapp.metrics.MetricsPublisher;
import com.amazonaws.services.cloudwatch.model.StandardUnit;

import javax.inject.Inject;
import java.time.ZonedDateTime;

/**
 * Handles requests to modify a reservation
 */
public class ModifyReservationActivity {

    private ReservationDao reservationDao;
    private MetricsPublisher metricsPublisher;

    /**
     * Construct ModifyReservationActivity.
     * @param reservationDao Dao used for modify reservations.
     */
    @Inject
    public ModifyReservationActivity(ReservationDao reservationDao, MetricsPublisher metricsPublisher) {
        this.reservationDao = reservationDao;
        this.metricsPublisher = metricsPublisher;
    }

    /**
     * Modifies the given reservation.
     * @param reservationId Id to modify reservations for
     * @param checkInDate modified check in date
     * @param numberOfNights modified number of nights
     * @return UpdatedReservation that includes the old reservation and the updated reservation details.
     */
    public UpdatedReservation handleRequest(final String reservationId, final ZonedDateTime checkInDate,
                                            final Integer numberOfNights) {

        UpdatedReservation updatedReservation = reservationDao.modifyReservation(reservationId, checkInDate,
            numberOfNights);

        // Increment the ModifiedReservationCount metric
        //                              metric-name             , value  , unit-of-measure-for-value
        metricsPublisher.addMetric(MetricsConstants.MODIFY_COUNT, 1, StandardUnit.Count);

        // Store a ReservationRevenue metric using the difference in totalCost of modified Reservation and original
        // The UpdatedReservation object contains the new and original reservation
        double revenueDifference = updatedReservation.getModifiedReservation().getTotalCost()
                        .subtract(updatedReservation.getOriginalReservation().getTotalCost()).doubleValue();

        metricsPublisher.addMetric(MetricsConstants.BOOKING_REVENUE, revenueDifference, StandardUnit.None);

        return updatedReservation;
    }
}
