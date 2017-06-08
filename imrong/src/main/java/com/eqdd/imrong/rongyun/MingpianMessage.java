package com.eqdd.imrong.rongyun;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.rong.common.ParcelUtils;
import io.rong.common.RLog;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

/**
 * Created by lvzhihao on 17-4-30.
 */
@MessageTag(value = "EQD:mp", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class MingpianMessage extends MessageContent implements Parcelable {

    Content content;


    public MingpianMessage(Content content) {
        this.content = content;
    }

    public static final Creator<MingpianMessage> CREATOR = new Creator() {
        public MingpianMessage createFromParcel(Parcel source) {
            return new MingpianMessage(source);
        }

        public MingpianMessage[] newArray(int size) {
            return new MingpianMessage[size];
        }
    };


    public static MingpianMessage obtain(Content content) {
        return new MingpianMessage(content);
    }

    protected MingpianMessage(Parcel in) {
        this.content = (Content) ParcelUtils.readFromParcel(in, Content.class);
        this.setUserInfo((UserInfo) ParcelUtils.readFromParcel(in, UserInfo.class));
    }

    public MingpianMessage() {
    }

    public MingpianMessage(byte[] data) {
        String jsonStr = new String(data);

        try {
            JSONObject e = new JSONObject(jsonStr);
            if (e.has("user")) {
                this.setUserInfo(this.parseJsonToUserInfo(e.getJSONObject("user")));
            }
            if (e.has("content")) {
                this.content = parseJsonContent(e.getJSONObject("content"));

            }
        } catch (JSONException var4) {
            Log.e("JSONException", var4.getMessage());
        }

    }

    public Content parseJsonContent(JSONObject jsonObj) {
        Content info = null;
        String name = jsonObj.optString("name");
        String imgurl = jsonObj.optString("imgurl");
        String bumen = jsonObj.optString("bumen");
        String gangwei = jsonObj.optString("gangwei");
        String company = jsonObj.optString("company");
        String uid = jsonObj.optString("uid");
        String comid = jsonObj.optString("comid");
        info = new Content(name, imgurl, bumen, gangwei, company, uid, comid);
        return info;
    }

    public JSONObject getJSONContent() {
        if (this.getContent() != null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", this.getContent().getName());
                jsonObject.put("imgurl", this.getContent().getImgurl());
                jsonObject.put("bumen", this.getContent().getBumen());
                jsonObject.put("gangwei", this.getContent().getGangwei());
                jsonObject.put("company", this.getContent().getCompany());
                jsonObject.put("uid", this.getContent().getUid());
                jsonObject.put("comid", this.getContent().getComid());
            } catch (JSONException var3) {
                RLog.e("MessageContent", "JSONException " + var3.getMessage());
            }

            return jsonObject;
        } else {
            return null;
        }
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            if (this.getJSONUserInfo() != null) {
                jsonObj.putOpt("user", this.getJSONUserInfo());
            }
            if (this.getContent()!=null){
                jsonObj.putOpt("content", this.getJSONContent());
            }
        } catch (JSONException var4) {
            Log.e("JSONException", var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
            return new byte[0];
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.content);
        ParcelUtils.writeToParcel(dest, this.getUserInfo());
    }

    private String getExpression(String content) {
        Pattern pattern = Pattern.compile("\\[/u([0-9A-Fa-f]+)\\]");
        Matcher matcher = pattern.matcher(content);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(sb, this.toExpressionChar(matcher.group(1)));
        }

        matcher.appendTail(sb);
        Log.d("getExpression--", sb.toString());
        return sb.toString();
    }

    private String toExpressionChar(String expChar) {
        int inthex = Integer.parseInt(expChar, 16);
        return String.valueOf(Character.toChars(inthex));
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}
