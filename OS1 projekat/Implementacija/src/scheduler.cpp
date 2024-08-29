//
// Created by os on 6/21/22.
//

#include "../h/scheduler.hpp"

//List<CCB> Scheduler:: readyCoroutineQueue;

Array_of_ccb Scheduler:: readyCoroutineQueue;

CCB* Scheduler::get(){
    return readyCoroutineQueue.remove_first();
}

void Scheduler::put(CCB* ccb){
    readyCoroutineQueue.add_last(ccb);
}