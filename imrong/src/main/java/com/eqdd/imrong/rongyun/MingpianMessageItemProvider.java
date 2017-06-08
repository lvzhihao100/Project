package com.eqdd.imrong.rongyun;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.alibaba.android.arouter.launcher.ARouter;
import com.eqdd.common.utils.ImageUtil;
import com.eqdd.imrong.R;
import com.eqdd.library.base.Config;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * Created by lvzhihao on 17-4-30.
 */
@ProviderTag(
        messageContent = MingpianMessage.class,
        showReadState = true
)
public class MingpianMessageItemProvider extends IContainerItemProvider.MessageProvider<MingpianMessage> {

    class ViewHolder {
        LinearLayout linearLayout;
        TextView textViewName;
        TextView textViewCom;
        ImageView imageViewHead;
    }

    private Context context;

    @Override
    public View newView(Context context, ViewGroup group) {
        this.context = context;
        ViewDataBinding inflate = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.imrong_item_mingpian_message, null, false);
        View view = inflate.getRoot();
        ViewHolder holder = new ViewHolder();
        holder.textViewName = (TextView) view.findViewById(R.id.tv_name);
        holder.textViewCom = (TextView) view.findViewById(R.id.tv_com);
        holder.imageViewHead = (ImageView) view.findViewById(R.id.iv_head);
        holder.linearLayout = (LinearLayout) view.findViewById(R.id.ll_root);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View v, int position, MingpianMessage content, UIMessage message) {
        ViewHolder holder = (ViewHolder) v.getTag();

        if (message.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            holder.linearLayout.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
        } else {
            holder.linearLayout.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
        }
        holder.textViewName.setText(content.getContent().getName() + "  " + content.getContent().getBumen() + " " + content.getContent().getGangwei());
        holder.textViewCom.setText(content.getContent().getCompany());
        ImageUtil.setCircleImage(content.getContent().getImgurl(), holder.imageViewHead);
    }


    @Override
    public Spannable getContentSummary(MingpianMessage data) {
        return new SpannableString("名片");
    }


    @Override
    public void onItemClick(View view, int position, MingpianMessage content, UIMessage message) {
        UserInfo userInfo = new UserInfo(content.getContent().getUid(),
                content.getContent().getName(), Uri.parse(content.getContent().getImgurl()));
        System.out.println(userInfo);
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", userInfo);
        ARouter.getInstance().build(Config.TONGXUNLU_FRIEND_DETAIL_INFO)
                .with(bundle)
                .navigation();

    }

    @Override
    public void onItemLongClick(View view, int position, MingpianMessage content, UIMessage message) {

    }

}