//
// Created by os on 6/24/22.
//
#include "../lib/hw.h"
#include "../lib/console.h"
#include "../h/kernel.hpp"

extern "C" void processing();

void processing(){

    uint64 a[7];
    asm volatile("sd a0, %0" : "=m" (a[0]));
    asm volatile("sd a1, %0" : "=m" (a[1]));
    asm volatile("sd a2, %0" : "=m" (a[2]));
    asm volatile("sd a3, %0" : "=m" (a[3]));
    asm volatile("sd a4, %0" : "=m" (a[4]));
    asm volatile("sd a5, %0" : "=m" (a[5]));
    asm volatile("sd a6, %0" : "=m" (a[6]));

    uint64* A = a;


    uint64 id_status;
    asm volatile("csrr %0, scause" : "=r" (id_status));

    if(id_status == 8 || id_status == 9){
        uint64 pc;
        asm volatile("csrr %0, sepc" : "=r" (pc));
        pc += 4;
        asm volatile("csrw sepc, %0" : : "r" (pc));
        uint64 id;
        asm volatile("mv %0, a0" : "=r" (id));
        switch(A[0]){
            case 0x01:{
                int sz;
                asm volatile("mv %0, a1" : "=r" (sz));
                kernel::mem_alloc(sz * MEM_BLOCK_SIZE);
                break;
                }
            case 0x02: {
                void* ptr;
                asm volatile("mv %0, a1" : "=r" (ptr));
                kernel::mem_free(ptr);
                break;
            }
            case 0x11: {
                CCB **thread = (CCB**)a[1];
                using foo = void (*)(void *);
                foo start_routine = (void (*)(void *))a[2];
                void *arg = (void*)a[3];
                void *stack_space = (void*)a[4];
                kernel::thread_create(&thread, start_routine, arg, stack_space);
                break;
            }
            case 0x12:{
                kernel::thread_exit();
                break;
            }
            case 0x13:{
                kernel::thread_dispatch();
                break;
            }
            case 0x14: {
                CCB ***thread = (CCB***)a[1];
                using foo = void (*)(void *);
                foo start_routine = (void (*)(void *))a[2];
                void *arg = (void*)a[3];
                void *stack_space = (void*)a[4];
                kernel::thread_create_no_start(thread, start_routine, arg, stack_space);
                break;
            }
            case 0x15:{
                CCB *handle = (CCB*)a[1];
                kernel::put_in_scheduler(handle);
                break;
            }
            case 0x21: {
                Sem** handle = (Sem**)a[1];
                unsigned init = (unsigned)a[2];
                kernel::sem_open(handle, init);
                break;
            }
            case 0x22: {
                Sem* handle = (Sem*)a[1];
                kernel::sem_close(handle);
                break;
            }
            case 0x23: {
                Sem* handle = (Sem*)a[1];
                kernel::sem_wait(handle);
                break;
            }
            case 0x24: {
                Sem* handle = (Sem*)a[1];
                kernel::sem_signal(handle);
                break;
            }
            case 0x41: {
                uint64 pc;
                asm volatile("csrr %0, sepc" : "=r" (pc));
                __getc();
                asm volatile("csrw sepc, %0" : : "r" (pc));
                break;
            }
            case 0x42: {
                char chr = (char)a[1];
                uint64 pc;
                asm volatile("csrr %0, sepc" : "=r" (pc));
                __putc(chr);
                asm volatile("csrw sepc, %0" : : "r" (pc));
                break;
            }
            case 0x70: {
                uint64 pc;
                asm volatile("csrr %0, sepc" : "=r" (pc));
                asm volatile("ld ra, %0" : :"m" (pc));
                asm volatile("ret");
            }
        }
        //uint64 tpis;
        //asm volatile("csrr %0, sip" : "=r" (tpis));
        //tpis &= ~0x2;
        //asm volatile("csrw sip, %0" : : "r" (tpis));

    }
    else if(id_status == 0x8000000000000009){
        uint64 pis;
        asm volatile("csrr %0, sip" : "=r" (pis));
        pis &= ~0x200;
        asm volatile("csrw sip, %0" : : "r" (pis));
        console_handler();
    }
    else if(id_status == 0x8000000000000001){
        uint64 tpis;
        asm volatile("csrr %0, sip" : "=r" (tpis));
        tpis &= ~0x2;
        asm volatile("csrw sip, %0" : : "r" (tpis));
    }

}