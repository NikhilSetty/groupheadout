package com.fartans.keyplus.ui.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fartans.keyplus.Model.AppKeyModel;
import com.fartans.keyplus.R;

import java.util.List;

/**
 * @author hitesh.sethiya on 07/02/16.
 */
public class KeyPreferencesAdapter extends ArrayAdapter<AppKeyModel> {

    private Context mContext;
    private List<AppKeyModel> mAppKeyModelList;
    private LayoutInflater mInflater;
    /**
     * Constructor
     *
     * @param context            The current context.
     * @param resource           The resource ID for a layout file containing a layout to use when
     *                           instantiating views.
     * @param objects            The objects to represent in the ListView.
     */
    public KeyPreferencesAdapter(Context context, int resource, List<AppKeyModel> objects) {
        super(context, resource, objects);
        mContext = context;
        mAppKeyModelList = objects;
        this.mInflater = (LayoutInflater) this.mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        return super.getCount();
    }

    /**
     * {@inheritDoc}
     *
     * @param position
     */
    @Override
    public AppKeyModel getItem(int position) {
        return super.getItem(position);
    }

    /**
     * Returns the position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     * @return The position of the specified item.
     */
    @Override
    public int getPosition(AppKeyModel item) {
        return super.getPosition(item);
    }

    /**
     * {@inheritDoc}
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AppKeyModel appKeyModel = getItem(position);
        ViewHolder theViewHolder;
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.app_key_list_item,parent,false);
            theViewHolder = new ViewHolder(convertView);
            convertView.setTag(theViewHolder);
        } else {
            theViewHolder = (ViewHolder) convertView.getTag();
        }

        theViewHolder.imageView.setImageResource(appKeyModel.getImageResource());
        theViewHolder.appNameTv.setText(appKeyModel.getAppName());
        theViewHolder.keyCodeTv.setText((char)appKeyModel.getKeyCode());

        return convertView;
    }

    class ViewHolder {
        private ImageView imageView;
        private TextView appNameTv;
        private TextView keyCodeTv;

        public ViewHolder(View convertView) {
            if(convertView != null) {
                imageView = (ImageView) convertView.findViewById(R.id.app_key_image_view);
                appNameTv = (TextView) convertView.findViewById(R.id.app_name_text_view);
                keyCodeTv = (TextView) convertView.findViewById(R.id.keypress_text_view);

            } else {
                throw new RuntimeException(
                        "Convertview cannot be null for view holder in com.headout.hack.grouphead.ui.activity.adapter.KeyPreferencesAdapter");
            }
        }
    }
}
