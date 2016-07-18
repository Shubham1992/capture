package in.capture.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import in.capture.model.BookingModel;
import in.capture.model.PhotoModel;
import in.capture.model.PhotographerModel;

/**
 * Created by Shubham.Goel on 19-05-2016.
 */
public class Parser {


    public static ArrayList<PhotographerModel> parse_photographer_list_data(JSONObject  jsonObject)
    {
        ArrayList<PhotographerModel> modelArrayList = new ArrayList<>();

        if(jsonObject.optInt("success") != 1)
            return null;
        JSONArray photographers = jsonObject.optJSONArray("photographers");
        for (int i = 0; i < photographers.length(); i++) {
            JSONObject photographer = photographers.optJSONObject(i);

            PhotographerModel photographerModel = new PhotographerModel();
            photographerModel.setName(photographer.optString("name"));
            photographerModel.setEmail(photographer.optString("email"));
            photographerModel.setRate(photographer.optString("rate"));
            photographerModel.setProfilepic(photographer.optString("profilepic"));
            photographerModel.setCoverpic(photographer.optString("coverpic"));
            photographerModel.setLocation(photographer.optString("location"));


            modelArrayList.add(photographerModel);
        }


        return modelArrayList;
    }

    public static PhotographerModel parse_photographer_data(JSONObject  jsonObject)
    {

        if(jsonObject.optInt("success") != 1)
            return null;
        PhotographerModel photographerModel = new PhotographerModel();

        JSONArray photographers = jsonObject.optJSONArray("photographers");
            JSONObject photographer = photographers.optJSONObject(0);


            photographerModel.setName(photographer.optString("name"));
            photographerModel.setEmail(photographer.optString("email"));
            photographerModel.setRate(photographer.optString("rate"));
            photographerModel.setProfilepic(photographer.optString("profilepic"));
            photographerModel.setCoverpic(photographer.optString("coverpic"));
            photographerModel.setLocation(photographer.optString("location"));




        return photographerModel;
    }

    public static ArrayList<PhotoModel> parse_all_photos(JSONObject response) {

        ArrayList<PhotoModel> photoModels = new ArrayList<>();

        JSONArray jsonArray = response.optJSONArray("photographs");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            PhotoModel photoModel = new PhotoModel();
            photoModel.setId(jsonObject.optString("id"));
            photoModel.setEmail(jsonObject.optString("email"));
            photoModel.setImage(jsonObject.optString("image"));

            photoModels.add(photoModel);
        }
        return photoModels;
    }

    public static ArrayList<BookingModel> parse_booking_list_data(JSONObject response) {
        ArrayList<BookingModel> bookingModels = new ArrayList<>();

        JSONArray jsonArray = response.optJSONArray("results");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            BookingModel bookingModel = new BookingModel();
            bookingModel.setId(jsonObject.optString("id"));
            bookingModel.setPname(jsonObject.optString("name"));
            bookingModel.setImage(jsonObject.optString("image"));
            bookingModel.setDate(jsonObject.optString("date"));
            bookingModel.setRate(jsonObject.optString("rate"));

            bookingModels.add(bookingModel);
        }
        return bookingModels;
    }
}
