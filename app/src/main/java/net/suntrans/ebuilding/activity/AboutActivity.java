package net.suntrans.ebuilding.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import net.suntrans.ebuilding.BuildConfig;
import net.suntrans.ebuilding.R;

/**
 * Created by Looney on 2017/7/24.
 */

public class AboutActivity extends BasedActivity {
    private TextView guangwang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        guangwang = (TextView) findViewById(R.id.guangwang);
        guangwang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://www.suntrans.net/");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        findViewById(R.id.fanhui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView textView = (TextView)findViewById(R.id.version);
        textView .setText("版本号:"+ BuildConfig.VERSION_NAME);
    }
}
