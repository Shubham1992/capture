package in.capture.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;

/**
 * Created by naveen on 6/29/2015.
 */
public class Interfaces {


    public interface removeImg {
        void clickedRemoveImg();
        void clickedRotateImg();
    }

    public interface viewCategory{
        public void onClickViewAll(String id);
    }

    public interface viewProduct{
        public void viewProduct(String id);
    }
    public interface changeTitle{
        public void setTitle(String title, float priceMax, float priceMin);
    }
    public interface filter{
        public void filterBy(int sortType, boolean excludeOutOfStock, float priceMin, float priceMax);
    }

    public interface DeleteAdd{
        public void deleteAddress(String id);
    }



    /**
     * interface for callbacks when Get or Post request are created
     */
    public interface PutCallbacks {

        public void postexecute(String url, int status);

        public void preexecute(String url);

        public void processResponse(HttpResponse response, String url);

        public HttpPut preparePutData(String url, HttpPut httpPost);

    }





}
