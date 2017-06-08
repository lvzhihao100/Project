package com.eqdd.library.bean;

import java.util.List;

/**
 * Created by lvzhihao on 17-3-29.
 */

public class IDCardBean {

    /**
     * image_id : WmU5N+rIgQi7V8sFNXxPNA==
     * request_id : 1490754247,fc2ae6f5-c2ce-4e91-8b6f-5637ac1fa854
     * cards : [{"name":"奥巴马","gender":"男","id_card_number":"123456196108047890","birthday":"1961-08-04","race":"哈萨克","address":"南昌市特区宜宾法尼亚大道1600号白宫","type":1,"side":"front"}]
     * time_used : 1455
     */

    private String image_id;
    private String request_id;
    private int time_used;
    private List<CardsBean> cards;

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public int getTime_used() {
        return time_used;
    }

    public void setTime_used(int time_used) {
        this.time_used = time_used;
    }

    public List<CardsBean> getCards() {
        return cards;
    }

    public void setCards(List<CardsBean> cards) {
        this.cards = cards;
    }

    public static class CardsBean {
        /**
         * name : 奥巴马
         * gender : 男
         * id_card_number : 123456196108047890
         * birthday : 1961-08-04
         * race : 哈萨克
         * address : 南昌市特区宜宾法尼亚大道1600号白宫
         * type : 1
         * side : front
         */

        private String name;
        private String gender;
        private String id_card_number;
        private String birthday;
        private String race;
        private String address;
        private int type;
        private String side;

        @Override
        public String toString() {
            return "CardsBean{" +
                    "name='" + name + '\'' +
                    ", gender='" + gender + '\'' +
                    ", id_card_number='" + id_card_number + '\'' +
                    ", birthday='" + birthday + '\'' +
                    ", race='" + race + '\'' +
                    ", address='" + address + '\'' +
                    ", type=" + type +
                    ", side='" + side + '\'' +
                    '}';
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getId_card_number() {
            return id_card_number;
        }

        public void setId_card_number(String id_card_number) {
            this.id_card_number = id_card_number;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getRace() {
            return race;
        }

        public void setRace(String race) {
            this.race = race;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getSide() {
            return side;
        }

        public void setSide(String side) {
            this.side = side;
        }
    }
}
