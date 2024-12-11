.386                  ; Enable 32-bit instructions
.MODEL FLAT,STDCALL           ; Use FLAT memory model for 32-bit systems
.STACK 100h           ; Define stack size;
option casemap :none
include\masm32\include\masm32rt.inc
includelib\masm32\lib\kernel32.lib
includelib\masm32\lib\masm32.lib
dll_dllcrt0 PROTO C
printf PROTO C : VARARG

.data
__new_line__ DB 13, 10, 0 ; CRLF
errorOverflowMul db "ERROR: Overflow detectado! Una multiplicacion de enteros excede el limite de 16 bits", 0
errorOverflowSub db "ERROR: Overflow detectado! Una resta de enteros da negativo", 0
errorRecursiveAttempt db "ERROR: Llamado recursivo detectado! No se permite la recursion directa ni indirecta.", 0
chk_rec BYTE 0
auxt_1 SINGLE ?
aux_float_2 REAL4 0X0
aux_float_2 REAL4 S@MAIN@S_FUN
auxt_3 SINGLE ?
auxt_4 REAL4 ?
aux_float_4 REAL4 2
aux_float_4 REAL4 S@MAIN@S_FUN
aux_float_5 REAL4 SS@MAIN@S_FUN
auxt_7 DW ?
auxt_9 REAL4 ?
aux_float_9 REAL4 S@MAIN@S_FUN
aux_float_9 REAL4 SS@MAIN@S_FUN
aux_float_10 REAL4 SS@MAIN@S_FUN
auxt_11 DW ?

.code
OverflowMul:
invoke StdOut, addr errorOverflowMul
invoke ExitProcess, 1   ; tiene que terminar con la ejecucion
OverflowSub:
invoke StdOut, addr errorOverflowSub
invoke ExitProcess, 1   ; tiene que terminar con la ejecucion
RecursiveAttempt:
invoke StdOut, addr errorRecursiveAttempt
invoke ExitProcess, 1   ; tiene que terminar con la ejecucion

S_FUN@MAIN PROC UUU@MAIN@S_FUN:WORD

fild 0X0
fstp auxt_1

fld aux_float_2
fstp aux_float_2

fild 2
fstp auxt_3

fld aux_float_4
fmul aux_float_4
fstp auxt_4

fld auxt_4
fstp aux_float_5

MOV AX, 0
MOV U@MAIN@S_FUN, AX

MOV AX ,2
MOV CX ,U@MAIN@S_FUN
MUL CX
CMP DX, 0
JNE OverflowMul
MOV auxt_7 ,AX

MOV AX, auxt_7
MOV UU@MAIN@S_FUN, AX

fld aux_float_9
fadd aux_float_9
fstp auxt_9

fld auxt_9
fstp aux_float_10

MOV AX, U@MAIN@S_FUN
ADD AX, UU@MAIN@S_FUN
MOV auxt_11, AX

MOV AX, auxt_11
MOV UU@MAIN@S_FUN, AX

MOV AX, UU@MAIN@S_FUN
MOV @S_FUN@MAIN, AX

ret

ret
S_FUN@MAIN ENDP

start:



MOV chk_rec, 0
CMP chk_rec, 0
JNZ RecursiveAttempt
invoke S_FUN@MAIN, U@MAIN

MOV AX, @S_FUN@MAIN
MOV U@MAIN, AX

end start


