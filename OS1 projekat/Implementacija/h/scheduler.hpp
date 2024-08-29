//
// Created by os on 6/21/22.
//

#ifndef PROJECT_BASE_V1_1_SCHEDULER_HPP
#define PROJECT_BASE_V1_1_SCHEDULER_HPP

//#include "list.hpp"
#include "array_of_ccb.hpp"

class CCB;

class Scheduler {
public:
    static Array_of_ccb readyCoroutineQueue;

public:
    static CCB* get();
    static void put(CCB *ccb);

};


#endif //PROJECT_BASE_V1_1_SCHEDULER_HPP
