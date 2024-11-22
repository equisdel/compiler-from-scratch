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
@TESTEVERYVERYLA@MAIN DW ?
SING1@MAIN REAL4 ?
__new_line__ DB 13, 10, 0 ; CRLF
errorOverflowMul db "ERROR: Overflow detectado! Una multiplicacion de enteros excede el limite de 16 bits", 0
errorOverflowSub db "ERROR: Overflow detectado! Una resta de enteros da negativo", 0
errorRecursiveAttempt db "ERROR: Llamado recursivo detectado! No se permite la recursion directa ni indirecta.", 0
chk_rec BYTE 0
aux_float_0 REAL4 1.2S+2
aux_float_1 REAL4 1.2S-2
auxt_7 DW ?
auxt_15 DW ?

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

TESTEVERYVERYLA@MAIN PROC INTVAL@MAIN@TESTEVERYVERYLA:WORD
;JMP SALTITO@@MAIN
MOV AX ,INTVAL@MAIN@TESTEVERYVERYLA
MUL INTVAL@MAIN@TESTEVERYVERYLA
CMP DX, 0
JNE OverflowMul
MOV auxt_7 ,AX
MOV AX, auxt_7
MOV @TESTEVERYVERYLA@MAIN, AX
ret
ret
TESTEVERYVERYLA@MAIN ENDP

start:

fld aux_float_0
fstp SING1@MAIN
fld aux_float_1
fstp SING2@MAIN
MOV chk_rec, 0
CMP chk_rec, 0
JNZ RecursiveAttempt
invoke TESTEVERYVERYLA@MAIN, 5
MOV AX, @TESTEVERYVERYLA@MAIN
MOV AUX@MAIN, AX
SALTITO@@MAIN:
INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), AUX@MAIN
MOV AX, AUX@MAIN
ADD AX, 1
MOV auxt_15, AX
MOV AX, auxt_15
MOV AUX@MAIN, AX

end start


