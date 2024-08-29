//
// Created by os on 6/21/22.
//

#ifndef PROJECT_BASE_V1_1_CCB_HPP
#define PROJECT_BASE_V1_1_CCB_HPP

#include "scheduler.hpp"
#include "kernel.hpp"
//#include "sem.hpp"

class CCB {
public:
    ~CCB(){ delete[] stack;}

    using Body = void(*)(void*);
    static CCB* createCoroutine(Body body, void* arg, void* stack_space);
    static CCB* createCoroutine_no_start(Body body, void* arg, void* stack_space);

    void set_finished(bool f){CCB::finished = f;}
    bool is_finished() const{return finished;}

    static uint64 constexpr STACK_SIZE = 4096;

    void* operator new(uint64 n){
        return kernel::mem_alloc(n);
    }

    void operator delete(void *p) {
        kernel::mem_free(p);
    }

    static void yield();

    static CCB* running;

private:

    CCB(Body body, void* argument, void* stack_space):
            body(body),
            stack(body != 0 ? (uint64*)stack_space : nullptr),
            arg(argument),
            context({body != nullptr ? (uint64)body_wrapper : 0, body != nullptr ? (uint64) &stack[STACK_SIZE] : 0}),
            finished(false)
    {
        if (body != nullptr) Scheduler::put(this);
    }

    CCB(void* argument, Body body, void* stack_space):
            body(body),
            stack(body != 0 ? (uint64*)stack_space : nullptr),
            arg(argument),
            context({body != nullptr ? (uint64)body_wrapper : 0, body != nullptr ? (uint64) &stack[STACK_SIZE] : 0}),
            finished(false)
    {}

    struct Context{
        uint64 ra;
        uint64 sp;
    };

    Body body;
    uint64 *stack;
    void* arg;
    Context context;
    bool finished;


    static void body_wrapper();

    static void dispatch();
    static void contextSwitch(Context* oldContext, Context* runningContext);

    static void switch_regime();

    static int exit();

    friend int kernel::thread_create (
            thread_t** handle,
            void(*start_routine)(void*),
            void* arg,
            void* stack_space
    );

    friend int kernel::thread_create_no_start (
            thread_t** handle,
            void(*start_routine)(void*),
            void* arg,
            void* stack_space
    );

    friend void kernel::thread_dispatch ();

    friend int kernel::thread_exit();

    friend class Sem;

};


#endif //PROJECT_BASE_V1_1_CCB_HPP
