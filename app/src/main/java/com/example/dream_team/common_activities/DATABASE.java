package com.example.dream_team.common_activities;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.dream_team.interfaces.CALLBACK;
import com.example.dream_team.interfaces.CheckingNewInterface;
import com.example.dream_team.modal_class.CONSTANTS;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DATABASE {
    private String TAG = "Dream_Team | DATABASE ";
    private FirebaseFirestore db;
    private CONSTANTS constants;
    private DocumentReference menuReference;
    private String userDocument = null;
    private Object arr[];
    private List<String> arr2 = new ArrayList<String>(); // to cast an array into List

    public DATABASE() {
        constants = new CONSTANTS();
        db = FirebaseFirestore.getInstance();
    }

    public void checkMobilePassword(String id, String password, final CheckingNewInterface callback) {
        try {
            Log.d(TAG, "checkUserExist | Checking user ");
            final Map<String, Object> responseObject = new HashMap<>();

            db.collection(constants.HOTEL_CRED())
                    .whereEqualTo(constants.MOBILE(), id)
                    .whereEqualTo(constants.PASSWORD(), password)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "checkUserExist | onComplete: " + task.getResult());
                                if (task.getResult().isEmpty()) {
                                    Log.e(TAG, "checkUserExist | onComplete | No user found with this credentials ");
                                    responseObject.put("MESSAGE", "User not registered ");
                                    callback.callbackWithData(1, responseObject);
                                } else {
                                    Log.d(TAG, "checkUserExist | onComplete | Valid User ");
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        final String docName = document.getString(constants.DOCUMENT_NAME());
                                        final String token = document.getString(constants.TOKEN());
                                        final String type = document.getString(constants.TYPE());

                                        Log.d(TAG, "checkUserExist | onComplete | Data =>  " + docName + "\n Token => " + token + "\nType => " + type);

                                        if (token.length() == 0) {
                                            Log.e(TAG, "checkUserExist | onComplete | Token not present. First sign up please. ");
                                            responseObject.put("MESSAGE", "User not signed up");
                                            callback.callbackWithData(1, responseObject);
                                        } else {
                                            String dbToken = LoginScreen.getSharedData(constants.TOKEN()).trim();
                                            Log.d(TAG, "checkUserExist | onComplete | dbToken => " + dbToken);
                                            if (dbToken.length() > 0) {
                                                //if (!dbToken.equals(token)) {
                                                Log.d(TAG, "checkUserExist | onComplete | dbToken and firebase token are different so updating db token.");
                                                LoginScreen.setSharedData(constants.TOKEN(), token);
                                                LoginScreen.setSharedData(constants.DOCUMENT_NAME(), docName);
                                                LoginScreen.setSharedData(constants.TYPE(), type);
                                                //} else {
                                                //    Log.d(TAG, "checkUserExist | onComplete | dbToken and firebase token are SAME ");
                                                //}
                                            } else {
                                                Log.d(TAG, "checkUserExist | onComplete | dbToken not present so inserting ");
                                                LoginScreen.setSharedData(constants.TOKEN(), token);
                                                LoginScreen.setSharedData(constants.DOCUMENT_NAME(), docName);
                                                LoginScreen.setSharedData(constants.TYPE(), type);
                                            }

                                            if (LoginScreen.getSharedData("REMEMBER").equals("false") || LoginScreen.getSharedData("REMEMBER").equals("")) {
                                                if (LoginScreen.rememberMeCheckedOrNot) {
                                                    LoginScreen.setSharedData("REMEMBER", "true");
                                                } else {
                                                    LoginScreen.setSharedData("REMEMBER", "false");
                                                }
                                            }

                                            responseObject.put(constants.TYPE(), type);
                                            callback.callbackWithData(0, responseObject);
                                        }
                                    }
                                }
                            } else {
                                Log.e(TAG, "onComplete | No user found with this credentials ");
                                callback.callbackWithData(1, null);
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "checkUserExist: " + e.getMessage());
            callback.callbackWithData(2, null);
        }
    }

    public void checkNumberWithType(String mobile, final String type, final Map<String, Object> data, final CALLBACK callback) {
        try {
            Log.d(TAG, "checkNumberWithType | In function checkNumberWithType Mobile => " + mobile + " Type => " + type);
            db.collection(constants.HOTEL_CRED())
                    .whereEqualTo(constants.MOBILE(), mobile.trim())
                    .whereEqualTo(constants.TYPE(), type.toUpperCase())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "checkNumberWithType | onComplete | task.getResult().isEmpty() " + task.getResult().isEmpty());
                                if (task.getResult().isEmpty()) {
                                    Log.e(TAG, "checkNumberWithType | onComplete | Empty Task ");
                                    callback.callBackMethod(1);
                                } else {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, "checkNumberWithType | onComplete | Found match ");
                                        //Log.d(TAG, "checkNumberWithType | onComplete | Data =>  " + document.getData());

                                        if (document.contains(constants.TOKEN())) {
                                            if (document.getString(constants.TOKEN()).trim().length() == 0) {
                                                if (type == "Owner") {
                                                    insertOwnerInfo(data, document.getId(), callback);
                                                } else {
                                                    insertEmployeeInfo(data, type, document.getId(), document.get(constants.DOCUMENT_NAME()).toString(), callback);
                                                }
                                            } else {
                                                Log.d(TAG, "checkNumberWithType | onComplete | User Already Signed up ");
                                            }
                                        } else {
                                            Log.d(TAG, "checkNumberWithType | onComplete | No token found ");
                                            if (type == "Owner") {
                                                insertOwnerInfo(data, document.getId(), callback);
                                            } else {
                                                insertEmployeeInfo(data, type, document.getId(), document.get(constants.DOCUMENT_NAME()).toString(), callback);
                                            }
                                        }
                                    }
                                }
                            } else {
                                Log.e(TAG, "checkNumberWithType | Exception occurred: ");
                                callback.callBackMethod(1);
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "checkNumberWithType | Exception occurred: " + e.getMessage());
            callback.callBackMethod(2);
        }
    }

    private void insertEmployeeInfo(Map<String, Object> data, String type, final String credsDocName, String docName, final CALLBACK callback) {
        try {
            Log.d(TAG, "insertEmployeeInfo | In function ");

            Log.d(TAG, "insertEmployeeInfo | Data " + data);
            Log.d(TAG, "insertEmployeeInfo | Type " + type);
            Log.d(TAG, "insertEmployeeInfo | docName " + docName);
            Long timestamp = System.currentTimeMillis();

            final Map<String, Object> appCredData = new HashMap<>();
            appCredData.put(constants.TOKEN(), UUID.randomUUID().toString() + "_-_" + timestamp.toString());
            appCredData.put(constants.TYPE(), type.toUpperCase());
            appCredData.put(constants.PASSWORD(), data.get(constants.PASSWORD()));

            Log.d(TAG, "insertEmployeeInfo | Credentials Data " + appCredData);

            Map<String, Object> empCredData = new HashMap<>();
            empCredData.put(constants.NAME(), data.get(constants.NAME()));
            empCredData.put(constants.PASSWORD(), data.get(constants.PASSWORD()));
            empCredData.put(constants.ADDRESS(), data.get(constants.ADDRESS()));
            empCredData.put(constants.MOBILE(), data.get(constants.MOBILE()));
            empCredData.put(constants.TYPE(), type.toUpperCase());
            empCredData.put(constants.IS_WORKING(), true);

            Log.d(TAG, "insertEmployeeInfo | Employee Data " + empCredData);

            Task credData = db.collection(constants.HOTEL_CRED()).document(credsDocName)
                    .set(appCredData, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "insertEmployeeInfo | onSuccess | Credentials updated ");

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "insertEmployeeInfo | onFailure | Data updated");
                        }
                    });

            Task empData = db.collection(constants.WHOLE_DB()).document(docName).collection(constants.EMP_CRED())
                    .add(empCredData)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "insertEmployeeInfo | onSuccess | Employee data updated " + documentReference);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "insertEmployeeInfo | onSuccess | Failed to update employee data ");
                        }
                    });

            Task allTasks = Tasks.whenAllComplete(credData, empData)
                    .addOnSuccessListener(new OnSuccessListener<List<Task<?>>>() {
                        @Override
                        public void onSuccess(List<Task<?>> tasks) {
                            LoginScreen.setSharedData(constants.TOKEN(), appCredData.get(constants.TOKEN()).toString());
                            LoginScreen.setSharedData("DOC_NAME", credsDocName);

                            Log.d(TAG, "insertOwnerInfo | onSuccess | DOC_NAME => " + appCredData.get(constants.TOKEN()).toString());
                            Log.d(TAG, "insertOwnerInfo | onSuccess | TOKEN => " + appCredData.get(constants.DOCUMENT_NAME()).toString());
                            Log.d(TAG, "insertOwnerInfo | onSuccess | REMEMBER => " + LoginScreen.rememberMeCheckedOrNot);

                            Log.d(TAG, "insertEmployeeInfo | onSuccess | Done with all updates ");
                            callback.callBackMethod(0);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "insertEmployeeInfo | onSuccess | Failed to update all data ");
                            callback.callBackMethod(1);
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "insertEmployeeInfo | Exception while inserting data " + e.getMessage());
            callback.callBackMethod(2);
        }
    }

    private void insertOwnerInfo(Map<String, Object> data, String docId, final CALLBACK callback) {
        try {
            Log.d(TAG, "insertOwnerInfo | In function. ");
            Log.d(TAG, "insertOwnerInfo | Document Id => " + docId + "\nDATA " + data);
            Log.d(TAG, "insertOwnerInfo | PASSWORD => " + data.get(constants.PASSWORD()));
            Long timestamp = System.currentTimeMillis();

            final Map<String, Object> appCredData = new HashMap<>();
            appCredData.put(constants.MOBILE(), data.get(constants.MOBILE()));
            appCredData.put(constants.PASSWORD(), data.get(constants.PASSWORD()));
            appCredData.put(constants.TYPE(), "OWNER");
            appCredData.put(constants.DOCUMENT_NAME(), data.get(constants.HOTEL_NAME()) + "_-_" + timestamp.toString());
            appCredData.put(constants.TOKEN(), UUID.randomUUID().toString() + "_-_" + timestamp.toString());

            Log.d(TAG, "insertOwnerInfo | appCredData " + appCredData);

            Map<String, Object> empCredData = new HashMap<>();
            empCredData.put(constants.MOBILE(), data.get(constants.MOBILE()));
            empCredData.put(constants.TYPE(), "OWNER");
            empCredData.put(constants.AADHAR(), data.get(constants.AADHAR()));
            empCredData.put(constants.GST_NO(), data.get(constants.GST_NO()));
            empCredData.put(constants.NAME(), data.get(constants.NAME()));
            empCredData.put(constants.PASSWORD(), data.get(constants.PASSWORD()));
            empCredData.put(constants.ADDRESS(), data.get(constants.ADDRESS()));

            Log.d(TAG, "insertOwnerInfo | empCredData " + empCredData);

            Task updateCreds = db.collection(constants.HOTEL_CRED()).document(docId)
                    .set(appCredData, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "insertOwnerInfo | onSuccess | Hotel_App_Credentials data updated ");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "insertOwnerInfo | onSuccess | Failed to update in Hotel_App_Credentials");
                        }
                    });

            Task updateDetails = db.collection(constants.WHOLE_DB()).document(appCredData.get(constants.DOCUMENT_NAME()).toString()).collection(constants.EMP_CRED())
                    .add(empCredData)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "insertOwnerInfo | onSuccess | Successfully inserted data in hotel data ");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "insertOwnerInfo | onSuccess | Failed to insert data in hotel data " + e.getMessage());
                        }
                    });

            Task allTasks = Tasks.whenAllComplete(updateCreds, updateDetails)
                    .addOnSuccessListener(new OnSuccessListener<List<Task<?>>>() {
                        @Override
                        public void onSuccess(List<Task<?>> tasks) {
                            LoginScreen.setSharedData(constants.TOKEN(), appCredData.get(constants.TOKEN()).toString());
                            LoginScreen.setSharedData("DOC_NAME", appCredData.get(constants.DOCUMENT_NAME()).toString());
                            Log.d(TAG, "insertOwnerInfo | onSuccess | TOKEN => " + appCredData.get(constants.TOKEN()).toString());
                            Log.d(TAG, "insertOwnerInfo | onSuccess | DOC_NAME => " + appCredData.get(constants.DOCUMENT_NAME()).toString());
                            Log.d(TAG, "insertOwnerInfo | onSuccess | REMEMBER => " + LoginScreen.rememberMeCheckedOrNot);
                            Log.d(TAG, "insertOwnerInfo | onSuccess| All tasks done successfully...");
                            callback.callBackMethod(0);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "insertOwnerInfo | onSuccess| All tasks failed...");
                            callback.callBackMethod(1);
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "insertOwnerInfo | Exception in insertOwnerInfo " + e.getMessage());
            callback.callBackMethod(2);
        }
    }

    public void checkNumberExist(final String mobile, final CALLBACK callback) {
        try {
            db.collection(constants.HOTEL_CRED())
                    .whereEqualTo(constants.MOBILE(), mobile)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty())
                                callback.callBackMethod(0);
                            else callback.callBackMethod(1);
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "checkOwnerNumberExist | Exception in checkOwnerNumberExist " + e.toString());
            callback.callBackMethod(2);
        }
    }

    public void addMenuCategory(final String category, final CALLBACK callback) {
        try {
            userDocument = LoginScreen.getSharedData("DOCUMENT_NAME");
            menuReference = db.collection(constants.WHOLE_DB()).document(userDocument).collection(constants.ADD_MENU()).document(constants.CATEGORIES());

            final Object[][] arr = {null};
            final List<String>[] arr2 = new List[]{new ArrayList<String>()};

            menuReference.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists() && !documentSnapshot.getData().isEmpty()) {
                                Map<String, Object> obj = documentSnapshot.getData();
                                arr[0] = obj.values().toArray();
                                arr2[0] = (List<String>) arr[0][0];

                                if (arr2[0].isEmpty()) add_category(category, callback);
                                else update_category(category, callback);

                            } else add_category(category, callback);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "addMenuCategory |  onFailure | Unable to add category in database ");
                    callback.callBackMethod(1);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "addMenuCategory | Exception " + e.getMessage());
            callback.callBackMethod(2);
        }
    }


    public void add_category(String category, final CALLBACK callback) {
        try {
            String[] arr = category.split("\\s*,\\s*");
            final List<String> tags = Arrays.asList(arr);
            final Map<String, Object> add = new HashMap<>();

            add.put("Category", tags);

            menuReference.set(add)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            callback.callBackMethod(0);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            callback.callBackMethod(1);
                        }
                    });
        } catch (Exception e) {
            callback.callBackMethod(2);
        }
    }


    public void update_category(String category, final CALLBACK callback) {
        try {
            menuReference.update("Category", FieldValue.arrayUnion(category))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            callback.callBackMethod(0);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            callback.callBackMethod(1);
                        }
                    });
        } catch (Exception e) {
            callback.callBackMethod(2);
        }
    }

    public void addDish(final CheckingNewInterface checkingNewInterface) {
        try {
            final Map<String, Object> responseObj = new HashMap<>();
            final List<String> categories = new ArrayList<>();

            userDocument = LoginScreen.getSharedData("DOCUMENT_NAME");
            menuReference = db.collection(constants.WHOLE_DB()).document(userDocument).collection(constants.ADD_MENU()).document(constants.CATEGORIES());

            menuReference.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists() && !documentSnapshot.getData().isEmpty()) {
                                Map<String, Object> obj = documentSnapshot.getData();
                                arr = obj.values().toArray();
                                arr2 = (List<String>) arr[0];
                                for (int j = 0; j < arr2.size(); j++) {
                                    categories.add(arr2.get(j));
                                }
                                responseObj.put("Data", categories);
                                checkingNewInterface.callbackWithData(0, responseObj);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    responseObj.put("Data", null);
                    checkingNewInterface.callbackWithData(1, responseObj);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "addDish | Exception => " + e.getMessage());
            checkingNewInterface.callbackWithData(2, null);
        }
    }

    public void add_dish(String dish_name, String dish_rate, String selected_category, final CheckingNewInterface checkingNewInterface) {
        try {
            final Map<String, Object> responseObj = new HashMap<>();
            CollectionReference menuReference = db.collection(constants.WHOLE_DB()).document(userDocument).collection(constants.ADD_MENU());

            Map<String, Object> dish = new HashMap<>();
            dish.put("Category", selected_category);
            dish.put("Dish-Name", dish_name);
            dish.put("Dish-Rate", dish_rate);

            menuReference.add(dish)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            responseObj.put("Data", "Success");
                            checkingNewInterface.callbackWithData(0, responseObj);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            responseObj.put("Data", "Failed");
                            checkingNewInterface.callbackWithData(1, responseObj);
                        }
                    });
        } catch (Exception e) {
            Log.d(TAG, "add_dish: | Exception in add_dish => " + e.getMessage());
            checkingNewInterface.callbackWithData(2, null);
        }
    }

    private void updatePasswordInDatabase(final String wholeDID, final String credDID, String mobile, String password, final CheckingNewInterface checkingNewInterface) {
        try {
            Log.d(TAG, "updatePasswordInDatabase | documentName => " + wholeDID);
            Log.d(TAG, "updatePasswordInDatabase | documentName => " + credDID);
            Log.d(TAG, "updatePasswordInDatabase | mobile => " + mobile);
            Log.d(TAG, "updatePasswordInDatabase | password => " + password);

            final Map<String, Object> responseObj = new HashMap<>();
            final Map<String, Object> updateData = new HashMap<>();
            updateData.put(constants.PASSWORD(), password);

            Task first = db.collection(constants.WHOLE_DB()).document(wholeDID).collection(constants.EMP_CRED())
                    .whereEqualTo(constants.MOBILE(), mobile)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String docId = document.getId();
                                    Log.d(TAG, "updatePasswordInDatabase | onComplete | docId " + docId);

                                    db.collection(constants.WHOLE_DB()).document(wholeDID).collection(constants.EMP_CRED()).document(docId)
                                            .update(updateData)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    responseObj.put("WHOLE_DATA", "Success");
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            responseObj.put("WHOLE_DATA", "Failed");
                                        }
                                    });
                                }
                            } else {
                                responseObj.put("WHOLE_DATA", "Failed");
                            }
                        }
                    });

            Task second = db.collection(constants.HOTEL_CRED()).document(credDID)
                    .update(updateData)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Log.d(TAG, "onComplete | task.isSuccessful() ==> " + task.isSuccessful());
                            Log.d(TAG, "onComplete | task.isComplete() ==> " + task.isComplete());

                            if (task.isSuccessful()) {
                                responseObj.put("CRED_DATA", "Success");
                            } else {
                                responseObj.put("CRED_DATA", "Failed");
                            }
                        }
                    });

            Task done = Tasks.whenAllComplete(first, second)
                    .addOnSuccessListener(new OnSuccessListener<List<Task<?>>>() {
                        @Override
                        public void onSuccess(List<Task<?>> tasks) {
                            Log.d(TAG, "onSuccess | tasks " + tasks);
                            responseObj.put("ALL_DATA", "Success");
                            checkingNewInterface.callbackWithData(0, responseObj);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure | Exception while updating data. " + e.getMessage());
                            responseObj.put("ALL_DATA", "Failed");
                            checkingNewInterface.callbackWithData(1, responseObj);
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "updatePasswordInDatabase | Exception in updatePasswordInDatabase " + e.getMessage());
            checkingNewInterface.callbackWithData(2, null);
        }
    }

    public void updatePassword(final String mobile, final String password, final CheckingNewInterface checkingNewInterface) {
        try {
            final Map<String, Object> responseObj = new HashMap<>();

            db.collection(constants.HOTEL_CRED())
                    .whereEqualTo(constants.MOBILE(), mobile)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.contains(constants.TOKEN())) {
                                        String wholeDID = document.getString(constants.DOCUMENT_NAME());
                                        String credDID = document.getId();

                                        Log.d(TAG, "onComplete | Document Name => " + wholeDID);
                                        updatePasswordInDatabase(wholeDID, credDID, mobile, password, checkingNewInterface);
                                    } else {
                                        responseObj.put("Data", "User not registered");
                                        checkingNewInterface.callbackWithData(1, responseObj);
                                    }
                                }
                            } else {
                                responseObj.put("Data", "Number not found");
                                checkingNewInterface.callbackWithData(1, responseObj);
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "updatePassword | Exception in updatePassword. " + e.getMessage());
            checkingNewInterface.callbackWithData(2, null);
        }
    }
}