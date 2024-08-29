//
// Created by os on 6/27/22.
//

#include "../h/sem.hpp"

//List<Sem> Sem::semaphores;
Array_of_sem Sem::semaphores;

extern "C" void pushRegisters();
extern "C" void popRegisters();

void Sem::block() {
    CCB* old = CCB::running;
    CCB::running = Scheduler::get();
    blocked.add_last(old);
    pushRegisters();
    CCB::contextSwitch(&old->context, &CCB::running->context);
    popRegisters();
}

void Sem::unblock() {
    CCB* next = blocked.remove_first();
    Scheduler::put(next);
}

void Sem::wait() {
    long sie, old_sie;
    asm volatile("csrr %0, sie" : "=r" (old_sie));
    sie = old_sie & ~0x200;
    asm volatile("csrw sie, %0" : : "r" (sie));

    if(--val < 0) block();

    asm volatile("csrw sie, %0" : : "r" (old_sie));
}

void Sem::signal() {
    long sie, old_sie;
    asm volatile("csrr %0, sie" : "=r" (old_sie));
    sie = old_sie & ~0x200;
    asm volatile("csrw sie, %0" : : "r" (sie));

    if(++val <= 0) unblock();

    asm volatile("csrw sie, %0" : : "r" (old_sie));
}

Sem* Sem::createSem(unsigned int init) {
    Sem* ptr = new Sem(init);
    return ptr;
}

int Sem::destroySem(Sem *ptr) {
    while(true){
        CCB* next = ptr->blocked.remove_first();
        if(next == nullptr) break;
        Scheduler::put(next);
    }
    Sem* ret = semaphores.remove_elem(ptr);
    if(ret == nullptr) return -1;
    delete ptr;
    return 0;
}
