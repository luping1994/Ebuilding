package net.suntrans.ebuilding.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Looney on 2017/9/7.
 * 一：沉稳
 * （1）不要随便显露你的情绪。
 * （2）不要逢人就诉说你的困难和遭遇。
 * （3）在征询别人的意见之前，自己先思考，但不要先讲。
 * （4）不要一有机会就唠叨你的不满。
 * （5）重要的决定尽量有别人商量，最好隔一天再发布。
 * （6）讲话不要有任何的慌张，走路也是。
 * （7）自信是好，但是别忽略任何人的想法。
 * （8）人无高低，不要一副拽拽的样子，对人对事，别忘了礼貌。你没有比任何人优秀。
 * <p>
 * 二：细心
 * （1）对身边发生的事情，常思考它们的因果关系。
 * （2）对做不到位的问题，要发掘它们的根本症结。
 * （3）对习以为常的做事方法，要有改进或优化的建议。
 * （4）做什么事情都要养成有条不紊和井然有序的习惯。
 * （5）经常去找几个别人看不出来的毛病或弊端。
 * （6）自己要随时随地对有所不足的地方补位。
 * <p>
 * 三：胆识
 * （1）不要常用缺乏自信的词句。
 * （2）不要常常反悔，轻易推翻已经决定的事。
 * （3）在众人争执不休时，不要没有主见。
 * （4）整体氛围低落时，你要乐观、阳光。
 * （5）做任何事情都要用心，因为有人在看着你。
 * （6）事情不顺的时候，歇口气，重新寻找突破口，就结束也要干净利落。
 * <p>
 * 四：大度
 * （1）不要刻意把有可能是伙伴的人变成对手。
 * （2）对别人的小过失、小错误不要斤斤计较。
 * （3）在金钱上要大方，学习三施（财施、法施、无畏施）。
 * （4）不要有权力的傲慢和知识的偏见。
 * （5）任何成果和成就都应和别人分享。
 * <p>
 * 五：诚信
 * （1）做不到的事情不要说，说了就努力做到。
 * （2）虚的口号或标语不要常挂嘴上。
 * （3）停止一切“不道德”的手段。
 * （4）耍弄小聪明，要不得！
 * <p>
 * 六：担当
 * （1）检讨任何过失的时候，先从自身或自己人开始反省。
 * （2）事情结束后，先审查过错，再列述功劳。
 * （3）一个计划，要统筹全局，规划未来。
 * （4）勇于承担责任所造成的损失。
 * <p>
 * 七：内涵
 * （1）学习各方面的知识，虚心观察周围的事物。眼界宽阔。
 * （2）了解自己，培养属于自己的审美观。
 * （3）笑对生活。懒惰要不得。培养健康的生活习惯。
 * （4）不要盲目的做任何事。要有目标。
 * （5）不仅仅只关注内在美，外在美也很重要。
 * （6）不要整天的对着电脑，玩着无聊的东西。
 * （7）理智的判断，学会控制情绪。
 */

public class HisEntity extends RespondBody<List<HisEntity.EleParmHisItem>> {

    public static class EleParmHisItem implements Parcelable {

        public String GetTime;

        @SerializedName("value")
        public String Value;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.GetTime);
            dest.writeString(this.Value);
        }

        public EleParmHisItem() {
        }

        protected EleParmHisItem(Parcel in) {
            this.GetTime = in.readString();
            this.Value = in.readString();
        }

        public static final Creator<EleParmHisItem> CREATOR = new Creator<EleParmHisItem>() {
            @Override
            public EleParmHisItem createFromParcel(Parcel source) {
                return new EleParmHisItem(source);
            }

            @Override
            public EleParmHisItem[] newArray(int size) {
                return new EleParmHisItem[size];
            }
        };
    }


    public static class ABCInfo{
        public String name;
        public List<EleParmHisItem> data;

    }

}
