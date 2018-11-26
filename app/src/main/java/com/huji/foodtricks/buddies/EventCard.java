package com.huji.foodtricks.buddies;

public class EventCard {

    private EventModel event;

    EventCard(EventModel event) {
        this.event = event;
    }

//    public int getImage() {
//        return event.;
//    }
//
//    public void setImage(int image) {
//        this.image = image;
//    }

    public String getName() {
        return event.getTitle();
    }

    public void setName(String name) {
        this.event.setTitle(name);
    }

}

