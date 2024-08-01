@echo off
echo generating ... T%1I%2N%3KD%4K.data
gen lit -tlen %1 -patlen %2 -nitems %3 -ntrans %4 -fname T%1I%2N%3KD%4K
echo T%1I%2N%3KD%4K.data ..... complete !