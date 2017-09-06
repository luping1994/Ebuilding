package net.suntrans.ebuilding.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Looney on 2017/4/20.
 */

public class UserInfo extends RespondBody<UserInfo.User>{

    public static class User   {
        public String id;
        public String username;
        public String nickname;
        public String avatar_url;
        public String title;
        public List<String> img_url;
    }

    /**
     *
     * new api
     */
//    public User data;
//    public int code;

}
