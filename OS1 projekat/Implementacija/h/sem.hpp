//
// Created by os on 6/27/22.
//

#ifndef PROJECT_BASE_V1_1_SEM_HPP
#define PROJECT_BASE_V1_1_SEM_HPP

#include "ccb.hpp"
#include "kernel.hpp"
//#include "list.hpp"
#include "array_of_semaphores.hpp"

class Sem{

private:

    static Sem* createSem(unsigned init);
    static int destroySem(Sem* ptr);

    static Array_of_sem semaphores;

    int val;
    Array_of_ccb blocked;

    Sem(unsigned init): val(init){
        semaphores.add_last(this);
    }

    void* operator new(uint64 n){
        return kernel::mem_alloc(n);
    }

    void operator delete(void *p) {
        kernel::mem_free(p);
    }

    void block();
    void unblock();

    void wait();
    void signal();

    friend int kernel::sem_open(sem_t* handle, unsigned init);

    friend int kernel::sem_close(sem_t handle);

    friend int kernel::sem_wait(sem_t id);

    friend int kernel::sem_signal(sem_t id);


};


#endif //PROJECT_BASE_V1_1_SEM_HPP
