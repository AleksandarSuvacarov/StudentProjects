//
// Created by os on 8/9/22.
//

#ifndef PROJECT_BASE_V1_1_ARRAY_OF_CCB_HPP
#define PROJECT_BASE_V1_1_ARRAY_OF_CCB_HPP


#define ARRAY_SIZE 100

class CCB;

class Array_of_ccb{

    CCB* array[ARRAY_SIZE];

    int end = 0;

public:
    Array_of_ccb(){
        for(int i = 0; i < ARRAY_SIZE; i++){
            array[i] = nullptr;
        }
    }

    void add_last(CCB*);
    CCB* remove_first();

};




#endif //PROJECT_BASE_V1_1_ARRAY_OF_CCB_HPP
