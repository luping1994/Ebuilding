package net.suntrans.guojj.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.suntrans.guojj.R;
import net.suntrans.guojj.bean.EnergyEntity;
import java.util.List;

/**
 * Created by Looney on 2017/7/12.
 */

public class EnergyFragAdapter2 extends RecyclerView.Adapter<EnergyFragAdapter2.ViewHolder> {

    private Context mContext;
    private List<EnergyEntity.EnergyData> datas;

    public EnergyFragAdapter2(Context mContext, List<EnergyEntity.EnergyData> datas, OnitemClickListener  listener) {
        this.mContext = mContext;
        this.datas = datas;
        this.listener = listener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_energy2, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);


    }


    @Override
    public int getItemCount() {
//        return da;
        return datas == null ? 0 : datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
//        private TextView ia;
//        private TextView ib;
//        private TextView ic;
//        private TextView va;
//        private TextView vb;
//        private TextView vc;
//        private TextView aap;
//        private TextView bap;
//        private TextView cap;
//        private TextView arp;
//        private TextView brp;
        private CardView root;
        private TextView allPower;
        private TextView yesterday;
        private TextView today;
        private TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            allPower = (TextView) itemView.findViewById(R.id.allPower);
            yesterday = (TextView) itemView.findViewById(R.id.yesterday);
            today = (TextView) itemView.findViewById(R.id.today);
            name = (TextView) itemView.findViewById(R.id.name);
//            va = (TextView) itemView.findViewById(R.id.VA);
//            vb = (TextView) itemView.findViewById(R.id.VB);
//            vc = (TextView) itemView.findViewById(R.id.VC);
//            aap = (TextView) itemView.findViewById(R.id.AAP);
//            bap = (TextView) itemView.findViewById(R.id.BAP);
//            cap = (TextView) itemView.findViewById(R.id.CAP);
//            arp = (TextView) itemView.findViewById(R.id.ARP);
//            brp = (TextView) itemView.findViewById(R.id.BRP);
//            crp = (TextView) itemView.findViewById(R.id.CRP);
//            pf = (TextView) itemView.findViewById(R.id.pf);
//            name = (TextView) itemView.findViewById(R.id.name);
//
            root = (CardView) itemView.findViewById(R.id.root);

            if (listener != null) {
                root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onItemClik(getAdapterPosition());
                    }
                });
            }
        }

        public void setData(int position) {

            today.setText(datas.get(position).today+"度");
            yesterday.setText(datas.get(position).yesterday+"度");
            allPower.setText(datas.get(position).ammeter3.EletricityValue+"度");
            name.setText(datas.get(position).name);

        }
    }


    private OnitemClickListener listener;
    public interface OnitemClickListener{
        void onItemClik(int position);
    }
}
