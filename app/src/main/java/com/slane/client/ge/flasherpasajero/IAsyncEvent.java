package com.slane.client.ge.flasherpasajero;

/**
 * Created by Roger Navarro on 29/09/2018.
 */
public abstract interface IAsyncEvent
{
    public abstract void Event(String event, String args);
}