package net.suntrans.ebuilding.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Looney on 2017/4/20.
 */

public class UserInfo {

    public static class User   implements Parcelable {
        public String id;
        public String username;
        public String nickname;

        public String title;
        public List<String> img_url;

        public String getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getNickname() {
            return nickname;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.username);
            dest.writeString(this.nickname);
        }

        public User() {
        }

        protected User(Parcel in) {
            this.id = in.readString();
            this.username = in.readString();
            this.nickname = in.readString();
        }

        public static final Creator<User> CREATOR = new Creator<User>() {
            @Override
            public User createFromParcel(Parcel source) {
                return new User(source);
            }

            @Override
            public User[] newArray(int size) {
                return new User[size];
            }
        };
    }



    /**
     *
     * new api
     */
    public User data;
    public int code;

}
