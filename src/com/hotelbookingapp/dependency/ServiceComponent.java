package com.hotelbookingapp.dependency;

import com.hotelbookingapp.activity.BookReservationActivity;
import com.hotelbookingapp.activity.CancelReservationActivity;
import com.hotelbookingapp.activity.ModifyReservationActivity;

import dagger.Component;

import javax.inject.Singleton;

/**
 * Declares the dependency roots that Dagger will provide.
 */
@Singleton
@Component(modules = {MetricModule.class, DaoModule.class})
public interface ServiceComponent {
    BookReservationActivity provideBookReservationActivity();
    CancelReservationActivity provideCancelReservationActivity();
    ModifyReservationActivity provideModifyReservationActivity();
}
