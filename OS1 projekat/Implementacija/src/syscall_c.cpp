//
// Created by os on 8/4/22.
//
#include "../h/syscall_c.h"
#include "../h/ccb.hpp"

void *mem_alloc(size_t size){
    size_t size_in_blks = size / MEM_BLOCK_SIZE + (size % MEM_BLOCK_SIZE > 0 ? 1 : 0);
    return abi::mem_alloc(size_in_blks);
}

int mem_free(void* ptr){
    return abi::mem_free(ptr);
}

int thread_create(
        thread_t *handle,
        void(*start_routine)(void *),
        void *arg
){
    void* stack_space = nullptr;
    if(start_routine != nullptr)stack_space = mem_alloc(CCB::STACK_SIZE);
    if(stack_space == nullptr && start_routine != nullptr) return -1;
    return abi::thread_create(handle, start_routine, arg, stack_space);
}

void thread_dispatch(){
    abi::thread_dispatch();
}

int thread_exit(){
    return abi::thread_exit();
}

int sem_open(sem_t *handle, unsigned init){
    return abi::sem_open(handle, init);
}

int sem_close(sem_t handle){
    return abi::sem_close(handle);
}

int sem_wait(sem_t id){
    return abi::sem_wait(id);
}

int sem_signal(sem_t id){
    return abi::sem_signal(id);
}

void putc(char chr){
    abi::putc(chr);
}

char getc(){
    return abi::getc();
}

int thread_create_no_start(thread_t *handle, void (*start_routine)(void *), void *arg) {
    void* stack_space = nullptr;
    if(start_routine != nullptr)stack_space = mem_alloc(CCB::STACK_SIZE);
    if(stack_space == nullptr && start_routine != nullptr) return -1;
    return abi::thread_create_no_start(handle, start_routine, arg, stack_space);
}

void put_in_scheduler(thread_t handle){
    abi::put_in_scheduler(handle);
}