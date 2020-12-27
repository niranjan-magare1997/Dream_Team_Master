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
import java.util.Objects;

public class DATABASE {
    private static final String TAG = "Dream_Team | DATABASE ";
    private static FirebaseFirestore db;
    private DocumentReference menuReference;
    private String userDocument = null;
    private Object[] arr;
    private List<String> arr2 = new ArrayList<String>(); // to cast an array into List

    public DATABASE() {
        db = FirebaseFirestore.getInstance();
    }

    public void logInUser(String id, final String password, final CheckingNewInterface callback) {
        try {
            Log.d(TAG, "checkUserExist | Checking user exist or not ");
            final Map<String, Object> responseObject = new HashMap<>();

            db.collection(CONSTANTS.hotel_cred())
                    .whereEqualTo(CONSTANTS.mobile(), id)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            Log.d(TAG, "checkUserExist | onComplete | " + Objects.requireNonNull(task.getResult()).isEmpty());
                            if (task.isSuccessful() && !Objects.requireNonNull(task.getResult()).isEmpty()) {
                                Log.d(TAG, "checkUserExist | onComplete | User exist ");
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    final String docName = document.getString(CONSTANTS.document_name());
//                                    final String token = document.getString(CONSTANTS.TOKEN());
                                    final String type = document.getString(CONSTANTS.type());
                                    final String dbPassword = document.getString(CONSTANTS.password());

                                    if (password.equals(dbPassword)) {
//                                        COMMON.setSharedData(CONSTANTS.TOKEN(), token);
                                        COMMON.setSharedData(CONSTANTS.document_name(), docName);
                                        COMMON.setSharedData(CONSTANTS.type(), type);
                                        if (LoginScreen.rememberMeCheckedOrNot) {
                                            COMMON.setSharedData(CONSTANTS.remember_me(), "true");
                                        } else {
                                            COMMON.setSharedData(CONSTANTS.remember_me(), "false");
                                        }
                                        responseObject.put(CONSTANTS.message(), "Log In Successfully");
                                        responseObject.put(CONSTANTS.type(), type);
                                        callback.callbackWithData(0, responseObject);
                                    } else {
                                        responseObject.put(CONSTANTS.message(), "Wrong password");
                                        callback.callbackWithData(1, responseObject);
                                    }
                                }
                            } else {
                                responseObject.put(CONSTANTS.message(), "User not exist");
                                callback.callbackWithData(1, responseObject);
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "checkUserExist | " + e.getMessage());
            callback.callbackWithData(2, null);
        }
    }

    public void signUpUser(String mobile, final String type, final Map<String, Object> data, final CheckingNewInterface callback) {
        try {
            Log.d(TAG, "checkNumberWithType | In function checkNumberWithType Mobile => " + mobile + " Type => " + type);

            final Map<String, Object> responseObject = new HashMap<>();

            db.collection(CONSTANTS.hotel_cred())
                    .whereEqualTo(CONSTANTS.mobile(), mobile.trim())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !Objects.requireNonNull(task.getResult()).isEmpty()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, "checkNumberWithType | onComplete | Found match ");
                                    String documentId = document.getId();
                                    String userDocument = document.getString(CONSTANTS.document_name());
                                    String dbType = Objects.requireNonNull(document.getString(CONSTANTS.type())).trim();

                                    if (dbType.equals("OWNER")) {
                                        insertOwnerInfo(data, documentId, callback);
                                    } else if (dbType.equals("CHEF") || dbType.equals("WAITER")) {
                                        insertEmployeeInfo(data, type, documentId, userDocument, callback);
                                    } else {
                                        Log.d(TAG, "checkNumberWithType | onComplete | Wrong user Type => " + dbType);
                                        callback.callbackWithData(1, null);
                                    }
                                }
                            } else {
                                Log.e(TAG, "checkNumberWithType | Number not exist ");
                                responseObject.put("MESSAGE", "User not exist");
                                callback.callbackWithData(1, responseObject);
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "checkNumberWithType | Exception occurred: " + e.getMessage());
            callback.callbackWithData(2, null);
        }
    }

    private void insertEmployeeInfo(Map<String, Object> data, String type, final String dbDocName, String docName, final CheckingNewInterface callback) {
        try {
            Log.d(TAG, "insertEmployeeInfo | In function ");

            final Map<String, Object> responseObject = new HashMap<>();

            Log.d(TAG, "insertEmployeeInfo | Data " + data);
            Log.d(TAG, "insertEmployeeInfo | Type " + type);
            Log.d(TAG, "insertEmployeeInfo | docName " + docName);
            long timestamp = System.currentTimeMillis();

            final Map<String, Object> appCredData = new HashMap<>();
//            appCredData.put(CONSTANTS.TOKEN(), UUID.randomUUID().toString() + "_-_" + timestamp);
            appCredData.put(CONSTANTS.type(), type.toUpperCase());
            appCredData.put(CONSTANTS.password(), data.get(CONSTANTS.password()));

            Log.d(TAG, "insertEmployeeInfo | Credentials Data " + appCredData);

            Map<String, Object> empCredData = new HashMap<>();
            empCredData.put(CONSTANTS.name(), data.get(CONSTANTS.name()));
            empCredData.put(CONSTANTS.password(), data.get(CONSTANTS.password()));
            empCredData.put(CONSTANTS.address(), data.get(CONSTANTS.address()));
            empCredData.put(CONSTANTS.mobile(), data.get(CONSTANTS.mobile()));
            empCredData.put(CONSTANTS.type(), type.toUpperCase());
            empCredData.put(CONSTANTS.is_working(), true);

            Log.d(TAG, "insertEmployeeInfo | Employee Data " + empCredData);

            Task credData = db.collection(CONSTANTS.hotel_cred()).document(dbDocName)
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

            Task empData = db.collection(CONSTANTS.whole_db()).document(docName).collection(CONSTANTS.emp_cred())
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

//                            Log.d(TAG, "insertOwnerInfo | onSuccess | TOKEN => " + appCredData.get(CONSTANTS.TOKEN()).toString());
                            Log.d(TAG, "insertOwnerInfo | onSuccess | DOC_NAME => " + appCredData.get(CONSTANTS.document_name()).toString());
                            Log.d(TAG, "insertOwnerInfo | onSuccess | REMEMBER => " + LoginScreen.rememberMeCheckedOrNot);

                            Log.d(TAG, "insertEmployeeInfo | onSuccess | Done with all updates ");

                            responseObject.put("MESSAGE", "Employee info inserted successfully");
                            callback.callbackWithData(0, responseObject);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "insertEmployeeInfo | onSuccess | Failed to update all data ");
                            responseObject.put("MESSAGE", "Failed to sign up");
                            callback.callbackWithData(1, responseObject);
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "insertEmployeeInfo | Exception while inserting data " + e.getMessage());
            callback.callbackWithData(2, null);
        }
    }

    private void insertOwnerInfo(Map<String, Object> data, String docId, final CheckingNewInterface callback) {
        try {
            Log.d(TAG, "insertOwnerInfo | In function. ");
            Log.d(TAG, "insertOwnerInfo | Document Id => " + docId + "\nDATA " + data);
            Log.d(TAG, "insertOwnerInfo | PASSWORD => " + data.get(CONSTANTS.password()));

            final Map<String, Object> responseObject = new HashMap<>();

            long timestamp = System.currentTimeMillis();

            final Map<String, Object> appCredData = new HashMap<>();
            appCredData.put(CONSTANTS.mobile(), data.get(CONSTANTS.mobile()));
            appCredData.put(CONSTANTS.password(), data.get(CONSTANTS.password()));
            appCredData.put(CONSTANTS.type(), "OWNER");
            appCredData.put(CONSTANTS.document_name(), data.get(CONSTANTS.hotel_name()) + "_-_" + timestamp);
//            appCredData.put(CONSTANTS.TOKEN(), UUID.randomUUID().toString() + "_-_" + timestamp);

            Log.d(TAG, "insertOwnerInfo | appCredData " + appCredData);

            Map<String, Object> empCredData = new HashMap<>();
            empCredData.put(CONSTANTS.mobile(), data.get(CONSTANTS.mobile()));
            empCredData.put(CONSTANTS.type(), "OWNER");
            empCredData.put(CONSTANTS.aadhar(), data.get(CONSTANTS.aadhar()));
            empCredData.put(CONSTANTS.gst_number(), data.get(CONSTANTS.gst_number()));
            empCredData.put(CONSTANTS.name(), data.get(CONSTANTS.name()));
            empCredData.put(CONSTANTS.password(), data.get(CONSTANTS.password()));
            empCredData.put(CONSTANTS.address(), data.get(CONSTANTS.address()));

            Log.d(TAG, "insertOwnerInfo | empCredData " + empCredData);

            Task updateCreds = db.collection(CONSTANTS.hotel_cred()).document(docId)
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

            Task updateDetails = db.collection(CONSTANTS.whole_db()).document(appCredData.get(CONSTANTS.document_name()).toString()).collection(CONSTANTS.emp_cred())
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
//                            Log.d(TAG, "insertOwnerInfo | onSuccess | TOKEN => " + appCredData.get(CONSTANTS.TOKEN()).toString());
                            Log.d(TAG, "insertOwnerInfo | onSuccess | DOC_NAME => " + appCredData.get(CONSTANTS.document_name()).toString());
                            Log.d(TAG, "insertOwnerInfo | onSuccess | REMEMBER => " + LoginScreen.rememberMeCheckedOrNot);
                            Log.d(TAG, "insertOwnerInfo | onSuccess| All tasks done successfully...");
                            responseObject.put("MESSAGE", "Sign up success");
                            callback.callbackWithData(0, responseObject);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "insertOwnerInfo | onSuccess| All tasks failed...");
                            responseObject.put("MESSAGE", "Failed to sign up");
                            callback.callbackWithData(1, responseObject);
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "insertOwnerInfo | Exception in insertOwnerInfo " + e.getMessage());
            callback.callbackWithData(2, null);
        }
    }

    public void checkNumberExist(final String mobile, final CALLBACK callback) {
        try {
            db.collection(CONSTANTS.hotel_cred())
                    .whereEqualTo(CONSTANTS.mobile(), mobile)
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
            userDocument = COMMON.getSharedData("DOCUMENT_NAME");
            menuReference = db.collection(CONSTANTS.whole_db()).document(userDocument).collection(CONSTANTS.add_menu()).document(CONSTANTS.categories());

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

            userDocument = COMMON.getSharedData("DOCUMENT_NAME");
            Log.d(TAG, "addDish | userDocument => " + userDocument);
            menuReference = db.collection(CONSTANTS.whole_db()).document(userDocument).collection(CONSTANTS.add_menu()).document(CONSTANTS.categories());

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
            CollectionReference menuReference = db.collection(CONSTANTS.whole_db()).document(userDocument).collection(CONSTANTS.add_menu());

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
            updateData.put(CONSTANTS.password(), password);

            Task first = db.collection(CONSTANTS.whole_db()).document(wholeDID).collection(CONSTANTS.emp_cred())
                    .whereEqualTo(CONSTANTS.mobile(), mobile)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String docId = document.getId();
                                    Log.d(TAG, "updatePasswordInDatabase | onComplete | docId " + docId);

                                    db.collection(CONSTANTS.whole_db()).document(wholeDID).collection(CONSTANTS.emp_cred()).document(docId)
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

            Task second = db.collection(CONSTANTS.hotel_cred()).document(credDID)
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

            db.collection(CONSTANTS.hotel_cred())
                    .whereEqualTo(CONSTANTS.mobile(), mobile)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !Objects.requireNonNull(task.getResult()).isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String wholeDID = document.getString(CONSTANTS.document_name());
                                    String credDID = document.getId();

                                    Log.d(TAG, "onComplete | Document Name => " + wholeDID);
                                    updatePasswordInDatabase(wholeDID, credDID, mobile, password, checkingNewInterface);
                                }
                            } else {
                                responseObj.put("Data", "User not found");
                                checkingNewInterface.callbackWithData(1, responseObj);
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "updatePassword | Exception in updatePassword. " + e.getMessage());
            checkingNewInterface.callbackWithData(2, null);
        }
    }

    public void addEmployee(final Map<String, Object> empInfo, final CheckingNewInterface checkingNewInterface) {
        try {
            final Map<String, Object> responseObj = new HashMap<>();
            final String documentName = COMMON.getSharedData(CONSTANTS.document_name());

            Log.d(TAG, "addEmployee: Document Name => " + documentName);

            if (documentName.equals("")) {
                responseObj.put(CONSTANTS.message(), "Document name not found in shared preference");
                checkingNewInterface.callbackWithData(1, responseObj);
            } else {
                checkNumberExist(empInfo.get(CONSTANTS.mobile()).toString(), new CALLBACK() {
                    @Override
                    public void callBackMethod(int result) {
                        if (result == 0) {
                            Log.d(TAG, "callBackMethod: Number already exist...");
                            responseObj.put(CONSTANTS.message(), "Number already Registered");
                            checkingNewInterface.callbackWithData(1, responseObj);
                        } else {
                            final Map<String, Object> credInfo = new HashMap<>();
                            credInfo.put(CONSTANTS.mobile(), empInfo.get(CONSTANTS.mobile()));
                            credInfo.put(CONSTANTS.type(), empInfo.get(CONSTANTS.type()).toString().toUpperCase());
                            credInfo.put(CONSTANTS.document_name(), documentName);

                            Log.d(TAG, "addEmployee: credInfo => " + credInfo);

                            final Map<String, Object> dataInfo = new HashMap<>();
                            dataInfo.put(CONSTANTS.mobile(), empInfo.get(CONSTANTS.mobile()));
                            dataInfo.put(CONSTANTS.type(), empInfo.get(CONSTANTS.type()).toString().toUpperCase());
                            dataInfo.put(CONSTANTS.name(), empInfo.get(CONSTANTS.name()));
                            dataInfo.put(CONSTANTS.status(), empInfo.get(CONSTANTS.status()));

                            Log.d(TAG, "addEmployee: dataInfo => " + dataInfo);

                            Task wholeDb = db.collection(CONSTANTS.whole_db()).document(documentName).collection(CONSTANTS.emp_cred())
                                    .add(dataInfo)
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "onFailure: Failed to update whole database... ");
                                        }
                                    });

                            Task credentialsDb = db.collection(CONSTANTS.hotel_cred())
                                    .add(credInfo)
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "onFailure: Failed to update credentials database... ");
                                        }
                                    });

                            Task bothDbUpdation = Tasks.whenAllComplete(wholeDb, credentialsDb)
                                    .addOnSuccessListener(new OnSuccessListener<List<Task<?>>>() {
                                        @Override
                                        public void onSuccess(List<Task<?>> tasks) {
                                            responseObj.put(CONSTANTS.message(), "Employee added successfully");
                                            responseObj.put("ALL_DATA", "Success");
                                            checkingNewInterface.callbackWithData(0, responseObj);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            responseObj.put(CONSTANTS.message(), "Failed to add employee");
                                            responseObj.put("ALL_DATA", "Failed");
                                            checkingNewInterface.callbackWithData(1, responseObj);
                                        }
                                    });
                        }
                    }
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "updatePassword | Exception in updatePassword. " + e.getMessage());
            checkingNewInterface.callbackWithData(2, null);
        }
    }
}