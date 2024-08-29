//
// Created by os on 6/21/22.
//

#include "../h/ccb.hpp"

extern "C" void pushRegisters();
extern "C" void popRegisters();

CCB* CCB::running = nullptr;

void CCB::yield() {
    pushRegisters();
    CCB::dispatch();
    popRegisters();
}

void CCB::dispatch() {

    CCB* old = running;
    if(!old->is_finished()) {Scheduler::put(old);}
    running = Scheduler::get();
    if(running != nullptr){
        CCB::contextSwitch(&old->context, &running->context);
    }
    else{
        if(!old->is_finished()) {running = old;}
    }

}

CCB *CCB::createCoroutine(CCB::Body boddy, void* argument, void* stack_space) {
    CCB* ptr = new CCB(boddy, argument, stack_space);
    if(running == nullptr) running = ptr;
    return ptr;
}

CCB* CCB::createCoroutine_no_start(CCB::Body boddy, void* argument, void* stack_space) {
    CCB* ptr = new CCB(argument, boddy, stack_space);
    if(running == nullptr) running = ptr;
    return ptr;
}

void CCB::switch_regime(){
    long status;
    asm volatile("csrr %0, sstatus" : "=r" (status));
    if(status == 0x100 || status == 0x0 || status != 0x666){
        //status &= ~0x100;
        //asm volatile("csrw sstatus, %0" : :"r" (status));
        long ra;
        asm volatile("mv %0, ra" : "=r" (ra));
        asm volatile("csrw sepc, %0" : :"r" (ra));
        long sie;
        asm volatile("csrr %0, sie" : "=r" (sie));
        sie |= 0x200;
        asm volatile("csrw sie, %0" : : "r" (sie));
        asm volatile("sret");
    }
}

void CCB::body_wrapper() {
    long sepc;
    asm volatile("csrr %0, sepc" : "=r" (sepc));
    switch_regime();
    running->body(running->arg);
    running->set_finished(true);
    asm volatile("mv a0, %0" : : "r" (0x70));
    asm volatile("ecall");
    asm volatile("csrw sepc, %0" : :"r" (sepc));
    //asm volatile("csrw sstatus, %0" : :"r" (0x100));
    CCB::yield();
}

int CCB::exit() {
    running->set_finished(true);
    kernel::mem_free(running->stack);
    CCB::yield();
    return -1;
}