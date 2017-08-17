package net.suntrans.ebuilding.activity;

import android.content.Intent;
import android.graphics.Paint;
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

        findViewById(R.id.fanhui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView textView = (TextView)findViewById(R.id.version);
        textView .setText("版本号:"+ BuildConfig.VERSION_NAME);
        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share("分享智能配电");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public  void share(String desc)
    {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,"");
        shareIntent.putExtra(Intent.EXTRA_TEXT,  "智能配电app下载地址:https://www.pgyer.com/5xcP");
        shareIntent.setType("text/plain");
       startActivity(Intent.createChooser(shareIntent, desc));
    }


}
