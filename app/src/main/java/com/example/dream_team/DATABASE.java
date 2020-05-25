package com.example.dream_team;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class DATABASE {
    private String TAG = "Dream_Team | DATABASE ";
    private FirebaseFirestore db;

    DATABASE() {
        db = FirebaseFirestore.getInstance();
    }

    public void checkUserExist(String id, String password, final CALLBACK callback) {
        try {
            Log.d(TAG, "checkUserExist | Checking user ");
            db.collection("Hotel_App_Credentials")
                    .whereEqualTo("MOBILE", id)
                    .whereEqualTo("PASSWORD", password)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().isEmpty()) {
                                    Log.e(TAG, "onComplete | No user found with this credentials ");
                                    callback.callBackMethod(1);
                                } else {
                                    Log.d(TAG, "checkUserExist | onComplete | Valid User ");
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        final String docName = document.getString("DOCUMENT_NAME");
                                        final String token = document.getString("TOKEN");
                                        Log.d(TAG, "checkUserExist | onComplete | Data =>  " + docName + "_" + token);
                                        if (token.length() == 0) {
                                            final String myToken = LoginScreen.generateToken();
                                            insertToken(document.getId(), myToken, new CALLBACK() {
                                                @Override
                                                public void callBackMethod(int result) {
                                                    if (result == 0) {
                                                        LoginScreen.setSharedData("DOC_NAME", docName);
                                                        if (LoginScreen.rememberMeCheckedOrNot) {
                                                            LoginScreen.setSharedData("TOKEN", myToken);
                                                            Log.d(TAG, "checkUserExist | callBackMethod | Token inserting in shared preference ");
                                                        }
                                                        callback.callBackMethod(0);
                                                    } else if (result == 1) {
                                                        callback.callBackMethod(1);
                                                    } else if (result == 2) {
                                                        callback.callBackMethod(2);
                                                    }
                                                }

                                                @Override
                                                public void getData(String docName, String token) {
                                                }
                                            });
                                        } else {
                                            LoginScreen.setSharedData("DOC_NAME", docName);
                                            if (LoginScreen.rememberMeCheckedOrNot) {
                                                LoginScreen.setSharedData("TOKEN", token);
                                                Log.d(TAG, "checkUserExist | Token inserting in shared preference ");
                                            }
                                            Log.d(TAG, "checkUserExist | onComplete | User token already exist ");
                                            callback.callBackMethod(0);
                                        }
                                    }
                                }
                            } else {
                                Log.e(TAG, "onComplete | No user found with this credentials ");
                                callback.callBackMethod(1);
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "checkUserExist: " + e.getMessage());
            callback.callBackMethod(2);
        }
    }

    private void insertToken(String credDocName, String token, final CALLBACK callback) {
        try {
            Log.d(TAG, "insertToken | Inserting token ");
            /*String token = LoginScreen.getSharedData("TOKEN");
            String docName = LoginScreen.getSharedData("DOC_NAME");*/

            Log.d(TAG, "insertToken | DocName: " + credDocName + " Token: " + token);
            Map<String, Object> map = new HashMap<>();
            map.put("TOKEN", token);

            db.collection("Hotel_App_Credentials").document(credDocName)
                    .update(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "insertToken | onComplete | Token inserted successfully ");
                                callback.callBackMethod(0);
                            } else {
                                Log.d(TAG, "insertToken | onComplete | Token insertion failed ");
                                callback.callBackMethod(1);
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "insertToken | Exception while inserting token " + e.getMessage());
            callback.callBackMethod(2);
        }
    }
}
