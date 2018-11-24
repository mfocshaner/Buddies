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

        CarouselPicker whoCarouselPicker = setupWhoCarousel();
        setupCarouselListeners(whoCarouselPicker);

        CarouselPicker whatCarouselPicker = setupWhatCarousel();
        setupCarouselListeners(whatCarouselPicker);

        CarouselPicker carouselPicker = setupWhenCarousel();
        setupCarouselListeners(carouselPicker);


    }

    /// parameters to be passed to new event
    private Date _time;
    private String _eventType; // not well defined
    private List<String> _invitees; // will maybe change if invitees become "People" objects,

    public void chooseGroup(String groupName) {
        // set invitees to be the group
    }

    public void chooseTime(Date time){
        _time = time;
    }

    public void chooseEventType(String eventType) {
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

    private CarouselPicker setupWhoCarousel() {
        CarouselPicker carouselPicker = (CarouselPicker) findViewById(R.id.who_carousel);

        // Case 1 : To populate the picker with images
//        List<CarouselPicker.PickerItem> imageItems = new ArrayList<>();
//        imageItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_launcher));
//        imageItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_launcher));
//        imageItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_launcher));
//        //Create an adapter
//        CarouselPicker.CarouselViewAdapter imageAdapter = new CarouselPicker.CarouselViewAdapter(this, imageItems, 0);
//        //Set the adapter
//        carouselPicker.setAdapter(imageAdapter);

        //Case 3 : To populate the picker with both images and text
        List<CarouselPicker.PickerItem> mixItems = new ArrayList<>();
        mixItems.add(new CarouselPicker.DrawableItem(R.drawable.pizza_icon_small));
        mixItems.add(new CarouselPicker.TextItem("hi", 20));
        mixItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_launcher));
        mixItems.add(new CarouselPicker.TextItem("Amit", 20));
        mixItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_launcher));
        mixItems.add(new CarouselPicker.TextItem("<3", 20));
        CarouselPicker.CarouselViewAdapter mixAdapter = new CarouselPicker.CarouselViewAdapter(this, mixItems, 0);
        carouselPicker.setAdapter(mixAdapter);

        return carouselPicker;
    }

    private CarouselPicker setupWhatCarousel() {
        CarouselPicker carouselPicker = (CarouselPicker) findViewById(R.id.what_carousel);

        // Case 1 : To populate the picker with images
//        List<CarouselPicker.PickerItem> imageItems = new ArrayList<>();
//        imageItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_launcher));
//        imageItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_launcher));
//        imageItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_launcher));
//        //Create an adapter
//        CarouselPicker.CarouselViewAdapter imageAdapter = new CarouselPicker.CarouselViewAdapter(this, imageItems, 0);
//        //Set the adapter
//        carouselPicker.setAdapter(imageAdapter);

        //Case 3 : To populate the picker with both images and text
        List<CarouselPicker.PickerItem> mixItems = new ArrayList<>();
        mixItems.add(new CarouselPicker.DrawableItem(R.drawable.pizza_icon_small));
        mixItems.add(new CarouselPicker.TextItem("dinner", 20));
        mixItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_launcher));
        mixItems.add(new CarouselPicker.TextItem("movie", 20));
        mixItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_launcher));
        mixItems.add(new CarouselPicker.TextItem("shnatz", 20));
        CarouselPicker.CarouselViewAdapter mixAdapter = new CarouselPicker.CarouselViewAdapter(this, mixItems, 0);
        carouselPicker.setAdapter(mixAdapter);

        return carouselPicker;
    }

    private CarouselPicker setupWhenCarousel() {
        CarouselPicker carouselPicker = (CarouselPicker) findViewById(R.id.when_carousel);

        //Case 2 : To populate the picker with text
        List<CarouselPicker.PickerItem> textItems = new ArrayList<>();
        //20 here represents the textSize in dp, change it to the value you want.
        textItems.add(new CarouselPicker.TextItem("Now", 20));
        textItems.add(new CarouselPicker.TextItem("Tomorrow", 20));
        textItems.add(new CarouselPicker.TextItem("This Week", 20));
        textItems.add(new CarouselPicker.TextItem("Custom", 20));
        CarouselPicker.CarouselViewAdapter textAdapter = new CarouselPicker.CarouselViewAdapter(this, textItems, 0);
        carouselPicker.setAdapter(textAdapter);


        return carouselPicker;
    }



    private void setupCarouselListeners(CarouselPicker carouselPicker) {
        carouselPicker.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                System.out.println("picked: ");
                System.out.println(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
