//
// Created by os on 6/24/22.
//

#include "../h/MemoryAllocator.hpp"
#include "../h/kernel.hpp"
#include "../h/ccb.hpp"
#include "../h/sem.hpp"

void *kernel::mem_alloc(size_t size) {
    return MemoryAllocator::mem_alloc_internal(size);
}

int kernel::mem_free(void *pointer) {
    return MemoryAllocator::mem_free_internal(pointer);
}

int kernel::thread_create(thread_t **handle, void (*start_routine)(void *), void *arg, void *stack_space) {
    **handle = CCB::createCoroutine(start_routine, arg, stack_space);
    if(**handle == nullptr) return -1;
    return 0;
}

void kernel::thread_dispatch() {
    CCB::yield();
}

int kernel::thread_exit() {
    return CCB::exit();
}

int kernel::thread_create_no_start(thread_t **handle, void (*start_routine)(void *), void *arg, void *stack_space) {
    **handle = CCB::createCoroutine_no_start(start_routine, arg, stack_space);
    if(**handle == nullptr) return -1;
    return 0;
}

void kernel::put_in_scheduler(thread_t handle) {
    Scheduler::put(handle);
}

int kernel::sem_open(sem_t *handle, unsigned int init) {
    *handle = Sem::createSem(init);
    if(*handle == nullptr) return -1;
    return 0;
}

int kernel::sem_wait(sem_t id) {
    /*List<Sem>::Elem* curr;
    for(curr = Sem::semaphores.head; curr != nullptr && curr->data != id; curr = curr->next);
    if(curr == nullptr) return -1;*/
    Sem* curr = nullptr;
    int i = 0;
    for(curr = Sem::semaphores.array[0]; curr != nullptr && i < Sem::semaphores.end; i++);
    if (curr == nullptr) return -1;
    id->wait();
    return 0;
}

int kernel::sem_signal(sem_t id) {
    /*List<Sem>::Elem* curr;
    for(curr = Sem::semaphores.head; curr != nullptr && curr->data != id; curr = curr->next);
    if(curr == nullptr) return -1;*/
    Sem* curr = nullptr;
    int i = 0;
    for(curr = Sem::semaphores.array[0]; curr != nullptr && i < Sem::semaphores.end; i++);
    if (curr == nullptr) return -1;
    id->signal();
    return 0;
}

int kernel::sem_close(sem_t handle) {
    return Sem::destroySem(handle);
}

