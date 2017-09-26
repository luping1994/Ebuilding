package net.suntrans.ebuilding.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Looney on 2017/9/26.
 */

public class SceneTimeResult implements Parcelable {

    /**
     * id : 4
     * scene_id : 2
     * status : 1
     * type : 0
     * timer : 13:39
     * created_at : 2017-09-23 13:14:11
     * updated_at : 2017-09-23 13:38:00
     */
    public String id;
    public String scene_id;
    public String status;
    public String type;
    public String timer;
    public String created_at;
    public String updated_at;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.scene_id);
        dest.writeString(this.status);
        dest.writeString(this.type);
        dest.writeString(this.timer);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
    }

    public SceneTimeResult() {
    }

    protected SceneTimeResult(Parcel in) {
        this.id = in.readString();
        this.scene_id = in.readString();
        this.status = in.readString();
        this.type = in.readString();
        this.timer = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
    }

    public static final Parcelable.Creator<SceneTimeResult> CREATOR = new Parcelable.Creator<SceneTimeResult>() {
        @Override
        public SceneTimeResult createFromParcel(Parcel source) {
            return new SceneTimeResult(source);
        }

        @Override
        public SceneTimeResult[] newArray(int size) {
            return new SceneTimeResult[size];
        }
    };
}
