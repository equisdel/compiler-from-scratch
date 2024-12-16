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
J@MAIN DW ?
B@MAIN DW ?
C@MAIN DW ?
I@MAIN DW ?
__new_line__ DB 13, 10, 0 ; CRLF
errorOverflowMul db "ERROR: Overflow detectado! Una multiplicacion de enteros excede el limite de 16 bits", 0
errorOverflowSub db "ERROR: Overflow detectado! Una resta de enteros da negativo", 0
errorRecursiveAttempt db "ERROR: Llamado recursivo detectado! No se permite la recursion directa ni indirecta.", 0
chk_rec BYTE 0
aux_charch_0  DB "ANIDADAS ",  0
aux_charch_3  DB " EN EL REPEAT ",  0
aux_charch_7  DB "THEN ",  0
aux_charch_8  DB "I MENOR A 5 ",  0
aux_charch_10  DB "ELSE ",  0
aux_charch_13  DB "EN REPEAT2 ",  0
auxt_15 DW ?
auxt_19 DW ?
aux_charch_23  DB "FUERA DEL FOR ",  0

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

MOV AX, 1
MOV I@MAIN, AX

labelt_2:

INVOKE printf, addr __new_line__
invoke printf, addr aux_charch_3

INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), I@MAIN

MOV BX, I@MAIN                  ; Mueve op1 a reg. B
CMP BX, 5                 ; Compara op1 con op2. R = op1 - op2. ZF = R == 0 ? 1 : 0. 

JAE labelt_10

INVOKE printf, addr __new_line__
invoke printf, addr aux_charch_7

INVOKE printf, addr __new_line__
invoke printf, addr aux_charch_8

JMP labelt_19

labelt_10:
INVOKE printf, addr __new_line__
invoke printf, addr aux_charch_10

MOV AX, 4
MOV J@MAIN, AX

labelt_12:

INVOKE printf, addr __new_line__
invoke printf, addr aux_charch_13

INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), J@MAIN

MOV AX, J@MAIN
MOV BX, 2
CMP AX, BX
jb OverflowSub
SUB AX, BX
MOV auxt_15, AX

MOV AX, auxt_15
MOV J@MAIN, AX

MOV BX, J@MAIN                  ; Mueve op1 a reg. B
CMP BX, 0                 ; Compara op1 con op2. R = op1 - op2. ZF = R == 0 ? 1 : 0. 

JA labelt_12

labelt_19:
MOV AX, I@MAIN
ADD AX, 1
MOV auxt_19, AX

MOV AX, auxt_19
MOV I@MAIN, AX

MOV BX, I@MAIN                  ; Mueve op1 a reg. B
CMP BX, 10                 ; Compara op1 con op2. R = op1 - op2. ZF = R == 0 ? 1 : 0. 

JBE labelt_2

INVOKE printf, addr __new_line__
invoke printf, addr aux_charch_23

invoke printf, addr __new_line__

end start


