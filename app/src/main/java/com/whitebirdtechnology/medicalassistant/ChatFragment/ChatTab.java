package com.whitebirdtechnology.medicalassistant.ChatFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.whitebirdtechnology.medicalassistant.R;
import com.whitebirdtechnology.medicalassistant.Sharepreference.ClsSharePreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dell on 22/3/17.
 */

public class ChatTab extends Fragment {
    ListView listViewChat;
    ArrayList<FeedItemChatHistory> arrayListFireBase;
    ClsSharePreference clsSharePreference;
    ListAdapterChatHistory listAdapterChatHistory;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_tab,container,false);
        listViewChat = (ListView)view.findViewById(R.id.listViewChatHistory);
        clsSharePreference = new ClsSharePreference(getActivity());
        arrayListFireBase = new ArrayList<>();
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
                Query query = firebaseDatabase.child("ChatMsg");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        if (isAdded()) {
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                String stringID = singleSnapshot.getKey();
                                int mid = stringID.length() / 2;
                                String stringID1 = stringID.substring(0, mid);
                                String stringID2 = stringID.substring(mid);
                                final String stringUserId = clsSharePreference.GetSharPrf(getString(R.string.SharPrfMobileNo));
                                final String stringAnotherId;
                                if (stringID1.equals(stringUserId)) {
                                    stringAnotherId = stringID2;
                                } else
                                    stringAnotherId = stringID1;
                                if (isAdded() && stringID1.equals(stringUserId) || stringID2.equals(stringUserId)) {
                                    Query query1 = singleSnapshot.getRef().orderByKey().limitToLast(1);
                                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(final DataSnapshot dataSnapshot1) {
                                            final FeedItemChatHistory feedItemChatHistory = new FeedItemChatHistory();
                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                            DatabaseReference reference = databaseReference.child("UserInfo");
                                            DatabaseReference ref = reference.child(clsSharePreference.GetSharPrf(getString(R.string.SharPrfMobileNo)));
                                            DatabaseReference reference1 = ref.child(stringAnotherId);
                                            reference1.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot2) {
                                                    feedItemChatHistory.setStringSenderName(dataSnapshot2.child("Name").getValue().toString());
                                                    feedItemChatHistory.setStringSenderOccu(dataSnapshot2.child("Occupation").getValue().toString());
                                                    String isFav =dataSnapshot2.child("IsFavourite").getValue().toString();
                                                    if(isFav.equals("true")){
                                                        feedItemChatHistory.setaBooleanIsFav(true);
                                                    }else
                                                        feedItemChatHistory.setaBooleanIsFav(false);

                                                    feedItemChatHistory.setStringSenderId(dataSnapshot2.child("UserId").getValue().toString());
                                                    feedItemChatHistory.setStringSenderImgPath(dataSnapshot2.child("ProfilePath").getValue().toString());
                                                    feedItemChatHistory.setStringMobNo(dataSnapshot2.getKey());



                                                    for (DataSnapshot snapshot : dataSnapshot1.getChildren()) {
                                                        String stringLastKey = snapshot.getKey();
                                                        feedItemChatHistory.setStringLastKey(stringLastKey);
                                                        if (stringUserId.equals(String.valueOf(snapshot.child("senderMobNo").getValue()))) {
                                                            feedItemChatHistory.setStringFlag("1");
                                                        } else {
                                                            feedItemChatHistory.setStringFlag("2");
                                                        }
                                                        feedItemChatHistory.setStringLastMsg(String.valueOf(snapshot.child("message").getValue()));
                                                        feedItemChatHistory.setStringTime(String.valueOf(snapshot.child("time").getValue()));
                                                        feedItemChatHistory.setStringType(String.valueOf(snapshot.child("type").getValue()));
                                                        feedItemChatHistory.setStringAnotherUser(stringAnotherId);
                                                        Boolean booleanCheck = true;
                                                        for (int i = 0; i < arrayListFireBase.size(); i++) {
                                                            FeedItemChatHistory feedItemChatHistory1 = arrayListFireBase.get(i);
                                                            if (feedItemChatHistory1.getStringAnotherUser().equals(stringAnotherId)) {
                                                                booleanCheck = false;
                                                                if (!feedItemChatHistory1.getStringLastKey().equals(stringLastKey)) {
                                                                    arrayListFireBase.set(i, feedItemChatHistory);
                                                                    listAdapterChatHistory = new ListAdapterChatHistory(getActivity(), arrayListFireBase);
                                                                    listViewChat.setAdapter(listAdapterChatHistory);
                                                                    listAdapterChatHistory.notifyDataSetChanged();
                                                                }
                                                            }
                                                        }
                                                        if (booleanCheck) {
                                                            arrayListFireBase.add(feedItemChatHistory);
                                                            listAdapterChatHistory = new ListAdapterChatHistory(getActivity(), arrayListFireBase);
                                                            listViewChat.setAdapter(listAdapterChatHistory);
                                                            listAdapterChatHistory.notifyDataSetChanged();
                                                        }

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("errorMsgHistory", "onCancelled", databaseError.toException());
                    }
                });

            }
        },0,1000);
        return view;
    }
}
