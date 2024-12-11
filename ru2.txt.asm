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
SING2@MAIN REAL4 ?
AUX@MAIN DW ?
SING1@MAIN REAL4 ?
__new_line__ DB 13, 10, 0 ; CRLF
errorOverflowMul db "ERROR: Overflow detectado! Una multiplicacion de enteros excede el limite de 16 bits", 0
errorOverflowSub db "ERROR: Overflow detectado! Una resta de enteros da negativo", 0
errorRecursiveAttempt db "ERROR: Llamado recursivo detectado! No se permite la recursion directa ni indirecta.", 0
chk_rec BYTE 0
aux_float_0 DD 1.2e+2
aux_float_1 DD 1.2e-2
aux_charch_6  DB "VALOR ACTUAL -> ",  0
aux_charch_10  DB "AUX MENOR A 10 ",  0
aux_charch_13  DB "AUX MAYOR A 5 ",  0
aux_charch_16  DB "AUX IGUAL A 9 ",  0
auxt_17 DW ?

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


fld aux_float_0
fstp SING1@MAIN

fld aux_float_1
fstp SING2@MAIN

MOV AX, 0
MOV AUX@MAIN, AX

SALTITO@@MAIN:

INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), AUX@MAIN

labelt_5:

INVOKE printf, addr __new_line__
invoke printf, addr aux_charch_6

INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), AUX@MAIN

MOV BX, AUX@MAIN                  ; Mueve op1 a reg. B
CMP BX, 10                 ; Compara op1 con op2. R = op1 - op2. ZF = R == 0 ? 1 : 0. 

JAE labelt_11

INVOKE printf, addr __new_line__
invoke printf, addr aux_charch_10

labelt_11:
MOV BX, AUX@MAIN                  ; Mueve op1 a reg. B
CMP BX, 5                 ; Compara op1 con op2. R = op1 - op2. ZF = R == 0 ? 1 : 0. 

JBE labelt_14

INVOKE printf, addr __new_line__
invoke printf, addr aux_charch_13

labelt_14:
MOV BX, AUX@MAIN                  ; Mueve op1 a reg. B
CMP BX, 9                 ; Compara op1 con op2. R = op1 - op2. ZF = R == 0 ? 1 : 0. 

JNE labelt_17

INVOKE printf, addr __new_line__
invoke printf, addr aux_charch_16

labelt_17:
MOV AX, AUX@MAIN
ADD AX, 1
MOV auxt_17, AX

MOV AX, auxt_17
MOV AUX@MAIN, AX

MOV BX, AUX@MAIN                  ; Mueve op1 a reg. B
CMP BX, 10                 ; Compara op1 con op2. R = op1 - op2. ZF = R == 0 ? 1 : 0. 

JBE labelt_5

invoke printf, addr __new_line__

end start


