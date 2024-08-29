//
// Created by os on 6/24/22.
//

#ifndef PROJECT_BASE_V1_1_KERNEL_HPP
#define PROJECT_BASE_V1_1_KERNEL_HPP

#include "../lib/hw.h"

class CCB;
typedef CCB* thread_t;

class Sem;
typedef Sem* sem_t;

namespace kernel {

    void *mem_alloc(size_t size);

    int mem_free(void *pointer);

    int thread_create (
            thread_t** handle,
            void(*start_routine)(void*),
            void* arg,
            void* stack_space
    );

    int thread_create_no_start (
            thread_t** handle,
            void(*start_routine)(void*),
            void* arg,
            void* stack_space
    );

    void put_in_scheduler(thread_t handle);

    void thread_dispatch ();

    int thread_exit();

    int sem_open(sem_t* handle, unsigned init);

    int sem_close(sem_t handle);

    int sem_wait(sem_t id);

    int sem_signal(sem_t id);

}

#endif //PROJECT_BASE_V1_1_KERNEL_HPP
