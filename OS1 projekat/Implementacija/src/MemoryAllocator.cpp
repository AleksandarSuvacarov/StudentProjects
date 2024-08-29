//
// Created by os on 6/18/22.
//

#include "../h/MemoryAllocator.hpp"

MemoryAllocator::Block_header* MemoryAllocator::free_mem_head = (MemoryAllocator::Block_header*)HEAP_START_ADDR;
int MemoryAllocator::ind = 1;
size_t MemoryAllocator::MIN_BLOCK_SIZE = MEM_BLOCK_SIZE;

void* MemoryAllocator::mem_alloc_internal(size_t size) {
    if (ind){
        free_mem_head = (Block_header*)HEAP_START_ADDR;
        free_mem_head->size = (uint8*)HEAP_END_ADDR - (uint8*)HEAP_START_ADDR - sizeof(Block_header);
        free_mem_head->next = nullptr;
        ind = 0;
    }

    size_t size_in_blks = size / MEM_BLOCK_SIZE + (size % MEM_BLOCK_SIZE > 0 ? 1 : 0);

    Block_header *curr = free_mem_head, *prev = nullptr;
    for (; curr != nullptr; prev = curr, curr = curr->next) {
        if (curr->size >= size_in_blks * MEM_BLOCK_SIZE) break;
    }

    if (curr == nullptr)
        return nullptr;

    size_t remaining_size = curr->size - size_in_blks * MEM_BLOCK_SIZE;
    if(remaining_size < MIN_BLOCK_SIZE){
        if(prev == nullptr) {
            free_mem_head = curr->next;
        }
        else{
            prev->next = curr->next;
        }
        return (uint8*)curr + sizeof(Block_header);
    }
    else{
        uint8* tmp = (uint8*)curr;
        tmp += sizeof(Block_header) + size_in_blks * MEM_BLOCK_SIZE;
        Block_header* next = (Block_header*) tmp;
        next->size = remaining_size - sizeof(Block_header);
        next->next = curr->next;
        curr->size = size_in_blks * MEM_BLOCK_SIZE;
        if(prev == nullptr) {
            free_mem_head = next;
        }
        else{
            prev->next = next;
        }
        return (uint8*)curr + sizeof(Block_header);
    }

}

int MemoryAllocator::mem_free_internal(void* pointer){

    if (pointer == nullptr) return -1;

    Block_header* curr = free_mem_head, *prev = nullptr;
    Block_header* ptr = (Block_header*)((uint8*)pointer - sizeof(Block_header));
    for(; curr != nullptr; prev = curr, curr = curr->next){
        if(curr > ptr) break;
    }

    if(prev != nullptr){
        prev->next = ptr;
        if((uint8*)prev + sizeof(Block_header) + prev->size == (uint8*)ptr){
            prev->size += ptr->size + sizeof(Block_header);
            ptr = prev;
        }
    }
    else{
        free_mem_head = ptr;
    }
    ptr->next = curr;

    if(curr != nullptr){
        if((uint8*)ptr + sizeof(Block_header) + ptr->size == (uint8*)curr){
            ptr->next = curr->next;
            ptr->size += curr->size + sizeof(Block_header);
        }
    }
    return 0;
}
