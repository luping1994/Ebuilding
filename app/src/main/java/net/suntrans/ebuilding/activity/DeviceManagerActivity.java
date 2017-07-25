package net.suntrans.ebuilding.activity;

import android.os.Bundle;

import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.fragment.per.DevicesManagerFragment;

/**
 * Created by Looney on 2017/4/20.
 */

public class DeviceManagerActivity extends BasedActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devicemanager);
        DevicesManagerFragment fragment = DevicesManagerFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }


}
