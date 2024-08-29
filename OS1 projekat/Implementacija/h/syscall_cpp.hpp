//
// Created by os on 6/27/22.
//

#ifndef PROJECT_BASE_V1_1_SYSCALL_CPP_HPP
#define PROJECT_BASE_V1_1_SYSCALL_CPP_HPP

#include "syscall_c.h"
#include "../lib/hw.h"


/*void* ::operator new (size_t size){
    return mem_alloc(size);
}
void ::operator delete (void* ptr){
    mem_free(ptr);
}*/


class Thread {
public:
    Thread (void (*body)(void*), void* arg);
    virtual ~Thread ();
    int start ();
    static void dispatch ();
    static int sleep (time_t){return 0;}
protected:
    Thread ();
    virtual void run () {}
private:
    thread_t myHandle = nullptr;
    static void wrapper(void* arg);
};


class Semaphore {
public:
    Semaphore (unsigned init = 1);
    virtual ~Semaphore ();
    int wait ();
    int signal ();
private:
    sem_t myHandle;
};

class Console {
public:
    static char getc ();
    static void putc (char);
};



#endif //PROJECT_BASE_V1_1_SYSCALL_CPP_HPP
