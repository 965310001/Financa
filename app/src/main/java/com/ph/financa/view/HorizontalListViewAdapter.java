package com.ph.financa.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ph.financa.R;
import com.ph.financa.activity.bean.SelectBean;

import java.util.List;

public class HorizontalListViewAdapter extends BaseAdapter {

    private final Context context;
    private final int screenWidth;
    private final List<SelectBean> data;

    public HorizontalListViewAdapter(Context applicationContext, int screenWidth, List<SelectBean> data) {
        this.context = applicationContext;
        this.screenWidth = screenWidth;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //计算每条的宽度
        ViewGroup.LayoutParams layoutParams = holder.ll.getLayoutParams();
        layoutParams.width = screenWidth / 5 * 2;
        layoutParams.height = convertView.getHeight();
        holder.ll.setLayoutParams(layoutParams);
//        ((LinearLayout.LayoutParams) layoutParams).setMargins(16, 0, 16, 0);


        SelectBean bean = data.get(position);

        holder.tvYear.setText(bean.getName());
        holder.tvPrice.setText(bean.getPrice());
        holder.tvOldPrice.setText(bean.getOriginalPrice());

//        ViewHolder holder;
//        if (convertView == null) {
//            holder = new ViewHolder();
//            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.horizontal_list_item, null);
//            holder.mImage = (ImageView) convertView.findViewById(R.id.img_list_item);
//            holder.mTitle = (TextView) convertView.findViewById(R.id.text_list_item);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        if (position == selectIndex) {
//            convertView.setSelected(true);
//        } else {
//            convertView.setSelected(false);
//        }
//
//        holder.mTitle.setText(mTitles[position]);
//        iconBitmap = getPropThumnail(mIconIDs[position]);
//        holder.mImage.setImageBitmap(iconBitmap);

        return convertView;
    }

    class ViewHolder {
        TextView tvYear;
        TextView tvPrice;
        TextView tvOldPrice;
        LinearLayout ll;

        public ViewHolder(View view) {
            tvYear = view.findViewById(R.id.tv_year);
            tvPrice = view.findViewById(R.id.tv_price);
            tvOldPrice = view.findViewById(R.id.tv_old_price);
            ll = view.findViewById(R.id.ll);
        }
    }
}


