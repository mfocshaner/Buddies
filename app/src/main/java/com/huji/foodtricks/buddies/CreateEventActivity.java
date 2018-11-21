package com.huji.foodtricks.buddies;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.goodiebag.carouselpicker.CarouselPicker;

public class CreateEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        CarouselPicker carouselPicker = setupCarousel(R.id.carousel);
        setupCarouselListeners(carouselPicker);
    }

    /// parameters to be passed to new event
    private Date _time;
    private String _eventType; // not well defined
    private List<String> _invitees; // will maybe change if invitees become "People" objects,

    private void chooseGroup(String groupName) {
        // set invitees to be the group
    }

    private void chooseTime(Date time){
        _time = time;
    }

    private void chooseEventType(String eventType) {
        _eventType = eventType;
    }

    private String makeTitle(){
        String title = _eventType.concat("At ").concat(_time.toString());
        return title;
    }

    /**
     * Called when "create event" button is clicked
     * should be enabled only if all parameters are chosen (disable button before that)
     * @return EventModel with all chosen parameters
     */
    private EventModel createEvent(){
        String title = makeTitle();
        EventModel newEvent = new EventModel(title, _time, _eventType, _invitees);
        return newEvent;
    }

    private CarouselPicker setupCarousel(int carouselID) {
        CarouselPicker carouselPicker = (CarouselPicker) findViewById(carouselID);

        // Case 1 : To populate the picker with images
//        List<CarouselPicker.PickerItem> imageItems = new ArrayList<>();
//        imageItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_launcher));
//        imageItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_launcher));
//        imageItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_launcher));
//        //Create an adapter
//        CarouselPicker.CarouselViewAdapter imageAdapter = new CarouselPicker.CarouselViewAdapter(this, imageItems, 0);
//        //Set the adapter
//        carouselPicker.setAdapter(imageAdapter);

        //Case 2 : To populate the picker with text
//        List<CarouselPicker.PickerItem> textItems = new ArrayList<>();
//        //20 here represents the textSize in dp, change it to the value you want.
//        textItems.add(new CarouselPicker.TextItem("Beer", 10));
//        textItems.add(new CarouselPicker.TextItem("Pizza", 10));
//        textItems.add(new CarouselPicker.TextItem("Hamburger", 10));
//        textItems.add(new CarouselPicker.TextItem("Movie", 10));
//        textItems.add(new CarouselPicker.TextItem("Other", 10));
//        CarouselPicker.CarouselViewAdapter textAdapter = new CarouselPicker.CarouselViewAdapter(this, textItems, 0);
//        carouselPicker.setAdapter(textAdapter);

        //Case 3 : To populate the picker with both images and text
        List<CarouselPicker.PickerItem> mixItems = new ArrayList<>();
        mixItems.add(new CarouselPicker.DrawableItem(R.drawable.pizza_icon_small));
        mixItems.add(new CarouselPicker.TextItem("hi", 20));
        mixItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_launcher));
        mixItems.add(new CarouselPicker.TextItem("Amit", 20));
        mixItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_launcher));
        mixItems.add(new CarouselPicker.TextItem("Amit", 20));
        CarouselPicker.CarouselViewAdapter mixAdapter = new CarouselPicker.CarouselViewAdapter(this, mixItems, 0);
        carouselPicker.setAdapter(mixAdapter);

        return carouselPicker;
    }

    private void setupCarouselListeners(CarouselPicker carouselPicker) {
        carouselPicker.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //position of the selected item
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
