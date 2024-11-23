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
X@MAIN DW ?
__new_line__ DB 13, 10, 0 ; CRLF
errorOverflowMul db "ERROR: Overflow detectado! Una multiplicacion de enteros excede el limite de 16 bits", 0
errorOverflowSub db "ERROR: Overflow detectado! Una resta de enteros da negativo", 0
errorRecursiveAttempt db "ERROR: Llamado recursivo detectado! No se permite la recursion directa ni indirecta.", 0
chk_rec BYTE 0
auxt_1 BYTE ?

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

start:


; Terceto <0>  operacion=':=', op1='X:MAIN', op2='1', subtipo='UINTEGER'
MOV AX, 1
MOV X@MAIN, AX

; Terceto <1>  operacion='!=', op1='X:MAIN', op2='1', subtipo='UINTEGER'
MOV BX, op1                 ; Mueve op1 a reg. B
CMP BX, op2                 ; Compara op1 con op2. R = op1 - op2. ZF = R == 0 ? 1 : 0. 
SETZ auxt_1                ; Guarda el valor de ZF en la var. auxiliar del terceto.
XOR auxt_1, 1              ; Invierto el bit: situacion deseable -> Z == 0.

; Terceto <2>  operacion='BF', op1='<1>', op2='<5>', subtipo=''
CMP auxt_1, 0     ; ZF = auxt_1 == 0 ? 1 : 0
JNZ labelt_5

; Terceto <3>  operacion=':=', op1='X:MAIN', op2='0', subtipo='UINTEGER'
MOV AX, 0
MOV X@MAIN, AX

; Terceto <4>  operacion='BI', op1='<6>', op2='null', subtipo=''
JMP labelt_6

; Terceto <5>  operacion=':=', op1='X:MAIN', op2='1', subtipo='UINTEGER'
labelt_5:
MOV AX, 1
MOV X@MAIN, AX

; Terceto <6>  operacion='OUTF', op1='X:MAIN', op2='null', subtipo='UINTEGER'
labelt_6:
INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), X@MAIN

end start


