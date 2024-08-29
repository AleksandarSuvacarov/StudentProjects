//
// Created by os on 6/21/22.
//
#include "../h/syscall_c.h"
#include "../h/syscall_cpp.hpp"


void* operator new(uint64 n){
    return mem_alloc(n);
}

void* operator new[](uint64 n){
    return mem_alloc(n);
}

void operator delete(void *p) noexcept{
    mem_free(p);
}

void operator delete[](void *p) noexcept {
    mem_free(p);
}

