package net.suntrans.ebuilding.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.bean.AreaEntity;
import java.util.List;

/**
 * Created by Looney on 2017/5/23.
 */

public class AddSenceDevGrpAdapter extends BaseExpandableListAdapter {
    private List<AreaEntity.AreaFloor> datas;
    private Context mContext;

    public AddSenceDevGrpAdapter(List<AreaEntity.AreaFloor> datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;

    }

    @Override
    public int getGroupCount() {
        return datas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return datas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return 1;
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
            groupHolder = (GroupHolder) view.getTag();
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_group, parent, false);
            groupHolder = new GroupHolder(view);
            view.setTag(groupHolder);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_children, parent, false);
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
        CheckBox state;
        String[] colors = new String[]{"#f99e5b", "#d3e4ad", "#94c9d6"};
        TextView count;

        public GroupHolder(View view) {
            mName = (TextView) view.findViewById(R.id.name);
            state = (CheckBox) view.findViewById(R.id.checkbox);
            count = (TextView) view.findViewById(R.id.count);

        }

        public void setData(final int groupPosition) {
            mName.setText(datas.get(groupPosition).name);
            state.setChecked(datas.get(groupPosition).isCheck);
            state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    notifyDataSetChanged();
                }
            });
        }
    }

    public class ChildHolder {
        ImageView mImage;
        TextView mText;
        CheckBox state;

        public ChildHolder(View view) {
            mText = (TextView) view.findViewById(R.id.name);
            state = (CheckBox) view.findViewById(R.id.checkbox);

        }

        public void setData(final int groupPosition, final int childPosition) {


        }
    }


    public void setOnItemClickListener(AddSenceDevGrpAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private onItemClickListener onItemClickListener;

    public interface onItemClickListener {
        void onParentChekBoxCheckedListener(CheckBox checkBox);

        void onChildrenChekBoxCheckedListener(CheckBox checkBox);
    }
}
