package com.example.chickencafe;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {
    private ArrayList<Message> mMessageList = new ArrayList<>();

    @Override
    public int getCount() {
        return mMessageList.size();
    }

    @Override
    public Object getItem(int i) {
        return mMessageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context mContext = viewGroup.getContext();
        boolean my_message = true;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.chatting_message, viewGroup, false);
        }

        TextView chatmessageTextView = (TextView)view.findViewById(R.id.chatmessage);
        Message msg = mMessageList.get(i);

        if(msg.getType().equals("0")){
            my_message = true;
        }else{
            my_message = false;
        }

        /** nine path image setting
         *
         */
        chatmessageTextView.setBackground(mContext.getResources().getDrawable((my_message ? R.drawable.speech_bubble_l:R.drawable.speech_bubble_r)));

        LinearLayout chatMessageContainer = (LinearLayout)view.findViewById(R.id.chatmessage_container);
        View viewRight = (View) view.findViewById(R.id.imageViewright);
        View viewLeft = (View) view.findViewById(R.id.imageViewleft);
        if(!my_message) {
            chatMessageContainer.setGravity(Gravity.LEFT);
            viewLeft.setVisibility(View.GONE);
            viewRight.setVisibility(View.VISIBLE);
        }else{
            chatMessageContainer.setGravity(Gravity.RIGHT);
            viewRight.setVisibility(View.GONE);
            viewLeft.setVisibility(View.VISIBLE);
        }


        chatmessageTextView.setText(msg.getMsg());
        chatmessageTextView.setMaxWidth(SelectUserActivity.width/5*4);

        return view;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Message msg) {
        mMessageList.add(msg);
    }
}