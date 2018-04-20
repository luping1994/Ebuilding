package net.suntrans.ebuilding.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.api.RetrofitHelper;
import net.suntrans.ebuilding.bean.AreaEntity;

import java.util.List;

/**
 * Created by Looney on 2017/5/23.
 */

public class AreaAdapter extends BaseExpandableListAdapter {
    private List<AreaEntity.AreaFloor> datas;
    private Context mContext;

    public AreaAdapter(List<AreaEntity.AreaFloor> datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;

    }

    @Override
    public int getGroupCount() {
        return datas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return datas.get(groupPosition).sub.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return datas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return datas.get(groupPosition).sub.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = null;
        GroupHolder groupHolder = null;
        if (convertView != null) {
            view = convertView;
            groupHolder = (GroupHolder) view.getTag(R.id.name);
            view.setTag(R.id.root,groupPosition);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_group_area, parent, false);
            groupHolder = new GroupHolder(view);
            view.setTag(R.id.name,groupHolder);
            view.setTag(R.id.root,groupPosition);
        }
        groupHolder.setData(groupPosition);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = null;
        ChildHolder holder = null;
        if (convertView != null) {
            view = convertView;
            holder = (ChildHolder) view.getTag();
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_children_area, parent, false);
            holder = new ChildHolder(view);
            view.setTag(holder);
        }
        holder.setData(groupPosition, childPosition);
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public class GroupHolder {
        TextView mName;
        ImageView mImage;
        String[] colors = new String[]{"#f99e5b", "#d3e4ad", "#94c9d6"};
        TextView count;
        private final View root;

        public GroupHolder(View view) {
            mName = (TextView) view.findViewById(R.id.name);
            mImage = (ImageView) view.findViewById(R.id.imageView);

            root = view.findViewById(R.id.root);
        }

        public void setData(final int groupPosition) {

            mName.setText(datas.get(groupPosition).name);
//            mImage.setBackgroundColor(Color.parseColor(colors[groupPosition % 3]));
//            count.setText(datas.get(groupPosition).sub.size() + "");
        }
    }

    public class ChildHolder {
        ImageView mImage;
        TextView mText;

        public ChildHolder(View view) {
            mText = (TextView) view.findViewById(R.id.name);
            mImage = (ImageView) view.findViewById(R.id.imageView);
        }

        public void setData(final int groupPosition, final int childPosition) {
            mText.setText(datas.get(groupPosition).sub.get(childPosition).name);
            Glide.with(mContext)
//                    .load(datas.get(groupPosition).sub.get(childPosition).img_url)
                    .load(R.drawable.house)
                    .centerCrop()
                    .crossFade()
                    .into(mImage);

        }
    }

    public void setOnGroupLongClickListener(AreaAdapter.onParentLongClickListener listener) {

        this.listener = listener;
    }

    private onParentLongClickListener listener;

    public interface onParentLongClickListener {
        void onLongParentClick(int parentPosition);

    }


}
