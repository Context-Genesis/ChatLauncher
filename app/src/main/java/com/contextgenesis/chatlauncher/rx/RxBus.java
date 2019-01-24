package com.contextgenesis.chatlauncher.rx;

import com.contextgenesis.chatlauncher.events.Event;
import com.jakewharton.rxrelay2.Relay;

import androidx.annotation.NonNull;
import io.reactivex.Observable;

public interface RxBus {

    /**
     * Registers for a particular event and returns an observable for subscription.
     *
     * @param eventClass the event
     * @param <T>        the class type of the event
     * @return observable that can be subscribed to.
     */
    <T> Observable<T> register(@NonNull Class<T> eventClass);

    /**
     * Registering to multiple sources
     */
    <T extends Event, T2 extends Event> Observable<Event> register(@NonNull Class<T> eventClass, @NonNull Class<T2> eventClass2);

    /**
     * Sends an event to all the observers who have registered to receive the event type.
     *
     * @param event an Event of any type.
     */
    void post(@NonNull Event event);

    Relay<Object> getBusSubject();
}
