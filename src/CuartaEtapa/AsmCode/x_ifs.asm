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
auxt_5 DW ?
auxt_6 DW ?
auxt_10 DW ?
auxt_18 DW ?
aux_charch_23  DB "AUX FINAL@ ",  0

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
MOV AX ,INTVAL@MAIN@TESTEVERYVERYLA
MOV CX ,4
MUL CX
CMP DX, 0
JNE OverflowMul
MOV auxt_5 ,AX
MOV AX, auxt_5
MOV CX, 2
DIV CX
MOV auxt_6 ,AX
MOV AX, auxt_6
MOV @TESTEVERYVERYLA@MAIN, AX
ret
MOV AX ,INTVAL@MAIN@TESTEVERYVERYLA
MOV CX ,INTVAL@MAIN@TESTEVERYVERYLA
MUL CX
CMP DX, 0
JNE OverflowMul
MOV auxt_10 ,AX
MOV AX, auxt_10
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
invoke TESTEVERYVERYLA@MAIN, 15
MOV AX, @TESTEVERYVERYLA@MAIN
MOV AUX@MAIN, AX
SALTITO@@MAIN:
INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), AUX@MAIN
MOV AX, AUX@MAIN
ADD AX, 1
MOV auxt_18, AX
MOV AX, auxt_18
MOV AUX@MAIN, AX
INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), AUX@MAIN
INVOKE printf, addr __new_line__
invoke printf, addr aux_charch_23
INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), AUX@MAIN

end start


