//
// Created by os on 8/9/22.
//
#include "../h/array_of_semaphores.hpp"

void Array_of_sem::add_last(Sem *arg) {
    if(end < ARRAY_SIZE_SEM){
        array[end] = arg;
        end++;
    }
}

Sem *Array_of_sem::remove_first() {
    Sem* ret = nullptr;
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

Sem *Array_of_sem::remove_elem(Sem *arg) {
    Sem* ret = nullptr;
    int i;
    for(i = 0; i < end; i++){
        if(array[i] == arg){
            ret = array[i];
            for(int j = i + 1; i < end; i++, j++){
                array[i] = array[j];
            }
            end--;
            break;
        }
    }
    return ret;
}
