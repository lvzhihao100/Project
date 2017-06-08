package com.eqdd.library.utils;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import com.eqdd.common.utils.ToastUtil;
import com.eqdd.library.bean.Person;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;

/**
 * Created by lvzhihao on 17-3-31.
 */

public class ContactsUtil {
    private static final String[] CONTACTOR_ION = new String[]{
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER};

    public static ArrayList<Person> getAllContacts(Context context) {
        ArrayList<Person> contacts = new ArrayList<Person>();

        RxPermissions.getInstance(context).request(Manifest.permission.READ_CONTACTS)
                .subscribe(granted -> {
                    if (granted) {

                        Cursor cursor = context.getContentResolver().query(
                                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                        while (cursor.moveToNext()) {

                            //新建一个联系人实例
                            String contactId = cursor.getString(cursor
                                    .getColumnIndex(ContactsContract.Contacts._ID));
                            String name = cursor.getString(cursor
                                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            Person temp = new Person(name);


                            //获取联系人所有电话号
                            Cursor phones = context.getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                                            + contactId, null, null);
                            while (phones.moveToNext()) {

                                String phoneNumber = phones
                                        .getString(phones
                                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                phoneNumber = phoneNumber.replace("-", "");
                                phoneNumber = phoneNumber.replace(" ", "");
                                temp.setPhone(phoneNumber);
                                break;
                            }
                            phones.close();
                            contacts.add(temp);
                        }
                    } else {
                        ToastUtil.showLong("请在设置中开启读取联系人权限");
                    }

                });
        return contacts;
    }

    public static ArrayList<Person> getQuickAllContacts(Context context) {
        ArrayList<Person> contacts = new ArrayList<Person>();

        RxPermissions.getInstance(context).request(Manifest.permission.READ_CONTACTS)
                .subscribe(granted -> {
                    if (granted) {
                        System.out.println("读取联系人授权成功");
                        Cursor phones = null;
                        ContentResolver cr = context.getContentResolver();
                        try {
                            phones = cr
                                    .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                                            , CONTACTOR_ION, null, null, "sort_key");

                            if (phones != null) {
                                final int contactIdIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
                                final int displayNameIndex = phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                                final int phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                                String phoneString, displayNameString, contactIdString;
                                while (phones.moveToNext()) {
                                    phoneString = phones.getString(phoneIndex);
                                    displayNameString = phones.getString(displayNameIndex);
//                                    contactIdString = phones.getString(contactIdIndex);
                                    Person temp = new Person(displayNameString);
                                    phoneString=  phoneString.replace(" ","");
                                    phoneString=  phoneString.replace("-","");
                                    temp.setPhone(phoneString);
                                    contacts.add(temp);
                                }
                            }
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        } finally {
                            if (phones != null)
                                phones.close();
                        }
                    } else {
                        System.out.println("读取联系人授权失败");

                    }

                });
        return contacts;
    }

    /**
     * 获取手机联系人头像
     *
     * @param c
     * @param personId
     * @param defaultIco
     * @return
     */
    private static Bitmap getContactPhoto(Context c, String personId,
                                          int defaultIco) {
        byte[] data = new byte[0];
        Uri u = Uri.parse("content://com.android.contacts/data");
        String where = "raw_contact_id = " + personId
                + " AND mimetype ='vnd.android.cursor.item/photo'";
        Cursor cursor = c.getContentResolver()
                .query(u, null, where, null, null);
        if (cursor.moveToFirst()) {
            data = cursor.getBlob(cursor.getColumnIndex("data15"));
        }
        cursor.close();
        if (data == null || data.length == 0) {
            return BitmapFactory.decodeResource(c.getResources(), defaultIco);
        } else
            return BitmapFactory.decodeByteArray(data, 0, data.length);
    }
}
