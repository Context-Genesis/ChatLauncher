package com.contextgenesis.chatlauncher.rx;

import com.contextgenesis.chatlauncher.events.Event;
import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import timber.log.Timber;

/**
 * A simple Event Bus powered by Jake Wharton's RxRelay and RxJava2
 *
 * @author rish
 */

public class RxEventBus implements RxBus {

    private Relay<Object> busSubject;

    public RxEventBus() {
        busSubject = PublishRelay.create().toSerialized();
    }

    /**
     * Registers for a particular event and returns an observable for subscription.
     *
     * @param eventClass the event
     * @param <T>        the class type of the event
     * @return observable that can be subscribed to.
     */
    @Override
    public <T> Observable<T> register(@NonNull Class<T> eventClass) {
        return busSubject
                .filter(event -> event.getClass().equals(eventClass))
                .map(obj -> (T) obj);
    }

    /**
     * Registering to multiple sources
     */
    @Override
    public <T extends Event, T2 extends Event> Observable<Event> register(@NonNull Class<T> eventClass, @NonNull Class<T2> eventClass2) {
        return busSubject
                .filter(event -> event.getClass().equals(eventClass) || event.getClass().equals(eventClass2))
                .map(obj -> (Event) obj);
    }

    /**
     * Sends an event to all the observers who have registered to receive the event type.
     *
     * @param event an Event of any type.
     */
    @Override
    public void post(@NonNull Event event) {
        Timber.d(event.toString());
        busSubject.accept(event);
    }

    @Override
    public Relay<Object> getBusSubject() {
        return busSubject;
    }
}