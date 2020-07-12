package com.example.dream_team.common_activities;

import android.util.Log;

import com.example.dream_team.interfaces.CALLBACK;
import com.example.dream_team.modal_class.CONSTANTS;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class DATABASE {
    private String TAG = "Dream_Team | DATABASE ";
    private FirebaseFirestore db;
    private CONSTANTS constants;

    DATABASE() {
        db = FirebaseFirestore.getInstance();
        constants = new CONSTANTS();
    }

    public void checkUserExist(String id, String password, final CALLBACK callback) {
        try {
            Log.d(TAG, "checkUserExist | Checking user "+id+" "+password);
            db.collection(constants.HOTEL_CRED())
                    .whereEqualTo(constants.MOBILE(), id)
                    .whereEqualTo(constants.PASSWORD(), password)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "checkUserExist | onComplete | Task => "+task.getResult());
                                Log.d(TAG, "checkUserExist | onComplete | Task => "+task.getResult().getDocuments());
                                Log.d(TAG, "checkUserExist | onComplete | EXCEPTION => "+task.getException());
                                if (task.getResult().isEmpty()) {
                                    Log.e(TAG, "onComplete | No user found with this credentials ");
                                    callback.callBackMethod(1);
                                } else {
                                    Log.d(TAG, "checkUserExist | onComplete | Valid User ");
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        final String docName = document.getString("DOCUMENT_NAME");
                                        final String token = document.getString(constants.TOKEN());
                                        Log.d(TAG, "checkUserExist | onComplete | Data =>  " + docName + "_" + token);
                                        if (token.length() == 0) {
                                            final String myToken = LoginScreen.generateToken();
                                            insertToken(document.getId(), myToken, new CALLBACK() {
                                                @Override
                                                public void callBackMethod(int result) {
                                                    if (result == 0) {
                                                        LoginScreen.setSharedData("DOC_NAME", docName);
                                                        if (LoginScreen.rememberMeCheckedOrNot) {
                                                            LoginScreen.setSharedData(constants.TOKEN(), myToken);
                                                            Log.d(TAG, "checkUserExist | callBackMethod | Token inserting in shared preference ");
                                                        }
                                                        callback.callBackMethod(0);
                                                    } else if (result == 1) {
                                                        callback.callBackMethod(1);
                                                    } else if (result == 2) {
                                                        callback.callBackMethod(2);
                                                    }
                                                }
                                            });
                                        } else {
                                            LoginScreen.setSharedData("DOC_NAME", docName);
                                            if (LoginScreen.rememberMeCheckedOrNot) {
                                                LoginScreen.setSharedData(constants.TOKEN(), token);
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

            db.collection(constants.HOTEL_CRED()).document(credDocName)
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

    public void checkNumberExist(String mobile, String type, final CALLBACK callback) {
        try {
            Log.d(TAG, "checkNumberExist | In function checkNumberExist ");
            db.collection(constants.HOTEL_CRED())
                    .whereEqualTo(constants.MOBILE(), mobile)
                    .whereEqualTo(constants.TYPE(), type.toUpperCase())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().isEmpty()) {
                                    Log.e(TAG, "checkNumberExist | onComplete | Empty Task ");
                                    callback.callBackMethod(1);
                                } else {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, "checkNumberExist | onComplete | Found match ");
                                        Log.d(TAG, "checkNumberExist | onComplete | Data =>  " + document.getData());
                                        callback.callBackMethod(0);
                                    }
                                }
                            } else {
                                Log.e(TAG, "checkNumberExist | onComplete | Task failed");
                                callback.callBackMethod(1);
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "checkNumberExist | Exception occurred: " + e.getMessage());
            callback.callBackMethod(2);
        }
    }

    public void insertOwnerInfo(Map<String, Object> data, final CALLBACK callback) {
        try {
            Log.d(TAG, "insertOwnerInfo | In function. ");
            Log.d(TAG, "insertOwnerInfo | DATA " + data);

            db.collection(constants.WHOLE_DB()).document(data.get(constants.HOTEL_NAME()).toString()).collection(constants.EMP_CRED())
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "insertOwnerInfo | onSuccess | Data inserted. ");
                            callback.callBackMethod(0);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "insertOwnerInfo | onFailure | Failed to insert data " + e.getMessage());
                    callback.callBackMethod(1);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "insertOwnerInfo | Exception in insertOwnerInfo " + e.getMessage());
            callback.callBackMethod(2);
        }
    }
}
