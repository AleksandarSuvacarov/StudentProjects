//
// Created by os on 8/4/22.
//

#ifndef PROJECT_BASE_V1_1_SYSCALL_C_H
#define PROJECT_BASE_V1_1_SYSCALL_C_H

#include "../lib/hw.h"
#include "../h/abi.hpp"

void *mem_alloc(size_t);

int mem_free(void *);

class CCB;
typedef CCB* thread_t;

class Sem;
typedef Sem* sem_t;

int thread_create(
        thread_t *handle,
        void(*start_routine)(void *),
        void *arg
);

int thread_create_no_start(
        thread_t *handle,
        void(*start_routine)(void *),
        void *arg
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


#endif //PROJECT_BASE_V1_1_SYSCALL_C_H
