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
U1@MAIN DW ?
SING4@MAIN REAL4 ?
U4@MAIN DW ?
SING3@MAIN REAL4 ?
U3@MAIN DW ?
SING1@MAIN REAL4 ?
U2@MAIN DW ?
__new_line__ DB 13, 10, 0 ; CRLF
errorOverflowMul db "ERROR: Overflow detectado! Una multiplicacion de enteros excede el limite de 16 bits", 0
errorOverflowSub db "ERROR: Overflow detectado! Una resta de enteros da negativo", 0
errorRecursiveAttempt db "ERROR: Llamado recursivo detectado! No se permite la recursion directa ni indirecta.", 0
chk_rec BYTE 0
auxt_3 DW ?
aux_float_11 REAL4 1.17549435S-38
aux_float_12 REAL4 3.40282347S+38
aux_float_13 REAL4 -3.40282347S+38
aux_float_14 REAL4 -1.17549435S-38
aux_float_15 REAL4 1.17549435S-39
aux_float_16 REAL4 3.40282347S+39
aux_float_17 REAL4 -3.40282347S+39
aux_float_18 REAL4 -1.17549435S-39

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


MOV AX, 0
MOV U1@MAIN, AX

MOV AX, 65535
MOV U2@MAIN, AX

MOV AX, 65535
MOV U3@MAIN, AX

MOV AX, U2@MAIN
ADD AX, 1
MOV auxt_3, AX

MOV AX, auxt_3
MOV U4@MAIN, AX


MOV AX, 0X0
MOV U1@MAIN, AX


MOV AX, 0XFFFF
MOV U2@MAIN, AX


MOV AX, 0X0
MOV U3@MAIN, AX

fld aux_float_11
fstp SING1@MAIN

fld aux_float_12
fstp SING2@MAIN

fld aux_float_13
fstp SING3@MAIN

fld aux_float_14
fstp SING4@MAIN

fld aux_float_15
fstp SING1@MAIN

fld aux_float_16
fstp SING2@MAIN

fld aux_float_17
fstp SING3@MAIN

fld aux_float_18
fstp SING4@MAIN

end start


