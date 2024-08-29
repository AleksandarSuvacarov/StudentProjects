
#include "../h/abi.hpp"
//#include "../h/ccb.hpp"
//#include "../lib/console.h"


void* abi::mem_alloc(size_t size){
    asm volatile("mv a1, %0" : : "r" (size));
    asm volatile("mv a0, %0" : : "r" (0x01));
    asm volatile("ecall");
    void* ptr;
    asm volatile("mv %0, a0" : "=r" (ptr));
    return ptr;
}

int abi::mem_free(void* pointer){
    asm volatile("mv a1, %0" : : "r" (pointer));
    asm volatile("mv a0, %0" : : "r" (0x02));
    asm volatile("ecall");
    int r;
    asm volatile("mv %0, a0" : "=r" (r));
    return r;
}

int abi::thread_create(thread_t *handle, void (*start_routine)(void *), void *arg, void* stack_space) {
/*    void* stack_space = nullptr;
    if(start_routine != nullptr)stack_space = mem_alloc(CCB::STACK_SIZE);
    if(stack_space == nullptr && start_routine != nullptr) return -1;*/
    asm volatile("mv a4, %0" : : "r" (stack_space));
    asm volatile("mv a3, %0" : : "r" (arg));
    asm volatile("mv a2, %0" : : "r" (start_routine));
    asm volatile("mv a1, %0" : : "r" (&handle));
    asm volatile("mv a0, %0" : : "r" (0x11));
    asm volatile("ecall");
    if (*handle == nullptr) {
        abi::mem_free(stack_space);
        return -1;
    }
    return 0;
}

void abi::thread_dispatch() {
    asm volatile("mv a0, %0" : : "r" (0x13));
    asm volatile("ecall");
}

int abi::thread_exit(){
    asm volatile("mv a0, %0" : : "r" (0x12));
    asm volatile("ecall");
    int r;
    asm volatile("mv %0, a0" : "=r" (r));
    return r;
}

int abi::thread_create_no_start(thread_t *handle, void (*start_routine)(void *), void *arg, void* stack_space) {
    asm volatile("mv a4, %0" : : "r" (stack_space));
    asm volatile("mv a3, %0" : : "r" (arg));
    asm volatile("mv a2, %0" : : "r" (start_routine));
    asm volatile("mv a1, %0" : : "r" (&handle));
    asm volatile("mv a0, %0" : : "r" (0x14));
    asm volatile("ecall");
    if (*handle == nullptr) {
        abi::mem_free(stack_space);
        return -1;
    }
    return 0;
}


int abi::sem_open(sem_t* handle, unsigned init){
    asm volatile("mv a2, %0" : : "r" (init));
    asm volatile("mv a1, %0" : : "r" (handle));
    asm volatile("mv a0, %0" : : "r" (0x21));
    asm volatile("ecall");
    int r;
    asm volatile("mv %0, a0" : "=r" (r));
    return r;
}

int abi::sem_close(sem_t handle){
    asm volatile("mv a1, %0" : : "r" (handle));
    asm volatile("mv a0, %0" : : "r" (0x22));
    asm volatile("ecall");
    int r;
    asm volatile("mv %0, a0" : "=r" (r));
    return r;
}

int abi::sem_wait(sem_t id){
    asm volatile("mv a1, %0" : : "r" (id));
    asm volatile("mv a0, %0" : : "r" (0x23));
    asm volatile("ecall");
    int r;
    asm volatile("mv %0, a0" : "=r" (r));
    return r;
}

int abi::sem_signal(sem_t id){
    asm volatile("mv a1, %0" : : "r" (id));
    asm volatile("mv a0, %0" : : "r" (0x24));
    asm volatile("ecall");
    int r;
    asm volatile("mv %0, a0" : "=r" (r));
    return r;
}

char abi::getc(){
    asm volatile("mv a0, %0" : : "r" (0x41));
    asm volatile("ecall");
    char ch;
    asm volatile("mv %0, a0" : "=r" (ch));
    return ch;
    //return __getc();
}

void abi::putc(char chr){
    asm volatile("mv a1, %0" : : "r" (chr));
    asm volatile("mv a0, %0" : : "r" (0x42));
    asm volatile("ecall");
    //__putc(chr);
}

void abi::put_in_scheduler(thread_t handle) {
    asm volatile("mv a1, %0" : : "r" (handle));
    asm volatile("mv a0, %0" : : "r" (0x15));
    asm volatile("ecall");
}
