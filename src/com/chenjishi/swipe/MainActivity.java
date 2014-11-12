package com.chenjishi.swipe;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.chenjishi.swipe.library.SwipeRefreshLayout;

public class MainActivity extends Activity implements SwipeRefreshLayout.OnSwipeRefreshListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.pull_refresh_view);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        ListView listView = (ListView) findViewById(R.id.list_view);
        SimpleListAdapter listAdapter = new SimpleListAdapter(this);
        listView.setAdapter(listAdapter);
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);

        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 3000L);
    }

    private static class SimpleListAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public SimpleListAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public String getItem(int i) {
            return "item " + i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View itemView;

            if (view == null) {
                itemView = mInflater.inflate(R.layout.list_item, viewGroup, false);
            } else {
                itemView = view;
            }

            ((TextView) itemView.findViewById(R.id.label)).setText(getItem(i));


            return itemView;
        }
    }
}
