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
P_R@MAIN REAL4 ?
U1@MAIN@F DW ?
@F@MAIN DD ?
U3@MAIN@F DW ?
U2@MAIN@F DW ?
__new_line__ DB 13, 10, 0 ; CRLF
errorOverflowMul db "ERROR: Overflow detectado! Una multiplicacion de enteros excede el limite de 16 bits", 0
errorOverflowSub db "ERROR: Overflow detectado! Una resta de enteros da negativo", 0
errorRecursiveAttempt db "ERROR: Llamado recursivo detectado! No se permite la recursion directa ni indirecta.", 0
chk_rec BYTE 0
auxt_3 DW ?
auxt_6 DW ?
auxt_8 SINGLE ?
aux_float_10 REAL4 P_F
aux_float_13 REAL4 P_R

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

F@MAIN PROC P_F@MAIN@F:WORD
MOV AX, 1
MOV U1@MAIN@F, AX
MOV AX, 2
MOV U2@MAIN@F, AX
MOV AX, U1@MAIN@F
ADD AX, U2@MAIN@F
MOV auxt_3, AX
MOV AX, auxt_3
MOV U3@MAIN@F, AX
INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), U3@MAIN@F
MOV AX, U1@MAIN@F
ADD AX, U2@MAIN@F
MOV auxt_6, AX
INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), auxt_6
fild U3@MAIN@F
fstp auxt_8
fld U3@MAIN@F
fstp P_F@MAIN@F
fld aux_float_10
fstp @F@MAIN
ret
ret
F@MAIN ENDP

start:

MOV chk_rec, 0
CMP chk_rec, 0
JNZ RecursiveAttempt
invoke F@MAIN, aux_float_13
fld @F@MAIN
fstp P_R@MAIN

end start


