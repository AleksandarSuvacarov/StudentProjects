//
// Created by os on 6/24/22.
//

#ifndef PROJECT_BASE_V1_1_ABI_HPP
#define PROJECT_BASE_V1_1_ABI_HPP

#include "../lib/hw.h"

class CCB;
class Sem;

namespace abi {

    void *mem_alloc(size_t);

    int mem_free(void *);

    typedef CCB *thread_t;
    typedef Sem *sem_t;

    int thread_create(
            thread_t *handle,
            void(*start_routine)(void *),
            void *arg,
            void* stack_space
    );

    int thread_create_no_start(
            thread_t *handle,
            void(*start_routine)(void *),
            void *arg,
            void* stack_space
    );

    void put_in_scheduler(thread_t handle);

    void thread_dispatch();

    int thread_exit();

    int sem_open(sem_t *handle, unsigned init);

    int sem_close(sem_t handle);

    int sem_wait(sem_t id);

    int sem_signal(sem_t id);

    void putc(char chr);

    char getc();
}

#endif //PROJECT_BASE_V1_1_ABI_HPP
