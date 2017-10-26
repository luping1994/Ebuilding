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

public class EnergyFragAdapter extends RecyclerView.Adapter<EnergyFragAdapter.ViewHolder> {

    private Context mContext;
    private List<EnergyEntity.EnergyData> datas;

    public EnergyFragAdapter(Context mContext, List<EnergyEntity.EnergyData> datas, OnitemClickListener  listener) {
        this.mContext = mContext;
        this.datas = datas;
        this.listener = listener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_energy, null, false);

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
        private TextView ia;
        private TextView ib;
        private TextView ic;
        private TextView va;
        private TextView vb;
        private TextView vc;
        private TextView aap;
        private TextView bap;
        private TextView cap;
        private TextView arp;
        private TextView brp;
        private TextView crp;
        private TextView pf;
        private TextView name;
        private CardView root;

        public ViewHolder(View itemView) {
            super(itemView);
            ia = (TextView) itemView.findViewById(R.id.IA);
            ib = (TextView) itemView.findViewById(R.id.IB);
            ic = (TextView) itemView.findViewById(R.id.IC);
            va = (TextView) itemView.findViewById(R.id.VA);
            vb = (TextView) itemView.findViewById(R.id.VB);
            vc = (TextView) itemView.findViewById(R.id.VC);
            aap = (TextView) itemView.findViewById(R.id.AAP);
            bap = (TextView) itemView.findViewById(R.id.BAP);
            cap = (TextView) itemView.findViewById(R.id.CAP);
            arp = (TextView) itemView.findViewById(R.id.ARP);
            brp = (TextView) itemView.findViewById(R.id.BRP);
            crp = (TextView) itemView.findViewById(R.id.CRP);
            pf = (TextView) itemView.findViewById(R.id.pf);
            name = (TextView) itemView.findViewById(R.id.name);

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
            ia.setText("A相电流：" + datas.get(position).ammeter3.IA + "A");
            ib.setText("B相电流：" + datas.get(position).ammeter3.IB + "A");
            ic.setText("C相电流：" + datas.get(position).ammeter3.IC + "A");

            va.setText("A相电压：" + datas.get(position).ammeter3.VolA + "V");
            vb.setText("B相电压：" + datas.get(position).ammeter3.VolB + "V");
            vc.setText("C相电压：" + datas.get(position).ammeter3.VolC + "V");

            aap.setText("A相有功功率：" + datas.get(position).ammeter3.ActivePowerA + "W");
            bap.setText("B相有功功率：" + datas.get(position).ammeter3.ActivePowerB + "W");
            cap.setText("C相有功功率：" + datas.get(position).ammeter3.ActivePowerC + "W");

            arp.setText("A相无功功率：" + datas.get(position).ammeter3.ReactivePowerA + "W");
            brp.setText("B相无功功率：" + datas.get(position).ammeter3.ReactivePowerB + "W");
            crp.setText("C相无功功率：" + datas.get(position).ammeter3.ReactivePowerC + "W");


            pf.setText("功率因数：" + datas.get(position).ammeter3.PowerFactor);
            name.setText("设备名称：" + datas.get(position).name);


        }
    }


    private OnitemClickListener listener;
    public interface OnitemClickListener{
        void onItemClik(int position);
    }
}
