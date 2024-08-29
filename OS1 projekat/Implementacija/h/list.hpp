//
// Created by os on 6/21/22.
//

#ifndef PROJECT_BASE_V1_1_LIST_HPP
#define PROJECT_BASE_V1_1_LIST_HPP

#include "kernel.hpp"


template<typename T>
class List{
private:
    struct Elem{
        T *data;
        Elem *next;
        Elem(T *data, Elem* next) : data(data), next(next){}

        void* operator new(uint64 n){
            return kernel::mem_alloc(n);
        }

        void operator delete(void *p) {
            kernel::mem_free(p);
        }
    };

    Elem *head, *tail;


public:
    List(): head(0), tail(0){}
    List(const List<T>&) = delete;
    List<T>& operator=(const List<T>&) = delete;

    void* operator new(uint64 n){
        return kernel::mem_alloc(n);
    }

    void operator delete(void *p) {
        kernel::mem_free(p);
    }


    void add_first(T *data){
        Elem* elem = new Elem(data, head);
        head = elem;
        if(!tail) {tail = head;}
    }

    void add_last(T *data){
        Elem *elem = new Elem(data, 0);
        if(tail){
            tail->next = elem;
            tail = elem;
        }
        else{
            head = tail = elem;
        }
    }

    T* remove_elem(T* elem){
        Elem* curr, *prev;
        for(curr = head, prev = nullptr; curr != nullptr && curr->data != elem; prev = curr, curr = curr->next);
        if(curr == nullptr) return nullptr;
        if(prev == nullptr){
            head = curr->next;
        }
        else{
            prev->next = curr->next;
        }
        if(curr == tail) tail = prev;
        T* ret = curr->data;
        delete curr;
        return ret;
    }

    T* remove_first(){
        if(!head) {return 0;}

        Elem* elem = head;
        head = head->next;
        if(!head){tail = 0;}

        T* ret = elem->data;
        delete elem;
        return ret;
    };

    T* peek_first(){
        if(!head){return 0;}
        return head->data;
    }

    T* remove_last(){
        if(!head) {return 0;}

        Elem *prev = 0;
        for(Elem* curr = head; curr && curr != tail; curr = curr->next){
            prev = curr;
        }

        Elem* elem = tail;
        if(prev) { prev->next = 0; }
        else { head = 0; }
        tail = prev;

        T* ret = elem->data;
        delete elem;
        return ret;
    }

    T* peek_last(){
        if(!tail) {return 0;}
        return tail->data;
    }

    friend int kernel::sem_wait(sem_t);
    friend int kernel::sem_signal(sem_t);
    friend int kernel::sem_close(sem_t);

};




#endif //PROJECT_BASE_V1_1_LIST_HPP
