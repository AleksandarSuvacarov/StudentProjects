//
// Created by os on 8/5/22.
//
#include "../h/syscall_cpp.hpp"


Thread::Thread (void (*body)(void*), void* arg){
    thread_create_no_start(&myHandle, body, this);
}

Thread::~Thread() {

}

int Thread::start() {
    int ret = 0;
    if(myHandle == nullptr) {
        ret = thread_create(&myHandle, wrapper, this);
    }
    else{
        put_in_scheduler(myHandle);
    }
    return ret;
}

void Thread::dispatch() {
    thread_dispatch();
}

Thread::Thread() {
    //Thread(nullptr, nullptr);
}

void Thread::wrapper(void *arg) {
    ((Thread*)arg)->run();
}


Semaphore::Semaphore(unsigned int init) {
    sem_open(&myHandle, init);
}

Semaphore::~Semaphore() {
    sem_close(myHandle);
}

int Semaphore::wait() {
    return sem_wait(myHandle);
}

int Semaphore::signal() {
    return sem_signal(myHandle);
}

char Console::getc() {
    return ::getc();
}

void Console::putc(char chr) {
    ::putc(chr);
}
