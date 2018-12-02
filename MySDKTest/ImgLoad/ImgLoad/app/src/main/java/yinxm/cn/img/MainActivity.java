package yinxm.cn.img;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    String url = "http://img5.imgtn.bdimg.com/it/u=987087732,826991765&fm=21&gp=0.jpg";
    ImageView img;
    RecyclerView recyclerview;
    RVAdapter mRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.img);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        initView();
    }

    public void initView() {
        List<String> list = new ArrayList<>();
        for (int i=0; i<10; i++) {
            list.add(url);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(linearLayoutManager);

        recyclerview.setItemAnimator(new DefaultItemAnimator());

        //默认没有分割线(DividerItemDecoration)，按需自己实现，添加自定义分割线
//        recyclerview.addItemDecoration(new DividerItemDecoration(
//                getBaseContext(), DividerItemDecoration.VERTICAL));
        recyclerview.addItemDecoration(new CustomDecoration(this, CustomDecoration.VERTICAL_LIST, R.drawable.divider_love, 50));
//        recyclerview.addItemDecoration(new CustomDividerItemDecoration(
//                getBaseContext(), CustomDividerItemDecoration.HORIZONTAL_LIST));
//        recyclerview.addItemDecoration(new MyRecyclerDivider());



//        recyclerview.addOnScrollListener(mOnScrollListener);


        mRVAdapter = new RVAdapter(this, list);
        recyclerview.setAdapter(mRVAdapter);

        LogUtil.d("recyclerview="+recyclerview+", list="+list);
    }

    public void glideLoad(View view) {
        Glide.with(this)
                .load(url)
                .placeholder(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img);
    }

    public void frescoLoad(View view) {
        startActivity(new Intent(MainActivity.this, FrescoTestActivity.class));
    }

   static class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder> {
        private List<String> mList = null;
        private Context mContext;
        public RVAdapter(Context context, List<String> list) {
            mList = list;
            mContext = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_rv, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            LogUtil.d("onCreateViewHolder="+myViewHolder);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            LogUtil.d("position="+position+", holder="+holder);
            holder.tv.setText(mList.get(position));
//            Glide.with(mContext)
//                    .load(mList.get(position))
//                    .into(holder.img);
        }

        @Override
        public int getItemCount() {
            LogUtil.d("getItemCount="+mList.size());

            return mList == null ? 0 : mList.size();
        }

        static class MyViewHolder extends RecyclerView.ViewHolder {
            private ImageView img;
            private TextView tv;
            public MyViewHolder(View itemView) {
                super(itemView);
                img = (ImageView) itemView.findViewById(R.id.img);
                tv = (TextView) itemView.findViewById(R.id.tv);
            }
        }
    }
}
