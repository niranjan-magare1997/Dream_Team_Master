package com.example.dream_team.interfaces;

/**
 * 0 = (Task Successful) True Result
 * 1 = (Task Failed) False Result
 * 2 = ERROR Caught in catch block
 */

public interface CALLBACK {
    void callBackMethod(int result);
}
