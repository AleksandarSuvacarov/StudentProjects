//
// Created by os on 6/17/22.
//

#ifndef PROJECT_BASE_V1_1_MEMORYALLOCATOR_HPP
#define PROJECT_BASE_V1_1_MEMORYALLOCATOR_HPP

#include "../lib/hw.h"
#include "kernel.hpp"


class MemoryAllocator {

private:

    typedef struct Block_header {
        Block_header *next;
        size_t size;
    } Block_header;

    static size_t MIN_BLOCK_SIZE;
    static Block_header* free_mem_head;
    static int ind;

    MemoryAllocator(){}

    MemoryAllocator(const MemoryAllocator& other) = delete;
    void operator=(const MemoryAllocator& other) = delete;

    static void *mem_alloc_internal(size_t size);
    static int mem_free_internal(void* pointer);


    friend void* kernel::mem_alloc(size_t);
    friend int kernel::mem_free(void*);

};

#endif //PROJECT_BASE_V1_1_MEMORYALLOCATOR_HPP
