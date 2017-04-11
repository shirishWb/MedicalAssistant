package com.whitebirdtechnology.medicalassistant;

import android.content.Context;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.whitebirdtechnology.medicalassistant.ChatScreen.FeedItemChat;
import com.whitebirdtechnology.medicalassistant.Sharepreference.ClsSharePreference;
import com.whitebirdtechnology.medicalassistant.SqlDatabase.SqlDatabaseChat;

import java.util.ArrayList;

/**
 * Created by dell on 10/4/17.
 */

public class ChatMsgFirebaseSync {
    Context context;
    ClsSharePreference clsSharePreference;
    DatabaseReference databaseReference;
    SqlDatabaseChat sqlDatabaseChat;
    String uniqueNo;
    public ChatMsgFirebaseSync(Context context){
        this.context = context;
        clsSharePreference = new ClsSharePreference(context);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        sqlDatabaseChat = new SqlDatabaseChat(context);
    }
    public void AddDataToSqlFrmFirebase(){
        DatabaseReference referenceUserInfo = databaseReference.child("UserInfo");
        final String stringThisMobNo = clsSharePreference.GetSharPrf(context.getString(R.string.SharPrfMobileNo));
        DatabaseReference referenceSelUser = referenceUserInfo.child(stringThisMobNo);
        referenceSelUser.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final FeedItemUserInfo feedItemUserInfo = new FeedItemUserInfo();
                feedItemUserInfo.setStringMobileNo(dataSnapshot.getKey());
                feedItemUserInfo.setStringId(String.valueOf(dataSnapshot.child("UserId").getValue()));
                feedItemUserInfo.setStringImgPath(String.valueOf(dataSnapshot.child("ProfilePath").getValue()));
                feedItemUserInfo.setStringIsFav(String.valueOf(dataSnapshot.child("IsFavourite").getValue()));
                feedItemUserInfo.setStringName(String.valueOf(dataSnapshot.child("Name").getValue()));
                feedItemUserInfo.setStringOccu(String.valueOf(dataSnapshot.child("Occupation").getValue()));
                sqlDatabaseChat.AddUserInfo(feedItemUserInfo);
                final ArrayList<FeedItemUserInfo> arrayList = sqlDatabaseChat.GetUserInfo();
                for(int i =0;i<arrayList.size();i++){
                    String stringAnotherMobNo = arrayList.get(i).getStringMobileNo();
                    long expertMobNo = Long.parseLong(stringAnotherMobNo);
                     long userMobNo = Long.parseLong(stringThisMobNo);

                    if(expertMobNo > userMobNo){
                        uniqueNo = String.valueOf(userMobNo)+String.valueOf(expertMobNo);
                    }else {
                        uniqueNo = String.valueOf(expertMobNo)+String.valueOf(userMobNo);
                    }
                    DatabaseReference referenceMsg = databaseReference.child("ChatMsg");
                    DatabaseReference referenceGetMsg = referenceMsg.child(uniqueNo);
                   String ss=  referenceGetMsg.getKey();
                    String bb =ss;
                    final int finalI = i;
                    referenceGetMsg.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            FeedItemChat feedItemChat = new FeedItemChat();
                            if (stringThisMobNo.equals(String.valueOf(dataSnapshot.child("senderMobNo").getValue()))) {
                                feedItemChat.setStringFlag("1");
                                feedItemChat.setStringImg(clsSharePreference.GetSharPrf(context.getString(R.string.SharPrfProImg)));
                            } else {
                                feedItemChat.setStringFlag("2");
                                feedItemChat.setStringImg(arrayList.get(finalI).getStringImgPath());
                            }
                            feedItemChat.setStringMsg(String.valueOf(dataSnapshot.child("message").getValue()));
                            feedItemChat.setStringTime(String.valueOf(dataSnapshot.child("time").getValue()));
                            feedItemChat.setStringType(String.valueOf(dataSnapshot.child("type").getValue()));
                            feedItemChat.setStringMobileNo(String.valueOf(dataSnapshot.child("senderMobNo").getValue()));
                            feedItemChat.setStringReadValue(String.valueOf(dataSnapshot.child("readValue").getValue()));
                            feedItemChat.setStringTime(String.valueOf(dataSnapshot.child("time").getValue()));
                            feedItemChat.setStringKeyValue(String.valueOf(dataSnapshot.child("key").getValue()));
                            feedItemChat.setStringType(String.valueOf(dataSnapshot.child("type").getValue()));
                            sqlDatabaseChat.CreateTable("TABLE"+String.valueOf(uniqueNo),feedItemChat);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
