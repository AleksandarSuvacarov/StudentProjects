//
// Created by os on 8/9/22.
//
#include "../h/array_of_ccb.hpp"

void Array_of_ccb::add_last(CCB *arg) {
    if(end < ARRAY_SIZE){
        array[end] = arg;
        end++;
    }
}

CCB *Array_of_ccb::remove_first() {
    CCB* ret = nullptr;
    if(array[0] != nullptr){
        ret = array[0];
        int i = 0, j = 1;
        for(; i < end; i++, j++){
            array[i] = array[j];
        }
        end--;
    }
    return ret;
}
