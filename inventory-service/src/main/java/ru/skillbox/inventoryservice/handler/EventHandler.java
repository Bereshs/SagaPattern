package ru.skillbox.inventoryservice.handler;



public interface EventHandler<T extends Event, R extends Event>{
    R handleEvent(T event);
}
