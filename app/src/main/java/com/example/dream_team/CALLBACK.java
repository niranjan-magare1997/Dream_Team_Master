package com.example.dream_team;

/**
 * 1 = (Task Failed) False Result
 * 0 = (Task Successful) True Result
 * 2 = ERROR
 */

public interface CALLBACK {
    void callBackMethod(int result);
    void getData(String docName, String token);
}
