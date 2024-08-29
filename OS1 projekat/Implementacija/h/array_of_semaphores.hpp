//
// Created by os on 8/9/22.
//

#ifndef PROJECT_BASE_V1_1_ARRAY_OF_SEMAPHORES_HPP
#define PROJECT_BASE_V1_1_ARRAY_OF_SEMAPHORES_HPP

#include "kernel.hpp"
#define ARRAY_SIZE_SEM 100

class Sem;

class Array_of_sem{

    Sem* array[ARRAY_SIZE_SEM];

    int end = 0;

public:
    Array_of_sem(){
        for(int i = 0; i < ARRAY_SIZE_SEM; i++){
            array[i] = nullptr;
        }
    }

    void add_last(Sem*);
    Sem* remove_first();
    Sem* remove_elem(Sem*);

    friend int kernel::sem_wait(sem_t);
    friend int kernel::sem_signal(sem_t);
    friend int kernel::sem_close(sem_t);
};





#endif //PROJECT_BASE_V1_1_ARRAY_OF_SEMAPHORES_HPP
