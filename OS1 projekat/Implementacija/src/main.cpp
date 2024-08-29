#include "../h/syscall_cpp.hpp"
#include "../test/printing.hpp"

extern "C" void interrupt();
void userMain();

long find_pc(){
    long pc = 0;
    asm volatile("mv %0, ra" : "=r" (pc));
    return pc;
}

void main(){
    uint64 ra, sp;
    asm volatile("mv %0, ra" : "=r" (ra));
    asm volatile("mv %0, sp" : "=r" (sp));
    asm volatile("csrw stvec, %0" : : "r" (&interrupt));
    uint64 sepc = find_pc();
    sepc += 12;
    asm volatile("csrw sepc, %0" : : "r" (sepc));
    asm volatile("sret");


/*    int* a = (int*)mem_alloc(sizeof(int));
    *a = 97;
    putc(*a);*/


    thread_t t;
    thread_create(&t, nullptr, nullptr);
    printString("Thread created main\n");

    userMain();

    printString("Kraj!!!\n");

    asm volatile("mv a0, %0" : : "r" (0x70));
    asm volatile("ecall");
    asm volatile("mv ra, %0" : : "r" (ra));
    asm volatile("mv sp, %0" : : "r" (sp));

}

