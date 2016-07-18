package in.capture.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import in.capture.R;
import in.capture.utils.Constants;

public class LaunchActivity extends AppCompatActivity {

    private Animation animZoomin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        TextView textView = (TextView) findViewById(R.id.tvAppname);
        ImageView imageViewSplash = (ImageView) findViewById(R.id.splashimage);
        Button buttonUser= (Button) findViewById(R.id.btnuser);
        Button buttonPhotographer= (Button) findViewById(R.id.btnptotographer);

        final SharedPreferences sharedPreferences  = getSharedPreferences(Constants.SHAREDPREF, MODE_PRIVATE);
        boolean gotomain = sharedPreferences.getBoolean("gotoMain", false);


        if(gotomain)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        buttonUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("gotoMain", true);
                editor.commit();

                Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        buttonPhotographer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("gotoMain", true);
                editor.commit();

                Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                intent.putExtra("gotoLogin", true);
                startActivity(intent);
            }
        });
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Adequate-ExtraLight.ttf");
        textView.setTypeface(typeface);

        animZoomin = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.zoom_in);
        imageViewSplash.startAnimation(animZoomin);

    }
}
