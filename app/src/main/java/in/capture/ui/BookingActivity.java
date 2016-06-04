package in.capture.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.capture.R;
import in.capture.utils.Constants;
import in.capture.utils.Utility;

public class BookingActivity extends AppCompatActivity {

    private Spinner state;
    private Button btnBook;
    private EditText pin, city, email, phone, adrs1;
    private TextView date, time;
    private Calendar calendar; int year, day, month;
    private int minute;
    private int hour;
    private String photographerEmail, photographerRate;
    private String photographerNam;
    private TextView name, rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        photographerEmail = getIntent().getStringExtra("email");
        photographerRate = getIntent().getStringExtra("rate");
        photographerNam = getIntent().getStringExtra("name");

        name = (TextView) findViewById(R.id.name);
        rate = (TextView) findViewById(R.id.rate);
        name.setText(photographerNam);
        rate.setText(photographerRate);

        email = (EditText) findViewById(R.id.input_email);
         phone = (EditText) findViewById(R.id.input_phone);
         adrs1 = (EditText) findViewById(R.id.input_adr_1);
         city = (EditText) findViewById(R.id.input_city);
         pin = (EditText) findViewById(R.id.input_pincode);
         date = (TextView) findViewById(R.id.input_date);
         time = (TextView) findViewById(R.id.input_time);

        state = (Spinner) findViewById(R.id.state);
        List<String> listCategory = new ArrayList<>();
        listCategory.add("NCR Region");
        listCategory.add("Delhi");
        listCategory.add("Uttar Pradesh");
        listCategory.add("Maharashtra");
        listCategory.add("Andra Pradesh");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.simple_spinner_item, listCategory);
        state.setAdapter(arrayAdapter);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

         hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);


        final Activity activity = this;
        date.setText(day+"/"+(month+1)+"/"+year);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.showDialog(999);
            }
        });

        time.setText( hour + ":" + minute);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(BookingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        btnBook = (Button) findViewById(R.id.bookFinal);
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid())
                {
                    bookRequest();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
          finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private boolean isValid() {
        if(email.getText().length()<5)
            return false;
        if(phone.getText().length() <= 0)
            return false;
        if(adrs1.getText().length()<= 0)
            return false;
        if(city.getText().length()<= 0)
            return false;
        if(pin.getText().length()<= 0)
            return false;
        if(time.getText().length() <= 0)
            return false;
        if(date.getText().length() <= 0)
            return false;
        return true;
    }

    public void bookRequest() {
        String POST_URL = "http://captureapp.in/api/photographer_booking.php/";

        final ProgressDialog progressDialog =  new ProgressDialog(BookingActivity.this);
        progressDialog.setMessage("Booking...");
        progressDialog.show();

        final JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("email", email.getText().toString().toLowerCase());
            jsonObject.put("phone", phone.getText().toString());
            jsonObject.put("address", adrs1.getText().toString().toLowerCase());
            jsonObject.put("city", city.getText().toString());
            jsonObject.put("pincode", pin.getText().toString());
            jsonObject.put("state", state.getSelectedItem().toString().toLowerCase());
            jsonObject.put("date", date.getText().toString().toLowerCase());
            jsonObject.put("photographerEmail", photographerEmail.toLowerCase());
            jsonObject.put("rate", rate);





        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest stringRequest = new JsonObjectRequest(POST_URL, jsonObject ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("response", response.toString());
                        progressDialog.dismiss();
                        if(response.optInt("success") == 0)
                            Utility.showToast(BookingActivity.this, "Something went wrong");
                        else
                        {

                        }


                    }




                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(BookingActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(BookingActivity.this);
        requestQueue.add(stringRequest);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
           date.setText(day+"/"+(month+1)+"/"+year);

        }
    };

}
