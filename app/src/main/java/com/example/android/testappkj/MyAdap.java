
package com.example.android.testappkj;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyAdap extends RecyclerView.Adapter<MyAdap.MyViewHolder> {

    private String[] mDataset;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;
        public MyViewHolder(TextView  v){
            super(v);
            mTextView =v;

        }
    }

    public MyAdap(String[] myDataSet){
        mDataset=myDataSet;}
    @Override
    public MyAdap.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset[position]);

    }
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
