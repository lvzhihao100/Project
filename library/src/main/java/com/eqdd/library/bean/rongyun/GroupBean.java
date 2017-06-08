package com.eqdd.library.bean.rongyun;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by lvzhihao on 17-4-10.
 */
@Entity
public class GroupBean implements Parcelable {

    /**
     * groupid : 1234
     * groupname : 测试
     */
    @Id(autoincrement = true)
    Long id;
    private String groupid;
    private String groupname;
    private String groupphoto;
    @Generated(hash = 965916890)
    public GroupBean(Long id, String groupid, String groupname, String groupphoto) {
        this.id = id;
        this.groupid = groupid;
        this.groupname = groupname;
        this.groupphoto = groupphoto;
    }
    @Generated(hash = 405578774)
    public GroupBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getGroupid() {
        return this.groupid;
    }
    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }
    public String getGroupname() {
        return this.groupname;
    }
    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }
    public String getGroupphoto() {
        return this.groupphoto;
    }
    public void setGroupphoto(String groupphoto) {
        this.groupphoto = groupphoto;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.groupid);
        dest.writeString(this.groupname);
        dest.writeString(this.groupphoto);
    }

    protected GroupBean(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.groupid = in.readString();
        this.groupname = in.readString();
        this.groupphoto = in.readString();
    }

    public static final Parcelable.Creator<GroupBean> CREATOR = new Parcelable.Creator<GroupBean>() {
        @Override
        public GroupBean createFromParcel(Parcel source) {
            return new GroupBean(source);
        }

        @Override
        public GroupBean[] newArray(int size) {
            return new GroupBean[size];
        }
    };
}
