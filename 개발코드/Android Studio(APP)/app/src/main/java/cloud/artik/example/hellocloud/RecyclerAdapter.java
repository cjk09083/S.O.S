package cloud.artik.example.hellocloud;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by charlie on 2017. 4. 24..
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {
    ArrayList<RecyclerItem> mItems;
    private int focusedItem = 0;

    public RecyclerAdapter(ArrayList<RecyclerItem> items){
        mItems = items;
    }


    public void setFocusedItem(int position){
        notifyItemChanged(focusedItem);
        focusedItem = position;
        notifyItemChanged(focusedItem);
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();

                // Return false if scrolled to the bounds and allow focus to move off the list
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        return tryMoveSelection(lm, 1);
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        return tryMoveSelection(lm, -1);
                    }
                }

                return false;
            }
        });
    }

    private boolean tryMoveSelection(RecyclerView.LayoutManager lm, int direction) {
        int tryFocusItem = focusedItem + direction;

        // If still within valid bounds, move the selection, notify to redraw, and scroll
        if (tryFocusItem >= 0 && tryFocusItem < getItemCount()) {
            notifyItemChanged(focusedItem);
            focusedItem = tryFocusItem;
            notifyItemChanged(focusedItem);
            lm.scrollToPosition(focusedItem);
            return true;
        }

        return false;
    }

    // 새로운 뷰 홀더 생성
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view,parent,false);
        return new ItemViewHolder(view);
    }


    // View 의 내용을 해당 포지션의 데이터로 바꿉니다.
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        String userData=mItems.get(position).getName();
        StringTokenizer stok = new StringTokenizer(userData, "_", false);
        String userId=  stok.nextToken();
        String userName = stok.nextToken();
        String device_id = "없음";
        String bracelet_id = "없음";
        String webcam_id = "없음";

        Log.d("유저데이터 길이", ""+userData.length());

        if(userData.length()>20)
        {  device_id= stok.nextToken();
            if(device_id.equals("없음"))
            {holder.mDeviceIDview.setImageResource(R.drawable.lampoff);}
            else{holder.mDeviceIDview.setImageResource(R.drawable.lampon);}
            if(userData.length()>50) {
                bracelet_id = stok.nextToken();
                webcam_id = stok.nextToken();
                if (bracelet_id.equals("없음")) {
                    holder.mBraceIDview.setImageResource(R.drawable.lampoff);
                } else {
                    holder.mBraceIDview.setImageResource(R.drawable.lampon);
                }
                if (webcam_id.equals("없음")) {
                    holder.mWebcamIDview.setImageResource(R.drawable.lampoff);
                } else {
                    holder.mWebcamIDview.setImageResource(R.drawable.lampon);
                }

            }
            Log.d("유저데이터", device_id+","+bracelet_id+","+webcam_id);

        }




            holder.mIDTv.setText(userId);
        holder.mNameTv.setText(userName);
        holder.mDeviceIDTv.setText(device_id);
        holder.mDeviceIDTv.setTextSize(12);
        holder.mBraceIDTv.setText(bracelet_id);
        holder.mBraceIDTv.setTextSize(12);
        holder.itemView.setSelected(focusedItem == position);
    }

    // 데이터 셋의 크기를 리턴해줍니다.
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    // 커스텀 뷰홀더
    // item layout 에 존재하는 위젯들을 바인딩합니다.
    class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView mNameTv;
        private TextView mIDTv;
        private TextView mDeviceIDTv;
        private ImageView mDeviceIDview;
        private TextView mBraceIDTv;
        private ImageView mBraceIDview;
        private ImageView mWebcamIDview;

        private CheckBox CheckBox;


        public ItemViewHolder(View itemView) {
            super(itemView);
            mNameTv = (TextView) itemView.findViewById(R.id.userName);
            mIDTv = (TextView) itemView.findViewById(R.id.userId);
            mDeviceIDTv = (TextView) itemView.findViewById(R.id.deviceid);
            mDeviceIDview = (ImageView) itemView.findViewById(R.id.deviceid_state);
            mBraceIDTv = (TextView) itemView.findViewById(R.id.braceid);
            mBraceIDview = (ImageView) itemView.findViewById(R.id.braceid_state);
            mWebcamIDview = (ImageView) itemView.findViewById(R.id.webcam_state);

        }
    }

}
