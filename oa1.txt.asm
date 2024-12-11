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
A@MAIN DW ?
B@MAIN DW ?
C@MAIN DW ?
I@MAIN DW ?
__new_line__ DB 13, 10, 0 ; CRLF
errorOverflowMul db "ERROR: Overflow detectado! Una multiplicacion de enteros excede el limite de 16 bits", 0
errorOverflowSub db "ERROR: Overflow detectado! Una resta de enteros da negativo", 0
errorRecursiveAttempt db "ERROR: Llamado recursivo detectado! No se permite la recursion directa ni indirecta.", 0
chk_rec BYTE 0
aux_charch_0  DB "PRUEBA DE OPERACIONES ARITMETICAS ",  0
aux_charch_1  DB "------------------------------------------------- ",  0
auxt_5 DW ?
auxt_6 DW ?
auxt_7 DW ?
auxt_8 DW ?
auxt_9 DW ?
aux_charch_14  DB "DIO BIEN ",  0
aux_charch_16  DB "DIO MAL ",  0

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


INVOKE printf, addr __new_line__
invoke printf, addr aux_charch_0

INVOKE printf, addr __new_line__
invoke printf, addr aux_charch_1

MOV AX, 1
MOV A@MAIN, AX

MOV AX, 2
MOV B@MAIN, AX

MOV AX, 3
MOV C@MAIN, AX

MOV AX ,C@MAIN
MOV CX ,2
MUL CX
CMP DX, 0
JNE OverflowMul
MOV auxt_5 ,AX

MOV AX, B@MAIN
ADD AX, auxt_5
MOV auxt_6, AX

MOV CX,0
MOV AX, B@MAIN
MOV CX, 2
DIV CX
MOV auxt_7 ,AX

MOV AX, auxt_6
MOV BX, auxt_7
CMP AX, BX
jb OverflowSub
SUB AX, BX
MOV auxt_8, AX

MOV AX, auxt_8
ADD AX, 4
MOV auxt_9, AX

MOV AX, auxt_9
MOV A@MAIN, AX

INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), A@MAIN

MOV BX, A@MAIN                  ; Mueve op1 a reg. B
CMP BX, 11                 ; Compara op1 con op2. R = op1 - op2. ZF = R == 0 ? 1 : 0. 

JNE labelt_16

INVOKE printf, addr __new_line__
invoke printf, addr aux_charch_14

JMP labelt_17

labelt_16:
INVOKE printf, addr __new_line__
invoke printf, addr aux_charch_16

labelt_17:
invoke printf, addr __new_line__

end start


