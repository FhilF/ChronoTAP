package com.switso.itap;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.switso.itap.Form.FormList;
import com.switso.itap.Form.FormListAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RawLogsAdapter extends RecyclerView.Adapter<RawLogsAdapter.ViewHolder> {

    public static final String TAG = "Adapter";
    Context mContext;
    private LayoutInflater mInflater;
    List<RawLogsList> list;



    public RawLogsAdapter(Context ctx, List<RawLogsList> list) {
        this.mContext = ctx;
        this.list = list;
        mInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RawLogsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.rawlogsitem, viewGroup, false);
        final RawLogsAdapter.ViewHolder holder = new RawLogsAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RawLogsAdapter.ViewHolder holder, int i) {
        RawLogsList rawLogsList = list.get(i);
        SimpleDateFormat formatWorkDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());

        Date employeeWorkdate = null;
        try {
            employeeWorkdate = formatWorkDate.parse(rawLogsList.getWorkdate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        formatWorkDate = new SimpleDateFormat("MMM dd, yyyy hh:mm EEE");
        holder.txtWorkdate.setText(formatWorkDate.format(employeeWorkdate));
        holder.txtIndicator.setText(rawLogsList.getIndicator());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtWorkdate, txtIndicator;

        LinearLayout rootView;//newly added field
        public ViewHolder(View itemView) {
            super(itemView);

            txtWorkdate = itemView.findViewById(R.id.TxtWorkdate);
            txtIndicator = itemView.findViewById(R.id.TxtIndicator);

        }
    }
}
