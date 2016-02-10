package com.fartans.keyplus;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fartans.keyplus.Model.Vault;

import java.util.List;

public class VaultListAdapter extends BaseAdapter {
    private List<Vault> VaultList;
    private LayoutInflater inflater;
    private Activity activity;

    public VaultListAdapter(Activity activity, List<Vault> vaultList) {
        this.VaultList=vaultList;
        this.activity=activity;
    }
    @Override
    public int getCount() {
        return VaultList.size();
    }

    @Override
    public Object getItem(int position) {
        return VaultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.listview_vault_row_item, null);

        TextView name = (TextView) convertView.findViewById(R.id.vault_name);
        TextView keys = (TextView) convertView.findViewById(R.id.vault_kyes);
        name.setText(String.valueOf(VaultList.get(position).getName()));
        keys.setText(String.valueOf(VaultList.get(position).getKeyNumber()));
        ImageView icon=(ImageView)convertView.findViewById(R.id.vault_icon);
        icon.setImageResource(R.drawable.icon_vault);
        return convertView;

    }
}
